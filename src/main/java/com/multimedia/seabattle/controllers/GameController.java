package com.multimedia.seabattle.controllers;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.multimedia.seabattle.config.ITemplateConfig;
import com.multimedia.seabattle.model.beans.Coordinates;
import com.multimedia.seabattle.model.beans.Game;
import com.multimedia.seabattle.model.beans.Message;
import com.multimedia.seabattle.model.beans.Round;
import com.multimedia.seabattle.model.beans.ShipInfo;
import com.multimedia.seabattle.model.beans.Ticket;
import com.multimedia.seabattle.model.beans.TurnResult;
import com.multimedia.seabattle.model.beans.User;
import com.multimedia.seabattle.model.types.PlayerReadyType;
import com.multimedia.seabattle.model.types.ShipCreationResult;
import com.multimedia.seabattle.model.types.ShipType;
import com.multimedia.seabattle.service.game.IGameService;
import com.multimedia.seabattle.service.ships.RandomShipGenerator;
import com.multimedia.seabattle.service.ticket.ITicketListener;
import com.multimedia.seabattle.service.ticket.ITicketService;
import com.multimedia.seabattle.service.user.IUserService;
import common.utils.CommonAttributes;

@Controller
@RequestMapping(value="/game")
public class GameController implements ITicketListener, MessageSourceAware{
	private static final Logger logger = LoggerFactory.getLogger(GameController.class);

	protected ITemplateConfig config;
	private ITicketService ticketService;
	private IGameService gameService;

	private IUserService userService;

	private MessageSource messageSource;

	private final String lobby_url = "/WEB-INF/views/game/player_lobby.jsp";
	private final String ready_url = "/WEB-INF/views/game/player_ready.jsp";
	private final String wait_url = "/WEB-INF/views/game/player_wait.jsp";
	private final String game_url = "/WEB-INF/views/game/player_game.jsp";

	/**
	 * try to start a game, if no users waiting, create new ticket and wait for a game
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/player.htm")
	public String index(Map<String, Object> model){
		User user = (User) model.get("user");
		Game game = (Game) model.get("game");
		if (game==null){
			Ticket t = ticketService.getTicket(user);
			if (t==null){
				waitPlayerSign(model);
			} else {
				startingGame(model);
			}
		} else {
			Boolean player1 = getPlayer(game, user);
			if (player1==null)
				return null;
			if (game.getReady1()&&game.getReady2()) {
				playGame(model);
			} else if (
				    	(player1&&game.getReady1()) ||
					    ((!player1)&&game.getReady2())
					)
			{
				waitPlayerPlaceShips(model);
			} else {
				startingGame(model);
			}
		}
		return config.getTemplateUrl();
	}

	/**
	 * player says that he/she is ready
	 */
	@RequestMapping(value="/player.htm", params="do=ready")
	public String playerReady(Map<String, Object> model) {
		User user = (User) model.get("user");
		Game game = (Game) model.get("game");
		if (game==null){
			return null;
		} else {
			Boolean player1 = getPlayer(game, user);
			if (player1==null)
				return null;
			PlayerReadyType ready = gameService.playerReady(game, player1);
			if (ready!=PlayerReadyType.READY) {
				CommonAttributes.addErrorMessage(ready.toString(), model);
				startingGame(model);
			} else if (
			    	(player1&&game.getReady2()) ||
				    ((!player1)&&game.getReady1())
					)
			{
				playGame(model);
		    } else {
				waitPlayerPlaceShips(model);
			}
		}
		return config.getTemplateUrl();
	}

	/**
	 * player says that he/she is ready
	 */
	@RequestMapping(value="/player.htm", params="do=auto")
	public String auto(Map<String, Object> model) {
		User user = (User) model.get("user");
		Game game = (Game) model.get("game");
		if (game==null){
			return null;
		} else {
			Boolean player1 = getPlayer(game, user);
			if (player1==null)
				return null;
			Boolean generated = gameService.generatePlayerShips(game, player1, new RandomShipGenerator());
			if (generated) {
				CommonAttributes.addHelpMessage("operation_succeed", model);
			} else {
				CommonAttributes.addErrorMessage("operation_fail", model);
			}
		}
		return index(model);
	}

	/** show page, while waiting for other player placing his ships */
	private void waitPlayerPlaceShips(Map<String, Object> model) {
		model.put(config.getContentUrlAttribute(), wait_url);
	}
	/** show a page with progress, while searching for other player signs for a game */
	private void waitPlayerSign(Map<String, Object> model) {
		model.put(config.getContentUrlAttribute(), lobby_url);
	}
	/** show a page with battlefield for placing ships */
	private void startingGame(Map<String, Object> model) {
		model.put(config.getContentUrlAttribute(), ready_url);
	}
	/** show a page with 2 battlefield's for placing ships */
	private void playGame(Map<String, Object> model) {
		model.put(config.getContentUrlAttribute(), game_url);
	}

	/**
	 * @return all ship types that may be used for the game
	 */
	@RequestMapping(value="/ships.htm")
	public @ResponseBody Set<ShipInfo> getShipTypes(Map<String, Object> model, Locale locale){
		Game game = (Game) model.get("game");
		User user = (User) model.get("user");
		if (game==null){
			if (logger.isDebugEnabled()){
				logger.debug("requested ships for non existing game");
			}
			return null;
		} else {
			if (logger.isDebugEnabled()){
				logger.debug("getting ships for game #"+game.getId());
			}
			Boolean player1 = getPlayer(game, user);
			if (player1==null)
				return null;
			Set<ShipInfo> rez = gameService.getAvailableShips(game, player1);
			for (ShipInfo info:rez){
				info.setName(messageSource.getMessage(info.getType(), null, locale));
			}
			return rez;
		}
	}

	/**
	 * try to deploy a ship in a given position
	 * @return result code
	 */
	@RequestMapping(value="/deployShip.htm")
	public @ResponseBody Message deployShip(Map<String, Object> model, Locale locale,
			 @RequestParam(value="type") ShipType type,
			 @RequestParam(value="x") Integer x, @RequestParam(value="y") Integer y)
	{
		Game game = (Game) model.get("game");
		User user = (User) model.get("user");
		if (game==null){
			if (logger.isDebugEnabled()){
				logger.debug("deploy ship for non existing game");
			}
			return null;
		} if (game.getStarted()!=null){
			if (logger.isDebugEnabled()){
				logger.debug("deploy ship for game that is started");
			}
			return null;
		} else {
			if (logger.isDebugEnabled()){
				logger.debug("deploy ship for game #"+game.getId());
			}
			Boolean player1 = getPlayer(game, user);
			if (player1==null){
				return null;
			}
			ShipCreationResult rez = gameService.createShip(new Coordinates(x, y), type, game, player1);
			if (rez==ShipCreationResult.OK){
				return new Message("OK");
			} else {
				return new Message(messageSource.getMessage(rez.toString(), null, locale));
			}
		}
	}

	/**
	 * try to deploy a ship in a given position
	 * @return result code
	 */
	@RequestMapping(value="/deleteShip.htm")
	public @ResponseBody List<Coordinates> deleteShip(Map<String, Object> model,
			 @RequestParam(value="x") Integer x, @RequestParam(value="y") Integer y)
	{
		Game game = (Game) model.get("game");
		User user = (User) model.get("user");
		if (game==null){
			if (logger.isDebugEnabled()){
				logger.debug("delete ship for non existing game");
			}
			return null;
		} if (game.getStarted()!=null){
			if (logger.isDebugEnabled()){
				logger.debug("delete ship for game that is started");
			}
			return null;
		} else {
			if (logger.isDebugEnabled()){
				logger.debug("delete ship for game #"+game.getId());
			}
			Boolean player1 = getPlayer(game, user);
			if (player1==null){
				return null;
			}
			return gameService.deleteShip(new Coordinates(x, y), game, player1);
		}
	}

	/**
	 * @return deployed ships coordinates
	 */
	@RequestMapping(value="/myShips.htm")
	public @ResponseBody List<Coordinates> getShips(Map<String, Object> model, Locale locale)
	{
		Game game = (Game) model.get("game");
		User user = (User) model.get("user");
		if (game==null){
			if (logger.isDebugEnabled()){
				logger.debug("requested placed ships for non existing game");
			}
			return null;
		} else {
			if (logger.isDebugEnabled()){
				logger.debug("getting placed ships for game #"+game.getId());
			}
			Boolean player1 = getPlayer(game, user);
			if (player1==null){
				return null;
			}
			return gameService.getUsedCoordinates(game, player1);
		}
	}

	@RequestMapping(value="/shoot.htm")
	public @ResponseBody Message shoot(Map<String, Object> model, Locale locale,
			@RequestParam(value="x") Integer x, @RequestParam(value="y") Integer y)
	{
		Game game = (Game) model.get("game");
		User user = (User) model.get("user");
		if (game==null){
			if (logger.isDebugEnabled()){
				logger.debug("requested shoot for non existing game");
			}
			return null;
		} else {
			if (logger.isDebugEnabled()){
				logger.debug("shooting in game #"+game.getId());
			}
			Boolean player1 = getPlayer(game, user);
			if (player1==null){
				return null;
			}
			TurnResult rez = gameService.makeTurn(game, player1, new Coordinates(x, y));
			StringBuilder sb = new StringBuilder();
			if (rez.getShootResult()!=null) {
				sb.append(messageSource.getMessage(rez.getShootResult().toString(), null, locale));
				sb.append(", ");
			}
			sb.append(messageSource.getMessage(rez.getRoundResult().toString(), null, locale));
			return new Message(sb.toString());
		}
	}

	/** shows all shots that were made in current game by given player */
	@RequestMapping(value="/shots.htm")
	public @ResponseBody List<Round> shots(Map<String, Object> model, Locale locale,
			@RequestParam(value="opponent") Boolean opponent)
	{
		Game game = (Game) model.get("game");
		User user = (User) model.get("user");
		if (game==null){
			if (logger.isDebugEnabled()){
				logger.debug("requested shots(rounds) for non existing game");
			}
			return null;
		} else {
			if (logger.isDebugEnabled()){
				logger.debug("getting shots(rounds) for game #"+game.getId());
			}
			Boolean player1 = getPlayer(game, user);
			if (player1==null){
				return null;
			}
			return gameService.getRounds(game, player1 ^ opponent);
		}
	}

	/**
	 * get player of the game
	 * @return null if wrong player
	 */
	private Boolean getPlayer(Game game, User user){
		if (game.getPlayer1().equals(user.getLogin())){
			return Boolean.TRUE;
		} else if (game.getPlayer2().equals(user.getLogin())){
			return Boolean.FALSE;
		} else {
			return null;
		}
	}

	@ModelAttribute
	public void getUser(HttpServletRequest request, Map<String, Object> model){
		User user = (User)com.multimedia.security.Utils.getCurrentUser(request);
		if (user==null){
			return;
		}
		if (logger.isDebugEnabled()){
			logger.debug("get game for user ["+user.getLogin()+"]");
		}
		model.put("user", user);
		model.put("game", gameService.getGame(user));
	}

	@Override
	public void ticketRemoved(Long id_owner, Long id_oponent) {
		gameService.createGame(
				userService.getUser(id_owner).getLogin(),
				userService.getUser(id_oponent).getLogin());
	}

	//---------------------------- setters ---------------------------

	@Required
	@Resource(name="templateConfig")
	public void setConfig(ITemplateConfig config) {
		this.config = config;
	}

	@Required
	@Resource(name="ticketService")
	public void setTicketService(ITicketService value){
		this.ticketService = value;
		this.ticketService.registerListener(this);
	}

	@Required
	@Resource(name="gameService")
	public void setGameService(IGameService gameService) {
		this.gameService = gameService;
	}

	@Required
	@Resource(name="userService")
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
}

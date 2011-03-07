package com.multimedia.seabattle.controllers;

import java.util.HashMap;
import java.util.HashSet;
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
import com.multimedia.seabattle.model.beans.ShipInfo;
import com.multimedia.seabattle.model.beans.Ticket;
import com.multimedia.seabattle.model.beans.User;
import com.multimedia.seabattle.model.types.ShipCreationResult;
import com.multimedia.seabattle.model.types.ShipType;
import com.multimedia.seabattle.service.game.IGameService;
import com.multimedia.seabattle.service.ticket.ITicketListener;
import com.multimedia.seabattle.service.ticket.ITicketService;
import com.multimedia.seabattle.service.user.IUserService;

@Controller
@RequestMapping(value="/game")
public class GameController implements ITicketListener, MessageSourceAware{
	private static final Logger logger = LoggerFactory.getLogger(GameController.class);

	protected ITemplateConfig config;
	private ITicketService ticketService;
	private IGameService gameService;

	private IUserService userService;

	private MessageSource messageSource;

	private final String lobby_url="/WEB-INF/views/game/player_lobby.jsp";
	private final String ready_url="/WEB-INF/views/game/player_ready.jsp";

	@RequestMapping(value="/player.htm")
	public String index(Map<String, Object> model){
		User user = (User) model.get("user");
		if (model.get("game")==null){
			Ticket t = ticketService.getTicket(user);
			if (t==null){
				waitPlayer(model);
			} else {
				startingGame(model);
			}
		} else {
			startingGame(model);
		}
		return config.getTemplateUrl();
	}

	@RequestMapping(value="/ships.htm")
	public @ResponseBody Set<ShipInfo> getShipTypes(Map<String, Object> model, Locale locale){
		Game game = (Game) model.get("game");
		if (game==null){
			if (logger.isDebugEnabled()){
				logger.debug("requested ships for non existing game");
			}
			return null;
		} else {
			if (logger.isDebugEnabled()){
				logger.debug("getting ships for game #"+game.getId());
			}
			Set<ShipType> ships = gameService.getAvailableShips(game);
			Set<ShipInfo> rez = new HashSet<ShipInfo>();
			for (ShipType ship : ships){
				ShipInfo si = new ShipInfo();
				si.setType(ship.toString());
				si.setName(messageSource.getMessage(ship.toString(), null, locale));
				si.setCoordinates(ship.getOffset());
				si.setValue(ship.getValue());
				rez.add(si);
			}
			return rez;
		}
	}

	@RequestMapping(value="/checkShip.htm")
	public @ResponseBody ShipCreationResult checkShip(Map<String, Object> model, Locale locale,
			 @RequestParam(value="type") ShipType type, @RequestParam(value="coords") Coordinates coords){
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
			Boolean player1 = null;
			if (game.getPlayer1().equals(user.getLogin())){
				player1 = Boolean.TRUE;
			} else if (game.getPlayer2().equals(user.getLogin())){
				player1 = Boolean.FALSE;
			} else {
				//TODO: no such player in this game
				return null;
			}
			ShipCreationResult rez = gameService.createShip(coords, type, game, player1);
			//si.setName(messageSource.getMessage(ship.toString(), null, locale));
			return rez;
		}
	}

	private void waitPlayer(Map<String, Object> model){
		model.put(config.getContentUrlAttribute(), lobby_url);
	}

	private void startingGame(Map<String, Object> model){
		model.put(config.getContentUrlAttribute(), ready_url);
	}

	@ModelAttribute
	public void getUser(HttpServletRequest request, Map<String, Object> model){
		User user = (User)com.multimedia.security.Utils.getCurrentUser(request);
		if (user==null){
			return;
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

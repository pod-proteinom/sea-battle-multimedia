package com.multimedia.seabattle.controllers;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.multimedia.seabattle.config.ITemplateConfig;
import com.multimedia.seabattle.model.beans.Ticket;
import com.multimedia.seabattle.model.beans.User;
import com.multimedia.seabattle.service.game.IGameService;
import com.multimedia.seabattle.service.ticket.ITicketListener;
import com.multimedia.seabattle.service.ticket.ITicketService;
import com.multimedia.seabattle.service.user.IUserService;

@Controller
@RequestMapping(value="/game")
public class GameController implements ITicketListener{
	private static final Logger logger = LoggerFactory.getLogger(GameController.class);

	protected ITemplateConfig config;
	private ITicketService ticketService;
	private IGameService gameService;

	private IUserService userService;

	private final String lobby_url="/WEB-INF/views/game/player_lobby.jsp";
	private final String ready_url="/WEB-INF/views/game/player_ready.jsp";

	@RequestMapping(value="/player.htm")
	public String index(Map<String, Object> model){
		User user = (User)model.get("user");
		if (model.get("game")==null){
			Ticket t = ticketService.getTicket(user);
			if (t==null){
				model.put(config.getContentDataAttribute(), "wait_player");
				model.put(config.getContentUrlAttribute(), lobby_url);
			} else {
				model.put(config.getContentDataAttribute(), "starting_game");
				model.put(config.getContentUrlAttribute(), ready_url);
			}
		} else {
			model.put(config.getContentDataAttribute(), "starting_game");
			model.put(config.getContentUrlAttribute(), ready_url);
		}
		return config.getTemplateUrl();
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
}

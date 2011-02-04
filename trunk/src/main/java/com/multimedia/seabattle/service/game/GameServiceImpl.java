package com.multimedia.seabattle.service.game;

import java.sql.Timestamp;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.multimedia.seabattle.dao.IGenericDAO;
import com.multimedia.seabattle.model.beans.Game;
import com.multimedia.seabattle.service.battlefield.IBattlefieldService;

@Service("gameService")
public class GameServiceImpl implements IGameService{
	private IGenericDAO<Game, Long> game_dao;

	private IBattlefieldService battlefield_service;

	@Override
	public Game startGame(String player_1_name, String player_2_name) {
		Game game = new Game();
		game.setStarted(new Timestamp(System.currentTimeMillis()));
		game.setPlayer1(player_1_name);
		game.setPlayer2(player_2_name);
		game.setReady1(Boolean.FALSE);
		game.setReady2(Boolean.FALSE);
		game_dao.makePersistent(game);
		game_dao.refresh(game);

		battlefield_service.createBattlefield(game);
		return game;
	}

	@Override
	public boolean deleteGame(Long id) {
		return game_dao.deleteById(id)==1;
	}

	@Override
	public boolean endGame(Game game){
		game.setEnded(new Timestamp(System.currentTimeMillis()));
		Boolean winner = findWinner(game);
		if (winner!=null){
			game.setWin1(winner);
			return true;
		} else {
			return false;
		}
	}

	private Boolean findWinner(Game game){
		//TODO: implement
		return Boolean.TRUE;
	}

// -------------------------------- dependencies --------------------------
	@Resource(name="gameDAO")
	public void setGameDAO(IGenericDAO<Game, Long> value){
		this.game_dao = value;
	}

	@Resource(name="battlefieldService")
	public void setBattlefieldService(IBattlefieldService value){
		this.battlefield_service = value;
	}

}

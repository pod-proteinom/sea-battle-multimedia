package com.multimedia.seabattle.service.game;

import java.sql.Timestamp;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.multimedia.seabattle.dao.IGenericDAO;
import com.multimedia.seabattle.model.beans2.Coordinates;
import com.multimedia.seabattle.model.beans2.Game;
import com.multimedia.seabattle.model.beans2.Ship;
import com.multimedia.seabattle.model.types.ShipType;
import com.multimedia.seabattle.service.battlefield.BattlefieldServiceImpl;
import com.multimedia.seabattle.service.battlefield.IBattlefieldService;

@Service("gameService")
public class GameServiceImpl implements IGameService{

	private static final Logger logger = LoggerFactory.getLogger(BattlefieldServiceImpl.class);

	private IGenericDAO<Game, Long> game_dao;
	private IGenericDAO<Ship, Long> ship_dao;

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
		if (logger.isDebugEnabled()){
			logger.debug("starting game ["+game.getId()+"] between ["+player_1_name+"] ["+player_2_name+"]");
		}
		return game;
	}

	@Override
	public boolean deleteGame(Long id) {
		if (logger.isDebugEnabled()){
			logger.debug("deleting game ["+id+"]");
		}
		return game_dao.deleteById(id)==1;
	}

	@Override
	public boolean endGame(Game game){
		game.setEnded(new Timestamp(System.currentTimeMillis()));
		Boolean winner = findWinner(game);
		if (logger.isDebugEnabled()){
			logger.debug("ending game ["+game.getId()+"] between ["+game.getPlayer1()+"] ["+game.getPlayer2()+"]");
		}
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

	@Override
	public Ship createShip(Coordinates coords, ShipType type, Game game,
			Boolean player1)
	{
		if (logger.isDebugEnabled()){
			logger.debug("creating ship ["+coords+"] ["+type+"] in game ["+game.getId()+"] for player ["+player1+"]");
		}
		Coordinates[] offset = type.getOffset();
		Coordinates[] ship_position = new Coordinates[offset.length];
		for (int i=0;i<offset.length;i++){
			Coordinates c = new Coordinates();
			c.setX(coords.getX() + offset[i].getX());
			c.setY(coords.getY() + offset[i].getY());
			ship_position[i] = c;
		}
		return battlefield_service.deployShip(ship_position, game, player1);
	}

	@Override
	public boolean deleteShip(Long id_ship, Game game, Boolean player1) {
		Ship ship = ship_dao.getById(id_ship);
		//1-st we need to check owner of this ship and game
		if ((ship==null) || (!ship.getGame().getId().equals(game.getId())) || (!ship.getPlayer1().equals(player1))){
			if (logger.isDebugEnabled()){
				logger.debug("deleting ship ["+id_ship +"] failed in game ["+game.getId()+"] for player ["+player1+"]");
			}
			return false;
		}
	if (logger.isDebugEnabled()){
			logger.debug("deleting ship ["+id_ship+"] succeed in game ["+game.getId()+"] for player ["+player1+"]");
		}
		return battlefield_service.releaseShip(ship);
	}

// -------------------------------- dependencies --------------------------
	@Resource(name="gameDAO")
	public void setGameDAO(IGenericDAO<Game, Long> value){
		this.game_dao = value;
	}

	@Resource(name="shipDAO")
	public void setShipDAO(IGenericDAO<Ship, Long> value){
		this.ship_dao = value;
	}

	@Resource(name="battlefieldService")
	public void setBattlefieldService(IBattlefieldService value){
		this.battlefield_service = value;
	}

}

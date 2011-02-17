package com.multimedia.seabattle.service.game;

import java.sql.Timestamp;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;

import com.multimedia.seabattle.dao.IGenericDAO;
import com.multimedia.seabattle.model.beans.Coordinates;
import com.multimedia.seabattle.model.beans.Game;
import com.multimedia.seabattle.model.beans.Ship;
import com.multimedia.seabattle.model.beans.TurnResult;
import com.multimedia.seabattle.model.types.GameShipType;
import com.multimedia.seabattle.model.types.PlayerReadyType;
import com.multimedia.seabattle.model.types.RoundResult;
import com.multimedia.seabattle.model.types.ShipCreationResult;
import com.multimedia.seabattle.model.types.ShipType;
import com.multimedia.seabattle.model.types.ShootResult;
import com.multimedia.seabattle.service.battlefield.IBattlefieldService;
import com.multimedia.seabattle.service.computer.IComputerPlayer;
import com.multimedia.seabattle.service.round.IRoundService;
import com.multimedia.seabattle.service.ships.IGameShips;
import com.multimedia.seabattle.service.ships.IShipBuilder;
import com.multimedia.seabattle.service.ships.IShipGenerator;

@Service("gameService")
public class GameServiceImpl implements IGameService{

	private static final Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);

	private IGenericDAO<Game, Long> game_dao;
	private IGenericDAO<Ship, Long> ship_dao;

	private IBattlefieldService battlefield_service;
	private IShipBuilder ship_builder;

	private IRoundService round_service;

	private EnumMap<GameShipType, IGameShips> game_ships = new EnumMap<GameShipType, IGameShips>(GameShipType.class);

	@Override
	public Game createGame(String player_1_name, String player_2_name) {
		Game game = new Game();
		game.setCreated(new Timestamp(System.currentTimeMillis()));
		game.setPlayer1(player_1_name);
		game.setPlayer2(player_2_name);
		game.setReady1(Boolean.FALSE);
		game.setReady2(Boolean.FALSE);
		game_dao.makePersistent(game);
		game_dao.refresh(game);

		battlefield_service.createBattlefield(game);
		if (logger.isDebugEnabled()){
			logger.debug("create game ["+game.getId()+"] between ["+game.getPlayer1()+"] ["+game.getPlayer2()+"]");
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
	public ShipCreationResult createShip(Coordinates coords, ShipType type, Game game, Boolean player1)
	{
		IGameShips ships_service = game_ships.get(game.getType());
		if (!ships_service.checkShipType(type)){
			return ShipCreationResult.SHIP_TYPE_NOT_ALLOWED;
		}
		if (!ships_service.canDeployShipType(type, getAllShipsLength(game, player1))){
			return ShipCreationResult.SHIP_TYPE_ALREADY_PLACED;
		}
		return ship_builder.createShip(type, coords, game, player1);
	}

	/** get length of all ships in given game, owned by given player */
	private List<Integer> getAllShipsLength(Game game, Boolean player1){
		return (List<Integer>)ship_dao.getSingleProperty("length",
				new String[]{"game", "player1"}, new Object[]{game, player1},
				0, 0, null, null);
	}

	@Override
	public boolean deleteShip(Coordinates coords, Game game, Boolean player1) {
		Long ship_id = battlefield_service.getShip(coords, game, player1);
		//1-st we need to check owner of this ship and game
		if (ship_id==null){
			if (logger.isDebugEnabled()){
				logger.debug("no ship with coordinates "+coords +" found in game ["+game.getId()+"] for player ["+player1+"]");
			}
			return false;
		}
		if (battlefield_service.releaseShip(ship_id)){
			if (logger.isDebugEnabled()){
				logger.debug("deleting ship ["+ship_id+"] succeed in game ["+game.getId()+"] for player ["+player1+"]");
			}
			return ship_dao.deleteById(ship_id)>0;
		}
		if (logger.isDebugEnabled()){
			logger.debug("deleting ship ["+ship_id+"] failed in game ["+game.getId()+"] for player ["+player1+"]");
		}
		return false;
	}

	@Override
	public PlayerReadyType playerReady(Game game, Boolean player1) {
		IGameShips ships_service = game_ships.get(game.getType());
		List<Integer> ships = getAllShipsLength(game, player1);
		PlayerReadyType res = ships_service.checkShips(ships);
		if (logger.isDebugEnabled()){
			logger.debug("player ["+player1+"] in game ["+game.getId()+"] has "+ships+" ships. res:"+res);
		}
		if (res == PlayerReadyType.READY){
			if (player1){
				game.setReady1(Boolean.TRUE);
			} else {
				game.setReady2(Boolean.TRUE);
			}
			game_dao.makePersistent(game);
		}
		return res;
	}

	@Override
	public boolean startGame(Game game){
		if (game.getReady1()&&game.getReady2()){
			game.setStarted(new Timestamp(System.currentTimeMillis()));
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Boolean firstTurn(Game game) {
		return round_service.proceedRound(game);
	}

	@Override
	public boolean generatePlayerShips(Game game, Boolean player1,
			IShipGenerator generator)
	{
		//first clear the battlefield
		if (!generator.supports(game)){
			return false;
		}
		IGameShips ships_service = game_ships.get(game.getType());

		battlefield_service.clear(game, player1);
		Map<Coordinates, ShipType> ships = battlefield_service.generateShips(game, ships_service, generator);
		if (ships==null){
			return false;
		}
		Iterator<Entry<Coordinates, ShipType>> i = ships.entrySet().iterator();
		while (i.hasNext()){
			Entry<Coordinates, ShipType> e = i.next();
			if (createShip(e.getKey(), e.getValue(), game, player1)!=ShipCreationResult.OK){
				return false;
			}
		}
		return true;
	}

	@Override
	public TurnResult makeTurn(Game game, Boolean player1,
			Coordinates target) {
		if (!round_service.proceedRound(game).equals(player1)){
			if (logger.isDebugEnabled()){
				logger.debug("turn wrong player ["+player1+"] game ["+game.getId()+"] target "+target);
			}
			return new TurnResult(RoundResult.TURN_WRONG, null);
		}
		ShootResult res;
		if (player1){
			res = battlefield_service.shoot(game, target, Boolean.FALSE);
		} else {
			res = battlefield_service.shoot(game, target, Boolean.TRUE);
		}
		if (logger.isDebugEnabled()){
			logger.debug("turn player ["+player1+"] game ["+game.getId()+"] target "+target+" result: ["+res+"]");
		}
		switch (res){
			case HIT:
				return new TurnResult(
						round_service.endRound(game, player1, target, Boolean.TRUE),
						res);
			case MISS:
				return new TurnResult(
						round_service.endRound(game, player1, target, Boolean.FALSE),
						res);
			case KILL:
				break;
			default:
				throw new UnsupportedOperationException("player has not hit, miss or kill that is impossible");
		}
		if (battlefield_service.hasMoreShips(game, player1)){
			return new TurnResult(
					round_service.endRound(game, player1, target, Boolean.TRUE),
					res);
		} else {
			round_service.endRound(game, player1, target, Boolean.TRUE);

			game.setEnded(new Timestamp(System.currentTimeMillis()));
			game.setWin1(player1);
			game_dao.makePersistent(game);

			if (logger.isDebugEnabled()){
				logger.debug("ending game ["+game.getId()+"] between ["+game.getPlayer1()+"] ["+game.getPlayer2()+"]"+" win="+game.getWin1());
			}
			return new TurnResult(
					RoundResult.WIN,
					res);
		}
	}


// -------------------------------- dependencies --------------------------
	@Required
	@Resource(name="gameDAO")
	public void setGameDAO(IGenericDAO<Game, Long> value){
		this.game_dao = value;
	}

	@Required
	@Resource(name="shipDAO")
	public void setShipDAO(IGenericDAO<Ship, Long> value){
		this.ship_dao = value;
	}

	@Required
	@Resource(name="battlefieldService")
	public void setBattlefieldService(IBattlefieldService value){
		this.battlefield_service = value;
	}

	@Required
	@Resource(name="classicGameShips")
	public void setClassicGameShips(IGameShips value){
		this.game_ships.put(value.getGameType(), value);
	}

	@Required
	@Resource(name="shipBuilder")
	public void setShipBuilder(IShipBuilder value){
		this.ship_builder = value;
	}

	@Required
	@Resource(name="RoundServiceDB")
	public void setRoundService(IRoundService round_service) {
		this.round_service = round_service;
	}

}

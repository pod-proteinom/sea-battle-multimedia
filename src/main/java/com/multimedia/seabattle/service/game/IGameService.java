package com.multimedia.seabattle.service.game;

import com.multimedia.seabattle.model.beans.Coordinates;
import com.multimedia.seabattle.model.beans.Game;
import com.multimedia.seabattle.model.beans.Round;
import com.multimedia.seabattle.model.beans.ShipInfo;
import com.multimedia.seabattle.model.beans.TurnResult;
import com.multimedia.seabattle.model.beans.User;
import com.multimedia.seabattle.model.types.PlayerReadyType;
import com.multimedia.seabattle.model.types.ShipCreationResult;
import com.multimedia.seabattle.model.types.ShipType;
import com.multimedia.seabattle.service.ships.IShipGenerator;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * this service represents user interacting with battlefield
 * @author Dmitriy_Demchuk
 */
public interface IGameService {
	/**
	 * create a game for two players.
	 * @param player_1_name name of player 1.
	 * @param player_2_name name of player 2.
	 */
	public Game createGame(String player_1_name, String player_2_name);

	/**
	 * player decides that he is prepared for the battle.
	 * other players status if both are ready the game may be started.
	 * @return status (READY) means that player is ready, others mean that player must fix errors.
	 */
	public PlayerReadyType playerReady(Game game, Boolean player1);

	/**
	 * an attempt to start the game.
	 * if both players are ready the game may be started.
	 * @return true means that the game can be started.
	 */
	public boolean startGame(Game game);

	/**
	 * deletes the game with given id from the database.
	 */
	public boolean deleteGame(Long id);

	/**
	 * try to create a ship in the game for player, with start coordinates coords
	 * and a given ship type.
	 * @return ship or null if error happened.
	 */
	public ShipCreationResult createShip(Coordinates coords, ShipType type, Game game, Boolean player1);

	/**
	 * try to delete ship with given coordinates in given game, for given player.
	 * @return list of coordinates that were released.
	 */
	public List<Coordinates> deleteShip(Coordinates coords, Game game, Boolean player1);

	/**
	 * generate ships in a given game for given player using given ship generator.
	 */
	public boolean generatePlayerShips(Game game, Boolean player1, IShipGenerator generator);

	/**
	 * get a player that will make its turn first.
	 */
	public Boolean firstTurn(Game game);

	/**
	 * a player makes its turn, and shoots.
	 * @param player1 player that makes its turn
	 * @param target where player1 shoots
	 * @return turn result(shoot result + round result)
	 */
	public TurnResult makeTurn(Game game, Boolean player1, Coordinates target);

	/**
	 * get game for given user or null if none found.
	 */
	public Game getGame(User user);

	/**
	 * get info about ships that can be placed in given game by given player.
	 */
	public Set<ShipInfo> getAvailableShips(Game game, Boolean player1);

	/**
	 * get info about ships that can be placed in given game by given player.
	 */
	public Map<ShipType, Integer> getUnplacedShips(Game game, Boolean player1);

	/**
	 * get a list of coordinates that contain ships.
	 */
	public List<Coordinates> getUsedCoordinates(Game game, Boolean player1);

	/**
	 * register game listener for this game service.
	 * @param listener a listener to register.
	 */
	public void registerGameListener(IGameListener listener);

	/**
	 * register round listener for this game service.
	 * @param listener a listener to register.
	 */
	public void registerRoundListener(IRoundListener  listener);

	/**
	 * get all rounds in a specified game by given player
	 * @return only coordinate + hit fields
	 */
	public List<Round> getRounds(Game game, Boolean player1);
}

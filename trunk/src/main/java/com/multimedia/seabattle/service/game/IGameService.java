package com.multimedia.seabattle.service.game;

import com.multimedia.seabattle.model.beans.Coordinates;
import com.multimedia.seabattle.model.beans.Game;
import com.multimedia.seabattle.model.types.PlayerReadyType;
import com.multimedia.seabattle.model.types.ShipCreationResult;
import com.multimedia.seabattle.model.types.ShipType;
import com.multimedia.seabattle.service.ships.IShipGenerator;

/**
 * this service represents user interacting with battlefield
 * @author Dmitriy_Demchuk
 */
public interface IGameService {
	/**
	 * create a game for two players
	 * @param player_1_name name of player 1
	 * @param player_2_name name of player 2
	 */
	public Game createGame(String player_1_name, String player_2_name);

	/**
	 * player decides that he is prepared for the battle
	 * other players status if both are ready the game may be started 
	 * @return status (READY) means that player is ready, others mean that player must fix errors
	 */
	public PlayerReadyType playerReady(Game game, Boolean player1);

	/**
	 * an attempt to start the game.
	 * if both players are ready the game may be started 
	 * @return true means that the game can be started
	 */
	public boolean startGame(Game game);

	/**
	 * sets game end time to cur, sets winner
	 * ends the game
	 * return false if no winners found
	 */
	public boolean endGame(Game game);

	/**
	 * deletes the game with given id from the database
	 */
	public boolean deleteGame(Long id);

	/**
	 * try to create a ship in the game for player, with start coordinates coords
	 * and a given ship type
	 * @return ship or null if error happened
	 */
	public ShipCreationResult createShip(Coordinates coords, ShipType type, Game game, Boolean player1);

	/**
	 * try to delete ship with given coordinates in given game, for given player
	 * @return false if no such ship found in specified game or owned by given player
	 */
	public boolean deleteShip(Coordinates coords, Game game, Boolean player1);

	/**
	 * generate ships in a given game for given player using given ship generator
	 */
	public boolean generatePlayerShips(Game game, Boolean player1, IShipGenerator generator);
}

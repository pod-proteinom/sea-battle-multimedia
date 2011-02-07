package com.multimedia.seabattle.service.game;

import com.multimedia.seabattle.model.beans.Coordinates;
import com.multimedia.seabattle.model.beans.Game;
import com.multimedia.seabattle.model.beans.Ship;
import com.multimedia.seabattle.model.types.ShipType;

/**
 * for starting and ending game
 * @author Dmitriy_Demchuk
 */
public interface IGameService {
	/**
	 * starts a game for two players
	 * @param player_1_name name of player 1
	 * @param player_2_name name of player 2
	 */
	public Game startGame(String player_1_name, String player_2_name);

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
	public Ship createShip(Coordinates coords, ShipType type, Game game, Boolean player1);

	/**
	 * try to delete ship with given id in given game, for given player
	 * @return false if no such ship found in specified game or owned by given player
	 */
	public boolean deleteShip(Long id_ship, Game game, Boolean player1);
}

package com.multimedia.seabattle.service.battlefield;

import java.util.List;

import com.multimedia.seabattle.model.beans.Cell;
import com.multimedia.seabattle.model.beans.Coordinates;
import com.multimedia.seabattle.model.beans.Game;
import com.multimedia.seabattle.model.beans.Ship;

/**
 * for working with battlefield(cells and boats)
 * @author Dmitriy_Demchuk
 *
 */
public interface IBattlefieldService {
	/**
	 * creates cells for given game
	 */
	public void createBattlefield(Game game);

	/**
	 * get all cells for a given player and given game
	 */
	public List<Cell> getBattlefield(Game game, Boolean player1);

	/**
	 * try to deploy ship on the given coordinates, game, player
	 * @param coords coordinates of the ship
	 * @param game game in which to place the ship
	 * @param player1 does player1 owns the ship
	 * @return a ship or null if an error happened
	 */
	public Ship deployShip(Coordinates[] coords, Game game, Boolean player1);

	/**
	 * try to release all cells that are occupied by this ship
	 * and deletes this this ship
	 * @return a ship or null if an error happened
	 */
	public boolean releaseShip(Ship ship);
}

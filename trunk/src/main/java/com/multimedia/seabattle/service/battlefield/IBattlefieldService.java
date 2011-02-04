package com.multimedia.seabattle.service.battlefield;

import java.util.List;

import com.multimedia.seabattle.model.beans.Cell;
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
	 * create a ship on the given cells
	 * @param cells where to place ship
	 * @return a ship or null if an error happened
	 */
	public Ship createShip(List<Cell> cells);
}

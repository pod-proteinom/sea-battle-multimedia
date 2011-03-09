package com.multimedia.seabattle.service.battlefield;

import java.util.List;
import java.util.Map;

import com.multimedia.seabattle.model.beans.Cell;
import com.multimedia.seabattle.model.beans.Coordinates;
import com.multimedia.seabattle.model.beans.Game;
import com.multimedia.seabattle.model.beans.Ship;
import com.multimedia.seabattle.model.types.ShipCreationResult;
import com.multimedia.seabattle.model.types.ShipType;
import com.multimedia.seabattle.model.types.ShootResult;
import com.multimedia.seabattle.service.ships.IGameShips;
import com.multimedia.seabattle.service.ships.IShipGenerator;

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
	 * clear all objects and marks on the battlefield
	 */
	public void clear(Game game, Boolean player1);

	/**
	 * get all cells for a given player and given game
	 */
	public List<Cell> getBattlefield(Game game, Boolean player1);

	/**
	 * try to deploy ship on the given coordinates, game, player.
	 * and saves a ship to the database
	 * @param coords coordinates of the ship
	 * @param game game in which to place the ship
	 * @param player1 does player1 owns the ship
	 * @return a ship or null if an error happened
	 */
	public ShipCreationResult deployShip(Coordinates[] coords, Game game, Boolean player1, Ship ship);

	/**
	 * try to release all cells that are occupied by ship with given id
	 * @return true if succeed
	 */
	public boolean releaseShip(Long ship_id);

	/**
	 * try to find a ship in a specified coordinates for a given game and given player
	 * @param coords where to search ship
	 * @return id of a ship or null if none found
	 */
	public Long getShip(Coordinates coords, Game game, Boolean player1);

	/**
	 * generate ships for a given game type using given generator
	 * @return map with ships and their coordinates
	 */
	public Map<Coordinates, ShipType> generateShips(Game game, IGameShips game_ships, IShipGenerator generator);

	/**
	 * the player shoots the target coordinates, owned by a given player
	 * @return result of shooting
	 */
	public ShootResult shoot(Game game, Coordinates target, Boolean player1);

	/**
	 * checks whether a player1 has more alive ships in the game
	 * @return true is has
	 */
	public boolean hasMoreShips(Game game, Boolean player1);

	/**
	 * get a list of coordinates that contain ships
	 */
	public List<Coordinates> getUsedCoordinates(Game game, Boolean player1);

	/**
	 * get a list of coordinates for a given ship
	 */
	public List<Coordinates> getShipCoordinates(Long id);
}

package com.multimedia.seabattle.service.ships;

import com.multimedia.seabattle.model.beans.Coordinates;
import com.multimedia.seabattle.model.beans.Game;
import com.multimedia.seabattle.model.types.ShipCreationResult;
import com.multimedia.seabattle.model.types.ShipType;

public interface IShipBuilder {

	/**
	 * creates a ship with given parameters on a battlefield
	 * @param type type of a ship
	 * @param c starting coordinates of a ship
	 * @param game game in which to create a ship
	 * @param player1 player that owns a ship
	 * @return a ship creation message, (OK=created, other - fail)
	 */
	public ShipCreationResult createShip(ShipType type, Coordinates coords, Game game, Boolean player1);

}

package com.multimedia.seabattle.service.collisions;

import com.multimedia.seabattle.model.beans.Coordinates;
import com.multimedia.seabattle.model.types.GameShipCollision;

/**
 * encapsulates logic for determining coordinates, that must be empty for placing the ship.
 * i.e. ship collisions
 * @author Dmitriy_Demchuk
 */
public interface IShipCollision {
	/**
	 * get coordinates that must be empty in order to place ship
	 * @param coordinates that are actually occupied by ship
	 */
	public CollisionIterator getShipCoordinates(Coordinates[] coords);

	/**
	 * get ship collision type handled by this class
	 */
	public GameShipCollision getShipCollisionType();
}

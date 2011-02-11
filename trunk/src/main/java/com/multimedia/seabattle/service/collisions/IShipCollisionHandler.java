package com.multimedia.seabattle.service.collisions;

import com.multimedia.seabattle.model.beans.Coordinates;
import com.multimedia.seabattle.model.beans.Game;

public interface IShipCollisionHandler {

	/**
	 * get ship coordinates offset for given game
	 * @param coords ship coordinates
	 * @return set of coordinates that must not be occupied by an other ship, but may not exist
	 */
	public CollisionIterator getShipCoordinates(Coordinates[] coords, Game game);

	/**
	 * get ship collisions for given game
	 */
	public IShipCollision getShipCollisions(Game game);

}

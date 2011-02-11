package com.multimedia.seabattle.service.collisions;

import java.util.EnumMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.multimedia.seabattle.model.beans.Coordinates;
import com.multimedia.seabattle.model.beans.Game;
import com.multimedia.seabattle.model.types.GameShipCollision;

/**
 * handling collisions of a specified game
 * @author Dmitriy_Demchuk
 */
@Service("shipCollisionHandler")
public class ShipCollisionHandler implements IShipCollisionHandler{
	private EnumMap<GameShipCollision, IShipCollision> ship_collisions =
		new EnumMap<GameShipCollision, IShipCollision>(GameShipCollision.class);

	@Override
	public CollisionIterator getShipCoordinates(Coordinates[] coords,
			Game game) {
		return ship_collisions.get(game.getShipCollisionType()).getShipCoordinates(coords);
	}

	@Override
	public IShipCollision getShipCollisions(Game game) {
		return ship_collisions.get(game.getShipCollisionType());
	}

	@Resource(name="classicShipCollision")
	public void setClassicShipCollision(IShipCollision value){
		ship_collisions.put(value.getShipCollisionType(), value);
	}
}

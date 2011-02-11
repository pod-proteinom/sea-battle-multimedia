package com.multimedia.seabattle.service.battlefield;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.multimedia.seabattle.model.beans.Coordinates;
import com.multimedia.seabattle.model.types.ShipType;
import com.multimedia.seabattle.service.collisions.CollisionIterator;
import com.multimedia.seabattle.service.collisions.IShipCollision;

/**
 * represents a battlefield, that stores its coordinates in sets (java.util.Set)
 * @author Dmitriy_Demchuk
 *
 */
public class HashBattlefield implements IBattlefield{
	private static final Logger logger = LoggerFactory.getLogger(HashBattlefield.class);

	private final Set<Coordinates> ship_coordinates = new HashSet<Coordinates>();
	private final Set<Coordinates> collision_coordinates = new HashSet<Coordinates>();
	private final List<Integer> ships_deployed = new ArrayList<Integer>();

	private final IShipCollision collision_handler;
	private final int width;
	private final int height;

	public HashBattlefield(IShipCollision collision_handler, int width, int height){
		this.collision_handler = collision_handler;
		this.width = width;
		this.height = height;
	}

	@Override
	public List<Integer> getShipsDeployed(){
		return ships_deployed;
	}

	@Override
	public void clear() {
		ship_coordinates.clear();
		collision_coordinates.clear();
		ships_deployed.clear();
	}

	@Override
	public boolean deployShip(Integer value, List<Coordinates> ship) {
		for (Coordinates c:ship){
			if (checkShipCoordinate(c)){
				return false;
			}
		}

		ships_deployed.add(value);

		CollisionIterator collisions = collision_handler.getShipCoordinates(ship.toArray(new Coordinates[0]));
		while (collisions.hasNext()){
			collision_coordinates.add(collisions.next());
		}

		for (int i=0;i<ship.size();i++){
			ship_coordinates.add(ship.get(i));
		}
		if (logger.isDebugEnabled()){
			logger.debug("ships values changed "+ships_deployed);
			logger.debug("collisions changed "+collision_coordinates);
			logger.debug("ships changed "+ship_coordinates);
		}
		return true;
	}

	@Override
	public boolean deployShip(ShipType type, Coordinates coord) {
		int len = type.getOffset().length;
		List<Coordinates> ship = new ArrayList<Coordinates>(len);
		for (int i=0;i<len;i++){
			ship.add(new Coordinates());
			ship.get(i).setX(coord.getX() + type.getOffset()[i].getX());
			ship.get(i).setY(coord.getY() + type.getOffset()[i].getY());
		}
		return deployShip(type.getValue(),ship);
	}

	@Override
	public boolean checkShipCoordinate(Coordinates c){
		return  c.getX()>=width || c.getX()<0 ||
				c.getY()>=height || c.getY()<0 ||
				collision_coordinates.contains(c);
	}
}

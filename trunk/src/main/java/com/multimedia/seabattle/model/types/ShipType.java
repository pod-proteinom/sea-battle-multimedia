package com.multimedia.seabattle.model.types;

import com.multimedia.seabattle.model.beans.Coordinates;

/**
 * defines a ship name, coordinates offset and length of all available ships
 * @author Dmitriy_Demchuk
 */
public enum ShipType {
	TINY(new Coordinates(0, 0)),
	SMALL_VERTICAL(new Coordinates(0, 0), new Coordinates(0, 1)),
	SMALL_HORISONTAL(new Coordinates(0, 0), new Coordinates(1, 0)),
	MEDIUM_VERTICAL(new Coordinates(0, 0), new Coordinates(0, 1), new Coordinates(0, 2)),
	MEDIUM_HORISONTAL(new Coordinates(0, 0), new Coordinates(1, 0), new Coordinates(2, 0)),
	LARGE_VERTICAL(new Coordinates(0, 0), new Coordinates(0, 1), new Coordinates(0, 2), new Coordinates(0, 3)),
	LARGE_HORISONTAL(new Coordinates(0, 0), new Coordinates(1, 0), new Coordinates(2, 0), new Coordinates(3, 0)),
	UNSUPPORTED_SHIP(new Coordinates(0, 0), new Coordinates(1, 0), new Coordinates(0, 1), new Coordinates(1, 1));

	private ShipType(Coordinates... coords){
		this.coords = coords;
	}

	private final Coordinates[] coords;

	/**
	 * get coordinates offset for this ship type
	 */
	public Coordinates[] getOffset(){
		return coords;
	}

	/**
	 * get value of current ship(ships with equal value are the same ship)
	 */
	public int getValue(){
		return coords.length;
	}
}

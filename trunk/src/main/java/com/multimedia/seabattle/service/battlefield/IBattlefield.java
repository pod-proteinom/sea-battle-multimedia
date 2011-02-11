package com.multimedia.seabattle.service.battlefield;

import java.util.List;

import com.multimedia.seabattle.model.beans.Coordinates;
import com.multimedia.seabattle.model.types.ShipType;

public interface IBattlefield {
	/**
	 * get values of ships that are deployed on the battlefield
	 */
	public List<Integer> getShipsDeployed();

	/**
	 * clear all objects and marks on the battlefield
	 */
	public void clear();

	/**
	 * attempts to deploy a ship on the battlefield
	 * @param value value of the ship
	 * @param ship coordinates of the ship
	 * @return true if succeed
	 */
	public boolean deployShip(Integer value, List<Coordinates> ship);

	/**
	 * attempts to deploy a ship on the battlefield
	 * @param type type of ship to deploy
	 * @param ship starting coordinates of ship
	 * @return true if succeed
	 */
	public boolean deployShip(ShipType type, Coordinates coord);

	/**
	 * checks whether a ship may be placed in this coordinate
	 * @return false if can not
	 */
	public boolean checkShipCoordinate(Coordinates c);
}

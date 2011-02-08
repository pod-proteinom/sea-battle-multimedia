package com.multimedia.seabattle.service.ships;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.multimedia.seabattle.model.types.GameShipType;
import com.multimedia.seabattle.model.types.ShipType;

/**
 * defines the base point for detecting if a given ship type is valid
 * and total size/quantity of ships that must be used in the game
 * @author Dmitriy_Demchuk
 *
 */
public interface IGameShips {
	/**
	 * get game type that can be handled by this class
	 */
	public GameShipType getGameType();

	/**
	 * checks whether this ship type is valid in current game type
	 * @return true if valid
	 */
	public boolean checkShipType(ShipType type);

	/**
	 * checks whether this ship type can be deployed
	 * @param ships ships that are already placed on a battlefield
	 * @return true if valid
	 */
	public boolean canDeployShipType(ShipType type, List<Integer> ships);

	/**
	 * get valid ship types for this game type
	 */
	public Set<ShipType> getValidShipTypes();

	/**
	 * checks whether any ship that is not allowed by the game rules is set.
	 * @param quantity ships that are already deployed on the battlefield
	 * @return ship type values that must be removed(if>0), added (if<0).
	 */
	public Map<Integer, Integer> getInvalidShipTypes(Map<ShipType, Integer> quantity);

	/**
	 * get valid ship types for this game type, ordered by length.
	 * @param quantity ships that are already deployed on the battlefield
	 * @return valid ShipTypes that must be deployed.
	 */
	public Set<ShipType> getValidShipTypes(Map<Integer, Integer> quantity);
}

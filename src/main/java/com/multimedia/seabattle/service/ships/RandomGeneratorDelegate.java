package com.multimedia.seabattle.service.ships;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.multimedia.seabattle.model.beans.Coordinates;
import com.multimedia.seabattle.model.beans.Game;
import com.multimedia.seabattle.model.types.ShipType;
import com.multimedia.seabattle.service.battlefield.HashBattlefield;
import com.multimedia.seabattle.service.battlefield.IBattlefield;
import com.multimedia.seabattle.service.collisions.IShipCollision;

/**
 * helper class for generating ships on a battlefield
 * @author Dmitriy_Demchuk
 */
public class RandomGeneratorDelegate {
	private static final Logger logger = LoggerFactory.getLogger(RandomGeneratorDelegate.class);

	private final Map<Coordinates, ShipType> result = new HashMap<Coordinates, ShipType>();

	private final IBattlefield battlefield;

	private final int battlefield_try_quantity;
	private final int ship_try_quantity;
	private final IGameShips game_ships;
	private final Game game;

	private final Random random = new Random();

	public RandomGeneratorDelegate(IGameShips game_ships,
			IShipCollision collision_handler, Game game,
			int battlefield_try_quantity, int ship_try_quantity)
	{
		this.game = game;
		this.game_ships = game_ships;
		this.battlefield_try_quantity = battlefield_try_quantity;
		this.ship_try_quantity = ship_try_quantity;

		battlefield = new HashBattlefield(collision_handler, game.getWidth(), game.getHeight());
	}

	/**
	 * generates a ship types with coordinates
	 * @param battlefield_try_quantity
	 * @param ship_try_quantity
	 * @return
	 */
	public Map<Coordinates, ShipType> generate(){
		if (logger.isDebugEnabled()){
			logger.debug("starting to generate");
		}
		Set<ShipType> ship_types;
		
		int tries = 0;
		boolean generated;

		do {
			generated = true;
			if (logger.isDebugEnabled()){
				logger.debug("generating battlefield try #"+tries);
			}
			while (!(ship_types = game_ships.getValidShipTypes(battlefield.getShipsDeployed())).isEmpty()){
				ShipType type = getNextShipType(ship_types);
				if (!placeShip(type)){
					reset();
					generated = false;
					break;
				}
				if (logger.isDebugEnabled()){
					logger.debug("generating battlefield try #"+tries+", ships "+battlefield.getShipsDeployed());
				}
			}
		} while (tries++<battlefield_try_quantity && !generated);
		if (generated){
			if (logger.isDebugEnabled()){
				logger.debug("generated: "+result);
			}
			return result;
		} else {
			if (logger.isDebugEnabled()){
				logger.debug("generation failed probably max tries limit reached, you may try again ");
			}
			return null;
		}
	}

	/**
	 * resets all variables, so that you can generate again
	 */
	private void reset(){
		battlefield.clear();
		result.clear();
	}

	/**
	 * generates a random ship_type from the given ones
	 */
	private ShipType getNextShipType(Set<ShipType> ship_types){
		int type_num = random.nextInt(ship_types.size());

		Iterator<ShipType> i = ship_types.iterator();
		while (i.hasNext()&&type_num-->0){
			i.next();
		}
		return i.next();
	}

	/**
	 * @param count quantity of times to try place ship, if fails return false
	 * @return false if cannot place given ship type
	 */
	private boolean placeShip(ShipType type)
	{
		Coordinates c = getNextCoordinates(type);
		if (c==null){
			return false;
		} else {
			result.put(c, type);
			return true;
		}
	}

	/**
	 * try to put a ship of a given type on a random coordinates in a battlefield
	 * @return null if fail or coordinates if succeed
	 */
	private Coordinates getNextCoordinates(ShipType type)
	{
		Coordinates c = new Coordinates();
		int len = type.getOffset().length;
		List<Coordinates> ship = new ArrayList<Coordinates>(len);
		for (int i=0;i<len;i++){
			ship.add(new Coordinates());
		}
		boolean generated;
		int tries = 0;
		do {
			generated = true;
			c.setX(random.nextInt(game.getWidth()));
			c.setY(random.nextInt(game.getHeight()));
			for (int i=0;i<len;i++){
				ship.get(i).setX(c.getX() + type.getOffset()[i].getX());
				ship.get(i).setY(c.getY() + type.getOffset()[i].getY());
				if (battlefield.checkShipCoordinate(ship.get(i))){
					generated = false;
					break;
				}
			}
			if (logger.isDebugEnabled()){
				logger.debug("get next coordinates try #"+tries+", "+c);
			}
		} while (tries++<ship_try_quantity && !generated);
		if (generated){
			if (logger.isDebugEnabled()){
				logger.debug("get next coordinates succeed "+c+", type "+type);
			}
			//because all is checked before we do not need do check result
			if (battlefield.deployShip(type.getValue(), ship)){
				return c;
			} else {
				return null;
			}
		}
		return null;
	}
}

package com.multimedia.seabattle.service.ships;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.multimedia.seabattle.model.beans.ShipInfo;
import com.multimedia.seabattle.model.types.GameShipType;
import com.multimedia.seabattle.model.types.PlayerReadyType;
import com.multimedia.seabattle.model.types.ShipType;

/**
 * defines rules for classic game.
 * 4 ships with length 1, 3 - 2, 2 - 3, 1 - 4
 * @author Dmitriy_Demchuk
 *
 */
@Service("classicGameShips")
public class ClassicGameShips implements IGameShips{
	private static final Logger logger = LoggerFactory.getLogger(ClassicGameShips.class);

	/** defines size and quantity of ships that can be used */
	private final HashMap<Integer, Integer> size_quantity;
	/** a set of valid ship types */
	private final EnumSet<ShipType> valid_ships;

	public ClassicGameShips(){
		size_quantity = new HashMap<Integer, Integer>();
		size_quantity.put(Integer.valueOf(4), Integer.valueOf(1));
		size_quantity.put(Integer.valueOf(3), Integer.valueOf(2));
		size_quantity.put(Integer.valueOf(2), Integer.valueOf(3));
		size_quantity.put(Integer.valueOf(1), Integer.valueOf(4));

		valid_ships = EnumSet.<ShipType>noneOf(ShipType.class);
		valid_ships.add(ShipType.LARGE_HORISONTAL);
		valid_ships.add(ShipType.LARGE_VERTICAL);
		valid_ships.add(ShipType.MEDIUM_HORISONTAL);
		valid_ships.add(ShipType.MEDIUM_VERTICAL);
		valid_ships.add(ShipType.SMALL_HORISONTAL);
		valid_ships.add(ShipType.SMALL_VERTICAL);
		valid_ships.add(ShipType.TINY);
	}

	@Override
	public boolean checkShipType(ShipType type) {
		if (logger.isDebugEnabled()){
			logger.debug("checking ship type ["+type+"] result = "+valid_ships.contains(type));
		}
		return valid_ships.contains(type);
	}

	@Override
	public Set<ShipType> getValidShipTypes() {
		return valid_ships.clone();
	}

	@Override
	public Set<ShipInfo> getShipsInfo(Collection<Integer> ships) {
		Set<ShipInfo> rez = new HashSet<ShipInfo>();
		Map<Integer, Integer> invalid = getInvalidShipTypes(ships);
		for (ShipType ship : valid_ships) {
			ShipInfo si = new ShipInfo();
			si.setType(ship.toString());
			si.setCoordinates(ship.getOffset());
			si.setValue(ship.getValue());
			si.setQuantity(invalid.get(ship.getValue()));
			rez.add(si);
		}
		return rez;
	}

	@Override
	public Set<ShipType> getValidShipTypes(Collection<Integer> ships) {
		Map<Integer, Integer> tmp = getInvalidShipTypes(ships);
		EnumSet<ShipType> ships_left = EnumSet.<ShipType>noneOf(ShipType.class);
		Iterator<ShipType> i = valid_ships.iterator();
		while(i.hasNext()){
			ShipType type = i.next();
			if (tmp.get(type.getValue()) > 0){
				ships_left.add(type);
			}
		}
		return ships_left;
	}

	@Override
	public Map<Integer, Integer> getInvalidShipTypes(Collection<Integer> ships) {
		Map<Integer, Integer> tmp = new HashMap<Integer, Integer>(size_quantity);

		for (Integer ship:ships){
			Integer quantity_cur = tmp.get(ship);
			if (quantity_cur==null){
				//TODO: an error must be here
			} else {
				tmp.put(ship, quantity_cur - 1);
			}
		}

		return tmp;
	}

	@Override
	public PlayerReadyType checkShips(List<Integer> ships){
		Iterator<Entry<Integer, Integer>> tmp = getInvalidShipTypes(ships).entrySet().iterator();
		PlayerReadyType res = PlayerReadyType.READY;
		while(tmp.hasNext()){
			Entry<Integer, Integer> item = tmp.next();
			if (item.getValue()==0){
				
			} else if (item.getValue()>0){
				if (res == PlayerReadyType.HAS_TOO_MANY_SHIPS){
					return PlayerReadyType.HAS_WRONG_SHIPS;
				} else {
					res = PlayerReadyType.HAS_NOT_ENOUGH_SHIPS;
				}
			} else {
				if (res == PlayerReadyType.HAS_NOT_ENOUGH_SHIPS){
					return PlayerReadyType.HAS_WRONG_SHIPS;
				} else {
					res = PlayerReadyType.HAS_TOO_MANY_SHIPS;
				}
			}
		}
		return res;
	}

	@Override
	public GameShipType getGameType() {
		return GameShipType.CLASSIC;
	}

	@Override
	public boolean canDeployShipType(ShipType type, List<Integer> ships) {
		Integer cur = type.getValue();
		int quantity = 0;
		for (Integer i:ships){
			if (cur.equals(i)){
				quantity++;
			}
		}
		if (logger.isDebugEnabled()){
			logger.debug("can deploy ship type ["+type+"] ships "+ships+" result = "+(size_quantity.get(cur) > quantity));
		}
		return size_quantity.get(cur) > quantity;
	}

}

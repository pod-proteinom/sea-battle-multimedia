package com.multimedia.seabattle.service.ships;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.multimedia.seabattle.model.types.GameShipType;
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
	public Set<ShipType> getValidShipTypes(Map<Integer, Integer> quantity) {
		EnumSet<ShipType> ships_left = EnumSet.<ShipType>noneOf(ShipType.class);
		Iterator<ShipType> i = valid_ships.iterator();
		while(i.hasNext()){
			ShipType type = i.next();
			Integer q = quantity.get(type.getValue());
			q = q==null?0:q;
			if (q < size_quantity.get(type.getValue())){
				ships_left.add(type);
			}
		}
		return ships_left;
	}

	@Override
	public Map<Integer, Integer> getInvalidShipTypes(Map<ShipType, Integer> quantity) {
		EnumSet<ShipType> ships_wrong = EnumSet.<ShipType>noneOf(ShipType.class);
		ships_wrong.addAll(quantity.keySet());
		Set<ShipType> types_valid = valid_ships.clone();
		ships_wrong.removeAll(types_valid);
		if (ships_wrong.size()>0){
			//this means that the game already contains wrong ships
			return null;
		}

		Iterator<ShipType> i = types_valid.iterator();
		Map<Integer, Integer> tmp = new HashMap<Integer, Integer>(size_quantity);
		while(i.hasNext()){
			ShipType type = i.next();
			Integer q = quantity.get(type);
			Integer quantity_cur = tmp.get(type.getValue());
			if (quantity_cur==null){
				tmp.put(type.getValue(), -q);
			} else {
				tmp.put(type.getValue(), quantity_cur - q);
			}
		}

		return tmp;
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

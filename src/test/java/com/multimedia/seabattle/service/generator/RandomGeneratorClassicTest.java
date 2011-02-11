package com.multimedia.seabattle.service.generator;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import com.multimedia.seabattle.model.beans.Coordinates;
import com.multimedia.seabattle.model.beans.Game;
import com.multimedia.seabattle.model.types.PlayerReadyType;
import com.multimedia.seabattle.model.types.ShipType;
import com.multimedia.seabattle.service.battlefield.HashBattlefield;
import com.multimedia.seabattle.service.collisions.ClassicShipCollision;
import com.multimedia.seabattle.service.collisions.IShipCollision;
import com.multimedia.seabattle.service.ships.ClassicGameShips;
import com.multimedia.seabattle.service.ships.IGameShips;
import com.multimedia.seabattle.service.ships.RandomGeneratorDelegate;

import static org.junit.Assert.*;

public class RandomGeneratorClassicTest {
	private RandomGeneratorDelegate generator;
	
	private HashBattlefield battlefield;

	private final int battlefield_try_quantity = 10;
	private final int ship_try_quantity = 10;
	private IGameShips game_ships;
	private IShipCollision collision_handler;
	private Game game;
	
	

	@Before
	public void before(){
		game_ships = new ClassicGameShips();
		collision_handler = new ClassicShipCollision();
		game = new Game();

		generator = new RandomGeneratorDelegate(game_ships, collision_handler, game, battlefield_try_quantity, ship_try_quantity);
		battlefield = new HashBattlefield(collision_handler, game.getWidth(), game.getHeight());
	}

	/**
	 * test automatic ship generation
	 * 1) check that all ships where generated in required quantity
	 * 2) check that there are no collisions between the ships
	 */
	@Test
	public void testGeneration(){
		Map<Coordinates, ShipType> result = generator.generate();
		if (result==null){
			return;
		}
		assertNotNull("result must not be null", result);
		assertEquals("result size in classic game", 10, result.size());
		Collection<ShipType> ships = result.values();
		List<Integer> ship_value = new ArrayList<Integer>(ships.size());

		for (ShipType ship:ships){
			assertTrue("ship is of a wrong type", game_ships.checkShipType(ship));
			ship_value.add(ship.getValue());
		}
		assertEquals("player must be ready but is not", PlayerReadyType.READY, game_ships.checkShips(ship_value));

		Iterator<Entry<Coordinates, ShipType>> i = result.entrySet().iterator();
		while (i.hasNext()){
			Entry<Coordinates, ShipType> ship = i.next();
			assertTrue("the generated ship must be deployed", battlefield.deployShip(ship.getValue(), ship.getKey()));
		}
	}

	/*@Test
	public void testManyTimes(){
		for (int i=0;i<10000;i++){
			before();
			testGeneration();
		}
	}*/
}

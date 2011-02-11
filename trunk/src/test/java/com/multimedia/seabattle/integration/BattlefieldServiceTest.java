package com.multimedia.seabattle.integration;

import java.util.List;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.multimedia.seabattle.model.beans.Cell;
import com.multimedia.seabattle.model.beans.Coordinates;
import com.multimedia.seabattle.model.beans.Game;
import com.multimedia.seabattle.model.beans.Ship;
import com.multimedia.seabattle.model.types.ShipCreationResult;
import com.multimedia.seabattle.model.types.ShootResult;
import com.multimedia.seabattle.service.battlefield.IBattlefieldService;
import com.multimedia.seabattle.service.game.IGameService;
import com.multimedia.seabattle.service.ships.IGameShips;
import com.multimedia.seabattle.service.ships.RandomShipGenerator;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
"classpath:/spring/root-context.xml"
,"classpath:/spring/hibernate-context.xml"})
public class BattlefieldServiceTest {
	private IGameService game_service;
	private IBattlefieldService battlefield_service;
	private IGameShips game_ships;

	private Game game;

	/**
	 * preparing battlefield before test
	 */
	@Before
	public void createBattlefield(){
		game = game_service.createGame("Player 1", "Player 2");
	}

	/**
	 * deleting battlefield after test is finished
	 */
	@After
	public void destroyBattlefield(){
		game_service.deleteGame(game.getId());
	}

	/**
	 * trying to place a ship on 4 cells vertically
	 * on player1 battlefield
	 */
	@Test
	public void testVerticalShip4Placement(){
		List<Cell> cells = battlefield_service.getBattlefield(game, Boolean.TRUE);
		Coordinates[] new_ship = new Coordinates[4];
		for (int i=11, k=0; i<15; i++){
			new_ship[k++] = cells.get(i).getCoordinates();
		}
		Ship ship = new Ship();
		ShipCreationResult res = battlefield_service.deployShip(new_ship, game, Boolean.TRUE, ship);
		assertEquals("ship not created", ShipCreationResult.OK, res);
		assertNotNull("ship not created", ship.getId());

		cells = battlefield_service.getBattlefield(game, Boolean.TRUE);
		for (int i=11; i<15; i++){
			assertTrue(" the battlefield doesn't contain created ship", cells.get(i).getShip().getId().equals(ship.getId()));
		}
	}

	/**
	 * trying to place a ship on 4 cells horizontally
	 * on player2 battlefield
	 */
	@Test
	public void testHorizontalShip4Placement(){
		List<Cell> cells = battlefield_service.getBattlefield(game, Boolean.FALSE);
		Coordinates[] new_ship = new Coordinates[4];
		for (int i=85, k=0; i<89; i++){
			new_ship[k++] = cells.get(i).getCoordinates();
		}
		Ship ship = new Ship();
		ShipCreationResult res = battlefield_service.deployShip(new_ship, game, Boolean.FALSE, ship);
		assertEquals("ship not created", ShipCreationResult.OK, res);
		assertNotNull("ship not created", ship.getId());

		cells = battlefield_service.getBattlefield(game, Boolean.FALSE);
		for (int i=85; i<89; i++){
			assertTrue(" the battlefield doesn't contain created ship", cells.get(i).getShip().getId().equals(ship.getId()));
		}
	}

	@Test
	public void testRandomGeneratorClassic(){
		RandomShipGenerator generator = new RandomShipGenerator();
		assertNotNull("generator must generate ships", battlefield_service.generateShips(game, game_ships, generator));
	}

	/**
	 * creating a ship and clearing the battlefield
	 */
	@Test
	public void testClear(){
		List<Cell> cells = battlefield_service.getBattlefield(game, Boolean.FALSE);
		Coordinates[] new_ship = new Coordinates[4];
		for (int i=85, k=0; i<89; i++){
			new_ship[k++] = cells.get(i).getCoordinates();
		}
		Ship ship = new Ship();
		ShipCreationResult res = battlefield_service.deployShip(new_ship, game, Boolean.FALSE, ship);
		assertEquals("ship not created", ShipCreationResult.OK, res);

		battlefield_service.clear(game, Boolean.FALSE);

		cells = battlefield_service.getBattlefield(game, Boolean.TRUE);
		for (int i=85; i<89; i++){
			assertNull("the battlefield contains ship", cells.get(i).getShip());
		}
	}

	/**
	 * test the case when we shoot the target and hit it
	 * 1) place a ship
	 * 2) shoot it
	 */
	@Test
	public void testShotHit(){
		List<Cell> cells = battlefield_service.getBattlefield(game, Boolean.FALSE);
		Coordinates[] new_ship = new Coordinates[4];
		for (int i=85, k=0; i<89; i++){
			new_ship[k++] = cells.get(i).getCoordinates();
		}
		Ship ship = new Ship();
		battlefield_service.deployShip(new_ship, game, Boolean.FALSE, ship);

		assertEquals("we hit the target but result was other", ShootResult.HIT, battlefield_service.shoot(game, new Coordinates(8, 5), Boolean.FALSE));
	}

	/**
	 * test the case when we shoot the target and miss it
	 * 1) place a ship
	 * 2) shoot it
	 */
	@Test
	public void testShotMiss(){
		List<Cell> cells = battlefield_service.getBattlefield(game, Boolean.FALSE);
		Coordinates[] new_ship = new Coordinates[4];
		for (int i=85, k=0; i<89; i++){
			new_ship[k++] = cells.get(i).getCoordinates();
		}
		Ship ship = new Ship();
		battlefield_service.deployShip(new_ship, game, Boolean.FALSE, ship);

		assertEquals("we miss the target but result was other", ShootResult.MISS, battlefield_service.shoot(game, new Coordinates(5, 5), Boolean.FALSE));
	}

	/**
	 * test the case when we shoot the target and kill it
	 * 1) place a ship
	 * 2) shoot it
	 */
	@Test
	public void testShotKill(){
		List<Cell> cells = battlefield_service.getBattlefield(game, Boolean.FALSE);
		Coordinates[] new_ship = new Coordinates[4];
		for (int i=85, k=0; i<89; i++){
			new_ship[k++] = cells.get(i).getCoordinates();
		}
		Ship ship = new Ship();
		battlefield_service.deployShip(new_ship, game, Boolean.FALSE, ship);

		assertEquals("we hit the target but result was other", ShootResult.HIT, battlefield_service.shoot(game, new Coordinates(8, 5), Boolean.FALSE));
		assertEquals("we hit the target but result was other", ShootResult.HIT, battlefield_service.shoot(game, new Coordinates(8, 6), Boolean.FALSE));
		assertEquals("we hit the target but result was other", ShootResult.HIT, battlefield_service.shoot(game, new Coordinates(8, 7), Boolean.FALSE));
		assertEquals("we kill the target but result was other", ShootResult.KILL, battlefield_service.shoot(game, new Coordinates(8, 8), Boolean.FALSE));
	}
	

	@Required
	@Resource(name="gameService")
	public void setDao(IGameService value){this.game_service = value;}

	@Required
	@Resource(name="battlefieldService")
	public void setDao(IBattlefieldService value){this.battlefield_service = value;}

	@Required
	@Resource(name="classicGameShips")
	public void setDao(IGameShips value){this.game_ships = value;}

}

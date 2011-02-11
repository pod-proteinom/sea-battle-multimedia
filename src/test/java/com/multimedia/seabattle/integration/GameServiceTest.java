package com.multimedia.seabattle.integration;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.multimedia.seabattle.model.beans.Coordinates;
import com.multimedia.seabattle.model.beans.Game;
import com.multimedia.seabattle.service.game.IGameService;
import com.multimedia.seabattle.service.ships.RandomShipGenerator;
import com.multimedia.seabattle.model.types.PlayerReadyType;
import com.multimedia.seabattle.model.types.ShipCreationResult;
import com.multimedia.seabattle.model.types.ShipType;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
"classpath:/spring/root-context.xml"
,"classpath:/spring/hibernate-context.xml"})
public class GameServiceTest {
	private IGameService game_service;

	private Game game;

	/**
	 * preparing battlefield before test
	 */
	@Before
	public void createGame(){
		game = game_service.createGame("Player 1", "Player 2");
	}

	/**
	 * deleting battlefield after test is finished
	 */
	@After
	public void destroyGame(){
		game_service.deleteGame(game.getId());
	}

	/**
	 * creates ships of all types and deletes them.
	 * No collisions possible so must be no errors
	 */
	@Test
	public void testShipCreationOk(){
		for (ShipType type:ShipType.values()){
			ShipCreationResult res = game_service.createShip(new Coordinates(1, 1), type, game, Boolean.TRUE);
			if (type.equals(ShipType.UNSUPPORTED_SHIP)){
				assertEquals("unsupported ship was created, but might not", ShipCreationResult.SHIP_TYPE_NOT_ALLOWED, res);
				assertFalse("deleted ship that does not exists", game_service.deleteShip(new Coordinates(1, 1), game, Boolean.TRUE));
			} else {
				assertEquals("unsupported ship was created, but might not", ShipCreationResult.OK, res);
				assertTrue("failed to delete ship", game_service.deleteShip(new Coordinates(1, 1), game, Boolean.TRUE));
			}
		}
	}

	/**
	 * creates ships so that their beginning is upper than the battlefield
	 */
	@Test
	public void testShipCreationFailBorderTop(){
		ShipCreationResult res = game_service.createShip(new Coordinates(1, -1), ShipType.LARGE_VERTICAL, game, Boolean.TRUE);
		assertEquals("ship created with coordinates upper then normal", ShipCreationResult.NOT_EXISTING_CELL, res);
	}

	/**
	 * creates ships so that their beginning is lefter than the battlefield
	 */
	@Test
	public void testShipCreationFailBorderLeft(){
		ShipCreationResult res = game_service.createShip(new Coordinates(-1, 1), ShipType.LARGE_HORISONTAL, game, Boolean.TRUE);
		assertEquals("ship created with coordinates lefter then normal", ShipCreationResult.NOT_EXISTING_CELL, res);
	}

	/**
	 * creates ships so that their beginning is lower than the battlefield
	 */
	@Test
	public void testShipCreationFailBorderBottom(){
		ShipCreationResult res = game_service.createShip(new Coordinates(1, 8), ShipType.LARGE_VERTICAL, game, Boolean.TRUE);
		assertEquals("ship created with coordinates lower then normal", ShipCreationResult.NOT_EXISTING_CELL, res);
	}

	/**
	 * creates ships so that their beginning is righter than the battlefield
	 */
	@Test
	public void testShipCreationFailBorderRight(){
		ShipCreationResult res = game_service.createShip(new Coordinates(8, 1), ShipType.LARGE_HORISONTAL, game, Boolean.TRUE);
		assertEquals("ship created with coordinates righter then normal", ShipCreationResult.NOT_EXISTING_CELL, res);
	}

	/**
	 * creates all needed ships on the battlefield.
	 */
	@Test
	public void testPlayerReady1(){
		assertEquals(ShipCreationResult.OK, game_service.createShip(new Coordinates(0, 0), ShipType.TINY, game, Boolean.TRUE));
		assertEquals(ShipCreationResult.OK, game_service.createShip(new Coordinates(2, 0), ShipType.TINY, game, Boolean.TRUE));
		assertEquals(ShipCreationResult.OK, game_service.createShip(new Coordinates(4, 0), ShipType.TINY, game, Boolean.TRUE));
		assertEquals(ShipCreationResult.OK, game_service.createShip(new Coordinates(6, 0), ShipType.TINY, game, Boolean.TRUE));
		assertEquals(ShipCreationResult.OK, game_service.createShip(new Coordinates(8, 0), ShipType.SMALL_VERTICAL, game, Boolean.TRUE));
		assertEquals(ShipCreationResult.OK, game_service.createShip(new Coordinates(0, 5), ShipType.SMALL_VERTICAL, game, Boolean.TRUE));
		assertEquals(ShipCreationResult.OK, game_service.createShip(new Coordinates(2, 5), ShipType.SMALL_VERTICAL, game, Boolean.TRUE));
		assertEquals(ShipCreationResult.OK, game_service.createShip(new Coordinates(4, 5), ShipType.MEDIUM_VERTICAL, game, Boolean.TRUE));
		assertEquals(ShipCreationResult.OK, game_service.createShip(new Coordinates(6, 5), ShipType.MEDIUM_VERTICAL, game, Boolean.TRUE));
		assertEquals(ShipCreationResult.OK, game_service.createShip(new Coordinates(8, 5), ShipType.LARGE_VERTICAL, game, Boolean.TRUE));

		PlayerReadyType res = game_service.playerReady(game, Boolean.TRUE);
		assertEquals("player has set all required ships", PlayerReadyType.READY, res);
		assertTrue("player was ready but game.ready1 was not set", game.getReady1());
	}

	/**
	 * creates not all ships and test player for readiness
	 */
	@Test
	public void testPlayerReady2(){
		assertEquals(ShipCreationResult.OK, game_service.createShip(new Coordinates(0, 0), ShipType.TINY, game, Boolean.TRUE));
		assertEquals(ShipCreationResult.OK, game_service.createShip(new Coordinates(2, 0), ShipType.TINY, game, Boolean.TRUE));
		assertEquals(ShipCreationResult.OK, game_service.createShip(new Coordinates(4, 0), ShipType.TINY, game, Boolean.TRUE));
		assertEquals(ShipCreationResult.OK, game_service.createShip(new Coordinates(6, 0), ShipType.TINY, game, Boolean.TRUE));
		assertEquals(ShipCreationResult.OK, game_service.createShip(new Coordinates(0, 5), ShipType.SMALL_VERTICAL, game, Boolean.TRUE));
		assertEquals(ShipCreationResult.OK, game_service.createShip(new Coordinates(6, 5), ShipType.MEDIUM_VERTICAL, game, Boolean.TRUE));
		assertEquals(ShipCreationResult.OK, game_service.createShip(new Coordinates(8, 5), ShipType.LARGE_VERTICAL, game, Boolean.TRUE));

		PlayerReadyType res = game_service.playerReady(game, Boolean.TRUE);
		assertEquals("player has not enough ships", PlayerReadyType.HAS_NOT_ENOUGH_SHIPS, res);
	}

	/**
	 * creates more ships then it is required and test player for readiness
	 */
	@Test
	public void testPlayerReady3(){
		assertEquals(ShipCreationResult.OK, game_service.createShip(new Coordinates(0, 0), ShipType.TINY, game, Boolean.TRUE));
		assertEquals(ShipCreationResult.OK, game_service.createShip(new Coordinates(2, 0), ShipType.TINY, game, Boolean.TRUE));
		assertEquals(ShipCreationResult.OK, game_service.createShip(new Coordinates(4, 0), ShipType.TINY, game, Boolean.TRUE));
		assertEquals(ShipCreationResult.OK, game_service.createShip(new Coordinates(6, 0), ShipType.TINY, game, Boolean.TRUE));
		assertEquals(ShipCreationResult.OK, game_service.createShip(new Coordinates(8, 0), ShipType.SMALL_VERTICAL, game, Boolean.TRUE));
		assertEquals(ShipCreationResult.OK, game_service.createShip(new Coordinates(0, 5), ShipType.SMALL_VERTICAL, game, Boolean.TRUE));
		assertEquals(ShipCreationResult.OK, game_service.createShip(new Coordinates(2, 5), ShipType.SMALL_VERTICAL, game, Boolean.TRUE));
		assertEquals(ShipCreationResult.OK, game_service.createShip(new Coordinates(4, 5), ShipType.MEDIUM_VERTICAL, game, Boolean.TRUE));
		assertEquals(ShipCreationResult.OK, game_service.createShip(new Coordinates(6, 5), ShipType.MEDIUM_VERTICAL, game, Boolean.TRUE));
		assertEquals(ShipCreationResult.OK, game_service.createShip(new Coordinates(8, 5), ShipType.LARGE_VERTICAL, game, Boolean.TRUE));
		assertEquals("placing the ship with value that is already reached its maximum",
				ShipCreationResult.SHIP_TYPE_ALREADY_PLACED, game_service.createShip(new Coordinates(9, 9), ShipType.TINY, game, Boolean.TRUE));

		PlayerReadyType res = game_service.playerReady(game, Boolean.TRUE);
		assertEquals("player has too many ships", PlayerReadyType.READY, res);
	}

	/**
	 * creates more ships of one type and less ships of other type and test player for readiness
	 */
	@Test
	public void testPlayerReady4(){
		assertEquals(ShipCreationResult.OK, game_service.createShip(new Coordinates(0, 0), ShipType.TINY, game, Boolean.TRUE));
		assertEquals(ShipCreationResult.OK, game_service.createShip(new Coordinates(2, 0), ShipType.TINY, game, Boolean.TRUE));
		assertEquals(ShipCreationResult.OK, game_service.createShip(new Coordinates(4, 0), ShipType.TINY, game, Boolean.TRUE));
		assertEquals(ShipCreationResult.OK, game_service.createShip(new Coordinates(6, 0), ShipType.SMALL_VERTICAL, game, Boolean.TRUE));
		assertEquals(ShipCreationResult.OK, game_service.createShip(new Coordinates(8, 0), ShipType.SMALL_VERTICAL, game, Boolean.TRUE));
		assertEquals(ShipCreationResult.OK, game_service.createShip(new Coordinates(0, 5), ShipType.SMALL_VERTICAL, game, Boolean.TRUE));
		assertEquals("placing the ship with value that is already reached its maximum",
				ShipCreationResult.SHIP_TYPE_ALREADY_PLACED, game_service.createShip(new Coordinates(2, 5), ShipType.SMALL_VERTICAL, game, Boolean.TRUE));
		assertEquals(ShipCreationResult.OK, game_service.createShip(new Coordinates(4, 5), ShipType.MEDIUM_VERTICAL, game, Boolean.TRUE));
		assertEquals(ShipCreationResult.OK, game_service.createShip(new Coordinates(6, 5), ShipType.MEDIUM_VERTICAL, game, Boolean.TRUE));
		assertEquals(ShipCreationResult.OK, game_service.createShip(new Coordinates(8, 5), ShipType.LARGE_VERTICAL, game, Boolean.TRUE));

		PlayerReadyType res = game_service.playerReady(game, Boolean.TRUE);
		assertEquals("player has more ships of one type and less ships of other type", PlayerReadyType.HAS_NOT_ENOUGH_SHIPS, res);
	}

	/**
	 * test random generation for player1 and then for player2
	 */
	@Test
	public void testRandomShipGeneration(){
		RandomShipGenerator generator = new RandomShipGenerator();
		assertTrue("Ship generation did not generate right ships", game_service.generatePlayerShips(game, Boolean.TRUE, generator));
		assertEquals("player must be ready after random ship generation", PlayerReadyType.READY, game_service.playerReady(game, Boolean.TRUE));
		assertTrue("player1 was ready but game.ready1 was not set", game.getReady1());

		assertTrue("Ship generation did not generate right ships", game_service.generatePlayerShips(game, Boolean.FALSE, generator));
		assertEquals("player must be ready after random ship generation", PlayerReadyType.READY, game_service.playerReady(game, Boolean.FALSE));
		assertTrue("player2 was ready but game.ready2 was not set", game.getReady2());
	}

	@Required
	@Resource(name="gameService")
	public void setGameService(IGameService value){this.game_service = value;}
}

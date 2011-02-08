package com.multimedia.seabattle.service.game;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.multimedia.seabattle.dao.IGenericDAO;
import com.multimedia.seabattle.model.beans.Coordinates;
import com.multimedia.seabattle.model.beans.Game;
import com.multimedia.seabattle.model.beans.Ship;
import com.multimedia.seabattle.service.game.IGameService;
import com.multimedia.seabattle.model.types.ShipType;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
"classpath:/spring/root-context.xml"
,"classpath:/spring/hibernate-context.xml"})
public class GameServiceTest {
	private IGameService game_service;

	private IGenericDAO<Ship, Long> ship_dao;

	private Game game;

	/**
	 * preparing battlefield before test
	 */
	@Before
	public void createGame(){
		game = game_service.startGame("Player 1", "Player 2");
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
			if (type.equals(ShipType.UNSUPPORTED_SHIP)){
				Ship ship = game_service.createShip(new Coordinates(1, 1), type, game, Boolean.TRUE);
				assertNull("unsupported ship was created, but might not", ship);
			} else {
				Ship ship = game_service.createShip(new Coordinates(1, 1), type, game, Boolean.TRUE);
				assertNotNull("ship creation failed ["+type.toString()+"]", ship);
				assertEquals("ship has wrong game", game.getId(), ship.getGame().getId());
				assertEquals("ship has wrong player", Boolean.TRUE, ship.getPlayer1());
				assertTrue("failed to delete ship", game_service.deleteShip(ship.getId(), game, Boolean.TRUE));
			}
		}
	}

	/**
	 * creates ships so that their beginning is upper than the battlefield
	 */
	@Test
	public void testShipCreationFailBorderTop(){
		Ship ship = game_service.createShip(new Coordinates(1, -1), ShipType.LARGE_VERTICAL, game, Boolean.TRUE);
		assertNull("ship created with coordinates upper then normal", ship);
	}

	/**
	 * creates ships so that their beginning is lefter than the battlefield
	 */
	@Test
	public void testShipCreationFailBorderLeft(){
		Ship ship = game_service.createShip(new Coordinates(-1, 1), ShipType.LARGE_HORISONTAL, game, Boolean.TRUE);
		assertNull("ship created with coordinates lefter then normal", ship);
	}

	/**
	 * creates ships so that their beginning is lower than the battlefield
	 */
	@Test
	public void testShipCreationFailBorderBottom(){
		Ship ship = game_service.createShip(new Coordinates(1, 8), ShipType.LARGE_VERTICAL, game, Boolean.TRUE);
		assertNull("ship created with coordinates lower then normal", ship);
	}

	/**
	 * creates ships so that their beginning is righter than the battlefield
	 */
	@Test
	public void testShipCreationFailBorderRight(){
		Ship ship = game_service.createShip(new Coordinates(8, 1), ShipType.LARGE_HORISONTAL, game, Boolean.TRUE);
		assertNull("ship created with coordinates righter then normal", ship);
	}

	@Required
	@Resource(name="gameService")
	public void setGameService(IGameService value){this.game_service = value;}

	@Required
	@Resource(name="shipDAO")
	public void setShipDao(IGenericDAO<Ship, Long> value){this.ship_dao = value;}
}

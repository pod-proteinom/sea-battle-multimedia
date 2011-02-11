package com.multimedia.seabattle.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
import com.multimedia.seabattle.model.types.ShipCreationResult;
import com.multimedia.seabattle.model.types.ShipType;
import com.multimedia.seabattle.service.battlefield.IBattlefieldService;
import com.multimedia.seabattle.service.game.IGameService;
import com.multimedia.seabattle.service.ships.IShipBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
"classpath:/spring/root-context.xml"
,"classpath:/spring/hibernate-context.xml"})
public class ShipBuilderTest {
	private IGameService game_service;
	private IShipBuilder ship_builder;
	private IBattlefieldService battlefield_service;
	private IGenericDAO<Ship, Long> ship_dao;

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
		ShipCreationResult res = ship_builder.createShip(ShipType.LARGE_VERTICAL, new Coordinates(1, 1), game, Boolean.TRUE);
		assertEquals("ship not created", ShipCreationResult.OK, res);
	}

	/**
	 * trying to place a ship on 4 cells horizontally
	 * on player2 battlefield
	 */
	@Test
	public void testHorizontalShip4Placement(){
		ShipCreationResult res = ship_builder.createShip(ShipType.LARGE_HORISONTAL, new Coordinates(3, 3), game, Boolean.TRUE);
		assertEquals("ship not created", ShipCreationResult.OK, res);
	}

	/**
	 * creating a ship, and checking whether all properties are set correctly
	 * on player2 battlefield
	 */
	@Test
	public void testShipPropertiesAfterCreation(){
		Coordinates coords = new Coordinates(3, 5);
		ShipCreationResult res = ship_builder.createShip(ShipType.MEDIUM_HORISONTAL, coords, game, Boolean.TRUE);
		assertEquals("ship not created", ShipCreationResult.OK, res);

		Long ship_id = battlefield_service.getShip(coords, game, Boolean.TRUE);
		assertNotNull("ship was created but its id is null", ship_id);

		Ship ship = ship_dao.getById(ship_id);
		assertEquals("the ship is not in a game where it was created", game.getId(), ship.getGame().getId());
		assertEquals("the ship's owner is not the owner it was created", Boolean.TRUE, ship.getPlayer1());
		assertEquals("the ships length doesn't match its type's value", ShipType.MEDIUM_HORISONTAL.getValue(), ship.getLength().intValue());
	}

	@Required
	@Resource(name="gameService")
	public void setDao(IGameService value){this.game_service = value;}

	@Required
	@Resource(name="shipBuilder")
	public void setShipBuilder(IShipBuilder value){
		this.ship_builder = value;
	}

	@Required
	@Resource(name="battlefieldService")
	public void setDao(IBattlefieldService value){this.battlefield_service = value;}

	@Required
	@Resource(name="shipDAO")
	public void setShipDAO(IGenericDAO<Ship, Long> value){
		this.ship_dao = value;
	}
}

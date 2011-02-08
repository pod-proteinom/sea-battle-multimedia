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
import com.multimedia.seabattle.service.battlefield.IBattlefieldService;
import com.multimedia.seabattle.service.game.IGameService;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
"classpath:/spring/root-context.xml"
,"classpath:/spring/hibernate-context.xml"})
public class BattlefieldServiceTest {
	private IGameService game_service;
	private IBattlefieldService battlefield_service;

	private Game game;
	private List<Cell> cells1;
	private List<Cell> cells2;

	/**
	 * preparing battlefield before test
	 */
	@Before
	public void createBattlefield(){
		game = game_service.startGame("Player 1", "Player 2");
		cells1 = battlefield_service.getBattlefield(game, Boolean.TRUE);
		cells2 = battlefield_service.getBattlefield(game, Boolean.FALSE);
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
		Coordinates[] new_ship = new Coordinates[4];
		for (int i=11, k=0; i<51; i += 10){
			new_ship[k++] = cells1.get(i).getCoordinates();
		}
		Ship ship = battlefield_service.deployShip(new_ship, game, Boolean.TRUE);
		assertNotNull("ship not created", ship);
		assertEquals("ship created, but its length is not 4", Integer.valueOf(4), ship.getLength());
	}

	/**
	 * trying to place a ship on 4 cells horizontally
	 * on player2 battlefield
	 */
	@Test
	public void testHorizontalShip4Placement(){
		Coordinates[] new_ship = new Coordinates[4];
		for (int i=85, k=0; i<89; i++){
			new_ship[k++] = cells2.get(i).getCoordinates();
		}
		Ship ship = battlefield_service.deployShip(new_ship, game, Boolean.FALSE);
		assertNotNull("ship not created", ship);
		assertEquals("ship created, but its length is not 4", Integer.valueOf(4), ship.getLength());
	}

	@Required
	@Resource(name="gameService")
	public void setDao(IGameService value){this.game_service = value;}

	@Required
	@Resource(name="battlefieldService")
	public void setDao(IBattlefieldService value){this.battlefield_service = value;}

}

package com.multimedia.seabattle.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.multimedia.seabattle.model.beans.Game;
import com.multimedia.seabattle.service.battlefield.IBattlefieldService;
import com.multimedia.seabattle.service.game.IGameService;
import com.multimedia.seabattle.model.beans.Cell;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
"classpath:/spring/root-context.xml"
,"classpath:/spring/hibernate-context.xml"})
public class GameServiceTest {
	private IGameService game_service;
	private IBattlefieldService battlefield_service;

	/**
	 * test a game process
	 *  starts a game between 2 players,
	 *  ends it, ands deletes
	 */
	@Test
	public void testGameCreation(){
		Game game = game_service.startGame("Player 1", "Player 2");

		assertNotNull(game);
		assertNotNull(game.getId());
		assertNotNull(game.getStarted());

		List<Cell> cells1 = battlefield_service.getBattlefield(game, Boolean.TRUE);
		List<Cell> cells2 = battlefield_service.getBattlefield(game, Boolean.FALSE);

		assertTrue(cells1.size()==100);
		assertTrue(cells2.size()==100);

		List<Cell> new_ship = new ArrayList<Cell>();
		new_ship.add(cells1.get(0));
		battlefield_service.createShip(new_ship);

		game_service.endGame(game);
		
		assertNotNull(game.getEnded());

		assertTrue(game_service.deleteGame(game.getId()));
	}

	@Required
	@Resource(name="gameService")
	public void setDao(IGameService value){this.game_service = value;}

	@Required
	@Resource(name="battlefieldService")
	public void setDao(IBattlefieldService value){this.battlefield_service = value;}
}

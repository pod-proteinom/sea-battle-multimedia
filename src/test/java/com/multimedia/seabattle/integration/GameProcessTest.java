package com.multimedia.seabattle.integration;

import static org.junit.Assert.*;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.multimedia.seabattle.model.beans.Cell;
import com.multimedia.seabattle.model.beans.Game;
import com.multimedia.seabattle.service.battlefield.IBattlefieldService;
import com.multimedia.seabattle.service.game.IGameService;
import com.multimedia.seabattle.service.ships.IShipGenerator;
import com.multimedia.seabattle.service.ships.RandomShipGenerator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
"classpath:/spring/root-context.xml"
,"classpath:/spring/hibernate-context.xml"})
public class GameProcessTest {
	private IGameService game_service;
	private IBattlefieldService battlefield_service;

	/**
	 * test a game process
	 *  creates a game between 2 players,
	 *  ends it, and deletes
	 */
	@Test
	public void testGameCreation(){
		Game game = game_service.createGame("Player 1", "Player 2");

		assertNotNull("game creation failed", game);
		assertNotNull("game has null id", game.getId());
		assertNotNull("game started time not set", game.getCreated());

		List<Cell> cells1 = battlefield_service.getBattlefield(game, Boolean.TRUE);
		List<Cell> cells2 = battlefield_service.getBattlefield(game, Boolean.FALSE);

		assertTrue("battlefield1 size is not 100", cells1.size()==game.getWidth()*game.getHeight());
		assertTrue("battlefield2 size is not 100", cells2.size()==game.getWidth()*game.getHeight());

		game_service.endGame(game);

		assertNotNull("game end time is not set", game.getEnded());

		assertTrue("game deleting failed", game_service.deleteGame(game.getId()));
	}

	/**
	 * test a game process
	 *  1)creates a game between 2 players
	 *  2)set player1 ready
	 *  3)set player2 ready
	 *  4)start game
	 *  5)end game
	 *  6)delete game
	 */
	@Test
	public void testGameStart(){
		Game game = game_service.createGame("Player 1", "Player 2");
		IShipGenerator generator = new RandomShipGenerator();

		game_service.generatePlayerShips(game, Boolean.TRUE, generator);
		game_service.playerReady(game, Boolean.TRUE);

		assertFalse("the game started but only one player was ready", game_service.startGame(game));

		game_service.generatePlayerShips(game, Boolean.FALSE, generator);
		game_service.playerReady(game, Boolean.FALSE);

		assertTrue("the game not started but both player were ready", game_service.startGame(game));
		assertNotNull("start time was not set but the game was started", game.getStarted());

		game_service.endGame(game);

		assertTrue("game deleting failed", game_service.deleteGame(game.getId()));
	}

	@Required
	@Resource(name="gameService")
	public void setDao(IGameService value){this.game_service = value;}

	@Required
	@Resource(name="battlefieldService")
	public void setDao(IBattlefieldService value){this.battlefield_service = value;}

}

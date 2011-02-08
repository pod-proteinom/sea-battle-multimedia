package com.multimedia.seabattle.service.game;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.multimedia.seabattle.service.game.IGameService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
"classpath:/spring/root-context.xml"
,"classpath:/spring/hibernate-context.xml"})
public class GameProcessTest {
	private IGameService game_service;
	private IBattlefieldService battlefield_service;

	/**
	 * test a game process
	 *  starts a game between 2 players,
	 *  ends it, and deletes
	 */
	@Test
	public void testGameCreation(){
		Game game = game_service.startGame("Player 1", "Player 2");

		assertNotNull("game creation failed", game);
		assertNotNull("game has null id", game.getId());
		assertNotNull("game started time not set", game.getStarted());

		List<Cell> cells1 = battlefield_service.getBattlefield(game, Boolean.TRUE);
		List<Cell> cells2 = battlefield_service.getBattlefield(game, Boolean.FALSE);

		assertTrue("battlefield1 size is not 100", cells1.size()==100);
		assertTrue("battlefield2 size is not 100", cells2.size()==100);

		game_service.endGame(game);
		
		assertNotNull("game end time is not set", game.getEnded());

		assertTrue("game deleting failed", game_service.deleteGame(game.getId()));
	}

	@Required
	@Resource(name="gameService")
	public void setDao(IGameService value){this.game_service = value;}

	@Required
	@Resource(name="battlefieldService")
	public void setDao(IBattlefieldService value){this.battlefield_service = value;}

}

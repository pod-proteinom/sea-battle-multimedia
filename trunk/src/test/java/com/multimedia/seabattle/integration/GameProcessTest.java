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
import com.multimedia.seabattle.model.beans.Coordinates;
import com.multimedia.seabattle.model.beans.Game;
import com.multimedia.seabattle.model.beans.TurnResult;
import com.multimedia.seabattle.model.types.RoundResult;
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
	 *  checks whether all cells were created
	 *  and deletes
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

		assertTrue("game deleting failed", game_service.deleteGame(game.getId()));
	}

	/**
	 * test a game process
	 *  1)creates a game between 2 players
	 *  2)generates ships for both players
	 *  3)set player1 ready
	 *  4)set player2 ready
	 *  5)start game
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

		assertTrue("game deleting failed", game_service.deleteGame(game.getId()));
	}

	/**
	 * test one player has won other player
	 *  1)creates a game between 2 players
	 *  2)generates ships for both players
	 *  3)set player1 ready
	 *  4)set player2 ready
	 *  5)start game
	 *  6)start round
	 *  7)simulate a game
	 *  8)test a winner
	 *  9)delete game
	 */
	@Test
	public void testGameWinning(){
		Game game = game_service.createGame("Player 1", "Player 2");
		IShipGenerator generator = new RandomShipGenerator();

		game_service.generatePlayerShips(game, Boolean.TRUE, generator);
		game_service.playerReady(game, Boolean.TRUE);

		game_service.startGame(game);

		game_service.generatePlayerShips(game, Boolean.FALSE, generator);
		game_service.playerReady(game, Boolean.FALSE);

		game_service.startGame(game);
		game.getStarted();

		Boolean player1 = game_service.firstTurn(game);
		assertNotNull("the player that goes first is undefined", player1);

		Coordinates c1 = new Coordinates(0, 0);
		Coordinates c2 = new Coordinates(0, 0);

		TurnResult res;
		if (player1){
			res = game_service.makeTurn(game, player1, c1);
		} else {
			res = game_service.makeTurn(game, player1, c2);
		}
		while (res.getRoundResult()!=RoundResult.WIN) {
			switch (res.getRoundResult()){
				case GAME_NOT_STARTED:
					throw new AssertionError("the game is started");
				case TURN_NEXT:
					player1 = !player1;
					//changing the player and making a turn
					//fall through
				case TURN_AGAIN:
					if (player1){
						res = game_service.makeTurn(game, player1, getNext(c1));
					} else {
						res = game_service.makeTurn(game, player1, getNext(c2));
					}
					break;
				case TURN_WRONG:
					throw new AssertionError("right player makes its turn");
				case WIN:
					break;
			}
		}

		assertNotNull("game is ended but has no end time", game.getEnded());
		assertNotNull("game is ended but has winner", game.getWin1());

		assertTrue("game deleting failed", game_service.deleteGame(game.getId()));
	}

	/**
	 * get next coordinate for shooting.
	 * modifies argument
	 */
	private Coordinates getNext(Coordinates target){
		if (target.getX()>8){
			assertFalse("all possible coordinates were hitted", target.getY()>8);
			target.setX(0);
			target.setY(target.getY()+1);
		} else {
			target.setX(target.getX()+1);
		}
		return target;
	}

//-----------------------------------------------------------------------------------------------------------------

	@Required
	@Resource(name="gameService")
	public void setDao(IGameService value){this.game_service = value;}

	@Required
	@Resource(name="battlefieldService")
	public void setDao(IBattlefieldService value){this.battlefield_service = value;}

}

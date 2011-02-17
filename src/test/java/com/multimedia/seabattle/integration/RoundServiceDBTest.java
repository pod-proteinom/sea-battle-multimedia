package com.multimedia.seabattle.integration;

import javax.annotation.Resource;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.multimedia.seabattle.dao.cell.ICellDAO;
import com.multimedia.seabattle.dao.round.IRoundDAO;
import com.multimedia.seabattle.model.beans.Coordinates;
import com.multimedia.seabattle.model.beans.Game;
import com.multimedia.seabattle.model.beans.Round;
import com.multimedia.seabattle.model.types.RoundResult;
import com.multimedia.seabattle.service.game.IGameService;
import com.multimedia.seabattle.service.round.IRoundService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
"classpath:/spring/root-context.xml"
,"classpath:/spring/hibernate-context.xml"})
public class RoundServiceDBTest {
	private IRoundService round_service;
	private IGameService game_service;
	private IRoundDAO round_dao;

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
	 * start first round
	 * the right player makes its turn
	 * and hits
	 */
	@Test
	public void testNextRound1(){
		Boolean player1 = round_service.proceedRound(game);
		assertEquals("the player has shot the ship but its not his round",
				RoundResult.TURN_AGAIN,
				round_service.endRound(game, player1, new Coordinates(1, 1), Boolean.TRUE));
	}

	/**
	 * start first round
	 * the right player makes its turn
	 * and misses
	 */
	@Test
	public void testNextRound2(){
		Boolean player1 = round_service.proceedRound(game);
		assertEquals("the player has missed the ship but its his round",
				RoundResult.TURN_NEXT,
				round_service.endRound(game, player1, new Coordinates(1, 1), Boolean.FALSE));
	}

	/**
	 * end round without starting round
	 */
	@Test
	public void testNextRound3(){
		assertEquals("the game is not started",
				RoundResult.GAME_NOT_STARTED,
				round_service.endRound(game, Boolean.TRUE, new Coordinates(1, 1), Boolean.FALSE));
	}

	/**
	 * start round
	 * wrong player tries to go
	 */
	@Test
	public void testNextRound4(){
		Boolean player1 = round_service.proceedRound(game);
		assertEquals("wrong player has made its turn",
				RoundResult.TURN_WRONG,
				round_service.endRound(game, !player1, new Coordinates(1, 1), Boolean.FALSE));
	}

	/**
	 * start round,
	 * player missed,
	 * proceed round
	 */
	@Test
	public void testProceedRound(){
		Boolean player1 = round_service.proceedRound(game);
		assertEquals("the player has missed the ship but its his round",
				RoundResult.TURN_NEXT,
				round_service.endRound(game, player1, new Coordinates(1, 1), Boolean.FALSE));
		Round round = round_dao.getLastRound(game);

		assertNotNull("round is started", round);
		assertEquals("player has not changed", !player1, round.getPlayer1());
		assertEquals("round has wrong nuber", Integer.valueOf(2), round.getNumber());
	}
	
//--------------------------------------------------------------------------------------------------------
	@Resource(name="RoundServiceDB")
	public void setRoundService(IRoundService value){
		this.round_service = value;
	}

	@Resource(name="gameService")
	public void setGameService(IGameService value){this.game_service = value;}

	@Resource(name="roundDAO")
	public void setRoundDAO(IRoundDAO value){this.round_dao = value;}
}

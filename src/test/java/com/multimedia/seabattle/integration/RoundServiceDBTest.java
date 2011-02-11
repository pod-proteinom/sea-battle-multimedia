package com.multimedia.seabattle.integration;

import javax.annotation.Resource;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.multimedia.seabattle.model.beans.Coordinates;
import com.multimedia.seabattle.model.beans.Game;
import com.multimedia.seabattle.service.game.IGameService;
import com.multimedia.seabattle.service.round.IRoundService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
"classpath:/spring/root-context.xml"
,"classpath:/spring/hibernate-context.xml"})
public class RoundServiceDBTest {
	private IRoundService round_service;
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
	 * just making some rounds and checking sequence 
	 */
	@Test
	public void testNextRound1(){
		boolean res = round_service.nextRound(game, Boolean.TRUE, new Coordinates(1, 1), Boolean.TRUE);
		assertTrue("the player has shot the ship but its not his round", res);
	}

	/**
	 * just making some rounds and checking sequence 
	 */
	@Test
	public void testNextRound2(){
		boolean res = round_service.nextRound(game, Boolean.FALSE, new Coordinates(1, 1), Boolean.TRUE);
		assertFalse("the player has shot the ship but its not his round", res);
	}

	/**
	 * just making some rounds and checking sequence 
	 */
	@Test
	public void testNextRound3(){
		boolean res = round_service.nextRound(game, Boolean.TRUE, new Coordinates(1, 1), Boolean.FALSE);
		assertFalse("the player has missed the ship but its his round", res);
	}

	/**
	 * just making some rounds and checking sequence 
	 */
	@Test
	public void testNextRound4(){
		boolean res = round_service.nextRound(game, Boolean.FALSE, new Coordinates(1, 1), Boolean.FALSE);
		assertTrue("the player has missed the ship but its his round", res);
	}
	
//--------------------------------------------------------------------------------------------------------
	@Resource(name="RoundServiceDB")
	public void setRoundService(IRoundService value){
		this.round_service = value;
	}

	@Resource(name="gameService")
	public void setGameService(IGameService value){this.game_service = value;}
}

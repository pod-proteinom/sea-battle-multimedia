package com.multimedia.seabattle.service.round;

import java.util.List;

import com.multimedia.seabattle.model.beans.Coordinates;
import com.multimedia.seabattle.model.beans.Game;
import com.multimedia.seabattle.model.beans.Round;
import com.multimedia.seabattle.model.types.RoundResult;

public interface IRoundService {

	/**
	 * starts a round, and returns a player that is making its turn
	 * get player that must make its turn.
	 * create a new round if none exists.
	 */
	public Boolean proceedRound(Game game);

	/**
	 * end this round and proceed to the next round
	 * @param game
	 * @param player1 player that shoots this round
	 * @param c coordinates that were shoot
	 * @param hit true if there was a ship else false
	 * @return player that will make its turn
	 */
	public RoundResult endRound(Game game, Boolean player1, Coordinates c, Boolean hit);

	/**
	 * get all rounds in a specified game by given player
	 * @return only coordinate + hit fields
	 */
	public List<Round> getRounds(Game game, Boolean player1);

}

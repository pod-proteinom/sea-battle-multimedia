package com.multimedia.seabattle.service.round;

import com.multimedia.seabattle.model.beans.Coordinates;
import com.multimedia.seabattle.model.beans.Game;

public interface IRoundService {
	/**
	 * returns player that will make its turn
	 */
	public boolean firstRound();

	/**
	 * proceed for the next round
	 * @param game
	 * @param player1 player that shoots this round
	 * @param c coordinates that were shoot
	 * @param alive true if there was a ship else false
	 * @return player that will make its turn
	 */
	public boolean nextRound(Game game, Boolean player1, Coordinates c, Boolean alive);
}

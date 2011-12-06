package com.multimedia.seabattle.service.game;

import com.multimedia.seabattle.model.beans.TurnResult;

/**
 * methods of this class are called when new round begins.
 * @author Dmitriy_Demchuk
 *
 */
public interface IRoundListener {
	/**
	 * is called when round ends.
	 * @param player1 player that must make its turn.
	 * @param name1 name of player1
	 * @param name2 name of player2
	 */
	void round(String name1, TurnResult res);
	/**
	 * is called when round ends.
	 * @param player1 player that must make its turn.
	 * @param name1 name of player1
	 * @param name2 name of player2
	 */
	void wait(String name1, TurnResult res);
	/**
	 * player wins.
	 */
	void win(String name);
	/**
	 * player looses.
	 */
	void loose(String name);
}

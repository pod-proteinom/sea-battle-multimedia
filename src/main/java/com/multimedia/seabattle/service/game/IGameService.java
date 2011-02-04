package com.multimedia.seabattle.service.game;

import com.multimedia.seabattle.model.beans.Game;

/**
 * for starting and ending game
 * @author Dmitriy_Demchuk
 */
public interface IGameService {
	/**
	 * starts a game for two players
	 * @param player_1_name name of player 1
	 * @param player_2_name name of player 2
	 */
	public Game startGame(String player_1_name, String player_2_name);

	/**
	 * sets game end time to cur, sets winner
	 * ends the game
	 * return false if no winners found
	 */
	public boolean endGame(Game game);

	/**
	 * deletes the game with given id from the database
	 */
	public boolean deleteGame(Long id);
}

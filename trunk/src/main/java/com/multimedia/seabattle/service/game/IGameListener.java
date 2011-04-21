package com.multimedia.seabattle.service.game;

/**
 * If you want to receive events of game service.
 * @author Dmitriy_Demchuk
 */
public interface IGameListener {
	/**
	 * This method is called when a user is ready for a game.(has placed all the ships) 
	 * @param name name of a player that is ready.
	 * @param oponent name of opponent of player that is ready.
	 */
	public void playerReady(final String name, final String opponent);
}

package com.multimedia.seabattle.model.types;

/**
 * the result of a current round
 * @author Dmitriy_Demchuk
 */
public enum RoundResult {
	/** this player goes again */
	TURN_AGAIN,
	/** another player makes its turn */
	TURN_NEXT,
	/** player wins the game */
	WIN,
	/** player is trying to go not in his turn */
	TURN_WRONG,
	/** game not started yet */
	GAME_NOT_STARTED
}

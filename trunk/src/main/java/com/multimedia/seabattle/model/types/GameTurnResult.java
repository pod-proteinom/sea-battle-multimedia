package com.multimedia.seabattle.model.types;

public enum GameTurnResult {
	/** hit the target, but it is not dead */
	HIT,
	/** miss the target */
	MISS,
	/** hit the target and kill it */
	KILL,
	/** player wins the game */
	WIN
}

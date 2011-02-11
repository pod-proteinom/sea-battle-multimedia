package com.multimedia.seabattle.model.types;

public enum PlayerReadyType {
	/** player has too many ships */
	HAS_TOO_MANY_SHIPS,
	/** player has not yet placed some ships */
	HAS_NOT_ENOUGH_SHIPS,
	/** player has too many ships of one type, and has a lack of other ships */
	HAS_WRONG_SHIPS,
	/** the player is ready for game to start */
	READY;
}

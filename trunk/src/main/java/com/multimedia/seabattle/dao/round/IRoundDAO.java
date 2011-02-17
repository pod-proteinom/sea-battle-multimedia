package com.multimedia.seabattle.dao.round;

import com.multimedia.seabattle.dao.IGenericDAO;
import com.multimedia.seabattle.model.beans.Game;
import com.multimedia.seabattle.model.beans.Round;

/**
 * specific methods for round model
 * @author Dmitriy_Demchuk
 */
public interface IRoundDAO extends IGenericDAO<Round, Long>{
	/**
	 * get last round in given game
	 */
	public Round getLastRound(Game game);
}

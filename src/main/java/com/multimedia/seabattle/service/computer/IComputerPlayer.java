package com.multimedia.seabattle.service.computer;

import com.multimedia.seabattle.model.beans.Coordinates;

/**
 * interface for a computer player to implement
 * @author Dmitriy_Demchuk
 */
public interface IComputerPlayer {
	/**
	 * @param game for retrieving some information about game
	 * @return a target where to shoot
	 */
	public Coordinates getTarget();

	/**
	 * get name for this computer
	 */
	public String getName();
}

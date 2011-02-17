package com.multimedia.seabattle.service.computer;

import com.multimedia.seabattle.model.beans.Coordinates;
import com.multimedia.seabattle.model.beans.Game;

/**
 * the algorithm is quiet simple,
 * just shooting from first to last cell in a battlefield
 * @author Dmitriy_Demchuk
 */
public class SequentComputer implements IComputerPlayer{
	private Coordinates nextTarget = new Coordinates(0, 0);
	private Coordinates target;
	private Game game;

	public SequentComputer(Game game){
		this.game = game; 
	}

	@Override
	public Coordinates getTarget() {
		target = nextTarget;
		nextTarget = new Coordinates();
		if (target.getX()>=game.getWidth()){
			//Assert.assertFalse("all possible coordinates were hitted", target.getY()>=game.getHeight());
			nextTarget.setX(0);
			nextTarget.setY(target.getY()+1);
		} else {
			nextTarget.setX(target.getX()+1);
		}

		return target;
	}

	@Override
	public String getName() {
		return "Se #"+System.currentTimeMillis();
	}
}

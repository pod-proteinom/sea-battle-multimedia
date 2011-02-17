package com.multimedia.seabattle.model.beans;

import com.multimedia.seabattle.model.types.RoundResult;
import com.multimedia.seabattle.model.types.ShootResult;

/**
 * used to give result of a turn, which consists of round result and shoot result
 * @author Dmitriy_Demchuk
 */
public class TurnResult {
	private RoundResult roundResult;
	private ShootResult shootResult;

	public TurnResult(RoundResult roundResult, ShootResult shootResult) {
		this.roundResult = roundResult;
		this.shootResult = shootResult;
	}

	public ShootResult getShootResult() {
		return shootResult;
	}
	
	public RoundResult getRoundResult() {
		return roundResult;
	}
}

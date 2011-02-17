package com.multimedia.seabattle.service.round;

import java.sql.Timestamp;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;

import com.multimedia.seabattle.dao.round.IRoundDAO;
import com.multimedia.seabattle.model.beans.Coordinates;
import com.multimedia.seabattle.model.beans.Game;
import com.multimedia.seabattle.model.beans.Round;
import com.multimedia.seabattle.model.types.RoundResult;

@Service("RoundServiceDB")
public class RoundServiceDB implements IRoundService{
	private IRoundDAO round_dao;

	private Random random = new Random();

	@Override
	public RoundResult endRound(Game game, Boolean player1, Coordinates c,
			Boolean hit)
	{
		Round round = round_dao.getLastRound(game);
		if (round==null){
			return RoundResult.GAME_NOT_STARTED;
		} else if (!round.getPlayer1().equals(player1)){
			return RoundResult.TURN_WRONG;
		}
		round.setCoordinates(c);
		round_dao.makePersistent(round);

		//starting new round
		Round nextRound = new Round();
		nextRound.setPlayer1(player1^(!hit));
		nextRound.setGame(game);
		nextRound.setNumber(round.getNumber()+1);
		nextRound.setStarted(new Timestamp(System.currentTimeMillis()));
		round_dao.makePersistent(nextRound);

		return hit?RoundResult.TURN_AGAIN:RoundResult.TURN_NEXT;
	}

	@Override
	public Boolean proceedRound(Game game) {
		Round round = round_dao.getLastRound(game);
		if (round==null){
			round = new Round();
			round.setPlayer1(generatePlayer());
			round.setGame(game);
			round.setNumber(Integer.valueOf(1));
			round.setStarted(new Timestamp(System.currentTimeMillis()));
			round_dao.makePersistent(round);
		}
		return round.getPlayer1();
	}

	private boolean generatePlayer() {
		return random.nextBoolean();
	}

//------------------------------------------- injection -------------------------------------

	@Required
	@Resource(name="roundDAO")
	public void setRoundDAO(IRoundDAO round_dao) {
		this.round_dao = round_dao;
	}
	
}

package com.multimedia.seabattle.service.round;

import java.sql.Timestamp;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.multimedia.seabattle.dao.IGenericDAO;
import com.multimedia.seabattle.model.beans.Coordinates;
import com.multimedia.seabattle.model.beans.Game;
import com.multimedia.seabattle.model.beans.Round;

@Service("RoundServiceDB")
public class RoundServiceDB implements IRoundService{
	private IGenericDAO<Round, Long> round_dao;

	private Random random = new Random();

	@Override
	public boolean firstRound() {
		return random.nextBoolean();
	}

	@Override
	public boolean nextRound(Game game, Boolean player1, Coordinates c,
			Boolean alive)
	{
		Round round = new Round();
		round.setCoordinates(c);
		round.setGame(game);
		round.setStarted(new Timestamp(System.currentTimeMillis()));
		round.setPlayer1(player1);
		Integer number = (Integer)round_dao.getSinglePropertyU("max(number)", "game", game);
		if (number==null){
			round.setNumber(Integer.valueOf(1));
		} else {
			round.setNumber(number+1);
		}
		round_dao.makePersistent(round);
		return player1^(!alive);
	}
	
	@Resource(name="roundDAO")
	public void setRoundDAO(IGenericDAO<Round, Long> round_dao) {
		this.round_dao = round_dao;
	}
}

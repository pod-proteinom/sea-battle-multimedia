package com.multimedia.seabattle.dao.round;

import org.springframework.transaction.annotation.Transactional;

import com.multimedia.seabattle.dao.GenericDAOHibernate;
import com.multimedia.seabattle.model.beans.Game;
import com.multimedia.seabattle.model.beans.Round;

public class RoundDAOHibernate extends GenericDAOHibernate<Round, Long> implements IRoundDAO{

	public RoundDAOHibernate() {
		super("com.multimedia.seabattle.model.beans.Round", "com.multimedia.seabattle.model.beans.Round");
	}

	@Override
	@Transactional(readOnly=true)
	public Round getLastRound(final Game game) {
		return (Round)getSessionFactory().getCurrentSession()
		.getNamedQuery("lastRound")
		.setParameter("game", game)
		.uniqueResult();
	}
}

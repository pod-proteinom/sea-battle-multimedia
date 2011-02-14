package com.multimedia.seabattle.dao.cell;

import org.springframework.transaction.annotation.Transactional;

import com.multimedia.seabattle.model.beans.Cell;
import com.multimedia.seabattle.model.beans.Game;
import com.multimedia.seabattle.dao.GenericDAOHibernate;

public class CellDAOHibernate extends GenericDAOHibernate<Cell, Long> implements ICellDAO{

	public CellDAOHibernate() {
		super("com.multimedia.seabattle.model.beans.Cell", "com.multimedia.seabattle.model.beans.Cell");
	}

	@Override
	@Transactional(readOnly=true)
	public Long getShipAliveCells(final Game game, final Boolean player1) {
		return (Long)getSessionFactory().getCurrentSession()
		.getNamedQuery("shipAliveCells")
		.setParameter("game", game).setParameter("player1", player1)
		.uniqueResult();
	}

}

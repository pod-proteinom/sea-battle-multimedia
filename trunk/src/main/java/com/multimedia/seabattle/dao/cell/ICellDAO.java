package com.multimedia.seabattle.dao.cell;

import com.multimedia.seabattle.dao.IGenericDAO;
import com.multimedia.seabattle.model.beans.Cell;
import com.multimedia.seabattle.model.beans.Game;

/**
 * added some specific methods for dealing with cells
 * @author Dmitriy_Demchuk
 */
public interface ICellDAO extends IGenericDAO<Cell, Long>{
	/**
	 * get quantity of alive cells that contain ships
	 */
	public Long getShipAliveCells(Game game, Boolean player1);
}

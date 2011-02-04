package com.multimedia.seabattle.service.battlefield;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.multimedia.seabattle.controllers.HomeController;
import com.multimedia.seabattle.dao.IGenericDAO;
import com.multimedia.seabattle.model.beans.Cell;
import com.multimedia.seabattle.model.beans.Coordinates;
import com.multimedia.seabattle.model.beans.Game;
import com.multimedia.seabattle.model.beans.Ship;

@Service("battlefieldService")
public class BattlefieldServiceImpl implements IBattlefieldService{

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	private IGenericDAO<Cell, Long> cell_dao;

	private final int WIDTH = 10;
	private final int HEIGHT = 10;

	@Override
	public void createBattlefield(Game game) {
		for (int i=0;i<WIDTH;i++){
			for (int j=0;j<HEIGHT;j++){
				cell_dao.makePersistent(createCell(i, j, game, Boolean.TRUE));
				cell_dao.makePersistent(createCell(i, j, game, Boolean.FALSE));
			}
		}
	}

	private static final String[] ORDER_BY  = {"x", "y"};
	private static final String[] ORDER_HOW = {"ASC", "ASC"};

	@Override
	public List<Cell> getBattlefield(Game game, Boolean player1) {
		return cell_dao.getByPropertiesValuePortionOrdered(null, null,
				new String[]{"game", "player1"}, new Object[]{game, player1}, 0, 0, ORDER_BY, ORDER_HOW);
	}

	private Cell createCell(Integer x, Integer y, Game game, Boolean player1){
		Cell cell = new Cell();
		cell.setAlive(Boolean.TRUE);
		cell.setGame(game);
		cell.setPlayer1(player1);
		Coordinates coordinates = new Coordinates();
		coordinates.setX(x);
		coordinates.setY(y);
		cell.setCoordinates(coordinates);
		return cell;
	}

	@Override
	public Ship createShip(List<Cell> cells) {
		Set<Coordinates> checked_cells = new HashSet<Coordinates>();
		for (Cell cell:cells){
			Coordinates base = cell.getCoordinates();
			checked_cells.add(cell.getCoordinates());
			if (checkRight(base)){
				checked_cells.add(getCoordinate(cell.getCoordinates(), 1, 0));
			}
			if (checkLeft(base)){
				checked_cells.add(getCoordinate(cell.getCoordinates(), -1, 0));
			}
			if (checkBottom(base)){
				checked_cells.add(getCoordinate(cell.getCoordinates(), 0, 1));
			}
			if (checkTop(base)){
				checked_cells.add(getCoordinate(cell.getCoordinates(), 0, -1));
			}
			if (checkTop(base)&&checkRight(base)){
				checked_cells.add(getCoordinate(cell.getCoordinates(), 1, -1));
			}
			if (checkTop(base)&&checkLeft(base)){
				checked_cells.add(getCoordinate(cell.getCoordinates(), -1, -1));
			}
			if (checkBottom(base)&&checkRight(base)){
				checked_cells.add(getCoordinate(cell.getCoordinates(), 1, 1));
			}
			if (checkBottom(base)&&checkLeft(base)){
				checked_cells.add(getCoordinate(cell.getCoordinates(), -1, 1));
			}
		}
		Iterator<Coordinates> i = checked_cells.iterator();
		while (i.hasNext()){
			Coordinates c = i.next();
			Object o = cell_dao.getSinglePropertyU("ship.id", "coordinates", c);
			if (o==null){
				logger.info("point is empty ["+c.getX()+"]["+c.getY()+"]");
			} else {
				logger.info("point is busy ["+c.getX()+"]["+c.getY()+"]");
			}
		}
		return null;
	}

	private Coordinates getCoordinate(Coordinates base, int x, int y){
		Coordinates c = new Coordinates();
		c.setX(base.getX() + x);
		c.setY(base.getY() + y);
		return c;
	}

	/** check if this coordinate can have something in top */
	private boolean checkTop(Coordinates base){
		return base.getY()>1;
	}

	/** check if this coordinate can have something in bottom */
	private boolean checkBottom(Coordinates base){
		return base.getY()<HEIGHT-1;
	}

	/** check if this coordinate can have something in left */
	private boolean checkLeft(Coordinates base){
		return base.getX()>1;
	}

	/** check if this coordinate can have something in right */
	private boolean checkRight(Coordinates base){
		return base.getX()<WIDTH-1;
	}

	// -------------------------------- dependencies --------------------------
		@Resource(name="cellDAO")
		public void setCell_dao(IGenericDAO<Cell, Long> value){
			this.cell_dao = value;
		}
}

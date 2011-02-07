package com.multimedia.seabattle.service.battlefield;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.multimedia.seabattle.dao.IGenericDAO;
import com.multimedia.seabattle.model.beans.Cell;
import com.multimedia.seabattle.model.beans.Coordinates;
import com.multimedia.seabattle.model.beans.Game;
import com.multimedia.seabattle.model.beans.Ship;

@Service("battlefieldService")
public class BattlefieldServiceImpl implements IBattlefieldService{

	private static final Logger logger = LoggerFactory.getLogger(BattlefieldServiceImpl.class);
	private IGenericDAO<Cell, Long> cell_dao;
	private IGenericDAO<Ship, Long> ship_dao;

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
	public Ship deployShip(Coordinates[] coords, Game game, Boolean player1){
		List<Cell> cells = getCellsForCoords(coords, game, player1);

		if (cells.size()==0 || cells.size()!=coords.length || !checkPositions(cells)){
			return null;
		} else {
			Ship ship = new Ship();
			ship.setLength(cells.size());
			for (Cell cell:cells){
				cell.setShip(ship);
			}
			ship.setPlayer1(player1);
			ship.setGame(game);
	
			ship_dao.makePersistent(ship);
			ship_dao.refresh(ship);
			return ship;
		}
	}

	private List<Cell> getCellsForCoords(Coordinates[] coords, Game game, Boolean player1){
		return cell_dao.getByPropertiesValuesPortionOrdered(null, null,
				new String[]{"game", "player1", "coordinates"},
				new Object[][]{new Object[]{game}, new Object[]{player1}, coords},
				0, 0, null, null);
		//return cell_dao.getByPropertyValues(null, "coordinates", coords);
	}

	/**
	 * checks the given cells for being empty, as well as cells near them
	 * @param cells to check
	 * @return true if ship may be safely deployed here
	 */
	private boolean checkPositions(List<Cell> cells) {
		Set<Coordinates> checked_cells = new HashSet<Coordinates>();
		for (Cell cell:cells){
			Coordinates base = cell.getCoordinates();
			//checking collision with other ships
			if (checkTop(base)&&checkLeft(base)){
				checked_cells.add(getCoordinate(cell.getCoordinates(), -1, -1));
			}
			if (checkTop(base)){
				checked_cells.add(getCoordinate(cell.getCoordinates(), 0, -1));
			}
			if (checkTop(base)&&checkRight(base)){
				checked_cells.add(getCoordinate(cell.getCoordinates(), 1, -1));
			}
			if (checkLeft(base)){
				checked_cells.add(getCoordinate(cell.getCoordinates(), -1, 0));
			}
			checked_cells.add(cell.getCoordinates());
			if (checkRight(base)){
				checked_cells.add(getCoordinate(cell.getCoordinates(), 1, 0));
			}
			if (checkBottom(base)&&checkRight(base)){
				checked_cells.add(getCoordinate(cell.getCoordinates(), 1, 1));
			}
			if (checkBottom(base)){
				checked_cells.add(getCoordinate(cell.getCoordinates(), 0, 1));
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
				logger.trace("point is empty "+c.toString());
			} else {
				logger.trace("point is busy "+c.toString());
				return false;
			}
		}
		return true;
	}

	private Coordinates getCoordinate(Coordinates base, int x, int y){
		Coordinates c = new Coordinates();
		c.setX(base.getX() + x);
		c.setY(base.getY() + y);
		return c;
	}

	/** check if this coordinate can have something in top */
	private boolean checkTop(Coordinates base){
		return base.getY()>0;
	}

	/** check if this coordinate can have something in bottom */
	private boolean checkBottom(Coordinates base){
		return base.getY()<HEIGHT-1;
	}

	/** check if this coordinate can have something in left */
	private boolean checkLeft(Coordinates base){
		return base.getX()>0;
	}

	/** check if this coordinate can have something in right */
	private boolean checkRight(Coordinates base){
		return base.getX()<WIDTH-1;
	}

	@Override
	public boolean releaseShip(Ship ship) {
		cell_dao.updateObjectArrayShortByProperty(new String[]{"ship"}, new Object[]{null},
				"ship.id", new Object[]{ship.getId()});
		return true;
	}

	// -------------------------------- dependencies --------------------------
		@Resource(name="cellDAO")
		public void setCell_dao(IGenericDAO<Cell, Long> value){
			this.cell_dao = value;
		}

		@Resource(name="shipDAO")
		public void setShip_dao(IGenericDAO<Ship, Long> value){
			this.ship_dao = value;
		}
}

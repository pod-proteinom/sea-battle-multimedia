package com.multimedia.seabattle.service.battlefield;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.multimedia.seabattle.dao.IGenericDAO;
import com.multimedia.seabattle.dao.cell.ICellDAO;
import com.multimedia.seabattle.model.beans.Cell;
import com.multimedia.seabattle.model.beans.Coordinates;
import com.multimedia.seabattle.model.beans.Game;
import com.multimedia.seabattle.model.beans.Ship;
import com.multimedia.seabattle.model.types.ShipCreationResult;
import com.multimedia.seabattle.model.types.ShipType;
import com.multimedia.seabattle.model.types.ShootResult;
import com.multimedia.seabattle.service.collisions.CollisionIterator;
import com.multimedia.seabattle.service.collisions.IShipCollisionHandler;
import com.multimedia.seabattle.service.ships.IGameShips;
import com.multimedia.seabattle.service.ships.IShipGenerator;

@Service("battlefieldService")
public class BattlefieldServiceImpl implements IBattlefieldService{

	private static final Logger logger = LoggerFactory.getLogger(BattlefieldServiceImpl.class);
	private ICellDAO cell_dao;
	private IGenericDAO<Ship, Long> ship_dao;

	private IShipCollisionHandler ship_collision_handler;

	@Override
	public void createBattlefield(Game game) {
		for (int i=0;i<game.getWidth();i++){
			for (int j=0;j<game.getHeight();j++){
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
	public ShipCreationResult deployShip(Coordinates[] coords, Game game, Boolean player1, Ship ship){
		List<Cell> cells = getCellsForCoords(coords, game, player1);

		if (cells.size()==0 || cells.size()!=coords.length){
			if (logger.isDebugEnabled()){
				logger.debug("deploying ship failed in cells "+cells+" "+ShipCreationResult.NOT_EXISTING_CELL);
			}
			return ShipCreationResult.NOT_EXISTING_CELL;
		} else if (!checkPositions(coords, game, player1)) {
			if (logger.isDebugEnabled()){
				logger.debug("deploying ship failed in cells "+cells+" "+ShipCreationResult.SHIP_COLLISION);
			}
			return ShipCreationResult.SHIP_COLLISION;
		} else {
			ship_dao.makePersistent(ship);
			ship_dao.refresh(ship);
			for (Cell cell:cells){
				cell.setShip(ship);
				cell_dao.update(cell);
			}
			if (logger.isDebugEnabled()){
				logger.debug("deploying ship succeed in cells "+cells);
			}
			return ShipCreationResult.OK;
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
	private boolean checkPositions(Coordinates[] coords, Game game, Boolean player1) {
		CollisionIterator i = ship_collision_handler.getShipCoordinates(coords, game);
		while (i.hasNext()){
			Coordinates c = i.next();
			Object o = cell_dao.getSinglePropertyU("ship.id", 
					new String[]{"coordinates", "game", "player1"}, new Object[]{c, game, player1}, 0, null, null);
			if (o==null){
				logger.trace("point is empty "+c.toString());
			} else {
				logger.trace("point is busy "+c.toString());
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean releaseShip(Long ship_id) {
		cell_dao.updateObjectArrayShortByProperty(new String[]{"ship"}, new Object[]{null},
				"ship.id", new Object[]{ship_id});
		return true;
	}

	@Override
	public Long getShip(Coordinates coords, Game game, Boolean player1) {
		return (Long)cell_dao.getSinglePropertyU("ship.id",
				new String[]{"game", "player1", "coordinates"},
				new Object[]{game, player1, coords},
				0, null, null);
	}

	@Override
	public Map<Coordinates, ShipType> generateShips(Game game, IGameShips game_ships, IShipGenerator generator) {
		return generator.generateShips(game_ships, ship_collision_handler.getShipCollisions(game), game);
	}

	@Override
	public void clear(Game game, Boolean player1) {
		cell_dao.updateObjectArrayShortByProperty(new String[]{"ship"}, new Object[]{null},
				new String[]{"game", "player1"}, new Object[]{game, player1});
	}
	
	@Override
	public ShootResult shoot(Game game, Coordinates target, Boolean player1) {
		List<Cell> cells = cell_dao.getByPropertiesValuePortionOrdered(null, null,
				new String[]{"game", "coordinates", "player1", "alive"}, 
				new Object[]{game, target, player1, Boolean.TRUE},
				0, 0, null, null);
		if (cells.size()<1){
			if (logger.isDebugEnabled()){
				logger.debug("target was alerady hit: game "+game.getId()+", "+target+", player1"+player1+". wrong cells count");
			}
			return ShootResult.MISS;
		}
		Cell cell = cells.get(0);
		cell.setAlive(Boolean.FALSE);
		cell_dao.makePersistent(cell);
		if (cell.getShip()==null){
			return ShootResult.MISS;
		} else if (isShipAlive(game, player1, cell.getShip())){
			return ShootResult.HIT;
		} else {
			return ShootResult.KILL;
		}
	}

	/**
	 * checks whether the ship of a player is alive in given game
	 * @return true is alive
	 */
	private boolean isShipAlive(Game game, Boolean player1, Ship ship){
		return cell_dao
			.getRowCount(
				new String[]{"game", "ship", "player1", "alive"}, 
				new Object[]{game, ship, player1, Boolean.TRUE})
			>0;
	}

	@Override
	public boolean hasMoreShips(Game game, Boolean player1) {
		return cell_dao.getShipAliveCells(game, player1)>0;
	}

// -------------------------------- dependencies --------------------------
	@Resource(name="cellDAO")
	public void setCell_dao(ICellDAO value){
		this.cell_dao = value;
	}

	@Resource(name="shipDAO")
	public void setShip_dao(IGenericDAO<Ship, Long> value){
		this.ship_dao = value;
	}

	@Resource(name="shipCollisionHandler")
	public void setShipCollisionHandler(IShipCollisionHandler value){
		this.ship_collision_handler = value;
	}
}

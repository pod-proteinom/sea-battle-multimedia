package com.multimedia.seabattle.service.collisions;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.multimedia.seabattle.model.beans.Coordinates;
import com.multimedia.seabattle.model.types.GameShipCollision;

/**
 * defines classic ship coordinates collision, i.e.
 * no ship may be placed near current ship. radius 1.
 * @author Dmitriy_Demchuk
 */
@Service("classicShipCollision")
public class ClassicShipCollision implements IShipCollision {
	private static final Logger logger = LoggerFactory.getLogger(ClassicShipCollision.class);

	@Override
	public CollisionIterator getShipCoordinates(Coordinates[] coords) {
		return new ClassicCollisionIterator(coords);
	}

	@Override
	public GameShipCollision getShipCollisionType() {
		return GameShipCollision.CLASSIC;
	}

	/**
	 * represents iterating through classic ship collisions
	 * it is not synchronized. 
	 * @author Dmitriy_Demchuk
	 */
	class ClassicCollisionIterator implements CollisionIterator{
		private Set<Coordinates> checked_cells = new HashSet<Coordinates>();

		/**
		 * stores next collision coordinate.
		 * is undefined before first hasNext
		 */
		private Coordinates next;

		/**
		 * stores current coordinate.
		 * is undefined before first hasNext
		 */
		private Coordinates current;

		private Coordinates[] coords;

		private int number;
		/**
		 * every coordinate may collision with nearer coordinates(i.e 9 times)
		 */
		private int max_number;

		private ClassicCollisionIterator(Coordinates[] coords) {
			this.coords = coords;
			this.number = 0;
			this.max_number = 9 * coords.length;
		}

		@Override
		public boolean hasNext() {
			boolean res = (number < max_number) && (getNext()!=null);
			if (logger.isDebugEnabled()){
				logger.debug("hasNext(): cur = "+current+", next = "+next+", number = "+number);
			}
			return res;
		}

		private Coordinates getNext(){
			if (next!=null){
				return next;
			} else if (number >= max_number){
				throw new ArrayIndexOutOfBoundsException(checked_cells.size());
			}
			int num;//current item number
			Coordinates base;
			Coordinates cur;
			do{
				num = number/9;
				base = coords[num];
				switch(number%9){//current position type (left, right, left-top ...)
					case 0:
						cur = getCoordinate(base, -1, -1);
						break;
					case 1:
						cur = getCoordinate(base, 0, -1);
						break;
					case 2:
						cur = getCoordinate(base, 1, -1);
						break;
					case 3:
						cur = getCoordinate(base, -1, 0);
						break;
					case 4:
						cur = base;
						break;
					case 5:
						cur = getCoordinate(base, 1, 0);
						break;
					case 6:
						cur = getCoordinate(base, 1, 1);
						break;
					case 7:
						cur = getCoordinate(base, 0, 1);
						break;
					case 8:
						cur = getCoordinate(base, -1, 1);
						break;
					default:
						throw new NullPointerException("x%9 > 8, ...");
				}
				number++;
			} while (checked_cells.contains(cur)&&(number<max_number));
			if (cur==null||checked_cells.contains(cur)){
				return null;
			} else {
				checked_cells.add(cur);
				next = cur;
				return cur;
			}
		}

		private Coordinates getCoordinate(Coordinates base, int x, int y){
			Coordinates c = new Coordinates();
			c.setX(base.getX() + x);
			c.setY(base.getY() + y);
			return c;
		}

		@Override
		public Coordinates next() {
			if (current==null){
				current = getNext();
			}
			current = next;
			next = null;
			if (logger.isDebugEnabled()){
				logger.debug("next(): cur = "+current+", number = "+number);
			}
			return current;
		}

		//TODO: mb implement something else
		@Override
		public void reset() {
			current = null;
			next = null;
			number = 0;
			checked_cells.clear();
			
		}

		//TODO: mb implement something else
		@Override
		public Set<Coordinates> getCoordinatesSet() {
			for (Coordinates base:coords){
				checked_cells.add(getCoordinate(base, -1, -1));
				checked_cells.add(getCoordinate(base, 0, -1));
				checked_cells.add(getCoordinate(base, 1, -1));
				checked_cells.add(getCoordinate(base, -1, 0));
				checked_cells.add(base);
				checked_cells.add(getCoordinate(base, 1, 0));
				checked_cells.add(getCoordinate(base, 1, 1));
				checked_cells.add(getCoordinate(base, 0, 1));
				checked_cells.add(getCoordinate(base, -1, 1));
			}
			return checked_cells;
		}

	}

}

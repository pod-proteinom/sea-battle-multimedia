package com.multimedia.seabattle.service.collisions;

import java.util.Set;

import com.multimedia.seabattle.model.beans.Coordinates;

/**
 * iterates over collection of coordinates, that represent collisions
 * i.e. may not be occupied by other ships
 * @author Dmitriy_Demchuk
 */
public interface CollisionIterator {
	/**
	 * whether this iterator has more collisions
	 * @return true if has more
	 */
	public boolean hasNext();

	/**
	 * get next collision coordinate
	 * @return coordinates of next collision
	 */
	public Coordinates next();

	/**
	 * resets this iterator, so that next now returns first element
	 */
	public void reset();

	/**
	 * this method is backed by the iterator, so all changes to it may affect iterator behavior.
	 * after calling this method iterator is reseted. (you may reset it manually)
	 * @return all collisions coordinates
	 */
	public Set<Coordinates> getCoordinatesSet();
}

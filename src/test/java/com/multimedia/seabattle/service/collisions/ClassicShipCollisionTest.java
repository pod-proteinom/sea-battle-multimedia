package com.multimedia.seabattle.service.collisions;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.multimedia.seabattle.model.beans.Coordinates;
import com.multimedia.seabattle.model.types.GameShipCollision;
import com.multimedia.seabattle.service.collisions.ClassicShipCollision;
import com.multimedia.seabattle.service.collisions.CollisionIterator;

import static org.junit.Assert.*;

public class ClassicShipCollisionTest {
	private ClassicShipCollision obj;

	@Before
	public void prepare(){
		obj = new ClassicShipCollision();
	}

	@Test
	public void testType(){
		assertEquals("the classic ship collision MUST GameShipCollision.CLASSIC", GameShipCollision.CLASSIC, obj.getShipCollisionType());
	}

	/**
	 * create a ship with one coordinate and assert its coordinates.
	 * test how collision iterator works using hasNext and next
	 */
	@Test
	public void testCollisionIteratorTINY(){
		Set<Coordinates> collisions = new HashSet<Coordinates>();
		for (int i=0;i<3;i++)
			for (int j=0;j<3;j++)
				collisions.add(new Coordinates(i, j));

		Coordinates[] coords = new Coordinates[]{new Coordinates(1, 1)};
		CollisionIterator i = obj.getShipCoordinates(coords);
		int size = 0;
		while (i.hasNext()){
			Coordinates c = i.next();
			assertTrue("the coord "+c+" was not removed", collisions.remove(c));
			size++;
		}
		assertEquals("size does not match", 9, size);
		assertEquals("does not contain some coordinates", 0, collisions.size());
	}

	/**
	 * create a ship with two coordinates horizontal and assert its coordinates.
	 * test how collision iterator works using hasNext and next
	 */
	@Test
	public void testCollisionIteratorSMALL_H(){
		Set<Coordinates> collisions = new HashSet<Coordinates>();
		for (int i=0;i<4;i++)
			for (int j=0;j<3;j++)
				collisions.add(new Coordinates(i, j));

		Coordinates[] coords = new Coordinates[]{new Coordinates(1, 1), new Coordinates(2, 1)};
		CollisionIterator i = obj.getShipCoordinates(coords);
		int size = 0;
		while (i.hasNext()){
			Coordinates c = i.next();
			assertTrue("the coord "+c+" was not removed", collisions.remove(c));
			size++;
		}
		assertEquals("size does not match", 12, size);
		assertEquals("does not contain some coordinates", 0, collisions.size());
	}

	/**
	 * create a ship with two coordinates vertical and assert its coordinates.
	 * test how collision iterator works using hasNext and next
	 */
	@Test
	public void testCollisionIteratorSMALL_V(){
		Set<Coordinates> collisions = new HashSet<Coordinates>();
		for (int i=0;i<3;i++)
			for (int j=0;j<4;j++)
				collisions.add(new Coordinates(i, j));

		Coordinates[] coords = new Coordinates[]{new Coordinates(1, 1), new Coordinates(1, 2)};
		CollisionIterator i = obj.getShipCoordinates(coords);
		int size = 0;
		while (i.hasNext()){
			Coordinates c = i.next();
			assertTrue("the coord "+c+" was not removed", collisions.remove(c));
			size++;
		}
		assertEquals("size does not match", 12, size);
		assertEquals("does not contain some coordinates", 0, collisions.size());
	}

	/**
	 * create a ship with two coordinates vertical
	 * and try to reset iterator after some iterations
	 */
	@Test
	public void testCollisionIteratorReset(){
		Set<Coordinates> collisions = new HashSet<Coordinates>();
		for (int i=0;i<3;i++)
			for (int j=0;j<4;j++)
				collisions.add(new Coordinates(i, j));

		Coordinates[] coords = new Coordinates[]{new Coordinates(1, 1), new Coordinates(1, 2)};
		CollisionIterator i = obj.getShipCoordinates(coords);
		int size = 0;
		while (i.hasNext() && size<5){
			i.next();
			size++;
		}

		i.reset();
		size = 0;
		while (i.hasNext()){
			Coordinates c = i.next();
			assertTrue("the coord "+c+" was not removed", collisions.remove(c));
			size++;
		}
		assertEquals("size does not match", 12, size);
		assertEquals("does not contain some coordinates", 0, collisions.size());
	}

	/**
	 * create a ship with two coordinates vertical
	 * and try to get a set of collision coordinates
	 */
	@Test
	public void testCollisionIteratorSet(){
		Set<Coordinates> collisions = new HashSet<Coordinates>();
		for (int i=0;i<3;i++)
			for (int j=0;j<4;j++)
				collisions.add(new Coordinates(i, j));

		Coordinates[] coords = new Coordinates[]{new Coordinates(1, 1), new Coordinates(1, 2)};
		Set<Coordinates> i = obj.getShipCoordinates(coords).getCoordinatesSet();
		assertEquals("size does not match", 12, i.size());
		
		assertTrue("nothing was deleted from the set", collisions.removeAll(i));
		assertEquals("does not contain some coordinates", 0, collisions.size());
	}
}

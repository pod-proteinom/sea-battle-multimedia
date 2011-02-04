/*******************************************************************************
 * Copyright (c) 2011 demchuck.dima@gmail.com.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     demchuck.dima@gmail.com - initial API and implementation
 ******************************************************************************/
package com.multimedia.seabattle.model.beans;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Cell {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@org.hibernate.annotations.ForeignKey(name="FK_cell_game")
	@JoinColumn(name="id_game")
	private Game game;

	@ManyToOne
	@org.hibernate.annotations.ForeignKey(name="FK_cell_ship")
	@JoinColumn(name="id_ship")
	private Ship ship;

	private Integer x;
	private Integer y;

	/** indicates that player1 owns it, else player2 is owner */
	private Boolean player1;
	/** indicates that it is not shoot (if true)*/
	private Boolean alive;
	/** indicates that player1 owns it, else player2 is owner */
	public void setPlayer1(Boolean player1) {
		this.player1 = player1;
	}
	/** indicates that player1 owns it, else player2 is owner */
	public Boolean getPlayer1() {
		return player1;
	}
	/** indicates that player1 owns it, else player2 is owner */
	public void setAlive(Boolean alive) {
		this.alive = alive;
	}
	/** indicates that player1 owns it, else player2 is owner */
	public Boolean getAlive() {
		return alive;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return id;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	public Game getGame() {
		return game;
	}
	public void setShip(Ship ship) {
		this.ship = ship;
	}
	public Ship getShip() {
		return ship;
	}
	public void setX(Integer x) {
		this.x = x;
	}
	public Integer getX() {
		return x;
	}
	public void setY(Integer y) {
		this.y = y;
	}
	public Integer getY() {
		return y;
	}

}

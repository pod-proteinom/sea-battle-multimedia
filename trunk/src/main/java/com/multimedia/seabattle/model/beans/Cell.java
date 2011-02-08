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

import javax.persistence.AttributeOverrides;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Cell {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@org.hibernate.annotations.ForeignKey(name="FK_cell_game")
	@JoinColumn(name="id_game")
	@OnDelete(action=OnDeleteAction.CASCADE)
	private Game game;

	@ManyToOne
	@org.hibernate.annotations.ForeignKey(name="FK_cell_ship")
	@JoinColumn(name="id_ship")
	private Ship ship;

	@Embedded
	@AttributeOverrides( {
           @AttributeOverride(name="x", column = @Column(name="x")) ,
           @AttributeOverride(name="y", column = @Column(name="y")) 
   } )
	private Coordinates coordinates;

	/** indicates that player1 owns it, else player2 is owner */
	@NotNull
	private Boolean player1;
	/** indicates that it is not shoot (if true)*/
	@NotNull
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
	public void setCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
	}
	public Coordinates getCoordinates() {
		return coordinates;
	}

}

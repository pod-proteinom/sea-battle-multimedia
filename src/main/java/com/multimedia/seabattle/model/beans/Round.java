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

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@NamedQuery(
		name="lastRound",
		query="from com.multimedia.seabattle.model.beans.Round where game = :game and number = (select max(number) from com.multimedia.seabattle.model.beans.Round where game = :game)")
public class Round {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@org.hibernate.annotations.ForeignKey(name="FK_round_game")
	@JoinColumn(name="id_game")
	@OnDelete(action=OnDeleteAction.CASCADE)
	private Game game;

	@Embedded
	@AttributeOverrides( {
           @AttributeOverride(name="x", column = @Column(name="x")) ,
           @AttributeOverride(name="y", column = @Column(name="y")) 
   } )
	private Coordinates coordinates;

	private Integer number;

	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(updatable=false)
	private Date started;

	/** indicates that player1 is shooting this round, else player2 */
	private Boolean player1;

	/** indicates that player1 is shooting this round, else player2 */
	public void setPlayer1(Boolean player1) {
		this.player1 = player1;
	}

	/** indicates that player1 is shooting this round, else player2 */
	public Boolean getPlayer1() {
		return player1;
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

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getNumber() {
		return number;
	}

	public void setStarted(Date started) {
		this.started = started;
	}

	public Date getStarted() {
		return started;
	}

	public void setCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
	}

	public Coordinates getCoordinates() {
		return coordinates;
	}

}

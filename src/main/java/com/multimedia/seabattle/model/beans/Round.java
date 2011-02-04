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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
public class Round {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@org.hibernate.annotations.ForeignKey(name="FK_round_game")
	@JoinColumn(name="id_game")
	private Game game;

	@ManyToOne
	@org.hibernate.annotations.ForeignKey(name="FK_round_cell")
	@JoinColumn(name="id_cell")
	private Cell cell;

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

	public void setCell(Cell cell) {
		this.cell = cell;
	}

	public Cell getCell() {
		return cell;
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

}

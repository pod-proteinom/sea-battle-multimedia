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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.multimedia.seabattle.model.types.GameShipType;

@Entity
public class Game {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	private String player1;
	private String player2;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable=false)
	private Date started;
	private Date ended;

	private Boolean ready1;
	private Boolean ready2;

	/** indicates that player 1 has won the game, else player 2 */
	private Boolean win1;

	//TODO: make it not transient when more game types will be available
	/** just for test. because we now have only one game type */
	@Transient
	private final GameShipType type = GameShipType.CLASSIC;

	public void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return id;
	}
	public void setPlayer1(String player1) {
		this.player1 = player1;
	}
	public String getPlayer1() {
		return player1;
	}
	public void setPlayer2(String player2) {
		this.player2 = player2;
	}
	public String getPlayer2() {
		return player2;
	}
	public void setStarted(Date started) {
		this.started = started;
	}
	public Date getStarted() {
		return started;
	}
	public void setEnded(Date ended) {
		this.ended = ended;
	}
	public Date getEnded() {
		return ended;
	}
	public void setReady1(Boolean ready1) {
		this.ready1 = ready1;
	}
	public Boolean getReady1() {
		return ready1;
	}
	public void setReady2(Boolean ready2) {
		this.ready2 = ready2;
	}
	public Boolean getReady2() {
		return ready2;
	}
	/** indicates that player 1 has won the game, else player 2 */
	public void setWin1(Boolean win1) {
		this.win1 = win1;
	}
	/** indicates that player 1 has won the game, else player 2 */
	public Boolean getWin1() {
		return win1;
	}
	@Override
	public String toString() {
		return "Game [id=" + id + ", player1=" + player1 + ", player2="
				+ player2 + ", started=" + started + ", ended=" + ended
				+ ", ready1=" + ready1 + ", ready2=" + ready2 + ", win1="
				+ win1 + "]";
	}

	public GameShipType getType() {
		return type;
	}

}

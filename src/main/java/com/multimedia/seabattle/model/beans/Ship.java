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

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Ship {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	private Integer length;

	@ManyToOne
	@org.hibernate.annotations.ForeignKey(name="FK_ship_game")
	@JoinColumn(name="id_game")
	@OnDelete(action=OnDeleteAction.CASCADE)
	private Game game;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getLength() {
		return length;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Game getGame() {
		return game;
	}

}

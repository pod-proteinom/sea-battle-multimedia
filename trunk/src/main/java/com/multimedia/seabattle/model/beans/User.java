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
public class User extends com.multimedia.security.beans.User{

	public User(){
		super();
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

	private String name;
	private String surname;

	@ManyToOne
	@org.hibernate.annotations.ForeignKey(name="FK_user_country")
	@JoinColumn(name="id_country")
	private Country country;

	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(updatable=false)
	private Date date;

	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getSurname() {
		return surname;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Country getCountry() {
		return country;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}
	/**
	 * copies only simple types i.e. strings, long ...
	 * @return
	 */
	public Object clone(){
		User o = new User();
		o.setEmail(this.getEmail());
		o.setId(this.getId());
		//o.id_pages = this.id_pages;
		o.setLogin(this.getLogin());
		//o.new_passwords = this.new_passwords;
		//o.pages = this.pages;
		o.setPassword(this.getPassword());
		o.setPassword_repeat(this.getPassword_repeat());
		//o.roles = this.roles;
		return o;
	}
}

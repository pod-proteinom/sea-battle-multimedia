package com.multimedia.seabattle.model.beans;

import java.util.Date;

/**
 * represents a ticket(preparation for a game).
 * @author Dmitriy_Demchuk
 *
 */
public class Ticket {

	private Long id_user;

	private Date received;

	public void setId_user(Long id_user) {
		this.id_user = id_user;
	}

	public Long getId_user() {
		return id_user;
	}

	public void setReceived(Date received) {
		this.received = received;
	}

	public Date getReceived() {
		return received;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id_user == null) ? 0 : id_user.hashCode());
		result = prime * result
				+ ((received == null) ? 0 : received.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ticket other = (Ticket) obj;
		if (id_user == null) {
			if (other.id_user != null)
				return false;
		} else if (!id_user.equals(other.id_user))
			return false;
		if (received == null) {
			if (other.received != null)
				return false;
		} else if (!received.equals(other.received))
			return false;
		return true;
	}

}

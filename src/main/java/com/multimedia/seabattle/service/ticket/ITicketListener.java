package com.multimedia.seabattle.service.ticket;

/**
 * an interface to implement if you want to be notified when a ticket is used.
 * (i.e. removed from the waiting)
 * @author Dmitriy_Demchuk
 */
public interface ITicketListener {
	/**
	 * indicates that a ticket for a given user was removed from the storage(used)
	 * @param id_owner an id of user that owns the ticket
	 * @param id_user an id of user that will be an oponent
	 */
	public void ticketRemoved(Long id_owner, Long id_oponent);
}

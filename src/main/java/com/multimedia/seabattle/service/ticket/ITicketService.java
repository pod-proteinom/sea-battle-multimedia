package com.multimedia.seabattle.service.ticket;

import com.multimedia.seabattle.model.beans.Ticket;
import com.multimedia.seabattle.model.beans.User;

/**
 * an interface for starting the game between to users
 * @author Dmitriy_Demchuk
 *
 */
public interface ITicketService {

	/**
	 * creates a ticket for this user
	 * @return a ticket for a game with another player or null
	 */
	public Ticket getTicket(User user);

	/**
	 * cancels all tickets for given player
	 */
	public void cancelAllTickets(User user);

	/**
	 * get total tickets count
	 */
	public int getTicketsCount();

	/**
	 * register new listener
	 */
	public void registerListener(ITicketListener listener);
	
}
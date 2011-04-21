package com.multimedia.seabattle.service.ticket;

import org.junit.Before;
import org.junit.Test;

import com.multimedia.seabattle.model.beans.Ticket;
import com.multimedia.seabattle.model.beans.User;

import static org.junit.Assert.*;

public class TicketServiceTest {
	private TicketServiceImpl ticketService;
	
	private static long id = 0L;

	@Before
	public void init(){
		ticketService = new TicketServiceImpl();
	}

	private synchronized User getUser(){
		User user = new User();
		user.setId(id++);
		return user;
	}

	@Test
	public void testGetTicket(){
		User user = getUser();
		assertNull("There were no tickets", ticketService.getTicket(user));
		assertEquals("only one user has got a ticket", 1, ticketService.getTicketsCount());
	}

	@Test
	public void testReceiveTicket(){
		User user1 = getUser();
		User user2 = getUser();
		ticketService.getTicket(user1);
		Ticket ticket = ticketService.getTicket(user2);
		assertNotNull("There was a ticket", ticket);
		assertEquals("Unknown user in ticket list", user1.getId(), ticket.getId_user());
		assertEquals("All tickets must be used", 0, ticketService.getTicketsCount());
	}

	@Test
	public void testRemoveTicket(){
		User user = getUser();
		assertNull("There were no tickets", ticketService.getTicket(user));
		ticketService.cancelAllTickets(user);
		assertEquals("only one user has got a ticket", 0, ticketService.getTicketsCount());
	}
}

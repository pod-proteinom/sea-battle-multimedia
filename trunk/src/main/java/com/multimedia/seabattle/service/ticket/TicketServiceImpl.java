package com.multimedia.seabattle.service.ticket;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.multimedia.seabattle.model.beans.Ticket;
import com.multimedia.seabattle.model.beans.User;

/**
 * Implements ticket service with a HashSet that actually stores tickets.
 * @author Dmitriy_Demchuk
 */
@Service(value="ticketService")
public class TicketServiceImpl implements ITicketService{

	private Map<Long, Ticket> tickets = new HashMap<Long, Ticket>();
	private List<ITicketListener> listeners =
		java.util.Collections.synchronizedList(new ArrayList<ITicketListener>());

	@Override
	public Ticket getTicket(User user) {
		Ticket ticket = null;
		synchronized (tickets) {
			Long id_user = null;
			if (!tickets.isEmpty()){
				Iterator<Long> ticket_iterator = tickets.keySet().iterator();
				id_user = ticket_iterator.next();
				if (id_user.equals(user.getId())){
					if (ticket_iterator.hasNext()){
						id_user = ticket_iterator.next();
					} else {
						//means that the only player waiting is he
						id_user = null;
					}
				} 
			}
			if (id_user==null){
				tickets.put(user.getId(), generateTicket(user));
			} else {
				ticket = tickets.remove(id_user);
			}
		}
		if (ticket!=null){
			//notify all the listeners
			for (ITicketListener listener:listeners){
				listener.ticketRemoved(ticket.getId_user(), user.getId());
			}
		}

		return ticket;
	}

	private Ticket generateTicket(User user){
		Ticket ticket = new Ticket();
		ticket.setId_user(user.getId());
		ticket.setReceived(new Date());
		return ticket;
	}

	@Override
	public void cancelAllTickets(User user) {
		synchronized (tickets) {
			tickets.remove(user.getId());
		}
	}

	@Override
	public int getTicketsCount() {
		return tickets.size();
	}
	
	@Override
	public synchronized void registerListener(ITicketListener listener) {
		//TODO: check whether this listener is duplicate
		listeners.add(listener);
	}

}

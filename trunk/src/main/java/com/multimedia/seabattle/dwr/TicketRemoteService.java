package com.multimedia.seabattle.dwr;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.ui.dwr.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;

import com.multimedia.seabattle.model.beans.User;
import com.multimedia.seabattle.service.ticket.ITicketListener;
import com.multimedia.seabattle.service.ticket.ITicketService;

@Service("TicketRemoteService")
@RemoteProxy(name="TicketRemoteService")
public class TicketRemoteService implements ITicketListener, InitializingBean{
	private static final Logger logger = LoggerFactory.getLogger(TicketRemoteService.class);

	private ScheduledThreadPoolExecutor executor;
	private Timer timer;

	@Override
    public void afterPropertiesSet() {
    	if (logger.isDebugEnabled()){
    		logger.debug("initialized [TicketRemoteService] service");
    	}
    	timer = new Timer(this);
        executor.scheduleAtFixedRate(timer, 1, 1, TimeUnit.SECONDS);
    }

    @RemoteMethod
    public void waitForPlayer(){
    	ScriptSession scriptSession = WebContextFactory.get().getScriptSession();
    	HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();

    	User user = (User)com.multimedia.security.Utils.getCurrentUser(request);
    	if (user==null){
        	if (logger.isDebugEnabled()){
        		logger.debug("trying to start a round for a null user");
        	}
    	} else {
        	if (logger.isDebugEnabled()){
        		logger.debug("user ["+user.getId()+"] starts waiting");
        	}
    		timer.startWaiting(scriptSession.getId(), user.getId());
    	}
    }

    /**
	 * displays the time that client waits for a game
     */
    public void setClockDisplay(final String sessionId, final int output) {
        Browser.withSession(sessionId, new Runnable() {
            public void run() {
                Util.setValue("waitTimer", output);
            }
        });
    }

    @Override
    public void ticketRemoved(final Long id_owner, final Long id_oponent) {
    	UserTime ut = timer.endWaiting(id_owner);
    	if (logger.isDebugEnabled()){
    		logger.debug("game between user ["+id_owner+"] and ["+id_oponent+"]");
    	}

    	//Browser.withPageFiltered("/seabattle/game/player.htm", new IdScriptSessionFilter(ut.getSessionId()), new Runnable() {
        Browser.withSession(ut.getSessionId(), new Runnable() {
            public void run() {
            	for (ScriptSession sc:Browser.getTargetSessions()){
            		ScriptBuffer sb = new ScriptBuffer();
            		sb.appendCall("oponentFound");
            		sc.addScript(sb);
                	if (logger.isDebugEnabled()){
                		logger.debug("end refreshing ["+id_owner+"]");
                	}
            	}
            }
        });
    }
//--------------------------------------------------------------------------------------
    @Required
    @Resource(name="executor")
    public void setExecutor(ScheduledThreadPoolExecutor executor) {
		this.executor = executor;
	}

    @Required
    @Resource(name="ticketService")
    public void setTicketService(ITicketService ticketService) {
		ticketService.registerListener(this);
	}
}

class Timer implements Runnable{
	private static final Logger logger = LoggerFactory.getLogger(Timer.class);

	private Hashtable<Long, UserTime> rounds = new Hashtable<Long, UserTime>();
	private TicketRemoteService ticketService;

	public Timer(TicketRemoteService value){
		this.ticketService = value;
	}

    //protected transient boolean active = false;

    public synchronized void run() {
    	if (logger.isDebugEnabled()){
    		logger.debug("refreshing "+rounds.size()+" timers");
    	}

    	Iterator<Entry<Long, UserTime>> iterator = rounds.entrySet().iterator();
    	while (iterator.hasNext()){
    		Entry<Long, UserTime> i = iterator.next();
            ticketService.setClockDisplay(i.getValue().getSessionId(), i.getValue().incrementAndGet());
    	}
    }

    /**
     * adds a new round to the timer with given sessionId
     */
    public synchronized void startWaiting(String sessionId, Long id_user){
    	if (logger.isDebugEnabled()){
    		logger.debug("start waiting for user ["+id_user+"]");
    	}
    	rounds.put(id_user, new UserTime(sessionId));
    }

    /**
     * end waiting for user with given id
     */
    public synchronized UserTime endWaiting(Long id_user){
    	if (logger.isDebugEnabled()){
    		logger.debug("end waiting for user ["+id_user+"]");
    	}
    	return rounds.remove(id_user);
    }
}

class UserTime{
	private int time;
	private String sessionId;

	protected UserTime(String sessionId) {
		this.time = 0;
		this.sessionId = sessionId;
	}

	protected int incrementAndGet(){
		return time++;
	}

	protected String getSessionId(){
		return sessionId;
	}
}

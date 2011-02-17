package com.multimedia.seabattle.dwr;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.impl.DaemonThreadFactory;
import org.directwebremoting.ui.dwr.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("RoundTimer")
@RemoteProxy(name="RoundTimer")
public class RoundTimer{
	private static final Logger logger = LoggerFactory.getLogger(RoundTimer.class);

	private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, new DaemonThreadFactory());
	private Timer timer;

    public RoundTimer() {
    	timer = new Timer(this);
        executor.scheduleAtFixedRate(timer, 1, 1, TimeUnit.SECONDS);
    }

    /**
     * Actually alter the clients.
     * @param output The string to display.
     */
    public void setClockDisplay(final String sessionId, final String output) {
    	logger.debug("refreshing time in rounds for ["+sessionId+"] time ["+output+"]");
    	//TODO: add real context here
        String page = "/seabattle/resources/RoundTimer.html";
        Browser.withPageFiltered(page, new AttributeScriptSessionFilter(sessionId), new Runnable() {
            public void run() {
                Util.setValue("roundTime", output);
            }
        });
    }

    @RemoteMethod
    public void startNewRound(final String sessionId){
    	logger.debug("starting new round for ["+sessionId+"]");
    	timer.startRound(sessionId);

    	ScriptSession scriptSession = WebContextFactory.get().getScriptSession();
        scriptSession.setAttribute(AttributeScriptSessionFilter.SESSION_ATTRIBUTE, sessionId);
    }

}

class AttributeScriptSessionFilter implements ScriptSessionFilter
{
	public final static String SESSION_ATTRIBUTE = "session-id";

    private final String sessionId;

    public AttributeScriptSessionFilter(String sessionId)
    {
        this.sessionId = sessionId;
    }

    @Override
    public boolean match(ScriptSession session)
    {
        Object check = session.getAttribute(SESSION_ATTRIBUTE);
        return (check != null && check.equals(sessionId));
    }
}

class Timer implements Runnable{
	private static final Logger logger = LoggerFactory.getLogger(Timer.class);

	private Hashtable<String, AtomicInteger> rounds = new Hashtable<String, AtomicInteger>();
	private RoundTimer roundTimer;
	private transient int seconds = 30;

	public Timer(RoundTimer roundTimer){
		logger.debug("created Timer");
		this.roundTimer = roundTimer;
	}

    //protected transient boolean active = false;

    public synchronized void run() {
    	if (logger.isDebugEnabled()){
    		logger.debug("refreshing "+rounds.size()+" timers");
    	}

    	Iterator<Entry<String, AtomicInteger>> iterator = rounds.entrySet().iterator();
    	while (iterator.hasNext()){
    		Entry<String, AtomicInteger> i = iterator.next();
            if (i.getValue().get()<=0){
            	if (logger.isDebugEnabled()){
            		logger.debug("removing round ["+i.getKey()+"]");
            	}
        		roundTimer.setClockDisplay(i.getKey(), "your time expired !!!");
        		iterator.remove();
            } else {
        		roundTimer.setClockDisplay(i.getKey(), "time left:" + i.getValue().decrementAndGet());
            }
    	}
    }

    /**
     * adds a new round to the timer with given sessionId
     */
    public synchronized void startRound(String sessionId){
    	if (logger.isDebugEnabled()){
    		logger.debug("adding round ["+sessionId+"]");
    	}
    	rounds.put(sessionId, new AtomicInteger(seconds));
    }

    /**
     * adds a new round to the timer with given sessionId
     */
    public synchronized void endRound(String sessionId){
    	if (logger.isDebugEnabled()){
    		logger.debug("ending round ["+sessionId+"]");
    	}
    	rounds.remove(sessionId);
    }

    /**
     * Called from the client to turn the clock on/off
     */
    /*public synchronized void toggle() {
        active = !active;

        if (active) {
            setClockDisplay("Started");
        }
        else {
            setClockDisplay("Stopped");
        }
    }*/
}

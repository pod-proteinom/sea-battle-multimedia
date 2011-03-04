package com.multimedia.seabattle.dwr;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.ui.dwr.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.multimedia.seabattle.dwr.util.AttributeIdScriptSessionFilter;

//@Service("RoundTimer")
//@RemoteProxy(name="RoundTimer")
public class RoundTimer{
	private static final Logger logger = LoggerFactory.getLogger(RoundTimer.class);

	private ScheduledThreadPoolExecutor executor;
	private WaitTimer timer;

	@PostConstruct
    public void afterPropertiesSet() {
    	timer = new WaitTimer(this);
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
        Browser.withPageFiltered(page, new AttributeIdScriptSessionFilter(sessionId), new Runnable() {
            public void run() {
                Util.setValue("roundTime", output);
            }
        });
    }

    @RemoteMethod
    public void startNewRound(){
    	ScriptSession scriptSession = WebContextFactory.get().getScriptSession();
    	if (logger.isDebugEnabled()){
    		logger.debug("starting new round for ["+scriptSession.getId()+"]");
    	}
    	timer.startRound(scriptSession.getId());

        scriptSession.setAttribute(AttributeIdScriptSessionFilter.SESSION_ATTRIBUTE, scriptSession.getId());
    }

//--------------------------------------------------------------------------------------
    @Required
    @Resource(name="executor")
    public void setExecutor(ScheduledThreadPoolExecutor executor) {
		this.executor = executor;
	}

}

class WaitTimer implements Runnable{
	private static final Logger logger = LoggerFactory.getLogger(Timer.class);

	private Hashtable<String, AtomicInteger> rounds = new Hashtable<String, AtomicInteger>();
	private RoundTimer roundTimer;
	private transient int seconds = 30;

	public WaitTimer(RoundTimer roundTimer){
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
}

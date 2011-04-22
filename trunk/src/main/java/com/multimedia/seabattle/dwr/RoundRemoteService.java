package com.multimedia.seabattle.dwr;

import java.util.Hashtable;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;

import com.multimedia.seabattle.model.beans.TurnResult;
import com.multimedia.seabattle.model.beans.User;
import com.multimedia.seabattle.service.game.IGameService;
import com.multimedia.seabattle.service.game.IRoundListener;

/**
 * this class calls a javascript function when round ends
 * @author Dmitriy_Demchuk
 */
@Service("RoundRemoteService")
@RemoteProxy(name="RoundRemoteService")
public class RoundRemoteService implements IRoundListener {
	private static final Logger logger = LoggerFactory.getLogger(RoundRemoteService.class);

	private Hashtable<String, String> user_sessions = new Hashtable<String, String>();
    @RemoteMethod
    public void waitRound() {
    	ScriptSession scriptSession = WebContextFactory.get().getScriptSession();
    	HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();

    	User user = (User)com.multimedia.security.Utils.getCurrentUser(request);
    	if (user==null){
        	if (logger.isDebugEnabled()) {
        		logger.debug("trying to start waiting for a null user");
        	}
    	} else {
        	if (logger.isDebugEnabled()) {
        		logger.debug("user ["+user.getId()+"] starts waiting for another player to deploy ships");
        	}
    		startWaiting(scriptSession.getId(), user.getLogin());
    	}
    }

	@Override
	public void round(final String name, final TurnResult tr) {
		String id_session = getSessionId(name);
		if (id_session==null)
			return;
    	if (logger.isDebugEnabled()){
    		logger.debug("new round for ["+name+"] player1 ["+tr.getShootResult()+"]");
    	}
    	//Browser.withPageFiltered("/seabattle/game/player.htm", new IdScriptSessionFilter(ut.getSessionId()), new Runnable() {
        Browser.withSession(id_session, new Runnable() {
            public void run() {
            	for (ScriptSession sc:Browser.getTargetSessions()){
            		ScriptBuffer sb = new ScriptBuffer();
            		sb.appendCall("myRound");
            		switch (tr.getShootResult()){
	            		case HIT:
	            			sb.appendCall("hit");
	            			break;
	            		case KILL:
	            			sb.appendCall("kill");
	            			break;
	            		case MISS:
	            			sb.appendCall("miss");
	            			break;
            		}
            		sc.addScript(sb);
            	}
            }
        });
	}

	@Override
	public void wait(final String name, final TurnResult tr) {
		String id_session = getSessionId(name);
		if (id_session==null)
			return;
    	if (logger.isDebugEnabled()){
    		logger.debug("new round for ["+name+"] player1 ["+tr.getShootResult()+"]");
    	}
    	//Browser.withPageFiltered("/seabattle/game/player.htm", new IdScriptSessionFilter(ut.getSessionId()), new Runnable() {
        Browser.withSession(id_session, new Runnable() {
            public void run() {
            	for (ScriptSession sc:Browser.getTargetSessions()){
            		ScriptBuffer sb = new ScriptBuffer();
            		sb.appendCall("hisRound");
            		switch (tr.getShootResult()){
	            		case HIT:
	            			sb.appendCall("hit");
	            			break;
	            		case KILL:
	            			sb.appendCall("kill");
	            			break;
	            		case MISS:
	            			sb.appendCall("miss");
	            			break;
            		}
            		sc.addScript(sb);
            	}
            }
        });
	}

	@Override
	public void win(String name) {
		String id_session = getSessionId(name);
		if (id_session==null)
			return;
		Browser.withSession(id_session, new Runnable() {
            public void run() {
            	for (ScriptSession sc:Browser.getTargetSessions()){
            		ScriptBuffer sb = new ScriptBuffer();
            		sb.appendCall("win");
            		sc.addScript(sb);
            	}
            }
        });
	}

	@Override
	public void loose(String name) {
		String id_session = getSessionId(name);
		if (id_session==null)
			return;
		Browser.withSession(id_session, new Runnable() {
            public void run() {
            	for (ScriptSession sc:Browser.getTargetSessions()){
            		ScriptBuffer sb = new ScriptBuffer();
            		sb.appendCall("loose");
            		sc.addScript(sb);
            	}
            }
        });
	}

	/**
	 * Register session of a user waiting for an opponent.
	 * @param id_session session bound to given user. 
	 * @param name name of a user that is waiting.
	 */
	private synchronized void startWaiting(String id_session, String name) {
		user_sessions.put(name, id_session);
	}

	/**
	 * Register session of a user waiting for an opponent.
	 * @param id_session session bound to given user. 
	 * @param name name of a user that is waiting.
	 */
	private synchronized String getSessionId(String name) {
		return user_sessions.get(name);
	}

//--------------------------------------------------------------------------------------
	@Required
	@Resource(name="gameService")
	public void setGameService(IGameService value) {
		value.registerRoundListener(this);
	}

}

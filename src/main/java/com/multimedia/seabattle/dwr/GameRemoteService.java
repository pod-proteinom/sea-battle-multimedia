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

import com.multimedia.seabattle.model.beans.User;
import com.multimedia.seabattle.service.game.IGameListener;
import com.multimedia.seabattle.service.game.IGameService;

/**
 * this class calls a javascript function when a player's opponent is ready for a game.
 * @author Dmitriy_Demchuk
 */
@Service("GameRemoteService")
@RemoteProxy(name="GameRemoteService")
public class GameRemoteService implements IGameListener {
	private static final Logger logger = LoggerFactory.getLogger(GameRemoteService.class);

	private Hashtable<String, String> user_sessions = new Hashtable<String, String>();

    @RemoteMethod
    public void waitForPlayer() {
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
	public void playerReady(final String name, final String opponent) {
		String id_session = endWaiting(opponent);
		if (id_session==null)
			return;
    	//Browser.withPageFiltered("/seabattle/game/player.htm", new IdScriptSessionFilter(ut.getSessionId()), new Runnable() {
        Browser.withSession(id_session, new Runnable() {
            public void run() {
            	for (ScriptSession sc:Browser.getTargetSessions()){
            		ScriptBuffer sb = new ScriptBuffer();
            		sb.appendCall("opponentReady");
            		sc.addScript(sb);
                	if (logger.isDebugEnabled()){
                		logger.debug("opponent ["+opponent+"] ready for a user ["+name+"]");
                	}
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
	private synchronized String endWaiting(String name) {
		return user_sessions.remove(name);
	}

//--------------------------------------------------------------------------------------
	@Required
	@Resource(name="gameService")
	public void setGameService(IGameService value) {
		value.registerGameListener(this);
	}
}

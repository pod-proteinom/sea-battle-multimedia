package com.multimedia.seabattle.dwr.util;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;

/**
 * searches a session with given id
 * @author Dmitriy_Demchuk
 *
 */
public class AttributeIdScriptSessionFilter implements ScriptSessionFilter {
	/**
	 * attribute in which session id will be stored
	 */
	public final static String SESSION_ATTRIBUTE = "session-id";

    private final String sessionId;

    public AttributeIdScriptSessionFilter(String sessionId){
        this.sessionId = sessionId;
    }


	@Override
	public boolean match(ScriptSession session) {
        return (session.getId().equals(sessionId));
	}

}

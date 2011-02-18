package com.multimedia.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.multimedia.security.beans.Role;
import com.multimedia.security.beans.User;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class Utils {
	private Utils() {}

	public static User getCurrentUser(HttpServletRequest req){
		return (User)req.getAttribute(Config.USER_ATTRIBUTE);
	}

	public static User getCurrentUser(HttpSession sess){
		return (User)sess.getAttribute(Config.USER_ATTRIBUTE);
	}

	public static boolean isUserInRole(User user, String roleName){
		Role r = new Role(roleName, null);
		return user.getRoles().contains(r);
	}

}

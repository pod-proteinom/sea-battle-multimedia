package com.multimedia.security.services;

import com.multimedia.security.beans.User;


/**
 *
 * @author demchuck.dima@gmail.com
 */
public interface ISecurityService {
	/** get user by given login and password */
	public User getUser(String login, String password);
	/** get user by given id */
	public User getUser(Long id);
	/** update user's last access time and mb smth else */
	public void userEntered(User user);
}

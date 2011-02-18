/*******************************************************************************
 * Copyright (c) 2011 demchuck.dima@gmail.com.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     demchuck.dima@gmail.com - initial API and implementation
 ******************************************************************************/
package com.multimedia.seabattle.service.user;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.multimedia.seabattle.dao.IGenericDAO;
import com.multimedia.seabattle.model.beans.User;

@Service("userService")
public class UserServiceImpl implements IUserService{
	private IGenericDAO<User, Long> user_dao;

	@Override
	public boolean registerUser(User user) {
		user.setLast_accessed(new java.sql.Timestamp(System.currentTimeMillis()));
		//TODO: mb make it somewhere else
        String md5=org.apache.catalina.realm.RealmBase.Digest(user.getPassword(),"MD5","UTF-8");
		user.setPassword(md5);
		//TODO: insert roles
		user_dao.makePersistent(user);
		return true;
	}

	public static final String[] SECURITY_WHERE =  new String[]{"login","password"};
	@Override
	public User getUser(String login, String password) {
		List<User> r =
				user_dao.getByPropertiesValuePortionOrdered(null, null, SECURITY_WHERE, new Object[]{login, password}, 0, 1, null, null);
		if (r!=null&&!r.isEmpty()){
			return r.get(0);
		}else{
			return null;
		}
	}

	@Override
	public User getUser(Long id) {
		return user_dao.getById(id);
	}

	protected static final String[] SECURITY_UPDATE = new String[]{"last_accessed"};
	@Override
	public void userEntered(com.multimedia.security.beans.User user) {
		if (user!=null){
			user_dao.updatePropertiesById(SECURITY_UPDATE, new Object[]{new java.util.Date()}, user.getId());
		}
	}

	@Override
	public boolean checkUserEmail(String email) {
		return user_dao.getRowCount("email", email)==0;
	}

	@Override
	public boolean checkUserLogin(String login) {
		return user_dao.getRowCount("login", login)==0;
	}

//------------------------------------------------------------------------------------------------
	@Resource(name="userDAO")
	public void setDao(IGenericDAO<User, Long> dao) {
		this.user_dao = dao;
	}
	
}

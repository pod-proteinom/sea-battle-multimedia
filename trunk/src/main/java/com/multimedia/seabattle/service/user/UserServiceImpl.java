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

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.multimedia.seabattle.dao.IGenericDAO;
import com.multimedia.seabattle.model.beans2.User;

@Service("userService")
public class UserServiceImpl implements IUserService{
	private IGenericDAO<User, Long> dao;

	@Resource(name="userDAO")
	public void setDao(IGenericDAO<User, Long> dao) {
		this.dao = dao;
	}
	
}

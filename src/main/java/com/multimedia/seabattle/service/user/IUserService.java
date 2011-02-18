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

import com.multimedia.seabattle.model.beans.User;
import com.multimedia.security.services.ISecurityService;

public interface IUserService extends ISecurityService{

	/**
	 * registers user in the system
	 */
	public boolean registerUser(User user);

	/**
	 * check whether you can use this login to register in a system
	 */
	public boolean checkUserLogin(String login);

	/**
	 * check whether you can use this email to register in a system
	 */
	public boolean checkUserEmail(String email);

}

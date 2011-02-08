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
package com.multimedia.seabattle.controllers;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.multimedia.seabattle.dao.IGenericDAO;
import com.multimedia.seabattle.model.beans2.Country;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	private IGenericDAO<Country, Long> dao;

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String home() {
		logger.info("Welcome home!");
		logger.info(dao.getAllShortOrdered(null, null, null).toString());
		return "home";
	}


	@Resource(name="countryDAO")
	public void setDao(IGenericDAO<Country, Long> dao) {
		this.dao = dao;
	}

	
}


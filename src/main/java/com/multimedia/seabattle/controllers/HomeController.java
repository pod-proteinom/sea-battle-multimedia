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

import com.multimedia.seabattle.dwr.DwrTestService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	private DwrTestService dwrTestService;

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value="/test.htm", method=RequestMethod.GET)
	public String home() {
		logger.info("Time updated");
		dwrTestService.updateClock("time:"+System.currentTimeMillis());
		return "/WEB-INF/views/home.jsp";
	}

	@Resource(name="DwrTestService")
	public void setDwrService(DwrTestService value){
		this.dwrTestService = value;
	}

	
}


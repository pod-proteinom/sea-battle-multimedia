package com.multimedia.seabattle.controllers;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.multimedia.seabattle.config.ITemplateConfig;

@Controller("IndexController")
@RequestMapping({"/index.htm", "/"})
public class IndexController {

	protected ITemplateConfig config;

	private final String index_url = "/WEB-INF/views/index.jsp";


	@RequestMapping
	public String mainWindow(Map<String, Object> model){
		model.put(config.getContentUrlAttribute(), index_url);
		return config.getTemplateUrl();
	}


	//---------------------------- setters ---------------------------

	@Required
	@Resource(name="templateConfig")
	public void setConfig(ITemplateConfig config) {
		this.config = config;
	}
}

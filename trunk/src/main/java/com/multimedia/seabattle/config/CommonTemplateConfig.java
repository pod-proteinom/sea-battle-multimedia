package com.multimedia.seabattle.config;

import org.springframework.context.annotation.Configuration;

@Configuration(value="templateConfig")
public class CommonTemplateConfig implements ITemplateConfig {

	protected final String templateUrl = "/WEB-INF/views/templates/my.jsp";

	protected final String contentUrlAttribute = "content_url";
	protected final String navigationUrlAttribute = "navigation_url";

	protected final String contentDataAttribute = "content_data";
	protected final String navigationDataAttribute = "navigation_data";

	@Override
	public String getTemplateUrl() {return templateUrl;}

	@Override
	public String getContentDataAttribute() {return contentDataAttribute;}

	@Override
	public String getNavigationDataAttribute() {return navigationDataAttribute;}

	@Override
	public String getContentUrlAttribute() {return contentUrlAttribute;}

	@Override
	public String getNavigationUrlAttribute() {return navigationUrlAttribute;}

}

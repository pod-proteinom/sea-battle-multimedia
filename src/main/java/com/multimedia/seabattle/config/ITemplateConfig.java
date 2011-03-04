package com.multimedia.seabattle.config;

public interface ITemplateConfig {

	/**
	* url of page that serves as a template (may include places for including navigation and content pages)
	* @return url
	*/
	public String getTemplateUrl();
	/**
	 * model name for content
	 * @return name of attribute
	 */
	public String getContentDataAttribute();
	/**
	 * model name for navigation
	 * @return name of attribute
	 */
	public String getNavigationDataAttribute();
    /**
	 * url to jsp that renders main content
     */
    public String getContentUrlAttribute();

    /**
	 * url to jsp that renders navigation content
     */
    public String getNavigationUrlAttribute();
}

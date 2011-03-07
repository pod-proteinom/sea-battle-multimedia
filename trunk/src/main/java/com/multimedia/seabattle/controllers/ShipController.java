package com.multimedia.seabattle.controllers;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/ship")
public class ShipController implements MessageSourceAware{
	private MessageSource messageSource;

	

//----------------------------------------------------------------------------------------------
	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
}

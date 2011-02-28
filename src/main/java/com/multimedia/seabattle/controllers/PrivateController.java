package com.multimedia.seabattle.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("PrivateController")
@RequestMapping(value="/private")
public class PrivateController {
	
	@RequestMapping("/index.htm")
	@ResponseBody
	public String go(){
		return "private data";
	}
}

package com.multimedia.seabattle.controllers;

import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.multimedia.seabattle.config.ITemplateConfig;
import com.multimedia.seabattle.model.beans.User;
import com.multimedia.seabattle.service.user.IUserService;

@Controller("UserRegisterController")
@RequestMapping(value={"/register.htm"})
public class UserRegisterController {
	private static final Logger logger = LoggerFactory.getLogger(UserRegisterController.class);

	protected ITemplateConfig config;
	protected IUserService user_service;

	protected final String register_url = "/WEB-INF/views/user/register.jsp";

	@RequestMapping
	public String doRegisterPrepare(Map<String, Object> model){
		logger.debug("do=register prepare");

		model.put(config.getContentUrlAttribute(), register_url);
		model.put(config.getContentDataAttribute(), new User());
		return config.getTemplateUrl();
	}

	@RequestMapping(params={"action=register"})
	public String doRegister(Map<String, Object> model, @Valid User obj, BindingResult res){
		logger.debug("do=register");

		model.put(config.getContentUrlAttribute(), register_url);
		model.put(config.getContentDataAttribute(), obj);
		model.put(BindingResult.MODEL_KEY_PREFIX+config.getContentDataAttribute(), res);

		if (obj.getPassword()==null||!obj.getPassword().equals(obj.getPassword_repeat())){
			res.rejectValue("password", "password_repeat.different");
		}
		if (res.hasErrors()){
			common.utils.CommonAttributes.addErrorMessage("form_errors", model);
		} else {
			if (user_service.registerUser(obj)){
				common.utils.CommonAttributes.addHelpMessage("operation_succeed", model);
			} else {
				common.utils.CommonAttributes.addErrorMessage("operation_fail", model);
			}
		}

		return config.getTemplateUrl();
	}


	//---------------------------- setters ---------------------------

		@Resource(name="templateConfig")
		public void setConfig(ITemplateConfig config) {
			this.config = config;
		}

		@Resource(name="userService")
		public void setUser_service(IUserService user_service) {
			this.user_service = user_service;
		}
}

package com.multimedia.seabattle.controllers;

import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.multimedia.seabattle.config.ITemplateConfig;
import com.multimedia.seabattle.model.beans.User;
import com.multimedia.seabattle.service.country.ICountryService;
import com.multimedia.seabattle.service.user.IUserService;
import com.multimedia.seabattle.service.user.IUserValidator;

@Controller("UserRegisterController")
@RequestMapping(value={"/register.htm"})
public class UserRegisterController {
	private static final Logger logger = LoggerFactory.getLogger(UserRegisterController.class);

	protected ITemplateConfig config;
	protected IUserService user_service;

	private IUserValidator user_validator;

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
		
		if (validateUser(obj, res).hasErrors()){
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

	private BindingResult validateUser(User obj, BindingResult res){
		String tmp = user_validator.validatePassword_repeat(obj.getPassword(), obj.getPassword_repeat()); 
		if (tmp!=null){
			res.rejectValue("password", tmp);
		}
		tmp = user_validator.validatePassword(obj.getPassword());
		if (tmp!=null){
			res.rejectValue("password", tmp);
		}
		tmp = user_validator.validateLogin(obj.getLogin());
		if (tmp!=null){
			res.rejectValue("login", tmp);
		}
		tmp = user_validator.validateEmail(obj.getEmail()); 
		if (tmp!=null){
			res.rejectValue("email", tmp);
		}
		tmp = user_validator.validateCountry(obj.getCountry()); 
		if (tmp!=null){
			res.rejectValue("country", tmp);
		}
		return res;
	}


	//---------------------------- setters ---------------------------

	@Required
	@Resource(name="templateConfig")
	public void setConfig(ITemplateConfig config) {
		this.config = config;
	}

	@Required
	@Resource(name="userService")
	public void setUser_service(IUserService user_service) {
		this.user_service = user_service;
	}

	@Required
	@Resource(name="UserValidator")
	public void setUserValidator(IUserValidator user_validator) {
		this.user_validator = user_validator;
	}
}

package com.multimedia.seabattle.controllers;

import java.io.IOException;
import java.io.Writer;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.multimedia.seabattle.service.user.IUserValidator;

@Controller("UserValidatorController")
@RequestMapping("/ajax")
public class UserValidatorController implements MessageSourceAware {
	private static final Logger logger = LoggerFactory.getLogger(UserValidatorController.class);

	private IUserValidator user_validator;

	private MessageSource msg_src;

	@RequestMapping("/validateLogin.htm")
	public void validateLogin(@RequestParam(value="login") String login, Locale locale, HttpServletResponse resp)
		throws IOException
	{
		if (logger.isDebugEnabled()){
			logger.debug("validating login ["+login+"] locale "+locale);
		}
		sendResponse(localize(user_validator.validateLogin(login), locale), resp);
	}

	@RequestMapping("/validateEmail.htm")
	public void validateEmail(@RequestParam(value="email") String email, Locale locale, HttpServletResponse resp)
		throws IOException
	{
		if (logger.isDebugEnabled()){
			logger.debug("validating email:"+email);
		}
		sendResponse(localize(user_validator.validateEmail(email), locale), resp);
	}

	@RequestMapping("/validatePassword.htm")
	public void validatePassword(@RequestParam(value="password") String password, Locale locale, HttpServletResponse resp)
		throws IOException
	{
		sendResponse(localize(user_validator.validatePassword(password), locale), resp);
	}

	@RequestMapping("/validatePasswordRepeat.htm")
	public void validatePassword_repeat(@RequestParam(value="password") String password, @RequestParam(value="repeat") String repeat, Locale locale, HttpServletResponse resp)
		throws IOException
	{
		sendResponse(localize(user_validator.validatePassword_repeat(password, repeat), locale), resp);
	}

	/**
	 * if msg is not null try to find its localization in bundle
	 */
	private String localize(String msg, Locale locale){
		if (msg==null){
			return null;
		} else {
			if (logger.isDebugEnabled()){
				logger.debug("get message ["+locale+"] ["+msg+"]:"+msg_src.getMessage(msg, null, locale));
			}
			return msg_src.getMessage(msg, null, locale);
		}
	}

	private void sendResponse(String msg, HttpServletResponse resp) throws IOException{
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		Writer w = resp.getWriter();
		if (msg!=null){
			w.append(msg);
		}
		w.flush();
		w.close();
	}
	
//----------------------------------------------------------------------------------------------
	@Required
	@Resource(name="UserValidator")
	public void setUserService(IUserValidator value){
		this.user_validator = value;
	}

	//@Required
	//@Resource(name="messageSource")
	public void setMessageSource(MessageSource msg_src){
		this.msg_src = msg_src;
	}
}

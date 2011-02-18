package com.multimedia.seabattle.dwr;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.hibernate.validator.constraints.impl.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;

import com.multimedia.seabattle.model.beans.Country;
import com.multimedia.seabattle.service.country.ICountryService;
import com.multimedia.seabattle.service.user.IUserService;

@RemoteProxy(name="UserValidator")
@Service("UserValidator")
public class UserValidator {
	private static final Logger logger = LoggerFactory.getLogger(UserValidator.class);

	private IUserService user_service;
	private ICountryService country_service;

	private EmailValidator validator_email = new EmailValidator();

	@RemoteMethod
	public String validateLogin(final String login){
		if (user_service.checkUserLogin(login)){
			return null;
		} else {
			return "already_exists";
		}
	}

	@RemoteMethod
	public String validateEmail(final String email){
		if (validator_email.isValid(email, null)){
			if (user_service.checkUserEmail(email)){
				return null;
			} else {
				return "already_exists";
			}
		} else {
			return "invalid_email";
		}
	}

	@RemoteMethod
	public String validatePassword(final String password){
		if (password.length()>5){
			return null;
		} else {
			return "short";
		}
	}

	@RemoteMethod
	public String validatePassword_repeat(final String password, final String repeat){
		if (password==null||!password.equals(repeat)){
			return "not_match";
		} else {
			return null;
		}
	}

	@RemoteMethod
	public List<Country> getCountries(final String prefix){
		if (prefix==null){
			return new ArrayList<Country>();
		}
		List<Country> list = country_service.getCountries(prefix);
		if (logger.isDebugEnabled()){
			logger.debug("getting countries"+list.toString());
		}
		return list;
	}

	
//----------------------------------------------------------------------------------------------
	@Required
	@Resource(name="userService")
	public void setUserService(IUserService value){
		this.user_service = value;
	}

	@Required
	@Resource(name="CountryService")
	public void setCountryService(ICountryService value){
		this.country_service = value;
	}
}

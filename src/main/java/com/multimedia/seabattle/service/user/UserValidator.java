package com.multimedia.seabattle.service.user;

import javax.annotation.Resource;

import org.hibernate.validator.constraints.impl.EmailValidator;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;

import com.multimedia.seabattle.model.beans.Country;
import com.multimedia.seabattle.service.country.ICountryService;

@Service("UserValidator")
public class UserValidator implements IUserValidator{

	private IUserService user_service;
	private ICountryService country_service;

	private EmailValidator validator_email = new EmailValidator();

	public String validateLogin(final String login){
		if (user_service.checkUserLogin(login)){
			return null;
		} else {
			return "exists.login";
		}
	}

	public String validateEmail(final String email){
		if (validator_email.isValid(email, null)){
			if (user_service.checkUserEmail(email)){
				return null;
			} else {
				return "exists.email";
			}
		} else {
			return "typeMismatch.email";
		}
	}

	public String validatePassword(final String password){
		if (password==null) {
			return "required";
		} else {
			return null;
		}
		/*if (password!=null&&password.length()>5){
			return null;
		} else {
			return "password.simple";
		}*/
	}

	public String validatePassword_repeat(final String password, final String repeat){
		if (password==null){
			return "required";
		} else if (!password.equals(repeat)) {
			return "password_repeat.different";
		} else {
			return null;
		}
	}

	@Override
	public String validateCountry(Country country) {
		if (country==null||(country.getId()==null&&country.getName()==null)){
			return "required";
		} else{
			if (country_service.checkCountry(country)){
				return null;
			} else {
				return "typeMismatch.country";
			}
		}
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

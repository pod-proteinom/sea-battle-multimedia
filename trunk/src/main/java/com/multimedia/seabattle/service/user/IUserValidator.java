package com.multimedia.seabattle.service.user;

import com.multimedia.seabattle.model.beans.Country;

/**
 * validates properties of a user object
 * @author Dmitriy_Demchuk
 *
 */
public interface IUserValidator {
	/**
	 * validates login
	 * @return message code or null if ok
	 */
	public String validateLogin(final String login);

	/**
	 * validates email
	 * @return message code or null if ok
	 */
	public String validateEmail(final String email);

	/**
	 * validates password
	 * @return message code or null if ok
	 */
	public String validatePassword(final String password);

	/**
	 * validates password and password repeat
	 * @return message code or null if ok
	 */
	public String validatePassword_repeat(final String password, final String repeat);

	/**
	 * checks whether given country is in the database
	 * @return message code or null if ok
	 */
	public String validateCountry(final Country country);
}

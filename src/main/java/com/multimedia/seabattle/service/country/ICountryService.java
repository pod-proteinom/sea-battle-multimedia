package com.multimedia.seabattle.service.country;

import java.util.List;

import com.multimedia.seabattle.model.beans.Country;

public interface ICountryService {
	/**
	 * get all countries that are in database, and are starting from prefix
	 * @return list of countries
	 */
	public List<Country> getCountries(String prefix);
}

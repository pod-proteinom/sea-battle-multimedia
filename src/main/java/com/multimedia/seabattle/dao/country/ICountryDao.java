package com.multimedia.seabattle.dao.country;

import java.util.List;

import com.multimedia.seabattle.dao.IGenericDAO;
import com.multimedia.seabattle.model.beans.Country;

public interface ICountryDao extends IGenericDAO<Country, Long>{
	/**
	 * get countries starting with given prefix
	 * @param prefix for country name to start
	 * @return list of appropriate countries
	 */
	public List<Country> getCountries(String prefix);
}

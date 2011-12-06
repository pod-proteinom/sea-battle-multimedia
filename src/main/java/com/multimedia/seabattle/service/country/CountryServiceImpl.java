package com.multimedia.seabattle.service.country;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.multimedia.seabattle.dao.country.ICountryDao;
import com.multimedia.seabattle.model.beans.Country;

@Service("CountryService")
public class CountryServiceImpl implements ICountryService{

	private ICountryDao country_dao;

	@Override
	public List<Country> getCountries(String prefix) {
		return country_dao.getCountries(prefix);
	}

	@Override
	public boolean checkCountry(Country country) {
		Country c = country_dao.getById(country.getId());
		if (c==null){
			return false;
		}
		country.setName(c.getName());
		country_dao.merge(c);
		return true;
	}
//--------------------------------------------------------------------------
	@Resource(name="countryDAO")
	public void setCountryDao(ICountryDao value){
		this.country_dao = value;
	}
}

package com.multimedia.seabattle.service.country;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.multimedia.seabattle.dao.country.ICountryDao;
import com.multimedia.seabattle.model.beans.Country;

@Service("CountryService")
public class CountryServiceImpl implements ICountryService{
	private static final Logger logger = LoggerFactory.getLogger(CountryServiceImpl.class);

	private ICountryDao country_dao;

	@Override
	public List<Country> getCountries(String prefix) {
		return country_dao.getCountries(prefix);
	}
	
//--------------------------------------------------------------------------
	@Resource(name="countryDAO")
	public void setCountryDao(ICountryDao value){
		this.country_dao = value;
	}
}

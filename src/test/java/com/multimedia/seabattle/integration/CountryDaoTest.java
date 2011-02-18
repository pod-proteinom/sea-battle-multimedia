package com.multimedia.seabattle.integration;

import static org.junit.Assert.*;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.multimedia.seabattle.dao.country.ICountryDao;
import com.multimedia.seabattle.model.beans.Country;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
"classpath:/spring/root-context.xml"
,"classpath:/spring/hibernate-context.xml"})
public class CountryDaoTest {
	private ICountryDao country_dao;

	/**
	 * testing a specific query
	 */
	@Test
	public void getCountries(){
		List<Country> list = country_dao.getCountries("J");
		assertNotNull("might be empty but not null", list);
		System.out.println(list);
	}
	
//-----------------------------------------------------------------------------
	@Required
	@Resource(name="countryDAO")
	public void setShipDAO(ICountryDao value){
		this.country_dao = value;
	}
}

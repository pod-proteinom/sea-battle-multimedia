package com.multimedia.seabattle.dao.country;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.multimedia.seabattle.dao.GenericDAOHibernate;
import com.multimedia.seabattle.model.beans.Country;

public class CountryDaoImpl extends GenericDAOHibernate<Country, Long> implements ICountryDao{
	private static final Logger logger = LoggerFactory.getLogger(CountryDaoImpl.class);

	public CountryDaoImpl() {
		super("com.multimedia.seabattle.model.beans.Country", "com.multimedia.seabattle.model.beans.Country");
	}

	@Override
	@Transactional(readOnly=true)
	public List<Country> getCountries(String prefix) {
		logger.debug("start to retrieve countries");
		List<Country> list = (List<Country>)getSessionFactory().getCurrentSession()
		.getNamedQuery("countries")
		.setParameter("name", prefix+"%")
		.list();
		logger.debug("retrieved countries "+list);
		return list;
	}

}

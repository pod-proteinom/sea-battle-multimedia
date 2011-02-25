package com.multimedia.seabattle.dao;


import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Restrictions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.multimedia.seabattle.dao.country.ICountryDao;
import com.multimedia.seabattle.model.beans.Cell;
import com.multimedia.seabattle.model.beans.Coordinates;
import com.multimedia.seabattle.model.beans.Country;
import com.multimedia.seabattle.model.beans.Game;
import com.multimedia.seabattle.model.beans.Ship;
import com.multimedia.seabattle.model.beans.User;
import com.multimedia.seabattle.model.types.ShipCreationResult;
import com.multimedia.seabattle.model.types.ShootResult;
import com.multimedia.seabattle.service.battlefield.IBattlefieldService;
import com.multimedia.seabattle.service.game.IGameService;
import com.multimedia.seabattle.service.ships.IGameShips;
import com.multimedia.seabattle.service.ships.RandomShipGenerator;
import com.multimedia.seabattle.service.user.IUserService;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
"classpath:/spring/root-context.xml"
,"classpath:/spring/hibernate-context.xml"})
public class UserDAOTest {
	private SessionFactory factory;

	@Test
	@Rollback(value=false)
	@Transactional()
	public void test1(){
		/*Country c = (Country)factory.getCurrentSession().get(Country.class, Long.valueOf(1));
	
		User u = new User();
		u.setDate(new java.util.Date());
		u.setCountry(c);
		factory.getCurrentSession().persist(u);*/
		User c = (User)factory.getCurrentSession().createCriteria(User.class)
			.add(Restrictions.eq("login", "test1")).uniqueResult();
		assertEquals(Long.valueOf(4), c.getId());
		assertEquals(Long.valueOf(1), c.getCountry().getId());
		assertEquals("Ukraine", c.getCountry().getName());
	}

	@Test
	@Rollback(value=false)
	@Transactional()
	public void test2(){
		/*Country c = (Country)factory.getCurrentSession().get(Country.class, Long.valueOf(1));
	
		User u = new User();
		u.setDate(new java.util.Date());
		u.setCountry(c);
		factory.getCurrentSession().persist(u);*/
		User c = (User)factory.getCurrentSession()
			.createQuery("from "+User.class.getName()+" where login = :login")
			.setParameter("login", "test1")
			.uniqueResult();
		assertEquals(Long.valueOf(4), c.getId());
		assertEquals(Long.valueOf(1), c.getCountry().getId());
		assertEquals("Ukraine", c.getCountry().getName());
	}
	

	@Required
	@Resource(name="sessionFactory")
	public void setShipDAO(SessionFactory value){
		this.factory = value;
	}
}

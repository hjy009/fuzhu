package com.cxic.dao;

import static org.junit.Assert.*;

import com.cxic.dao.BaseDaoTestCase;
import com.cxic.model.Passport;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

public class PassportDaoTest extends BaseDaoTestCase{
	private GenericDao<Passport, Long> passportDao;

	@Autowired
	public void setPassportDao(GenericDao<Passport, Long> passportDao){
		this.passportDao = passportDao;
	}
	
	@Test(expected=DataAccessException.class)
	public void test() {
		Passport passport = new Passport();
		passport.setUsername("Country");
		passport.setPassword("Bry");

		passport = passportDao.save(passport);
		flush();

		passport = passportDao.get(passport.getId());

		assertEquals("Country", passport.getUsername());
		assertNotNull(passport.getId());

		log.debug("removing passport...");

		passportDao.remove(passport.getId());
		flush();

		// should throw DataAccessException
		passportDao.get(passport.getId());
	}

}

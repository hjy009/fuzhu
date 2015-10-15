package com.cxic.service.impl;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import com.cxic.dao.GenericDao;
import com.cxic.model.Passport;

public class PassportManagerImplTest  extends BaseManagerMockTestCase {

	@InjectMocks
	private GenericManagerImpl<Passport, Long> passportManager;
	
	@Autowired
	public void setPassportManager(GenericManagerImpl<Passport, Long> passportManager){
		this.passportManager = passportManager;
	}
	
	@Mock
	private GenericDao<Passport, Long> passportDao;

	@Autowired
	public void setPassportDao(GenericDao<Passport, Long> passportDao){
		this.passportDao = passportDao;
	}
	
	@Test
	public void testGetPassport() {
        log.debug("testing get...");
        //given
        final Long id = 7L;
        final Passport passport = new Passport();
        given(passportDao.get(id)).willReturn(passport);
        //when
        Passport result = passportManager.get(id);
        //then
        assertSame(passport, result);
	}

    @Test
    public void testGetPassports() {
        log.debug("testing getAll...");
        //given
        final List passports = new ArrayList();
        given(passportDao.getAll()).willReturn(passports);
        //when
        List result = passportManager.getAll();
        //then
        assertSame(passports, result);
    }
 
    @Test
    public void testSavePassport() {
        log.debug("testing save...");
        //given
        final Passport passport = new Passport();
        // enter all required fields
         
        given(passportDao.save(passport)).willReturn(passport);
        //when
        passportManager.save(passport);
        //then
        verify(passportDao).save(passport);
    }
    @Test
    public void testRemovePassport() {
        log.debug("testing remove...");
        //given
        final Long id = -11L;
        willDoNothing().given(passportDao).remove(id);
        //when
        passportManager.remove(id);
        //then
        verify(passportDao).remove(id);
    }
}

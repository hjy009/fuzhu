package com.cxic.webapp.controller;

import com.cxic.service.GenericManager;
import com.cxic.model.Passport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/passports*")
public class PassportController {
    private GenericManager<Passport, Long> passportManager;
    
    @Autowired
    public void setPassportManager(@Qualifier("passportManager") GenericManager<Passport, Long> passportManager) {
        this.passportManager = passportManager;
    }
 
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView handleRequest()
    throws Exception {
        return new ModelAndView().addObject(passportManager.getAll());
    }

}

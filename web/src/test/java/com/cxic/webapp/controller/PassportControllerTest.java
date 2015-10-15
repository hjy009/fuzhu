package com.cxic.webapp.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PassportControllerTest extends BaseControllerTestCase {
    @Autowired
    private PassportController controller;
 
    private MockMvc mockMvc;
 
    @Before
    public void setup() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/pages/");
        viewResolver.setSuffix(".jsp");
 
        mockMvc = MockMvcBuilders.standaloneSetup(controller).setViewResolvers(viewResolver).build();
    }
 
    @Test
    public void testHandleRequest() throws Exception {
        mockMvc.perform(get("/passports"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("passportList"))
                .andExpect(view().name("passports"));
    }
}
package com.iisigroup.java.tech.servelet;



import com.iisigroup.java.tech.controller.LdapControler;

import javax.json.JsonArray;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource; 
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {LdapViewServletTestConfig.class})
public class LdapViewServletTest {
	@Autowired	
	private LdapViewServlet component ;
	
    @Before
    public void setUp() throws Exception {
    	 
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testBuildJsonV1() throws Exception {
    	
    	JsonArray    index  = 	component.retireveLdapTree ();
        Assert.assertNotNull(index); ;
    }
   
}
@Configuration
class LdapViewServletTestConfig {
	 @Bean
	    public LdapViewServlet getLdapViewServlet() {
		 LdapViewServlet ctl = new LdapViewServlet()  ;
	        return ctl;
	    }

    @Bean
    public LdapControler getLdapControler() {
    	LdapControler ctl = new LdapControler()  ;
        return ctl;
    }
}
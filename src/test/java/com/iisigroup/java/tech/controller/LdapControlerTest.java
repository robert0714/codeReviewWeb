package com.iisigroup.java.tech.controller;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iisigroup.java.tech.ldap.internal.Node;

public class LdapControlerTest {
	/** The Constant logger. */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(LdapControlerTest.class);
	private LdapControler component ;
	
	@Before
	public void setUp() throws Exception {
		component = new LdapControler();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetNodes() throws Exception {
		 Node node = component.getNodes();
		 org.junit.Assert.assertNotNull(node);
		 ;
	}

	 
}

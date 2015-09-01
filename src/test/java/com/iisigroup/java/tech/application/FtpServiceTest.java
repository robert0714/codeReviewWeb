package com.iisigroup.java.tech.application;

import com.iisigroup.java.tech.controller.LdapControler;

import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FtpServiceTest {


	//================================================
	//== [Enumeration types] Block Start
	//== [Enumeration types] Block End 
	//================================================
	//== [static variables] Block Start
	private FtpService component ;
	//== [static variables] Block Stop 
	//================================================
	//== [instance variables] Block Start
	//== [instance variables] Block Stop 
	//================================================
	//== [static Constructor] Block Start
	//== [static Constructor] Block Stop 
	//================================================
	//== [Constructors] Block Start (含init method)
	//== [Constructors] Block Stop 
	//================================================
	//== [Static Method] Block Start
	//== [Static Method] Block Stop 
	//================================================
	//== [Accessor] Block Start
	//== [Accessor] Block Stop 
	//================================================
	//== [Overrided Method] Block Start (Ex. toString/equals+hashCode)
	//== [Overrided Method] Block Stop 
	//================================================
	//== [Method] Block Start
	//####################################################################
	//## [Method] sub-block : 
	//####################################################################    
	//== [Method] Block Stop 
	//================================================
	//== [Inner Class] Block Start
	//== [Inner Class] Block Stop 
	//================================================

	@Before
	public void setUp() throws Exception {
		component = new FtpService();
	}
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetProjectFolderName() throws Exception {
		final Map<String , String > data = component.getProjectFolderName();
		 Assert.assertEquals(true, MapUtils.isNotEmpty(data));
	}
}

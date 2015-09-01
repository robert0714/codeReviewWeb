package com.iisigroup.java.tech.ldap;


import java.io.File;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.iisigroup.java.tech.ldap.internal.Node;
import com.iisigroup.java.tech.utils.Utils;

public class LdapExportNodeTest {
	private LdapExportNode component ;
    @Before
    public void setUp() throws Exception {
    	component = new LdapExportNode();// pass argument here 
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testExportV1() throws Exception {
    	//java技術處為OU=TDD10,OU=15_TDD00,OU=IE,DC=iead,DC=local
    	final 	Map<String, Node> index = component.exportV1("OU=15_TDD00,OU=IE,DC=iead,DC=local");
    	// external handle for exporting to file
        Utils.serialization(index, new File(FileUtils.getTempDirectory() , "ldapdata"));
        Assert.assertNotNull(index); ;
    }
   
}

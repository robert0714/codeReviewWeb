package com.iisigroup.scan.folder.internal;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class EmpFactoryTest {
	 /** The component. */
    private EmpFactory component;

    /**
     * Sets the up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        component = component.getEmpFactory();
    }

    /**
     * Test get scan folder.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetUserProjByLdap() throws Exception {
       List<UserProjProfile> list = component.getUserProjByLdap();
         assertNotNull(list); 
    }

    

}

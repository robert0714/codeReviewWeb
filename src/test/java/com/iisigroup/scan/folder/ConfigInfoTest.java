package com.iisigroup.scan.folder;


import java.io.File;

import static org.junit.Assert.assertNotNull;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import com.iisigroup.java.tech.utils.Utils;
import com.iisigroup.scan.folder.internal.UserFolder;
import com.iisigroup.scan.folder.internal.UserProjProfile;
 
/**
 * The Class ConfigInfoTest.
 */
public class ConfigInfoTest {
    
    /** The component. */
    private ConfigInfo component;

    /**
     * Sets the up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        component = new ConfigInfo();
    }

    /**
     * Test get scan folder.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetScanFolder() throws Exception {
        final File file = component.getScanFolder();
         assertNotNull(file); 
    }

    /**
     * Test get sonar project folder.
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetSonarProjectFolder() throws Exception {
        final File file = component.getSonarProjectFolder();
        assertNotNull(file); 
    }
    @Test
    public void testGetNotifyConfigFolder() {
    	 final File file = component.getNotifyConfigFolder();
         assertNotNull(file); 
    }
}

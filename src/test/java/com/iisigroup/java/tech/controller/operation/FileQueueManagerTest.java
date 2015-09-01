/**
 * 
 */
package com.iisigroup.java.tech.controller.operation;
 
 

import java.io.File;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith; 
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock; 
import org.mockito.stubbing.Answer; 
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iisigroup.java.tech.utils.DateUtils;
import com.iisigroup.java.tech.utils.Utils;
import com.iisigroup.scan.folder.ConfigInfo;
import com.iisigroup.scan.folder.internal.EmpDto;
import com.iisigroup.scan.folder.internal.UserFolder;
import com.iisigroup.scan.folder.internal.UserProjProfile;

/**
 * @author robert.lee
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Utils.class })
public class FileQueueManagerTest {
	 /** The Constant logger. */
		private static final Logger LOGGER = LoggerFactory
				.getLogger(FileQueueManagerTest.class);
	@Mock
	private ConfigInfo config;
	
	@InjectMocks
	private FileQueueManager fileQueueManager = new FileQueueManager() ;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		 
	}

	/**
	 * Test method for {@link com.iisigroup.java.tech.controller.operation.FileQueueManager#getFileName(com.iisigroup.scan.folder.internal.UserFolder)}.
	 */	
	@Test
	public void testGetFileName() throws Exception {
		final UserFolder unit = Mockito.mock(UserFolder.class) ;
		final UserProjProfile info = Mockito.mock(UserProjProfile.class) ;
		final EmpDto empdata = Mockito.mock(EmpDto.class) ;
		 
		Mockito.when(empdata.getEmpId()).thenReturn("mockEmpId");
		Mockito.when(info.getEmpdata()).thenReturn(empdata);
		Mockito.when(unit.getInfo()).thenReturn(info);
		Mockito.when(unit.getProjectVersion()).thenReturn("mockProjectVersion");
		
		final String fileName = fileQueueManager.getFileName(unit);
		LOGGER.info("fileName: {}",fileName);
		org.junit.Assert.assertEquals("mockEmpId_mockProjectVersion", fileName);
		
	}

	/**
	 * Test method for {@link com.iisigroup.java.tech.controller.operation.FileQueueManager#getQueuedEmpIds()}.
	 */
	@Test
	public void testGetQueuedEmpIds() throws Exception {
		final	File mockDictionary = Mockito.mock(File.class);
		Mockito.when(config.getFileQueueFolder()).thenReturn(mockDictionary);
		Mockito.when(mockDictionary.isDirectory()).thenReturn(true); 
		Mockito.when(mockDictionary.list()).thenReturn(new File("/").list()); 
		
		String[] empIds = fileQueueManager.getQueuedEmpIds();
		LOGGER.info("fileName: {}",empIds);
	}

	/**
	 * Test method for {@link com.iisigroup.java.tech.controller.operation.FileQueueManager#getTheOldest()}.
	 */
//	@Test
//	public void testGetTheOldest() throws Exception {
//		final	File mockDictionary = Mockito.mock(File.class);
//		Mockito.when(config.getFileQueueFolder()).thenReturn(mockDictionary);
//		Mockito.when(mockDictionary.isDirectory()).thenReturn(true); 
//		Mockito.when(mockDictionary.list()).thenReturn(new File("/").list()); 
//		Mockito.when(mockDictionary.listFiles()).thenReturn(new File("/").listFiles()); 
//		 
//		
//		
//		final	UserFolder data = fileQueueManager.getTheOldest();
//		LOGGER.info("fileName: {}",ToStringBuilder.reflectionToString(data));
//	}

}

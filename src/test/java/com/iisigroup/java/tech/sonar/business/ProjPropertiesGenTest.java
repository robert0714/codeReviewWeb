package com.iisigroup.java.tech.sonar.business;


import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.iisigroup.scan.folder.internal.EmpDto;
import com.iisigroup.scan.folder.internal.UserFolder;
import com.iisigroup.scan.folder.internal.UserProjProfile;

public class ProjPropertiesGenTest {
	private ProjPropertiesGen component ; 
	private UserFolder testTarget ; 
	private String TEST_FOLDER = "TestFolder" ;
	private String TEST_PJ_V = "2015-02-03" ;
	private static String sonarRunnerHome =FileUtils.getTempDirectoryPath();
	@Before
	public void setUp() throws Exception {
		component = new ProjPropertiesGen();
		this.testTarget = Mockito.mock(UserFolder.class);	
		
		EmpDto empdata = Mockito.mock(EmpDto.class);   
		final UserProjProfile info = Mockito.mock(UserProjProfile.class);
		
		Mockito.when(info.getEmpdata()).thenReturn(empdata) ;
		Mockito.when(info.getProjectKey()).thenReturn("robert.lee:project") ;
		Mockito.when(info.getProjectName()).thenReturn("TestProject") ;
		Mockito.when(info.getFolderName()).thenReturn(TEST_FOLDER) ;
		
		Mockito.when(testTarget.getInfo()).thenReturn(info) ;
		Mockito.when(testTarget.getProjectVersion()).thenReturn(TEST_PJ_V) ;
		Mockito.when(testTarget.getSourceEncoding()).thenReturn("UTF-8") ;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetSonarRunarCmdContent() throws Exception {
		String result = component.getSonarRunarCmdContent(sonarRunnerHome);
		System.out.println("1.testGetSonarRunarCmdContent....");
		System.out.println(result);
	}

	@Test
	public void testGetSonarRunarCmdContentExt() throws Exception {
		String result = component.getSonarRunarCmdContentExt(testTarget, sonarRunnerHome);
		System.out.println("2.testGetSonarRunarCmdContentExt....");
		System.out.println(result);
	}

	@Test
	public void testGetSonarConfigContent() throws Exception {
		String result = component.getSonarConfigContent(testTarget);
		System.out.println("3.testGetSonarConfigContent....");
		System.out.println(result);
	}

}

package com.iisigroup.java.tech.controller.operation;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.iisigroup.java.tech.sonar.business.ProjPropertiesGen;
import com.iisigroup.java.tech.utils.ReflectionUtils;
import com.iisigroup.scan.folder.ConfigInfo;
import com.iisigroup.scan.folder.internal.EmpDto;
import com.iisigroup.scan.folder.internal.UserFolder;
import com.iisigroup.scan.folder.internal.UserProjProfile;

public class UserFolderOpTest {
	private UserFolderOp component ;
	private UserFolder testTarget ; 
	private String TEST_FOLDER = "TestFolder" ;
	private String TEST_PJ_V = "2015-02-03" ;
	private File tmpSonar ;
	@After
	public void tearDown ()throws Exception {
		FileUtils.forceDelete(tmpSonar);
		
	}
	@Before
	public void setUp() throws Exception {
		this.tmpSonar = new File(FileUtils.getTempDirectory(),"tmpFile");
		File sonarProjectFolder= new File(tmpSonar,"sonarProject");
		File scanProjectFolder= new File(tmpSonar,"project");
		FileUtils.forceMkdir(tmpSonar);
		FileUtils.forceMkdir(sonarProjectFolder);
		FileUtils.forceMkdir(scanProjectFolder);
		component = new UserFolderOp();
		final ConfigInfo config = Mockito.mock(ConfigInfo.class);		
		
		Mockito.when(config.getSonarProjectFolder()	).thenReturn(sonarProjectFolder) ;
		
		Mockito.when(config.getScanFolder()	).thenReturn(scanProjectFolder) ;
		
		
		Mockito.when(config. getSonarRunnerHome()).thenReturn(FileUtils.getTempDirectory()) ;
		
		
		ReflectionUtils.setField(component, "config", config);
		

		ProjPropertiesGen generator = Mockito.mock(ProjPropertiesGen.class);
		ReflectionUtils.setField(component, "generator", generator);
		
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

	@Test
	public void testFormatVerSrc() throws Exception { 
		File scanProjectFolder= new File(this.tmpSonar,"project");
		File result = component.formatVerSrc(this.testTarget, scanProjectFolder);
		File expectEdFile = new File(tmpSonar.getAbsolutePath() + File.separator +"project"+ File.separator +TEST_FOLDER+ File.separator +TEST_PJ_V+ File.separator +"src");
		
		Assert.assertEquals(expectEdFile.getCanonicalPath(), result.getCanonicalPath());
		
	}
	@Test
	public void testGetAllVersion() throws Exception {
		component.getAllVersion(testTarget.getInfo());
		
	}
	@Test
	public void testGetSonarProjVerSRC() throws Exception {
		File result = component.getSonarProjVerSRC(this.testTarget);
		File sonarProject = new File(tmpSonar.getAbsolutePath() + File.separator +"sonarProject"+ File.separator +TEST_FOLDER+ File.separator +TEST_PJ_V+ File.separator +"src");
		Assert.assertEquals(sonarProject.getCanonicalPath(), result.getCanonicalPath());
	}
	@Test
	public void testPrepareSonarRunnerEnv1() throws Exception {
		File srcFolder = new File(tmpSonar.getAbsolutePath() + File.separator +"project"+ File.separator +TEST_FOLDER+ File.separator +TEST_PJ_V+ File.separator +"src");
		FileUtils.forceMkdir(srcFolder);
		
		File result = component.prepareSonarRunnerEnv(this.testTarget);
		System.out.println(result);
	}
	@Test
	public void testPrepareSonarRunnerEnv2() throws Exception {
		File srcFolder = new File(tmpSonar.getAbsolutePath() + File.separator +"project"+ File.separator +TEST_FOLDER+ File.separator +TEST_PJ_V+ File.separator +"src");
		FileUtils.forceMkdir(srcFolder);
		File sonarProject = new File(tmpSonar.getAbsolutePath() + File.separator +"sonarProject"+ File.separator +TEST_FOLDER+ File.separator +TEST_PJ_V+ File.separator +"src");
		
		FileUtils.forceMkdir(sonarProject);
		File existedDir = new File(sonarProject,"testDir");
		FileUtils.forceMkdir(existedDir);
		File result = component.prepareSonarRunnerEnv(this.testTarget);
		System.out.println(result);
	}
	
}

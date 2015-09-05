package com.iisigroup.java.tech.controller;


import java.io.File;

import org.apache.commons.io.FileUtils; 
import org.junit.Before;
import org.mockito.Mockito; 

import com.iisigroup.java.tech.controller.operation.UserFolderOp;
import com.iisigroup.java.tech.service.SonarExecService;
import com.iisigroup.java.tech.sonar.business.OSvalidator;
import com.iisigroup.java.tech.sonar.business.ProjPropertiesGen;
import com.iisigroup.java.tech.utils.ReflectionUtils;
import com.iisigroup.scan.folder.internal.UserFolder;
import com.iisigroup.scan.folder.internal.UserProjProfile;

public class SonarExecControllerTest {
	private SonarExecService component ;
	
	@Before
	public void setUp() throws Exception {
		component = new SonarExecService();
		final UserFolderOp op =  Mockito.mock(UserFolderOp.class);
		final ProjPropertiesGen projPropertiesGen =  Mockito.mock(ProjPropertiesGen.class);
		Mockito.when(op.prepareSonarRunnerEnv(Mockito.any(UserFolder.class))).thenReturn(FileUtils.getTempDirectory()) ;
	
		ReflectionUtils.setField(component, "op", op);
		ReflectionUtils.setField(component, "projPropertiesGen", projPropertiesGen);
	}

//	@Test
	public void testExternalCommand() throws Exception {
		String[] commandLins = null;
		String commandline ="exec";
		String commandline02 ="wget  log  \"http://10.0.2.15:8080/codeReviewWeb/manualCR?encoding=UTF8&projectVersion=2015-02-25&projectKey=robert.lee:project&command=GETPCR\" -O  \"李建德'sJAVA處ManualCR_評估準則_查核表單_2015_02_25_1125.xls\" ";
		String commandline03 ="move \"李建德\'sJAVA處ManualCR_評估準則_查核表單_2015_02_25_1125.xls\" D:\\SONAR\\Projects\\1204003李建德\\2015-02-25";
		/***
		 * windows的batch file不支援UTF8編碼內容
		 * 
		 * **/
		 if (OSvalidator.isWindows()) {
			 commandLins = new String[]{ commandline,commandline02,commandline03 };
         } else if (OSvalidator.isUnix()) {
        	 commandLins = new String[]{ "" };
         }
		component.externalCommand(commandLins,new File("D:/SONAR/sonarProjects/1204003李建德/2015-02-25"));
	}

//	@Test
	public void testExeAnalysis() throws Exception {
		final UserFolder folder = Mockito.mock(UserFolder.class);		
		final UserProjProfile info = Mockito.mock(UserProjProfile.class);
		Mockito.when(folder.getInfo()).thenReturn(info) ;
		Mockito.when(folder.getProjectVersion()).thenReturn("2015-02-03") ;
		Mockito.when(folder.getSourceEncoding()).thenReturn("UTF-8") ;
		component.exeAnalysis(folder);
	}
}

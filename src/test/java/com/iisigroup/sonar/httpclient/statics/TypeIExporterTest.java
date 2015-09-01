package com.iisigroup.sonar.httpclient.statics;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.iisigroup.java.tech.utils.Utils;
import com.iisigroup.scan.folder.internal.EmpDto;
import com.iisigroup.scan.folder.internal.UserFolder;
import com.iisigroup.scan.folder.internal.UserProjProfile;
import com.iisigroup.sonar.httpclient.statics.model.MonthlyProjSum;
import com.iisigroup.sonar.httpclient.statics.model.ProjectSum;
import com.iisigroup.sonar.httpclient.statics.model.SumPer2Weeks;

public class TypeIExporterTest {
    private StaticsPreview component1 ;
    private StaticsMonthly component2 ;
    
    @Before
    public void setUp() throws Exception {
        component1 = new StaticsPreview();
        component2 = new StaticsMonthly();
    }

    @After
    public void tearDown() throws Exception {
    }

//    @Test
    public void testExportJxlsV3() throws Exception {
        final   List<ProjectSum> result = component1.calculateProjectsByProjectKey("peijun.jian:project");
        final   SimpleDateFormat sdf  =new SimpleDateFormat("yyyyMMddmmss");
        final   String fileName = "data"+sdf.format(new Date())+".xls" ; 
        final   File targetFile = new File(FileUtils.getTempDirectory(),fileName); 
        
        TypeIExporter.exportJxlsV3(result, targetFile);
    }

//    @Test
    public void testExportJxlsV4() throws Exception {
        final   List<MonthlyProjSum> data = component2.calculateAllProjects();
        final   SimpleDateFormat sdf  =new SimpleDateFormat("yyyyMMddmmss");
        final   String fileName = "month_data"+sdf.format(new Date())+".xls" ; 
        final   File targetFile = new File(FileUtils.getTempDirectory(),fileName);  ;
        TypeIExporter.exportJxlsV4(data, targetFile);
        
    }

//      @Test
    public void testExportXlsV01() throws Exception {
        final   List<MonthlyProjSum> data = component2.calculateAllProjects();
        final   SimpleDateFormat sdf  =new SimpleDateFormat("yyyyMMddmmss");
        final   String fileName = "month_data"+sdf.format(new Date())+".xls" ; 
        final   File targetFile = new File(FileUtils.getTempDirectory(),fileName);  ;
        TypeIExporter.exportXlsV01(data, targetFile);
    }
//     @Test
    public void testExportXlsV02() throws Exception {
        List<SumPer2Weeks> data = component2.cal2WeeksAllProjects();
        final   SimpleDateFormat sdf  =new SimpleDateFormat("yyyyMMddmmss");
        final   String fileName = "2weeks_data"+sdf.format(new Date())+".xls" ;
        final   File tmpFile =  new File(FileUtils.getTempDirectory(),"tmp20150205") ;
//        Utils.serialization(data, tmpFile );
        
//        List<SumPer2Weeks>   data = Utils.deserialization(tmpFile) ;
        final   File targetFile = new File(FileUtils.getTempDirectory(),fileName);  ;
        TypeIExporter.exportXlsV02(data, targetFile);
    }
     
//     @Test
     public void testExportXlsV04() throws Exception {
//        Map<String, List<SumPer2Weeks>> data = component2.cal2WeeksAllProjectsGroupByDepart();
         final   SimpleDateFormat sdf  =new SimpleDateFormat("yyyyMMddmmss");
         final   String fileName = "2weeks_datav04"+sdf.format(new Date())+".xls" ;
         final   File tmpFile =  new File(FileUtils.getTempDirectory(),"tmp20150205") ;
//         Utils.serialization(data, tmpFile );
         
         Map<String, List<SumPer2Weeks>> data = Utils.deserialization(tmpFile) ;
         final   File targetFile = new File(FileUtils.getTempDirectory(),fileName);  ;
         TypeIExporter.exportXlsV04(data, targetFile);
     }
//	@Test
	public void testExportXlsV03() throws Exception {
		final SimpleDateFormat sdf  =new SimpleDateFormat("yyyyMMddmmss");
		final String fileName = "JAVA處ManualCR_評估準則_查核表單_"+sdf.format(new Date())+".xls" ;
        final File tmpFile =  new File(FileUtils.getTempDirectory(),fileName) ;
        
        final  UserProjProfile profile =new UserProjProfile();
        profile.setProjectKey("peijun.jian:project");   
        EmpDto empdata = new EmpDto();
        empdata.setChtName("簡佩君");
        empdata.setEmail("peijun.jian@iisigroup.com");
		profile.setEmpdata(empdata);
        final   UserFolder data =new UserFolder ();
        data.setInfo(profile);
		TypeIExporter.exportXlsV03(data, tmpFile);
	}
}

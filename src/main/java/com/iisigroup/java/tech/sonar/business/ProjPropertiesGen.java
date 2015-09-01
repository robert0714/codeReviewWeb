package com.iisigroup.java.tech.sonar.business;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iisigroup.java.tech.controller.operation.UserFolderOp;
import com.iisigroup.java.tech.utils.Utils;
import com.iisigroup.scan.folder.internal.UserFolder;

/**
 * The Class ProjPropertiesGen.
 */
public class ProjPropertiesGen {
	 /** The Constant logger. */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ProjPropertiesGen.class);
    
    /** The Constant FILE_NAME. */
    public final static String FILE_NAME = "sonar-project.properties";
    public final static String EXEC_FILE_NAME = OSvalidator.isUnix()?"exec.sh":"exec.bat";
    
    /** The Constant TEMPLATE_EXECBAT. */
    private final static String TEMPLATE_EXECBAT = "%s\\bin\\sonar-runner.bat";
    
    /** The Constant TEMPLATE_EXECSH. */
    private final static String TEMPLATE_EXECSH = "sh %s/bin/sonar-runner";
    
    /** The Constant TEMPLATE_CONTENT. */
    private final static String TEMPLATE_CONTENT = "# Required metadata\r"
            + "sonar.projectKey=%s\r"
            + "sonar.projectName=%s\r"
            + "sonar.projectVersion=%s\r"
            + "\r"
            + "# sonar.projectName在windows下使用中文名稱則會出現亂碼 \r"
            + "\r"
            + "# Path to the parent source code directory."
            + "# Path is relative to the sonar-project.properties file. Replace \"\" by \"/\" on Windows. \r"
            + "# Since SonarQube 4.2, this property is optional if sonar.modules is set.  \r"
            + "# If not set, SonarQube starts looking for source code from the directory containing  \r"
            + "# the sonar-project.properties file. \r"
            + "sonar.sources=src \r"
            + "\r" 
            + "# Encoding of the source code\r"
            + "\r" 
            + "sonar.sourceEncoding=%s\r" 
            + "\r"
            + "sonar.skipPackageDesign=true\r" 
            + "\r"
            + "sonar.scm.disabled=true\r" 
            + "\r";
    
    /**
     * Gets the sonar runar cmd content.
     *
     * @param sonarRunnerHome the sonar runner home
     * @return the sonar runar cmd content
     */
    public String getSonarRunarCmdContent(final String sonarRunnerHome){
        if(OSvalidator.isWindows() ){
            return String.format(TEMPLATE_EXECBAT, sonarRunnerHome) ;
        }else if (OSvalidator.isUnix()){
            return String.format(TEMPLATE_EXECSH, sonarRunnerHome) ;
        }else{
            return String.format(TEMPLATE_EXECSH, sonarRunnerHome) ;
        }
    }
    
    public String moveXls2Project(final UserFolder folder  ){
    	String finalFileName =getFileName(folder);
    	if(OSvalidator.isWindows() ){
    		 return String.format("move \"%s\" %s", finalFileName ,new UserFolderOp().getProjFolderVer(folder)) ;            
        }else if (OSvalidator.isUnix()){
            return String.format("mv \"%s\" %s", finalFileName ,new UserFolderOp().getProjFolderVer(folder)) ;
        }else{
        	return String.format("mv \"%s\" %s", finalFileName ,new UserFolderOp().getProjFolderVer(folder)) ;
        }
    }
    public String getFileName(final UserFolder folder){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HHmm");
		String templateFileName = "%s'sJAVA處ManualCR_評估準則_查核表單_%s.xls";
		String finalFileName = String.format(templateFileName, folder.getInfo()
				.getEmpdata().getEmpId(), sdf.format(new Date()));
		return finalFileName ; 
    }
	public String getWgetTemplate(final UserFolder folder) {
		String finalFileName =getFileName(folder);
		String ip4 = Utils.getIpv4();
		String template = null;
		if (OSvalidator.isWindows()) {
			template = "wget   \"http://%s:8080/codeReviewWeb/manualCR?encoding=UTF8&projectVersion=%s&projectKey=%s&command=GETPCR\" -O  \"%s\"  ";

		} else {
			template = "wget -c -o log  \"http://%s:8080/codeReviewWeb/manualCR?encoding=UTF8&projectVersion=%s&projectKey=%s&command=GETPCR\" -O  \"%s\"  ";
		}
		return String.format(template, ip4, folder.getProjectVersion(), folder
				.getInfo().getProjectKey(), finalFileName);
	}
    
    /**
     * Gets the sonar runner script.
     *
     * @return the sonar runner script
     */
    public String getSonarRunarCmdContentExt(final UserFolder folder,final String sonarRunnerHome){
        File homedirectory = new File(sonarRunnerHome);
        String result =null;
        if(homedirectory.exists()){ 
		 
			/**
			 * 由於Windos的batch file不支援ASCII以外編碼
			 *
			 * **/
			if(OSvalidator.isWindows() ){
				return  getSonarRunarCmdContent(sonarRunnerHome) ;
				
	        }else{ 
	        	
	        	result =getSonarRunarCmdContent(sonarRunnerHome)
				+ StringUtils.LF
				+  getWgetTemplate(folder )
				+ StringUtils.LF
				+ moveXls2Project(folder );
	        	
	        }           
        }
        return result;
    }
    /**
     * Gets the sonar config content.
     *
     * @param data the data
     * @return the sonar config content
     */
    public String getSonarConfigContent(final UserFolder data) {
        final String result = String.format(TEMPLATE_CONTENT, data.getInfo()
                .getProjectKey(), data.getInfo().getProjectName(), data
                .getProjectVersion(), data.getSourceEncoding());

        return result;
    }
}

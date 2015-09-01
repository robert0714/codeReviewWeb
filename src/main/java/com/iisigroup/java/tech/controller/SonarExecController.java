package com.iisigroup.java.tech.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset; 
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.iisigroup.java.tech.controller.operation.UserFolderOp;
import com.iisigroup.java.tech.sonar.business.OSvalidator;  
import com.iisigroup.java.tech.sonar.business.ProjPropertiesGen;
import com.iisigroup.scan.folder.ConfigInfo; 
import com.iisigroup.scan.folder.internal.EmpFactory;
import com.iisigroup.scan.folder.internal.UserFolder;
import com.iisigroup.scan.folder.internal.UserProjProfile;

/**
 * The Class SonarExecController.
 */
@Component
public class SonarExecController {
    /** The Constant logger. */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(SonarExecController.class);

    private  final UserFolderOp op = new UserFolderOp();
    
    
    /** The config. */
    private final ConfigInfo config = new ConfigInfo();
    
    private final ProjPropertiesGen projPropertiesGen = new ProjPropertiesGen();
    /**
     * Exec by project key version.
     * 
     * @param projectKey
     *            the project key
     * @param projectVersion
     *            the project version
     * @return the string
     */
    public String execByProjectKeyVersion(final String projectKey,
            final String projectVersion, final String encoding) {
        if (projectKey == null || projectVersion == null || encoding == null) {
            throw new NullPointerException(
                    String.format(
                            "projectKey is %s , projectVersion is %s , encoding is %s  ",
                            projectKey, projectVersion, encoding));

        }
        final EmpFactory factory = EmpFactory.getEmpFactory();

        final Map<String, UserProjProfile> map = factory
                .getUserProjProfileMapForCodeReview();

        final UserProjProfile info = map.get(projectKey);
        final UserFolder folder = new UserFolder();

        folder.setInfo(info);
        folder.setProjectVersion(projectVersion);
        folder.setSourceEncoding(encoding);

        return exeAnalysis(folder);
    }

    public String getLog(final UserFolder folder) {
        String result = null;
        final File folderFile = this.op.getLogFileFolder(folder);
        if (folderFile != null && folderFile.isDirectory()
                && folderFile.exists()) {
            final StringBuilder sbf = new StringBuilder();
            for (File file : folderFile.listFiles()) {
                if (file.isFile()) {

                    try {
                        sbf.append(FileUtils.readFileToString(file, "UTF8"))
                                .append(StringUtils.CR);
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage(), e);

                    }
                }
            }
            result = sbf.toString();
        }

        return result;
    }

    /**
     * Exe sonar analysis.
     * 
     * @param folder
     *            the folder
     * @return the string
     */
    public String exeAnalysis(final UserFolder folder) {

        // 產生sonar 專案資料夾之UserFolder對應sonar設定檔以及指令檔,以及上傳資料夾的一切複製到sonar執行目錄下以利準備執行sonar 分析
        final File underTarget = this.op.prepareSonarRunnerEnv(folder);       
 		String log = null;
        if (underTarget != null ) {
        	//get sonar runner executable dictionary..
            final File sonarRunnerHome = this.config. getSonarRunnerHome();    
            	final  String wget =  this.projPropertiesGen. getWgetTemplate( folder);
            	final  String moveScript =    this.projPropertiesGen. moveXls2Project(folder);
        	 if (OSvalidator.isWindows()) {
        		 //因為windows 的批次檔案不支援ASCCII以外編碼....所以程式碼才那麼難看        		  
     			
        		 
                 log = 	 externalCommand(new String[]{"exec.bat" , wget ,moveScript }, underTarget);
             } else if (OSvalidator.isUnix()) {
               	String command = this.projPropertiesGen. getSonarRunarCmdContent( sonarRunnerHome.getAbsolutePath());
            	 log =  externalCommand(new String[]{command, wget ,moveScript}, underTarget);
             }
        	
        }
        return log;
    }
    public String externalCommand(final String[] commandLins,final File workDirectory){
    	final StringBuilder sbf = new StringBuilder();
    	if(ArrayUtils.isNotEmpty(commandLins)){
    		for(String commandLine : commandLins){
    			 String content = null;
                /**
                 * 產生子process(程序)呼叫sonar runner
                 * */              
                Process processs = null;

                try {
                    String[] cmd = null;
                    if (OSvalidator.isWindows()) {
                        cmd = new String[] { "cmd.exe", "/c", commandLine };
                        /***
                         * 由於目錄名稱是中文字,轉成UTF8有變成亂碼的機會，所以還是沿用MS950
                         * http://www.microsoft.com/resources/documentation/windows/xp/all/proddocs/en-us/chcp.mspx?mfr=true
                         * cmd =new String[] {"cmd.exe","/c","chcp","65001","&","cmd.exe","/c","dir" };
                         * chcp，使用chcp加上Code Page代碼，就可切換指定的語系。UTF-8的Code Page是65001
                         * cmd =new String[] {"cmd.exe","/c","chcp","65001","&","cmd.exe","/c",
                         * initSonarRunnerScript};
                         * ****/
                    } else if (OSvalidator.isUnix()) {
                    	  cmd = new String[] {"/bin/sh", "-c",  commandLine };
                    }

                    ProcessBuilder pb = new ProcessBuilder(cmd);
                    pb.redirectErrorStream(true);
                    pb.directory(workDirectory);

                    /* Start the process */
                    processs = pb.start();
//                    LOGGER.info("Process started !");

                    // 可以參考http://www.4byte.cn/question/310459/processbuilder-hanging-on-the-readline-method.html
                    // 來進行修改

                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                } finally {
                    if (processs != null) {
                        BufferedReader in = null;

                        String sourceEncoding = Charset.defaultCharset().name();
                        LOGGER.info("sourceEncoding: {}" , sourceEncoding);
                        try {
                            if (OSvalidator.isWindows()) {
                                in = new BufferedReader(new InputStreamReader(
                                        processs.getInputStream(), "MS950"));
                                /**
                                 * processs.getInputStream(), "UTF-8"));
                                 * ***/
                                
                                /**
                                 * 由於目錄名稱是中文字,轉成UTF8有變成亂碼的機會，所以還是沿用MS950
                                 * http://www.microsoft.com/resources/documentation/windows/xp/all/proddocs/en-us/chcp.mspx?mfr=true
                                 * cmd =new String[] {"cmd.exe","/c","chcp","65001","&","cmd.exe","/c","dir" };
                                 * chcp，使用chcp加上Code Page代碼，就可切換指定的語系。UTF-8的Code Page是65001
                                 * ****/
                            } else {
                                in = new BufferedReader(new InputStreamReader(
                                        processs.getInputStream(), "UTF-8"));
                            }

                            content = IOUtils.toString(in);
//                            LOGGER.info("sonar Runar \r:{}", content);
                            LOGGER.info(content);
                            sbf.append(content) ;
                        } catch (IOException e) {
                            LOGGER.error(e.getMessage(), e);
                        } finally {
                            if (in != null) {
                                try {
                                    in.close();
                                } catch (IOException e) {
                                    LOGGER.error(e.getMessage(), e);
                                }
                            }
                        }
                        /* Clean-up */
                        processs.destroy();
//                        LOGGER.info("Process ended !");
                         
                    }
                }
            
    		}
    	}
    	return sbf.toString();
    } 
}

package com.iisigroup.java.tech.controller.operation;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iisigroup.java.tech.sonar.business.ProjPropertiesGen;
import com.iisigroup.scan.folder.ConfigInfo;
import com.iisigroup.scan.folder.internal.UserFolder;
import com.iisigroup.scan.folder.internal.UserProjProfile;


/**
 * The Class UserFolderOp.
 * 對sonar 專案資料夾進行一切的相關操作負責元件
 */
public class UserFolderOp {
    /** The Constant logger. */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(UserFolderOp.class);
    
    /** The Constant encoding. */
    private static final String encoding = Charsets.UTF_8.toString();
    
    /** The generator. */
    private final  ProjPropertiesGen generator = new ProjPropertiesGen();
    
    /** The config. */
    private final ConfigInfo config = new ConfigInfo();
    
    /**
     * Gets the all version.
     *
     * @param info the info
     * @return the all version
     */
    public List<String> getAllVersion(final  UserProjProfile info){
        try {
        	
            final File needScanFolder  =   this.config.getScanFolder();
            final File directory = new File(needScanFolder,info.getFolderName()  );           
            if(!directory.exists()){
            	FileUtils.forceMkdir(directory);            	
            }
            final  String[] a = directory.list() ;
            final   List<String> tmp = Arrays.asList(a);
            Collections.sort(tmp);
            return  tmp;
        } catch (IOException e) {
            LOGGER.error(e.getMessage() , e );
        }
       return  new  ArrayList<String>(0);
    }

	/**
	 * Format ver info.
	 *
	 * @param folder the folder
	 * @param dictionary the dictionary
	 * @return the file
	 */
	protected File formatVerInfo(final UserFolder folder, final File dictionary) {
		final File verDirectory = new File(dictionary.getAbsolutePath()
				+ File.separator + folder.getInfo().getFolderName()
				+ File.separator + folder.getProjectVersion());
		return verDirectory;
	}
	
	/**
	 * Format ver src.
	 *
	 * @param folder the folder
	 * @param dictionary the dictionary
	 * @return the file
	 */
	protected File formatVerSrc(final UserFolder folder, final File dictionary) {
		final File verDirectory = new File(dictionary.getAbsolutePath()
				+ File.separator + folder.getInfo().getFolderName()
				+ File.separator + folder.getProjectVersion() + File.separator
				+ "src");
		return verDirectory;
		
	}
    /**
     * Gets the scanned folder.
     * 跟據UserFolder 而得到其所對應的上傳最初 資料夾 相關資訊
     * @param folder the folder
     * @return the scanned folder ver
     */
    public File getProjFolderVer(final  UserFolder folder){ 
        final File scanFolder  =   this. config.getScanFolder();       
        return formatVerInfo(folder, scanFolder); 
    }
    /**
     * Gets the sonar proj ver.
     * 跟據UserFolder 而得到其所對應的sonar 資料夾 應該是如何的 相關資訊
     * @param folder the folder
     * @return the sonar proj ver
     */
    public File getSonarProjVer(final  UserFolder folder){
        final File sonarRootFolder  =  this. config.getSonarProjectFolder();       
        return formatVerInfo(folder, sonarRootFolder); 
    }
    
    /**
     * Gets the sonar proj ver log  
     * 跟據UserFolder 而得到其所對應的sonar之 log 資料夾下的log關資訊.
     *
     * @param folder the folder
     * @return the sonar proj ver  log
     */
    public File getLogFile(final  UserFolder folder){
    	final String fileName = String.format("%s_%s.log", folder.getInfo().getEmpdata().getEmpId() ,folder.getProjectVersion() ) ;
    	return new File ( getSonarProjVer(folder).getAbsolutePath() + File.separator +"LOG" + File.separator + fileName);
    }
    
    /**
     * Gets the sonar proj ver log  folder
     * 跟據UserFolder 而得到其所對應的sonar之 log 資料夾下 .
     *
     * @param folder the folder
     * @return the sonar proj ver  log  folder
     */
    public File getLogFileFolder(final  UserFolder folder){
    	 return new File ( getSonarProjVer(folder),"LOG"  );
    }
    /**
     * Gets the sonar proj ver source code source folder.
     * 跟據UserFolder 而得到其所對應的sonar 資料夾 應該是如何的 相關資訊
     * @param folder the folder
     * @return the sonar proj ver source folder
     */
    public File getProjFolderVerSRC(final  UserFolder folder){
    	  final File scanFolder  =  this.config.getScanFolder();       
         return formatVerSrc(folder, scanFolder); 
    }
    /**
     * Gets the sonar proj ver source code source folder.
     * 跟據UserFolder 而得到其所對應的sonar 資料夾 應該是如何的 相關資訊
     * @param folder the folder
     * @return the sonar proj ver source folder
     */
    public File getSonarProjVerSRC(final  UserFolder folder){
    	 final File sonarRootFolder  =   this.config.getSonarProjectFolder();       
         return formatVerSrc(folder, sonarRootFolder); 
    }
    /**
     * preparation for  sonar runner environment.
     * 產生sonar 專案資料夾之UserFolder對應sonar設定檔以及指令檔,
     * 以及上傳資料夾的一切複製到sonar執行目錄下以利準備執行sonar 分析
     * 
     * @param folder the folder
     * @return the file
     */
    public File prepareSonarRunnerEnv( final  UserFolder folder){
        
        /***
         * 產生sonar project configuration content
         * **/
        final String sonarCnfigContent =   this.generator.getSonarConfigContent(folder);
        
        
        try {
        	//get sonar runner executable dictionary..
            final File sonarRunnerHome = this.config. getSonarRunnerHome();            
           
            /***
             * 產生sonar project executable file
             * **/
            final String execContent =   this.generator.
//            		getSonarRunarCmdContent(sonarRunnerHome.getCanonicalPath());
            		getSonarRunarCmdContentExt(folder , sonarRunnerHome.getCanonicalPath());
            
            final File sonarProjVerSRC = getSonarProjVerSRC(folder);   
                        
            if(sonarProjVerSRC != null && !sonarProjVerSRC.exists()){
            	//建立該對應的版本資料夾
                FileUtils.forceMkdir(sonarProjVerSRC);				
            }else{
                for (File aFile : sonarProjVerSRC.listFiles()) {
                    if (aFile.isFile()) {
                    	LOGGER.debug("delete File:{}" ,aFile.getCanonicalPath());
                    	
                        FileUtils.deleteQuietly(aFile);
                    } else if (aFile.isDirectory()) {
                        FileUtils.deleteDirectory(aFile);
                    }
                }
            }
            //將上傳版本資料夾的一切複製
            
            final File projFolderVer =getProjFolderVer(folder);
                      
            final FileFilter fileFilter = new FileFilter(){
                @Override
                public boolean accept(File pathname) {
                    try {
                        if(pathname!=null && !StringUtils.containsIgnoreCase(pathname.getCanonicalPath(), ".svn")){
                            return true ; 
                        }
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage() ,e ); 
                        return false;
                    } 
                    return false;
                }};
            
            //複製動作以後
            FileUtils.copyDirectory(projFolderVer, sonarProjVerSRC , fileFilter , true);
			
            
            final File sonarConfigFile = new File(sonarProjVerSRC.getParentFile(),generator.FILE_NAME );
                   
            final File sonarRunnerExecFile = new File(sonarProjVerSRC.getParentFile(), generator.EXEC_FILE_NAME );
            
            //將設定檔寫入硬碟
            FileUtils.writeStringToFile(sonarConfigFile, sonarCnfigContent, encoding);
            
            //將執行檔寫入硬碟
            FileUtils.writeStringToFile(sonarRunnerExecFile, execContent, encoding);
              
            return sonarProjVerSRC.getParentFile() ;
        } catch (IOException e) {
            LOGGER.error(e.getMessage() , e );
        } finally {
        }
        return null ;
    }
}

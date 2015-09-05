package com.iisigroup.scan.folder;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils; 
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iisigroup.java.tech.controller.internal.XlsFilenameFilter;
import com.iisigroup.java.tech.controller.operation.UserFolderOp;
import com.iisigroup.java.tech.service.PersonService;
import com.iisigroup.java.tech.utils.notification.NotificationMgr;
import com.iisigroup.scan.folder.internal.UserFolder;
import com.iisigroup.scan.folder.internal.UserProjProfile;

/**
 * The Class DetectModificationInFolder.
 */
public class DetectModificationInFolder {
    /** The Constant logger. */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DetectModificationInFolder.class);
    
    private UserFolderOp op = new UserFolderOp();
    /**
     * Scan folder preparation.
     * 啟動執行時機為每日定時,以及發信通知時這兩個時間點
     */
    public void scanFolderPreparation(){
       final PersonService perCtrl = new PersonService();
       final List<UserFolder> allUser = perCtrl.getAllUserByLdap() ;
       final  List<UserFolder> needToNotify = new ArrayList<UserFolder>();
        
       for(final UserFolder folder : allUser){
          
           try {
               
               final File verDirectory = this. op.getProjFolderVer(folder);
               final File sonarverDirectory =  this. op.getSonarProjVer(folder);
               
               final File userFolder = verDirectory.getParentFile();
               
               if(!verDirectory.exists()){
                   FileUtils.forceMkdir(verDirectory);
               } 
               deleteUnusedFolder(userFolder);
               deleteSonarUnusedFolder(sonarverDirectory.getParentFile());
               
               folderOperation( userFolder ,folder) ;
               
               final  String absolutePath = verDirectory.getAbsolutePath();
//               LOGGER.info("{} FileUtils.sizeOf(verDirectory): {}",absolutePath ,  FileUtils.sizeOf(verDirectory) );
//               LOGGER.info("{} size(): {}" ,absolutePath ,  FileUtils.byteCountToDisplaySize(FileUtils.sizeOf(verDirectory)) );
//               LOGGER.info("{} length(): {}" ,absolutePath , verDirectory.length() );
               if(FileUtils.sizeOf(verDirectory) > 0 ){
            	   //發現當日的有異動進行通知
            	   needToNotify.add(folder);
               }
           } catch (IOException e) {
               LOGGER.error(e.getMessage() , e );
           }
       }
       if(CollectionUtils.isNotEmpty(needToNotify)){
    	   new    NotificationMgr().processUserFolders(needToNotify);
       }
      
    }
    
    
    /**
     * Delete unused folder.
     * 移除前日的空資料日期目錄
     * @param rootFolder the root folder
     */
    protected void deleteSonarUnusedFolder(final File rootFolder){       
        if (ArrayUtils.isNotEmpty(rootFolder.listFiles())) {
            final File[] listFiles = rootFolder.listFiles();
            for (int i = 0; i < listFiles.length; ++i) {
            	final File targetDir = listFiles[i] ; 
                final boolean sameAsToday = com.iisigroup.java.tech.utils.DateUtils.getNowDate().equals(targetDir.getName());
                if(targetDir.isDirectory() && !sameAsToday){                    
                    final Collection<File> subFiles = FileUtils.listFiles(targetDir, new String[]{"java","xlsx","xls"},true);
                    if(CollectionUtils.isEmpty(subFiles)){
                        
                        try {
                        	LOGGER.debug("delete File:{}" ,targetDir.getAbsolutePath());
                            FileUtils.deleteDirectory(targetDir);
                        } catch (IOException e) {
                            LOGGER.info("{} size is {}" ,  targetDir.getAbsolutePath() ,String.valueOf(FileUtils.sizeOfDirectory(targetDir)));
                            LOGGER.error(e.getMessage() ,e );
                        }
                        
                    }                    
                }
            }
        }
    }
    
    /**
     * Delete unused folder.
     * 移除前日的空資料日期目錄
     * @param rootFolder the root folder
     */
    protected void deleteUnusedFolder(final File rootFolder){       
        if (ArrayUtils.isNotEmpty(rootFolder.listFiles())) {
            final File[] listFiles = rootFolder.listFiles();
            for (int i = 0; i < listFiles.length; ++i) {
                final boolean sameAsToday = com.iisigroup.java.tech.utils.DateUtils.getNowDate().equals(listFiles[i].getName());
                if(listFiles[i].isDirectory() && !sameAsToday){
                    final long sizeOfDirectory = FileUtils.sizeOfDirectory(listFiles[i]);
                    final  String[] fileNmaes = listFiles[i].list();
                    boolean noXLS = true ;
                    if(ArrayUtils.isNotEmpty(fileNmaes)){
                    	for(final String name : fileNmaes){
                    		if(StringUtils.containsIgnoreCase(name, ".xls")
                    				||StringUtils.containsIgnoreCase(name, ".xlsx")
                    				){
                    			noXLS = false;
                    		}
                    	}
                    }else {
                    	noXLS = true ; 
                    }
                    if(sizeOfDirectory == 0  && noXLS == true){
                        try {
                            FileUtils.deleteDirectory(listFiles[i]);
                            LOGGER.info("delete {} successfully" ,  listFiles[i].getAbsolutePath() );
                        } catch (IOException e) {
                            LOGGER.info("{} size is {}" ,  listFiles[i].getAbsolutePath() ,String.valueOf(sizeOfDirectory));
                            LOGGER.error(e.getMessage() ,e );
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Folder operation.
     * 專案資料夾進行業務邏輯的複製、壓縮、搬移到備份資料夾
     * @param rootFolder the root folder
     * @param folder the folder
     */
    protected void folderOperation(final File rootFolder,
            final UserFolder folder) {
        if (ArrayUtils.isNotEmpty(rootFolder.listFiles())) {
            final File[] listFiles = rootFolder.listFiles();
            for (int i = 0; i < listFiles.length; ++i) {
                if (listFiles[i].isDirectory()) {
                    final String projectVersion = listFiles[i].getName();
                    folder.setProjectVersion(projectVersion);
                    

                    // 得到所對應的sonar 資料夾資訊
                    final File targetSonarProject = this. op
                            .getSonarProjVerSRC(folder);

                    // 複製資料夾過去後...便將此資料夾進行壓縮移動到backup資料夾
                    try {

                        FileUtils.forceMkdir(targetSonarProject);
                      
                        
                        
                        //要將複製過去目錄下的excel [ JAVA處ManualCR_評估準則_查核表單.xlsx ]從src移除
                        // 
//                        removeXLSFromSrc(folder);
                        
                    } catch (IOException e) {
                        LOGGER.info("{} size is {}", listFiles[i]
                                .getAbsolutePath(), String.valueOf(FileUtils
                                .sizeOfDirectory(listFiles[i])));
                        LOGGER.error(e.getMessage(), e);
                    }

                    // TODO 進行壓縮移動到backup資料夾

                }
            }
        }
    } 
    @Deprecated
    public void removeXLSFromSrc(final UserFolder folder){
    	 File[] xlsFiles = manualCRFiles(folder);
    	 for(File file : xlsFiles){
    		 	LOGGER.debug("delete File:{}" ,file.getAbsolutePath());
				FileUtils.deleteQuietly(file) ;
			 
    		 
    	 }
    }
    protected File[]  manualCRFiles(final UserFolder folder){
		final File sonarProjFolderVer = new UserFolderOp().getSonarProjVerSRC(folder);
		final XlsFilenameFilter xlsfilter = new XlsFilenameFilter();
		final File[] array = sonarProjFolderVer.listFiles(xlsfilter);
		return array ;
	}
    /**
     * Gets the user proj profile map.
     *
     * @return the user proj profile map
     */
    protected Map<String, UserProjProfile> getUserProjProfileMap() {
        final Map<String, UserProjProfile> result = new HashMap<String, UserProjProfile>();

        return result;
    }
    
    /**
     * Process directory.
     *
     * @param acceptExam the accept exam
     * @param info the info
     */
    public void processDirectory(final File acceptExam ,final UserProjProfile info) {
        if(acceptExam == null || acceptExam.isFile()){
            return ;
        }
        final  String folderName = acceptExam.getName();
        final  File[] listFiles = acceptExam.listFiles();
        for(final File aFile : listFiles ){
            if(aFile.isFile()){
                //如果是檔案則是搬移檔案到該project的今日
                
            }else if(aFile.isDirectory() && aFile.getName().matches("\\d{1-4}-\\d\\d-\\d\\d")){
                //如果目錄名稱是符合2014-11-27此格式,就開始搬移至所對應porject
             
                
            }  
        }
        
    }
    
}

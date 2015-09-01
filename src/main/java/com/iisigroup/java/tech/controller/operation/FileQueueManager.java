package com.iisigroup.java.tech.controller.operation;

import java.io.File;
import java.io.IOException; 
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iisigroup.java.tech.controller.SonarExecController;
import com.iisigroup.java.tech.utils.DateUtils;
import com.iisigroup.java.tech.utils.Utils;
import com.iisigroup.scan.folder.ConfigInfo;
import com.iisigroup.scan.folder.internal.UserFolder;
import com.iisigroup.sonar.httpclient.statics.TypeIExporter;

// TODO: Auto-generated Javadoc
/**
 * The Class FileQueueManager.
 */
public class FileQueueManager implements QueueManager {
	 /** The Constant logger. */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(FileQueueManager.class);
	
	/** The config. */
	private ConfigInfo config = new ConfigInfo();
	
	/** The tasks state. */
	private static ConcurrentMap<TaskState, List<UserFolder>> TASKS_STATE = new ConcurrentHashMap<TaskState, List<UserFolder>>() ; 
	
	/**
	 * Gets the state by enp uid.
	 *
	 * @param empUid the emp uid
	 * @return the state by enp uid
	 */
	public String  getStateByEnpUid(String empUid){		
		if(ArrayUtils.contains(getQueuedEmpIds() ,empUid)){
			return "等待處理中";
		}
		final List<UserFolder> processList = TASKS_STATE.get(TaskState.PROCESSING);
		if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(processList)  ){
			for(UserFolder unit : processList){
				if(StringUtils.equalsIgnoreCase(unit.getInfo().getEmpdata().getEmpId(), empUid)){
					return String.format("版本: %s 處理中", unit.getClass());
				}
			}
			return "未處理";
		}else {
			//檢查是否處理完畢?
			List<UserFolder> list = TASKS_STATE.get(TaskState.FINISH);
			if(list!=null ){
				for(UserFolder ut : list){
					if(StringUtils.equalsIgnoreCase(ut.getInfo().getEmpdata().getEmpId(), empUid)){
						return "已處理完畢";
					}
				}
			}
			return "未處理";
		}
	}

	/**
	 * Gets the file name.
	 *
	 * @param unit the unit
	 * @return the file name
	 */
	public String getFileName(final UserFolder unit) {
		return String.format("%s_%s", unit.getInfo().getEmpdata().getEmpId(),
				unit.getProjectVersion());
	}
	 
	/**
	 * *
	 * 處理駐列.
	 */
	public void processQueue(){
		final List<UserFolder> processList = TASKS_STATE.get(TaskState.PROCESSING);
		if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(processList)  ){
			//如果別的執行序已經執行..就skip
		    LOGGER.info("發現別的執行序已經執行..進行skip");
			return;
		}
		final ExecutorService service = Executors.newFixedThreadPool(1);
		final List<FileQueueProcessTask> tasks = new ArrayList<FileQueueProcessTask>(); 
		for(int i =0 ;i < getQueuedEmpIds().length ;++i ){
			tasks.add(new FileQueueProcessTask());
		}
		if(tasks.size() > 0){
			try {
	             List<Future<Boolean>> futureList = service.invokeAll(tasks, 10000, TimeUnit.SECONDS);
	             for(Future<Boolean> ft : futureList){
	            	final   Boolean unitResult = ft.get();
	             }
	        } catch (InterruptedException e) {
	        	LOGGER.error(e.getMessage(), e);
	        } catch (ExecutionException e) {
	        	LOGGER.error(e.getMessage(), e);
			} finally {
	            service.shutdown();
	        }
		}
	}
	
	/**
	 * Process queued.
	 *
	 * @return the boolean
	 */
	protected Boolean processQueued() {

		// TODO　考慮以後實作多執行序的問題
		final UserFolder theOldest = poll();

		if (theOldest != null) {
			boolean success = false ;
			try {

				offerProcessingState(theOldest);
				final SonarExecController controller = new SonarExecController();

				// 得到處理紀錄
				final String processLog = controller
						.exeAnalysis(theOldest);

				// 留下log紀錄以便查閱失敗原因
				recordProcessLog(theOldest, processLog);
				success = true ;
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);

				return false;
			} finally {
				// 清除「正在處理」的資訊
				pollProcessingState(theOldest);
//				if(success){
//					generateReport(theOldest);
//				}
			}
		}
		return true;
	}
	
	
	
	/**
	 * 加入處理中資訊
	 * *.
	 *
	 * @param unit the unit
	 */
	protected void offerProcessingState(final UserFolder unit ){
		List<UserFolder> list = TASKS_STATE.get(TaskState.PROCESSING);
		if(list == null ){
			list = new ArrayList<UserFolder>();
		}
		list.add(unit);
	}
	
	/**
	 * 加入處理中資訊
	 * *.
	 *
	 * @param unit the unit
	 */
	protected void pollProcessingState(final UserFolder unit ){
		List<UserFolder> list = TASKS_STATE.get(TaskState.PROCESSING);
		if(list == null ){
			list = new ArrayList<UserFolder>();
		}
		list.remove(unit);
	}
	
	/**
	 * 留下log紀錄以便查閱失敗原因.
	 *
	 * @param theOldest the the oldest
	 * @param processLog the process log
	 */
	public void recordProcessLog(final UserFolder theOldest,final String processLog){
		final File logfile = new UserFolderOp().getLogFile(theOldest);
		try {
			
			FileUtils.forceMkdir(logfile.getParentFile());
			
			//[2014/12/06-14:11:05.026]
			final String summary = String.format("[%s] %s", DateUtils.getNowTime() ,StringUtils.CR);
			
			
			FileUtils.writeStringToFile(logfile, summary, "UTF8",true);
			
			FileUtils.writeStringToFile(logfile, processLog, "UTF8",true);
			
			LOGGER.error("writing to File:{}", logfile.getAbsolutePath());
			List<UserFolder> list = TASKS_STATE.get(TaskState.FINISH);
			if(list == null ){
				list = new ArrayList<UserFolder>();
			}
			list.add(theOldest);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			LOGGER.error(ToStringBuilder.reflectionToString(theOldest) );
		}
	}
	
	/**
	 * 取出處理駐列.
	 *
	 * @return the user folder
	 */
	public UserFolder poll(){
		final	File theOldestFile = getTheOldestFile();
		if(theOldestFile.exists()){
		    final UserFolder theOldest = getTheOldest(theOldestFile);          
	                theOldestFile.delete();
	                return theOldest;
		}else{
		    return null;
		} 
	}
	
	/**
	 * 加入處理駐列.
	 *
	 * @param unit the unit
	 */
	public void offer(final UserFolder unit) {
		try {
			final String targetFileName =  getFileName(unit);
			final File fileQueueFolder = config.getFileQueueFolder();
			if(fileQueueFolder !=null ){
				FileUtils.forceMkdir(fileQueueFolder);
			}else if(fileQueueFolder.isFile() ){
				FileUtils.forceDelete(fileQueueFolder);
			}
			if(!ArrayUtils.contains(fileQueueFolder.list(), targetFileName)){
				//發現不存在才有必要加入駐列
				Utils.serialization(unit, new File(fileQueueFolder.getCanonicalPath() +File.separator + targetFileName));
			}else {
				LOGGER.error("file already exists: {} " , ToStringBuilder.reflectionToString(unit));				
			}
			
		} catch (IOException e) {
			LOGGER.error(e.getMessage() , e );
			
		}		
	}
	 
	/**
	 * Gets the queued emp ids.
	 *
	 * @return 傳駐列中的員工編號(包含正在執行中的)
	 */
	public String[] getQueuedEmpIds(){
		final File fileQueueFolder = config.getFileQueueFolder();
		if(fileQueueFolder !=null && fileQueueFolder.isDirectory()){
			 final	String [] filenames = fileQueueFolder.list();
			 final String [] result =new String[filenames.length];
			 for(int i = 0 ; i < filenames.length ; ++i ){
				 String fileName  =  filenames [i] ;
				 final String[] tmp = StringUtils.splitPreserveAllTokens(fileName ,"_");
				 result[i]  = StringUtils.trim(tmp[0]);
			 }
			 return result ;
		}else{
			return new String[0];
		}
	}
	
	/**
	 * 回傳最舊的排程檔案.
	 *
	 * @return 最舊的排程檔案
	 */
	public File getTheOldestFile() {
		final File fileQueueFolder = config.getFileQueueFolder();
		final File[] data = fileQueueFolder.listFiles();
		if (data != null ) {
			final List<File> files = Arrays.asList(fileQueueFolder.listFiles());
			Collections.sort(files, new TheOldestFileCp());
			if (CollectionUtils.isNotEmpty(files)) {
				
				return files.get(0);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	/**
	 * 回傳最舊的排程檔案.
	 *
	 * @param target the target
	 * @return 最舊的排程檔案
	 */
	public UserFolder getTheOldest(final File target) { 
		if(target != null || target.exists()){
			return (UserFolder) Utils.deserialization(target);
		}else{
			return null; 
		}
	}
	
	/**
	 * 回傳最舊的排程檔案.
	 *
	 * @return 最舊的排程檔案
	 */
	public UserFolder getTheOldest() {
		final File target = getTheOldestFile();
		if(target != null){
			return (UserFolder) Utils.deserialization(target);
		}else{
			return null; 
		}
	}
	
	/**
	 * The Class TheOldestFileCp.
	 */
	class TheOldestFileCp implements Comparator<File>{
		
		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(File o1, File o2) {
			return (int)(o1.lastModified() - o2.lastModified() );
		}
		
	}
	
	/**
	 * The Class FileQueueProcessTask.
	 */
	class FileQueueProcessTask implements Callable<Boolean> {
        
        /* (non-Javadoc)
         * @see java.util.concurrent.Callable#call()
         */
        @Override
        public Boolean call() throws Exception {
            return processQueued();
        }
    }
	
	/**
	 * The Enum TaskState.
	 */
	static public  enum TaskState{
		
		/** The unbegining. */
		UNBEGINING,//未處理
		/** The processing. */
PROCESSING,//處理中
		/** The queued. */
QUEUED,//等待處理
		/** The finish. */
FINISH,//處理完畢
		;
		
	}
}

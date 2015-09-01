package com.iisigroup.java.tech.utils.notification;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.iisigroup.java.tech.utils.DateUtils;
import com.iisigroup.java.tech.utils.GmailUtils;
import com.iisigroup.java.tech.utils.Utils;
import com.iisigroup.scan.folder.ConfigInfo;
import com.iisigroup.scan.folder.internal.CodeReviwEvent;
import com.iisigroup.scan.folder.internal.EmpDto;
import com.iisigroup.scan.folder.internal.EmpFactory;
import com.iisigroup.scan.folder.internal.UserFolder;
import com.iisigroup.scan.folder.internal.UserProjProfile;
 
/**
 * The Class NotificationMgr.
 */
public class NotificationMgr {
    /** The Constant logger. */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(NotificationMgr.class);
    
    /** The uploader already done. */
    private String uploaderAlreadyDone = "員工編號:%s , 姓名:%s 已於%s進行檔案上傳";
    
    /** The general title. */
    private String generalTitle = "Code Review執行通知";
    
    /** The sum end. */
    private String sumEnd = String.format(
            "請到[http://%s:8080/codeReviewWeb/]個人程式原始碼 findbugs 分析執行頁面 .."
                    + "\n\n執行分析與下載ManualCR_評估準則_查核表單", Utils.getIpv4());

    

    /**
     * Com msg.
     *
     * @param aUserFolder the a user folder
     * @return the string
     */
    public String comMsg(final UserFolder aUserFolder) {
        final String empId = aUserFolder.getInfo().getEmpdata().getEmpId();
        final String name = aUserFolder.getInfo().getEmpdata().getChtName();
        final String version = aUserFolder.getProjectVersion();
        return String.format(uploaderAlreadyDone, empId, name, version);
    }

    /**
     * 處理新上傳source code資料夾的資料
     * *.
     *
     * @param folder the folder
     */
    public void processUserFolders(final List<UserFolder> folder) {
        final String nowDate = DateUtils.getNowDate();
        
        //取得員工編號與員工資料對應Map
        final Map<String, EmpDto> index = getEmailIndex();
        
        //取得
        final Map<String, List<String>> map = getWatcherList();
        final Set<Entry<String, List<String>>> entrySet = map.entrySet(); 
        for (Entry<String, List<String>> entryUnit : entrySet) {
            final StringBuilder sbf = new StringBuilder();
            final String watcherEmpId = entryUnit.getKey();
            final List<String> watchedEmpIdList = entryUnit.getValue();
            boolean needToNotify =false;
            final List<String> tokenList = new ArrayList<String>();
            for (UserFolder unit : folder) {
                if (watchedEmpIdList.contains(unit.getInfo().getEmpdata()
                        .getEmpId())) {
                    sbf.append(comMsg(unit)).append("\n");
                    needToNotify = true;
                    String tokenName = String.format("%s-%s", nowDate,
                            unit.getInfo().getEmpdata().getEmpId());
                    tokenList.add(tokenName);
                }
            }
            if(!needToNotify){
                continue;
            }
            final String msg = sbf.toString();
            final EmpDto watcher = index.get(watcherEmpId);
            if(watcher == null ){
                LOGGER.info("watcher:{} 找不到" ,watcherEmpId);
                continue ;
            }
            final String watchersEmail =  watcher.getEmail();
            final Session session = GmailUtils.createSession();
            final String tilte = String.format("%s-%s", nowDate,
                    generalTitle);
            String emailContent = String.format("哈囉 %s !! ,\n\n%s\n\n%s ",
                    watcher.getChtName(), msg, sumEnd);
           
             
            ConfigInfo config = new ConfigInfo();
            final  File tokenFolder = config.getTokenFolder();
            if(tokenFolder !=null && !tokenFolder.exists()){
                try {
                    FileUtils.forceMkdir(tokenFolder);
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
            final String[] listFileName = tokenFolder.list();
            
            final  Predicate<? super String> predicate = new Predicate<String>() {
                public boolean evaluate(String str) {                   
                    return StringUtils.contains(str, nowDate);
                }
                
            };
           
            try {
                final String existFileName   = CollectionUtils.find(Arrays.asList(listFileName), predicate);
                if(existFileName== null ){                
                    final Message message = GmailUtils.createMessage(watchersEmail,
                            tilte, emailContent, session);
                    Transport.send(message);
                    for(String token : tokenList){
                        FileUtils.write(new File(tokenFolder ,token ), new Date().toString());  
                    }
                   
                }                
            } catch (AddressException e) {
                LOGGER.error(e.getMessage(), e);
            } catch (MessagingException e) {
                LOGGER.error(e.getMessage(), e);
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
            LOGGER.info("Done");
        }
    }

    /**
     * Gets the all emp info.
     *
     * @return the all emp info
     */
    public Map<String, EmpDto> getEmailIndex() {
        final Map<String, EmpDto> result = new HashMap<String, EmpDto>();
        final EmpFactory factory = EmpFactory.getEmpFactory();
        final List<EmpDto> empList = factory.getEmpDtoByLdap();
         
        for(EmpDto emp  : empList){ 
            result.put(emp.getEmpId(), emp);
        }
        return result;
    }
    
    
    
    /**
     * Modify watchers.
     *
     * @param emp the emp
     * @param addOrDel the add or del
     * @param watcherId the watcher id
     * @return true, if successful
     */
    public boolean modifyWatchers(final String empId , final boolean addOrDel ,final String watcherId){
    	boolean result = false ;
    	if(StringUtils.isBlank(empId) || StringUtils.isBlank(watcherId) ){
    		return result;
    	}
		final ConfigInfo configs = new ConfigInfo();

		final File dir = configs.getNotifyConfigFolder();
		if (!dir.exists()) {
			try {
				FileUtils.forceMkdir(dir);
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}

		File config = new File(dir, empId);
		try {
			if (config.exists()) {

				String content = FileUtils.readFileToString(config,
						Charsets.UTF_8);
				String[] old = StringUtils.splitPreserveAllTokens(content, ",");
				final Set<String> idSet =new HashSet<String>();
				for(String line :old ){
					idSet.add(StringUtils.trim(line));
				}
				final EmpFactory empFactory = EmpFactory.getEmpFactory();
				final UserProjProfile watcherData = empFactory
						.getUserProjForCR(watcherId);
				EmpDto empData = null;

				if (watcherData != null) {
					empData = watcherData.getEmpdata();
				}
				
				if (empData != null) {
					String[] newData = null;
					if (addOrDel) {
						idSet.add(StringUtils.trim(watcherId));
						newData = idSet.toArray(new String[]{});
						
					} else {
						idSet.remove(StringUtils.trim(watcherId));
						newData = idSet.toArray(new String[]{});
					}

					FileUtils.write(config, StringUtils.join(newData, ","),
							Charsets.UTF_8);
					result = true;
				}

			} else {
				final EmpFactory empFactory = EmpFactory.getEmpFactory();
				final UserProjProfile watcherData = empFactory
						.getUserProjForCR(watcherId);
				EmpDto empData = null;

				if (watcherData != null) {
					empData = watcherData.getEmpdata();
				}
				if (empData != null && addOrDel) {

					FileUtils.write(config, watcherId, Charsets.UTF_8);
					result = true;

				}
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return result;
	}
    
    
    /**
     * Gets the notify config.
     * 根據員工資訊找尋該員工的通知設定...根據設定組裝內容
     * @param emp the emp
     * @return the notify config
     */
    public CodeReviwEvent getNotifyConfig(EmpDto emp){
    	final ConfigInfo configs = new ConfigInfo();
    
    	 final File dir = configs.getNotifyConfigFolder();
    	 if(!dir.exists()){
    		 try {
				FileUtils.forceMkdir(dir);
			} catch (IOException e) {
				   LOGGER.error(e.getMessage(), e);
			}
    	 }
    	 
    	File donfig =new File(dir , emp.getEmpId());
    	CodeReviwEvent result = new CodeReviwEvent();
    	if(donfig.exists()){
    		 try {
				String content = FileUtils.readFileToString(donfig,Charsets.UTF_8);
				 result.setWatchers(StringUtils.splitPreserveAllTokens(content ,","));
			} catch (IOException e) {
				   LOGGER.error(e.getMessage(), e);
			}    		 
    	}
    	return result ;
    }
    /**
     * key是watcher的empUid value是監看範圍的empId清單
     * 
     * **.
     *
     * @return the watcher list
     */
    public Map<String, List<String>> getWatcherList() {
        final Map<String, List<String>> result = new HashMap<String, List<String>>();
        final EmpFactory factory = EmpFactory.getEmpFactory();
        List<EmpDto> empList = factory.getEmpDtoByLdap();
        for(EmpDto emp : empList){
        	final String empId = emp.getEmpId();
        	String[] watchers = emp.getCodereview().getWatchers();
			if (ArrayUtils.isNotEmpty(watchers)) {
				for(String watcher : watchers){
					List<String> list = result.get(watcher);
					if(list == null ){
						list = new ArrayList<String>();
					}
					list.add(empId);
					result.put(watcher, list);
				}
			}
        }
        return result;
    }
}

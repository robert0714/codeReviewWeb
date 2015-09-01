package com.iisigroup.scan.folder;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;  
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iisigroup.java.tech.sonar.business.OSvalidator;
   
/**
 * The Class ConfigInfo.
 */
public class ConfigInfo {
    /** The Constant logger. */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ConfigInfo.class);
    
    /** The Constant CONF. */
    private static final String CONF = "/config.properties";
    
    /** The Constant SCAN_FOLDER. */
    private static final String SCAN_FOLDER = OSvalidator.isWindows()? "win_scanFolder": "scanFolder";
    
    /** The Constant SONAR_PROJECT_FOLDER. */
    private static final String SONAR_PROJECT_FOLDER = OSvalidator.isWindows()? "win_sonarProjectFolder": "sonarProjectFolder";
    

    /** The Constant SONAR_PROJECT_FOLDER. */
    private static final String SONAR_RUNNER_HOME = OSvalidator.isWindows()? "win_sonarRunnerHome" : "sonarRunnerHome";
    
    private static final String FILE_QUEUE_HOME = OSvalidator.isWindows()? "win_fileQueueHome" : "fileQueueHome";
    
    private static final String FILE_TOKEN_HOME = OSvalidator.isWindows()? "win_fileTokenHome" : "fileTokenHome";
    
    private static final String NOTIFY_WATCHERS_CONF_HOME = OSvalidator.isWindows()? "win_notifyWatchersConfHome" : "notifyWatchersConfHome";
    
    private static final String LDAP_CACHE_HOME = OSvalidator.isWindows()? "win_ldapCacheHome" : "ldapCacheHome";
    
    private static ThreadLocal<Map<String, String>> COF_MAP = new ThreadLocal<Map<String, String>>(){
        @Override
        protected Map<String, String> initialValue() {
        	final Properties properties = new Properties();
            InputStream inStream = null;
            final Map<String, String> result = new HashMap<String, String>();
            try {
                inStream = getClass().getResourceAsStream(CONF);
                properties.load(inStream);
                final Set<Entry<Object, Object>> entrySet = properties.entrySet();
                for ( Entry<Object, Object> unit : entrySet ){
                    if(unit.getKey()!=null && unit.getValue() !=null ){
                        result.put(unit.getKey().toString(),unit.getValue().toString());
                    }
                   
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }finally{
                if(inStream != null ){
                    try {
                        inStream.close();
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                }
            }
            return result ;
        }        
    };
    /**
     * Gets the conf map.
     *
     * @return the conf map
     */
    protected Map<String, String> getConfMap() {
        
        return COF_MAP.get() ;
    }

    /**
     * Gets the sonar runner home.
     *
     * @return the sonar runner home
     */
    public File getSonarRunnerHome(){
        final String path = getConfMap().get(SONAR_RUNNER_HOME);
        final File homedirectory = new File(path);
        return homedirectory;
    }
    /**
     * Gets the sonar runner script.
     *
     * @return the sonar runner script
     */
    public String getSonarRunnerScript(){
        final String path = getConfMap().get(SONAR_RUNNER_HOME);
        File homedirectory = new File(path);
        if(homedirectory.exists()){
            try {
                final String result = homedirectory.getCanonicalPath()+ File.separator + "bin" + File.separator +"sonar-runner" ;
                return result ;
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        return null;
    }
   
    /**
     * Gets the scan folder.
     *
     * @return the scan folder
     */
    public File getScanFolder() {
        final String path = getConfMap().get(SCAN_FOLDER);
        return new File(path);
    }

    /**
     * Gets the sonar project folder.
     *
     * @return the sonar project folder
     */
    public File getSonarProjectFolder() {
        final String path = getConfMap().get(SONAR_PROJECT_FOLDER);
        return new File(path);
    }
    /**
     * Gets the sonar project folder.
     *
     * @return the sonar project folder
     */
    public File getFileQueueFolder() {
        final String path = getConfMap().get(FILE_QUEUE_HOME);
        return new File(path);
    }
    /**
     * Gets the sonar project folder.
     *
     * @return the sonar project folder
     */
    public File getTokenFolder() {
        final String path = getConfMap().get(FILE_TOKEN_HOME);
        return new File(path);
    }
    public File getNotifyConfigFolder() {
        final String path = getConfMap().get(NOTIFY_WATCHERS_CONF_HOME);
        return new File(path);
    }
    public File getLdapCacheFolder() {
        final String path = getConfMap().get(LDAP_CACHE_HOME);
        return new File(path);
    }
}

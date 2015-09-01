package com.iisigroup.java.tech.timmer;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iisigroup.scan.folder.DetectModificationInFolder;
 

/**
 * The Class DateTask.
 */
public class DateTask extends TimerTask {
    /** The Constant logger. */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DateTask.class);
    
    /* (non-Javadoc)
     * @see java.util.TimerTask#run()
     */
    @Override
    public void run() {
        try {
            DetectModificationInFolder scan = new DetectModificationInFolder();
            scan.scanFolderPreparation();
        } catch (Exception e) {
            LOGGER.info("DateTask has problem !");
            LOGGER.error(e.getMessage() ,e );
        }
        
    }

}

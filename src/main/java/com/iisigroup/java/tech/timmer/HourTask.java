package com.iisigroup.java.tech.timmer;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iisigroup.java.tech.controller.operation.FileQueueManager;
 

/**
 * The Class DateTask.
 */
public class HourTask extends TimerTask {
    /** The Constant logger. */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(HourTask.class);
    
    /* (non-Javadoc)
     * @see java.util.TimerTask#run()
     */
    @Override
    public void run() {
        try {
            
        	new FileQueueManager().processQueue() ;
        } catch (Exception e) {
            LOGGER.info("HourTask has problem !");
            LOGGER.error(e.getMessage() ,e );
        }
    }

}

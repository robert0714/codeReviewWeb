package com.iisigroup.java.tech.application;

import java.util.Date;
import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iisigroup.java.tech.timmer.DateTask;
import com.iisigroup.java.tech.timmer.HourTask;

/**
 * The listener interface for receiving initialization events.
 * The class that is interested in processing a initialization
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addInitializationListener<code> method. When
 * the initialization event occurs, that object's appropriate
 * method is invoked.
 *
 * @see InitializationEvent
 */
public class InitializationListener 
implements ServletContextListener 
{
    /** The Constant logger. */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(InitializationListener.class);
    
    /** The timer. */
//    final Timer timer = new Timer();
    
    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    public void contextInitialized(final ServletContextEvent sce) {
       
      
        // 設定填入schedule中的 Date firstTime     為現在的15秒後
//        final  Date firstTime = DateUtils.addSeconds(new Date(),  15);
        
        // 設定填入schedule中的 Date firstTime     為現在的30秒後
//        final  Date sencondTime = DateUtils.addSeconds(new Date(),  30);
        
        /***
         * 改成每個小時的原因....是當初設定一天一次....
         * 會因為開發階段而很容易造成晚上一天一次,
         * 使得產生日期資料夾會晚於同仁使用時間,而只能上傳到前一天日期資料夾
         *  
         * *****/
//        timer.schedule(new DateTask(), firstTime,DateUtils.MILLIS_PER_HOUR );
//        
//       
//        
//        timer.schedule(new HourTask(), sencondTime, DateUtils.MILLIS_PER_MINUTE);
//        FtpService.start();
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    public void contextDestroyed(final ServletContextEvent sce) {
    	System.out.println("........................");
        LOGGER.info("cancel timmer start");
        FtpService.stop();
//        timer.cancel();
        LOGGER.info("cancel timmer end");
       
    }

}

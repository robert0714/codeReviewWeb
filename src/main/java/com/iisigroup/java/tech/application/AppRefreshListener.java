/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package com.iisigroup.java.tech.application;

import com.iisigroup.java.tech.timmer.DateTask;
import com.iisigroup.java.tech.timmer.HourTask;

import java.util.Date;
import java.util.Timer;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class AppRefreshListener implements ApplicationListener<ContextRefreshedEvent> {

	
	
	//================================================
	//== [Enumeration types] Block Start
	//== [Enumeration types] Block End 
	//================================================
	//== [static variables] Block Start
    private static final Logger LOGGER = LoggerFactory
            .getLogger(AppRefreshListener.class);
    
    /** The timer. */
//    final static  Timer timer = new Timer();
	//== [static variables] Block Stop 
	//================================================
	//== [instance variables] Block Start
	//== [instance variables] Block Stop 
	//================================================
	//== [static Constructor] Block Start
	//== [static Constructor] Block Stop 
	//================================================
	//== [Constructors] Block Start (含init method)
	//== [Constructors] Block Stop 
	//================================================
	//== [Static Method] Block Start
	//== [Static Method] Block Stop 
	//================================================
	//== [Accessor] Block Start
	//== [Accessor] Block Stop 
	//================================================
	//== [Overrided Method] Block Start (Ex. toString/equals+hashCode)
	 
	public void onApplicationEvent(ContextRefreshedEvent event) {
		LOGGER.info("ContextRefreshedEvent............................................."+event.getSource());
		 Timer timer = new Timer();
	       
	      
        // 設定填入schedule中的 Date firstTime     為現在的15秒後
        final  Date firstTime = DateUtils.addSeconds(new Date(),  15);
        
        // 設定填入schedule中的 Date firstTime     為現在的30秒後
        final  Date sencondTime = DateUtils.addSeconds(new Date(),  30);
        
        /***
         * 改成每個小時的原因....是當初設定一天一次....
         * 會因為開發階段而很容易造成晚上一天一次,
         * 使得產生日期資料夾會晚於同仁使用時間,而只能上傳到前一天日期資料夾
         *  
         * *****/
        timer.schedule(new DateTask(), firstTime,DateUtils.MILLIS_PER_HOUR );
        
       
        
        timer.schedule(new HourTask(), sencondTime, DateUtils.MILLIS_PER_MINUTE);
        FtpService.start();
    
	}
	
	//== [Overrided Method] Block Stop 
	//================================================
	//== [Method] Block Start
	//####################################################################
	//## [Method] sub-block : 
	//####################################################################    
	//== [Method] Block Stop 
	//================================================
	//== [Inner Class] Block Start
	//== [Inner Class] Block Stop 
	//================================================
}

package com.iisigroup.sonar.httpclient.statics.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

public class StringProcess {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String sample ="2014-12-28T23:40:31+0800";
        System.out.println(StringUtils.substring(sample, 0, 19).replace("T", " ") );
        
      

    }

}

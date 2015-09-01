package com.iisigroup.sonar.httpclient.statics;


import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.iisigroup.sonar.httpclient.statics.model.MonthlyProjSum;

public class StaticsMonthlyTest {
    private StaticsMonthly component ;
    
    @Before
    public void setUp() throws Exception {
        component = new StaticsMonthly();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testCalMonthNumByProjectKey() throws Exception {
        final String[] personNames = getPersonName();
        for(String personName : personNames){
            String projectKey = String.format("%s:project", personName);
            Map<String, String> data = component.calMonthNumByProjectKey(projectKey);
        }
        Map<String, String> data = component.calMonthNumByProjectKey("peijun.jian:project");
        System.out.println(ToStringBuilder.reflectionToString(data));
    }
    private static String[] getPersonName(){
        return new String[]{
                "tzufung.lo",
                "chunhsiang.bai",
                "chehsien.lee",
                "jungchang.liu",
                "peijun.jian",
                "yichen.chiang",
                "robert.lee",
                "rain.lee",
                "irena.hsu",
                "yenchieh.lee",
                "chawrry.chen",
                "sophia.chan",
                "lucas.lee" 
                
        }; 
    }
    
    @Test
    public void testCalculateAllProjects() throws Exception {
        List<MonthlyProjSum> data = component.calculateAllProjects();
        System.out.println(data.size());
    }

    @Test
    public void testCal2WeekNumProjectKey() throws Exception {
        Map<String, String> data = component.cal2WeekNumProjectKey("peijun.jian:project");
        System.out.println(data.size());
    }

}

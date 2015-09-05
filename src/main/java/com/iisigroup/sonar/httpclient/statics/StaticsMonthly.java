package com.iisigroup.sonar.httpclient.statics;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iisigroup.java.tech.service.PersonService;
import com.iisigroup.scan.folder.internal.UserFolder;
import com.iisigroup.sonar.httpclient.JsonClient01;
import com.iisigroup.sonar.httpclient.internal.ProjectInfo;
import com.iisigroup.sonar.httpclient.statics.model.MonthlyProjSum;
import com.iisigroup.sonar.httpclient.statics.model.ProjectSum;
import com.iisigroup.sonar.httpclient.statics.model.SumPer2Weeks;

 
/**
 * The Class StaticsMonthly.
 */
public class StaticsMonthly { 
    /** The Constant logger. */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(StaticsMonthly.class);
    
    /** The extract month reg expr. */
    private  final String extractMonthRegExpr ="(\\d{4}-\\d{2})-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\+\\d{4}"; 
   
    /** The extract date reg expr. */
    private  final String extractDateRegExpr ="(\\d{4}-\\d{2}-\\d{2})T\\d{2}:\\d{2}:\\d{2}\\+\\d{4}"; 
    /**
     * Extract month.
     *
     * @param line the line
     * @return the string
     */
    public String extractMonth(String line) {
        final Matcher matcher = Pattern.compile(extractMonthRegExpr).matcher(
                line);
        if (matcher.find()) {
            return matcher.group(matcher.groupCount());
        } else {
            return null;
        }
    }
    
    /**
     * Extract date.
     *
     * @param line the line
     * @return the string
     */
    public String extractDate(String line) {
        final Matcher matcher = Pattern.compile(extractDateRegExpr).matcher(
                line);
        if (matcher.find()) {
            return matcher.group(matcher.groupCount());
        } else {
            return null;
        }
    }
    /**
     * Cal2 week num project key.
     *
     * @param projectKey the project key
     * @return the map
     */
    public Map<String ,String> cal2WeekNumProjectKey(final String projectKey){
        final List<ProjectSum> init = new StaticsPreview().calculateProjectsByProjectKey(projectKey);
        final Map<String ,List<String>> data = new HashMap<String ,List<String>>();
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd"); 
        SimpleDateFormat sdf2 =new SimpleDateFormat("yyyy-MM-");
                //2014-12-17
        for(ProjectSum p : init){
            final String  date = extractDate(p.getDate());
            final String calValue = p.getCalValues();
            LOGGER.info(date);
            LOGGER.info(calValue);
            Date dateData = null ;
            Date standardDateData = null ;
            try {
                dateData = sdf.parse(date);
                final String standard =  sdf2.format(dateData)+"16";
                standardDateData = sdf.parse(standard); ;
                
            } catch (ParseException e) {
                LOGGER.error ( e.getMessage() ,e );                
            }
            
            final String  month = extractMonth(p.getDate());
            String key =null; 
            if(standardDateData.getTime() > dateData.getTime() ){
                //該月上半個月
                key = String.format("%s 上半個月", month);
            }else {
                //該月下半個月 
                key = String.format("%s 下半個月", month);
            }
//            int weekNum =  getWeek(dateData);
            List<String> list = data.get(key);
            if(list == null){
                list = new ArrayList<String>(); 
            }
            list.add(calValue);
            
            data.put(key, list);
        }
        return commonCalculation(data) ;
    }
    /**
     * Calculate all projects.
     * 以兩個禮拜進行計算
     *
     * @return the list
     */
    public List<SumPer2Weeks> cal2WeeksAllProjects() {
        final PersonService psC = new PersonService();
        final List<UserFolder> list = psC.getAllUserForCodeReview();
        final List<SumPer2Weeks> result =new ArrayList<SumPer2Weeks>();
        JsonClient01 client = new JsonClient01();
        final List<ProjectInfo> allProject = client.getAllProjects();
        final Set<String > allProjectSet = new HashSet<String>();
        
        for (ProjectInfo info  : allProject){
            allProjectSet.add(info.getK());
        }
        for(UserFolder uf : list ){
            final String projectKey = uf.getInfo().getProjectKey();
            if(!allProjectSet.contains(projectKey)){
                continue ; 
            }
            final Map<String ,String> unitData =  cal2WeekNumProjectKey(projectKey);
            final SumPer2Weeks unit = new SumPer2Weeks();
            
            unit.setProjectKey(projectKey);
            unit.setEmpData(uf.getInfo().getEmpdata());
            
            if(unitData != null){ 
                unit.setData(unitData);
            }
            boolean needToInclude = false ;
            for(String value: unitData.values()){
                if(StringUtils.isNotBlank(value)){
                    needToInclude = true ; 
                }
            }
            
            if(needToInclude){
                result.add(unit);
            }
        }

        return result;
    }
    
    public  Map<String ,List<SumPer2Weeks>> cal2WeeksAllProjectsGroupByDepart() {    	
    	
        final PersonService psC = new PersonService();
        final List<UserFolder> list = psC.getAllUserForCodeReview();
        
        final Map<String ,List<SumPer2Weeks> > result =new  HashMap<String, List<SumPer2Weeks>>();
        JsonClient01 client = new JsonClient01();
        final List<ProjectInfo> allProject = client.getAllProjects();
        final Set<String > allProjectSet = new HashSet<String>();
        
        for (ProjectInfo info  : allProject){
            allProjectSet.add(info.getK());
        }
        for(UserFolder uf : list ){
            final String projectKey = uf.getInfo().getProjectKey();
            if(!allProjectSet.contains(projectKey)){
                continue ; 
            }
            final Map<String ,String> unitData =  cal2WeekNumProjectKey(projectKey);
            final SumPer2Weeks unit = new SumPer2Weeks();
            
            unit.setProjectKey(projectKey);
            unit.setEmpData(uf.getInfo().getEmpdata());
            
            if(unitData != null){ 
                unit.setData(unitData);
            }
            boolean needToInclude = false ;
            for(String value: unitData.values()){
                if(StringUtils.isNotBlank(value)){
                    needToInclude = true ; 
                }
            }
            
            if(needToInclude){
            	final String depart = unit.getEmpData().getDepart();
            	List<SumPer2Weeks> unitlist = result.get(depart);
            	if(unitlist == null){
            		unitlist = new ArrayList<SumPer2Weeks>();
            	}
            	unitlist.add(unit);
                result.put(depart, unitlist);
                
            }
        }

        return result;
    }
    /**
     * Common calculation.
     *
     * @param data the data
     * @return the map
     */
    public Map<String ,String> commonCalculation(final Map<String ,List<String>> data){
        final Set<Entry<String, List<String>>> entrySet = data.entrySet();
        final Map<String ,String> result = new HashMap<String, String>(); 
        for (Entry<String, List<String>> entryUnit : entrySet){
            
                String key = entryUnit.getKey();
                List<String> value = entryUnit.getValue();
                BigDecimal total = new BigDecimal(0) ;
                for(String line : value){
                    if(NumberUtils.isNumber(line)){
                        total =total.add(new BigDecimal(line)); 
                    }            
                }
//                BigDecimal answer =  total.divide(new BigDecimal(value.size() ),10,RoundingMode.HALF_EVEN);
                BigDecimal answer =  total.multiply(new BigDecimal(100 )).divide(new BigDecimal(value.size() ),2,RoundingMode.HALF_EVEN);
                
              
                result.put(key, answer.toString()+"%");
           
        }
        return result ;
    }
    /**
     * Gets the week.
     * 得到是第幾個禮拜?
     * @param date the date
     * @return the week
     */
    public int getWeek(Date date){
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        final int week = cal.get(Calendar.WEEK_OF_MONTH); 
        return week;
    }
    /**
     * Cal month num by project key.
     * 根據projectkey計算每個月的缺失密度
     * @param projectKey the project key
     * @return the map
     */
    public  Map<String ,String>   calMonthNumByProjectKey(final String projectKey){
        
        final List<ProjectSum> init = new StaticsPreview().calculateProjectsByProjectKey(projectKey);
        final Map<String ,List<String>> data = new HashMap<String ,List<String>>();
        for(ProjectSum p : init){
          final String  month = extractMonth(p.getDate());
          final String calValue = p.getCalValues();
          List<String> list = data.get(month);
          if(list == null){
              list = new ArrayList<String>(); 
          }
          list.add(calValue);
          
          data.put(month, list);
          
        }
        return commonCalculation(data) ;
    }
    
    /**
     * Calculate all projects.
     *
     * @return the list
     */
    public List<MonthlyProjSum> calculateAllProjects() {
        final PersonService psC = new PersonService();
        final List<UserFolder> list = psC.getAllUserForCodeReview();
        final List<MonthlyProjSum> result =new ArrayList<MonthlyProjSum>();
        for(UserFolder uf : list ){
            final String projectKey = uf.getInfo().getProjectKey();
            final Map<String ,String> unitData =  calMonthNumByProjectKey(projectKey);
            final MonthlyProjSum unit = new MonthlyProjSum();
            
            unit.setProjectKey(projectKey);
            unit.setEmpData(uf.getInfo().getEmpdata());
            
            if(unitData != null){ 
                unit.setData(unitData);
            }
            boolean needToInclude = false ;
            for(String value: unitData.values()){
                if(StringUtils.isNotBlank(value)){
                    needToInclude = true ; 
                }
            }
            
            if(needToInclude){
                result.add(unit);
            }
        }

        return result;
    }
     
}

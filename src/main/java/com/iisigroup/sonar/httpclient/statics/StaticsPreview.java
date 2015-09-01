package com.iisigroup.sonar.httpclient.statics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iisigroup.sonar.httpclient.JsonClient01;
import com.iisigroup.sonar.httpclient.internal.Cell;
import com.iisigroup.sonar.httpclient.internal.Column;
import com.iisigroup.sonar.httpclient.internal.ProjectInfo;
import com.iisigroup.sonar.httpclient.internal.TimeMachineData;
import com.iisigroup.sonar.httpclient.statics.model.ProjectSum;

public class StaticsPreview { 
    /** The logger. */
    private static Logger LOGGER = LoggerFactory
            .getLogger(StaticsPreview.class);

    protected ProjectSum convert(final Map<String, String> mapdata) {
        final ProjectSum result = new ProjectSum();
        final Set<Entry<String, String>> entrySet = mapdata.entrySet();

        for (Entry<String, String> entryUnit : entrySet) {
            final String key = entryUnit.getKey();
            final String value = entryUnit.getValue();
            final     int data = NumberUtils.toInt(value);
            switch (StringUtils.trim(key)) {
            case "ncloc":
                result.setLinesOfCode(data);                
                break;
            case "blocker_violations":
                result.setNumOfBlocker(data);
                break;
            case "critical_violations":
                result.setNumOfCritical(data);
                break;
            case "major_violations":                
                result.setNumOfMajor(data);                
                break;
            case "minor_violations":
                result.setNumOfMinor(data);
                break;
            case "info_violations":
                result.setNumOfInfo(data);
                break;
            default: 
                break;

            }
        }
        return result;
    }

    public List<ProjectSum>  calculateProjectsByProjectKey(final String projectKey){
        final List<ProjectSum> result = new ArrayList<ProjectSum>();
        JsonClient01 client = new JsonClient01();
        TimeMachineData tm = client.countIssueByTimemachineByProjectKey(projectKey);
        final List<Column> columnList = tm.getColumnList();

        final List<Cell> cellList = tm.getCellList();
 
        if (CollectionUtils.isNotEmpty(cellList)) {
            /**
             * 由於cellList當中會有無效資料也就是size大小不合...或是0的情形
             * ***/
            final  Iterator<Cell> iterator = cellList.iterator();
            while(iterator.hasNext()){
                final   Cell cellData = iterator.next() ; 
                if (cellData.getValues().length != columnList.size()) {
                    iterator.remove();
                }else{
                    final Map<String, String> mapdata = new HashMap<String, String>();
                    String date = cellData.getDate();
                    for (int i = 0; i < columnList.size(); ++i) {
                        Column data = columnList.get(i);
                        final String metricName = data.getMetric();

                        mapdata.put(metricName, cellData.getValues()[i]);
                        
                    }
                    final ProjectSum unitData = convert(mapdata);
                    unitData.setKey(projectKey);
                    unitData.setDate(date);
                    result.add(unitData);
                }
            }  
             
        }
        return result ;
    }
    
    public List<ProjectSum> calculateAllProjects() {
        List<ProjectSum> result = new ArrayList<ProjectSum>();
        JsonClient01 client = new JsonClient01();
        final List<ProjectInfo> allProjects = client.getAllProjects();
        if (CollectionUtils.isNotEmpty(allProjects)) {
            for (ProjectInfo unit : allProjects) {
                final Map<String, String> mapdata = new HashMap<String, String>();
                String projectkey = unit.getK();
                final TimeMachineData tm = client
                        .countIssueByTimemachineByProjectKey(projectkey);

                final List<Column> columnList = tm.getColumnList();

                final List<Cell> cellList = tm.getCellList();

                String date = null; 
                if (CollectionUtils.isNotEmpty(cellList)) {
                    /**
                     * 由於cellList當中會有無效資料也就是size大小不合...或是0的情形
                     * ***/
                    final  Iterator<Cell> iterator = cellList.iterator();
                    while(iterator.hasNext()){
                        final   Cell data = iterator.next() ; 
                        if (data.getValues().length != columnList.size()) {
                            iterator.remove();
                        }
                    }
                    if (CollectionUtils.isNotEmpty(cellList)) {

                        Collections.reverse(cellList);
                        final Cell tmp = cellList.get(0);
                        date = tmp.getDate();

                        for (int i = 0; i < columnList.size(); ++i) {
                            Column data = columnList.get(i);
                            final String metricName = data.getMetric();
                             
                            mapdata.put(metricName, tmp.getValues()[i]);
                        }

                    }
                }
                final ProjectSum unitData = convert(mapdata);
                unitData.setKey(projectkey);
                unitData.setDate(date);
                result.add(unitData) ; 
            }
        }

        return result;
    }

}

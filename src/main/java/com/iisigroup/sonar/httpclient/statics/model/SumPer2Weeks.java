package com.iisigroup.sonar.httpclient.statics.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.iisigroup.scan.folder.internal.EmpDto;

public class SumPer2Weeks implements Serializable {


    /**
     * 
     */
    private static final long serialVersionUID = 4843677301185984272L;

    private EmpDto empData ; 
    
    private String projectKey ; 
    
    private Map<String,String> data ;

    public EmpDto getEmpData() {
        return empData;
    }

    public void setEmpData(EmpDto empData) {
        this.empData = empData;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public Map<String, String> getData() {
        if(data == null ){
            data = new HashMap<String, String>();
        }
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
    

}

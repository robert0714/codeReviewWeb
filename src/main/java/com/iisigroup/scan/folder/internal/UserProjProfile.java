package com.iisigroup.scan.folder.internal;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
 
/**
 * The Class UserProjProfile.
 */
public class UserProjProfile    implements Serializable {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 3833174345516675965L;
    
    /** The empdata. */
    private EmpDto empdata;    

    /** The project key. */
    private String projectKey;
    
    /** The project name. */
    private String projectName;
    
    /** The folder name. */
    private String folderName;
    
    /** The template. */
    private static final  String template = "%s:project"; 
    
    /** The sonar url template. */
    private static  final String sonarUrlTemplate = "%s/dashboard/index/%s";
    
    //sonar link可以透過projectKey組成
    //http://localhost:9000/dashboard/index/jackeyWu:project
    
    /**
     * Gets the sonar url.
     *
     * @param sonarUrl the sonar url
     * @return the sonar url
     */
    public String getSonarURL(final String sonarUrl){
       
        return String.format(sonarUrlTemplate, sonarUrl , getProjectKey() );
    }
    /**
     * Gets the project key.
     *
     * @return the project key
     */
    public String getProjectKey() {
        if(this.empdata!=null ){
            final String engName = getProjectName();        
            this.projectKey = String.format(this.template, engName );
        }
        return this.projectKey;
    }
    
    /**
     * Sets the project key.
     *
     * @param projectKey the new project key
     */
    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }
    
    /**
     * Gets the project name.
     *
     * @return the project name
     */
    public String getProjectName() {
        if(this.empdata!=null ){
            this.projectName = StringUtils.substring(this.empdata.getEmail(), 0, this.empdata.getEmail().indexOf("@")); 
        }
        return this.projectName;
    }
    
    /**
     * Sets the project name.
     *
     * @param projectName the new project name
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    
    /**
     * Gets the folder name.
     *
     * @return the folder name
     */
    public String getFolderName() {
        if(this.empdata!=null ){
            this.folderName =  this.empdata.getEmpId()+this.empdata.getChtName(); 
        }
        return folderName;
    }
    
    /**
     * Sets the folder name.
     *
     * @param folderName the new folder name
     */
    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }  
    
    /**
     * Gets the empdata.
     *
     * @return the empdata
     */
    public EmpDto getEmpdata() {
        return empdata;
    }

    /**
     * Sets the empdata.
     *
     * @param empdata the new empdata
     */
    public void setEmpdata(EmpDto empdata) {
        this.empdata = empdata;
    }

}

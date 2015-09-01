package com.iisigroup.scan.folder.internal;

import java.io.Serializable;
 
/**
 * The Class UserFolder.
 */
public class UserFolder implements Serializable {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 3833174345517675965L;
    

    /** The info. */
    private UserProjProfile info ; 
    
    /** The source encoding. */
    private String sourceEncoding;
    
    /** The project version. */
    private String projectVersion;

    

    /**
     * Gets the info.
     *
     * @return the info
     */
    public UserProjProfile getInfo() {
        return info;
    }

    /**
     * Sets the info.
     *
     * @param info the new info
     */
    public void setInfo(UserProjProfile info) {
        this.info = info;
    }

    /**
     * Gets the project version.
     *
     * @return the project version
     */
    public String getProjectVersion() {
        return projectVersion;
    }

    /**
     * Sets the project version.
     *
     * @param projectVersion the new project version
     */
    public void setProjectVersion(String projectVersion) {
        this.projectVersion = projectVersion;
    }

    /**
     * Gets the source encoding.
     *
     * @return the source encoding
     */
    public String getSourceEncoding() {
        return sourceEncoding;
    }

    /**
     * Sets the source encoding.
     *
     * @param sourceEncoding the new source encoding
     */
    public void setSourceEncoding(String sourceEncoding) {
        this.sourceEncoding = sourceEncoding;
    }

}

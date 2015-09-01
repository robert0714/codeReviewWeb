package com.iisigroup.sonar.httpclient.internal;

import java.io.Serializable;

/**
 * The Class Component.
 */
public class Component implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 8271840621743816151L;
    
    /** The key. */
    private String key;

    /** The id. */
    private String id;
    
    /** The qualifier. */
    private String qualifier;
    
    /** The name. */
    private String name;
    
    /** The long name. */
    private String longName;
    
    /** The path. */
    private String path;
    
    
    private String enabled;  
    
    
    public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	/** The project id. */
    private String projectId;
    
    /** The sub project id. */
    private String subProjectId;

    /**
     * Gets the key.
     *
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the key.
     *
     * @param key the new key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the qualifier.
     *
     * @return the qualifier
     */
    public String getQualifier() {
        return qualifier;
    }

    /**
     * Sets the qualifier.
     *
     * @param qualifier the new qualifier
     */
    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the long name.
     *
     * @return the long name
     */
    public String getLongName() {
        return longName;
    }

    /**
     * Sets the long name.
     *
     * @param longName the new long name
     */
    public void setLongName(String longName) {
        this.longName = longName;
    }

    /**
     * Gets the path.
     *
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the path.
     *
     * @param path the new path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Gets the project id.
     *
     * @return the project id
     */
    public String getProjectId() {
        return projectId;
    }

    /**
     * Sets the project id.
     *
     * @param projectId the new project id
     */
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    /**
     * Gets the sub project id.
     *
     * @return the sub project id
     */
    public String getSubProjectId() {
        return subProjectId;
    }

    /**
     * Sets the sub project id.
     *
     * @param subProjectId the new sub project id
     */
    public void setSubProjectId(String subProjectId) {
        this.subProjectId = subProjectId;
    }

}

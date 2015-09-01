package com.iisigroup.sonar.httpclient.internal;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class Rule.
 */
public class Rule implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4490466241262009678L;
    
    /** The key. */
    private String key;
    
    /** The name. */
    private String name;
    
    /** The desc. */
    private String desc;
    
    /** The status. */
    private String status;
    
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
     * Gets the desc.
     *
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }
    
    /**
     * Sets the desc.
     *
     * @param desc the new desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    /**
     * Gets the status.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * Sets the status.
     *
     * @param status the new status
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    
}

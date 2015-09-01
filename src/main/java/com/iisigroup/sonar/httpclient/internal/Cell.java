package com.iisigroup.sonar.httpclient.internal;

import java.io.Serializable;

import com.iisigroup.sonar.httpclient.internal.annotations.JsonMappingName;
  
/**
 * The Class Cell.
 */
public class Cell implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 6602400292291574193L;

    /** The date. */
    @JsonMappingName(jsonName="d")
    private String date;
    
    /** The values. */
    @JsonMappingName(jsonName="v")
    private String[] values;

    /**
     * Gets the date.
     *
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date.
     *
     * @param date the new date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gets the values.
     *
     * @return the values
     */
    public String[] getValues() {
        return values;
    }

    /**
     * Sets the values.
     *
     * @param values the new values
     */
    public void setValues(String[] values) {
        this.values = values;
    }

    
    
    
}

package com.iisigroup.sonar.httpclient.statics.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.lang3.StringUtils;

/**
 * The Class ProjectSum.
 */
public class ProjectSum implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -5517747087680290761L;

    /** The key. */
    private String key;

    /** The id. */
    private String id;

    /** The name. */
    private String name;

    /** The date. */
    private String date;

    /** The lines of code. */
    private int linesOfCode;

    /** The num of blocker. */
    private int numOfBlocker;

    /** The num of critical. */
    private int numOfCritical;

    /** The num of major. */
    private int numOfMajor;

    /** The num of minor. */
    private int numOfMinor;

    /** The num of info. */
    private int numOfInfo;

    private String calValues ;
    
    public String getDisplayDate(){
        return StringUtils.substring(this.date, 0, 19).replace("T", " ") ;
    }
    public String getDisplayValues() {
        int total =  numOfBlocker + numOfCritical + numOfMajor ;
        if(linesOfCode != 0 ){            
            BigDecimal answer =  new BigDecimal(total).multiply(new BigDecimal(100 )).divide(new BigDecimal(linesOfCode ),2,RoundingMode.HALF_EVEN);
            
            calValues = String.valueOf(answer.floatValue() )+"%";
        }else{
            calValues ="";
        }
        return calValues;
    }
    
    public String getCalValues() {
        int total =  numOfBlocker + numOfCritical + numOfMajor ;
        if(linesOfCode != 0 ){

            BigDecimal answer =  new BigDecimal(total).divide(new BigDecimal(linesOfCode ),10,RoundingMode.HALF_EVEN);
             
            calValues = String.valueOf(answer.floatValue() );
            
//            BigDecimal answer =  new BigDecimal(total).multiply(new BigDecimal(100 )).divide(new BigDecimal(linesOfCode ),2,RoundingMode.HALF_EVEN);
//            
//            calValues = String.valueOf(answer.floatValue() )+"%";
        }else{
            calValues ="";
        }
        return calValues;
    }

    public void setCalValues(String calValues) {
        this.calValues = calValues;
    }

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
     * Gets the lines of code.
     *
     * @return the lines of code
     */
    public int getLinesOfCode() {
        return linesOfCode;
    }

    /**
     * Sets the lines of code.
     *
     * @param lineOfCodes the new lines of code
     */
    public void setLinesOfCode(int lineOfCodes) {
        this.linesOfCode = lineOfCodes;
    }

    /**
     * Gets the num of blocker.
     *
     * @return the num of blocker
     */
    public int getNumOfBlocker() {
        return numOfBlocker;
    }

    /**
     * Sets the num of blocker.
     *
     * @param numOfBlocker the new num of blocker
     */
    public void setNumOfBlocker(int numOfBlocker) {
        this.numOfBlocker = numOfBlocker;
    }

    /**
     * Gets the num of critical.
     *
     * @return the num of critical
     */
    public int getNumOfCritical() {
        return numOfCritical;
    }

    /**
     * Sets the num of critical.
     *
     * @param numOfCritical the new num of critical
     */
    public void setNumOfCritical(int numOfCritical) {
        this.numOfCritical = numOfCritical;
    }

    /**
     * Gets the num of major.
     *
     * @return the num of major
     */
    public int getNumOfMajor() {
        return numOfMajor;
    }

    /**
     * Sets the num of major.
     *
     * @param numOfMajor the new num of major
     */
    public void setNumOfMajor(int numOfMajor) {
        this.numOfMajor = numOfMajor;
    }

    /**
     * Gets the num of minor.
     *
     * @return the num of minor
     */
    public int getNumOfMinor() {
        return numOfMinor;
    }

    /**
     * Sets the num of minor.
     *
     * @param numOfMinor the new num of minor
     */
    public void setNumOfMinor(int numOfMinor) {
        this.numOfMinor = numOfMinor;
    }

    /**
     * Gets the num of info.
     *
     * @return the num of info
     */
    public int getNumOfInfo() {
        return numOfInfo;
    }

    /**
     * Sets the num of info.
     *
     * @param numOfInfo the new num of info
     */
    public void setNumOfInfo(int numOfInfo) {
        this.numOfInfo = numOfInfo;
    }

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

}

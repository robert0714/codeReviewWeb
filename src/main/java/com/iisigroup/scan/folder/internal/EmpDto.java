package com.iisigroup.scan.folder.internal;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
  
/**
 * 員工基本資訊
 * The Class EmpDto.
 */
public class EmpDto implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -6938570176735915226L;

    /** The emp id. 員工編號 */
    private String empId;
    
    /** The cht name. 中文姓名*/
    private String chtName;
    
    /** The email. 電子信箱 */
    private String email;
    
    /** The depart. 所屬單位 */
    private String depart;
    
    
    public String getEngName(){
       return  StringUtils.substring(getEmail(), 0, getEmail().indexOf("@"));
    }
    /**
     * Gets the codereview.
     *
     * @return the codereview
     */
    public CodeReviwEvent getCodereview() {
        if(this.codereview == null ){
            this. codereview = new CodeReviwEvent();
        }
        return this. codereview;
    }

    /**
     * Sets the codereview.
     *
     * @param codereview the new codereview
     */
    public void setCodereview(CodeReviwEvent codereview) {
        this.codereview = codereview;
    }

    /** The codereview. */
    private CodeReviwEvent codereview;
    /**
     * Gets the emp id.
     *
     * @return the emp id
     */
    public String getEmpId() {
        return this.empId;
    }
    
    /**
     * Sets the emp id.
     *
     * @param empId the new emp id
     */
    public void setEmpId(final String empId) {
        this.empId = empId;
    }
    
    /**
     * Gets the cht name.
     *
     * @return the cht name
     */
    public String getChtName() {
        return this.chtName;
    }
    
    /**
     * Sets the cht name.
     *
     * @param chtName the new cht name
     */
    public void setChtName(final String chtName) {
        this.chtName = chtName;
    }
    
    /**
     * Gets the email.
     *
     * @return the email
     */
    public String getEmail() {
        return this.email;
    }
    
    /**
     * Sets the email.
     *
     * @param email the new email
     */
    public void setEmail(final String email) {
        this.email = email;
    }
	public String getDepart() {
		return depart;
	}
	public void setDepart(String depart) {
		this.depart = depart;
	}

    
}

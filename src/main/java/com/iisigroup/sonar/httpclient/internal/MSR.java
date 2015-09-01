package com.iisigroup.sonar.httpclient.internal;

import java.io.Serializable;

public class MSR implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5649022993173880760L;
	
	private String key ;
	
	private String val;
	
	private String frmt_val;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public String getFrmt_val() {
		return frmt_val;
	}

	public void setFrmt_val(String frmt_val) {
		this.frmt_val = frmt_val;
	}
	
	
}

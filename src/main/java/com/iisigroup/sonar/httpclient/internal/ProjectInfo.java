package com.iisigroup.sonar.httpclient.internal;

import java.io.Serializable;

public class ProjectInfo implements Serializable {

	 
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5756222183327329714L;

	private String id ;
	
	private String k ;
	
	private String nm ;
	
	private String sc ;
	
	private String qu ;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getK() {
		return k;
	}

	public void setK(String k) {
		this.k = k;
	}

	public String getNm() {
		return nm;
	}

	public void setNm(String nm) {
		this.nm = nm;
	}

	public String getSc() {
		return sc;
	}

	public void setSc(String sc) {
		this.sc = sc;
	}

	public String getQu() {
		return qu;
	}

	public void setQu(String qu) {
		this.qu = qu;
	}

}

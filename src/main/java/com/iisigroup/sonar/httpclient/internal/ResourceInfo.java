package com.iisigroup.sonar.httpclient.internal;

import java.io.Serializable;

public class ResourceInfo implements Serializable {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2222895859539497775L;

	private String id ;
	
	
	private String key ;
	
	private String name ;
	
	private String scope;
	
	private String qualifier ;
	
	private String date;
	
	private String creationDate ;
	
	private String lname ;
	
	private String version ;
	
	private String description ;
	
	private MSR[] msr; 


	public String getKey() {
		return key;
	}


	public void setKey(String key) {
		this.key = key;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getQualifier() {
		return qualifier;
	}


	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getScope() {
		return scope;
	}


	public void setScope(String scope) {
		this.scope = scope;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getCreationDate() {
		return creationDate;
	}


	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}


	public String getLname() {
		return lname;
	}


	public void setLname(String lname) {
		this.lname = lname;
	}


	public String getVersion() {
		return version;
	}


	public void setVersion(String version) {
		this.version = version;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public MSR[] getMsr() {
		return msr;
	}


	public void setMsr(MSR[] msr) {
		this.msr = msr;
	}

}

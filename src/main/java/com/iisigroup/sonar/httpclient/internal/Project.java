package com.iisigroup.sonar.httpclient.internal;

import java.io.Serializable;

public class Project implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8516091030808314006L;
	
	private String key ;
	
	private String id ;
	
	private String qualifier ;
	
	private String name ;
	
	
	private String longName ;


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


	public String getLongName() {
		return longName;
	}


	public void setLongName(String longName) {
		this.longName = longName;
	} 
	
	

}

package com.iisigroup.java.tech.ldap.internal;

import java.io.Serializable;

public class JsonNode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5276845125392002016L;
	private String id;
	private String pId;
	private String name;
	private boolean open;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
	
}

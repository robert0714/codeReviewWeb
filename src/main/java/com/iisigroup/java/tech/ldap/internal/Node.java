package com.iisigroup.java.tech.ldap.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Node implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3144992643172586014L;
	private Map<String,String[]> data ;
	private Node parent;
	private List<Node> children ;
	private String distinguishedName ; 
	public Map<String, String[]> getData() {
		if(data == null ){
			data = new HashMap<String, String[]>(); 
		}
		return data;
	}
	public void setData(Map<String, String[]> data) {
		this.data = data;
	}
	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
	public List<Node> getChildren() {
		if(children == null ){
			children = new ArrayList<Node>();
		}
		return children;
	}
	public void setChildren(List<Node> children) {
		this.children = children;
	}
	public String getDistinguishedName() {
		return distinguishedName;
	}
	public void setDistinguishedName(String distinguishedName) {
		this.distinguishedName = distinguishedName;
	}
	
}

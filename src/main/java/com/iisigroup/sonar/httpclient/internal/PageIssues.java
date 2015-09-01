package com.iisigroup.sonar.httpclient.internal;

import java.io.Serializable;

public class PageIssues implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6061553425242069702L;
    
    private boolean maxResultsReached;
    
    private  PageInfo paging;
    
    private Issue[] issues; 
    
    private Component[] components; 
    
    private Rule[] rules ;
    
    private User[] users;
    
    private Project[]  projects ;

	public boolean isMaxResultsReached() {
		return maxResultsReached;
	}

	public void setMaxResultsReached(boolean maxResultsReached) {
		this.maxResultsReached = maxResultsReached;
	}

	public PageInfo getPaging() {
		return paging;
	}

	public void setPaging(PageInfo paging) {
		this.paging = paging;
	}

	public Issue[] getIssues() {
		return issues;
	}

	public void setIssues(Issue[] issues) {
		this.issues = issues;
	}

	public Component[] getComponents() {
		return components;
	}

	public void setComponents(Component[] components) {
		this.components = components;
	}

	public Rule[] getRules() {
		return rules;
	}

	public void setRules(Rule[] rules) {
		this.rules = rules;
	}

	public User[] getUsers() {
		return users;
	}

	public void setUsers(User[] users) {
		this.users = users;
	}

	public Project[] getProjects() {
		return projects;
	}

	public void setProjects(Project[] projects) {
		this.projects = projects;
	}
    
}

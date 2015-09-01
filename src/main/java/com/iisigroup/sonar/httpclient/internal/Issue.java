package com.iisigroup.sonar.httpclient.internal;

import java.io.Serializable;
 
/**
 * The Class Issue.
 */
public class Issue implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7658042690241842744L;
    
    /** The key. */
    private String key;

    /** The component. */
    private String component;
    
    /** The component id. */
    private String componentId;
    
    /** The project. */
    private String project;
    
    /** The rule. */
    private String rule;
    
    /** The status. */
    private String status;

    /** The resolution. */
    private String resolution;
    
    /** The severity. */
    private String severity;
    
    /** The message. */
    private String message;
    
    /** The line. */
    private String line;

    /** The debt. */
    private String debt;
    
    private String assignee;
    
    /** The creation date. */
    private String creationDate;
    
    /** The update date. */
    private String updateDate;
    
    /** The update age. */
    private String fUpdateAge;
    
    /** The close date. */
    private String closeDate;

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
     * Gets the component.
     *
     * @return the component
     */
    public String getComponent() {
        return component;
    }

    /**
     * Sets the component.
     *
     * @param component the new component
     */
    public void setComponent(String component) {
        this.component = component;
    }

    /**
     * Gets the component id.
     *
     * @return the component id
     */
    public String getComponentId() {
        return componentId;
    }

    /**
     * Sets the component id.
     *
     * @param componentId the new component id
     */
    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    /**
     * Gets the project.
     *
     * @return the project
     */
    public String getProject() {
        return project;
    }

    /**
     * Sets the project.
     *
     * @param project the new project
     */
    public void setProject(String project) {
        this.project = project;
    }

    /**
     * Gets the rule.
     *
     * @return the rule
     */
    public String getRule() {
        return rule;
    }

    /**
     * Sets the rule.
     *
     * @param rule the new rule
     */
    public void setRule(String rule) {
        this.rule = rule;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status the new status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the resolution.
     *
     * @return the resolution
     */
    public String getResolution() {
        return resolution;
    }

    /**
     * Sets the resolution.
     *
     * @param resolution the new resolution
     */
    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    /**
     * Gets the severity.
     *
     * @return the severity
     */
    public String getSeverity() {
        return severity;
    }

    /**
     * Sets the severity.
     *
     * @param severity the new severity
     */
    public void setSeverity(String severity) {
        this.severity = severity;
    }

    /**
     * Gets the message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message.
     *
     * @param message the new message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the line.
     *
     * @return the line
     */
    public String getLine() {
        return line;
    }

    /**
     * Sets the line.
     *
     * @param line the new line
     */
    public void setLine(String line) {
        this.line = line;
    }

    /**
     * Gets the debt.
     *
     * @return the debt
     */
    public String getDebt() {
        return debt;
    }

    /**
     * Sets the debt.
     *
     * @param debt the new debt
     */
    public void setDebt(String debt) {
        this.debt = debt;
    }

    /**
     * Gets the creation date.
     *
     * @return the creation date
     */
    public String getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the creation date.
     *
     * @param creationDate the new creation date
     */
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Gets the update date.
     *
     * @return the update date
     */
    public String getUpdateDate() {
        return updateDate;
    }

    /**
     * Sets the update date.
     *
     * @param updateDate the new update date
     */
    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * Gets the f update age.
     *
     * @return the f update age
     */
    public String getfUpdateAge() {
        return fUpdateAge;
    }

    /**
     * Sets the f update age.
     *
     * @param fUpdateAge the new f update age
     */
    public void setfUpdateAge(String fUpdateAge) {
        this.fUpdateAge = fUpdateAge;
    }

    /**
     * Gets the close date.
     *
     * @return the close date
     */
    public String getCloseDate() {
        return closeDate;
    }

    /**
     * Sets the close date.
     *
     * @param closeDate the new close date
     */
    public void setCloseDate(String closeDate) {
        this.closeDate = closeDate;
    }

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

}

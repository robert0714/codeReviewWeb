/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package com.iisigroup.java.tech.controller.internal;

import java.io.Serializable;

/**
 *
 */
public class CheckForm implements Serializable {

	/**  */
	private static final long serialVersionUID = 3376911568783779766L;
	private String command;
	private String projectKey;
	private String projectVersion;
	private String encoding;
	private String[] step;

	public String getCommand() {
		return this.command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getProjectKey() {
		return this.projectKey;
	}

	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}

	public String getProjectVersion() {
		return this.projectVersion;
	}

	public void setProjectVersion(String projectVersion) {
		this.projectVersion = projectVersion;
	}

	public String getEncoding() {
		return this.encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String[] getStep() {
		return this.step;
	}

	public void setStep(String[] step) {
		this.step = step;
	} 
	
}

package com.iisigroup.java.tech.controller.internal;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.commons.lang3.StringUtils;

public class XlsFilenameFilter implements FilenameFilter {
	private final String xlsReg = ".*.[x][l][s]x?";

	public boolean accept(File dir, String name) {
		boolean result = StringUtils.lowerCase(name).matches(xlsReg);
		return result;
	}
}

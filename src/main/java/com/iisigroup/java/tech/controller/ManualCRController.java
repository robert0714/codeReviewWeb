/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package com.iisigroup.java.tech.controller;
 

import com.iisigroup.java.tech.utils.UserFolderUtils;
import com.iisigroup.scan.folder.internal.UserFolder;
import com.iisigroup.sonar.httpclient.statics.StaticsPreview;
import com.iisigroup.sonar.httpclient.statics.TypeIExporter;
import com.iisigroup.sonar.httpclient.statics.model.ProjectSum;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

// TODO: Auto-generated Javadoc
/**
 * The Class ManualCRController.
 */
@RequestMapping(value = "/crCtrl")
@Controller
public class ManualCRController {
	
	/** The logger. */
	private static Logger LOGGER = LoggerFactory
			.getLogger(ManualCRController.class); 
	
	/**
	 * Download xls.
	 * <br/>
	 * {@link http://localhost:8080/codeReviewWeb/ctrl/crCtrl/getPcr?projectKey=robert.lee:project&encoding=UTF-8&command=GETPCR&projectVersion=2015-02-04}
	 * 
	 * <br/>
	 * @param command the command
	 * @param projectKey the project key
	 * @param projectVersion the project version
	 * @param encoding the encoding
	 * @return the model and view
	 */
	@RequestMapping(value = "/getPcr", method = RequestMethod.GET) 
    public  ModelAndView downloadXLS(
			@RequestParam(value = "command", required = false) String command,
			@RequestParam(value = "projectKey", required = false) String projectKey,
			@RequestParam(value = "projectVersion", required = false) String projectVersion,
			@RequestParam(value = "encoding", required = false) String encoding  ) {
		LOGGER.debug("projectKey: {}", projectKey);
		LOGGER.debug("projectVersion: {}", projectVersion);
		LOGGER.debug("encoding: {}", encoding);
		LOGGER.debug("command: {}", command);
		
        if ( "GETPCR".equalsIgnoreCase(command)) {
        	
            final UserFolder target = UserFolderUtils.convert(projectKey,projectVersion, encoding);
            return new ModelAndView("pcrExcelView", "userFolder", target);
            
        }else if ("GETPSTATICS".equalsIgnoreCase(command)) {
            final UserFolder target = UserFolderUtils.convert(projectKey,
                    projectVersion, encoding);

           
            return new ModelAndView("pStatExcelView", "userFolder", target);
        }else{
        	return null;
        }
    }
       
       
}

/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package com.iisigroup.java.tech.controller;
 

import com.iisigroup.java.tech.service.ManualCRService;
import com.iisigroup.java.tech.utils.UserFolderUtils;
import com.iisigroup.scan.folder.internal.UserFolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * The Class ManualCRController.
 */
@RequestMapping(value = "/crCtrl")
@Controller
public class ManualCRController {
	
	/** The logger. */
	private static Logger LOGGER = LoggerFactory
			.getLogger(ManualCRController.class); 
	
	 @Autowired
	 private ManualCRService ctl;
	/**
	 * Download xls.
	 * <br/>
	 * {@link http://localhost:8080/codeReviewWeb/ctrl/crCtrl/getXls?projectKey=robert.lee:project&encoding=UTF-8&command=GETPCR&projectVersion=2015-02-04}
	 * 
	 * <br/>
	 * @param command the command
	 * @param projectKey the project key
	 * @param projectVersion the project version
	 * @param encoding the encoding
	 * @return the model and view
	 */
	@RequestMapping(value = "/getXls", method = RequestMethod.GET) 
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
        }else if ("GETSTATICS".equalsIgnoreCase(command)) {
             
            
            return new ModelAndView("statExcelView");
        } else  {
        	return null;
        }
    }
	
	/**
	 * Download zip.
	 * <br/>
	 * {@link http://localhost:8080/codeReviewWeb/ctrl/crCtrl/getZip?projectKey=robert.lee:project&encoding=UTF-8&command=GETPCR&projectVersion=2015-02-04 }
	 * 
	 * <br/>
	 * @param command the command
	 * @param projectKey the project key
	 * @param projectVersion the project version
	 * @param encoding the encoding
	 * @param request the request
	 * @param response the response
	 * @return the byte[]
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@RequestMapping(value = "/getZip", method = RequestMethod.GET) 
	@ResponseBody
    public  byte[]  downloadZip(
			@RequestParam(value = "command", required = false) String command,
			@RequestParam(value = "projectKey", required = false) String projectKey,
			@RequestParam(value = "projectVersion", required = false) String projectVersion,
			@RequestParam(value = "encoding", required = false) String encoding ,
			HttpServletRequest request,
			HttpServletResponse response ) throws FileNotFoundException, IOException {
		
		LOGGER.debug("projectKey: {}", projectKey);
		LOGGER.debug("projectVersion: {}", projectVersion);
		LOGGER.debug("encoding: {}", encoding);
		LOGGER.debug("command: {}", command);
		boolean userAgentIsWindows = userAgentIsWindows(request);
		
		LOGGER.debug("userAgentIsWindows: {}", userAgentIsWindows);
		
		final UserFolder target = UserFolderUtils.convert(projectKey,
                projectVersion, encoding);
		
        if ( "GETUPLOADERFILE".equalsIgnoreCase(command)) { 
        	 final File targetFile = 	this.ctl
                     .getZipFileFromUploader(target,userAgentIsWindows);
			final String fileName = target.getInfo().getEmpdata().getEmpId()
					+ "GETUPLOADERFILE";
        	 addInfZipRep(fileName, response);
        	 
            return  IOUtils.toByteArray(new FileInputStream(targetFile));            
        }else if ("GETFINISH".equalsIgnoreCase(command)) {
        	final File targetFile = 	this.ctl
                    .getZipFileFromFinish(target,userAgentIsWindows);
			final String fileName = target.getInfo().getEmpdata().getEmpId()
					+ "GETFINISH";
			 addInfZipRep(fileName, response);
        	 
	         return  IOUtils.toByteArray(new FileInputStream(targetFile));   
        } else  {
        	return null;
        }
    }
    public  static void  addInfZipRep(final String fileName   ,HttpServletResponse response) throws UnsupportedEncodingException{	
		response.setContentType("application/x-zip-compressed");
		final String attachment = String.format("attachment; filename=%s.zip",
				fileName);
		response.setHeader("Content-disposition", attachment);
	}
    private boolean userAgentIsWindows(  HttpServletRequest request){
        boolean userAgentIsWindows = false ;
        Enumeration<String> tmp = request.getHeaders("user-agent");
        while(     tmp.hasMoreElements()){
           final String value = tmp.nextElement();
           if(StringUtils.containsIgnoreCase(value, "Windows")){
               userAgentIsWindows = true ;
           };
        }
        return userAgentIsWindows;
    }   
}

/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package com.iisigroup.java.tech.controller;
 

import com.iisigroup.java.tech.controller.internal.CheckForm;
import com.iisigroup.java.tech.controller.internal.Message;
import com.iisigroup.java.tech.service.ManualCRService;
import com.iisigroup.java.tech.utils.UserFolderUtils;
import com.iisigroup.scan.folder.internal.UserFolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.validation.Valid;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * The Class ManualCRController.
 */
@RequestMapping(value = "crCtrl")
@Controller
public class ManualCRController {
	
	 private static final String UPLOAD_DIRECTORY = "upload_tmp_files";
	/** The LOGGER. */
	private static Logger LOGGER = LoggerFactory
			.getLogger(ManualCRController.class); 
	
	 @Autowired
	 private ManualCRService ctl;
	 
	 @Autowired
	 private MessageSource messageSource;
	 
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

	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String handleFormUpload(@RequestParam("name") String name,
			@RequestParam("file") MultipartFile file) {
		LOGGER.info("handleFormUpload");
		return "redirect:uploadFailure";
	}
	@RequestMapping(method=RequestMethod.POST)
	public Callable<String> processUpload(final MultipartFile file) {
		LOGGER.info("upload"); 
	    return new Callable<String>() {
	        public String call() throws Exception {
	            // ...
	            return "someView";
	        }
	    };

	}
	@RequestMapping(  value = "/upload"  ,  method = RequestMethod.POST) 
    public String upload(
    		CheckForm checkForm,
    		BindingResult bindingResult, 
    		Model uiModel, 
    		HttpServletRequest httpServletRequest, 
    		RedirectAttributes redirectAttributes, 
    		Locale locale,
    		@RequestParam(value="fileSelect", required=false) Part fileSelect) {
		LOGGER.info("upload");  
		
		
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("message", new Message("error",
                    messageSource.getMessage("contact_save_fail", new Object[]{}, locale)));
            uiModel.addAttribute("checkForm", checkForm);
            return "contacts/create";
        }
        
     // constructs the directory path to store upload file
        // this path is relative to application's directory
        
        String uploadPath = httpServletRequest.getServletContext().getRealPath("")
                + File.separator + UPLOAD_DIRECTORY;
        
        // creates the directory if it does not exist
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        uiModel.asMap().clear();
        redirectAttributes.addFlashAttribute("message", new Message("success",
                messageSource.getMessage("contact_save_success", new Object[]{}, locale)));
 

        // Process upload file
        if (fileSelect != null) {
            LOGGER.info("File name: " + fileSelect.getName());
            LOGGER.info("File size: " + fileSelect.getSize());
            LOGGER.info("File content type: " + fileSelect.getContentType());
            String fileName = null;
			
            byte[] fileContent = null;
			try {
				if (StringUtils.equals(
						fileSelect.getClass().getCanonicalName(),
						"org.apache.catalina.core.ApplicationPart")) {

					Object tmpName = PropertyUtils.getProperty(fileSelect,
							"submittedFileName");
					LOGGER.error("fileName : {}", tmpName);
					if (tmpName != null) {
						fileName = (String) tmpName;

					}
					InputStream inputStream = fileSelect.getInputStream();
					if (inputStream == null) {
						LOGGER.info("File inputstream is null");
					}

					fileContent = IOUtils.toByteArray(inputStream);
					// TODO 進行對上傳檔案的整理
					if (fileContent != null
							&& checkForm.getProjectKey() != null
							&& checkForm.getProjectVersion() != null
							&& checkForm.getEncoding() != null) {
						final UserFolder target = UserFolderUtils.convert(
								checkForm.getProjectKey(),
								checkForm.getProjectVersion(),
								checkForm.getEncoding());
						String step = StringUtils
								.join(checkForm.getStep(), ",");
						if(StringUtils.isBlank(fileName)){
							fileName= String.valueOf(RandomUtils.nextInt(0, 100));
						}
						switch (step) {
						case "2,4":
							this.ctl.writeFileByByte(fileContent, target,
									fileName);
							break;
						case "5":
							this.ctl.writeFileByByte(fileContent, target,
									fileName);
							break;

						default:
							break;
						}
					}

				}

			} catch (Exception ex) {
            	LOGGER.error(ex.getMessage() , ex);
                LOGGER.error("Error saving uploaded file");
            }
           
        }
        
        return "redirect:ldapViewCtrl/";
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

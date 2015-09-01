package com.iisigroup.java.tech.servelet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iisigroup.java.tech.controller.PersonContoller;
import com.iisigroup.java.tech.controller.SonarExecController;
import com.iisigroup.java.tech.controller.operation.FileQueueManager;
import com.iisigroup.java.tech.utils.UserFolderUtils;
import com.iisigroup.scan.folder.internal.UserFolder;

/**
 * The Class SonarExecServlet.
 */
//@WebServlet(name = "sonarExecServlet", urlPatterns = { "/sonarAnalysis" })
@Component("sonarExecServlet")
public class SonarExecServlet   extends AbstractServlet {

	@Autowired
	SonarExecController ctr;
    
    /** The logger. */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(SonarExecServlet.class);

    /**
     * Constructor of the object.
     */
    public SonarExecServlet() {
        super();
    }

    /**
     * The doGet method of the servlet. <br>
     * 
     * This method is called when a form has its tag value method equals to get.
     * 
     * @param request
     *            the request send by the client to the server
     * @param response
     *            the response send by the server to the client
     * @throws ServletException
     *             if an error occurred
     * @throws IOException
     *             if an error occurred
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        LOGGER.debug("doGet");
        String method = request.getParameter("method");
        String otherText = request.getParameter("otherText");
        LOGGER.debug("method: {}", method);
        LOGGER.debug("otherText: {}", otherText);
       
    }

    /**
     * The doPost method of the servlet. <br>
     * 
     * This method is called when a form has its tag value method equals to post.
     * 
     * @param request
     *            the request send by the client to the server
     * @param response
     *            the response send by the server to the client
     * @throws ServletException
     *             if an error occurred
     * @throws IOException
     *             if an error occurred
     */
    public void doPost(final HttpServletRequest request,
            final HttpServletResponse response) throws ServletException,
            IOException {
        LOGGER.debug("doPost"); 
        final String command = request.getParameter("command");
        final String projectKey = request.getParameter("projectKey");
        final String projectVersion = request.getParameter("projectVersion");
        final String encoding = request.getParameter("encoding");
        LOGGER.debug("projectKey: {}", projectKey);
        LOGGER.debug("projectVersion: {}", projectVersion);
        LOGGER.debug("encoding: {}", encoding);
        String result = null ;
        
        final UserFolder target = UserFolderUtils.convert (projectKey, projectVersion, encoding); 
        
        if("ANALYSIS".equalsIgnoreCase(command)){
        	synchronized (this) {
        		
          	//加入排程          	       	
          	new FileQueueManager().offer(target);
          	LOGGER.info("加入排程: {}" , ToStringBuilder.reflectionToString(target));
          	//嘗試立即處理排程
          	new FileQueueManager().processQueue() ;
          }
        }else {
        	result = ctr.getLog(target);
        	LOGGER.info("呼叫LOG: {}" , ToStringBuilder.reflectionToString(target));
        }
        if(StringUtils.isNotBlank(result)){
            response.setContentType ("application/text; charset=UTF-8");
            final PrintWriter writer = response.getWriter();
            writer.write(result);
        }
    } 

}

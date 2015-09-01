package com.iisigroup.java.tech.servelet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iisigroup.java.tech.controller.ManualCRController;
import com.iisigroup.java.tech.utils.UserFolderUtils;
import com.iisigroup.scan.folder.internal.UserFolder;
import com.iisigroup.sonar.httpclient.statics.StaticsMonthly;
import com.iisigroup.sonar.httpclient.statics.StaticsPreview;
import com.iisigroup.sonar.httpclient.statics.TypeIExporter;
import com.iisigroup.sonar.httpclient.statics.model.ProjectSum;
import com.iisigroup.sonar.httpclient.statics.model.SumPer2Weeks;

/**
 * The Class ManualCRServlet.
 */
//@WebServlet(name = "manualCRServlet", urlPatterns = { "/manualCR" })
@Component("manualCRServlet")
public class ManualCRServlet  extends AbstractServlet {

	 @Autowired
	 ServletContext context; 
	 
	 @Autowired
	 ManualCRController ctl;
	 
    /** The logger. */
    private static Logger LOGGER = LoggerFactory
            .getLogger(ManualCRServlet.class);
    // location to store file uploaded
    private static final String UPLOAD_DIRECTORY = "upload_tmp_files";
    // upload settings
    private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3; // 3MB
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB

    /**
     * Constructor of the object.
     */
    public ManualCRServlet() {
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
    	/***
    	 * http://192.168.9.22:8080/codeReviewWeb/manualCR?encoding=UTF8&projectVersion=2015-02-04&projectKey=robert.lee:project&command=GETPCR
    	 * 
    	 * *****/
        LOGGER.debug("doGet");
        String command = request.getParameter("command");
        String projectKey = request.getParameter("projectKey");
        String projectVersion = request.getParameter("projectVersion");
        String encoding = request.getParameter("encoding");
        String info  =  request.getHeader("User-Agent");
        LOGGER.debug("info: {}",info);
        if (StringUtils.containsIgnoreCase(info, info) && "GETPCR".equalsIgnoreCase(command)) {
        	
            final UserFolder target = UserFolderUtils.convert(projectKey,
                    projectVersion, encoding);

            String chtName = target.getInfo().getEmpdata().getChtName();

            final SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyyMMddmmss");

            final String fileName = String.format("%s'sJAVA處ManualCR_查核表單%s", StringUtils
                    .isBlank(chtName) ? projectKey.replace(":project", "")
                    : chtName, sdf.format(new Date()));

            final File targetFile = new File(FileUtils.getTempDirectory(),
                    fileName);

            try {
            	TypeIExporter.exportXlsV03(target, targetFile);
            }  catch (Exception e) { 
                LOGGER.error(String.format("exportFile:%s  exception:%s",fileName , e.getMessage() ), e );
                //則target = null 則會有發生xls下載長度為0的 東西出來
                //TODO ...須要列出執行
               
            }
            procesDownloadXLS(targetFile, fileName, response); 
        }else if (command!=null ) {
        	doPost(request, response);        	
        	
        }else{
        	 response.setStatus(HttpStatus.SC_FORBIDDEN) ;
        	 response.flushBuffer();
        }

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
        boolean commit = false;
        LOGGER.debug("doPost");
        String command = request.getParameter("command");
        String projectKey = request.getParameter("projectKey");
        String projectVersion = request.getParameter("projectVersion");
        String encoding = request.getParameter("encoding");
        String step = request.getParameter("step");
        LOGGER.debug("projectKey: {}", projectKey);
        LOGGER.debug("projectVersion: {}", projectVersion);
        LOGGER.debug("encoding: {}", encoding);

        String contentType = request.getContentType();
        LOGGER.debug("contentType: {}", contentType);
        if ((StringUtils.indexOf(contentType, "multipart/form-data") >= 0)) {
            // configures upload settings
            DiskFileItemFactory factory = new DiskFileItemFactory();
            // sets memory threshold - beyond which files are stored in disk
            factory.setSizeThreshold(MEMORY_THRESHOLD);
            // sets temporary location to store files
            factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

            final ServletFileUpload upload = new ServletFileUpload(factory);

            // sets maximum size of upload file
            upload.setFileSizeMax(MAX_FILE_SIZE);
            
            upload.setHeaderEncoding(Charsets.UTF_8.toString());            

            // sets maximum size of request (include file + form data)
            upload.setSizeMax(MAX_REQUEST_SIZE);

            // constructs the directory path to store upload file
            // this path is relative to application's directory
            String uploadPath = getServletContext().getRealPath("")
                    + File.separator + UPLOAD_DIRECTORY;

            // creates the directory if it does not exist
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            try {
                List<FileItem> formItems = upload.parseRequest(request);
                String fileName1 = "";
                File tmpstoreFile = null;
                if (formItems != null && formItems.size() > 0) {
                    for (FileItem item : formItems) {
                        if(item.getFieldName()==null){
                            continue;
                        }
                        // processes only fields that are not form fields
                        if (!item.isFormField() && StringUtils.isNoneBlank(item.getName())) {
                        	
                            // processUploadedFile(item);
                        	
                            String fileName = new File(item.getName())
                                    .getName();
                            fileName1 += fileName;
                            String filePath = uploadPath + File.separator
                                    + fileName;
                            tmpstoreFile = new File(filePath);
                            // saves the file on disk
                            item.write(tmpstoreFile);
                        } else if (item.isFormField() ) {
                            if ("command".equalsIgnoreCase(item.getFieldName())) {
                                command = item.getString();
                            }
                            if ("projectKey".equalsIgnoreCase(item
                                    .getFieldName())) {
                                projectKey = item.getString();
                            }
                            if ("command".equalsIgnoreCase(item.getFieldName())) {
                                command = item.getString();
                            }
                            if ("projectVersion".equalsIgnoreCase(item
                                    .getFieldName())) {
                                projectVersion = item.getString();
                            }
                            if ("encoding"
                                    .equalsIgnoreCase(item.getFieldName())) {
                                encoding = item.getString();
                            }
                            if ("step"
                                    .equalsIgnoreCase(item.getFieldName())) {
                                step = item.getString();
                            }
                        }
                    }
                }
                // TODO 進行對上傳檔案的整理
                if (tmpstoreFile != null && projectKey != null && projectVersion != null && encoding != null ) {
                    final UserFolder target = UserFolderUtils.convert(
                            projectKey, projectVersion, encoding);
                    
                    switch (step) {
                    case "2,4":
                    	this.ctl.moveFileToWorkProject(tmpstoreFile,
                                target);
                        break;
                    case "5":
                    	this.ctl.moveFileToSonarProject(tmpstoreFile,
                                target);
                        break;

                    default:
                        break;
                    }
                   
                   
                }
                // out.println("Upload has been done successfully!"+fileName1);
            } catch (Exception e) {
                LOGGER.info(e.getMessage(), e);
            }
        } else {
            boolean userAgentIsWindows = userAgentIsWindows(request);
           
            
            if ("GETUPLOADERFILE".equalsIgnoreCase(command)) {
                final UserFolder target = UserFolderUtils.convert(projectKey,
                        projectVersion, encoding);
                final File targetFile = 	this.ctl
                        .getZipFileFromUploader(target,userAgentIsWindows);
                procesDownload(targetFile, target.getInfo().getEmpdata()
                        .getEmpId()
                        + "GETUPLOADERFILE", response);
                commit = true;
            } else if ("GETFINISH".equalsIgnoreCase(command)) {
                final UserFolder target = UserFolderUtils.convert(projectKey,
                        projectVersion, encoding);
                final File targetFile = 	this.ctl
                        .getZipFileFromFinish(target,userAgentIsWindows);
                procesDownload(targetFile, target.getInfo().getEmpdata()
                        .getEmpId()
                        + "GETFINISH", response);
                commit = true;
            } else if ("GETSTATICS".equalsIgnoreCase(command)) {
                final SimpleDateFormat sdf = new SimpleDateFormat(
                        "yyyyMMddmmss");
                String fileName = "data" + sdf.format(new Date()) + ".xls";
                final File targetFile = new File(FileUtils.getTempDirectory(),
                        fileName);
                Map<String, List<SumPer2Weeks>> data = new StaticsMonthly().cal2WeeksAllProjectsGroupByDepart();
                try {

                    TypeIExporter.exportXlsV04(data, targetFile);
                }  catch (Exception e) {
                    LOGGER.error(e.getMessage() , e );
                }
                procesDownloadXLS(targetFile, fileName, response);
                commit = true;
            } else if ("GETPSTATICS".equalsIgnoreCase(command)) {
                final UserFolder target = UserFolderUtils.convert(projectKey,
                        projectVersion, encoding);

                //String chtName = target.getInfo().getEmpdata().getChtName();
                String empId = target.getInfo().getEmpdata().getEmpId();

                final SimpleDateFormat sdf = new SimpleDateFormat(
                        "yyyyMMddmmss");

                final String fileName = String.format("%s's%s.xls", StringUtils
                        .isBlank(empId) ? projectKey.replace(":project", "")
                        : empId, sdf.format(new Date()));

                final File targetFile = new File(FileUtils.getTempDirectory(),
                        fileName);

                List<ProjectSum> data = new StaticsPreview()
                        .calculateProjectsByProjectKey(projectKey);
                try {
                    TypeIExporter.exportJxlsV3(data, targetFile);
                }  catch (Exception e) { 
                    LOGGER.error(e.getMessage() , e );
                }
                procesDownloadXLS(targetFile, fileName, response);
                commit = true;
            }else if ("GETPCR".equalsIgnoreCase(command)) {
                final UserFolder target = UserFolderUtils.convert(projectKey,
                        projectVersion, encoding);
                //String chtName = target.getInfo().getEmpdata().getChtName();
                String empId = target.getInfo().getEmpdata().getEmpId();
              

                final SimpleDateFormat sdf = new SimpleDateFormat(
                        "yyyyMMddmmss");

                final String fileName = String.format("%s'sJAVA處ManualCR_查核表單%s", StringUtils
                        .isBlank(empId) ? projectKey.replace(":project", "")
                        : empId, sdf.format(new Date()));

                final File targetFile = new File(FileUtils.getTempDirectory(),
                        fileName);

                try {
                	TypeIExporter.exportXlsV03(target, targetFile);
                }  catch (Exception e) { 
                    LOGGER.error(e.getMessage() , e );
                }
                procesDownloadXLS(targetFile, fileName, response);
                commit = true;
            }
        }

        if (!commit) {
        	this.context.getRequestDispatcher("/")
                    .forward(request, response);
        }

    }
    private ServletContext getServletContext() {
		return this.context;
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
    
    private void procesDownload(final File targetFile, final String fileName,
            final HttpServletResponse response) throws IOException {
        if (targetFile == null) {
            LOGGER.info("File not exist");
            return;
        }
        LOGGER.info("FileName: {}", targetFile.getCanonicalPath());

        if (targetFile == null || targetFile.getTotalSpace() == 0) {
            return;
        }
        response.setContentType("application/x-zip-compressed");
        final String attachment = String.format("attachment; filename=%s.zip",
                fileName);
        response.setHeader("Content-disposition", attachment);
        response.setContentLength((int) targetFile.length());
        response.getOutputStream().write(getByteArrayFromFile(targetFile));
        targetFile.delete();
        LOGGER.debug("delete File:{}" ,targetFile.getCanonicalPath());
    }

    private void procesDownloadXLS(final File targetFile,
            final String fileName, final HttpServletResponse response)
            throws IOException {
        if (targetFile == null) {
            LOGGER.info("File not exist");
            return;
        }
        LOGGER.info("FileName: {}", targetFile.getCanonicalPath());

        if (targetFile == null || targetFile.getTotalSpace() == 0) {
            return;
        }
        response.setContentType("application/vnd.ms-excel");
        String newfileName = java.net.URLEncoder.encode(fileName, "UTF-8")
                .replaceAll("\\+", "%20"); // 取代+號是為了處理檔名中的空白字元問題
        final String attachment = String.format(
                ";filename*=utf-8'zh_TW'%s.xls", newfileName);
        response.setHeader("Content-disposition", attachment);
        response.setContentLength((int) targetFile.length());
        response.getOutputStream().write(getByteArrayFromFile(targetFile));
        targetFile.delete();
        LOGGER.debug("delete File:{}" ,targetFile.getCanonicalPath());
    }

    private byte[] getByteArrayFromFile(final File file) {
        FileInputStream fis = null;

        byte[] result = null;
        try {
            fis = new FileInputStream(file);
            result = IOUtils.toByteArray(fis);
        } catch (FileNotFoundException e) {
            LOGGER.info(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.info(e.getMessage(), e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    LOGGER.info(e.getMessage(), e);
                }
            }
        }

        return result;
    }

}

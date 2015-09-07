package com.iisigroup.java.tech.utils;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.iisigroup.scan.folder.internal.EmpFactory;
import com.iisigroup.scan.folder.internal.UserFolder;
import com.iisigroup.scan.folder.internal.UserProjProfile;
 
/**
 * The Class UserFolderUtils.
 */
public class UserFolderUtils {
    
    /**
     * Instantiates a new user folder utils.
     */
    private UserFolderUtils() {
    }

    /**
     * Exec by project key version.
     *
     * @param projectKey            the project key
     * @param projectVersion            the project version
     * @param encoding the encoding
     * @return the string
     */
    public static UserFolder convert(final String projectKey,
            final String projectVersion, final String encoding) {
        if (projectKey == null || projectVersion == null || encoding == null) {
            throw new NullPointerException(
                    String.format(
                            "projectKey is %s , projectVersion is %s , encoding is %s  ",
                            projectKey, projectVersion, encoding));

        }
        final EmpFactory factory = EmpFactory.getEmpFactory();

        final Map<String, UserProjProfile> map = factory
                .getUserProjProfileMapForCodeReview();

        final UserProjProfile info = map.get(projectKey);
        
        final UserFolder folder = new UserFolder();
        if(info != null ){
        	folder.setInfo(info);
        }else{
        	String email =StringUtils.replace(projectKey, ":project", "@iisigroup.com");
        	UserProjProfile candidate = factory .getUserProjForCRByEmail(email);
        	folder.setInfo(candidate);
        }
        
        folder.setProjectVersion(projectVersion);
        folder.setSourceEncoding(encoding);

        return folder;
    }
    
    /**
     * Adds the inf xls rep.
     *
     * @param target the target
     * @param response the response
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    public  static void  addInfXlsRep(final String fileName   ,HttpServletResponse response) throws UnsupportedEncodingException{		
		

		response.setContentType("application/vnd.ms-excel");
		String newfileName = java.net.URLEncoder.encode(fileName, "UTF-8")
				.replaceAll("\\+", "%20"); // 取代+號是為了處理檔名中的空白字元問題
		final String attachment = String.format(
				";filename*=utf-8'zh_TW'%s.xls", newfileName);
		response.setHeader("Content-disposition", attachment);
	}
}

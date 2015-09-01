package com.iisigroup.java.tech.utils;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.iisigroup.scan.folder.internal.EmpFactory;
import com.iisigroup.scan.folder.internal.UserFolder;
import com.iisigroup.scan.folder.internal.UserProjProfile;

public class UserFolderUtils {
    private UserFolderUtils() {
    }

    /**
     * Exec by project key version.
     * 
     * @param projectKey
     *            the project key
     * @param projectVersion
     *            the project version
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
}

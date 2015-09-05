package com.iisigroup.java.tech.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.iisigroup.java.tech.controller.operation.UserFolderOp;
import com.iisigroup.java.tech.utils.DateUtils;
import com.iisigroup.scan.folder.internal.EmpFactory;
import com.iisigroup.scan.folder.internal.UserFolder;
import com.iisigroup.scan.folder.internal.UserProjProfile;

/**
 * The Class PersonContoller.
 */

@Component
public class PersonService {
    /** The Constant logger. */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(PersonService.class);
    private  final Comparator<UserFolder> cp = new UFCP();
    
    public PersonService(){
    	//UserFolderOp
    }
    /**
     * Gets the all user.
     * 
     * @return the all user
     */
    public List<UserFolder> getAllUserForCodeReview() {
        final List<UserFolder> result = new ArrayList<UserFolder>();
        
        final EmpFactory factory = EmpFactory.getEmpFactory();
       
        final List<UserProjProfile> userProj = factory.getUserProjByLdap();
        for (UserProjProfile info : userProj) {
            UserFolder folder = new UserFolder();
            folder.setInfo(info);
            folder.setProjectVersion(DateUtils.getNowDate());
            folder.setSourceEncoding("UTF-8");
            result.add(folder);
        }

        Collections.sort(result, this.cp);
        return result;
    }
    public List<UserFolder> getAllUserByLdap() {
        final List<UserFolder> result = new ArrayList<UserFolder>();
        final EmpFactory factory = EmpFactory.getEmpFactory();
        final  List<UserProjProfile> userProjProfiles = factory.getUserProjByLdap();
        
        for (UserProjProfile info :userProjProfiles) {
            UserFolder folder = new UserFolder();
            folder.setInfo(info);
            folder.setProjectVersion(DateUtils.getNowDate());
            folder.setSourceEncoding("UTF-8");
            result.add(folder);
        }

        Collections.sort(result, this.cp);
        return result;
    }
    public List<UserFolder> getUserForCRByEmpUid(final String empUid) {
    	UserFolderOp op = new UserFolderOp();
        final List<UserFolder> result = new ArrayList<UserFolder>();
        
		final EmpFactory factory = EmpFactory.getEmpFactory();
		UserProjProfile info = factory.getUserProjForCR(empUid);

		UserFolder folder = new UserFolder();
		folder.setInfo(info);
		folder.setProjectVersion(DateUtils.getNowDate());
		folder.setSourceEncoding("UTF-8");
		result.add(folder);
		final File verDirectory = op.getProjFolderVer(folder);
		final File versonarverDirectory = op.getSonarProjVer(folder);
		try {
			if (!verDirectory.exists()) {
				FileUtils.forceMkdir(verDirectory);
			}
			if (!versonarverDirectory.exists()) {
				FileUtils.forceMkdir(verDirectory);
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage() , e );
			;
		}
        return result;
    }
    class UFCP implements Comparator<UserFolder> {

        @Override
        public int compare(UserFolder o1, UserFolder o2) {
            if (o1.getInfo().getEmpdata() != null
                    && o2.getInfo().getEmpdata() != null) {

                return o1.getInfo().getEmpdata().getEmpId()
                        .compareTo(o2.getInfo().getEmpdata().getEmpId());
            }
            return 0;
        }

    }
}

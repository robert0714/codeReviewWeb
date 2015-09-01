package com.iisigroup.java.tech.controller;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.iisigroup.java.tech.controller.internal.XlsFilenameFilter;
import com.iisigroup.java.tech.controller.operation.UserFolderOp;
import com.iisigroup.java.tech.utils.UserFolderUtils;
import com.iisigroup.java.tech.utils.ZipUtils;
import com.iisigroup.scan.folder.internal.UserFolder;

/**
 * 人工Code review 控制器
 * *.
 */
@Component
public class ManualCRController {
    /** The Constant logger. */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ManualCRController.class);

    /**
     * Gets the manual cr state.
     *
     * @param projectKey the project key
     * @param projectVersion the project version
     * @return the manual cr state
     */
    public String getManualCRState(String projectKey, String projectVersion) {
        final UserFolder target = UserFolderUtils.convert(projectKey,
                projectVersion, "UTF8");
        if (manualCRFilesExist(target)) {
            return "已上傳";
        } else {
            return "無上傳資料";
        }
    }

    /**
     * Manual cr files exist.
     *
     * @param folder the folder
     * @return the boolean
     */
    public Boolean manualCRFilesExist(final UserFolder folder) {
        final File[] array = manualCRFiles(folder);
         
        return ArrayUtils.isNotEmpty(array);
    }

    /**
     * Manual cr files.
     *
     * @param folder the folder
     * @return the file[]
     */
    protected File[] manualCRFiles(final UserFolder folder) {
        final File projFolderVer = new UserFolderOp().getSonarProjVer(folder);
        final XlsFilenameFilter xlsfilter = new XlsFilenameFilter();
        final File[] array = projFolderVer.listFiles(xlsfilter);
        return array;
    }

    /**
     * Gets the uloaders files state.
     *
     * @param projectKey the project key
     * @param projectVersion the project version
     * @return the uloaders files state
     */
    public String getUloadersFilesState(String projectKey, String projectVersion) {
        final UserFolder target = UserFolderUtils.convert(projectKey,
                projectVersion, "UTF8");
        if (uloadersFilesExist(target)) {
            return "已上傳";
        } else {
            return "無上傳資料";
        }
    }

    /**
     * Uloaders files exist.
     *
     * @param folder the folder
     * @return the boolean
     */
    public Boolean uloadersFilesExist(final UserFolder folder) {
        final File[] array = uloadersFiles(folder);
        if (ArrayUtils.isNotEmpty(array)) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Uloaders files.
     *
     * @param folder the folder
     * @return the file[]
     */
    protected File[] uloadersFiles(final UserFolder folder) {
        final File projFolderVer = new UserFolderOp().getProjFolderVer(folder);
        final XlsFilenameFilter xlsfilter = new XlsFilenameFilter();
        final File[] array = projFolderVer.listFiles(xlsfilter);
        return array;
    }

    /**
     * Gets the zip file from uploader.
     *
     * @param folder the folder
     * @return the zip file from uploader
     */
    public File getZipFileFromUploader(final UserFolder folder,boolean userAgentIsWindows) {
        final File[] array = uloadersFiles(folder);
        if (ArrayUtils.isNotEmpty(array)) {
            return userAgentIsWindows? ZipUtils.compressForWin(array):ZipUtils.compressForLinux(array)  ;
        } else {
            return null;
        }

    }

    /**
     * Finish files.
     *
     * @param folder the folder
     * @return the file[]
     */
    protected File[] finishFiles(final UserFolder folder) {
        final File sonarProjFolderVer = new UserFolderOp()
                .getSonarProjVer(folder);
        final XlsFilenameFilter xlsfilter = new XlsFilenameFilter();
        final File[] array = sonarProjFolderVer.listFiles(xlsfilter);
        return array;
    }

    /**
     * Gets the zip file from finish.
     *
     * @param folder the folder
     * @return the zip file from finish
     */
    public File getZipFileFromFinish(UserFolder folder ,boolean userAgentIsWindows) {
        final File[] array = finishFiles(folder);
        if (ArrayUtils.isNotEmpty(array)) {
            return userAgentIsWindows? ZipUtils.compressForWin(array):ZipUtils.compressForLinux(array)  ;
        } else {
            return null;
        }
    }

    /**
     * Process upload sonar project file.
     * 將檔案搬移到sonar Project當中
     * @param tmpstoreFile the tmpstore file
     * @param target the target
     */
    public void moveFileToSonarProject(File tmpstoreFile, UserFolder target) {
        final File sonarFolderVer = new UserFolderOp().getSonarProjVer(target);
        try {
            final File destFile = new File(sonarFolderVer, tmpstoreFile.getName());
            if (destFile.exists()) {
                LOGGER.info("{} already exist", destFile.getCanonicalPath());
                destFile.delete();
            	LOGGER.debug("delete File:{}" ,destFile.getCanonicalPath());
            }
            FileUtils.moveFile(tmpstoreFile, destFile);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    /**
     * Process upload work project file.
     * 將檔案搬移到work Project當中
     * @param tmpstoreFile the tmpstore file
     * @param target the target
     */
    public void moveFileToWorkProject(File tmpstoreFile, UserFolder target) {
        final File projectVerFolderVer = new UserFolderOp().getProjFolderVer(target);
        try {
            final File destFile = new File(projectVerFolderVer, tmpstoreFile.getName());
            if (destFile.exists()) {
                LOGGER.info("{} already exist", destFile.getCanonicalPath());
                destFile.delete();
                LOGGER.debug("delete File:{}" ,destFile.getCanonicalPath());
            }
            FileUtils.moveFile(tmpstoreFile, destFile);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}

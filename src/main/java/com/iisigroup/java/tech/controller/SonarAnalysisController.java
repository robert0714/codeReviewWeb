package com.iisigroup.java.tech.controller;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iisigroup.java.tech.controller.operation.FileQueueManager;
import com.iisigroup.java.tech.controller.operation.UserFolderOp;
import com.iisigroup.java.tech.utils.UserFolderUtils;
import com.iisigroup.scan.folder.internal.UserFolder;

@RequestMapping(value = "/sonarAnalysisCtrl")
@Controller
public class SonarAnalysisController {
	private static Logger LOGGER = LoggerFactory
			.getLogger(SonarAnalysisController.class);
	
	@Autowired
    private  UserFolderOp op  ;
	
	@ResponseBody
	@RequestMapping(value = "/sonarExeStatus" )
	public String sonarExeStatus(
			@RequestParam(value = "command", required = false) String command,
			@RequestParam(value = "projectKey", required = false) String projectKey,
			@RequestParam(value = "projectVersion", required = false) String projectVersion,
			@RequestParam(value = "encoding", required = false) String encoding) {

		String result = null;
		LOGGER.debug("doPost");

		LOGGER.debug("projectKey: {}", projectKey);
		LOGGER.debug("projectVersion: {}", projectVersion);
		LOGGER.debug("encoding: {}", encoding);

		final UserFolder target = UserFolderUtils.convert(projectKey,
				projectVersion, encoding);

		if ("ANALYSIS".equalsIgnoreCase(command)) {
			synchronized (this) {

				// 加入排程
				new FileQueueManager().offer(target);
				LOGGER.info("加入排程: {}",
						ToStringBuilder.reflectionToString(target));
				// 嘗試立即處理排程
				new FileQueueManager().processQueue();
			}
		} else {
			result = getLog(target);
			LOGGER.info("呼叫LOG: {}", ToStringBuilder.reflectionToString(target));
		}
		return result;
	}
	 

	public String getLog(final UserFolder folder) {
		String result = null;
		final File folderFile = this.op.getLogFileFolder(folder);
		if (folderFile != null && folderFile.isDirectory()
				&& folderFile.exists()) {
			final StringBuilder sbf = new StringBuilder();
			for (File file : folderFile.listFiles()) {
				if (file.isFile()) {

					try {
						sbf.append(FileUtils.readFileToString(file, "UTF8"))
								.append(StringUtils.CR);
					} catch (IOException e) {
						LOGGER.error(e.getMessage(), e);

					}
				}
			}
			result = sbf.toString();
		}

		return result;
	}
}

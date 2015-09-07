package com.iisigroup.java.tech.controller.view;

import com.iisigroup.java.tech.utils.UserFolderUtils;
import com.iisigroup.scan.folder.internal.UserFolder;
import com.iisigroup.sonar.httpclient.statics.CopySheets;
import com.iisigroup.sonar.httpclient.statics.StaticsPreview;
import com.iisigroup.sonar.httpclient.statics.TypeIExporter;
import com.iisigroup.sonar.httpclient.statics.model.ProjectSum;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractExcelView;

/**
 * This class builds an Excel spreadsheet document using Apache POI library.
 *
 */
@Component("pStatExcelView")
public class PStaticsExcelBuilder extends AbstractExcelView {
	private static Logger LOGGER = LoggerFactory
			.getLogger(PStaticsExcelBuilder.class);

	public PStaticsExcelBuilder() {
		LOGGER.debug(".......PStaticsExcelBuilder.......");

	}

	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// get data model which is passed by the Spring container

		Object tmp2 = model.get("userFolder");

		if (tmp2 != null) {
			final UserFolder target = (UserFolder) tmp2;

			// String chtName = target.getInfo().getEmpdata().getChtName();
			final String empId = target.getInfo().getEmpdata().getEmpId();

			final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddmmss");
			final String projectKey = target.getInfo().getProjectKey();

			List<ProjectSum> data = new StaticsPreview()
					.calculateProjectsByProjectKey(projectKey);
			try {
				final Workbook tmp = TypeIExporter.exportWorkbookJxlsV3(data);
				if (tmp != null) {
					final HSSFWorkbook workbooksrc = (HSSFWorkbook) tmp;
					for (int i = 0; i < workbooksrc.getNumberOfSheets(); i++) {
						final HSSFSheet destiSheet = workbook.createSheet();
						CopySheets.copySheets(destiSheet,
								workbooksrc.getSheetAt(i), true);
					}
				}
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}

			final String fileName = String.format("%s's%s.xls", StringUtils
					.isBlank(empId) ? projectKey.replace(":project", "")
					: empId, sdf.format(new Date()));

			UserFolderUtils.addInfXlsRep(fileName, response);
		}
	}
}

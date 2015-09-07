package com.iisigroup.java.tech.controller.view;

import com.iisigroup.java.tech.utils.UserFolderUtils;
import com.iisigroup.scan.folder.internal.UserFolder;
import com.iisigroup.sonar.httpclient.internal.PageIssues;
import com.iisigroup.sonar.httpclient.statics.TypeIExporter;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractExcelView;

/**
 * This class builds an Excel spreadsheet document using Apache POI library.
 *
 */
@Component("pcrExcelView")
public class PCRExcelBuilder extends AbstractExcelView {
	private static Logger LOGGER = LoggerFactory
			.getLogger(PCRExcelBuilder.class);

	public PCRExcelBuilder() {
		LOGGER.debug(".......PCRExcelBuilder.......");

	}

	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// get data model which is passed by the Spring container

		Object tmp2 = model.get("userFolder");

		if (tmp2 != null) {
			final UserFolder target = (UserFolder) tmp2;

			final List<PageIssues> srcTmp = TypeIExporter.getSrcV03(target);
			TypeIExporter.generatCommonXlsV03(srcTmp, target, workbook);
			UserFolderUtils.addInfXlsRep(target, response);			
		}
	}
}

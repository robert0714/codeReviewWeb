package com.iisigroup.java.tech.controller.view;

import com.iisigroup.java.tech.utils.UserFolderUtils;
import com.iisigroup.sonar.httpclient.statics.StaticsMonthly;
import com.iisigroup.sonar.httpclient.statics.TypeIExporter;
import com.iisigroup.sonar.httpclient.statics.model.SumPer2Weeks;

import java.text.SimpleDateFormat;
import java.util.Date;
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
@Component("statExcelView")
public class StaticsExcelBuilder extends AbstractExcelView {
	private static Logger LOGGER = LoggerFactory
			.getLogger(StaticsExcelBuilder.class);

	public StaticsExcelBuilder() {
		LOGGER.debug(".......staticsExcelBuilder.......");

	}

	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// get data model which is passed by the Spring container

		final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddmmss");
		String fileName = "data" + sdf.format(new Date()) + ".xls";

		Map<String, List<SumPer2Weeks>> data = new StaticsMonthly()
				.cal2WeeksAllProjectsGroupByDepart();
		try {

			TypeIExporter.exportXlsV04(data, workbook);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		UserFolderUtils.addInfXlsRep(fileName, response);

	}
}

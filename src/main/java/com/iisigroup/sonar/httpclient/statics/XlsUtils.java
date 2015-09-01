package com.iisigroup.sonar.httpclient.statics;

import java.util.Calendar; 

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class XlsUtils {
    /**
     * 調整字型/畫線
     * 
     * @param sheet
     * @param font
     *            字型大小
     * @param columnStyle
     *            {@see CellStyle.ALIGN_CENTER}
     * @param line
     *            畫線(預設值:黑色)
     * @param bold
     *            粗體字
     * @param color
     *            底色
     */
    public static CellStyle buildCellStyle(Sheet sheet, Integer fontSize,
            short columnStyle, boolean line, boolean bold, Integer color) {
        return buildCellStyle(sheet, fontSize, columnStyle, line, bold, color,
                CellStyle.VERTICAL_CENTER);
    }

    private static CellStyle buildCellStyle(Sheet sheet, Integer fontSize,
            short columnStyle, boolean line, boolean bold, Integer color,
            short aligment) {
        HSSFWorkbook wb = (HSSFWorkbook) sheet.getWorkbook();
        HSSFCellStyle cellStyle = (HSSFCellStyle) wb.createCellStyle();
        // HSSFCellStyle cellStyle = new TestHSSFCellStyle(wb).getHSSFCellStyle();
        cellStyle.setAlignment(columnStyle); // 置中對齊
        cellStyle.setVerticalAlignment(aligment); // 水平對齊
        cellStyle.setWrapText(true);

        Font titleFont = wb.createFont();
        if (bold) {
            titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD); // 粗體
        }
        titleFont.setFontName("微軟正黑體");
        if (fontSize != null) {
            short fontHeightInPoints = fontSize.shortValue();
            titleFont.setFontHeightInPoints(fontHeightInPoints);
        }
        cellStyle.setFont(titleFont);
        if (color != null) {
            cellStyle.setFillForegroundColor((short) (int) color);
            cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        }
        // cellStyle.setDataFormat(sheet.getWorkbook().createDataFormat().getFormat("#,##0")); // 千分位數學符號(ex.
        // 1,000)

        if (line) {
            cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
            cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
            cellStyle.setBorderRight(CellStyle.BORDER_THIN);
            cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        }

        return cellStyle;
    }

    public static HSSFCellStyle buildCellStyle(Sheet sheet, Integer fontSize,
            short columnStyle, String fontName, boolean bold) {
        HSSFWorkbook wb = (HSSFWorkbook) sheet.getWorkbook();
        HSSFCellStyle cellStyle = (HSSFCellStyle) wb.createCellStyle();
        // HSSFCellStyle cellStyle = new TestHSSFCellStyle(wb).getHSSFCellStyle();
        cellStyle.setAlignment(columnStyle); // 置中對齊

        cellStyle.setWrapText(true);

        Font titleFont = wb.createFont();
        if (bold) {
            titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD); // 粗體
        }
        if (fontName != null)
            titleFont.setFontName(fontName);
        else
            titleFont.setFontName("微軟正黑體");
        if (fontSize != null) {
            short fontHeightInPoints = fontSize.shortValue();
            titleFont.setFontHeightInPoints(fontHeightInPoints);
        }
        cellStyle.setFont(titleFont);

        return cellStyle;
    }
    public static void setSheetCellBlank(Sheet sheet, int x_position,
            int y_position, CellStyle cellStyle){
    	  for(int i =0 ; i< x_position ;++i){
          	for(int j =0 ;j< y_position ;++j){
          		 XlsUtils.setSheetCellPosValue(sheet, i, j, "", cellStyle, 10);
          	}
          }   	
    }
    /**
     * @param x_position
     *            第x攔 Columns are 0 based.
     * @param y_position
     *            第Y行 Rows are 0 based.
     * @param cellStyle
     *            {@see CellStyle.ALIGN_CENTER}
     * 
     */
    public static void setSheetCellPosValue(Sheet sheet, int x_position,
            int y_position, Object o, CellStyle cellStyle, float rowHeight) {
        // Create a row and put some cells in it. Rows are 0 based.
        Row row = sheet.getRow(y_position);
        if (row == null)
            row = sheet.createRow(y_position); // Create a cell and put a value in it.
        row.setHeightInPoints(rowHeight); // row 高度

        Cell cell = row.getCell(x_position);
        if (cell == null)
            cell = row.createCell(x_position);
        if (o instanceof Number) {
            cell.setCellValue(((Number) o).doubleValue());
        } else if (o instanceof RichTextString) {
            cell.setCellValue((RichTextString) o);
        } else if (o instanceof String) {
            cell.setCellValue((String) o);
        } else if (o instanceof Boolean) {
            cell.setCellValue((Boolean) o);
        }
        cell.setCellStyle(cellStyle);
    }

    public static void setSheetCellPosFormulaValue(Sheet sheet, int x_position,
            int y_position, Object o, CellStyle cellStyle, float rowHeight,
            String formula) {
        // Create a row and put some cells in it. Rows are 0 based.
        Row row = sheet.getRow(y_position);
        if (row == null)
            row = sheet.createRow(y_position); // Create a cell and put a value in it.
        row.setHeightInPoints(rowHeight); // row 高度

        Cell cell = row.getCell(x_position);
        if (cell == null)
            cell = row.createCell(x_position);

        cell.setCellFormula(formula);

        if (o instanceof Number) {
            cell.setCellValue(((Number) o).doubleValue());
        } else if (o instanceof RichTextString) {
            cell.setCellValue((RichTextString) o);
        } else if (o instanceof String) {
            cell.setCellValue((String) o);

        } else if (o instanceof Boolean) {
            cell.setCellValue((Boolean) o);
        } else if (o instanceof Calendar) {
            cell.setCellValue((Calendar) o);
        }

        cell.setCellStyle(cellStyle);
    }
}

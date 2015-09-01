package com.iisigroup.sonar.httpclient.statics;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException; 
import java.util.Calendar; 
 
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
 
/**
 * The Class POIUtils.
 */
public class POIUtils {
    /** The logger. */
    private static Logger LOGGER = LoggerFactory
            .getLogger(POIUtils.class);
	

    /**
     * Write workbook out.
     *
     * @param output the output
     * @param document the document
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void writeWorkbookOut(File output, Workbook document) throws IOException {
        ByteArrayOutputStream ostream = new ByteArrayOutputStream();
        FileOutputStream out = new FileOutputStream(output);
        try {
            document.write(ostream);
            out.write(ostream.toByteArray());
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage() , e );
            }
            try {
                ostream.close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage() , e );
            }
        }
    }

    /**
     * Write poixml document part out.
     *
     * @param output the output
     * @param document the document
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void writePOIXMLDocumentPartOut(File output, POIXMLDocument document) throws IOException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(output);
            document.write(out);
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage() , e );
            }
        }
    }

}

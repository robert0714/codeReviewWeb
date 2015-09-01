package com.iisigroup.java.tech.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset; 
import java.util.zip.ZipEntry; 
import java.util.zip.ZipOutputStream;
 
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream; 
import org.apache.commons.compress.utils.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ZipUtils.
 * http://commons.apache.org/proper/commons-compress/examples.html
 */
public class ZipUtils {
    
    /**
     * Instantiates a new zip utils.
     */
    private ZipUtils() {
    }

    /** The Constant logger. */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ZipUtils.class);
    
    /** The Constant BUFFER. */
    private static final int BUFFER = 2048;

    /**
     * Compress.
     *
     * @param files the files
     * @return the file
     */
    public static File compress(final File... files) {
        ZipOutputStream zos = null;
        FileInputStream in = null;
        File result = null;
        try {
            result = File.createTempFile("tmp_zip", ".zip");
            
            /**
             * ZipOutputStream建構子只有1.7以上才可以指定編碼..也就是1.6以下建議使用 apache compress library
             * ***/
            FileOutputStream fos = new FileOutputStream(result);
			zos = new ZipOutputStream(fos,Charset.forName("MS950"));
            /***
             * 由於中文檔案名使用...所產生的zip file會造成Windows' "compressed folder" feature 異常
             * If Windows' "compressed folders" is your primary consumer, 
             * then your best option is to explicitly set the encoding to the target platform.<br/>
             *  You may want to enable creation of Unicode extra fields so the tools that support 
             *  them will extract the file names correctly.
             *  <br/>
             *  http://commons.apache.org/proper/commons-compress/zip.html
             *   zout.setEncoding("MS950");
             * ******/
//            zos = new ZipOutputStream(fos);
             
            for (File unitFile : files) {
             
                byte[] buffer = new byte[BUFFER];
                ZipEntry ze = new ZipEntry(unitFile.getName());
                ze.setComment(unitFile.getName());                
                zos.putNextEntry(ze);
                in = new FileInputStream(unitFile);

                try {
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                } finally {
                    if (in != null) {
                        in.close();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (zos != null) {
                try {
                    zos.closeEntry();
                    // remember close it
                    zos.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }

        }
        return result;
    }
    public static File compress(String encoding , final File... files){

        ZipArchiveOutputStream zout = null;
        FileInputStream in = null;
        File result = null;
        try {
            result = File.createTempFile("tmp_zip", ".zip");
            zout = new ZipArchiveOutputStream(new BufferedOutputStream(new FileOutputStream(result)));
            /***
             * 由於中文檔案名使用...所產生的zip file會造成Windows' "compressed folder" feature 異常
             * If Windows' "compressed folders" is your primary consumer, 
             * then your best option is to explicitly set the encoding to the target platform.<br/>
             *  You may want to enable creation of Unicode extra fields so the tools that support 
             *  them will extract the file names correctly.
             *  <br/>
             *  http://commons.apache.org/proper/commons-compress/zip.html
             * ******/
            zout.setEncoding(encoding);
            
            for (File unitFile : files) {
                byte[] buffer = new byte[BUFFER];
                
               
                final ZipArchiveEntry ae = new ZipArchiveEntry(unitFile , unitFile.getName());
                 
                                
                zout.putArchiveEntry(ae);
                in = new FileInputStream(unitFile);

                try {
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        zout.write(buffer, 0, len);
                    }
                } finally {
                    if (in != null) {
                        in.close();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (zout != null) {
                try {
                    zout.closeArchiveEntry();
                    // remember close it
                    zout.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }

        }
        return result;
    
    }
    /**
     * Compress.
     *
     * @param files the files
     * @return the file
     */
    public static File compressForWin(final File... files) {
        /***
         * 由於中文檔案名使用...所產生的zip file會造成Windows' "compressed folder" feature 異常
         * If Windows' "compressed folders" is your primary consumer, 
         * then your best option is to explicitly set the encoding to the target platform.<br/>
         *  You may want to enable creation of Unicode extra fields so the tools that support 
         *  them will extract the file names correctly.
         *  <br/>
         *  http://commons.apache.org/proper/commons-compress/zip.html
         * ******/
        final File result =  compress("MS950" ,  files);
       
        return result;
    } 
    /**
     * Compress.
     *
     * @param files the files
     * @return the file
     */
    public static File compressForLinux(final File... files) {
        /***
         * 由於中文檔案名使用...所產生的zip file會造成Windows' "compressed folder" feature 異常
         * If Windows' "compressed folders" is your primary consumer, 
         * then your best option is to explicitly set the encoding to the target platform.<br/>
         *  You may want to enable creation of Unicode extra fields so the tools that support 
         *  them will extract the file names correctly.
         *  <br/>
         *  http://commons.apache.org/proper/commons-compress/zip.html
         * ******/
        final File result =  compress(Charsets.UTF_8.toString() ,  files);
       
        return result;
    }
}

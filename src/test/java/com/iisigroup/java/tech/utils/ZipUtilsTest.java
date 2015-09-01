package com.iisigroup.java.tech.utils;


import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ZipUtilsTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testCompress() throws Exception {
    	String value = System.getProperty("file.encoding");   	
    	
    	System.out.println(value);
    	
    	System.out.println("===== ENV VARIABLES =====");
    	dumpVars(System.getenv());
    	final File tmpFile1 = new File (FileUtils.getTempDirectory(),"中文檔名測試1.txt");
        final File tmpFile2 = new File (FileUtils.getTempDirectory(),"中文檔名測試2.txt");
        
        FileUtils.writeStringToFile(tmpFile1, "中文內容1",true);
        FileUtils.writeStringToFile(tmpFile2, "中文內容2",true);
        
        final File tmpZipFile = ZipUtils.compress(tmpFile1,tmpFile2);
        
        Assert.assertTrue(tmpZipFile.exists());
    }
    private static void dumpVars(Map<String, ?> m) {
    	  List<String> keys = new ArrayList<String>(m.keySet());
    	  Collections.sort(keys);
    	  for (String k : keys) {
    	    System.out.println(k + " : " + m.get(k));
    	  }
    	}
}

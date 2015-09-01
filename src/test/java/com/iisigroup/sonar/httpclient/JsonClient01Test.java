package com.iisigroup.sonar.httpclient;


import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.iisigroup.sonar.httpclient.internal.Issue;
import com.iisigroup.sonar.httpclient.internal.PageIssues;
import com.iisigroup.sonar.httpclient.internal.ProjectInfo;
import com.iisigroup.sonar.httpclient.internal.ResourceInfo;
import com.iisigroup.sonar.httpclient.internal.TimeMachineData;



public class JsonClient01Test {
	JsonClient01 component = null;
    @Before
    public void setUp() throws Exception {
    	component = new JsonClient01();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSearchIssuessByProjectId() throws Exception {
//    	List<PageIssues> pages = component.searchIssuessByProjectKey("tw.com.iisigroup.java.tech:codeReviewWeb");
    	List<PageIssues> pages = component.searchIssuessByProjectKey("yichen.chiang:project");
    	
        System.out.println(pages);
        Assert.assertNotNull(pages);
    }
//    @Test
    public void testSsearchResourceInfoByProjectId() throws Exception {
    	ResourceInfo data = component.searchResourceInfoByProjectId("peijun.jian:project");
    	
    	   System.out.println(data);
           Assert.assertNotNull(data);
    }
//    @Test
    public void testGetAllProjects() throws Exception {
    	List<ProjectInfo> data = component.getAllProjects();
    	
    	System.out.println(data);
    	 Assert.assertNotNull(data);
    }

//    @Test
    public void testCountIssueByTimemachineByProjectKey() throws Exception {
        final  TimeMachineData data = component.countIssueByTimemachineByProjectKey("peijun.jian:project");
        Assert.assertNotNull(data);
    }

    //@Test
	public void testSearchIssuessByProjectKeyStringString() throws Exception {
		List<PageIssues> pages = component.searchIssuessByProjectKey("peijun.jian:project","2015-02-09");
    	//2015-02-03T13:40:10+0800
        System.out.println(pages);
        Assert.assertNotNull(pages);
	}

	//@Test
	public void testSearchIssues() throws Exception {
		//2015-02-03T13:40:10+0800
		 List<PageIssues>  issues = component.searchIssues("peijun.jian:project",
				"2015-02-03T13:40:10+0800");
		       //2015-02-03T13:40:10+0800
		       //2015-02-03T15:17:29+0800
		System.out.println(issues);
		Assert.assertNotNull(issues);
	}

//	@Test
	public void testGetLastAnalyzedIssues() throws Exception {
		 List<PageIssues>  issues = component.getLastAnalyzedIssues("yichen.chiang:project");
		System.out.println(issues);
		Assert.assertNotNull(issues);
	}
   
}

package com.iisigroup.sonar.httpclient.statics;


import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.iisigroup.sonar.httpclient.statics.model.ProjectSum;

public class StaticsPreviewTest {
    private StaticsPreview component ;

    @Before
    public void setUp() throws Exception {
        component = new StaticsPreview();
    }

    @After
    public void tearDown() throws Exception {
    }
 
    @Test
    public void testCalculateAllProjects() throws Exception {
        List<ProjectSum> result = component.calculateAllProjects();
        System.out.println("sizes of Project : " + result.size());
        Assert.assertNotNull(result);
    }

    @Test
    public void testCalculateProjectsByProjectKey() throws Exception {
        List<ProjectSum> result = component.calculateProjectsByProjectKey("peijun.jian:project");
        Assert.assertNotNull(result);
    }

}

package com.iisigroup.sonar.httpclient;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive; 
import com.iisigroup.sonar.httpclient.internal.Cell;
import com.iisigroup.sonar.httpclient.internal.Column;
import com.iisigroup.sonar.httpclient.internal.Component;
import com.iisigroup.sonar.httpclient.internal.Issue;
import com.iisigroup.sonar.httpclient.internal.MSR;
import com.iisigroup.sonar.httpclient.internal.PageInfo;
import com.iisigroup.sonar.httpclient.internal.PageIssues;
import com.iisigroup.sonar.httpclient.internal.Project;
import com.iisigroup.sonar.httpclient.internal.ProjectInfo;
import com.iisigroup.sonar.httpclient.internal.ResourceInfo;
import com.iisigroup.sonar.httpclient.internal.Rule;
import com.iisigroup.sonar.httpclient.internal.TimeMachineData;
import com.iisigroup.sonar.httpclient.internal.User;
import com.iisigroup.sonar.httpclient.internal.annotations.JsonMappingName;

/**
 * The Class JsonClient01.
 */
public class JsonClient01 extends BaseCommonHttpMethod {
    /** The Constant logger. */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(JsonClient01.class);

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        new JsonClient01().method03();
    }

    /** The prefix url. */
    private static String prefixUrl = "http://192.168.57.22:9000";

    /**
     * Method01.
     */
    public void method01() {
        final CloseableHttpClient httpclient = getHttpClient(null, 0);

        final HttpPost httpPost = new HttpPost(prefixUrl + "/api/issues/search");
        final List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        nvps.add(new BasicNameValuePair("ID", "2240"));
        CloseableHttpResponse response = null;

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF8"));
            response = httpclient.execute(httpPost);
            LOGGER.info("{}",response.getStatusLine());
            HttpEntity entity2 = response.getEntity();
            String content = EntityUtils.toString(entity2);

            // do something useful with the response body
            // and ensure it is fully consumed

            EntityUtils.consume(entity2);
        } catch (final Exception e) {
            LOGGER.error("ERR:" + e.toString());
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                	LOGGER.error("ERR:" + e.toString());
                }
            }
        }

    }

    /**
     * Common retrieve data.
     *
     * @param param the param
     * @return the string
     */
    protected String commonRetrieveData(String param) {
        String result = null;
        final CloseableHttpClient httpclient = getHttpClient(null, 0);
        CloseableHttpResponse response = null;

        try {
            final HttpGet httpPost = new HttpGet(prefixUrl

            /****
             * reference http://docs.codehaus.org/pages/viewpage.action?pageId=229743280
             * 
             * *****/

            + param);
            response = httpclient.execute(httpPost);
            LOGGER.info("{}",response.getStatusLine());
            HttpEntity entity2 = response.getEntity();
            result = EntityUtils.toString(entity2);
            EntityUtils.consume(entity2);
        } catch (final Exception e) {
        	LOGGER.error("ERR:" + e.toString());
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOGGER.info("ERR:" + e.toString());
                }
            }
        }
        try {
            httpclient.close();
        } catch (IOException e) {
        	LOGGER.error("ERR:" + e.toString());
        }
        return result;
    }

    /**
     * Method03.
     */
    public void method03() {
        String param = "";
        param = "/api/projects/index";// 可以取得全部的project Id
        // param ="/api/issues/search";
//        param = "/api/issues/search?componentRoots=peijun.jian:project";// 指定projectId為peijun.jian:project時取得issue摘要資訊

        // param ="/api/metrics?key=ncloc_language_distribution";//取得屬性表 (不常用)
        // param ="/api/resources?resource=peijun.jian:project&metrics=ncloc,coverage";//Get lines of code and
        // code coverage measures for project
        // param ="/api/resources?resource=peijun.jian:project&depth=1&metrics=ncloc";//(可以得到Lines Of Code)
//        param = "/api/timemachine/index?resource=peijun.jian:project&depth=1&metrics=ncloc";// (可以得到各自時間版本的Lines
                                                                                            // Of Code)
         param ="/api/metrics";//取得屬性表(透過description敘述取得key之後,得知metrics要填入什麼？)
//       param ="/api/resources?resource=peijun.jian:project";
//       param ="/api/issues/search?componentRoots=peijun.jian:project&createdAfter=2015-02-03";
//       param ="/api/issues/search?pageIndex=1&createdAfter=2015-02-03&componentRoots=peijun.jian:project";
       //tw.com.iisigroup.java.tech:codeReviewWeb
//       param ="/api/issues/search?pageIndex=1&componentRoots=tw.com.iisigroup.java.tech:codeReviewWeb";
//       /api/issues/search?pageIndex=2&componentRoots=peijun.jian:project&createdAfter=2015-02-03
        // param
        // ="/api/timemachine/index?resource=peijun.jian:project&depth=1&metrics=critical_violations";//(可以得到各自時間版本的critical_violations)
        // param
        // ="/api/timemachine/index?resource=peijun.jian:project&depth=1&metrics=blocker_violations";//(可以得到各自時間版本的blocker_violations)
        // param
        // ="/api/timemachine/index?resource=peijun.jian:project&depth=1&metrics=info_violations";//(可以得到各自時間版本的info_violations)
//         param
//         ="/api/timemachine/index?resource=peijun.jian:project&depth=1&metrics=major_violations";//(可以得到各自時間版本的major_violations)
        // param
        // ="/api/timemachine/index?resource=peijun.jian:project&depth=1&metrics=minor_violations";//(可以得到各自時間版本的minor_violations)
//        param = "/api/timemachine/index?resource=peijun.jian:project&metrics=ncloc,blocker_violations,critical_violations,major_violations,minor_violations,info_violations";
        
//        param
//        ="/api/timemachine/index?resource=peijun.jian:project";//(可以得到各自時間版本的major_violations)
         param ="/api/issues/search?pageIndex=1&severities=BLOCKER,CRITICAL,MAJOR&componentRoots=pouten.tsai:project";
         
        String iniData = commonRetrieveData(param);
        LOGGER.info(iniData);
    }

    /**
     * Count issue by timemachine by project key.
     *
     * @param projectKey the project key
     * @return the time machine data
     */
    public TimeMachineData countIssueByTimemachineByProjectKey(String projectKey) {
        final String param = String
                .format("/api/timemachine/index?resource=%s&metrics=ncloc,blocker_violations,critical_violations,major_violations,minor_violations,info_violations",
                        projectKey);
        final String jsonData = commonRetrieveData(param);
        final com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
        final TimeMachineData result = new TimeMachineData();
        if(jsonData==null){
        	return result;
        }
        JsonElement element = parser.parse(jsonData);
        List<Column> columnList = null;
        List<Cell> cellList = null;
        if (element.isJsonArray()) {
            final JsonArray jsonArray = element.getAsJsonArray();
            Iterator<JsonElement> iterator = jsonArray.iterator();
            while (iterator.hasNext()) {
                final JsonElement tmp = iterator.next();
                if (tmp.isJsonObject()) {
                    final com.google.gson.JsonObject object = tmp
                            .getAsJsonObject();
                    final Set<Entry<String, JsonElement>> entrySet = object
                            .entrySet();
                    for (Entry<String, JsonElement> entryUnit : entrySet) {
                        String keyName = entryUnit.getKey();
                        JsonElement jsonElement = entryUnit.getValue();
                        if (jsonElement.isJsonArray() && "cols".equals(keyName)) {

                            try {
                                columnList = processJsonArray(jsonElement,
                                        Column.class);
                            } catch (IllegalAccessException
                                    | InstantiationException
                                    | InvocationTargetException
                                    | NoSuchMethodException e) {
                                LOGGER.error(e.getMessage(), e);
                                ;
                            }
                        } else if (jsonElement.isJsonArray()
                                && "cells".equals(keyName)) {

                            try {
                                cellList = processJsonArray(jsonElement,
                                        Cell.class);
                            } catch (IllegalAccessException
                                    | InstantiationException
                                    | InvocationTargetException
                                    | NoSuchMethodException e) {
                                LOGGER.error(e.getMessage(), e);
                                ;
                            }

                        }
                    }

                }
            }
        }

       
        result.setColumnList(columnList);
        result.setCellList(cellList);

        return result;
    }

    public List<PageIssues> searchIssues(String projectKey,String datetime) {
    	 String createdAfter = StringUtils.substring(datetime, 0 , 10);
    	 List<PageIssues> pages  =  searchIssuessByProjectKey(projectKey, createdAfter);
    	 
    	 return pages ;
    }
   
    /**
     * Search issuess by project key.
     *
     * @param projectKey the project key
     * @param createdAfter the project key
     * @return the list
     */
    public List<PageIssues> searchIssuessByProjectKey(String projectKey,String createdAfter) {
        final List<PageIssues> result = new ArrayList<PageIssues>();

        try {
            final PageIssues result01 = searchIssuessByProjectKey(projectKey, createdAfter, 1);

            final int toatal = result01.getPaging().getTotal();
            final int pageSize = result01.getPaging().getPageSize();
            final int pages = result01.getPaging().getPages();

            result.add(result01);
            for (int i = 2; i <= pages; i++) {
                final PageIssues page = searchIssuessByProjectKey(projectKey , createdAfter , i);
                result.add(page);
            }
        } catch (Exception e) {
        	LOGGER.error("projectKey: " + projectKey + "  throws errors");
            throw e;
        }

        return result;
    }
    /**
     * Search issuess by project key.
     *
     * @param projectKey the project key
     * @return the list
     */
    public List<PageIssues> searchIssuessByProjectKey(String projectKey) {
        final List<PageIssues> result = new ArrayList<PageIssues>();

        try {
            final PageIssues result01 = searchIssuessByProjectKey(projectKey, 1);

            final int toatal = result01.getPaging().getTotal();
            final int pageSize = result01.getPaging().getPageSize();
            final int pages = result01.getPaging().getPages();

            result.add(result01);
            for (int i = 2; i <= pages; i++) {
                final PageIssues page = searchIssuessByProjectKey(projectKey, i);
                result.add(page);
            }
        } catch (Exception e) {
        	LOGGER.error("projectKey: " + projectKey + "  throws errors");
            throw e;
        }

        return result;
    }

    
    /**
     * Search issuess by project key.
     *
     * @param projectKey the project key
     * @param pageIndex the page index
     * @return the page issues
     */
    protected PageIssues searchIssuessByProjectKey(final String projectKey,final String createdAfter,
            final int pageIndex) {
        String param = String.format(
                "/api/issues/search?pageIndex=%s&severities=BLOCKER,CRITICAL,MAJOR&componentRoots=%s&createdAfter=%s", pageIndex,
                projectKey,createdAfter);
        String jsonData = commonRetrieveData(param);
        com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
        JsonElement element = parser.parse(jsonData);
        PageIssues result = convertPageIssues(element);
        return result;
    }
    /**
     * Search issuess by project key.
     *
     * @param projectKey the project key
     * @param pageIndex the page index
     * @return the page issues
     */
    protected PageIssues searchIssuessByProjectKey(final String projectKey,
            final int pageIndex) {
        String param = String.format(
                "/api/issues/search?pageIndex=%s&componentRoots=%s", pageIndex,
                projectKey);
        String jsonData = commonRetrieveData(param);
        com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
        JsonElement element = parser.parse(jsonData);
        PageIssues result = convertPageIssues(element);
        return result;
    }

    /**
     * Search resource info by project id.
     *
     * @param projectKey the project key
     * @return the resource info
     */
    public ResourceInfo searchResourceInfoByProjectId(final String projectKey) {
        String param = String
                .format("/api/resources?resource=%s&metrics=ncloc,coverage",
                        projectKey);
        String jsonData = commonRetrieveData(param);
        com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
        JsonElement element = parser.parse(jsonData);
        ResourceInfo result = convertResourceInfo(element);
        return result;
    }
    public  List<PageIssues>  getLastAnalyzedIssues(final String projectKey){
    	
    	final ResourceInfo ri = searchResourceInfoByProjectId(projectKey);
    	final String creationDate = ri.getDate();
    	final  List<PageIssues>   result = searchIssues(projectKey, creationDate);
    	
    	
    	return result;
    }
    /**
     * Convert page issues.
     *
     * @param element the element
     * @return the page issues
     */
    public PageIssues convertPageIssues(final JsonElement element) {
        PageIssues result = convertTCatchException(element, PageIssues.class);
        return result;
    }

    /**
     * Convert resource info.
     *
     * @param element the element
     * @return the resource info
     */
    public ResourceInfo convertResourceInfo(final JsonElement element) {
        ResourceInfo result = convertTCatchException(element,
                ResourceInfo.class);
        return result;
    }

    /**
     * Gets the all projects.
     *
     * @return the all projects
     */
    public List<ProjectInfo> getAllProjects() {
        String param = "/api/projects/index";
        String jsonData = commonRetrieveData(param);
        com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
        JsonElement element = parser.parse(jsonData);
        List<ProjectInfo> result = convertListTCatchException(element,
                ProjectInfo.class);
        return result;
    }

    /**
     * Convert list t catch exception.
     *
     * @param <T> the generic type
     * @param element the element
     * @param clazz the clazz
     * @return the list
     */
    public <T> List<T> convertListTCatchException(final JsonElement element,
            Class<T> clazz) {
        List<T> result = null;
        try {
            result = processJsonArray(element, clazz);
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            LOGGER.error(e.getMessage(), e);
            ;
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            LOGGER.error(e.getMessage(), e);
            ;
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            LOGGER.error(e.getMessage(), e);
            ;
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            LOGGER.error(e.getMessage(), e);
            ;
        }
        return result;
    }

    /**
     * Convert t catch exception.
     *
     * @param <T> the generic type
     * @param element the element
     * @param clazz the clazz
     * @return the t
     */
    public <T> T convertTCatchException(final JsonElement element,
            Class<T> clazz) {
        T result = null;
        try {
            result = convertT(element, clazz);
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            LOGGER.error(e.getMessage(), e);
            ;
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            LOGGER.error(e.getMessage(), e);
            ;
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            LOGGER.error(e.getMessage(), e);
            ;
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            LOGGER.error(e.getMessage(), e);
            ;
        }
        return result;
    }

    /**
     * Convert t.
     *
     * @param <T> the generic type
     * @param element the element
     * @param clazz the clazz
     * @return the t
     * @throws IllegalAccessException the illegal access exception
     * @throws InstantiationException the instantiation exception
     * @throws InvocationTargetException the invocation target exception
     * @throws NoSuchMethodException the no such method exception
     */
    public <T> T convertT(final JsonElement element, Class<T> clazz)
            throws IllegalAccessException, InstantiationException,
            InvocationTargetException, NoSuchMethodException {
        T result = clazz.newInstance();

        if (element.isJsonObject()) {
            result = convertZ(element, clazz);
        } else if (element.isJsonArray()) {
            // 應急處理方法
            List<T> list = processJsonArray(element, clazz);
            if (CollectionUtils.isNotEmpty(list)) {
                result = list.get(0);
            }
        }

        return result;
    }

    /**
     * Fill unit data.
     *
     * @param <Z> the generic type
     * @param result the result
     * @param name the name
     * @param value the value
     * @throws IllegalAccessException the illegal access exception
     * @throws InvocationTargetException the invocation target exception
     * @throws NoSuchMethodException the no such method exception
     * @throws InstantiationException the instantiation exception
     */
    protected <Z> void fillUnitData(final Z result, String name,
            JsonElement value) throws IllegalAccessException,
            InvocationTargetException, NoSuchMethodException,
            InstantiationException {
        final Class<?> classType = PropertyUtils.getPropertyType(result, name);

        if (ClassUtils.isAssignable(classType, String.class)) {
            PropertyUtils.setProperty(result, name,
                    StringUtils.remove(value.toString(), "\""));
        } else if (ClassUtils.isAssignable(classType, Integer.class)) {
            PropertyUtils
                    .setProperty(result, name, Integer.valueOf(StringUtils
                            .remove(value.toString(), "\"")));

        } else if (ClassUtils.isAssignable(classType, Float.class)) {
            PropertyUtils.setProperty(result, name,
                    Float.valueOf(StringUtils.remove(value.toString(), "\"")));

        } else if (ClassUtils.isAssignable(classType, Boolean.class)) {

            PropertyUtils.setProperty(result, name,
                    BooleanUtils.toBooleanObject(value.toString()));
        } else if (ClassUtils.isAssignable(classType, PageInfo.class)) {
            PageInfo data = convertT(value, PageInfo.class);
            PropertyUtils.setProperty(result, name, data);

        } else if (ClassUtils.isAssignable(classType, Issue[].class)) {
            final JsonArray jsonArray = value.getAsJsonArray();
            final int size = jsonArray.size();
            Issue[] data = new Issue[size];
            Iterator<JsonElement> iterator = jsonArray.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                JsonElement jelement = iterator.next();

                if (jelement != null) {
                    data[i] = convertT(jelement, Issue.class);
                }
                ++i;
            }
            PropertyUtils.setProperty(result, name, data);
            // LOGGER.info(value);
        } else if (ClassUtils.isAssignable(classType, Component[].class)) {
            Component[] data = processJsonArray(value, Component.class)
                    .toArray(new Component[0]);
            PropertyUtils.setProperty(result, name, data);
            // LOGGER.info(value);
        } else if (ClassUtils.isAssignable(classType, Rule[].class)) {
            Rule[] data = processJsonArray(value, Rule.class).toArray(
                    new Rule[0]);
            PropertyUtils.setProperty(result, name, data);
            LOGGER.info("{}",value);
        } else if (ClassUtils.isAssignable(classType, User[].class)) {
            User[] data = processJsonArray(value, User.class).toArray(
                    new User[0]);
            PropertyUtils.setProperty(result, name, data);
            // LOGGER.info(value);
        } else if (ClassUtils.isAssignable(classType, Project[].class)) {
            Project[] data = processJsonArray(value, Project.class).toArray(
                    new Project[0]);
            PropertyUtils.setProperty(result, name, data);
            // LOGGER.info(value);
        } else if (ClassUtils.isAssignable(classType, MSR[].class)) {
            MSR[] data = processJsonArray(value, MSR.class).toArray(new MSR[0]);
            PropertyUtils.setProperty(result, name, data);
            // LOGGER.info(value);
        } else if (ClassUtils.isAssignable(classType, String[].class)) {
            String[] data = processJsonArray(value, String.class).toArray(
                    new String[] {});
            PropertyUtils.setProperty(result, name, data);
            // LOGGER.info(value);
        }

    }

    /**
     * Convert z.
     *
     * @param <Z> the generic type
     * @param element the element
     * @param clazz the clazz
     * @return the z
     * @throws IllegalAccessException the illegal access exception
     * @throws InstantiationException the instantiation exception
     * @throws InvocationTargetException the invocation target exception
     * @throws NoSuchMethodException the no such method exception
     */
    public <Z> Z convertZ(final JsonElement element, Class<Z> clazz)
            throws IllegalAccessException, InstantiationException,
            InvocationTargetException, NoSuchMethodException {
        Z result = clazz.newInstance();
        if (element.isJsonObject()) {
            com.google.gson.JsonObject albums = element.getAsJsonObject();
            Set<Entry<String, JsonElement>> entrySet = albums.entrySet();

            /*** 以下為新版本寫法 ***/
            final PropertyDescriptor[] pds = PropertyUtils
                    .getPropertyDescriptors(clazz);
            for (PropertyDescriptor pd : pds) {
                final String name = pd.getName();
                if ("class".equals(name)) {
                    continue;
                }
                Field field = null;

                try {
                    field = clazz.getDeclaredField(name);
                } catch (NoSuchFieldException e) {
                    LOGGER.error(e.getMessage(), e);
                    ;
                } catch (SecurityException e) {
                    LOGGER.error(e.getMessage(), e);
                    ;
                }

                JsonMappingName annotation = field
                        .getAnnotation(JsonMappingName.class);
                if (annotation != null) {
                    String jsonName = annotation.jsonName();
                    JsonElement value = albums.get(jsonName);
                    fillUnitData(result, name, value);
                } else {
                    /***
                     * 如果沒有使用annotation 則使用jsonName同名於fieldName的方式處理
                     * **/
                    JsonElement value = albums.get(name);

                    if (result != null && name != null && value != null) {
                        fillUnitData(result, name, value);
                    }

                }

            }
            /*** 以上為新版本寫法 ***/

            /*** 以下為舊版本寫法 ***/
            // for (Entry<String, JsonElement> unit : entrySet) {
            // String name = unit.getKey();
            //
            // JsonElement value = unit.getValue();
            // fillUnitData(result, name, value);
            // }
            /*** 以上為舊版本寫法 ***/
        }
        return result;
    }

    /**
     * Process json array.
     *
     * @param <E> the element type
     * @param value the value
     * @param clazz the clazz
     * @return the list
     * @throws IllegalAccessException the illegal access exception
     * @throws InstantiationException the instantiation exception
     * @throws InvocationTargetException the invocation target exception
     * @throws NoSuchMethodException the no such method exception
     */
    public <E> List<E> processJsonArray(final JsonElement value, Class<E> clazz)
            throws IllegalAccessException, InstantiationException,
            InvocationTargetException, NoSuchMethodException {
        final JsonArray jsonArray = value.getAsJsonArray();
        final List<E> container = new ArrayList<E>();
        Iterator<JsonElement> iterator = jsonArray.iterator();
        while (iterator.hasNext()) {
            JsonElement jelement = iterator.next();
            if (jelement != null && jelement.isJsonObject()) {
                E unit = convertZ(jelement, clazz);
                container.add(unit);
            } else if (jelement != null && jelement.isJsonPrimitive()) {
                JsonPrimitive jsonPrimitive = jelement.getAsJsonPrimitive();
                container.add((E) jsonPrimitive.toString());
            }
        }

        return container;
    }
}

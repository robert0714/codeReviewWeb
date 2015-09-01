package com.iisigroup.java.tech.servelet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iisigroup.java.tech.controller.ManualCRController;
import com.iisigroup.java.tech.controller.PersonContoller;
import com.iisigroup.java.tech.ldap.internal.Node;
import com.iisigroup.java.tech.service.LdapService;
import com.iisigroup.scan.folder.internal.UserFolder;

/**
 * Servlet implementation class PersonCRServlet
 */
//@WebServlet(name = "personCRServlet", urlPatterns = { "/personCR" })
@Component("personCRServlet")
public class PersonCRServlet  extends AbstractServlet {
	  
	/** The logger. */
    private static Logger LOGGER = LoggerFactory
            .getLogger(PersonCRServlet.class);
	@Autowired
	private LdapService ctl ;
    @Autowired
    PersonContoller pctr;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PersonCRServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOGGER.debug("doGet");
		final		String empUid = request.getParameter("empUid");
		LOGGER.debug("empUid: {}", empUid);
		
		final	List<UserFolder> list = pctr.getUserForCRByEmpUid(empUid);
		request.getSession().setAttribute("userFolderList", list);
		RequestDispatcher dispatcher = request.getRequestDispatcher("ldapv01.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//http://www.csstablegenerator.com/?table_id=52
		LOGGER.debug("doPost");
		String strategy = request.getParameter("strategy");
		String baseDN = request.getParameter("baseDN");

		LOGGER.debug("strategy: {}", strategy);
		LOGGER.debug("baseDN: {}", baseDN);
		JsonArray jsonArray = null;
		
		switch (strategy) {
		case "node":
			jsonArray = retireveLdapTree ();
			break;

		default:
			break;
		}
		
		
		response.setContentType ("application/json; charset=UTF-8");
		try (JsonWriter jsonWriter = Json.createWriter(response.getWriter())) {
			jsonWriter.writeArray(jsonArray);
		}
	}
	protected JsonArray getPseudo(){
		JsonBuilderFactory factory = Json.createBuilderFactory(null);
		JsonArray jsonArray = factory
				.createArrayBuilder()
				.add(factory.createObjectBuilder().add("id", "OU=TDD13,OU=TDD10,OU=15_TDD00,OU=IE,DC=iead,DC=local")
						.add("pId", "0").add("name", "TDD10").add("open", "true"))
				.add(factory.createObjectBuilder().add("id", "CN=黃少丞,OU=TDD13,OU=TDD10,OU=15_TDD00,OU=IE,DC=iead,DC=local")
						.add("pId", "OU=TDD13,OU=TDD10,OU=15_TDD00,OU=IE,DC=iead,DC=local").add("name", "黃少丞"))
				.build();
		return jsonArray;
	}
	protected JsonArray retireveLdapTree (){
		final	JsonBuilderFactory factory = Json.createBuilderFactory(null);
//		final	LdapControler ctl = new LdapControler();

		final	JsonArrayBuilder builder = factory.createArrayBuilder();
		buildJson(ctl.getNodeMap(), builder, factory);
		
		return builder.build();
	}
	protected void  buildJson(final Map<String,Node> index ,final	JsonArrayBuilder builder,JsonBuilderFactory factory ){
		final List<String> keyList = new ArrayList<String>(index.keySet());
		Collections.sort(keyList, new CompareLdapPerson(index));
		
		for(String id : keyList){
			Node node = index.get(id);			
			if(node.getData().keySet().contains("member")){
				continue;
			}
			String pId = StringUtils.substring(id, StringUtils.indexOf(id, ",")+1, id.length());
			String name =null;
			String[] candidate01 = node.getData().get("description");
			if(ArrayUtils.isNotEmpty(candidate01)){
				name =candidate01[0];
			}			
			final JsonObjectBuilder unit = factory.createObjectBuilder().add("id", id)
			.add("pId", pId).add("name", name).add("open", "true");
			
			builder.add(unit);
		}
	}
	private class CompareLdapPerson implements Comparator<String>{
		final Map<String,Node> index ;
		private static final String REGEXPRESS =".*(\\d{7}).*";
		private   Pattern pattern = Pattern.compile(REGEXPRESS) ;
		public CompareLdapPerson(final Map<String,Node> index ){
			this.index = index ;
		}
		@Override
		public int compare(String dn01, String dn02) {
			Node node01 = index.get(dn01);
			Node node02 = index.get(dn02);
			String empId01 =getEmpId ( getDs(node01));
			String empId02 =getEmpId ( getDs(node02));
			if(empId01.length() == 0 && empId02.length() == 0 ){
				return dn01.compareTo(dn02);
			}else{
				return empId01.compareTo(empId02);
			}
			
		}
		private String  getDs(Node node01){
			String[] candidate01 = node01.getData().get("description");
			if(ArrayUtils.isNotEmpty(candidate01)){
				return candidate01[0];
			}
			return "";
		}
		private String getEmpId(String ds){			
			final	Matcher matcher = pattern.matcher(ds);
			if(matcher.find()){
				return matcher.group(matcher.groupCount());
			}else {
				return "";
			}
		}
		
	}
	
}

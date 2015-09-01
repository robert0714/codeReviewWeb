package com.iisigroup.java.tech.servelet;

import java.io.IOException;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.iisigroup.java.tech.utils.notification.NotificationMgr;
import com.iisigroup.scan.folder.internal.EmpFactory;
import com.iisigroup.scan.folder.internal.UserProjProfile;

/**
 * The Class WatchersMgrServlet.
 */
//@WebServlet(name = "watchersMgrServlet", urlPatterns = { "/watchersMgr" })
@Component("watchersMgrServlet")
public class WatchersMgrServlet  extends AbstractServlet{

	
	/** The logger. */
    private static Logger LOGGER = LoggerFactory
            .getLogger(WatchersMgrServlet.class);

       
    /**
     * Instantiates a new watchers mgr servlet.
     *
     * @see HttpServlet#HttpServlet()
     */
    public WatchersMgrServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * Do get.
	 *
	 * @param request the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOGGER.debug("doGet");
		final		String empUid = request.getParameter("empUid");
		LOGGER.debug("empUid: {}", empUid);
		
			
		RequestDispatcher dispatcher = request.getRequestDispatcher("notifyConfigv01.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * Do post.
	 *
	 * @param request the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//http://www.csstablegenerator.com/?table_id=52
		LOGGER.debug("doPost");
		String empId = request.getParameter("empId");
		String strategy = request.getParameter("strategy");
		String addWatcherId = request.getParameter("addWatcherId");
		String delWatcherId = request.getParameter("delWatcherId");
		LOGGER.debug("empId: {}", empId);
		LOGGER.debug("strategy: {}", strategy);
		LOGGER.debug("addWatcherId: {}", addWatcherId);
		LOGGER.debug("delWatcherId: {}", delWatcherId);
		final NotificationMgr nmr =new NotificationMgr();
		switch (strategy) {
		
		case "add":
			nmr.modifyWatchers(empId, true, addWatcherId);
			break;
		case "delete":
			nmr.modifyWatchers(empId, false, delWatcherId);
			break;
		default:
			break;
		}
		
		JsonArray jsonArray = retrieveWatchersByEmpId (empId);
		
		response.setContentType ("application/json; charset=UTF-8");
		try (JsonWriter jsonWriter = Json.createWriter(response.getWriter())) {
			jsonWriter.writeArray(jsonArray);
		}
	}
	
	
	
	protected JsonArray retrieveWatchersByEmpId(final String empId) {		
		final JsonBuilderFactory factory = Json.createBuilderFactory(null);
		final JsonArrayBuilder builder = factory.createArrayBuilder();
		
		final EmpFactory empFactory = EmpFactory.getEmpFactory();
		final UserProjProfile userProj = empFactory.getUserProjForCR(empId);
		String[] watchers = null;
		
		if(userProj != null ){
			watchers = userProj.getEmpdata().getCodereview()
					.getWatchers();
		}
		
		if (ArrayUtils.isNotEmpty(watchers) ) {
			for (String watcherId : watchers) {
				String chtName = "";
				UserProjProfile tmp01 = empFactory.getUserProjForCR(watcherId);
				if(tmp01 != null ){
					chtName = tmp01.getEmpdata().getChtName();
				}
				final JsonObjectBuilder unit = factory.createObjectBuilder()
						.add("id", watcherId).add("name", chtName);

				builder.add(unit);
			}
		}
		return builder.build();
	}
	
	
}

package com.iisigroup.java.tech.controller;


import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iisigroup.java.tech.utils.notification.NotificationMgr;
import com.iisigroup.scan.folder.internal.EmpFactory;
import com.iisigroup.scan.folder.internal.UserProjProfile;

@RequestMapping(value = "/watchersMgrCtrl")
@Controller
public class WatchersMgrControler {

	private static Logger LOGGER = LoggerFactory
			.getLogger(WatchersMgrControler.class);
	@Autowired
	private NotificationMgr nmr  ;

	public WatchersMgrControler() {
		LOGGER.debug("...............WatchersMgrControler.........");
		;
	}
	@ResponseBody
	@RequestMapping(value = "/operateWatchers" )
	public String operateWatchers(
			@RequestParam(value = "empId", required = false) String empId,
			@RequestParam(value = "strategy", required = false) String strategy,
			@RequestParam(value = "addWatcherId", required = false) String addWatcherId,
			@RequestParam(value = "delWatcherId", required = false) String delWatcherId) {

		 
		LOGGER.debug("empId: {}", empId);
		LOGGER.debug("strategy: {}", strategy);
		LOGGER.debug("addWatcherId: {}", addWatcherId);
		LOGGER.debug("delWatcherId: {}", delWatcherId);
		
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
		
		return jsonArray.toString() ;
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

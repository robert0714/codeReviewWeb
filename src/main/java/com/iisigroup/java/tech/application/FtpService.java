/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package com.iisigroup.java.tech.application;

import com.iisigroup.java.tech.ldap.internal.Node;
import com.iisigroup.java.tech.service.LdapService;
import com.iisigroup.scan.folder.ConfigInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class FtpService {
	//================================================
	//== [Enumeration types] Block Start
	//== [Enumeration types] Block End 
	//================================================
	//== [static variables] Block Start
	 /** The Constant logger. */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(FtpService.class);
	//== [static variables] Block Stop 
	//================================================
	//== [instance variables] Block Start
    private static FtpServer server =null;
    
    private static LdapService ldapService = null;
	//== [instance variables] Block Stop 
	//================================================
	//== [static Constructor] Block Start
	//== [static Constructor] Block Stop 
	//================================================
	//== [Constructors] Block Start (含init method)
	//== [Constructors] Block Stop 
	//================================================
	//== [Static Method] Block Start
	public static void start() {
		FtpServerFactory serverFactory = new FtpServerFactory();
		List<Authority> authorities =  new  ArrayList<Authority>();  
	    authorities.add( new  WritePermission()); 
	    final String path =  new ConfigInfo().getScanFolder().getAbsolutePath();
		BaseUser user =  new  BaseUser();  
	    user.setName( "anonymous" );  
	    user.setHomeDirectory( path );
	    BaseUser user21 =  new  BaseUser();  
	    user21.setName( "21" );  
	    user21.setPassword("21");
	    user21.setHomeDirectory( path  ); 
	    user21.setAuthorities(authorities);  
	    try {
			serverFactory.getUserManager().save(user);
			serverFactory.getUserManager().save(user21);
		} catch (FtpException e) {
			LOGGER.error(e.getMessage(), e); 
		}  
		
	    final Map<String, String> mapData = getProjectFolderName();
	    if(MapUtils.isNotEmpty(mapData)){
	    	final	Set<Entry<String, String>> entrySet = mapData.entrySet();
	    	for(Entry<String, String> unit : entrySet){
	    		final String id = unit.getKey() ;
	    		final String folderName  = unit.getValue() ;
	    		BaseUser unituser =  new  BaseUser();  
	    		unituser.setName( id );  
	    		unituser.setHomeDirectory( folderName );
	    		unituser.setAuthorities(authorities); 
				try {
					serverFactory.getUserManager().save(unituser);
				} catch (FtpException e) {
					LOGGER.error(e.getMessage(), e);
				}
	    	}
	    }
	    
	    
		
		ListenerFactory factory = new ListenerFactory();
		// set the port of the listener
		factory.setPort(2221);
		// replace the default listener
		serverFactory.addListener("default", factory.createListener());
		// start the server
		
		if(FtpService.server  == null ){
			FtpService.server = serverFactory.createServer();
		}
		try {
			if(FtpService.server.isStopped()){
				FtpService.server.start();
			}
			if(FtpService.server.isSuspended()){
				FtpService.server.start();;
			}
			
			
		} catch (FtpException e) { 
			
			LOGGER.error(e.getMessage(), e); 
			
		}
	}
	public static  Map<String,String> getProjectFolderName(){
		final String path =  new ConfigInfo().getScanFolder().getAbsolutePath();
		final Map<String,String> result = new HashMap<String,String> ();
		if (ldapService == null) {
			ldapService = new LdapService();
		}
		if (ldapService != null) {
			try {
				final Map<String, Node> data = ldapService.getIndex();
				final	Set<Entry<String, Node>> entrySet = data.entrySet();
				for(final Entry<String, Node> entryUnit : entrySet){
					final String dn = entryUnit.getKey();
					final Node node = entryUnit.getValue();
					String[] ids = node.getData().get("sAMAccountName");
					String[] objectClass = node.getData().get("objectClass");
					String[] names = node.getData().get("name");
					boolean judgement  = ArrayUtils.contains(objectClass, "person")  
							&&  ArrayUtils.isNotEmpty(ids) 
							&& ! ids[0].matches("[P|T]\\d*")  
							&&  ArrayUtils.isNotEmpty(names) ;
					/**
					 * 排除員編有字母的員工
					 * ***/
					if( judgement){					
						final String unitName = String.format("%s%s%s%s" ,path, File.separator, ids[0], names[0]) ;
						result.put(ids[0],unitName);
					}
				}
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
		return result ;
	}
	public static void stop() {
		if( FtpService.server != null ){
			FtpService.server.stop();
		}
	}
	//== [Static Method] Block Stop 
	//================================================
	//== [Accessor] Block Start
	//== [Accessor] Block Stop 
	//================================================
	//== [Overrided Method] Block Start (Ex. toString/equals+hashCode)
	//== [Overrided Method] Block Stop 
	//================================================
	//== [Method] Block Start
	//####################################################################
	//## [Method] sub-block : 
	//####################################################################    
	//== [Method] Block Stop 
	//================================================
	//== [Inner Class] Block Start
	//== [Inner Class] Block Stop 
	//================================================
}

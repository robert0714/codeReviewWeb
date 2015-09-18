package com.iisigroup.java.tech.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.iisigroup.java.tech.ldap.LdapExportNode;
import com.iisigroup.java.tech.ldap.internal.Node;
import com.iisigroup.java.tech.utils.Utils;
import com.iisigroup.scan.folder.ConfigInfo;
@Component("ldapService")
public class LdapService {
	/** The Constant logger. */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(LdapService.class);
	
	private ConfigInfo config = new ConfigInfo() ;
	private LdapExportNode component = new LdapExportNode();
	;
	
	public Map<String, Node> getIndex () throws IOException{
		Map<String, Node> index =null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String today = sdf.format(new Date());
		File folder = config.getLdapCacheFolder();
		if( !folder.exists()){
			FileUtils.forceMkdir(folder);
		}
		for(File file : folder.listFiles()){
			if(StringUtils.equalsIgnoreCase(today, file.getName())){
				final InputStream is = new 	FileInputStream(file);				
				index = Utils.deserialization(is);				
			}else{
				
			}
		}
		if(index == null || MapUtils.isEmpty(index)){
			//java技術處為OU=TDD10,OU=15_TDD00,OU=IE,DC=iead,DC=local
	    	index = component.exportV1("OU=15_TDD00-BK,OU=IE,DC=iead,DC=local");
	    	// external handle for exporting to file
	        Utils.serialization(index, new File(folder , today));
		}
		return index ; 
	}
	public Map<String, Node> getNodeMap() {
//		final InputStream is = LdapService.class
//				.getResourceAsStream("ldapdata");
		//TODO 將來要改變成每天會去讀一次ldap....當天匯留一份快取...當天就都是使用這分快取
		Map<String, Node> index;

		try {
//			index = Utils.deserialization(is);
			index = getIndex ();
			final	Set<Entry<String, Node>> entrySet = index.entrySet();
			final	Map<String, Node> newIndex =new HashMap<String, Node>();
			for(final Entry<String, Node> entryUnit : entrySet){
				final String dn = entryUnit.getKey();
				final Node node = entryUnit.getValue();
				final Map<String, String[]> nodeData = node.getData();
				
				String[] ids = nodeData.get("sAMAccountName");
				String[] objectClass = nodeData.get("objectClass");
				String[] departNames = nodeData.get("department");
				if(ArrayUtils.isNotEmpty(departNames)){
					String depart =departNames[0] ;
					if(StringUtils.containsIgnoreCase(depart, "business")){
						continue ;
					}
				}
				boolean judgement  = ArrayUtils.contains(objectClass, "person")  &&  ArrayUtils.isNotEmpty(ids) && ! ids[0].matches("\\d*") ;
				/**
				 * 排除員編有字母的員工
				 * ***/
				if( !judgement){					
					newIndex.put(dn, node);
				}
			}
			return newIndex;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			index = new HashMap<String, Node>();
		} finally {
//			if (is != null) {
//				try {
//					is.close();
//				} catch (IOException e) {
//					LOGGER.error(e.getMessage(), e);
//					;
//				}
//			}
		}

		return index;
	}
	public Node getNodes(){ 
		final 	Map<String, Node> index =  getNodeMap();
		String rootOU = null; 
		for(String distinguishedName : index.keySet()){
			System.out.println(distinguishedName);
			if(rootOU !=null && distinguishedName.length() <= StringUtils.length(rootOU)){
				rootOU = distinguishedName;
			}else if (rootOU == null){
				rootOU = distinguishedName;
			}
		}
		final	Node rootNote = refernece(rootOU,index);
		return rootNote;
	}
	private Node refernece(final String  rootOU ,final 	Map<String, Node> index){
		final	Node rootNote = index.get(rootOU);
		String regExpress = String.format("\\w*=.*,%s", rootOU) ; 
		 
		Iterator<String> iterator = index.keySet().iterator(); 
		while(iterator.hasNext()){
			String distinguishedName = iterator.next() ;
			 if(distinguishedName.matches(regExpress)){			 
				
				 final Node child =  refernece(distinguishedName, index);
				 child.setParent(rootNote);				 
				 rootNote.getChildren().add(child);
				
			 }
		}
		return rootNote;
	}
}

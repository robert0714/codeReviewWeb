//package com.iisigroup.java.tech.controller;
//
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Set;
//
//import org.springframework.stereotype.Component;
//
//import com.iisigroup.java.tech.ldap.internal.Node;
//import com.iisigroup.java.tech.service.LdapService;
//@Component("ldapControler")
//public class LdapControler { 
//	private LdapService component ;
//	
//	public LdapControler(){
//		component = new LdapService();
//	}
//	
//	public Map<String, Node> getNodeMap() {
//		 Map<String, Node> result =  component.getNodeMap() ;
//		 
////		 for(Node aNode : result.values()){
////			 final	Set<Entry<String, String[]>> entrySet = aNode.getData().entrySet();
////			 
////			 for(Entry<String, String[]> unit : entrySet){
////				 System.out.println( unit.getKey() +" : "+unit.getValue()[0]);
////			 }
////		 }
//		 //
//		return result;  
//	}
//	public Node getNodes(){
//		return component . getNodes() ;
//	}
//}

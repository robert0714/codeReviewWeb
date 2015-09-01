package com.iisigroup.java.tech.ldap;


import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.EnumerationUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iisigroup.java.tech.ldap.internal.Node;
 
/*
 This class responsible for writing "o=oblix" tree into the idif file.
 
 */
/**
 * The Class LdapExportNode.
 * 參考http://osdir.com/ml/java.sun.jndi/2006-05/msg00003.html
 * 寫出來的
 * 
 */
public class LdapExportNode { 
    /** The logger. */
    private static Logger LOGGER = LoggerFactory
            .getLogger(LdapExportNode.class);
    /** The Constant SUBTREE. */
    private static final int SUBTREE = 2;

    /** The context. */
    private DirContext context;
    
    /** The env. */
    private Hashtable<String,String> env;
    
    /**
     * Instantiates a new ldap export node.
     */
    public LdapExportNode() {       
    }
    
    /**
     * Connect.
     */
    public void connect() {
        env = new Hashtable<String,String>(11);
        env.put(Context.INITIAL_CONTEXT_FACTORY,
                "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://192.168.2.12:389");
        env.put(Context.SECURITY_PRINCIPAL, "iisildap@iead.local");
        env.put(Context.SECURITY_CREDENTIALS, "iisi@1qaz2wsx");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        try {
            this.context = new InitialDirContext(env);
        } catch (NamingException e) {
            LOGGER.error("Directory server binding error");
            LOGGER.error(e.getMessage() , e );
            // logging code goes here
        }
    }

    /**
     * Convert2 node.
     *
     * @param si the si
     * @return the node
     */
    private Node convert2Node(SearchResult si ){
        final Attributes attr = si.getAttributes();
        NamingEnumeration<? extends Attribute> all = attr.getAll();
        Map<String,String[]> index = new HashMap<String,String[]>();
        Node node = new Node ();
        try {
        	
            while (all.hasMore()) {
                final Attribute nt = all.next(); 
                final String id = nt.getID();
                NamingEnumeration<?> values = nt.getAll();
                List list = EnumerationUtils.toList(values);                
                if(CollectionUtils.isNotEmpty(list)){
                    if(list.size()==1){
                        index.put(id, new String[]{list.get(0).toString()});
                    }else {
                        String[] strs = new String[list.size()];
                        for(int i = 0 ; i < list.size() ; ++i ){
                            Object oo = list.get(i);
                            strs[i] = oo.toString();
                        }
                        index.put(id, strs);
                    }
                }
                String[] dn = index.get("distinguishedName");
                node.setDistinguishedName(ArrayUtils.isNotEmpty(dn)?dn[0]:"");;                
                node.setData(index);
            }
           
        } catch (NamingException e) {
            LOGGER.error(e.getMessage() , e );
        } 
        return node ;
    }
    // here dn is base in the directry, which will be exported
    /**
     * Export v1.
     *
     * @param dn the dn
     * @return the map
     */
    public  Map<String , Node> exportV1(String dn) {
        connect();
        final      Map<String , Node> result =new HashMap<String, Node>();
        if (this.context == null){
            return result;
        }
        
        String filter = "objectclass=*";

        try {
            SearchResult si;
            final SearchControls ctls = new SearchControls();
            ctls.setSearchScope(SUBTREE);
            for (NamingEnumeration results = context.search(dn, filter, ctls); results
                    .hasMore(); ) {
                si = (SearchResult) results.next();
                
                final Node node = convert2Node(si);
                result .put(node.getData().get("distinguishedName")[0], node); 
            }

        } catch (NamingException e) {
            LOGGER.error(e.getMessage() , e );
            LOGGER.error("Failure during exporting!" + e);
            return result;
        }
        return result;
    }
     
}

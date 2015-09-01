package com.iisigroup.scan.folder.internal;
 
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils; 
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iisigroup.java.tech.ldap.internal.Node;
import com.iisigroup.java.tech.service.LdapService;
import com.iisigroup.java.tech.utils.notification.NotificationMgr;
 
/**
 * A factory for creating Emp objects.
 */
public class EmpFactory {
    /** The Constant logger. */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(EmpFactory.class);
    /** The sdf. */
    private static ThreadLocal<EmpFactory> INSTANCE = new ThreadLocal<EmpFactory>() {
        @Override
        protected EmpFactory initialValue() {
            return new EmpFactory();
        }
    };
    private static ThreadLocal<LdapService> INSTANCE2 = new ThreadLocal<LdapService>() {
        @Override
        protected LdapService initialValue() {
            return new LdapService();
        }
    };
    private static ThreadLocal<NotificationMgr> INSTANCE3 = new ThreadLocal<NotificationMgr>() {
        @Override
        protected NotificationMgr initialValue() {
            return new NotificationMgr();
        }
    };
    
    public static EmpFactory getEmpFactory(){
        return INSTANCE.get();
    }
    private EmpFactory(){}
     
    /**
     * Retrieve local ips.
     * 得到腳本發送端(遙控端) ip 資訊
     * @return the list
     */
    public static String getIpv4()  {
        try {
            final  Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()){
                NetworkInterface current = interfaces.nextElement();
                if (!current.isUp() || current.isLoopback()
                        || current.isVirtual()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = current.getInetAddresses();
                while (addresses.hasMoreElements()){
                    InetAddress current_addr = addresses.nextElement();
                    if (current_addr.isLoopbackAddress()) {
                        continue;
                    }
                    ;
                    if (current_addr instanceof Inet4Address){
                        return current_addr.getHostAddress();
                    }
                        
                }
            }
        } catch (SocketException e) {
            LOGGER.error(e.getMessage() ,e );
            
        }
        return null;
    }

    /**
     * Convert.
     *
     * @param emp the emp
     * @return the user proj profile
     */
    protected UserProjProfile convert(final EmpDto emp) {
        final UserProjProfile profile = new UserProjProfile();

        profile.setEmpdata(emp);

        return profile;
    }

    /**
     * Gets the user proj profile map.
     * 列出需要CR的員工清單
     * @return the user proj profile map 
     */
    public Map<String, UserProjProfile> getUserProjProfileMapForCodeReview() {
        final Map<String, UserProjProfile> result = new HashMap<String, UserProjProfile>();
        final List<EmpDto> empList = getEmpDtoByLdap();
        for (final EmpDto emp : empList) {
        	
//            if(emp.getCodereview().isVisible()){
                final UserProjProfile value = convert(emp);
                result.put(value.getProjectKey(), value);
//            }
        }
        return result;
    }
    /***
     * 列出需要CR的員工清單
     * 
     * **/
    public List<EmpDto> getEmpDtoByLdap() {
    	List<EmpDto> result = new ArrayList<EmpDto>();
		final LdapService service = INSTANCE2.get();
		final NotificationMgr mgr = INSTANCE3.get();
		
		final Collection<Node> nodes = service.getNodeMap().values();
		for (Node node : nodes) {
			final	Map<String, String[]> data = node.getData() ;
			String[] mails = data.get("mail");
			String[] ids = data.get("sAMAccountName");
			String[] departNames = data.get("department");
			
			if (ArrayUtils.isNotEmpty(mails)) {
				String id = ids[0];
				String mail = StringUtils.trim(mails[0]);
				EmpDto empDto = new EmpDto();
				empDto.setEmpId(id);
				empDto.setChtName( StringUtils.trim(data.get("name")[0]));
				empDto.setDepart(StringUtils.trim(departNames[0]));				
				empDto.setEmail(mail);
				
				CodeReviwEvent codereview = mgr.getNotifyConfig(empDto);;
				empDto.setCodereview(codereview);
				result.add(empDto);				
			}
		}
		return result;
    }
    /***
     * 列出需要CR的員工清單
     * 
     * **/
	public List<UserProjProfile> getUserProjByLdap() {
		final List<UserProjProfile> result = new ArrayList<UserProjProfile>();
		
		final List<EmpDto> empList =  getEmpDtoByLdap() ; 
		
		for (EmpDto empDto  : empList) {
			final UserProjProfile unit = convert(empDto);
			result.add(unit);
		}
		return result;
	}

    public  UserProjProfile getUserProjForCR(String empUid){
    	final NotificationMgr mgr = INSTANCE3.get();
    	EmpDto empDto =null;
    	LdapService service = INSTANCE2.get();    	
    	Collection<Node> nodes = service.getNodeMap().values();
    	for(Node node : nodes ){
    		final	Map<String, String[]> nodeData = node.getData(); 
    		String[] ids = nodeData.get("sAMAccountName");
    		if(ArrayUtils.isNotEmpty(ids)){
    			String id = ids[0];
    			if(StringUtils.equals(id, empUid)){
    				empDto = new EmpDto ();
    				empDto.setEmpId(id);
    				empDto.setChtName( nodeData.get("name")[0]);
    				
    				if( ArrayUtils.isNotEmpty(nodeData.get("mail")) ){    					
    					empDto.setEmail( StringUtils.trim(nodeData.get("mail")[0]));
    				}
    				
    				CodeReviwEvent codereview = mgr.getNotifyConfig(empDto);;
    				empDto.setCodereview(codereview);
    				break;
    			}
    		}
    	}
    	
    	if(empDto != null ){
    		final UserProjProfile result = convert(empDto);
        	return result ;
    	}else{
    		return null; 
    	}
    }
    public  UserProjProfile getUserProjForCRByEmail(String email){
    	final NotificationMgr mgr = INSTANCE3.get();
    	EmpDto empDto =null;
    	LdapService service = INSTANCE2.get();    	
    	Collection<Node> nodes = service.getNodeMap().values();
    	for(Node node : nodes ){
    		String[] mails = node.getData().get("mail");
    		String[] ids = node.getData().get("sAMAccountName");
    		if(ArrayUtils.isNotEmpty(mails)){
    			String id = ids[0];
    			String mail = mails[0];
    			if(StringUtils.equals(mail, email)){
    				empDto = new EmpDto ();
    				empDto.setEmpId(id);
    				empDto.setChtName(node.getData().get("name")[0]);
    				empDto.setEmail( StringUtils.trim(node.getData().get("mail")[0]));
    				CodeReviwEvent codereview = mgr.getNotifyConfig(empDto);;
    				empDto.setCodereview(codereview);
    				break;
    			}
    		}
    	}
    	
    	if(empDto != null ){
    		final UserProjProfile result = convert(empDto);
        	return result ;
    	}else{
    		return null; 
    	}
    	 
    }
}

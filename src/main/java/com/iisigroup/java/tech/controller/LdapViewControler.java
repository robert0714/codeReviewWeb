package com.iisigroup.java.tech.controller;

import java.io.IOException;
import java.io.InputStream;
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
import javax.servlet.http.Part;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.iisigroup.java.tech.ldap.internal.Node;
import com.iisigroup.java.tech.servelet.Filter;
import com.iisigroup.java.tech.service.LdapService;
import com.iisigroup.java.tech.service.PersonService;
import com.iisigroup.scan.folder.internal.UserFolder;

@RequestMapping(value = "/ldapViewCtrl")
@Controller
public class LdapViewControler {

	private static Logger LOGGER = LoggerFactory
			.getLogger(LdapViewControler.class);
	@Autowired
	private LdapService component ;
    @Autowired
    PersonService pctr;

	public LdapViewControler() {
		LOGGER.debug("...............LdapViewControler.........");
		;
	}
	@RequestMapping(   params = "form", method = RequestMethod.POST)
//	@RequestMapping(  value = "/upload" ,  method = RequestMethod.GET )
    public String upload(
    		@RequestParam(value = "command", required = false) String command,
			@RequestParam(value = "projectKey", required = false) String projectKey,
			@RequestParam(value = "projectVersion", required = false) String projectVersion,
			@RequestParam(value = "encoding", required = false) String encoding , 
			@RequestParam(value = "step", required = false) String[] step ,
    		 
    		@RequestParam(value="file", required=false) Part file) {
		LOGGER.info("upload");  
		
		
//        if (bindingResult.hasErrors()) {
//            uiModel.addAttribute("message", new Message("error",
//                    messageSource.getMessage("contact_save_fail", new Object[]{}, locale)));
//            uiModel.addAttribute("contact", contact);
//            return "contacts/create";
//        }
        
     // constructs the directory path to store upload file
        // this path is relative to application's directory
        
//        String uploadPath = httpServletRequest.getServletContext().getRealPath("")
//                + File.separator + UPLOAD_DIRECTORY;
//        
//        // creates the directory if it does not exist
//        File uploadDir = new File(uploadPath);
//        if (!uploadDir.exists()) {
//            uploadDir.mkdir();
//        }
//        uiModel.asMap().clear();
//        redirectAttributes.addFlashAttribute("message", new Message("success",
//                messageSource.getMessage("contact_save_success", new Object[]{}, locale)));
 

        // Process upload file
        if (file != null) {
            LOGGER.info("File name: " + file.getName());
            LOGGER.info("File size: " + file.getSize());
            LOGGER.info("File content type: " + file.getContentType());
            byte[] fileContent = null;
            try {
                InputStream inputStream = file.getInputStream();
                if (inputStream == null) LOGGER.info("File inputstream is null");
                fileContent = IOUtils.toByteArray(inputStream);
               
            } catch (IOException ex) {
            	LOGGER.error(ex.getMessage() , ex);
                LOGGER.error("Error saving uploaded file");
            }
           
        }
        
        return "redirect:/contacts/";
    }
	@RequestMapping(value = "/personCR")
	public ModelAndView personCR(
			@RequestParam(value = "empUid", required = false) String empUid)
			throws Exception {

		LOGGER.debug("personCR");
		LOGGER.debug("empUid: {}", empUid);

		final List<UserFolder> list = pctr.getUserForCRByEmpUid(empUid);

		ModelAndView model = new ModelAndView("ldapv01");
		model.addObject("userFolderList", list);
		return model;
	}
	@ResponseBody
	@RequestMapping(value = "/listTree" )
	public String listTree(
			@RequestParam(value = "strategy", required = false) String strategy,
			@RequestParam(value = "baseDN", required = false) String baseDN) {

		LOGGER.debug("listTree");

		LOGGER.debug("strategy: {}", strategy);
		LOGGER.debug("baseDN: {}", baseDN); 
		final  String result = retireveLdapTree().toString();
		
		return result ;
	} 

	protected JsonArray retireveLdapTree() {
		final JsonBuilderFactory factory = Json.createBuilderFactory(null);

		final JsonArrayBuilder builder = factory.createArrayBuilder();

		final Filter filter = new NodeFilter();
		buildJson(component.getNodeMap(), builder, factory, filter);

		return builder.build();
	}

	protected void buildJson(final Map<String, Node> index,
			final JsonArrayBuilder builder, JsonBuilderFactory factory,
			Filter filter) {
		final List<String> keyList = new ArrayList<String>(index.keySet());
		Collections.sort(keyList, new CompareLdapPerson(index));

		for (String id : keyList) {
			final Node node = index.get(id);
			if (node.getData().keySet().contains("member")) {
				continue;
			}
			final String pId = StringUtils.substring(id,
					StringUtils.indexOf(id, ",") + 1, id.length());

			final String name = getName(node);

			if (StringUtils.isBlank(name) || StringUtils.isBlank(pId)
					|| StringUtils.isBlank(id)) {
				continue;
			}
			final JsonObjectBuilder unit = factory.createObjectBuilder()
					.add("id", id).add("pId", pId).add("name", name);

			
			if (filter.include(node)) {
				unit.add("open", "true");
			} else {
				unit.add("open", "false");
			}
			builder.add(unit);
		}
	}

	private class CompareLdapPerson implements Comparator<String> {
		final Map<String, Node> index;
		private static final String REGEXPRESS = ".*(\\d{7}).*";
		private Pattern pattern = Pattern.compile(REGEXPRESS);

		public CompareLdapPerson(final Map<String, Node> index) {
			this.index = index;
		}

		@Override
		public int compare(String dn01, String dn02) {
			Node node01 = index.get(dn01);
			Node node02 = index.get(dn02);
			String empId01 = getEmpId(getDs(node01));
			String empId02 = getEmpId(getDs(node02));
			if (empId01.length() == 0 && empId02.length() == 0) {
				return dn01.compareTo(dn02);
			} else {
				return empId01.compareTo(empId02);
			}

		}

		private String getDs(Node node01) {
			String[] candidate01 = node01.getData().get("description");
			if (ArrayUtils.isNotEmpty(candidate01)) {
				return candidate01[0];
			}
			return "";
		}

		private String getEmpId(String ds) {
			final Matcher matcher = pattern.matcher(ds);
			if (matcher.find()) {
				return matcher.group(matcher.groupCount());
			} else {
				return "";
			}
		}

	}

	String getName(Node node) {
		String name = null;
		String[] candidate01 = node.getData().get("description");
		if (ArrayUtils.isNotEmpty(candidate01)) {
			name = candidate01[0];
		}
		return name;
	}

	private class NodeFilter implements Filter {
		String includeRegExpr = ".*[處|部]";
		String excludeRegExpr = ".*組";

		@Override
		public boolean exclude(Node node) {
			String name = getName(node);

			if (name.matches(excludeRegExpr)) {
				return true;
			}
			return false;
		}

		@Override
		public boolean include(Node node) {
			String name = getName(node);

			if (name.matches(includeRegExpr)) {
				return true;
			}
			return false;
		}

	}
}

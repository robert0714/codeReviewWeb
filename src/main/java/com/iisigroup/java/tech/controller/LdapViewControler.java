package com.iisigroup.java.tech.controller;

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

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iisigroup.java.tech.ldap.internal.JsonNode;
import com.iisigroup.java.tech.ldap.internal.Node;
import com.iisigroup.java.tech.servelet.Filter;

@RequestMapping(value = "/ldapViewCtrl")
@Controller
public class LdapViewControler {

	private static Logger LOGGER = LoggerFactory
			.getLogger(LdapViewControler.class);
	@Autowired
	private LdapControler ctl;

	public LdapViewControler() {
		LOGGER.debug("...............LdapViewControler.........");
		;
	}

	@ResponseBody
	@RequestMapping(value = "/listTree", method = RequestMethod.GET)
	public JsonArray listTree(
			@RequestParam(value = "strategy", required = false) String strategy,
			@RequestParam(value = "baseDN", required = false) String baseDN) {

		LOGGER.debug("listTree");

		LOGGER.debug("strategy: {}", strategy);
		LOGGER.debug("baseDN: {}", baseDN); 
		JsonArray  result = retireveLdapTree();
		return  result ;
	}
	protected JsonArray getPseudo() {
		JsonBuilderFactory factory = Json.createBuilderFactory(null);
		JsonArray jsonArray = factory
				.createArrayBuilder()
				.add(factory
						.createObjectBuilder()
						.add("id",
								"OU=TDD13,OU=TDD10,OU=15_TDD00,OU=IE,DC=iead,DC=local")
						.add("pId", "0").add("name", "TDD10")
						.add("open", "true"))
				.add(factory
						.createObjectBuilder()
						.add("id",
								"CN=黃少丞,OU=TDD13,OU=TDD10,OU=15_TDD00,OU=IE,DC=iead,DC=local")
						.add("pId",
								"OU=TDD13,OU=TDD10,OU=15_TDD00,OU=IE,DC=iead,DC=local")
						.add("name", "黃少丞")).build();
		return jsonArray;
	}

	protected JsonArray retireveLdapTree() {
		final JsonBuilderFactory factory = Json.createBuilderFactory(null);
		// final LdapControler ctl = new LdapControler();

		final JsonArrayBuilder builder = factory.createArrayBuilder();

		final Filter filter = new NodeFilter();
		buildJson(ctl.getNodeMap(), builder, factory, filter);

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
//			JsonNode unit01 = new JsonNode();
//			unit01.setId(id);
//			unit01.setName(name);
//			unit01.setpId(pId);
			
			if (filter.include(node)) {
				unit.add("open", "true");
//				unit01.setOpen(true);
			} else {
				unit.add("open", "false");
//				unit01.setOpen(false);
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

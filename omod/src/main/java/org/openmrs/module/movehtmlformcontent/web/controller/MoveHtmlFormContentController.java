/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.movehtmlformcontent.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.openmrs.Form;
import org.openmrs.api.context.Context;
import org.openmrs.module.htmlformentry.HtmlForm;
import org.openmrs.module.htmlformentry.HtmlFormEntryService;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The main controller.
 */
@Controller
public class MoveHtmlFormContentController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(value = "/module/movehtmlformcontent/move", method = RequestMethod.GET)
	public void showForm(Model model) {
	}
	
	@RequestMapping(value = "/module/movehtmlformcontent/move", method = RequestMethod.POST)
	public String handleSubmit(@RequestParam("server") String server, @RequestParam("username") String username,
	                           @RequestParam("password") String password, @RequestParam("uuids") String uuids,
	                           HttpSession session) throws Exception {
		List<String> toFetch = new ArrayList<String>();
		for (StringTokenizer st = new StringTokenizer(uuids, ", "); st.hasMoreTokens();) {
			toFetch.add(st.nextToken());
		}
		
		HttpClient httpClient = new DefaultHttpClient();
		
		StringBuilder log = new StringBuilder();
		
		try {
			while (toFetch.size() > 0) {
				String uuid = toFetch.remove(0);
				log.append("Fetching " + uuid);
				String url = server;
				if (!url.endsWith("/")) {
					url += "/";
				}
				url += "module/movehtmlformcontent/htmlformxml/" + uuid;
				
				URIBuilder builder = new URIBuilder(url);
				builder.addParameter("username", username);
				builder.addParameter("password", password);
				
				HttpGet get = new HttpGet(builder.build());
				HttpResponse response = httpClient.execute(get);
				String xml = EntityUtils.toString(response.getEntity());
				
				Form form = Context.getFormService().getFormByUuid(uuid);
				HtmlForm hf = Context.getService(HtmlFormEntryService.class).getHtmlFormByForm(form);
				hf.setXmlData(xml);
				Context.getService(HtmlFormEntryService.class).saveHtmlForm(hf);
				log.append(" ... saved. Length = " + xml.length() + "\n");
			}
		}
		catch (Exception ex) {
			throw new RuntimeException("When things broke, we had already done:\n" + log, ex);
		}
		session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "<pre>" + log + "</pre>");
		return "redirect:move.form";
	}
}

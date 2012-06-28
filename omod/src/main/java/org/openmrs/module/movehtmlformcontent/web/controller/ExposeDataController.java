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

import java.util.HashMap;
import java.util.Map;

import org.openmrs.Form;
import org.openmrs.api.context.Context;
import org.openmrs.module.htmlformentry.HtmlForm;
import org.openmrs.module.htmlformentry.HtmlFormEntryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ExposeDataController {
	
	@ResponseBody
	@RequestMapping(value = "/module/movehtmlformcontent/htmlformxml/{formUuid}", method = RequestMethod.GET)
	public void getFormXml(@RequestParam("username") String username,
	                       @RequestParam("password") String password,
	                       @PathVariable("formUuid") String formUuid) {
		if (!Context.isAuthenticated()) {
			Context.authenticate(username, password);
		}
		Form form = Context.getFormService().getFormByUuid(formUuid);
		HtmlForm hf = Context.getService(HtmlFormEntryService.class).getHtmlFormByForm(form);
		Map<String, String> ret = new HashMap<String, String>();
		ret.put("xml", hf.getXmlData());
	}
	
}

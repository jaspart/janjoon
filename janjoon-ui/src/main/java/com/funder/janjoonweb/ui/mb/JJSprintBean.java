package com.funder.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.List;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.funder.janjoonweb.domain.JJSprint;

@RooSerializable
@RooJsfManagedBean(entity = JJSprint.class, beanName = "jJSprintBean")
public class JJSprintBean {
	
	public List<JJSprint> completeSprint(String query) {
		List<JJSprint> suggestions = new ArrayList<JJSprint>();

		for (JJSprint jJSprint :  jJSprintService.getAllJJSprints()) {
			String jJChapterStr = String.valueOf(jJSprint.getName() + " "
					+ jJSprint.getDescription() + " "
					+ jJSprint.getCreationDate() + " "
					+ jJSprint.getUpdatedDate());
			if (jJChapterStr.toLowerCase().startsWith(query.toLowerCase())) {
				suggestions.add(jJSprint);
			}
		}
		return suggestions;
	}
	
}

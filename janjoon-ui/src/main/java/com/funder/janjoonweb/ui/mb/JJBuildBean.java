package com.funder.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.List;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.funder.janjoonweb.domain.JJBuild;

@RooSerializable
@RooJsfManagedBean(entity = JJBuild.class, beanName = "jJBuildBean")
public class JJBuildBean {
	
	
	public List<JJBuild> completeBuild(String query) {
		List<JJBuild> suggestions = new ArrayList<JJBuild>();

		for (JJBuild jJBuild :  jJBuildService.findAllJJBuilds()) {
			String jJChapterStr = String.valueOf(jJBuild.getName() + " "
					+ jJBuild.getDescription() + " "
					+ jJBuild.getCreationDate() + " "
					+ jJBuild.getUpdatedDate());
			if (jJChapterStr.toLowerCase().startsWith(query.toLowerCase())) {
				suggestions.add(jJBuild);
			}
		}
		return suggestions;
	}
}

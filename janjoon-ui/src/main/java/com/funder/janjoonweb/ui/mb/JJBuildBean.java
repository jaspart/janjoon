package com.funder.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.List;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.funder.janjoonweb.domain.JJBuild;

@RooSerializable
@RooJsfManagedBean(entity = JJBuild.class, beanName = "jJBuildBean")
public class JJBuildBean {

	private JJBuild jJBuild;
	private List<JJBuild> jJBuildList;

	public JJBuild getjJBuild() {
		return jJBuild;
	}

	public void setjJBuild(JJBuild jJBuild) {
		this.jJBuild = jJBuild;
	}

	public List<JJBuild> getjJBuildList() {
		jJBuildList = jJBuildService.getAllJJBuilds();
		return jJBuildList;
	}

	public void setjJBuildList(List<JJBuild> jJBuildList) {
		this.jJBuildList = jJBuildList;
	}

	public List<JJBuild> completeBuild(String query) {
		List<JJBuild> suggestions = new ArrayList<JJBuild>();

		for (JJBuild jJBuild : jJBuildService.getAllJJBuilds()) {
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

	public void handleSelectBuild(
			JJTestcaseexecutionBean jJTestcaseexecutionBean,
			JJTeststepexecutionBean jJTeststepexecutionBean,
			JJTestcaseBean jJTestcaseBean) {
		
		if (jJTestcaseexecutionBean != null) {
			jJTestcaseexecutionBean.setCurrentBuild(jJBuild);
		}
		if (jJTeststepexecutionBean != null) {
			jJTeststepexecutionBean.setCurrentBuild(jJBuild);
		}
		if (jJTestcaseBean != null) {
			jJTestcaseBean.setCurrentBuild(jJBuild);
		}

	}
}

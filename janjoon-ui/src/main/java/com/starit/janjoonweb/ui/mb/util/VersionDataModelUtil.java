package com.starit.janjoonweb.ui.mb.util;

import java.util.List;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJBugService;
import com.starit.janjoonweb.domain.JJBuild;
import com.starit.janjoonweb.domain.JJBuildService;
import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJRequirementService;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.ui.mb.JJBugBean;
import com.starit.janjoonweb.ui.mb.JJRequirementBean;
import com.starit.janjoonweb.ui.mb.LoginBean;

public class VersionDataModelUtil {

	private String name;
	private JJVersion version;
	private List<JJBuild> builds;
	private List<JJRequirement> requirementRoadMap;
	private List<JJBug> bugRoadMap;

	public VersionDataModelUtil(JJVersion version, JJBuildService jjBuildService) {

		JJRequirementBean requirementBean = (JJRequirementBean) LoginBean
				.findBean("jJRequirementBean");
		JJBugBean bugBean = (JJBugBean) LoginBean.findBean("jJBugBean");

		this.version = version;
		this.builds = jjBuildService.getBuilds(null, version, true);

		if (requirementBean == null)
			requirementBean = new JJRequirementBean();

		if (bugBean == null)
			bugBean = new JJBugBean();

		this.requirementRoadMap = requirementBean
				.getInfinshedRequirement(version);
		this.bugRoadMap = bugBean.getInfinshedBugs(version);

		this.name = (version != null) ? version.getProduct().getName() + "/"
				+ version.getName() : MessageFactory.getMessage(
				"label_noVersion").getDetail();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public JJVersion getVersion() {
		return version;
	}

	public void setVersion(JJVersion version) {
		this.version = version;
	}

	public List<JJBuild> getBuilds() {
		return builds;
	}

	public void setBuilds(List<JJBuild> builds) {
		this.builds = builds;
	}

	public List<JJRequirement> getRequirementRoadMap() {
		return requirementRoadMap;
	}

	public void setRequirementRoadMap(List<JJRequirement> requirementRoadMap) {
		this.requirementRoadMap = requirementRoadMap;
	}

	public List<JJBug> getBugRoadMap() {
		return bugRoadMap;
	}

	public void setBugRoadMap(List<JJBug> bugRoadMap) {
		this.bugRoadMap = bugRoadMap;
	}

	@Override
	public boolean equals(Object object) {
		return (object instanceof VersionDataModelUtil)
				&& (getVersion() != null) ? getVersion().equals(
				((VersionDataModelUtil) object).getVersion())
				: (object == this);
	}

}
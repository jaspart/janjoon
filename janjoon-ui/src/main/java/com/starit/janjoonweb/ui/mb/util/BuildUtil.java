package com.starit.janjoonweb.ui.mb.util;

import java.util.List;

import com.starit.janjoonweb.domain.JJBuild;
import com.starit.janjoonweb.domain.JJBuildService;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.ui.mb.JJBuildBean;

public class BuildUtil {

	private JJVersion version;
	private List<JJBuild> versionBuilds;
	private List<JJBuild> versionSelectedBuilds;

	public BuildUtil(JJVersion version, List<JJBuild> versionBuilds) {

		this.version = version;
		this.versionBuilds = versionBuilds;
		this.versionSelectedBuilds = versionBuilds;
	}

	public JJVersion getVersion() {
		return version;
	}

	public void setVersion(JJVersion version) {
		this.version = version;
	}

	public List<JJBuild> getVersionBuilds() {
		return versionBuilds;
	}

	public void setVersionBuilds(List<JJBuild> versionBuilds) {
		this.versionBuilds = versionBuilds;
	}

	public List<JJBuild> getVersionSelectedBuilds() {
		return versionSelectedBuilds;
	}

	public void setVersionSelectedBuilds(List<JJBuild> versionSelectedBuilds) {
		this.versionSelectedBuilds = versionSelectedBuilds;
	}

	public void updateVersionBuilds(JJBuildBean bean) {
		for (JJBuild build : versionBuilds) {
			if (!versionSelectedBuilds.contains(build)) {
				build.setVersion(null);
				build.setEnabled(false);
				bean.updateJJBuild(build);
			}

		}

	}

	public static int BuildUtil(JJVersion version, List<BuildUtil> list) {
		int i = -1;
		int j = 0;
		while (j < list.size()) {
			if (list.get(j).getVersion().equals(version)) {
				i = j;
				j = list.size();
			}

			j++;
		}
		return i;
	}

	// public boolean contain(JJBuild build)
	// {
	// boolean contain=false;
	// int i=0;
	// while(i<versionSelectedBuilds.size() && !contain)
	// {
	// contain=versionSelectedBuilds.get(i).equals(build);
	// i++;
	// }
	// return contain;
	// }
}

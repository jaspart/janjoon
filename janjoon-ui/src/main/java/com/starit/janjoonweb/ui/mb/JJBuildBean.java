package com.starit.janjoonweb.ui.mb;

import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJBuild;
import com.starit.janjoonweb.domain.JJVersion;

@RooSerializable
@RooJsfManagedBean(entity = JJBuild.class, beanName = "jJBuildBean")
public class JJBuildBean {

	private JJBuild build;
	private List<JJBuild> builds;

	public JJBuild getBuild() {
		return build;
	}

	public void setBuild(JJBuild build) {
		this.build = build;
	}

	public List<JJBuild> getBuilds() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJVersionBean jJVersionBean = (JJVersionBean) session
				.getAttribute("jJVersionBean");

		JJVersion version = jJVersionBean.getVersion();

		builds = jJBuildService.getBuilds(version, true, true);

		return builds;
	}

	public void setBuilds(List<JJBuild> builds) {
		this.builds = builds;
	}

}

package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJBuild;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.ui.mb.util.BuildUtil;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJBuild.class, beanName = "jJBuildBean")
public class JJBuildBean {

	private int index;
	private String buildName;
	private JJBuild build;
	private List<JJBuild> builds;
	private List<BuildUtil> buildUtils;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getBuildName() {
		return buildName;
	}

	public void setBuildName(String buildName) {
		this.buildName = buildName;
	}

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
		builds = jJBuildService.getBuilds(((JJProductBean) session.getAttribute("jJProductBean")).getProduct(),version, true);

		return builds;
	}

	public void setBuilds(List<JJBuild> builds) {
		this.builds = builds;
	}

	public List<BuildUtil> getBuildUtils() {
		return buildUtils;
	}

	public void setBuildUtils(List<BuildUtil> buildUtils) {
		this.buildUtils = buildUtils;
	}

	public void loadingRowExpansion(JJVersion v) {

		if (buildUtils == null)
			buildUtils = new ArrayList<BuildUtil>();

		JJVersion version = null;
		
		if(v.getId() != null)
			version = jJVersionService.findJJVersion(v.getId());
		
		if(version == null)
		{
			v.setProduct(((JJProductBean) LoginBean.findBean("jJProductBean")).getProductAdmin());
			((JJVersionBean)LoginBean.findBean("jJVersionBean")).saveJJVersion(v);
			version = jJVersionService.findJJVersion(v.getId());
		}
		long l=version.getId();

		if (version == null) {
			buildUtils.add(new BuildUtil(version, null));
		} else {
			int i = BuildUtil.BuildUtil(version,
					buildUtils);
			if (i == -1)
				buildUtils.add(new BuildUtil(version, jJBuildService.getBuilds(
						version, true, true)));			
		}

		index = BuildUtil.BuildUtil(jJVersionService.findJJVersion(l),
				buildUtils);
		System.out.println(index+"/"+l);
		System.out.println("size :"+buildUtils.get(index).getVersionBuilds().size());

	}

	public void rowToggle() {

		System.err.println("rowToggle");
	}

	public void updateVersionBuilds(long id) {

		int i=BuildUtil.BuildUtil(jJVersionService.findJJVersion(id), buildUtils);
		BuildUtil buildutil=buildUtils.get(i);
		buildutil.updateVersionBuilds(this);
		buildUtils.set(i, new BuildUtil(buildutil.getVersion(), jJBuildService.getBuilds(buildutil.getVersion(), true,
						true)));
		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_updated", "Version");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		
		

	}

	public void addBuild(Long l) {

		int i = BuildUtil.BuildUtil(jJVersionService.findJJVersion(l),
				buildUtils);
		BuildUtil buildUtil = buildUtils.get(i);
		JJVersion version = buildUtil.getVersion();
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);		
		JJBuild b = new JJBuild();
		b.setName(buildName);		
		b.setEnabled(true);		
		b.setVersion(buildUtil.getVersion());
		b.setDescription("Build for Version " + version.getName());

		saveJJBuild(b);
		buildName = null;
		buildUtils.set(
				i,
				new BuildUtil(version, jJBuildService.getBuilds(version, true,
						true)));

		index=i;
		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_created", "Bug");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}

	public void updateJJBuild(JJBuild b) {
		
		JJContact contact=((LoginBean) ((HttpSession) FacesContext.getCurrentInstance().getExternalContext()
				.getSession(false)).getAttribute("loginBean")).getContact();
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJBuildService.updateJJBuild(b);
		
	}
	
	public void saveJJBuild(JJBuild b) {
		
		JJContact contact=((LoginBean) ((HttpSession) FacesContext.getCurrentInstance().getExternalContext()
				.getSession(false)).getAttribute("loginBean")).getContact();
		b.setCreatedBy(contact);
		b.setCreationDate(new Date());
		jJBuildService.saveJJBuild(b);
		
	}

}

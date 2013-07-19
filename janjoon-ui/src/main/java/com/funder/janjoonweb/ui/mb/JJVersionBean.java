package com.funder.janjoonweb.ui.mb;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.funder.janjoonweb.domain.JJProduct;
import com.funder.janjoonweb.domain.JJProject;
import com.funder.janjoonweb.domain.JJVersion;

@RooSerializable
@RooJsfManagedBean(entity = JJVersion.class, beanName = "jJVersionBean")
public class JJVersionBean {

	private JJVersion myJJVersion;
	private List<JJVersion> myJJVersionList;
	private boolean disabled = true;
	private JJProduct product;
	private JJProject project;

	public JJVersion getMyJJVersion() {
		return myJJVersion;
	}

	public void setMyJJVersion(JJVersion myJJVersion) {
		this.myJJVersion = myJJVersion;
	}

	public List<JJVersion> getMyJJVersionList() {
		myJJVersionList = jJVersionService.getAllJJVersionsWithProduct(product);
		JJVersion version = new JJVersion();
		version.setId((long) 1234567890);
		version.setName("Select All");
		myJJVersionList.add(0, version);
		return myJJVersionList;
	}

	public void setMyJJVersionList(List<JJVersion> myJJVersionList) {
		this.myJJVersionList = myJJVersionList;
	}

	public boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public JJProduct getProduct() {
		return product;
	}

	public void setProduct(JJProduct product) {
		this.product = product;
	}

	public JJProject getProject() {
		return project;
	}

	public void setProject(JJProject project) {
		this.project = project;
	}

	public void handleSelect() {
		System.out.println("HEEEEERRREE");
		if (myJJVersion != null) {
			System.out.println("myJJVersion.getId() " + myJJVersion.getId());
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Version selected: " + myJJVersion.getName(),
					"Selection info");

			FacesContext.getCurrentInstance().addMessage(null, message);

			// Requete getReqwithProject&Product&Version
		} else {
			
			// Requete getReqwithProject&Product
			System.out.println("My version is null");
		}

	}
}

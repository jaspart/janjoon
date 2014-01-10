package com.funder.janjoonweb.ui.mb;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.funder.janjoonweb.domain.JJProduct;
import com.funder.janjoonweb.domain.JJProject;
import com.funder.janjoonweb.domain.JJRequirementService;
import com.funder.janjoonweb.domain.JJVersion;

@RooSerializable
@RooJsfManagedBean(entity = JJVersion.class, beanName = "jJVersionBean")
public class JJVersionBean {

	private JJVersion version;
	private List<JJVersion> versionList;
	private boolean disabled = true;
	private JJProduct product;
	private JJProject project;

	@Autowired
	JJRequirementService jJRequirementService;

	public void setjJRequirementService(JJRequirementService jJRequirementService) {
		this.jJRequirementService = jJRequirementService;
	}

	public JJVersion getVersion() {
		return version;
	}

	public void setVersion(JJVersion version) {
		this.version = version;
	}

	public List<JJVersion> getVersionList() {
		versionList = jJVersionService.getAllJJVersionsWithProduct(product);
		JJVersion version = new JJVersion();
		version.setId((long) 1234567890);
		version.setName("Select All");
		versionList.add(0, version);
		return versionList;
	}

	public void setVersionList(List<JJVersion> versionList) {
		this.versionList = versionList;
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

	public void handleSelect(JJRequirementBean jJRequirementBean) {
		if (version != null) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Version selected: " + version.getName(), "Selection info");

			FacesContext.getCurrentInstance().addMessage(null, message);

			if (jJRequirementBean != null) {
				jJRequirementBean.setVersion(version);

				jJRequirementBean.setMyBusinessJJRequirements(jJRequirementService
						.getAllJJRequirementsWithProjectAndProductAndVersion("BUSINESS", project, product, version));

				jJRequirementBean.setMyFunctionalJJRequirements(jJRequirementService
						.getAllJJRequirementsWithProjectAndProductAndVersion("FUNCTIONAL", project, product, version));

				jJRequirementBean.setMyTechnicalJJRequirements(jJRequirementService
						.getAllJJRequirementsWithProjectAndProductAndVersion("TECHNICAL", project, product, version));
			}
		} else {
			// IF VERSION IS NULL GET ALL JJREQUIRMENTS WITH PROJECT AND PRODUCT
			jJRequirementBean.setVersion(version);

			jJRequirementBean.setMyBusinessJJRequirements(jJRequirementService
					.getAllJJRequirementsWithProjectAndProduct("BUSINESS", project, product));

			jJRequirementBean.setMyFunctionalJJRequirements(jJRequirementService
					.getAllJJRequirementsWithProjectAndProduct("FUNCTIONAL", project, product));

			jJRequirementBean.setMyTechnicalJJRequirements(jJRequirementService
					.getAllJJRequirementsWithProjectAndProduct("TECHNICAL",	project, product));
		}
	}
}

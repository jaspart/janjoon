package com.funder.janjoonweb.ui.mb;

import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.ListDataModel;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.SelectableDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.funder.janjoonweb.domain.JJProduct;
import com.funder.janjoonweb.domain.JJProject;
import com.funder.janjoonweb.domain.JJRequirementService;
import com.funder.janjoonweb.domain.JJVersion;
import com.funder.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJVersion.class, beanName = "jJVersionBean")
public class JJVersionBean {

	@Autowired
	JJRequirementService jJRequirementService;

	public void setjJRequirementService(
			JJRequirementService jJRequirementService) {
		this.jJRequirementService = jJRequirementService;
	}

	private JJVersion version;
	private List<JJVersion> versionList;

	private JJVersion versionAdmin;
	private List<JJVersion> versionListTable;

	private VersionDataModel versionDataModel;

	private boolean disabled = true;

	private JJProduct product;
	private JJProject project;

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

	public JJVersion getVersionAdmin() {
		return versionAdmin;
	}

	public void setVersionAdmin(JJVersion versionAdmin) {
		this.versionAdmin = versionAdmin;
	}

	public List<JJVersion> getVersionListTable() {
		return versionListTable;
	}

	public void setVersionListTable(List<JJVersion> versionListTable) {
		this.versionListTable = versionListTable;
	}

	public VersionDataModel getVersionDataModel() {
		versionDataModel = new VersionDataModel(
				jJVersionService.getAllJJVersionWithoutProduct());
		return versionDataModel;
	}

	public void setVersionDataModel(VersionDataModel versionDataModel) {
		this.versionDataModel = versionDataModel;
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
			// FacesMessage message = new
			// FacesMessage(FacesMessage.SEVERITY_INFO,
			// "Version selected: " + version.getName(), "Selection info");
			//
			// FacesContext.getCurrentInstance().addMessage(null, message);

			if (jJRequirementBean != null) {
				jJRequirementBean.setVersion(version);

				jJRequirementBean
						.setMyBusinessJJRequirements(jJRequirementService
								.getAllJJRequirementsWithProjectAndProductAndVersion(
										"BUSINESS", project, product, version));

				jJRequirementBean
						.setMyFunctionalJJRequirements(jJRequirementService
								.getAllJJRequirementsWithProjectAndProductAndVersion(
										"FUNCTIONAL", project, product, version));

				jJRequirementBean
						.setMyTechnicalJJRequirements(jJRequirementService
								.getAllJJRequirementsWithProjectAndProductAndVersion(
										"TECHNICAL", project, product, version));
			}
		} else {
			// IF VERSION IS NULL GET ALL JJREQUIRMENTS WITH PROJECT AND PRODUCT
			jJRequirementBean.setVersion(version);

			jJRequirementBean.setMyBusinessJJRequirements(jJRequirementService
					.getAllJJRequirementsWithProjectAndProduct("BUSINESS",
							project, product));

			jJRequirementBean
					.setMyFunctionalJJRequirements(jJRequirementService
							.getAllJJRequirementsWithProjectAndProduct(
									"FUNCTIONAL", project, product));

			jJRequirementBean.setMyTechnicalJJRequirements(jJRequirementService
					.getAllJJRequirementsWithProjectAndProduct("TECHNICAL",
							project, product));
		}
	}

	public void newVersion() {
		System.out.println("Initial bean version");

		versionAdmin = new JJVersion();
		versionAdmin.setEnabled(true);
		versionAdmin.setCreationDate(new Date());
		
		versionListTable = null;

	}

	public void save() {
		System.out.println("SAVING Version...");
		String message = "";

		if (versionAdmin.getId() == null) {
			System.out.println("IS a new JJVersion");
			versionAdmin.setDescription("This is version "
					+ versionAdmin.getName());
			jJVersionService.saveJJVersion(versionAdmin);
			message = "message_successfully_created";

			newVersion();

		} else {
			jJVersionService.updateJJVersion(versionAdmin);
			message = "message_successfully_updated";
		}

		FacesMessage facesMessage = MessageFactory.getMessage(message,
				"JJVersion");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}
	
	public void onRowSelect(SelectEvent event) {

		System.out.println("Row selected");

		if(versionListTable !=null){
			System.out.println("la selection est non null");
		}

	}

	private class VersionDataModel extends ListDataModel<JJVersion> implements
			SelectableDataModel<JJVersion> {

		public VersionDataModel() {
		}

		public VersionDataModel(List<JJVersion> data) {
			super(data);
		}

		@Override
		public JJVersion getRowData(String rowKey) {
			// In a real app, a more efficient way like a query by rowKey should
			// be implemented to deal with huge data

			List<JJVersion> versions = (List<JJVersion>) getWrappedData();

			for (JJVersion version : versions) {
				if (version.getName().equals(rowKey))
					return version;
			}

			return null;
		}

		@Override
		public Object getRowKey(JJVersion version) {
			return version.getName();
		}
	}

}

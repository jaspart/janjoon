package com.starit.janjoonweb.ui.mb;

import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.model.ListDataModel;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.SelectableDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJRequirementService;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.ui.util.MessageFactory;

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

	private JJProduct product;

	private JJVersion versionAdmin;
	private List<JJVersion> versionListTable;

	private VersionDataModel versionDataModel;

	private boolean disabled = true;

	public JJVersion getVersion() {
		
		return version;
	}

	public void setVersion(JJVersion version) {
		this.version = version;
	}

	public List<JJVersion> getVersionList() {

		versionList = jJVersionService.getVersions(true, true, product);
		
		return versionList;
	}

	public void setVersionList(List<JJVersion> versionList) {
		this.versionList = versionList;
	}

	public JJProduct getProduct() {
		return product;
	}

	public void setProduct(JJProduct product) {
		this.product = product;
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
		versionDataModel = new VersionDataModel(jJVersionService.getVersions(
				true, false, null));
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

	public void handleSelectVersion(JJRequirementBean jJRequirementBean,
			JJChapterBean jJChapterBean) {
		
		jJRequirementBean.setVersion(version);
		jJChapterBean.setVersion(version);	
	}
	
	public List<JJTask> getTastksByVersion(JJVersion jJversion)
	{
		return jJVersionService.getTastksByVersion(jJversion);
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

		if (versionListTable != null) {
			System.out.println("la selection est non null");
		}

	}

	@SuppressWarnings("unchecked")
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

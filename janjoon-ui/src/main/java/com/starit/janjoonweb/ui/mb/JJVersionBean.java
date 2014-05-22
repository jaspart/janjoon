package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJRequirementService;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

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
	private boolean disabled = true;

	private JJVersion versionAdmin;
	private List<VersionDataModel> versionDataModel;

	private boolean checkVersion;
	private boolean checkVersions;
	private boolean disabledCheckVersion;

	public JJVersion getVersion() {

		return version;
	}

	public void setVersion(JJVersion version) {

		// HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
		// .getExternalContext().getSession(false);
		// JJBuildBean jJBuildBean = (JJBuildBean) session
		// .getAttribute("jJBuildBean");
		// jJBuildBean.setBuild(null);
		// jJBuildBean.getBuildList();

		this.version = version;
	}

	public List<JJVersion> getVersionList() {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJProductBean jJProductBean = (JJProductBean) session
				.getAttribute("jJProductBean");
		JJProduct product = jJProductBean.getProduct();

		versionList = jJVersionService.getVersions(true, true, product);

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

	public JJVersion getVersionAdmin() {
		return versionAdmin;
	}

	public void setVersionAdmin(JJVersion versionAdmin) {
		this.versionAdmin = versionAdmin;
	}

	public List<VersionDataModel> getVersionDataModel() {
		return versionDataModel;
	}

	public void setVersionDataModel(List<VersionDataModel> versionDataModel) {
		this.versionDataModel = versionDataModel;
	}

	public boolean getCheckVersion() {
		return checkVersion;
	}

	public void setCheckVersion(boolean checkVersion) {
		this.checkVersion = checkVersion;
	}

	public boolean getCheckVersions() {
		return checkVersions;
	}

	public void setCheckVersions(boolean checkVersions) {
		this.checkVersions = checkVersions;
	}

	public boolean getDisabledCheckVersion() {
		return disabledCheckVersion;
	}

	public void setDisabledCheckVersion(boolean disabledCheckVersion) {
		this.disabledCheckVersion = disabledCheckVersion;
	}

	public List<JJTask> getTastksByVersion(JJVersion jJversion) {
		return jJVersionService.getTastksByVersion(jJversion);
	}

	public void newVersion() {

		versionAdmin = new JJVersion();
		versionAdmin.setEnabled(true);
		versionAdmin.setCreationDate(new Date());
	}

	public void addVersion() {

		if (versionDataModel == null) {
			versionDataModel = new ArrayList<VersionDataModel>();
		}

		versionAdmin.setDescription("This is version "
				+ versionAdmin.getName());
		
		versionDataModel.add(new VersionDataModel(versionAdmin, true, false));
		newVersion();
	}

	public void save() {

		String message = "";

		if (versionAdmin.getId() == null) {
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

	public void fillVersionTable(JJProduct product) {
		versionDataModel = new ArrayList<VersionDataModel>();

		List<JJVersion> versions = jJVersionService.getVersions(true, true,
				product);

		for (JJVersion version : versions) {
			versionDataModel.add(new VersionDataModel(version, true, true));
		}

		checkVersions = true;
	}

	public void checkVersions() {

		for (VersionDataModel versionModel : versionDataModel) {
			versionModel.setCheckVersion(checkVersions);
		}

	}

	public void checkVersion() {

		boolean checkAll = true;
		for (VersionDataModel versionModel : versionDataModel) {

			if (!versionModel.getCheckVersion()) {
				checkAll = false;
				break;
			}

		}
		checkVersions = checkAll;
	}

	public class VersionDataModel {

		private JJVersion version;
		private boolean checkVersion;
		private boolean old;

		public VersionDataModel(JJVersion version, boolean checkVersion,
				boolean old) {
			super();
			this.version = version;
			this.checkVersion = checkVersion;
			this.old = old;
		}

		public JJVersion getVersion() {
			return version;
		}

		public void setVersion(JJVersion version) {
			this.version = version;
		}

		public boolean getCheckVersion() {
			return checkVersion;
		}

		public void setCheckVersion(boolean checkVersion) {
			this.checkVersion = checkVersion;
		}

		public boolean getOld() {
			return old;
		}

		public void setOld(boolean old) {
			this.old = old;
		}

	}

}

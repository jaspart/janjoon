package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJCompany;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJRequirementService;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.domain.JJVersionService;

@RooSerializable
@RooJsfManagedBean(entity = JJVersion.class, beanName = "jJVersionBean")
public class JJVersionBean {

	@Autowired
	JJRequirementService jJRequirementService;

	public void setjJRequirementService(
			JJRequirementService jJRequirementService) {
		this.jJRequirementService = jJRequirementService;
	}

	
	public JJVersionService getJJVersionService()
	{
		return jJVersionService;
	}
	private JJProduct product;
	private JJVersion version;
	private List<JJVersion> versionList;
	private boolean disabled = true;

	private JJVersion versionAdmin;
	private List<VersionDataModel> versionDataModel;

	private boolean checkVersion;
	private boolean checkVersions;
	private boolean disabledCheckVersion;

	public JJProduct getProduct() {
		return product;
	}

	public void setProduct(JJProduct product) {
		this.product = product;
	}

	public JJVersion getVersion() {

		return version;
	}

	public void setVersion(JJVersion version) {

		this.version = version;
	}

	public List<JJVersion> getVersionList() {

		
		JJProduct jJproduct = LoginBean.getProduct();
		if (((LoginBean) LoginBean.findBean("loginBean")).getContact() != null) {
			if (product == null) {
				product = jJproduct;
				versionList = jJVersionService.getVersions(true, true, product,
						((LoginBean) LoginBean.findBean("loginBean"))
								.getContact().getCompany(),true);

			} else if (!product.equals(jJproduct)) {
				product = jJproduct;
				versionList = jJVersionService.getVersions(true, true, product,
						((LoginBean) LoginBean.findBean("loginBean"))
								.getContact().getCompany(),true);
			}
		}

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

	public void newVersion() {

		versionAdmin = new JJVersion();
		versionAdmin.setEnabled(true);
	}

	public void addVersion() {

		if (versionDataModel == null) {
			versionDataModel = new ArrayList<VersionDataModel>();
		}

		versionAdmin
				.setDescription("This is version " + versionAdmin.getName());

		versionDataModel.add(new VersionDataModel(versionAdmin, true, false));
		newVersion();
	}

	public void fillVersionTable(JJProduct product) {
		versionDataModel = new ArrayList<VersionDataModel>();

		List<JJVersion> versions = jJVersionService.getVersions(true, true,
				product, ((LoginBean) LoginBean.findBean("loginBean"))
						.getContact().getCompany(),true);

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

	public boolean versionNameExist(String name, JJProduct prod) {
		return jJVersionService.getVersionByName(name, prod) != null;
	}

	public boolean saveJJVersion(JJVersion b) {
		
		if(!versionNameExist(b.getName(), b.getProduct()))
		{
			JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
					.getContact();
			b.setCreatedBy(contact);
			b.setCreationDate(new Date());
			jJVersionService.saveJJVersion(b);
			return true;
		}else
			return false;
		
	}

	public boolean updateJJVersion(JJVersion b) {
		
		JJVersion v=jJVersionService.getVersionByName(b.getName(), b.getProduct());
		if(v != null)
		{
			if(v.getId().equals(b.getId()))
			{
				JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
						.getContact();
				b.setUpdatedBy(contact);
				b.setUpdatedDate(new Date());
				jJVersionService.updateJJVersion(b);
				return true;
			}else
				return false;
		}else 
		{
			JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
					.getContact();
			b.setUpdatedBy(contact);
			b.setUpdatedDate(new Date());
			jJVersionService.updateJJVersion(b);
			return true;
		}
		
	}

	public List<JJTask> getTastksByVersion(JJVersion jJversion) {
		return jJVersionService.getTastksByVersion(jJversion);
	}
}

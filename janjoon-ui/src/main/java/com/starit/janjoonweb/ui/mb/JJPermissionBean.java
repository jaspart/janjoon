package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJPermission;
import com.starit.janjoonweb.domain.JJPermissionService;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProfile;
import com.starit.janjoonweb.domain.JJProject;

@RooSerializable
@RooJsfManagedBean(entity = JJPermission.class, beanName = "jJPermissionBean")
public class JJPermissionBean {

	private JJPermission permissionAdmin;

	private List<PermissionDataModel> permissionDataModel;

	private JJProfile profile;
	private List<JJProfile> profiles;

	private JJProject project;
	private List<JJProject> projects;

	private JJProduct product;
	private List<JJProduct> products;

	private boolean checkPermission;
	private boolean checkPermissions;
	private boolean disabledCheckPermission;

	public JJPermissionService getJJPermissionService()
	{
		return this.jJPermissionService;
	}

	public JJPermission getPermissionAdmin() {
		return permissionAdmin;
	}

	public void setPermissionAdmin(JJPermission permissionAdmin) {
		this.permissionAdmin = permissionAdmin;
	}

	public List<PermissionDataModel> getPermissionDataModel() {
		return permissionDataModel;
	}

	public void setPermissionDataModel(
			List<PermissionDataModel> permissionDataModel) {
		this.permissionDataModel = permissionDataModel;
	}

	public JJProfile getProfile() {
		return profile;
	}

	public void setProfile(JJProfile profile) {
		this.profile = profile;
	}

	public List<JJProfile> getProfiles() {
		profiles = jJProfileService.getProfiles(true);
		return profiles;
	}

	public void setProfiles(List<JJProfile> profiles) {
		this.profiles = profiles;
	}

	public JJProject getProject() {
		return project;
	}

	public void setProject(JJProject project) {
		this.project = project;
	}

	public List<JJProject> getProjects() {
		projects = jJProjectService.getProjects(true);
		return projects;
	}

	public void setProjects(List<JJProject> projects) {
		this.projects = projects;
	}

	public JJProduct getProduct() {
		return product;
	}

	public void setProduct(JJProduct product) {
		this.product = product;
	}

	public List<JJProduct> getProducts() {
		products = jJProductService.getProducts(true);
		return products;
	}

	public void setProducts(List<JJProduct> products) {
		this.products = products;
	}

	public boolean getCheckPermission() {
		return checkPermission;
	}

	public void setCheckPermission(boolean checkPermission) {
		this.checkPermission = checkPermission;
	}

	public boolean getCheckPermissions() {
		return checkPermissions;
	}

	public void setCheckPermissions(boolean checkPermissions) {
		this.checkPermissions = checkPermissions;
	}

	public boolean getDisabledCheckPermission() {
		return disabledCheckPermission;
	}

	public void setDisabledCheckPermission(boolean disabledCheckPermission) {
		this.disabledCheckPermission = disabledCheckPermission;
	}

	public void newPermission() {

		permissionAdmin = new JJPermission();
		permissionAdmin.setEnabled(true);

		profile = null;
		project = null;
		product = null;
	}

	public void addPermission() {

		permissionAdmin.setProfile(profile);
		permissionAdmin.setProject(project);
		permissionAdmin.setProduct(product);

		boolean valide = true;

		if (permissionDataModel == null) {
			permissionDataModel = new ArrayList<PermissionDataModel>();
		} else {
			System.out.println("gfggfdfgdfg");
			for (PermissionDataModel permissionData : permissionDataModel) {

				if ((permissionData.getProfile().getId()
						.equals(profile.getId()))
						&& (((permissionData.getProject() == null) && (project == null)) || (((permissionData
								.getProject() != null) && (project != null)) && permissionData
								.getProject().getId().equals(project.getId())))
						&& (((permissionData.getProduct() == null) && (product == null)) || (((permissionData
								.getProduct() != null) && (product != null)) && permissionData
								.getProduct().getId().equals(product.getId())))) {
					valide = false;

					break;
				}
			}
		}

		if (valide) {
			permissionDataModel.add(new PermissionDataModel(permissionAdmin,
					profile, project, product, true, false));
			newPermission();
		} else {

			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"This permission is already attributed to this contact",
									"Permission"));
		}

	}
	public void fillPermissionTable(JJContact contact) {
		permissionDataModel = new ArrayList<PermissionDataModel>();

		List<JJPermission> permissions = jJPermissionService.getPermissions(
				contact, true, null, null, null);		

		for (JJPermission permission : permissions) {
			permissionDataModel.add(new PermissionDataModel(permission,
					permission.getProfile(), permission.getProject(),
					permission.getProduct(), true, true));
		}

		checkPermissions = true;
	}

	public void checkPermissions() {

		for (PermissionDataModel permissionModel : permissionDataModel) {
			permissionModel.setCheckPermission(checkPermissions);
		}

	}

	public void checkPermission() {

		boolean checkAll = true;
		for (PermissionDataModel permissionModel : permissionDataModel) {

			if (!permissionModel.getCheckPermission()) {
				checkAll = false;
				break;
			}

		}
		checkPermissions = checkAll;
	}

	public class PermissionDataModel {

		private JJPermission permission;
		private JJProfile profile;
		private JJProject project;
		private JJProduct product;
		private boolean checkPermission;
		private boolean old;

		public PermissionDataModel(JJPermission permission, JJProfile profile,
				JJProject project, JJProduct product, boolean checkPermission,
				boolean old) {
			super();
			this.permission = permission;
			this.profile = profile;
			this.project = project;
			this.product = product;
			this.checkPermission = checkPermission;
			this.old = old;
		}

		public JJPermission getPermission() {
			return permission;
		}

		public void setPermission(JJPermission permission) {
			this.permission = permission;
		}

		public JJProfile getProfile() {
			return profile;
		}

		public void setProfile(JJProfile profile) {
			this.profile = profile;
		}

		public JJProject getProject() {
			return project;
		}

		public void setProject(JJProject project) {
			this.project = project;
		}

		public JJProduct getProduct() {
			return product;
		}

		public void setProduct(JJProduct product) {
			this.product = product;
		}

		public boolean getCheckPermission() {
			return checkPermission;
		}

		public void setCheckPermission(boolean checkPermission) {
			this.checkPermission = checkPermission;
		}

		public boolean getOld() {
			return old;
		}

		public void setOld(boolean old) {
			this.old = old;
		}

	}

}

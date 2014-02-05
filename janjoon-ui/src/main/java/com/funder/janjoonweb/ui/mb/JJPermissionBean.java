package com.funder.janjoonweb.ui.mb;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.funder.janjoonweb.domain.JJContact;
import com.funder.janjoonweb.domain.JJPermission;
import com.funder.janjoonweb.domain.JJProduct;
import com.funder.janjoonweb.domain.JJProfile;
import com.funder.janjoonweb.domain.JJProject;
import com.funder.janjoonweb.ui.mb.util.MessageFactory;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

@RooSerializable
@RooJsfManagedBean(entity = JJPermission.class, beanName = "jJPermissionBean")
public class JJPermissionBean {

	private JJPermission permissionAdmin;
	private List<JJPermission> permisssionListTable;

	private JJProfile profile;
	private List<JJProfile> profileList;

	private JJProject project;
	private List<JJProject> projectList;

	private JJProduct product;
	private List<JJProduct> productList;

	private JJContact contact;

	public JJPermission getPermissionAdmin() {
		return permissionAdmin;
	}

	public void setPermissionAdmin(JJPermission permissionAdmin) {
		this.permissionAdmin = permissionAdmin;
	}

	public List<JJPermission> getPermisssionListTable() {
		if(contact!=null){
		permisssionListTable = jJPermissionService
				.getJJPermissionsWithContact(contact);
		}else
			permisssionListTable = null;
		return permisssionListTable;
	}

	public void setPermisssionListTable(List<JJPermission> permisssionListTable) {
		this.permisssionListTable = permisssionListTable;
	}

	public JJProfile getProfile() {
		return profile;
	}

	public void setProfile(JJProfile profile) {
		this.profile = profile;
	}

	public List<JJProfile> getProfileList() {
		profileList = jJProfileService.findAllJJProfiles();
		return profileList;
	}

	public void setProfileList(List<JJProfile> profileList) {
		this.profileList = profileList;
	}

	public JJProject getProject() {
		return project;
	}

	public void setProject(JJProject project) {
		this.project = project;
	}

	public List<JJProject> getProjectList() {
		projectList = jJProjectService.getAllJJProjects();
		return projectList;
	}

	public void setProjectList(List<JJProject> projectList) {
		this.projectList = projectList;
	}

	public JJProduct getProduct() {
		return product;
	}

	public void setProduct(JJProduct product) {
		this.product = product;
	}

	public List<JJProduct> getProductList() {
		productList = jJProductService.getAllJJProducts();
		return productList;
	}

	public void setProductList(List<JJProduct> productList) {
		this.productList = productList;
	}

	public JJContact getContact() {
		return contact;
	}

	public void setContact(JJContact contact) {
		this.contact = contact;
	}

	public void newPermission() {
		System.out.println("Initial bean permission");
		permissionAdmin = new JJPermission();
		profile = null;
		project = null;
		product = null;
	}

	public void save() {
		System.out.println("SAVING Permission...");
		String message = "";

		if (permissionAdmin.getId() == null) {
			System.out.println("IS a new JJPermission");
			permissionAdmin.setProfile(profile);
			permissionAdmin.setProject(project);
			permissionAdmin.setProduct(product);
			permissionAdmin.setContact(contact);

			// if(jJPermissionService.) Requete pour vérifier si ce permission.profile and permission.contact existe dans la base

			jJPermissionService.saveJJPermission(permissionAdmin);

			message = "message_successfully_created";

			newPermission();

		} else {
			jJPermissionService.updateJJPermission(permissionAdmin);

			message = "message_successfully_updated";
		}

		FacesMessage facesMessage = MessageFactory.getMessage(message,
				"JJPermission");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}
}

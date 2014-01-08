package com.funder.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.funder.janjoonweb.domain.JJContact;
import com.funder.janjoonweb.domain.JJPermission;
import com.funder.janjoonweb.domain.JJPermissionService;
import com.funder.janjoonweb.domain.JJProduct;
import com.funder.janjoonweb.domain.JJProductService;
import com.funder.janjoonweb.domain.JJProfile;
import com.funder.janjoonweb.domain.JJProfileService;
import com.funder.janjoonweb.domain.JJProject;
import com.funder.janjoonweb.domain.JJProjectService;
import com.funder.janjoonweb.domain.JJRequirement;
import com.funder.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJContact.class, beanName = "jJContactBean")
public class JJContactBean {

	private JJContact contact;
	private List<JJContact> contactList;
	private JJProfile selectedProfile;
	private List<JJProfile> profileList;
	private JJProject selectedProject;
	private List<JJProject> projectList;
	private JJProduct selectedProduct;
	private List<JJProduct> productList;
	private List<JJPermission> permissionList;
	private String message;

	@Autowired
	JJProfileService jJProfileService;

	public void setjJProfileService(JJProfileService jJProfileService) {
		this.jJProfileService = jJProfileService;
	}

	@Autowired
	JJProjectService jJProjectService;

	public void setjJProjectService(JJProjectService jJProjectService) {
		this.jJProjectService = jJProjectService;
	}

	@Autowired
	JJProductService jJProductService;

	public void setjJProductService(JJProductService jJProductService) {
		this.jJProductService = jJProductService;
	}

	@Autowired
	JJPermissionService jJPermissionService;

	public void setjJPermissionService(JJPermissionService jJPermissionService) {
		this.jJPermissionService = jJPermissionService;
	}

	public JJContact getContact() {
		return contact;
	}

	public void setContact(JJContact contact) {
		this.contact = contact;
	}

	public List<JJContact> getContactList() {
		contactList = jJContactService.getAllJJContact();
		return contactList;
	}

	public void setContactList(List<JJContact> contactList) {
		this.contactList = contactList;
	}

	public JJProfile getSelectedProfile() {
		return selectedProfile;
	}

	public void setSelectedProfile(JJProfile selectedProfile) {
		this.selectedProfile = selectedProfile;
	}

	public List<JJProfile> getProfileList() {
		profileList = jJProfileService.findAllJJProfiles();
		return profileList;
	}

	public void setProfileList(List<JJProfile> profileList) {
		this.profileList = profileList;
	}

	public JJProject getSelectedProject() {
		return selectedProject;
	}

	public void setSelectedProject(JJProject selectedProject) {
		this.selectedProject = selectedProject;
	}

	public List<JJProject> getProjectList() {
		projectList = jJProjectService.getAllJJProjects();
		return projectList;
	}

	public void setProjectList(List<JJProject> projectList) {
		this.projectList = projectList;
	}

	public JJProduct getSelectedProduct() {
		return selectedProduct;
	}

	public void setSelectedProduct(JJProduct selectedProduct) {
		this.selectedProduct = selectedProduct;
	}

	public List<JJProduct> getProductList() {
		productList = jJProductService.getAllJJProducts();
		return productList;
	}

	public void setProductList(List<JJProduct> productList) {
		this.productList = productList;
	}

	public List<JJPermission> getPermissionList() {
		// permissionList = jJPermissionService.findAllJJPermissions();
		return permissionList;
	}

	public void setPermissionList(List<JJPermission> permissionList) {
		this.permissionList = permissionList;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void newContact() {
		message = "New User";
		contact = new JJContact();
		contact.setEnabled(true);
		contact.setCreationDate(new Date());
		contact.setDescription("Defined as a Contact");
		contact.setName("TEMP Value");
		permissionList = new ArrayList<JJPermission>();
		selectedProfile = null;
		selectedProject = null;
		selectedProduct = null;
	}

	public void editContact() {
		message = "Edit User";
	}

	public void save() {
		System.out.println("SAVING ...");
		String message = "";

		if (contact.getId() == null) {
			System.out.println("IS a new Contact");
			jJContactService.saveJJContact(contact);
			message = "message_successfully_created";

		} else {
			jJContactService.updateJJContact(contact);
			message = "message_successfully_updated";
		}

		if (permissionList.size() > 0) {
			System.out
					.println("permissionList.size() " + permissionList.size());
			contact = jJContactService.findJJContact(contact.getId());
			Set<JJPermission> permissions = new HashSet<JJPermission>();
			for (JJPermission permission : permissionList) {
				permission.setContact(contact);
				permissions.add(permission);
				jJPermissionService.saveJJPermission(permission);
			}

			contact.setPermissions(permissions);
			jJContactService.updateJJContact(contact);
		}

		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("contactDialogWidget.hide()");

		FacesMessage facesMessage = MessageFactory.getMessage(message,
				"JJContact");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}

	public void addMessage() {
		String summary = contact.getEnabled() ? "Active Contact"
				: "Inactive Contact";

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(summary));
	}

	public void dialogClose(CloseEvent event) {
		newContact();
	}

	public void addPermission() {
	
		JJPermission permission = new JJPermission();
		permission.setProfile(selectedProfile);

		if (selectedProject != null)
			permission.setProject(selectedProject);
		if (selectedProduct != null)
			permission.setProduct(selectedProduct);

//		jJPermissionService.saveJJPermission(permission);
//
//		System.out.println("JJPermission sauvé");
//		
//		permission = jJPermissionService.findJJPermission(permission.getId());
		permissionList.add(permission);

		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_created", "JJPermission");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}

}

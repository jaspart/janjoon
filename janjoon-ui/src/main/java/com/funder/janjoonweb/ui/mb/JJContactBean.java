package com.funder.janjoonweb.ui.mb;

import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.funder.janjoonweb.domain.JJContact;
import com.funder.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJContact.class, beanName = "jJContactBean")
public class JJContactBean {

	private JJContact contactAdmin;
	private List<JJContact> contactListTable;

	private String message;
	private boolean disabled;

	public JJContact getContactAdmin() {
		return contactAdmin;
	}

	public void setContactAdmin(JJContact contactAdmin) {
		this.contactAdmin = contactAdmin;
	}

	public List<JJContact> getContactListTable() {
		contactListTable = jJContactService.getAllJJContact();
		return contactListTable;
	}

	public void setContactListTable(List<JJContact> contactListTable) {
		this.contactListTable = contactListTable;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public void newContact(JJPermissionBean jJPermissionBean) {
		System.out.println("Initial bean contact");
		message = "New Contact";
		disabled = false;
		contactAdmin = new JJContact();
		contactAdmin.setEnabled(true);
		contactAdmin.setCreationDate(new Date());
		jJPermissionBean.setContact(contactAdmin);
		jJPermissionBean.newPermission();
	}

	public void editContact() {
		message = "Edit Contact";
	}

	public void save(JJPermissionBean jJPermissionBean) {
		System.out.println("SAVING Contact...");
		String message = "";

		if (contactAdmin.getId() == null) {
			System.out.println("IS a new JJContact");
			contactAdmin.setDescription("This contact is "
					+ contactAdmin.getFirstname() + " "
					+ contactAdmin.getName());

			jJContactService.saveJJContact(contactAdmin);

			message = "message_successfully_created";

			JJContact contact = jJContactService.findJJContact(contactAdmin
					.getId());

			jJPermissionBean.setContact(contact);

			disabled = true;

		} else {
			jJContactService.updateJJContact(contactAdmin);
			message = "message_successfully_updated";
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("contactDialogWidget.hide()");
		}

		FacesMessage facesMessage = MessageFactory.getMessage(message,
				"JJContact");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}

	public void addMessage() {
		String summary = contactAdmin.getEnabled() ? "Active Contact"
				: "Inactive Contact";

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(summary));
	}

	public void closeDialog(JJPermissionBean jJPermissionBean) {
		System.out.println("close dialog");
		contactAdmin = null;
		disabled = false;
		jJPermissionBean.setPermissionAdmin(null);
		jJPermissionBean.setPermisssionListTable(null);
		jJPermissionBean.setProfile(null);
		jJPermissionBean.setProfileList(null);
		jJPermissionBean.setProject(null);
		jJPermissionBean.setProjectList(null);
		jJPermissionBean.setProduct(null);
		jJPermissionBean.setProductList(null);
		jJPermissionBean.setContact(null);

	}
}

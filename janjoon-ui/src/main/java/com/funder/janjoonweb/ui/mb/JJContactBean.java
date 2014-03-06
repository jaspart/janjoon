package com.funder.janjoonweb.ui.mb;

import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.ListDataModel;

import org.primefaces.context.RequestContext;
import org.primefaces.model.SelectableDataModel;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.funder.janjoonweb.domain.JJContact;
import com.funder.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJContact.class, beanName = "jJContactBean")
public class JJContactBean {

	private JJContact contactAdmin;
	private List<JJContact> contactDataTable;
	private ContactDataModel contactDataModel;

	private String message;
	private boolean disabled;

	public JJContact getContactAdmin() {
		return contactAdmin;
	}

	public void setContactAdmin(JJContact contactAdmin) {
		this.contactAdmin = contactAdmin;
	}

	public List<JJContact> getContactDataTable() {
		contactDataTable = jJContactService.getAllJJContact();
		return contactDataTable;
	}

	public void setContactDataTable(List<JJContact> contactDataTable) {
		this.contactDataTable = contactDataTable;
	}

	public ContactDataModel getContactDataModel() {
		contactDataModel = new ContactDataModel(
				jJContactService.getAllJJContact());
		return contactDataModel;
	}

	public void setContactDataModel(ContactDataModel contactDataModel) {
		this.contactDataModel = contactDataModel;
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

	public void deleteContact() {
		// message = "Edit Contact";

		if (contactAdmin != null) {
			System.out.println(contactAdmin.getName());

			contactAdmin.setEnabled(false);
			jJContactService.updateJJContact(contactAdmin);

		}
	}
	
	public JJContact getContactByEmail(String email)
	{
		return jJContactService.getJJContactWithEmail(email);
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

	private class ContactDataModel extends ListDataModel<JJContact> implements
			SelectableDataModel<JJContact> {

		public ContactDataModel(List<JJContact> data) {
			super(data);
		}

		@Override
		public JJContact getRowData(String rowKey) {
			// In a real app, a more efficient way like a query by rowKey should
			// be implemented to deal with huge data

			List<JJContact> contacts = (List<JJContact>) getWrappedData();

			for (JJContact contact : contacts) {
				if (contact.getName().equals(rowKey))
					return contact;
			}

			return null;
		}

		@Override
		public Object getRowKey(JJContact contact) {
			return contact.getName();
		}
	}

}

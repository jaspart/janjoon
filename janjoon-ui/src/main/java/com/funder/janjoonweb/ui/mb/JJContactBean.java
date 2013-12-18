package com.funder.janjoonweb.ui.mb;

import java.util.Date;
import java.util.List;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.funder.janjoonweb.domain.JJContact;

@RooSerializable
@RooJsfManagedBean(entity = JJContact.class, beanName = "jJContactBean")
public class JJContactBean {

	private JJContact contact;

	private List<JJContact> contactList;

	// @Email(message = "must be a valid email")
	// private String email;

	private String message;

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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	// public String getEmail() {
	// return email;
	// }
	//
	// public void setEmail(String email) {
	// this.email = email;
	// }

	public void newContact() {
		message = "New User";
		contact = new JJContact();
		contact.setEnabled(true);
		contact.setCreationDate(new Date());
		contact.setDescription("Defined as a Contact");
	}

	public void editContact() {
		message = "Edit User";
	}

	public void save() {
		JJContact tmpcontact = null;
		tmpcontact = jJContactService.findJJContact(contact.getId());
		if (tmpcontact == null) {
			System.out.println("IS a new Contact");
			jJContactService.saveJJContact(contact);
		} else {
			jJContactService.updateJJContact(contact);
		}
	}

}

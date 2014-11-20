package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.starit.janjoonweb.domain.JJConfiguration;
import com.starit.janjoonweb.domain.JJConfigurationService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJPermission;
import com.starit.janjoonweb.domain.JJPermissionService;
import com.starit.janjoonweb.ui.mb.JJPermissionBean.PermissionDataModel;
import com.starit.janjoonweb.ui.mb.lazyLoadingDataTable.LazyContactDataModel;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJContact.class, beanName = "jJContactBean")
public class JJContactBean {

	@Autowired
	private JJConfigurationService jJConfigurationService;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private JJPermissionService jJPermissionService;

	private JJContact contactAdmin;
	private List<JJContact> contacts;
	private LazyContactDataModel contactsLazyModel;
	private String message;
	private boolean disabledContactMode;
	private boolean disabledPermissionMode;
	private boolean contactState;

	public LazyContactDataModel getContactsLazyModel() {
		if (contactsLazyModel == null)
			contactsLazyModel = new LazyContactDataModel(jJContactService);
		getContacts();
		return contactsLazyModel;
	}

	public void setContactsLazyModel(LazyContactDataModel contactsLazyModel) {
		this.contactsLazyModel = contactsLazyModel;
	}

	public void setEncoder(BCryptPasswordEncoder encoder) {
		this.encoder = encoder;
	}

	public void setjJConfigurationService(
			JJConfigurationService jJConfigurationService) {
		this.jJConfigurationService = jJConfigurationService;
	}

	public void setjJPermissionService(JJPermissionService jJPermissionService) {
		this.jJPermissionService = jJPermissionService;
	}

	public JJContact getContactAdmin() {

		return contactAdmin;
	}

	public void setContactAdmin(JJContact contactAdmin) {
		this.contactAdmin = contactAdmin;
	}

	public List<JJContact> getContacts() {
		if (contacts == null)
			contacts = jJContactService.getContacts(true);
		return contacts;
	}

	public void setContacts(List<JJContact> contacts) {
		this.contacts = contacts;
	}

	public boolean getDisabledContactMode() {
		return disabledContactMode;
	}

	public void setDisabledContactMode(boolean disabledContactMode) {
		this.disabledContactMode = disabledContactMode;
	}

	public boolean getDisabledPermissionMode() {
		return disabledPermissionMode;
	}

	public void setDisabledPermissionMode(boolean disabledPermissionMode) {
		this.disabledPermissionMode = disabledPermissionMode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void newContact(JJPermissionBean jJPermissionBean) {

		message = "admin_contact_new_title";

		contactAdmin = new JJContact();
		contactAdmin.setEnabled(true);
		contactAdmin.setCreationDate(new Date());

		jJPermissionBean.setPermissionDataModel(null);
		jJPermissionBean.setDisabledCheckPermission(true);
		jJPermissionBean.newPermission();

		disabledContactMode = false;
		disabledPermissionMode = true;

		contactState = true;

	}

	public void editContact(JJPermissionBean jJPermissionBean) {
		message = "admin_contact_edit_title";

		jJPermissionBean.setDisabledCheckPermission(false);
		jJPermissionBean.newPermission();
		jJPermissionBean.fillPermissionTable(contactAdmin);

		disabledContactMode = false;
		disabledPermissionMode = false;

		contactState = false;

	}

	public void deleteContact() {
		// message = "Edit Contact";

		if (contactAdmin != null) {

			contactAdmin.setEnabled(false);
			contactAdmin.setUpdatedDate(new Date());
			contactAdmin.setUpdatedBy(((JJContact)LoginBean.findBean("JJContact")));
			if (!jJContactService.updateJJContactTransaction(contactAdmin)) {

				FacesMessage facesMessage = MessageFactory.getMessage(
						"jjcontact_unsuccessfully_created",
						FacesMessage.SEVERITY_ERROR, "Contact");
				contactAdmin.setEmail("");
				contactAdmin.setPassword("");
				FacesContext.getCurrentInstance()
						.addMessage(null, facesMessage);
			}

		}
		contacts.remove(contains(contactAdmin.getId()));
		contactsLazyModel = null;
	}

	public void addContact(JJPermissionBean jJPermissionBean) {

		FacesMessage facesMessage = null;

		if (contactAdmin.getId() == null) {

			contactAdmin.setDescription("This contact is "
					+ contactAdmin.getFirstname() + " "
					+ contactAdmin.getName());
			contactAdmin
					.setPassword(encoder.encode(contactAdmin.getPassword()));
			contactAdmin.setCreationDate(new Date());
			contactAdmin.setCreatedBy(((JJContact)LoginBean.findBean("JJContact")));

			if (jJContactService.saveJJContactTransaction(contactAdmin)) {

				disabledContactMode = true;
				disabledPermissionMode = false;

				jJPermissionBean
						.setPermissionDataModel(new ArrayList<PermissionDataModel>());
				HttpSession session = (HttpSession) FacesContext
						.getCurrentInstance().getExternalContext()
						.getSession(false);
				LoginBean loginBean = (LoginBean) session
						.getAttribute("loginBean");
				if (contactAdmin.equals(loginBean.getContact()))
					loginBean.getAuthorisationService().setSession(session);

				facesMessage = MessageFactory.getMessage(
						"message_successfully_created",
						FacesMessage.SEVERITY_INFO, "Contact");
				contacts.add(contactAdmin);

			} else {

				facesMessage = MessageFactory.getMessage(
						"jjcontact_unsuccessfully_created",
						FacesMessage.SEVERITY_ERROR, "Contact");
				contactAdmin.setEmail("");
				contactAdmin.setPassword("");
			}

		}
		contactsLazyModel = null;

		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}

	public void save(JJPermissionBean jJPermissionBean) {

		System.out.println("in save permission");

		contactAdmin.setUpdatedDate(new Date());		
		contactAdmin.setUpdatedBy(((JJContact)LoginBean.findBean("JJContact")));

		if (!contactAdmin.getPassword().equals(
				jJContactService.findJJContact(contactAdmin.getId())
						.getPassword())) {
			contactAdmin
					.setPassword(encoder.encode(contactAdmin.getPassword()));
		}

		if (jJContactService.updateJJContactTransaction(contactAdmin)) {
			List<JJPermission> permissions = jJPermissionService
					.getPermissions(contactAdmin, true, null, null, null);

			List<PermissionDataModel> permissionDataModels = jJPermissionBean
					.getPermissionDataModel();

			List<JJPermission> selectedPermissions = new ArrayList<JJPermission>();
			for (PermissionDataModel permissionDataModel : permissionDataModels) {
				if (permissionDataModel.getCheckPermission()) {
					selectedPermissions
							.add(permissionDataModel.getPermission());

				}
			}

			if (!selectedPermissions.isEmpty() && !permissions.isEmpty()) {

				for (JJPermission permission : selectedPermissions) {
					if (permission.getId() == null) {

						permission.setContact(contactAdmin);
						// contactAdmin.getPermissions().add(permission);
						jJPermissionBean.saveJJPermission(permission);
					}
				}

				for (JJPermission permission : permissions) {
					if (!selectedPermissions.contains(permission)) {

						permission.setEnabled(false);
						jJPermissionBean.updateJJPermission(permission);
					}
				}

			} else if (selectedPermissions.isEmpty() && !permissions.isEmpty()) {

				for (JJPermission permission : permissions) {

					permission.setEnabled(false);
					jJPermissionBean.updateJJPermission(permission);

				}

			} else if (!selectedPermissions.isEmpty() && permissions.isEmpty()) {

				for (JJPermission permission : selectedPermissions) {
					permission.setContact(contactAdmin);
					// contactAdmin.getPermissions().add(permission);
					jJPermissionBean.saveJJPermission(permission);
				}

			}

			contactAdmin = jJContactService.findJJContact(contactAdmin.getId());
			if (contains(contactAdmin.getId()) != -1)
				contacts.set(contains(contactAdmin.getId()), contactAdmin);
			else
				contacts.add(contactAdmin);

			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);
			LoginBean loginBean = (LoginBean) session.getAttribute("loginBean");
			if (contactAdmin.equals(loginBean.getContact()))
				loginBean.getAuthorisationService().setSession(session);

			FacesContext.getCurrentInstance().addMessage(
					null,
					MessageFactory.getMessage("message_successfully_updated",
							"Contact"));

			RequestContext context = RequestContext.getCurrentInstance();

			if (contactState) {
				if (getContactDialogConfiguration()) {
					context.execute("PF('contactDialogWidget').hide()");
				} else {
					newContact(jJPermissionBean);
				}
			} else {
				context.execute("PF('contactDialogWidget').hide()");
			}

			System.out.println("dfgdfgf");
			contactsLazyModel = null;
		} else
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Mail Already Exist", "Contact"));

	}

	// public void save(JJPermissionBean jJPermissionBean) {
	//
	// FacesMessage facesMessage = null;
	//
	// if (contactAdmin.getId() == null) {
	//
	// contactAdmin.setDescription("This contact is "
	// + contactAdmin.getFirstname() + " "
	// + contactAdmin.getName());
	//
	// if (jJContactService.saveJJContactTransaction(contactAdmin)) {
	// facesMessage = MessageFactory.getMessage(
	// "message_successfully_created",
	// FacesMessage.SEVERITY_INFO, "Contact");
	// JJContact contact = jJContactService.findJJContact(contactAdmin
	// .getId());
	// jJPermissionBean.setContact(contact);
	//
	// } else {
	//
	// facesMessage = MessageFactory.getMessage(
	// "jjcontact_unsuccessfully_created",
	// FacesMessage.SEVERITY_ERROR, "Contact");
	// contactAdmin.setEmail("");
	// contactAdmin.setPassword("");
	// }
	//
	// } else {
	//
	// contactAdmin.setUpdatedDate(new Date());
	// if (jJContactService.updateJJContactTransaction(contactAdmin)) {
	// facesMessage = MessageFactory.getMessage(
	// "message_successfully_updated",
	// FacesMessage.SEVERITY_INFO, "Contact");
	//
	// } else {
	// facesMessage = MessageFactory.getMessage(
	// "jjcontact_unsuccessfully_created",
	// FacesMessage.SEVERITY_ERROR, "Contact");
	// contactAdmin.setEmail("");
	// contactAdmin.setPassword("");
	// }
	// }
	//
	// FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	//
	// }

	public void addMessage() {
		String summary = contactAdmin.getEnabled() ? "Active Contact"
				: "Inactive Contact";

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(summary));
	}

	public void closeDialog(JJPermissionBean jJPermissionBean) {

		contactAdmin = null;

		jJPermissionBean.setPermissionAdmin(null);
		jJPermissionBean.setPermissionDataModel(null);
		jJPermissionBean.setProfile(null);
		jJPermissionBean.setProfiles(null);
		jJPermissionBean.setProject(null);
		jJPermissionBean.setProjects(null);
		jJPermissionBean.setProduct(null);
		jJPermissionBean.setProducts(null);

		contactState = true;

	}

	public JJContact getContactByEmail(String email) {
		return jJContactService.getContactByEmail(email, true);
	}

	public int contains(Long id) {
		int i = 0;
		int j = -1;

		if (contacts != null) {
			while (i < contacts.size()) {
				if (contacts.get(i).getId().equals(id)) {
					j = i;
					i = contacts.size();
				} else
					i++;
			}
		}

		return j;

	}

	public boolean emailValid(String mail, JJContact c) {
		boolean valid = true;

		JJContact ctc = jJContactService.getContactByEmail(mail, false);

		if (ctc != null) {
			if (c.getId() == null)
				valid = false;
			else {
				if (!ctc.equals(c)) {
					valid = false;
				}

			}
		}
		return valid;
	}
	
	private boolean getContactDialogConfiguration() {
		return jJConfigurationService.getDialogConfig("AdminUserDialog",
				"admin.user.create.saveandclose");
	}
}

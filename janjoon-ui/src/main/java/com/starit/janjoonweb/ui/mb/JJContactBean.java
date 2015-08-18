package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJCategoryService;
import com.starit.janjoonweb.domain.JJCompany;
import com.starit.janjoonweb.domain.JJConfigurationService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJPermission;
import com.starit.janjoonweb.domain.JJPermissionService;
import com.starit.janjoonweb.domain.JJProfile;
import com.starit.janjoonweb.domain.JJProfileService;
import com.starit.janjoonweb.domain.JJRight;
import com.starit.janjoonweb.domain.JJRightService;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.ui.mb.JJPermissionBean.PermissionDataModel;
import com.starit.janjoonweb.ui.mb.lazyLoadingDataTable.LazyContactDataModel;
import com.starit.janjoonweb.ui.mb.util.Contact;
import com.starit.janjoonweb.ui.mb.util.ContactCalendarUtil;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;
import com.starit.janjoonweb.ui.mb.util.SprintUtil;

@RooSerializable
@RooJsfManagedBean(entity = JJContact.class, beanName = "jJContactBean")
public class JJContactBean {

	@Autowired
	private JJConfigurationService jJConfigurationService;

	@Autowired
	private JJProfileService jJProfileService;

	@Autowired
	private JJCategoryService jJCategoryService;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private JJPermissionService jJPermissionService;

	@Autowired
	private JJRightService jJRightService;

	public void setjJRightService(JJRightService jJRightService) {
		this.jJRightService = jJRightService;
	}

	public void setjJProfileService(JJProfileService jJProfileService) {
		this.jJProfileService = jJProfileService;
	}

	public void setContactState(boolean contactState) {
		this.contactState = contactState;
	}

	private JJContact contactAdmin;
	private Contact contactUtil;
	private ContactCalendarUtil calendarUtil;
	private List<JJCategory> categories;
	private List<JJCategory> loggedContactCategories;
	// private List<JJContact> contacts;
	private List<JJVersion> versionList;
	private LazyContactDataModel contactsLazyModel;
	private String message;
	private boolean disabledContactMode;
	private boolean disabledPermissionMode;
	private boolean contactState;
	private Date startDate;
	private Date endDate;

	private List<JJContact> deletedContact;
	private List<JJContact> restoredContact;

	public LazyContactDataModel getContactsLazyModel() {

		LoginBean loginBean = (LoginBean) LoginBean.findBean("loginBean");
		JJCompany company = null;
		if (!loginBean.getAuthorisationService().isAdminCompany())
			company = loginBean.getContact().getCompany();

		if (contactsLazyModel == null)
			contactsLazyModel = new LazyContactDataModel(jJContactService,
					company);

		// getContacts();

		return contactsLazyModel;
	}

	public Contact getContactUtil() {
		if (contactUtil == null)
			contactUtil = new Contact(
					((LoginBean) LoginBean.findBean("loginBean")).getContact(),
					jJPermissionService.getPermissions(((LoginBean) LoginBean
							.findBean("loginBean")).getContact(), true, null,
							null, null));
		return contactUtil;
	}

	public void setContactUtil(Contact contactUtil) {
		this.contactUtil = contactUtil;
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

	public void setjJCategoryService(JJCategoryService jJCategoryService) {
		this.jJCategoryService = jJCategoryService;
	}

	public ContactCalendarUtil getCalendarUtil() {
		if (calendarUtil == null)
			calendarUtil = new ContactCalendarUtil(
					((LoginBean) LoginBean.findBean("loginBean")).getContact());
		return calendarUtil;
	}

	public void setCalendarUtil(ContactCalendarUtil calendarUtil) {
		this.calendarUtil = calendarUtil;
	}

	public JJContact getContactAdmin() {

		if (contactAdmin == null)
			contactAdmin = new JJContact();

		return contactAdmin;
	}

	public void setContactAdmin(JJContact contactAdmin) {
		this.contactAdmin = contactAdmin;
	}

	// public List<JJContact> getContacts() {
	// if (contacts == null) {
	// LoginBean loginBean = (LoginBean) LoginBean.findBean("loginBean");
	// contacts = jJContactService.getContacts(true, loginBean
	// .getContact().getCompany(), loginBean.getContact());
	// }
	//
	// return contacts;
	// }

	// public void setContacts(List<JJContact> contacts) {
	// this.contacts = contacts;
	// }

	public List<JJVersion> getVersionList() {

		if (((LoginBean) LoginBean.findBean("loginBean")).getContact() != null) {
			JJContact c = ((LoginBean) LoginBean.findBean("loginBean"))
					.getContact();
			c.setLastVersion(null);
			versionList = jJVersionService.getVersions(true, true,
					c.getLastProduct(), c.getCompany(), true);
		}
		return versionList;
	}

	public void setVersionList(List<JJVersion> versionList) {
		this.versionList = versionList;
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

	public List<JJCategory> getCategories() {

		if (categories == null)
			categories = jJCategoryService.getCategories(null, false, true,
					true);
		return categories;
	}

	public void setCategories(List<JJCategory> categories) {
		this.categories = categories;
	}

	public List<JJCategory> getLoggedContactCategories() {

		if (loggedContactCategories == null)
			loggedContactCategories = new ArrayList<JJCategory>(
					jJContactService.findJJContact(
							((LoginBean) LoginBean.findBean("loginBean"))
									.getContact().getId()).getCategories());
		return loggedContactCategories;
	}

	public void setLoggedContactCategories(
			List<JJCategory> loggedContactCategories) {
		this.loggedContactCategories = loggedContactCategories;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<JJContact> getDeletedContact() {
		if (deletedContact == null) {
			LoginBean loginBean = (LoginBean) LoginBean.findBean("loginBean");
			deletedContact = jJContactService.getContacts(false, loginBean
					.getContact().getCompany(), loginBean.getContact());
		}

		return deletedContact;
	}

	public void setDeletedContact(List<JJContact> deletedContact) {
		this.deletedContact = deletedContact;
	}

	public List<JJContact> getRestoredContact() {
		if (restoredContact == null)
			restoredContact = new ArrayList<JJContact>();
		return restoredContact;
	}

	public void setRestoredContact(List<JJContact> restoredContact) {
		this.restoredContact = restoredContact;
	}

	public void restoreContacts() {
		for (JJContact con : restoredContact) {
			con.setEnabled(true);
			updateJJContact(con);
		}

		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_restored", FacesMessage.SEVERITY_INFO,
				"Contact");

		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		deletedContact = null;
		restoredContact = null;
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
			contactAdmin.setUpdatedBy(((LoginBean) LoginBean
					.findBean("loginBean")).getContact());
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
		// contacts.remove(contains(contactAdmin.getId()));
		contactsLazyModel = null;
	}

	public void addContact(JJPermissionBean jJPermissionBean) {

		FacesMessage facesMessage = null;

		if (contactAdmin.getId() == null) {

			contactAdmin.setDescription("This contact is "
					+ contactAdmin.getFirstname() + " "
					+ contactAdmin.getName());
			contactAdmin.setPassword(encoder.encode(contactAdmin.getPassword()
					.trim()));
			contactAdmin.setCreationDate(new Date());
			contactAdmin.setCreatedBy(((LoginBean) LoginBean
					.findBean("loginBean")).getContact());

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
				// contacts.add(contactAdmin);

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
		contactAdmin.setUpdatedBy(((LoginBean) LoginBean.findBean("loginBean"))
				.getContact());

		if (!contactAdmin
				.getPassword()
				.trim()
				.equals(jJContactService.findJJContact(contactAdmin.getId())
						.getPassword().trim())) {
			contactAdmin.setPassword(encoder.encode(contactAdmin.getPassword()
					.trim()));
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
			// if (contains(contactAdmin.getId()) != -1)
			// contacts.set(contains(contactAdmin.getId()), contactAdmin);
			// else
			// contacts.add(contactAdmin);

			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);
			LoginBean loginBean = (LoginBean) session.getAttribute("loginBean");
			loginBean.getAuthorisationService().setSession(session);

			JJSprintBean jJSprintBean = (JJSprintBean) session
					.getAttribute("jJSprintBean");

			if (jJSprintBean != null && jJSprintBean.getSprintList() != null
					&& !jJSprintBean.getSprintList().isEmpty()) {

				for (SprintUtil sprintUtil : jJSprintBean.getSprintList()) {
					if (sprintUtil.getContacts().contains(contactAdmin)) {
						sprintUtil.setPriseReal(null);
						sprintUtil.setPriseSold(null);
					}

				}
			}

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
			contactsLazyModel = null;
		} else
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mail Exist",
							"Contact"));

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
		// contacts = null;

		jJPermissionBean.setPermissionAdmin(null);
		jJPermissionBean.setPermissionDataModel(null);
		jJPermissionBean.setProfile(null);
		jJPermissionBean.setProfiles(null);
		jJPermissionBean.setProject(null);
		jJPermissionBean.setProduct(null);

		contactState = true;

		restoredContact = null;
		deletedContact = null;

	}

	public void addVacation(LoginBean loginBean, JJContact jJContact) {
		calendarUtil = new ContactCalendarUtil(jJContact);
		calendarUtil.addVacation(startDate, endDate, jJContactService);
		jJContact.setUpdatedDate(new Date());
		jJContactService.updateJJContact(jJContact);
		startDate = null;
		endDate = null;
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		loginBean.getAuthorisationService().setSession(session);
		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_updated", FacesMessage.SEVERITY_INFO,
				"Contact " + loginBean.getContact().getName());
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		session.setAttribute("jJContactBean", new JJContactBean());
	}

	public void updateUserConfiguration(LoginBean loginBean) {
		JJContact jJContact = contactUtil.getJJContact(jJContactService
				.getContactByEmail(loginBean.getUsername(), true));
		jJContact.setUpdatedDate(new Date());
		jJContact.setCategories(new HashSet<JJCategory>(this
				.getLoggedContactCategories()));
		jJContactService.updateJJContact(jJContact);
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		loginBean.getAuthorisationService().setSession(session);
		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_updated", FacesMessage.SEVERITY_INFO,
				"Contact " + loginBean.getContact().getName());

		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}

	public JJContact getContactByEmail(String email) {
		return jJContactService.getContactByEmail(email, true);
	}

	// public int contains(Long id) {
	// int i = 0;
	// int j = -1;
	//
	// if (contacts != null) {
	// while (i < contacts.size()) {
	// if (contacts.get(i).getId().equals(id)) {
	// j = i;
	// i = contacts.size();
	// } else
	// i++;
	// }
	// }
	//
	// return j;
	//
	// }

	public String signUp() {

		contactAdmin.setDescription("This contact is "
				+ contactAdmin.getFirstname() + " " + contactAdmin.getName());
		contactAdmin.setPassword(encoder.encode(contactAdmin.getPassword()
				.trim()));
		contactAdmin.setCreationDate(new Date());
		contactAdmin.setEnabled(true);
		contactAdmin.setCreatedBy(((LoginBean) LoginBean.findBean("loginBean"))
				.getContact());

		if (jJContactService.saveJJContactTransaction(contactAdmin)) {

			JJProfile customProfile = jJProfileService.getOneProfile(
					"CustomProfile", true);
			if (customProfile == null) {

				customProfile = createCustomProfile();

			}
			JJPermission permission = new JJPermission();
			permission.setContact(contactAdmin);
			permission.setProfile(customProfile);
			permission.setEnabled(true);
			jJPermissionService.saveJJPermission(permission);
			return "success";

		} else {

			contactAdmin.setEmail("");
			contactAdmin.setPassword("");
			return "fail";
		}

	}

	public void updateJJContact(JJContact b) {

		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		if (contact.equals(b)) {
			b.setUpdatedBy(b);
			((LoginBean) LoginBean.findBean("loginBean")).setContact(null);
		} else
			b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJContactService.updateJJContact(b);
	}

	private JJProfile createCustomProfile() {
		JJProfile customProfile = new JJProfile();
		customProfile.setName("CustomProfile");
		customProfile.setEnabled(true);
		jJProfileService.saveJJProfile(customProfile);

		String[] names = { "Project", "Product", "Contact", "Version", "Build",
				"Category", "Phase" };

		for (String name : names) {
			JJRight right = new JJRight();
			right.setObjet(name);
			right.setR(true);
			right.setW(false);
			right.setX(false);
			right.setEnabled(true);
			right.setProfile(customProfile);
			jJRightService.saveJJRight(right);
		}

		String[] names2 = { "Requirement", "Testcase", "Teststep", "Bug",
				"Sprint", "Testcaseexecution", "Teststepexecution" };

		for (String name : names2) {
			JJRight right = new JJRight();
			right.setObjet(name);
			right.setR(true);
			right.setW(true);
			right.setX(false);
			right.setEnabled(true);
			right.setProfile(customProfile);
			jJRightService.saveJJRight(right);
		}

		String[] names3 = { "Task", "Message", "Imputation" };

		for (String name : names3) {
			JJRight right = new JJRight();
			right.setObjet(name);
			right.setR(true);
			right.setW(true);
			right.setX(true);
			right.setEnabled(true);
			right.setProfile(customProfile);
			jJRightService.saveJJRight(right);
		}

		return customProfile;
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

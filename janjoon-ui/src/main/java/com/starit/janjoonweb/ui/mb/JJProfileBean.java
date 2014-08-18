package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJConfigurationService;
import com.starit.janjoonweb.domain.JJProfile;
import com.starit.janjoonweb.domain.JJRight;
import com.starit.janjoonweb.domain.JJRightService;
import com.starit.janjoonweb.ui.mb.JJRightBean.RightDataModel;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJProfile.class, beanName = "jJProfileBean")
public class JJProfileBean {

	@Autowired
	public JJConfigurationService jJConfigurationService;

	public void setjJConfigurationService(
			JJConfigurationService jJConfigurationService) {
		this.jJConfigurationService = jJConfigurationService;
	}

	@Autowired
	public JJRightService jJRightService;

	public void setjJRightService(JJRightService jJRightService) {
		this.jJRightService = jJRightService;
	}

	private JJProfile profileAdmin;
	private List<JJProfile> profileListTable;

	private String message;

	private boolean disabledProfileMode;
	private boolean disabledRightMode;

	private boolean profileState;

	public JJProfile getProfileAdmin() {
		return profileAdmin;
	}

	public void setProfileAdmin(JJProfile profileAdmin) {
		this.profileAdmin = profileAdmin;
	}

	public List<JJProfile> getProfileListTable() {

		profileListTable = jJProfileService.getProfiles(true);
		return profileListTable;
	}

	public void setProfileListTable(List<JJProfile> profileListTable) {
		this.profileListTable = profileListTable;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean getDisabledProfileMode() {
		return disabledProfileMode;
	}

	public void setDisabledProfileMode(boolean disabledProfileMode) {
		this.disabledProfileMode = disabledProfileMode;
	}

	public boolean getDisabledRightMode() {
		return disabledRightMode;
	}

	public void setDisabledRightMode(boolean disabledRightMode) {
		this.disabledRightMode = disabledRightMode;
	}

	public void newProfile(JJRightBean jJRightBean) {
		message = "admin_profile_new_title";
		
		profileAdmin = new JJProfile();
		profileAdmin.setEnabled(true);

		jJRightBean.setRightDataModel(null);
		jJRightBean.setDisabledCheckRight(true);
		jJRightBean.newRight();

		disabledProfileMode = false;
		disabledRightMode = true;

		profileState = true;
	}

	public void editProfile(JJRightBean jJRightBean) {
		message = "admin_profile_edit_title";

		jJRightBean.setDisabledCheckRight(false);
		jJRightBean.newRight();
		
		if (profileAdmin == null) {
			System.out.println("null");
		} else {
			System.out.println("111111   sdfsdfsd " + profileAdmin.getId());
		}

		
		jJRightBean.fillRightTable(profileAdmin);

		disabledProfileMode = false;
		disabledRightMode = false;

		profileState = false;
	}

	public void deleteProfile() {

		if (profileAdmin != null) {

			profileAdmin.setEnabled(false);
			jJProfileService.updateJJProfile(profileAdmin);

		}
	}

	public void addProfile(JJRightBean jJRightBean) {

		if (profileAdmin.getId() == null) {

			jJProfileService.saveJJProfile(profileAdmin);

			disabledProfileMode = true;
			disabledRightMode = false;

			jJRightBean.setRightDataModel(new ArrayList<RightDataModel>());

			FacesContext.getCurrentInstance().addMessage(
					null,
					MessageFactory.getMessage("message_successfully_created",
							"JJProfile"));

		}
	}

	public void save(JJRightBean jJRightBean) {

		System.out.println("in save right");

		jJProfileService.updateJJProfile(profileAdmin);

		System.out.println("pppp");

		List<JJRight> rights = jJRightService.getRights(profileAdmin, true);

		List<RightDataModel> rightDataModels = jJRightBean.getRightDataModel();
		List<JJRight> selectedRights = new ArrayList<JJRight>();
		for (RightDataModel rightDataModel : rightDataModels) {
			if (rightDataModel.getCheckRight()) {
				selectedRights.add(rightDataModel.getRight());

			}
		}

		if (!selectedRights.isEmpty() && !rights.isEmpty()) {

			for (JJRight right : selectedRights) {
				if (right.getId() == null) {
					right.setProfile(profileAdmin);

					profileAdmin.getRights().add(right);
					jJRightService.saveJJRight(right);
				}
			}

			for (JJRight right : rights) {
				if (!selectedRights.contains(right)) {
					right.setEnabled(false);
					jJRightService.updateJJRight(right);
				}
			}

		} else if (selectedRights.isEmpty() && !rights.isEmpty()) {

			for (JJRight right : rights) {
				right.setEnabled(false);
				jJRightService.updateJJRight(right);
			}

		} else if (!selectedRights.isEmpty() && rights.isEmpty()) {

			for (JJRight right : selectedRights) {
				right.setProfile(profileAdmin);

				profileAdmin.getRights().add(right);
				jJRightService.saveJJRight(right);
			}

		}

		System.out.println("herer");

		FacesContext.getCurrentInstance().addMessage(
				null,
				MessageFactory.getMessage("message_successfully_updated",
						"JJProfile"));

		RequestContext context = RequestContext.getCurrentInstance();

		if (profileState) {
			if (getProfileDialogConfiguration()) {
				context.execute("profileDialogWidget.hide()");
			} else {
				newProfile(jJRightBean);
			}
		} else {
			context.execute("profileDialogWidget.hide()");
		}

		System.out.println("dfgdfgf");
	}

	public void closeDialog(JJRightBean jJRightBean) {

		profileAdmin = null;
		jJRightBean.setRightAdmin(null);
		jJRightBean.setRightDataModel(null);
		jJRightBean.setCategory(null);
		jJRightBean.setCategories(null);
		jJRightBean.setObject(null);
		jJRightBean.setObjects(null);
	}

	private boolean getProfileDialogConfiguration() {

		return jJConfigurationService.getDialogConfig("ProfileDialog",
				"profile.create.saveandclose");
	}
}

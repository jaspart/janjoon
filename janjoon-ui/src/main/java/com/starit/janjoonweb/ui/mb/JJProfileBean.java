package com.starit.janjoonweb.ui.mb;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJProfile;
import com.starit.janjoonweb.domain.JJRight;
import com.starit.janjoonweb.ui.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJProfile.class, beanName = "jJProfileBean")
public class JJProfileBean {

	private JJProfile profileAdmin;
	private List<JJProfile> profileListTable;

	private String message;

	public JJProfile getProfileAdmin() {
		return profileAdmin;
	}

	public void setProfileAdmin(JJProfile profileAdmin) {
		this.profileAdmin = profileAdmin;
	}

	public List<JJProfile> getProfileListTable() {

		profileListTable = jJProfileService.findAllJJProfiles();
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

	public void newProfile(JJRightBean jJRightBean) {
		System.out.println("Initial bean product");
		message = "New Profile";
		profileAdmin = new JJProfile();

		jJRightBean.newRight();

	}
	
	public void deleteProfile() {
		
		if (profileAdmin != null) {
			System.out.println(profileAdmin.getName());

//			profileAdmin.setEnabled(false);
//			jJProfileService.updateJJProfile(profileAdmin);

		}
	}

	public void save(JJRightBean jJRightBean) {
		System.out.println("SAVING Profile...");
		String message = "";

		if (profileAdmin.getId() == null) {
			System.out.println("IS a new JJProfile");

			jJProfileService.saveJJProfile(profileAdmin);
			message = "message_successfully_created";

			List<JJRight> rights = jJRightBean.getRightListTable();

			if (rights != null && !rights.isEmpty()) {
				JJProfile profile = jJProfileService.findJJProfile(profileAdmin
						.getId());
				profile.getRights().addAll(rights);

				for (JJRight right : rights) {
					right.setProfile(profile);
				}

				jJProfileService.updateJJProfile(profile);

			}

			newProfile(jJRightBean);

		} else {
			jJProfileService.updateJJProfile(profileAdmin);
			message = "message_successfully_updated";
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("profileDialogWidget.hide()");
		}

		FacesMessage facesMessage = MessageFactory.getMessage(message,
				"JJProfile");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}

	public void closeDialog(JJRightBean jJRightBean) {
		System.out.println("close dialog");
		profileAdmin = null;
		jJRightBean.setRightAdmin(null);
		jJRightBean.setRightListTable(null);
		jJRightBean.setCategory(null);
		jJRightBean.setCategoryList(null);
		jJRightBean.setObject(null);
		jJRightBean.setObjectList(null);
	}
}

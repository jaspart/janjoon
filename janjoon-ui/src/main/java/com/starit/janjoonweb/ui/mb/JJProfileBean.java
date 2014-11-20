package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJConfigurationService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJProfile;
import com.starit.janjoonweb.domain.JJRight;
import com.starit.janjoonweb.domain.JJRightService;
import com.starit.janjoonweb.ui.mb.JJRightBean.RightDataModel;
import com.starit.janjoonweb.ui.mb.lazyLoadingDataTable.LazyProfileDataModel;
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
	private LazyProfileDataModel profileListTable;

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

	public LazyProfileDataModel getProfileListTable() {

		if(profileListTable == null)
			profileListTable=new LazyProfileDataModel(jJProfileService);		
		return profileListTable;
	}

	public void setProfileListTable(LazyProfileDataModel profileListTable) {
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
		jJRightBean.fillRightTable(profileAdmin);

		disabledProfileMode = false;
		disabledRightMode = false;

		profileState = false;
	}

	public void deleteProfile() {

		if (profileAdmin != null) {

			profileAdmin.setEnabled(false);
			updateJJProfile(profileAdmin);
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
					.getSession(false);
			LoginBean loginBean=(LoginBean) session.getAttribute("loginBean");
			loginBean.getAuthorisationService().setSession(session);
			profileListTable=null;

		}
	}

	public void addProfile(JJRightBean jJRightBean) {

		if (profileAdmin.getId() == null) {
			

			saveJJProfile(profileAdmin);

			disabledProfileMode = true;
			disabledRightMode = false;
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
					.getSession(false);
			LoginBean loginBean=(LoginBean) session.getAttribute("loginBean");
			loginBean.getAuthorisationService().setSession(session);
			
			jJRightBean.setRightDataModel(new ArrayList<RightDataModel>());

			FacesContext.getCurrentInstance().addMessage(
					null,
					MessageFactory.getMessage("message_successfully_created",
							"Profile"));
			profileListTable=null;

		}
	}

	public void save(JJRightBean jJRightBean) {

		System.out.println("in save right");

		updateJJProfile(profileAdmin);		
		
		profileAdmin = jJProfileService.findJJProfile(profileAdmin.getId());

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
					jJRightBean.saveJJRight(right);
				}
			}

			List<Long> longs = new ArrayList<Long>();
			for (JJRight right : rights) {
				if (!selectedRights.contains(right)) {
					longs.add(right.getId());
				}
			}
			for (long l : longs) {
				JJRight r=jJRightService.findJJRight(l);
				r.setEnabled(false);
				jJRightBean.updateJJRight(r);
			}

		} else if (selectedRights.isEmpty() && !rights.isEmpty()) {

			List<Long> longs = new ArrayList<Long>();
			for (JJRight right : rights) {

				longs.add(right.getId());

			}
			for (long l : longs) {
				JJRight r=jJRightService.findJJRight(l);
				r.setEnabled(false);
				jJRightBean.updateJJRight(r);
			}

		} else if (!selectedRights.isEmpty() && rights.isEmpty()) {

			for (JJRight right : selectedRights) {
				right.setProfile(profileAdmin);

				profileAdmin.getRights().add(right);
				jJRightBean.saveJJRight(right);
			}

		}

		System.out.println("herer");

		FacesContext.getCurrentInstance().addMessage(
				null,
				MessageFactory.getMessage("message_successfully_updated",
						"Profile"));
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
				.getSession(false);
		LoginBean loginBean=(LoginBean) session.getAttribute("loginBean");
		loginBean.getAuthorisationService().setSession(session);


		RequestContext context = RequestContext.getCurrentInstance();

		if (profileState) {
			if (getProfileDialogConfiguration()) {
				context.execute("PF('profileDialogWidget').hide()");
			} else {
				newProfile(jJRightBean);
			}
		} else {
			context.execute("PF('profileDialogWidget').hide()");
		}

		System.out.println("dfgdfgf");
		profileListTable=null;
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
	
	public void saveJJProfile(JJProfile b)
	{		
		jJProfileService.saveJJProfile(b);
	}
	
	public void updateJJProfile(JJProfile b)
	{
		jJProfileService.updateJJProfile(b);
	}
}

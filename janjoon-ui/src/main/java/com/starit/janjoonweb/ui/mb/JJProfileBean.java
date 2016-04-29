package com.starit.janjoonweb.ui.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;

import com.starit.janjoonweb.domain.JJConfiguration;
import com.starit.janjoonweb.domain.JJConfigurationService;
import com.starit.janjoonweb.domain.JJPermissionService;
import com.starit.janjoonweb.domain.JJProfile;
import com.starit.janjoonweb.domain.JJRight;
import com.starit.janjoonweb.domain.JJRightService;
import com.starit.janjoonweb.ui.mb.JJRightBean.RightDataModel;
import com.starit.janjoonweb.ui.mb.lazyLoadingDataTable.LazyProfileDataModel;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;


@RooJsfManagedBean(entity = JJProfile.class, beanName = "jJProfileBean")
public class JJProfileBean implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	public JJConfigurationService jJConfigurationService;

	@Autowired
	public JJRightService jJRightService;

	@Autowired
	private JJPermissionService jJPermissionService;

	private JJProfile profileAdmin;

	private LazyProfileDataModel profileListTable;

	private String message;

	private boolean disabledProfileMode;
	private boolean disabledRightMode;

	private boolean profileState;

	public void addProfile(final JJRightBean jJRightBean) {

		if (profileAdmin.getId() == null) {

			saveJJProfile(profileAdmin);

			disabledProfileMode = true;
			disabledRightMode = false;

			jJRightBean.setRightDataModel(new ArrayList<RightDataModel>());

			FacesContext.getCurrentInstance().addMessage(null, MessageFactory
					.getMessage("message_successfully_created", "Profile", ""));
			profileListTable = null;

		}
	}

	public void closeDialog(final JJRightBean jJRightBean) {

		profileAdmin = null;
		jJRightBean.setRightAdmin(null);
		jJRightBean.setRightDataModel(null);
		jJRightBean.setCategory(null);
		jJRightBean.setObject(null);
		jJRightBean.setObjects(null);
		final HttpSession session = (HttpSession) FacesContext
				.getCurrentInstance().getExternalContext().getSession(false);
		final LoginBean loginBean = (LoginBean) session
				.getAttribute("loginBean");
		loginBean.getAuthorisationService().setSession(session);
	}

	public void deleteProfile() {

		if (profileAdmin != null) {

			profileAdmin.setEnabled(false);
			updateJJProfile(profileAdmin);
			final HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);
			final LoginBean loginBean = (LoginBean) session
					.getAttribute("loginBean");
			loginBean.getAuthorisationService().setSession(session);
			profileListTable = null;

		}
	}

	public void editProfile(final JJRightBean jJRightBean) {
		message = "admin_profile_edit_title";

		jJRightBean.setDisabledCheckRight(false);
		jJRightBean.newRight();
		jJRightBean.fillRightTable(profileAdmin);

		disabledProfileMode = false;
		disabledRightMode = false;

		profileState = false;
	}

	public boolean getDisabledProfileMode() {
		return disabledProfileMode;
	}

	public boolean getDisabledRightMode() {
		return disabledRightMode;
	}

	public String getMessage() {
		return message;
	}

	public JJProfile getProfileAdmin() {
		return profileAdmin;
	}

	private boolean getProfileDialogConfiguration() {
		
		Boolean val = jJConfigurationService.getDialogConfig("ProfileDialog",
				"profile.create.saveandclose");
		if(val == null)
		{
			JJConfiguration configuration = new JJConfiguration();
			configuration.setName("ProfileDialog");
			configuration.setDescription(
					"specify action after submit in profil dialog");
			configuration.setCreatedBy(
					((LoginBean) LoginBean.findBean("loginBean")).getContact());
			configuration.setCreationDate(new Date());
			configuration.setEnabled(true);
			configuration.setParam("profile.create.saveandclose");
			configuration.setVal("true");
			jJConfigurationService.saveJJConfiguration(configuration);
			
			val = jJConfigurationService.getDialogConfig("ProfileDialog",
					"profile.create.saveandclose");
		}
		return val;		
	}

	public LazyProfileDataModel getProfileListTable() {

		if (profileListTable == null) {
			profileListTable = new LazyProfileDataModel(jJProfileService,
					jJPermissionService);
		}
		return profileListTable;
	}

	public void newProfile(final JJRightBean jJRightBean) {
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

	public void save(final JJRightBean jJRightBean) {

		System.out.println("in save right");

		updateJJProfile(profileAdmin);

		profileAdmin = jJProfileService.findJJProfile(profileAdmin.getId());

		final List<JJRight> rights = jJRightService.getRights(profileAdmin,
				true);

		final List<RightDataModel> rightDataModels = jJRightBean
				.getRightDataModel();
		final List<JJRight> selectedRights = new ArrayList<JJRight>();
		for (final RightDataModel rightDataModel : rightDataModels) {
			if (rightDataModel.getCheckRight()) {
				selectedRights.add(rightDataModel.getRight());

			}
		}

		if (!selectedRights.isEmpty() && !rights.isEmpty()) {

			for (final JJRight right : selectedRights) {
				if (right.getId() == null) {
					right.setProfile(profileAdmin);

					profileAdmin.getRights().add(right);
					jJRightBean.saveJJRight(right);
				}
			}

			final List<Long> longs = new ArrayList<Long>();
			for (final JJRight right : rights) {
				if (!selectedRights.contains(right)) {
					longs.add(right.getId());
				}
			}
			for (final long l : longs) {
				final JJRight r = jJRightService.findJJRight(l);
				r.setEnabled(false);
				jJRightBean.updateJJRight(r);
			}

		} else if (selectedRights.isEmpty() && !rights.isEmpty()) {

			final List<Long> longs = new ArrayList<Long>();
			for (final JJRight right : rights) {

				longs.add(right.getId());

			}
			for (final long l : longs) {
				final JJRight r = jJRightService.findJJRight(l);
				r.setEnabled(false);
				jJRightBean.updateJJRight(r);
			}

		} else if (!selectedRights.isEmpty() && rights.isEmpty()) {

			for (final JJRight right : selectedRights) {
				right.setProfile(profileAdmin);

				profileAdmin.getRights().add(right);
				jJRightBean.saveJJRight(right);
			}

		}

		System.out.println("herer");

		FacesContext.getCurrentInstance().addMessage(null, MessageFactory
				.getMessage("message_successfully_updated", "Profile", ""));
		final HttpSession session = (HttpSession) FacesContext
				.getCurrentInstance().getExternalContext().getSession(false);
		final LoginBean loginBean = (LoginBean) session
				.getAttribute("loginBean");
		loginBean.getAuthorisationService().setSession(session);

		final RequestContext context = RequestContext.getCurrentInstance();

		if (profileState) {
			if (getProfileDialogConfiguration()) {
				context.execute("PF('profileDialogWidget').hide()");
				RequestContext.getCurrentInstance().update("growlForm");
			} else {
				newProfile(jJRightBean);
			}
		} else {
			context.execute("PF('profileDialogWidget').hide()");
			RequestContext.getCurrentInstance().update("growlForm");
		}
		profileListTable = null;
	}

	public void saveJJProfile(final JJProfile b) {
		jJProfileService.saveJJProfile(b);
	}

	public void setDisabledProfileMode(final boolean disabledProfileMode) {
		this.disabledProfileMode = disabledProfileMode;
	}

	public void setDisabledRightMode(final boolean disabledRightMode) {
		this.disabledRightMode = disabledRightMode;
	}

	public void setjJConfigurationService(
			final JJConfigurationService jJConfigurationService) {
		this.jJConfigurationService = jJConfigurationService;
	}

	public void setjJPermissionService(
			final JJPermissionService jJPermissionService) {
		this.jJPermissionService = jJPermissionService;
	}

	public void setjJRightService(final JJRightService jJRightService) {
		this.jJRightService = jJRightService;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public void setProfileAdmin(final JJProfile profileAdmin) {
		this.profileAdmin = profileAdmin;
	}

	public void setProfileListTable(
			final LazyProfileDataModel profileListTable) {
		this.profileListTable = profileListTable;
	}

	public void updateJJProfile(final JJProfile b) {
		jJProfileService.updateJJProfile(b);
	}
}

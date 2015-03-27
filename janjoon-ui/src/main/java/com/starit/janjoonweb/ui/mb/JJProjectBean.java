package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJCompany;
import com.starit.janjoonweb.domain.JJConfigurationService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJPermissionService;
import com.starit.janjoonweb.domain.JJPhase;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJProjectService;
import com.starit.janjoonweb.ui.mb.lazyLoadingDataTable.LazyProjectDataModel;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJProject.class, beanName = "jJProjectBean")
public class JJProjectBean {

	@Autowired
	public JJConfigurationService jJConfigurationService;

	@Autowired
	JJPermissionService jJPermissionService;

	private JJProject project;
	private JJProject projectAdmin;
	private LazyProjectDataModel projectListTable;
	private JJContact projectManager;
	private List<JJContact> projectManagerList;
	private String message;
	private boolean projectState;

	public void setjJConfigurationService(
			JJConfigurationService jJConfigurationService) {
		this.jJConfigurationService = jJConfigurationService;
	}

	public void setjJPermissionService(JJPermissionService jJPermissionService) {
		this.jJPermissionService = jJPermissionService;
	}

	public boolean isProjectState() {
		return projectState;
	}

	public void setProjectState(boolean projectState) {
		this.projectState = projectState;
	}

	public JJProject getProject() {	
			
		return project;

	}

	public void setProject(JJProject project) {
		this.project = project;
	}

	public List<JJProject> getProjectList() {

		if (((LoginBean) LoginBean.findBean("loginBean")).isEnable())
			return jJProjectService.getProjects(((LoginBean) LoginBean
					.findBean("loginBean")).getContact().getCompany(),
					((LoginBean) LoginBean.findBean("loginBean")).getContact(),
					true, false);
		else
			return new ArrayList<JJProject>();
	}

	public JJProject getProjectAdmin() {
		return projectAdmin;
	}

	public void setProjectAdmin(JJProject projectAdmin) {
		this.projectAdmin = projectAdmin;
	}

	public LazyProjectDataModel getProjectListTable() {

		LoginBean loginBean = (LoginBean) LoginBean.findBean("loginBean");
		JJCompany company = null;
		if (!loginBean.getAuthorisationService().isAdminCompany())
			company = loginBean.getContact().getCompany();

		if (projectListTable == null)
			projectListTable = new LazyProjectDataModel(jJProjectService,
					company);
		return projectListTable;
	}

	public void setProjectListTable(LazyProjectDataModel projectListTable) {
		this.projectListTable = projectListTable;
	}

	public JJContact getProjectManager() {
		return projectManager;
	}

	public void setProjectManager(JJContact projectManager) {
		this.projectManager = projectManager;
	}

	public List<JJContact> getProjectManagerList() {

		if (projectAdmin.getId() == null)
			projectManagerList = jJPermissionService.getManagers(
					((LoginBean) LoginBean.findBean("loginBean")).getContact()
							.getCompany(), ((LoginBean) LoginBean
							.findBean("loginBean")).getContact(), "Project");
		else
			projectManagerList = jJPermissionService.getManagers(projectAdmin
					.getManager().getCompany(), ((LoginBean) LoginBean
					.findBean("loginBean")).getContact(), "Product");
		;
		return projectManagerList;
	}

	public void setProjectManagerList(List<JJContact> projectManagerList) {
		this.projectManagerList = projectManagerList;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void newProject() {
		message = "admin_project_new_title";

		projectAdmin = new JJProject();
		projectAdmin.setEnabled(true);
		projectAdmin.setDescription("Defined as a Project");
		projectManager = null;
		projectState = true;
	}

	public void editProject() {
		message = "admin_project_edit_title";

		getProjectManagerList();
		if (projectManagerList.isEmpty()) {
			projectManager = null;

		} else {
			if (projectManagerList.contains(projectAdmin.getManager())) {
				projectManager = projectAdmin.getManager();
			} else {
				projectManager = null;
			}
		}
		projectState = false;
	}

	public void deleteProject() {

		if (projectAdmin != null) {

			projectAdmin.setEnabled(false);
			updateJJProject(projectAdmin);
			projectListTable = null;
		}
	}

	public void save() {

		String message = "";

		projectAdmin.setManager(projectManager);

		if (projectAdmin.getId() == null) {

			saveJJProject(projectAdmin);
			projectListTable = null;
			message = "message_successfully_created";

			newProject();

		} else {
			updateJJProject(projectAdmin);
			projectListTable = null;

			message = "message_successfully_updated";

		}

		FacesMessage facesMessage = MessageFactory.getMessage(message,
				"Project");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		RequestContext context = RequestContext.getCurrentInstance();

		if (projectState) {

			if (getProjectDialogConfiguration()) {
				context.execute("PF('projectDialogWidget').hide()");
			} else {
				newProject();
			}

		} else {

			context.execute("PF('projectDialogWidget').hide()");
		}
	}

	public void savePlanning() {

		String message = "";
		updateJJProject(project);
		project = jJProjectService.findJJProject(project.getId());
		projectListTable = null;
		message = "message_successfully_updated";
		FacesMessage facesMessage = MessageFactory.getMessage(message,
				"Project");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}

	public void addMessage() {
		String summary = projectAdmin.getEnabled() ? "Enabled Project"
				: "Disabled Project";

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(summary));
	}

	public void closeDialog() {

		projectAdmin = null;
		projectManager = null;
		projectManagerList = null;

		projectState = true;
	}

	public void saveJJProject(JJProject b) {
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setCreatedBy(contact);
		b.setCreationDate(new Date());
		jJProjectService.saveJJProject(b);
	}

	public void updateJJProject(JJProject b) {
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJProjectService.updateJJProject(b);
	}

	private boolean getProjectDialogConfiguration() {

		return jJConfigurationService.getDialogConfig("ProjectDialog",
				"project.create.saveandclose");
	}

}

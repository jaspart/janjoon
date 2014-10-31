package com.starit.janjoonweb.ui.mb;

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

import com.starit.janjoonweb.domain.JJConfigurationService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJPermissionService;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJProject.class, beanName = "jJProjectBean")
public class JJProjectBean {

	@Autowired
	public JJConfigurationService jJConfigurationService;

	
	public void setjJConfigurationService(
			JJConfigurationService jJConfigurationService) {
		this.jJConfigurationService = jJConfigurationService;
	}

	@Autowired
	JJPermissionService jJPermissionService;

	private JJProject project;
	private List<JJProject> projectList;

	private JJProject projectAdmin;
	private List<JJProject> projectListTable;

	private JJContact projectManager;
	private Set<JJContact> projectManagerList;

	private String message;

	private boolean projectState;
	
	private boolean haveTaskPermision;
	
	private String warmMessage;

	public void setjJPermissionService(JJPermissionService jJPermissionService) {
		this.jJPermissionService = jJPermissionService;
	}

	public boolean isProjectState() {
		return projectState;
	}

	public void setProjectState(boolean projectState) {
		this.projectState = projectState;
	}

	public boolean isHaveTaskPermision() {				
		return haveTaskPermision;
	}

	public void setHaveTaskPermision(boolean haveTaskPermision) {
		this.haveTaskPermision = haveTaskPermision;
	}

	public String getWarmMessage() {
		if(warmMessage==null)
			checkPersmission();
		return warmMessage;
	}

	public void setWarmMessage(String warmMessage) {
		this.warmMessage = warmMessage;
	}

	public JJProject getProject() {
		return project;
	}

	public void setProject(JJProject project) {
		this.project = project;
	}

	public List<JJProject> getProjectList() {

		if (projectList == null)
			projectList = jJProjectService.getProjects(true);
		return projectList;
	}

	public void setProjectList(List<JJProject> projectList) {
		this.projectList = projectList;
	}

	public JJProject getProjectAdmin() {
		return projectAdmin;
	}

	public void setProjectAdmin(JJProject projectAdmin) {
		this.projectAdmin = projectAdmin;
	}

	public List<JJProject> getProjectListTable() {
		projectListTable = jJProjectService.getProjects(true);
		return projectListTable;
	}

	public void setProjectListTable(List<JJProject> projectListTable) {
		this.projectListTable = projectListTable;
	}

	public JJContact getProjectManager() {
		return projectManager;
	}

	public void setProjectManager(JJContact projectManager) {
		this.projectManager = projectManager;
	}

	public Set<JJContact> getProjectManagerList() {

		projectManagerList = jJPermissionService.getManagers("JJProject");
		return projectManagerList;
	}

	public void setProjectManagerList(Set<JJContact> projectManagerList) {
		this.projectManagerList = projectManagerList;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public void checkPersmission()
	{		
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
				.getSession(false);
		JJContact contact = (JJContact) session.getAttribute("JJContact");
		haveTaskPermision=jJPermissionService.isAuthorized(contact, project, null, "JJTask", null,true, null, null);
		
		if (!haveTaskPermision)
			warmMessage = "You Have no permisson to access this Category";
		else
			warmMessage = "Project";
	}

	public void newProject() {
		message = "admin_project_new_title";
		
		projectAdmin = new JJProject();
		projectAdmin.setEnabled(true);
		projectAdmin.setCreationDate(new Date());
		projectAdmin.setDescription("Defined as a Project");
		projectManager = null;
		projectState = true;
	}

	public void editProject() {
		message = "admin_project_edit_title";

		getProjectManagerList();
		if (projectManagerList.isEmpty()) {
			projectManager = null;

		}
		else {
			if (projectManagerList.contains(projectAdmin.getManager())) {
				projectManager = projectAdmin.getManager();
			}
			else {
				projectManager = null;
			}
		}
		projectState = false;
	}

	public void deleteProject() {

		if (projectAdmin != null) {

			projectAdmin.setEnabled(false);
			jJProjectService.updateJJProject(projectAdmin);
			projectList = null;
		}
	}

	public void save() {

		String message = "";

		projectAdmin.setManager(projectManager);

		if (projectAdmin.getId() == null) {

			jJProjectService.saveJJProject(projectAdmin);
			projectList = null;
			message = "message_successfully_created";

			newProject();

		} else {

			projectAdmin.setUpdatedDate(new Date());

			jJProjectService.updateJJProject(projectAdmin);

			projectList = null;

			message = "message_successfully_updated";

		}

		FacesMessage facesMessage = MessageFactory.getMessage(message,
				"JJProject");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		RequestContext context = RequestContext.getCurrentInstance();

		if (projectState) {

			if (getProjectDialogConfiguration()) {
				context.execute("projectDialogWidget.hide()");
			} else {
				newProject();
			}

		} else {

			context.execute("projectDialogWidget.hide()");
		}
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

	private boolean getProjectDialogConfiguration() {

		return jJConfigurationService.getDialogConfig("ProjectDialog",
				"project.create.saveandclose");
	}

}

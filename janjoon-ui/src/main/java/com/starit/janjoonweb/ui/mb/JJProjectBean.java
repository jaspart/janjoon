package com.starit.janjoonweb.ui.mb;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJPermissionService;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJProject.class, beanName = "jJProjectBean")
public class JJProjectBean {

	@Autowired
	JJPermissionService jJPermissionService;

	private JJProject project;
	private List<JJProject> projectList;

	private JJProject projectAdmin;
	private List<JJProject> projectListTable;

	private JJContact projectManager;
	private Set<JJContact> projectManagerList;

	private String message;

	public void setjJPermissionService(JJPermissionService jJPermissionService) {
		this.jJPermissionService = jJPermissionService;
	}

	public JJProject getProject() {
		return project;
	}

	public void setProject(JJProject project) {
		this.project = project;
	}

	public List<JJProject> getProjectList() {
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

	public void newProject() {
		System.out.println("Initial bean project");
		message = "New Project";
		projectAdmin = new JJProject();
		projectAdmin.setEnabled(true);
		projectAdmin.setCreationDate(new Date());
		projectAdmin.setDescription("Defined as a Project");
		projectManager = null;
	}

	public void editProject() {
		System.out.println("Update bean project");
		message = "Edit Project";
		projectManager = projectAdmin.getManager();
	}

	public void deleteProject() {
		
		if (projectAdmin != null) {
			System.out.println(projectAdmin.getName());

			projectAdmin.setEnabled(false);
			jJProjectService.updateJJProject(projectAdmin);

		}
	}

	public void save() {
		System.out.println("SAVING Project...");
		String message = "";

		projectAdmin.setManager(projectManager);

		if (projectAdmin.getId() == null) {
			System.out.println("IS a new JJProject");

			jJProjectService.saveJJProject(projectAdmin);
			message = "message_successfully_created";

			newProject();

		} else {
			System.out.println("UPDATING Project...");

			projectAdmin.setUpdatedDate(new Date());

			jJProjectService.updateJJProject(projectAdmin);

			message = "message_successfully_updated";

			// closeDialog();

			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("projectDialogWidget.hide()");

		}

		FacesMessage facesMessage = MessageFactory.getMessage(message,
				"JJProject");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}

	public void addMessage() {
		String summary = projectAdmin.getEnabled() ? "Enabled Project"
				: "Disabled Project";

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(summary));
	}

	public void closeDialog() {
		System.out.println("close dialog");
		projectAdmin = null;
		projectManager = null;
		projectManagerList = null;
	}

}

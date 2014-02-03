package com.funder.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.funder.janjoonweb.domain.JJBuild;
import com.funder.janjoonweb.domain.JJBuildService;
import com.funder.janjoonweb.domain.JJCategory;
import com.funder.janjoonweb.domain.JJCategoryService;
import com.funder.janjoonweb.domain.JJContact;
import com.funder.janjoonweb.domain.JJContactService;
import com.funder.janjoonweb.domain.JJPermission;
import com.funder.janjoonweb.domain.JJPermissionService;
import com.funder.janjoonweb.domain.JJProduct;
import com.funder.janjoonweb.domain.JJProductService;
import com.funder.janjoonweb.domain.JJProfile;
import com.funder.janjoonweb.domain.JJProfileService;
import com.funder.janjoonweb.domain.JJProject;
import com.funder.janjoonweb.domain.JJRequirementService;
import com.funder.janjoonweb.domain.JJRight;
import com.funder.janjoonweb.domain.JJRightService;
import com.funder.janjoonweb.domain.JJSprint;
import com.funder.janjoonweb.domain.JJSprintService;
import com.funder.janjoonweb.domain.JJStatus;
import com.funder.janjoonweb.domain.JJStatusService;
import com.funder.janjoonweb.domain.JJTask;
import com.funder.janjoonweb.domain.JJTaskService;
import com.funder.janjoonweb.domain.JJVersion;
import com.funder.janjoonweb.domain.JJVersionService;
import com.funder.janjoonweb.ui.mb.util.MessageFactory;

import com.funder.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJProject.class, beanName = "jJProjectBean")
public class JJProjectBean {

	private List<JJProject> projectListTable;

	private JJContact projectManager;
	private List<JJContact> projectManagerList;

	private String message;

	@Autowired
	JJStatusService jJStatusService;

	public void setjJStatusService(JJStatusService jJStatusService) {
		this.jJStatusService = jJStatusService;
	}

	@Autowired
	JJBuildService jJBuildService;

	public void setjJBuildService(JJBuildService jJBuildService) {
		this.jJBuildService = jJBuildService;
	}

	@Autowired
	JJSprintService jJSprintService;

	public void setjJSprintService(JJSprintService jJSprintService) {
		this.jJSprintService = jJSprintService;
	}

	@Autowired
	JJProductService jJProductService;

	public void setjJProductService(JJProductService jJProductService) {
		this.jJProductService = jJProductService;
	}

	@Autowired
	JJVersionService jJVersionService;

	public void setjJVersionService(JJVersionService jJVersionService) {
		this.jJVersionService = jJVersionService;
	}

	@Autowired
	JJCategoryService jJCategoryService;

	public void setjJCategoryService(JJCategoryService jJCategoryService) {
		this.jJCategoryService = jJCategoryService;
	}

	@Autowired
	JJRequirementService jJRequirementService;

	public void setjJRequirementService(
			JJRequirementService jJRequirementService) {
		this.jJRequirementService = jJRequirementService;
	}

	@Autowired
	JJContactService jJContactService;

	public void setjJContactService(JJContactService jJContactService) {
		this.jJContactService = jJContactService;
	}

	@Autowired
	JJPermissionService jJPermissionService;

	public void setjJPermissionService(JJPermissionService jJPermissionService) {
		this.jJPermissionService = jJPermissionService;
	}

	@Autowired
	JJRightService jJRightService;

	public void setjJRightService(JJRightService jJRightService) {
		this.jJRightService = jJRightService;
	}

	@Autowired
	JJTaskService jJTaskService;

	public void setjJTaskService(JJTaskService jJTaskService) {
		this.jJTaskService = jJTaskService;
	}

	@Autowired
	JJProfileService jJProfileService;

	public void setjJProfileService(JJProfileService jJProfileService) {
		this.jJProfileService = jJProfileService;
	}

	private JJProject project;
	private List<JJProject> projectList;

	private JJProject projectAdmin;
	private List<JJProject> projectListTable;

	private JJContact projectManager;
	private List<JJContact> projectManagerList;

	private String message;

	public JJProject getProject() {

		if (project == null) {
			System.out.println("Projet is null");
			project = jJProjectService.getJJProjectWithName("Default Project");
		}

		return project;
	}

	public void setProject(JJProject project) {

		this.project = project;
	}

	public void setProjectList(List<JJProject> projectList) {
		this.projectList = projectList;
	}

	public List<JJProject> getProjectListTable() {
		projectListTable = jJProjectService.getAllJJProjects();
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

	public List<JJContact> getProjectManagerList() {

		projectManagerList = new ArrayList<JJContact>();

		List<JJRight> rights = jJRightService.getObjectWriterList("Project");
		System.out.println("rights "+rights.size());
		for (JJRight right : rights ) {
			List<JJPermission> permissions = jJPermissionService
					.getProjectManagerPermissions(right.getProfile());
			for (JJPermission permission : permissions) {
				projectManagerList.add(permission.getContact());
			}
		}
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
		message = "New Project";
		project = new JJProject();
		project.setEnabled(true);
		project.setCreationDate(new Date());
		project.setDescription("Defined as a Project");
	}

	public void editProject() {
		message = "Edit Project";
	}

	public void save() {
		System.out.println("SAVING ...");
		String message = "";

		if (project.getId() == null) {
			System.out.println("IS a new Project");
			jJProjectService.saveJJProject(project);
			message = "message_successfully_created";
		} else {
			jJProjectService.updateJJProject(project);
			message = "message_successfully_updated";
		}

		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("projectDialogWidget.hide()");

		FacesMessage facesMessage = MessageFactory.getMessage(message,
				"JJProject");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}
	
	public void dialogClose(CloseEvent event) {
		newProject();
	}

/*	public void handleSelectProject(JJVersionBean jJVersionBean,
			JJProjectBean jJProjectBean, JJRequirementBean jJRequirementBean,
			JJChapterBean jJChapterBean) {

		if (project != null) {

//			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
//					"Project selected: " + project.getName(), "Selection info");
//			FacesContext.getCurrentInstance().addMessage(null, message);

			// Requete getReqwithProject&Project
			if (jJVersionBean != null) {
				jJVersionBean.setDisabled(false);
				jJVersionBean.setProduct(null);
				jJVersionBean.setProduct(project);
				jJVersionBean.setProject(project);
			}

			if (jJRequirementBean != null) {

				jJRequirementBean.setProduct(project);

				jJRequirementBean
						.setMyBusinessJJRequirements(jJRequirementService
								.getAllJJRequirementsWithProjectAndProduct(
										"BUSINESS", project, project));

				jJRequirementBean
						.setMyFunctionalJJRequirements(jJRequirementService
								.getAllJJRequirementsWithProjectAndProduct(
										"FUNCTIONAL", project, project));

				jJRequirementBean
						.setMyTechnicalJJRequirements(jJRequirementService
								.getAllJJRequirementsWithProjectAndProduct(
										"TECHNICAL", project, project));

			}
			if (jJChapterBean != null)
				jJChapterBean.setProduct(project);

		} else {
			// IF PRODUCT IS NULL GET ALL JJREQUIRMENTS WITH PROJECT

			if (jJVersionBean != null) {
				jJVersionBean.setDisabled(true);
				jJVersionBean.setProduct(null);
			}

			jJRequirementBean.setProduct(project);
			jJChapterBean.setProduct(project);

		if (jJCategoryService.getAllJJCategorys().isEmpty()) {
			String name = "BUSINESS";
			JJCategory CategoryBusiness = new JJCategory();
			CategoryBusiness.setName(name);
			CategoryBusiness.setCreationDate(new Date());
			CategoryBusiness.setDescription("A JJCategory defined as " + name);
			CategoryBusiness.setEnabled(true);
			CategoryBusiness.setStage(1);
			jJCategoryService.saveJJCategory(CategoryBusiness);

			name = "FUNCTIONAL";
			JJCategory CategoryFunctional = new JJCategory();
			CategoryFunctional.setName(name);
			CategoryFunctional.setCreationDate(new Date());
			CategoryFunctional
					.setDescription("A JJCategory defined as " + name);
			CategoryFunctional.setEnabled(true);
			CategoryFunctional.setStage(2);
			jJCategoryService.saveJJCategory(CategoryFunctional);

			name = "TECHNICAL";
			JJCategory CategoryTechnical = new JJCategory();
			CategoryTechnical.setName(name);
			CategoryTechnical.setCreationDate(new Date());
			CategoryTechnical.setDescription("A JJCategory defined as " + name);
			CategoryTechnical.setEnabled(true);
			CategoryTechnical.setStage(3);
			jJCategoryService.saveJJCategory(CategoryTechnical);

			name = "ARCHITECTURE";
			JJCategory CategoryArchitecture = new JJCategory();
			CategoryArchitecture.setName(name);
			CategoryArchitecture.setCreationDate(new Date());
			CategoryArchitecture.setDescription("A JJCategory defined as "
					+ name);
			CategoryArchitecture.setEnabled(true);
			CategoryArchitecture.setStage(2);
			jJCategoryService.saveJJCategory(CategoryArchitecture);

			name = "SECURITY";
			JJCategory CategorySecurity = new JJCategory();
			CategorySecurity.setName(name);
			CategorySecurity.setCreationDate(new Date());
			CategorySecurity.setDescription("A JJCategory defined as " + name);
			CategorySecurity.setEnabled(true);
			CategorySecurity.setStage(2);
			jJCategoryService.saveJJCategory(CategorySecurity);
		}

		if (jJRightService.findAllJJRights().isEmpty()) {

			JJCategory businessCategory = jJCategoryService
					.getJJCategoryWithName("BUSINESS");
			JJCategory functionalCategory = jJCategoryService
					.getJJCategoryWithName("FUNCTIONAL");
			JJCategory technicalCategory = jJCategoryService
					.getJJCategoryWithName("TECHNICAL");
			JJCategory architectureCategory = jJCategoryService
					.getJJCategoryWithName("ARCHITECTURE");

			JJProfile projectManagerProfile = jJProfileService
					.getJJProfileWithName("ProjectManager");
			JJProfile productManagerProfile = jJProfileService
					.getJJProfileWithName("ProductManager");
			JJProfile ceoProfile = jJProfileService.getJJProfileWithName("CEO");
			JJProfile ctoProfile = jJProfileService.getJJProfileWithName("CTO");
			JJProfile testerProfile = jJProfileService
					.getJJProfileWithName("Tester");
			JJProfile developerProfile = jJProfileService
					.getJJProfileWithName("Developer");

			// Project Manager Profile
			JJRight right = new JJRight();
			right.setObjet("Project");
			right.setR(true);
			right.setW(true);
			right.setX(true);
			right.setProfile(projectManagerProfile);

			projectManagerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Requirement");
			right.setCategory(businessCategory);
			right.setR(true);
			right.setW(true);
			right.setX(true);
			right.setProfile(projectManagerProfile);

			projectManagerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Requirement");
			right.setCategory(functionalCategory);
			right.setR(true);
			right.setW(false);
			right.setX(true);
			right.setProfile(projectManagerProfile);

			projectManagerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Requirement");
			right.setCategory(technicalCategory);
			right.setR(true);
			right.setW(false);
			right.setX(false);
			right.setProfile(projectManagerProfile);

			projectManagerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Requirement");
			right.setCategory(architectureCategory);
			right.setR(true);
			right.setW(false);
			right.setX(false);
			right.setProfile(projectManagerProfile);

			projectManagerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Planning");
			right.setCategory(architectureCategory);
			right.setR(true);
			right.setW(true);
			right.setX(false);
			right.setProfile(projectManagerProfile);

			projectManagerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Planning");
			right.setCategory(technicalCategory);
			right.setR(true);
			right.setW(true);
			right.setX(true);
			right.setProfile(projectManagerProfile);

			projectManagerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Test");
			right.setR(true);
			right.setW(false);
			right.setX(false);
			right.setProfile(projectManagerProfile);

			projectManagerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Product");
			right.setR(true);
			right.setW(false);
			right.setX(false);
			right.setProfile(projectManagerProfile);

			projectManagerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			// Product Manager Profile

			right = new JJRight();
			right.setObjet("Product");
			right.setCategory(functionalCategory);
			right.setR(true);
			right.setW(true);
			right.setX(false);
			right.setProfile(productManagerProfile);

			productManagerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Requirement");
			right.setCategory(businessCategory);
			right.setR(true);
			right.setW(false);
			right.setX(true);
			right.setProfile(productManagerProfile);

			productManagerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Requirement");
			right.setCategory(functionalCategory);
			right.setR(true);
			right.setW(true);
			right.setX(true);
			right.setProfile(productManagerProfile);

			productManagerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Requirement");
			right.setCategory(technicalCategory);
			right.setR(true);
			right.setW(true);
			right.setX(true);
			right.setProfile(productManagerProfile);

			productManagerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Requirement");
			right.setCategory(architectureCategory);
			right.setR(true);
			right.setW(true);
			right.setX(true);
			right.setProfile(productManagerProfile);

			productManagerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Planning");
			right.setCategory(architectureCategory);
			right.setR(true);
			right.setW(true);
			right.setX(true);
			right.setProfile(productManagerProfile);

			productManagerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Planning");
			right.setCategory(technicalCategory);
			right.setR(true);
			right.setW(true);
			right.setX(true);
			right.setProfile(productManagerProfile);

			productManagerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Test");
			right.setR(true);
			right.setW(true);
			right.setX(false);
			right.setProfile(productManagerProfile);

			productManagerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			// CEO Profile
			right = new JJRight();
			right.setObjet("*");
			right.setR(true);
			right.setW(true);
			right.setX(false);
			right.setProfile(ceoProfile);

			ceoProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			// CEO Profile
			right = new JJRight();
			right.setObjet("*");
			right.setR(true);
			right.setW(true);
			right.setX(true);
			right.setProfile(ctoProfile);

			ctoProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			// Tester Profile
			right = new JJRight();
			right.setObjet("Test");
			right.setR(true);
			right.setW(true);
			right.setX(true);
			right.setProfile(testerProfile);

			testerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Requirement");
			right.setR(true);
			right.setW(false);
			right.setX(true);
			right.setProfile(testerProfile);

			testerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Product");
			right.setCategory(technicalCategory);
			right.setR(false);
			right.setW(false);
			right.setX(true);
			right.setProfile(testerProfile);

			testerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			// Developer Profile
			right = new JJRight();
			right.setObjet("Test");
			right.setR(true);
			right.setW(true);
			right.setX(false);
			right.setProfile(developerProfile);

			developerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Requirement");
			right.setR(true);
			right.setW(false);
			right.setX(false);
			right.setProfile(developerProfile);

			developerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Product");
			right.setCategory(technicalCategory);
			right.setR(true);
			right.setW(true);
			right.setX(true);
			right.setProfile(developerProfile);

			developerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

		}

		// if (localJJContactService.getAllJJContact().isEmpty()) {
		// JJContact newJJContact = new JJContact();
		// newJJContact.setPassword("starit");
		// newJJContact.setEmail("starit@gmail.com");
		// newJJContact.setLdap(123456);
		// newJJContact.setFirstname("janjoon");
		// newJJContact.setLastname("admin");
		// newJJContact.setDateofbirth(new Date());
		// newJJContact.setEnabled(true);
		// newJJContact.setAccountNonExpired(true);
		// newJJContact.setCredentialsNonExpired(true);
		// newJJContact.setAccountNonLocked(true);
		//
		// // newJJContact.setJjright(rights);
		// jJContactService.saveJJContact(newJJContact);
		// }

		/*** End Temporary ***/

		projectList = jJProjectService.getAllJJProjects();
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
		projectListTable = jJProjectService.getAllJJProjects();
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

	public List<JJContact> getProjectManagerList() {

		projectManagerList = new ArrayList<JJContact>();

		List<JJRight> rights = jJRightService.getObjectWriterList("Project");
		System.out.println("rights " + rights.size());
		for (JJRight right : rights) {
			List<JJPermission> permissions = jJPermissionService
					.getManagerPermissions(right.getProfile());
			for (JJPermission permission : permissions) {
				projectManagerList.add(permission.getContact());
			}
		}

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

	public void handleSelectProject(JJProductBean jJProductBean,
			JJVersionBean jJVersionBean, JJRequirementBean jJRequirementBean,
			JJChapterBean jJChapterBean, JJTestcaseBean jJTestcaseBean,
			JJTeststepBean jJTeststepBean, JJBugBean jJBugBean) {
		if (project != null) {
			//
			// FacesMessage message = new
			// FacesMessage(FacesMessage.SEVERITY_INFO,
			// "Project selected: " + project.getName(), "Selection info");
			//
			// FacesContext.getCurrentInstance().addMessage(null, message);

			jJProductBean.setDisabled(false);
			jJProductBean.setProduct(null);
			jJProductBean.setProject(project);

			jJVersionBean.setDisabled(true);
			jJVersionBean.setProduct(null);

			jJRequirementBean.setProject(project);
			jJChapterBean.setProject(project);

			jJRequirementBean.setMyBusinessJJRequirements(jJRequirementService
					.getAllJJRequirementsWithCategoryAndProject("BUSINESS",
							project));

			jJRequirementBean
					.setMyFunctionalJJRequirements(jJRequirementService
							.getAllJJRequirementsWithCategoryAndProject(
									"FUNCTIONAL", project));

			jJRequirementBean.setMyTechnicalJJRequirements(jJRequirementService
					.getAllJJRequirementsWithCategoryAndProject("TECHNICAL",
							project));

			jJChapterBean.setProject(project);

			jJTestcaseBean.setCurrentProject(project);
			jJTestcaseBean.setRendered(false);
			jJTestcaseBean.initTestCaseParameter(jJTeststepBean);

			jJBugBean.setCurrentProject(project);

		}
	}

	public void newProject() {
		System.out.println("Initial bean project");
		message = "New Project";
		projectAdmin = new JJProject();
		projectAdmin.setEnabled(true);
		projectAdmin.setCreationDate(new Date());
		projectAdmin.setDescription("Defined as a Project");
	}

	public void save() {
		System.out.println("SAVING Project...");
		String message = "";

		if (projectAdmin.getId() == null) {
			System.out.println("IS a new JJProject");

			projectAdmin.setManager(projectManager);
			jJProjectService.saveJJProject(projectAdmin);
			message = "message_successfully_created";

			newProject();

		} else {
			jJProjectService.updateJJProject(projectAdmin);
			message = "message_successfully_updated";
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

	public void closeDialog(CloseEvent event) {
		System.out.println("close dialog");
		projectAdmin = null;
	}

}
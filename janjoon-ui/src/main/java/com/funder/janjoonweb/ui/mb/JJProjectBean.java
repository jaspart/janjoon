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

@RooSerializable
@RooJsfManagedBean(entity = JJProject.class, beanName = "jJProjectBean")
public class JJProjectBean {

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

	public List<JJProject> getProjectList() {
		/*** Begin Temporary ***/

		if (jJProfileService.findAllJJProfiles().isEmpty()) {
			JJProfile profile = new JJProfile();
			profile.setName("ProjectManager");
			jJProfileService.saveJJProfile(profile);

			profile = new JJProfile();
			profile.setName("ProductManager");
			jJProfileService.saveJJProfile(profile);

			profile = new JJProfile();
			profile.setName("CEO");
			jJProfileService.saveJJProfile(profile);

			profile = new JJProfile();
			profile.setName("CTO");
			jJProfileService.saveJJProfile(profile);

			profile = new JJProfile();
			profile.setName("Tester");
			jJProfileService.saveJJProfile(profile);

			profile = new JJProfile();
			profile.setName("Developer");
			jJProfileService.saveJJProfile(profile);

		}

		if (jJBuildService.getAllJJBuilds().isEmpty()) {
			JJBuild build;
			for (int i = 0; i < 4; i++) {
				build = new JJBuild();
				build.setName("Build 0." + i);
				build.setCreationDate(new Date());
				build.setDescription("Build 0." + i + " Description");
				build.setEnabled(true);
				jJBuildService.saveJJBuild(build);
			}
		}

		if (jJSprintService.getAllJJSprints().isEmpty()) {
			JJSprint sprint;
			for (int i = 0; i < 4; i++) {
				sprint = new JJSprint();
				sprint.setName("Sprint " + i);
				sprint.setCreationDate(new Date());
				sprint.setDescription("Sprint " + i + " Description");
				sprint.setEnabled(true);
				jJSprintService.saveJJSprint(sprint);
			}
		}

		if (jJStatusService.getAllJJStatuses().isEmpty()) {
			String name = "NEW";
			JJStatus StatusNew = new JJStatus();
			StatusNew.setName(name);
			StatusNew.setCreationDate(new Date());
			StatusNew.setDescription("A JJStatus defined as " + name);
			StatusNew.setEnabled(true);
			jJStatusService.saveJJStatus(StatusNew);

			name = "DELETED";
			JJStatus StatusDeleted = new JJStatus();
			StatusDeleted.setName(name);
			StatusDeleted.setCreationDate(new Date());
			StatusDeleted.setDescription("A JJStatus defined as " + name);
			StatusDeleted.setEnabled(true);
			jJStatusService.saveJJStatus(StatusDeleted);

			name = "DISCARDED";
			JJStatus StatusDiscarded = new JJStatus();
			StatusDiscarded.setName(name);
			StatusDiscarded.setCreationDate(new Date());
			StatusDiscarded.setDescription("A JJStatus defined as " + name);
			StatusDiscarded.setEnabled(true);
			jJStatusService.saveJJStatus(StatusDiscarded);

			name = "MODIFIED";
			JJStatus StatusModified = new JJStatus();
			StatusModified.setName(name);
			StatusModified.setCreationDate(new Date());
			StatusModified.setDescription("A JJStatus defined as " + name);
			StatusModified.setEnabled(true);
			jJStatusService.saveJJStatus(StatusModified);

			name = "RELEASED";
			JJStatus StatusReleased = new JJStatus();
			StatusReleased.setName(name);
			StatusReleased.setCreationDate(new Date());
			StatusReleased.setDescription("A JJStatus defined as " + name);
			StatusReleased.setEnabled(true);
			jJStatusService.saveJJStatus(StatusReleased);

			name = "FAILED";
			JJStatus StatusFailed = new JJStatus();
			StatusFailed.setName(name);
			StatusFailed.setCreationDate(new Date());
			StatusFailed.setDescription("A JJStatus defined as " + name);
			StatusFailed.setEnabled(true);
			jJStatusService.saveJJStatus(StatusFailed);

			name = "PASSED";
			JJStatus StatusPassed = new JJStatus();
			StatusPassed.setName(name);
			StatusPassed.setCreationDate(new Date());
			StatusPassed.setDescription("A JJStatus defined as " + name);
			StatusPassed.setEnabled(true);
			jJStatusService.saveJJStatus(StatusPassed);

			name = "CANCELED";
			JJStatus StatusCanceled = new JJStatus();
			StatusCanceled.setName(name);
			StatusCanceled.setCreationDate(new Date());
			StatusCanceled.setDescription("A JJStatus defined as " + name);
			StatusCanceled.setEnabled(true);
			jJStatusService.saveJJStatus(StatusCanceled);

			name = "RUNNING";
			JJStatus StatusRunning = new JJStatus();
			StatusRunning.setName(name);
			StatusRunning.setCreationDate(new Date());
			StatusRunning.setDescription("A JJStatus defined as " + name);
			StatusRunning.setEnabled(true);
			jJStatusService.saveJJStatus(StatusRunning);

		}

		if (jJTaskService.findAllJJTasks().isEmpty()) {
			JJTask task;
			for (int i = 0; i < 4; i++) {
				task = new JJTask();
				task.setName("TaskName " + i);
				task.setDescription("TaskDescription " + i);
				task.setCreationDate(new Date());
				task.setEnabled(true);
				jJTaskService.saveJJTask(task);
			}
		}

		if (jJVersionService.getAllJJVersion().isEmpty()) {
			JJVersion version;
			for (int i = 0; i < 4; i++) {
				version = new JJVersion();
				version.setName("VersionName " + i);
				version.setDescription("VersionDescription " + i);
				version.setCreationDate(new Date());
				version.setEnabled(true);
				jJVersionService.saveJJVersion(version);
			}
		}

		if (jJProductService.getAllJJProducts().isEmpty()) {
			JJProduct product;
			for (int i = 0; i < 2; i++) {
				product = new JJProduct();
				product.setName("ProductName " + i);
				product.setDescription("ProductDescription " + i);
				product.setCreationDate(new Date());
				product.setEnabled(true);
				product.setExtname("ProductExtName " + i);

				List<JJVersion> jJVersionList = jJVersionService
						.getAllJJVersion();

				Set<JJVersion> versions = new HashSet<JJVersion>();

				if (i == 0) {
					jJVersionList.get(i).setProduct(product);
					versions.add(jJVersionList.get(i));
					jJVersionList.get(i + 1).setProduct(product);
					versions.add(jJVersionList.get(i + 1));
				} else {
					jJVersionList.get(i + 1).setProduct(product);
					versions.add(jJVersionList.get(i + 1));
					jJVersionList.get(i + 2).setProduct(product);
					versions.add(jJVersionList.get(i + 2));
				}

				product.setVersions(versions);

				jJProductService.saveJJProduct(product);
			}
		}

		if (jJProjectService.getAllJJProjects().isEmpty()) {
			JJProject project;
			for (int i = 0; i < 2; i++) {
				project = new JJProject();
				project.setName("ProjectName " + i);
				project.setDescription("ProjectDescription " + i);
				project.setCreationDate(new Date());
				project.setEnabled(true);

				jJProjectService.saveJJProject(project);
			}
			project = new JJProject();
			project.setName("Default Project");
			project.setDescription("Delault ProjectDescription ");
			project.setCreationDate(new Date());
			project.setEnabled(true);

			jJProjectService.saveJJProject(project);
		}

		if (jJCategoryService.getAllJJCategories().isEmpty()) {
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
		projectManager = null;
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
		projectManager = null;
		projectManagerList = null;
	}

	public void handleSelectProjectManager() {
		System.out.println(projectManager.getFirstname() + " "
				+ projectManager.getName());
	}

}

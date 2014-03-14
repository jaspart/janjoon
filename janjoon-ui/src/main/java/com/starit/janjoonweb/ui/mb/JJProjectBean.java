package com.starit.janjoonweb.ui.mb;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJBuild;
import com.starit.janjoonweb.domain.JJBuildService;
import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJCategoryService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJContactService;
import com.starit.janjoonweb.domain.JJPermission;
import com.starit.janjoonweb.domain.JJPermissionService;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProductService;
import com.starit.janjoonweb.domain.JJProfile;
import com.starit.janjoonweb.domain.JJProfileService;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJRequirementService;
import com.starit.janjoonweb.domain.JJRight;
import com.starit.janjoonweb.domain.JJRightService;
import com.starit.janjoonweb.domain.JJSprint;
import com.starit.janjoonweb.domain.JJSprintService;
import com.starit.janjoonweb.domain.JJStatus;
import com.starit.janjoonweb.domain.JJStatusService;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJTaskService;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.domain.JJVersionService;
import com.starit.janjoonweb.ui.util.MessageFactory;

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
			project = jJProjectService.getJJProjectWithName("Default Project");
		}
		return project;
	}

	public void setProject(JJProject project) {
		this.project = project;
	}

	public List<JJProject> getProjectList() {
		/*** Begin Temporary ***/

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

		if (jJVersionService.getVersions(true, true, null).isEmpty()) {
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

		if (jJCategoryService.getCategories(null, false, true, true).isEmpty()) {
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

		if (jJRightService.findAllJJRights().isEmpty()) {

			JJCategory businessCategory = jJCategoryService
					.getCategoryWithName("BUSINESS", true);
			JJCategory functionalCategory = jJCategoryService
					.getCategoryWithName("FUNCTIONAL", true);
			JJCategory technicalCategory = jJCategoryService
					.getCategoryWithName("TECHNICAL", true);
			JJCategory architectureCategory = jJCategoryService
					.getCategoryWithName("ARCHITECTURE", true);

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
			right.setObjet("JJProject");
			right.setR(true);
			right.setW(true);
			right.setX(true);
			right.setProfile(projectManagerProfile);

			projectManagerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("JJRequirement");
			right.setCategory(businessCategory);
			right.setR(true);
			right.setW(true);
			right.setX(true);
			right.setProfile(projectManagerProfile);

			projectManagerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("JJRequirement");
			right.setCategory(functionalCategory);
			right.setR(true);
			right.setW(false);
			right.setX(true);
			right.setProfile(projectManagerProfile);

			projectManagerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("JJRequirement");
			right.setCategory(technicalCategory);
			right.setR(true);
			right.setW(false);
			right.setX(false);
			right.setProfile(projectManagerProfile);

			projectManagerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("JJRequirement");
			right.setCategory(architectureCategory);
			right.setR(true);
			right.setW(false);
			right.setX(false);
			right.setProfile(projectManagerProfile);

			projectManagerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("JJPlanning");
			right.setCategory(architectureCategory);
			right.setR(true);
			right.setW(true);
			right.setX(false);
			right.setProfile(projectManagerProfile);

			projectManagerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("JJPlanning");
			right.setCategory(technicalCategory);
			right.setR(true);
			right.setW(true);
			right.setX(true);
			right.setProfile(projectManagerProfile);

			projectManagerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("JJTest");
			right.setR(true);
			right.setW(false);
			right.setX(false);
			right.setProfile(projectManagerProfile);

			projectManagerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("JJProduct");
			right.setR(true);
			right.setW(false);
			right.setX(false);
			right.setProfile(projectManagerProfile);

			projectManagerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			// Product Manager Profile

			right = new JJRight();
			right.setObjet("JJProduct");
			right.setCategory(functionalCategory);
			right.setR(true);
			right.setW(true);
			right.setX(false);
			right.setProfile(productManagerProfile);

			productManagerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("JJRequirement");
			right.setCategory(businessCategory);
			right.setR(true);
			right.setW(false);
			right.setX(true);
			right.setProfile(productManagerProfile);

			productManagerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("JJRequirement");
			right.setCategory(functionalCategory);
			right.setR(true);
			right.setW(true);
			right.setX(true);
			right.setProfile(productManagerProfile);

			productManagerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("JJRequirement");
			right.setCategory(technicalCategory);
			right.setR(true);
			right.setW(true);
			right.setX(true);
			right.setProfile(productManagerProfile);

			productManagerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("JJRequirement");
			right.setCategory(architectureCategory);
			right.setR(true);
			right.setW(true);
			right.setX(true);
			right.setProfile(productManagerProfile);

			productManagerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("JJPlanning");
			right.setCategory(architectureCategory);
			right.setR(true);
			right.setW(true);
			right.setX(true);
			right.setProfile(productManagerProfile);

			productManagerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("JJPlanning");
			right.setCategory(technicalCategory);
			right.setR(true);
			right.setW(true);
			right.setX(true);
			right.setProfile(productManagerProfile);

			productManagerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("JJTest");
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
			right.setObjet("JJTest");
			right.setR(true);
			right.setW(true);
			right.setX(true);
			right.setProfile(testerProfile);

			testerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("JJRequirement");
			right.setR(true);
			right.setW(false);
			right.setX(true);
			right.setProfile(testerProfile);

			testerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("JJProduct");
			right.setCategory(technicalCategory);
			right.setR(false);
			right.setW(false);
			right.setX(true);
			right.setProfile(testerProfile);

			testerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			// Developer Profile
			right = new JJRight();
			right.setObjet("JJTest");
			right.setR(true);
			right.setW(true);
			right.setX(false);
			right.setProfile(developerProfile);

			developerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("JJRequirement");
			right.setR(true);
			right.setW(false);
			right.setX(false);
			right.setProfile(developerProfile);

			developerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("JJProduct");
			right.setCategory(technicalCategory);
			right.setR(true);
			right.setW(true);
			right.setX(true);
			right.setProfile(developerProfile);

			developerProfile.getRights().add(right);

			jJRightService.saveJJRight(right);

		}

		if (jJContactService.findAllJJContacts().isEmpty()) {

			JJContact contact = new JJContact();
			contact.setName("janjoon");
			contact.setFirstname("Admin");
			contact.setDescription("This contact is " + contact.getFirstname()
					+ " " + contact.getName());
			contact.setPassword("BeHappy2012");
			contact.setEnabled(true);
			contact.setEmail("admin@gmail.com");
			contact.setCreationDate(new Date());

			jJContactService.saveJJContact(contact);

			JJProfile projectManagerProfile = jJProfileService
					.getJJProfileWithName("ProjectManager");
			JJProfile productManagerProfile = jJProfileService
					.getJJProfileWithName("ProductManager");

			JJPermission permission = new JJPermission();
			permission.setContact(contact);
			permission.setProfile(projectManagerProfile);
			contact.getPermissions().add(permission);
			jJPermissionService.saveJJPermission(permission);

			permission = new JJPermission();
			permission.setContact(contact);
			permission.setProfile(productManagerProfile);
			contact.getPermissions().add(permission);
			jJPermissionService.saveJJPermission(permission);

		}

		if (jJProductService.getProducts(true).isEmpty()) {
			JJContact manager = null;
			List<JJContact> contacts = jJContactService.getContacts(
					"admin@gmail.com", true);

			if (contacts.size() > 0) {
				manager = contacts.get(0);
			}

			JJProduct product;
			for (int i = 0; i < 2; i++) {
				product = new JJProduct();
				product.setName("ProductName " + i);
				product.setDescription("ProductDescription " + i);
				product.setCreationDate(new Date());
				product.setEnabled(true);
				product.setExtname("ProductExtName " + i);
				product.setManager(manager);

				List<JJVersion> jJVersionList = jJVersionService.getVersions(
						true, true, null);

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
			product = new JJProduct();
			product.setName("Default Product");
			product.setDescription("Delault ProductDescription ");
			product.setCreationDate(new Date());
			product.setEnabled(true);
			product.setExtname("ProductExtName ");
			product.setManager(manager);

			List<JJVersion> jJVersionList = jJVersionService.getVersions(true,
					true, null);

			Set<JJVersion> versions = new HashSet<JJVersion>();

			jJVersionList.get(0).setProduct(product);
			versions.add(jJVersionList.get(0));
			jJVersionList.get(0 + 1).setProduct(product);
			versions.add(jJVersionList.get(0 + 1));
			product.setVersions(versions);
			jJProductService.saveJJProduct(product);
		}

		if (jJProjectService.getProjects(true).isEmpty()) {
			JJContact manager = null;
			List<JJContact> contacts = jJContactService.getContacts(
					"admin@gmail.com", true);

			if (contacts.size() > 0) {
				manager = contacts.get(0);
			}
			JJProject project;
			project = new JJProject();
			project.setName("Default Project");
			project.setDescription("Delault ProjectDescription ");
			project.setCreationDate(new Date());
			project.setEnabled(true);
			project.setManager(manager);
			jJProjectService.saveJJProject(project);

			for (int i = 0; i < 2; i++) {
				project = new JJProject();
				project.setName("ProjectName " + i);
				project.setDescription("ProjectDescription " + i);
				project.setCreationDate(new Date());
				project.setEnabled(true);
				project.setManager(manager);

				jJProjectService.saveJJProject(project);
			}

		}
		projectList = jJProjectService.getProjects(true);
		// List<JJProduct> productList = jJProductService.getProducts(true);
		// if (jJRequirementService.findAllJJRequirements().isEmpty()) {
		// JJRequirement jJRequirement;
		// for (int j = 0; j < projectList.size(); j++) {
		// jJRequirement = new JJRequirement();
		// jJRequirement.setName("Requirement " + j);
		// jJRequirement.setDescription("RequirementDescription " + j);
		// jJRequirement.setCreationDate(new Date());
		// jJRequirement.setEnabled(true);
		// jJRequirement.setProduct(productList.get(j));
		// jJRequirement.setProject(projectList.get(j));
		// jJRequirementService.saveJJRequirement(jJRequirement);
		// JJTask jJTask;
		// for (int i = 0; i < 4; i++) {
		// jJTask = new JJTask();
		// jJTask.setName("TaskName " + i + ":R-" + j);
		// jJTask.setDescription("TaskDescription " + i + ":R-" + j);
		// jJTask.setCreationDate(new Date());
		// jJTask.setEnabled(true);
		// jJTask.setRequirement(jJRequirement);
		// jJTask.setWorkloadPlanned(10);
		// jJTaskService.saveJJTask(jJTask);
		// }
		// }
		// }

		/*** End Temporary ***/

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

	public List<JJContact> getProjectManagerList() {

		projectManagerList = jJPermissionService.getManagers("JJProject");
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
			JJTeststepBean jJTeststepBean, JJBugBean jJBugBean,
			JJTaskBean jJTaskBean) {

		jJProductBean.setProject(project);

		jJRequirementBean.setProject(project);

		jJChapterBean.setProject(project);

		jJTestcaseBean.setCurrentProject(project);
		jJTestcaseBean.setRendered(false);
		jJTestcaseBean.initTestCaseParameter(jJTeststepBean);

		jJBugBean.setCurrentProject(project);
		jJTaskBean.setProject(project);

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
		System.out.println("Update bean category");
		message = "Edit Project";
		projectManager = projectAdmin.getManager();
	}

	public void deleteProject() {
		// message = "Edit Contact";

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
			projectAdmin.setUpdatedDate(new Date());

			jJProjectService.updateJJProject(projectAdmin);
			message = "message_successfully_updated";
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("projectDialogWidget.hide()");
			closeDialog();
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

	public void handleSelectProjectManager() {
		if (projectManager != null) {
			System.out.println(projectManager.getFirstname() + " "
					+ projectManager.getName());
		}
	}

}

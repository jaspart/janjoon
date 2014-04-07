package com.starit.janjoonweb.ui.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.starit.janjoonweb.domain.JJBuild;
import com.starit.janjoonweb.domain.JJBuildService;
import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJCategoryService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJContactService;
import com.starit.janjoonweb.domain.JJCriticity;
import com.starit.janjoonweb.domain.JJCriticityService;
import com.starit.janjoonweb.domain.JJPermission;
import com.starit.janjoonweb.domain.JJPermissionService;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProductService;
import com.starit.janjoonweb.domain.JJProfile;
import com.starit.janjoonweb.domain.JJProfileService;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJProjectService;
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
import com.starit.janjoonweb.ui.mb.ApplicationBean;

public class ConfigListener implements ServletContextListener {

	@Autowired
	JJProjectService jJProjectService;

	@Autowired
	JJStatusService jJStatusService;

	@Autowired
	JJBuildService jJBuildService;

	@Autowired
	JJSprintService jJSprintService;

	@Autowired
	JJProductService jJProductService;

	@Autowired
	JJCriticityService jJCriticityService;

	@Autowired
	JJVersionService jJVersionService;

	@Autowired
	JJCategoryService jJCategoryService;

	@Autowired
	JJRequirementService jJRequirementService;

	@Autowired
	JJContactService jJContactService;

	@Autowired
	JJPermissionService jJPermissionService;

	@Autowired
	JJRightService jJRightService;

	@Autowired
	JJTaskService jJTaskService;

	@Autowired
	JJProfileService jJProfileService;

	public void setjJProjectService(JJProjectService jJProjectService) {
		this.jJProjectService = jJProjectService;
	}

	public void setjJStatusService(JJStatusService jJStatusService) {
		this.jJStatusService = jJStatusService;
	}

	public void setjJBuildService(JJBuildService jJBuildService) {
		this.jJBuildService = jJBuildService;
	}

	public void setjJSprintService(JJSprintService jJSprintService) {
		this.jJSprintService = jJSprintService;
	}

	public void setjJProductService(JJProductService jJProductService) {
		this.jJProductService = jJProductService;
	}

	public void setjJCriticityService(JJCriticityService jJCriticityService) {
		this.jJCriticityService = jJCriticityService;
	}

	public void setjJVersionService(JJVersionService jJVersionService) {
		this.jJVersionService = jJVersionService;
	}

	public void setjJCategoryService(JJCategoryService jJCategoryService) {
		this.jJCategoryService = jJCategoryService;
	}

	public void setjJRequirementService(
			JJRequirementService jJRequirementService) {
		this.jJRequirementService = jJRequirementService;
	}

	public void setjJContactService(JJContactService jJContactService) {
		this.jJContactService = jJContactService;
	}

	public void setjJPermissionService(JJPermissionService jJPermissionService) {
		this.jJPermissionService = jJPermissionService;
	}

	public void setjJRightService(JJRightService jJRightService) {
		this.jJRightService = jJRightService;
	}

	public void setjJTaskService(JJTaskService jJTaskService) {
		this.jJTaskService = jJTaskService;
	}

	public void setjJProfileService(JJProfileService jJProfileService) {
		this.jJProfileService = jJProfileService;
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		WebApplicationContextUtils
				.getRequiredWebApplicationContext(sce.getServletContext())
				.getAutowireCapableBeanFactory().autowireBean(this);
		initApplication();
		System.out.println("contextInitialized-from-ConfigListener");

	}

	private void initApplication() {

		if (jJCriticityService.getCriticities("JJMessage", true).isEmpty()) {

			String[] names = { "ALERT", "INFO" };

			for (String name : names) {
				JJCriticity criticity = new JJCriticity();
				criticity.setObjet("JJMessage");
				criticity.setName(name);
				criticity.setDescription("AlertCriticityDescription");
				criticity.setCreationDate(new Date());
				criticity.setEnabled(true);
				criticity.setLevelCriticity(name.length());
				jJCriticityService.saveJJCriticity(criticity);
			}
		}

		if (jJBuildService.getBuilds(null, false, true).isEmpty()) {
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

		if (jJSprintService.getSprints(true).isEmpty()) {
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

		String[] objects = { "JJRequirement", "JJBug" };

		for (String object : objects) {

			List<String> names = new ArrayList<String>();

			if (jJStatusService.getStatus(object, true,
					new ArrayList<String>(), false).isEmpty()) {

				if (object.equalsIgnoreCase("JJRequirement")) {
					names.add("NEW");
					names.add("DELETED");
					names.add("DISCARDED");
					names.add("MODIFIED");
					names.add("RELEASED");
					names.add("FAILED");
					names.add("PASSED");
					names.add("CANCELED");
					names.add("RUNNING");

				} else if (object.equalsIgnoreCase("JJBug")) {
					names.add("NEW");
					names.add("ASSIGNED");
					names.add("REOPENED");
					names.add("READY");
					names.add("FIXED");
					names.add("INVALID");
					names.add("WONTFIX");
					names.add("DUPLICATE");
					names.add("RESOLVED");
					names.add("VERIFIED");
					names.add("CLOSED");
				}

				for (String name : names) {
					JJStatus status = new JJStatus();

					status.setObjet(object);
					status.setName(name);
					status.setCreationDate(new Date());
					status.setDescription("A JJStatus defined as " + name);
					status.setEnabled(true);
					jJStatusService.saveJJStatus(status);

				}
			}
		}

		if (jJVersionService.getVersions(true, true, null).isEmpty()) {
			JJVersion version;
			version = new JJVersion();
			version.setName("Default-Version");
			version.setDescription("Default-VersionDescription ");
			version.setCreationDate(new Date());
			version.setEnabled(true);
			jJVersionService.saveJJVersion(version);
			for (int i = 0; i < 2; i++) {
				version = new JJVersion();
				version.setName("VersionName " + i);
				version.setDescription("VersionDescription " + i);
				version.setCreationDate(new Date());
				version.setEnabled(true);
				jJVersionService.saveJJVersion(version);
			}
		}

		if (jJCategoryService.getCategories(null, false, true, true).isEmpty()) {
			String[] names = { "BUSINESS", "FUNCTIONAL", "TECHNICAL",
					"ARCHITECTURE", "SECURITY" };
			for (String name : names) {
				int stage = 0;
				if (name.equalsIgnoreCase("BUSINESS")) {
					stage = 1;
				} else if (name.equalsIgnoreCase("FUNCTIONAL")
						|| name.equalsIgnoreCase("ARCHITECTURE")
						|| name.equalsIgnoreCase("SECURITY")) {
					stage = 2;
				} else if (name.equalsIgnoreCase("TECHNICAL")) {
					stage = 3;
				}

				JJCategory category = new JJCategory();
				category.setName(name);
				category.setCreationDate(new Date());
				category.setDescription("A JJCategory defined as " + name);
				category.setEnabled(true);
				category.setStage(stage);
				jJCategoryService.saveJJCategory(category);
			}

		}

		if (jJProfileService.findAllJJProfiles().isEmpty()) {

			String[] names = { "ProjectManager", "ProductManager", "CEO",
					"CTO", "Tester", "Developer" };
			for (String name : names) {
				JJProfile profile = new JJProfile();
				profile.setName(name);
				jJProfileService.saveJJProfile(profile);
			}

		}

		if (jJRightService.findAllJJRights().isEmpty()) {

			JJCategory businessCategory = jJCategoryService.getCategory(
					"BUSINESS", true);
			JJCategory functionalCategory = jJCategoryService.getCategory(
					"FUNCTIONAL", true);
			JJCategory technicalCategory = jJCategoryService.getCategory(
					"TECHNICAL", true);
			JJCategory architectureCategory = jJCategoryService.getCategory(
					"ARCHITECTURE", true);

			JJProfile projectManagerProfile = jJProfileService
					.getOneProfile("ProjectManager");
			JJProfile productManagerProfile = jJProfileService
					.getOneProfile("ProductManager");
			JJProfile ceoProfile = jJProfileService.getOneProfile("CEO");
			JJProfile ctoProfile = jJProfileService.getOneProfile("CTO");
			JJProfile testerProfile = jJProfileService.getOneProfile("Tester");
			JJProfile developerProfile = jJProfileService
					.getOneProfile("Developer");

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

		if (jJContactService.getContacts(null, true).isEmpty()) {

			JJContact contact = new JJContact();
			contact.setName("janjoon");
			contact.setFirstname("mailer");
			contact.setDescription("This contact is " + contact.getFirstname()
					+ " " + contact.getName());
			contact.setPassword("BeHappy2012");
			contact.setEnabled(true);
			contact.setEmail("janjoon.mailer@gmail.com");
			contact.setCreationDate(new Date());

			jJContactService.saveJJContact(contact);

			List<JJProfile> profiles = new ArrayList<JJProfile>();
			profiles.add(jJProfileService.getOneProfile("ProjectManager"));
			profiles.add(jJProfileService.getOneProfile("ProductManager"));

			for (JJProfile profile : profiles) {
				JJPermission permission = new JJPermission();
				permission.setContact(contact);
				permission.setProfile(profile);
				contact.getPermissions().add(permission);
				jJPermissionService.saveJJPermission(permission);
			}
		}

		if (jJProductService.getProducts(true).isEmpty()) {
			JJContact manager = null;
			List<JJContact> contacts = jJContactService.getContacts(
					"janjoon.mailer@gmail.com", true);

			if (contacts.size() > 0) {
				manager = contacts.get(0);
			}

			JJProduct product;
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
			
			jJProductService.saveJJProduct(product);
			jJVersionService.updateJJVersion(jJVersionList.get(0));
			product.setVersions(versions);
			versions = new HashSet<JJVersion>();
			for (int i = 0; i < 2; i++) {
				product = new JJProduct();
				product.setName("ProductName " + i);
				product.setDescription("ProductDescription " + i);
				product.setCreationDate(new Date());
				product.setEnabled(true);
				product.setExtname("ProductExtName " + i);
				product.setManager(manager);
				int j;
				if (i == 0) {
					j = i + 1;
					jJVersionList.get(j).setProduct(product);
					versions.add(jJVersionList.get(j));
					jJVersionList.get(j + 1).setProduct(product);
					versions.add(jJVersionList.get(j + 1));
					jJProductService.saveJJProduct(product);
					
					jJVersionService.updateJJVersion(jJVersionList.get(j));
					jJVersionService.updateJJVersion(jJVersionList.get(j+1));
					product.setVersions(versions);

				} else {
					jJProductService.saveJJProduct(product);
					JJVersion version;
					version = new JJVersion();
					version.setName("main");
					version.setDescription("VersionDescription Main");
					version.setCreationDate(new Date());
					version.setEnabled(true);
					version.setProduct(product);
					jJVersionService.saveJJVersion(version);

					versions.add(version);
					JJVersion version1 = new JJVersion();
					version1.setName("integ/14.1");
					version1.setDescription("VersionDescription Integ V:14.1");
					version1.setCreationDate(new Date());
					version1.setEnabled(true);
					version1.setProduct(product);
					jJVersionService.saveJJVersion(version1);

					versions.add(version1);
					JJVersion version2 = new JJVersion();
					version2.setName("integ/14.2");
					version2.setDescription("VersionDescription Integ V:14.2");
					version2.setCreationDate(new Date());
					version2.setEnabled(true);
					version2.setProduct(product);
					jJVersionService.saveJJVersion(version2);

					versions.add(version2);
					JJVersion version3 = new JJVersion();
					version3.setName("prod/13.4");
					version3.setDescription("VersionDescription Production V:13.4");
					version3.setCreationDate(new Date());
					version3.setEnabled(true);
					version3.setProduct(product);
					jJVersionService.saveJJVersion(version3);

					versions.add(version3);
					
					jJProductService.saveJJProduct(product);
					product.setVersions(versions);

				}
				
				

			}

		}

		if (jJProjectService.getProjects(true).isEmpty()) {
			JJContact manager = null;
			List<JJContact> contacts = jJContactService.getContacts(
					"janjoon.mailer@gmail.com", true);

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

			List<JJProject> projectList = jJProjectService.getProjects(true);

			List<JJProduct> productList = jJProductService.getProducts(true);
			if (jJRequirementService.findAllJJRequirements().isEmpty()) {
				JJRequirement jJRequirement;
				for (int j = 0; j < projectList.size(); j++) {
					jJRequirement = new JJRequirement();
					jJRequirement.setName("Requirement " + j);
					jJRequirement.setDescription("RequirementDescription " + j);
					jJRequirement.setCreationDate(new Date());
					jJRequirement.setEnabled(true);
					jJRequirement.setProduct(productList.get(j));
					jJRequirement.setProject(projectList.get(j));
					jJRequirementService.saveJJRequirement(jJRequirement);
					JJTask jJTask;
					for (int i = 0; i < 4; i++) {
						jJTask = new JJTask();
						jJTask.setName("TaskName " + i + ":R-" + j);
						jJTask.setDescription("TaskDescription " + i + ":R-"
								+ j);
						jJTask.setCreationDate(new Date());
						jJTask.setEnabled(true);
						jJTask.setRequirement(jJRequirement);
						jJTask.setWorkloadPlanned(10);
						jJTaskService.saveJJTask(jJTask);
					}
				}
			}
		}

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("contextDestroyed-from-ConfigListener");

	}

}
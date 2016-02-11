package com.starit.janjoonweb.ui.security;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.starit.janjoonweb.domain.*;

public class ConfigListener implements ServletContextListener {

	@Autowired
	JJMessageService jJMessageService;

	@Autowired
	JJImportanceService jJImportanceService;

	@Autowired
	JJCompanyService jJCompanyService;

	@Autowired
	JJBugService jJBugService;

	@Autowired
	JJConfigurationService jJConfigurationService;

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

	@Autowired
	JJPhaseService jJPhaseService;

	@Autowired
	private JJWorkflowService jJWorkflowService;

	public void setjJPhaseService(JJPhaseService jJPhaseService) {
		this.jJPhaseService = jJPhaseService;
	}

	public void setjJMessageService(JJMessageService jJMessageService) {
		this.jJMessageService = jJMessageService;
	}

	public void setjJCompanyService(JJCompanyService jJCompanyService) {
		this.jJCompanyService = jJCompanyService;
	}

	public void setjJBugService(JJBugService jJBugService) {
		this.jJBugService = jJBugService;
	}

	public void setjJConfigurationService(
			JJConfigurationService jJConfigurationService) {
		this.jJConfigurationService = jJConfigurationService;
	}

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

	public void setjJImportanceService(JJImportanceService importanceService) {
		this.jJImportanceService = importanceService;
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

	public void setjJWorkflowService(JJWorkflowService jJWorkflowService) {
		this.jJWorkflowService = jJWorkflowService;
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		WebApplicationContextUtils
				.getRequiredWebApplicationContext(sce.getServletContext())
				.getAutowireCapableBeanFactory().autowireBean(this);
		try {
			initApplication(true);
		} catch (FileNotFoundException e) {
			try {
				initApplication(false);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("contextInitialized-from-ConfigListener");

	}

	private void initApplication(boolean index) throws FileNotFoundException,
			URISyntaxException {

		if (index) {
			if (jJCompanyService.findAllJJCompanys().isEmpty()) {

				JJCompany company = new JJCompany();
				company.setName("StarIt");
				company.setDescription(company.getName()
						+ "Message-CompanyDescription");
				company.setCreationDate(new Date());
				company.setEnabled(true);
				company.setCalendar(convertStreamToString(company.getName()));
				jJCompanyService.saveJJCompany(company);
				//
				// company = new JJCompany();
				// company.setName("StarConsulting");
				// company.setDescription(company.getName()
				// + "Message-CompanyDescription");
				// company.setCreationDate(new Date());
				// company.setEnabled(true);
				// company.setCalendar(convertStreamToString(company.getName()));
				// jJCompanyService.saveJJCompany(company);
			}

		}

		 for(JJRequirement req:jJRequirementService.findAllJJRequirements())
		 {
		 if(req.getState() != null)
		 {
		 req.setState(null);
		 jJRequirementService.updateJJRequirement(req);
		 }
		 }

		// for(JJRight right:jJRightService.findAllJJRights())
		// {
		// if(right.getObjet().startsWith("JJ"))
		// {
		// right.setObjet(right.getObjet().substring(2));
		// jJRightService.saveJJRight(right);
		// }
		//
		// }
		//
		// for(JJCriticity criticity:jJCriticityService.findAllJJCriticitys())
		// {
		// if(criticity.getObjet().startsWith("JJ"))
		// {
		// criticity.setObjet(criticity.getObjet().substring(2));
		// jJCriticityService.saveJJCriticity(criticity);
		// }
		// }
		//
		// for(JJStatus status:jJStatusService.findAllJJStatuses())
		// {
		// if(status.getObjet().startsWith("JJ"))
		// {
		// status.setObjet(status.getObjet().substring(2));
		// jJStatusService.saveJJStatus(status);
		// }
		// }
		//

		if (jJImportanceService.findAllJJImportances().isEmpty()) {
			String[] names = { "High", "Medium", "Low" };
			Integer i = 3;

			for (String name : names) {
				JJImportance importance = new JJImportance();
				importance.setCreationDate(new Date());
				importance.setEnabled(true);
				importance.setObjet("Bug");
				importance.setLevelImportance(i);
				importance.setName(name);
				importance
						.setDescription(name + " :Bug Importance Description");
				jJImportanceService.saveJJImportance(importance);
				i--;

			}
		}

		if (jJCriticityService.getCriticities("Message", true).isEmpty()) {

			String[] names = { "ALERT", "INFO" };

			for (String name : names) {
				JJCriticity criticity = new JJCriticity();
				criticity.setObjet("Message");
				criticity.setName(name);
				criticity.setDescription(name + "Message-CriticityDescription");
				criticity.setCreationDate(new Date());
				criticity.setEnabled(true);
				criticity.setLevelCriticity(name.length());
				jJCriticityService.saveJJCriticity(criticity);
			}
		}

		if (jJCriticityService.getCriticities("Bug", true).isEmpty()) {

			String[] names = { "ALERT", "INFO" };

			for (String name : names) {
				JJCriticity criticity = new JJCriticity();
				criticity.setObjet("Bug");
				criticity.setName(name);
				criticity.setDescription(name + "Bug-CriticityDescription");
				criticity.setCreationDate(new Date());
				criticity.setEnabled(true);
				criticity.setLevelCriticity(name.length());
				jJCriticityService.saveJJCriticity(criticity);
			}
		}

		if (jJConfigurationService.findAllJJConfigurations().isEmpty()) {
			JJConfiguration configuration = new JJConfiguration();
			configuration.setName("ConfigurationManager");
			configuration.setCreationDate(new Date());
			configuration.setDescription("Test Configuration Manager");
			configuration.setParam("git");
			configuration.setVal("https://github.com/janjoon/");
			configuration.setEnabled(true);
			jJConfigurationService.saveJJConfiguration(configuration);

			configuration = new JJConfiguration();
			configuration.setName("AdminUserDialog");
			configuration.setCreationDate(new Date());
			configuration.setDescription("Test AdminUserDialog");
			configuration.setParam("admin.user.create.saveandclose");
			configuration.setVal("true");
			configuration.setEnabled(true);
			jJConfigurationService.saveJJConfiguration(configuration);

			configuration = new JJConfiguration();
			configuration.setName("RequirementDialog");
			configuration.setCreationDate(new Date());
			configuration.setDescription("Test RequirementDialog");
			configuration.setParam("specs.requirement.create.saveandclose");
			configuration.setVal("true");
			configuration.setEnabled(true);
			jJConfigurationService.saveJJConfiguration(configuration);

			configuration = new JJConfiguration();
			configuration.setName("TestcaseDialog");
			configuration.setCreationDate(new Date());
			configuration.setDescription("Test TestcaseDialog");
			configuration.setParam("testcases.create.saveandclose");
			configuration.setVal("true");
			configuration.setEnabled(true);
			jJConfigurationService.saveJJConfiguration(configuration);

			configuration = new JJConfiguration();
			configuration.setName("ChapterDialog");
			configuration.setCreationDate(new Date());
			configuration.setDescription("Test ChapterDialog");
			configuration.setParam("chapter.create.saveandclose");
			configuration.setVal("true");
			configuration.setEnabled(true);
			jJConfigurationService.saveJJConfiguration(configuration);

			configuration = new JJConfiguration();
			configuration.setName("ProductDialog");
			configuration.setCreationDate(new Date());
			configuration.setDescription("Test ProductDialog");
			configuration.setParam("product.create.saveandclose");
			configuration.setVal("true");
			configuration.setEnabled(true);
			jJConfigurationService.saveJJConfiguration(configuration);

			configuration = new JJConfiguration();
			configuration.setName("ProjectDialog");
			configuration.setCreationDate(new Date());
			configuration.setDescription("Test ProjectDialog");
			configuration.setParam("project.create.saveandclose");
			configuration.setVal("true");
			configuration.setEnabled(true);
			jJConfigurationService.saveJJConfiguration(configuration);

			configuration = new JJConfiguration();
			configuration.setName("ProfileDialog");
			configuration.setCreationDate(new Date());
			configuration.setDescription("Test ProfileDialog");
			configuration.setParam("profile.create.saveandclose");
			configuration.setVal("true");
			configuration.setEnabled(true);
			jJConfigurationService.saveJJConfiguration(configuration);

			configuration = new JJConfiguration();
			configuration.setName("CategoryDialog");
			configuration.setCreationDate(new Date());
			configuration.setDescription("Test CategoryDialog");
			configuration.setParam("category.create.saveandclose");
			configuration.setVal("true");
			configuration.setEnabled(true);
			jJConfigurationService.saveJJConfiguration(configuration);

		}

		// if (jJBuildService.getBuilds(null, false, true).isEmpty()) {
		// JJBuild build;
		// for (int i = 0; i < 4; i++) {
		// build = new JJBuild();
		// build.setName("Build 0." + i);
		// build.setCreationDate(new Date());
		// build.setDescription("Build 0." + i + " Description");
		// build.setEnabled(true);
		// jJBuildService.saveJJBuild(build);
		// }
		// }
		//
		// if (jJSprintService.getSprints(null, true).isEmpty()) {
		// JJSprint sprint;
		// for (int i = 0; i < 4; i++) {
		// sprint = new JJSprint();
		// sprint.setName("Sprint " + i);
		// sprint.setCreationDate(new Date());
		// sprint.setDescription("Sprint " + i + " Description");
		// sprint.setEnabled(true);
		// jJSprintService.saveJJSprint(sprint);
		// }
		// }

		String[] objects = { "Requirement", "Bug", "Message", "Task", "Build",
				"TaskType", "RequirementState" };

		for (String object : objects) {

			List<String> names = new ArrayList<String>();

			if (jJStatusService.getStatus(object, true,
					new ArrayList<String>(), false).isEmpty()) {

				if (object.equalsIgnoreCase("RequirementState")) {
					names.add("Specified");
					names.add("UnLinked");
					names.add("InProgress");
					names.add("Finished");
					names.add("InTesting");

				} else if (object.equalsIgnoreCase("TaskType")) {
					names.add("developper");
					names.add("specifier");

				} else if (object.equalsIgnoreCase("Requirement")) {
					names.add("NEW");
					names.add("DELETED");
					names.add("MODIFIED");
					names.add("RELEASED");
					names.add("FAILED");
					names.add("PASSED");
					names.add("CANCELED");
					names.add("RUNNING");

				} else if (object.equalsIgnoreCase("Bug")) {
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
				} else if (object.equalsIgnoreCase("Message")) {
					names.add("NEW");
					names.add("CLOSED");
				} else if (object.equalsIgnoreCase("Task")) {
					names.add("TODO");
					names.add("DONE");
					names.add("IN PROGRESS");
				} else if (object.equalsIgnoreCase("Build")) {
					names.add("ALPHA");
					names.add("BETA");
					names.add("RELEASE");
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

		if (jJStatusService.getOneStatus("Any", "*", true) == null) {
			JJStatus status = new JJStatus();
			status.setObjet("*");
			status.setName("Any");
			status.setCreationDate(new Date());
			status.setDescription("A JJStatus defined as " + "Any");
			status.setEnabled(true);
			jJStatusService.saveJJStatus(status);
		}
		// initBasiqueWorkFlows
		if (jJWorkflowService.findAllJJWorkflows().isEmpty()) {

			JJWorkflow workFlow = new JJWorkflow();
			workFlow.setCreationDate(new Date());
			workFlow.setEnabled(true);
			workFlow.setActionWorkflow("createMessageWorkFlow");
			workFlow.setName(workFlow.getActionWorkflow());
			workFlow.setDescription(workFlow.getActionWorkflow());
			workFlow.setObjet("Bug");
			workFlow.setSource(jJStatusService.getOneStatus("Any", "*", true));
			workFlow.setTarget(jJStatusService.getOneStatus("Any", "*", true));
			jJWorkflowService.saveJJWorkflow(workFlow);

			workFlow = new JJWorkflow();
			workFlow.setCreationDate(new Date());
			workFlow.setEnabled(true);
			workFlow.setActionWorkflow("createMessageWorkFlow");
			workFlow.setName(workFlow.getActionWorkflow());
			workFlow.setDescription(workFlow.getActionWorkflow());
			workFlow.setObjet("Requirement");
			workFlow.setSource(jJStatusService.getOneStatus("Any", "*", true));
			workFlow.setTarget(jJStatusService.getOneStatus("Any", "*", true));
			jJWorkflowService.saveJJWorkflow(workFlow);

			workFlow = new JJWorkflow();
			workFlow.setCreationDate(new Date());
			workFlow.setEnabled(true);
			workFlow.setActionWorkflow("setRequirementToREALEASEDWorkFlow");
			workFlow.setName(workFlow.getActionWorkflow());
			workFlow.setDescription(workFlow.getActionWorkflow());
			workFlow.setObjet("Task");
			workFlow.setSource(jJStatusService.getOneStatus("Any", "*", true));
			workFlow.setTarget(jJStatusService.getOneStatus("IN PROGRESS",
					"task", true));
			jJWorkflowService.saveJJWorkflow(workFlow);

			workFlow = new JJWorkflow();
			workFlow.setCreationDate(new Date());
			workFlow.setEnabled(true);
			workFlow.setActionWorkflow("sendMailWorkFlow");
			workFlow.setName(workFlow.getActionWorkflow());
			workFlow.setDescription(workFlow.getActionWorkflow());
			workFlow.setObjet("Task");
			workFlow.setSource(jJStatusService.getOneStatus("Any", "*", true));
			workFlow.setTarget(jJStatusService.getOneStatus("Done", "task",
					true));
			jJWorkflowService.saveJJWorkflow(workFlow);

		}

		if (jJPhaseService.findAllJJPhases().isEmpty()) {

			JJPhase phase = new JJPhase();
			phase.setName("BETA");
			phase.setDescription("BETA");
			phase.setEnabled(true);
			phase.setCreationDate(new Date());
			phase.setLevelPhase(2);
			jJPhaseService.saveJJPhase(phase);
			phase = new JJPhase();
			phase.setName("ALPHA");
			phase.setDescription("ALPHA");
			phase.setEnabled(true);
			phase.setCreationDate(new Date());
			phase.setLevelPhase(1);
			jJPhaseService.saveJJPhase(phase);
			phase = new JJPhase();
			phase.setName("RELEASE");
			phase.setDescription("RELEASE");
			phase.setEnabled(true);
			phase.setCreationDate(new Date());
			phase.setLevelPhase(3);
			jJPhaseService.saveJJPhase(phase);
		}

		// if (jJBugService.getBugs(null, null, null, true, true).isEmpty()) {
		// List<JJStatus> statuses = jJStatusService.getStatus("Bug", true,
		// null, true);
		// List<JJCriticity> criticities = jJCriticityService.getCriticities(
		// "Bug", true);
		// JJCriticity crit = null;
		// int iCrit = 0;
		// for (JJStatus stat : statuses) {
		// JJBug bug;
		// crit = criticities.get(iCrit);
		// bug = new JJBug();
		// bug.setName(stat.getName() + "-bug : " + crit.getName());
		// bug.setDescription(stat.getName() + "-bugDescription : "
		// + crit.getName());
		// bug.setCreationDate(new Date());
		// bug.setEnabled(true);
		// bug.setStatus(stat);
		// bug.setCriticity(crit);
		// jJBugService.saveJJBug(bug);
		// iCrit = (iCrit + 1) % 2;
		// crit = criticities.get(iCrit);
		// bug = new JJBug();
		// bug.setName(stat.getName() + "-bug : " + crit.getName());
		// bug.setDescription(stat.getName() + "-bugDescription : "
		// + crit.getName());
		// bug.setCreationDate(new Date());
		// bug.setEnabled(true);
		// bug.setStatus(stat);
		// bug.setCriticity(crit);
		// jJBugService.saveJJBug(bug);
		//
		// }
		//
		// }

		if (jJCategoryService.getCategories(null, false, true, true, null)
				.isEmpty()) {
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
					"CTO", "Tester", "Developer", "CustomProfile" };
			for (String name : names) {
				JJProfile profile = new JJProfile();
				profile.setName(name);
				profile.setEnabled(true);
				jJProfileService.saveJJProfile(profile);
			}

		}

		if (jJRightService.findAllJJRights().isEmpty()) {

			JJCategory businessCategory = jJCategoryService.getCategory(
					"BUSINESS", null, true);
			JJCategory functionalCategory = jJCategoryService.getCategory(
					"FUNCTIONAL", null, true);
			JJCategory technicalCategory = jJCategoryService.getCategory(
					"TECHNICAL", null, true);
			JJCategory architectureCategory = jJCategoryService.getCategory(
					"ARCHITECTURE", null, true);

			JJProfile projectManagerProfile = jJProfileService.getOneProfile(
					"ProjectManager", null, true);
			JJProfile productManagerProfile = jJProfileService.getOneProfile(
					"ProductManager", null, true);
			JJProfile ceoProfile = jJProfileService.getOneProfile("CEO", null,
					true);
			JJProfile ctoProfile = jJProfileService.getOneProfile("CTO", null,
					true);
			JJProfile testerProfile = jJProfileService.getOneProfile("Tester",
					null, true);
			JJProfile developerProfile = jJProfileService.getOneProfile(
					"Developer", null, true);
			JJProfile customProfile = jJProfileService.getOneProfile(
					"CustomProfile", null, true);

			// Project Manager Profile
			JJRight right = new JJRight();
			right.setObjet("Project");
			right.setR(true);
			right.setW(true);
			right.setX(true);
			right.setEnabled(true);
			right.setProfile(projectManagerProfile);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Requirement");
			right.setCategory(businessCategory);
			right.setR(true);
			right.setW(true);
			right.setX(true);
			right.setEnabled(true);
			right.setProfile(projectManagerProfile);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Requirement");
			right.setCategory(functionalCategory);
			right.setR(true);
			right.setW(false);
			right.setX(true);
			right.setEnabled(true);
			right.setProfile(projectManagerProfile);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Requirement");
			right.setCategory(technicalCategory);
			right.setR(true);
			right.setW(false);
			right.setX(false);
			right.setEnabled(true);
			right.setProfile(projectManagerProfile);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Requirement");
			right.setCategory(architectureCategory);
			right.setR(true);
			right.setW(false);
			right.setX(false);
			right.setEnabled(true);
			right.setProfile(projectManagerProfile);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Planning");
			right.setCategory(architectureCategory);
			right.setR(true);
			right.setW(true);
			right.setX(false);
			right.setEnabled(true);
			right.setProfile(projectManagerProfile);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Planning");
			right.setCategory(technicalCategory);
			right.setR(true);
			right.setW(true);
			right.setX(true);
			right.setEnabled(true);
			right.setProfile(projectManagerProfile);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Test");
			right.setR(true);
			right.setW(false);
			right.setX(false);
			right.setEnabled(true);
			right.setProfile(projectManagerProfile);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Product");
			right.setR(true);
			right.setW(false);
			right.setX(false);
			right.setEnabled(true);
			right.setProfile(projectManagerProfile);

			jJRightService.saveJJRight(right);

			// Product Manager Profile

			right = new JJRight();
			right.setObjet("Product");
			right.setCategory(functionalCategory);
			right.setR(true);
			right.setW(true);
			right.setX(false);
			right.setEnabled(true);
			right.setProfile(productManagerProfile);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Requirement");
			right.setCategory(businessCategory);
			right.setR(true);
			right.setW(false);
			right.setX(true);
			right.setEnabled(true);
			right.setProfile(productManagerProfile);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Requirement");
			right.setCategory(functionalCategory);
			right.setR(true);
			right.setW(true);
			right.setX(true);
			right.setEnabled(true);
			right.setProfile(productManagerProfile);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Requirement");
			right.setCategory(technicalCategory);
			right.setR(true);
			right.setW(true);
			right.setX(true);
			right.setEnabled(true);
			right.setProfile(productManagerProfile);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Requirement");
			right.setCategory(architectureCategory);
			right.setR(true);
			right.setW(true);
			right.setX(true);
			right.setEnabled(true);
			right.setProfile(productManagerProfile);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Planning");
			right.setCategory(architectureCategory);
			right.setR(true);
			right.setW(true);
			right.setX(true);
			right.setEnabled(true);
			right.setProfile(productManagerProfile);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Planning");
			right.setCategory(technicalCategory);
			right.setR(true);
			right.setW(true);
			right.setX(true);
			right.setEnabled(true);
			right.setProfile(productManagerProfile);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Test");
			right.setR(true);
			right.setW(true);
			right.setX(false);
			right.setEnabled(true);
			right.setProfile(productManagerProfile);

			jJRightService.saveJJRight(right);

			// CEO Profile
			right = new JJRight();
			right.setObjet("*");
			right.setR(true);
			right.setW(true);
			right.setX(false);
			right.setEnabled(true);
			right.setProfile(ceoProfile);

			jJRightService.saveJJRight(right);

			// cutomProfile rights
			right = new JJRight();
			right.setObjet("Build");
			right.setR(false);
			right.setW(false);
			right.setX(false);
			right.setEnabled(true);
			right.setProfile(customProfile);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Requirement");
			right.setR(false);
			right.setW(false);
			right.setX(false);
			right.setEnabled(true);
			right.setProfile(customProfile);

			jJRightService.saveJJRight(right);

			// CEO Profile
			right = new JJRight();
			right.setObjet("*");
			right.setR(true);
			right.setW(true);
			right.setX(true);
			right.setEnabled(true);
			right.setProfile(ctoProfile);

			jJRightService.saveJJRight(right);

			// Tester Profile
			right = new JJRight();
			right.setObjet("Test");
			right.setR(true);
			right.setW(true);
			right.setX(true);
			right.setEnabled(true);
			right.setProfile(testerProfile);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Requirement");
			right.setR(true);
			right.setW(false);
			right.setX(true);
			right.setEnabled(true);
			right.setProfile(testerProfile);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Product");
			right.setCategory(technicalCategory);
			right.setR(false);
			right.setW(false);
			right.setX(true);
			right.setEnabled(true);
			right.setProfile(testerProfile);

			jJRightService.saveJJRight(right);

			// Developer Profile
			right = new JJRight();
			right.setObjet("Test");
			right.setR(true);
			right.setW(true);
			right.setX(false);
			right.setEnabled(true);
			right.setProfile(developerProfile);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Requirement");
			right.setR(true);
			right.setW(false);
			right.setX(false);
			right.setEnabled(true);
			right.setProfile(developerProfile);

			jJRightService.saveJJRight(right);

			right = new JJRight();
			right.setObjet("Product");
			right.setCategory(technicalCategory);
			right.setR(true);
			right.setW(true);
			right.setX(true);
			right.setEnabled(true);
			right.setProfile(developerProfile);

			jJRightService.saveJJRight(right);

		}

		if (jJContactService.findAllJJContacts().isEmpty()) {

			PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(11);
			JJContact contact = new JJContact();
			contact.setName("janjoon");
			contact.setFirstname("mailer");
			contact.setDescription("This contact is " + contact.getFirstname()
					+ " " + contact.getName());
			contact.setPassword(passwordEncoder.encode("BeHappy2012"));
			contact.setEnabled(true);
			contact.setEmail("janjoon.mailer@gmail.com");
			contact.setCreationDate(new Date());
			contact.setCompany(jJCompanyService.getActifCompanies().get(0));

			jJContactService.saveJJContact(contact);

			List<JJProfile> profiles = new ArrayList<JJProfile>();
			// profiles.add(jJProfileService.getOneProfile("ProjectManager",
			// true));
			// profiles.add(jJProfileService.getOneProfile("ProductManager",
			// true));
			profiles.add(jJProfileService.getOneProfile("CTO", null, true));

			for (JJProfile profile : profiles) {
				JJPermission permission = new JJPermission();
				permission.setContact(contact);
				permission.setProfile(profile);
				permission.setEnabled(true);
				contact.getPermissions().add(permission);
				jJPermissionService.saveJJPermission(permission);
			}

			// contact = new JJContact();
			// contact.setName("Thierry");
			// contact.setFirstname("Thierry");
			// contact.setPassword(passwordEncoder.encode("BeHappy2012"));
			// contact.setDescription("This contact is " +
			// contact.getFirstname()
			// + " " + contact.getName());
			// contact.setEnabled(true);
			// contact.setEmail("thierry@startit.fr");
			// contact.setCreationDate(new Date());
			// contact.setCompany(jJCompanyService.getActifCompanies().get(0));
			// jJContactService.saveJJContact(contact);
			//
			// JJProfile profile =
			// jJProfileService.getOneProfile("CustomProfile",
			// true);
			// JJPermission permission = new JJPermission();
			// permission.setContact(contact);
			// permission.setProfile(profile);
			// permission.setEnabled(true);
			// contact.getPermissions().add(permission);
			// jJPermissionService.saveJJPermission(permission);

		}

		// if (jJProductService.getProducts(true).isEmpty()) {
		// JJContact manager = null;
		// JJContact contact = jJContactService.getContactByEmail(
		// "janjoon.mailer@gmail.com", true);
		//
		// if (contact != null) {
		// manager = contact;
		// }
		//
		// Set<JJVersion> versions = new HashSet<JJVersion>();
		//
		// JJProduct product = new JJProduct();
		// product.setName("ProductName 1");
		// product.setDescription("ProductDescription 1");
		// product.setCreationDate(new Date());
		// product.setEnabled(true);
		// product.setExtname("ProductExtName 1");
		// product.setManager(manager);
		//
		// jJProductService.saveJJProduct(product);
		// JJVersion version;
		// version = new JJVersion();
		// version.setName("main");
		// version.setDescription("VersionDescription Main");
		// version.setCreationDate(new Date());
		// version.setEnabled(true);
		// version.setProduct(product);
		// jJVersionService.saveJJVersion(version);
		//
		// versions.add(version);
		// JJVersion version1 = new JJVersion();
		// version1.setName("integ/14.1");
		// version1.setDescription("VersionDescription Integ V:14.1");
		// version1.setCreationDate(new Date());
		// version1.setEnabled(true);
		// version1.setProduct(product);
		// jJVersionService.saveJJVersion(version1);
		//
		// versions.add(version1);
		// JJVersion version2 = new JJVersion();
		// version2.setName("integ/14.2");
		// version2.setDescription("VersionDescription Integ V:14.2");
		// version2.setCreationDate(new Date());
		// version2.setEnabled(true);
		// version2.setProduct(product);
		// jJVersionService.saveJJVersion(version2);
		//
		// versions.add(version2);
		// JJVersion version3 = new JJVersion();
		// version3.setName("prod/13.4");
		// version3.setDescription("VersionDescription Production V:13.4");
		// version3.setCreationDate(new Date());
		// version3.setEnabled(true);
		// version3.setProduct(product);
		// jJVersionService.saveJJVersion(version3);
		//
		// versions.add(version3);
		//
		// jJProductService.saveJJProduct(product);
		// product.setVersions(versions);
		//
		// }
		// JJContact contact = jJContactService.getContactByEmail(
		// "janjoon.mailer@gmail.com", true);
		// JJContact manager = null;
		// if (contact != null) {
		// manager = contact;
		// }

		// if (jJProjectService.getProjects(true).isEmpty()) {
		//
		// JJProject project;
		//
		// for (int i = 0; i < 2; i++) {
		// project = new JJProject();
		// project.setName("ProjectName " + i);
		// project.setDescription("ProjectDescription " + i);
		// project.setCreationDate(new Date());
		// project.setEnabled(true);
		// project.setManager(manager);
		//
		// jJProjectService.saveJJProject(project);
		// }
		// }

		// List<JJProject> projectList = jJProjectService.getProjects(true);
		// //
		// List<JJProduct> productList = jJProductService.getProducts(true);
		// if (jJRequirementService.findAllJJRequirements().isEmpty()) {
		// List<JJStatus> status = jJStatusService.getStatus("Task", true,
		// null, true);
		// JJSprint s = jJSprintService.getSprints(true).get(0);
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
		// jJTask.setSprint(s);
		// jJTask.setStatus(status.get(i % 3));
		// jJTaskService.saveJJTask(jJTask);
		// }
		// }
		// }

		JJCompany company = jJCompanyService.getCompanyByName("StarIt");
		if (company == null)
			company = jJCompanyService.getActifCompanies().get(0);
		jJMessageService.updateAll(company);

		//
		// int i = 0;
		// while (i < productList.size()) {
		//
		// for (int j = 0; j < 5; j++) {
		//
		// JJMessage mes = new JJMessage();
		// mes.setName("mes : " + j + "/" + i);
		// mes.setCreatedBy(manager);
		// // mes.setContact(manager);
		// mes.setProduct(productList.get(i));
		// mes.setProject(projectList.get(i));
		// mes.setDescription("mesDescription : " + j + "/" + i);
		// mes.setCreationDate(new Date());
		// mes.setEnabled(true);
		// mes.setMessage("message tttttt" + j + "/" + i);
		// jJMessageService.saveJJMessage(mes);
		//
		// }
		// i++;
		//
		// }
		// }
	}

	// public String setCompanyCalendar(String company)
	// {
	// StringWriter writer = new StringWriter();
	// IOUtils.copy(ClassLoader.getSystemResourceAsStream(""), writer,
	// encoding);
	// String theString = writer.toString();
	// return "";
	// }

	public String convertStreamToString(String company)
			throws FileNotFoundException, URISyntaxException {

		// InputStream
		// is=ConfigListener.class.getResourceAsStream("/resources/Calandar"+
		// company);

		FileInputStream inputStream = new FileInputStream(this.getClass()
				.getResource("/Calendar" + company + ".properties").getFile());
		Scanner s = new Scanner(inputStream).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("contextDestroyed-from-ConfigListener");

	}

}

package com.starit.janjoonweb.ui.mb.util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.itextpdf.text.Element;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.starit.janjoonweb.domain.JJAuditLog;
import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJBugService;
import com.starit.janjoonweb.domain.JJBuild;
import com.starit.janjoonweb.domain.JJBuildService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJContactService;
import com.starit.janjoonweb.domain.JJCriticity;
import com.starit.janjoonweb.domain.JJCriticityService;
import com.starit.janjoonweb.domain.JJMessage;
import com.starit.janjoonweb.domain.JJMessageService;
import com.starit.janjoonweb.domain.JJPermissionService;
import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJRequirementService;
import com.starit.janjoonweb.domain.JJStatus;
import com.starit.janjoonweb.domain.JJStatusService;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJTaskService;
import com.starit.janjoonweb.domain.JJTestcase;
import com.starit.janjoonweb.domain.JJTestcaseService;
import com.starit.janjoonweb.domain.JJWorkflow;
import com.starit.janjoonweb.ui.mb.JJAuditLogBean;
import com.starit.janjoonweb.ui.mb.JJBugBean;
import com.starit.janjoonweb.ui.mb.JJRequirementBean;
import com.starit.janjoonweb.ui.mb.JJTaskBean;
import com.starit.janjoonweb.ui.mb.JJTestcaseBean;
import com.starit.janjoonweb.ui.mb.LoginBean;
import com.starit.janjoonweb.ui.mb.RequirementBean;

@SuppressWarnings("deprecation")
@Configurable
public class WorkFlowsActions {

	@Autowired
	private JJBugService jJBugService;

	@Autowired
	private JJBuildService jJBuildService;

	@Autowired
	private JJRequirementService jJRequirementService;

	@Autowired
	private JJCriticityService jJCriticityService;

	@Autowired
	private JJMessageService jJMessageService;

	@Autowired
	private JJTaskService jJTaskService;

	@Autowired
	private JJTestcaseService jJTestcaseService;

	@Autowired
	private JJStatusService jJStatusService;

	// @Autowired
	// private JJWorkflowService jJWorkflowService;

	@Autowired
	private JJPermissionService jJPermissionService;

	@Autowired
	private JJContactService jJContactService;

	private MailingService mailingService;

	private List<JJWorkflow> workFlows;
	private Object object;

	public void setjJBugService(JJBugService jJBugService) {
		this.jJBugService = jJBugService;
	}

	public void setjJRequirementService(
			JJRequirementService jJRequirementService) {
		this.jJRequirementService = jJRequirementService;
	}

	public void setjJBuildService(JJBuildService jJBuildService) {
		this.jJBuildService = jJBuildService;
	}

	public void setjJMessageService(JJMessageService jJMessageService) {
		this.jJMessageService = jJMessageService;
	}

	public void setjJCriticityService(JJCriticityService jJCriticityService) {
		this.jJCriticityService = jJCriticityService;
	}

	public void setjJTaskService(JJTaskService jJTaskService) {
		this.jJTaskService = jJTaskService;
	}

	// public void setjJProjectService(JJProjectService jJProjectService) {
	// this.jJProjectService = jJProjectService;
	// }

	public void setjJTestcaseService(JJTestcaseService jJTestcaseService) {
		this.jJTestcaseService = jJTestcaseService;
	}

	public void setjJStatusService(JJStatusService jJStatusService) {
		this.jJStatusService = jJStatusService;
	}

	// public void setjJWorkflowService(JJWorkflowService jJWorkflowService) {
	// this.jJWorkflowService = jJWorkflowService;
	// }

	public void setjJPermissionService(
			JJPermissionService jJPermissionService) {
		this.jJPermissionService = jJPermissionService;
	}

	public void setjJContactService(JJContactService jJContactService) {
		this.jJContactService = jJContactService;
	}

	public MailingService getMailingService() {
		return mailingService;
	}

	public void setMailingService(MailingService mailingService) {
		this.mailingService = mailingService;
	}

	public List<JJWorkflow> getWorkFlows() {
		return workFlows;
	}

	public void setWorkFlows(List<JJWorkflow> workFlows) {
		this.workFlows = workFlows;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public void createMessageWorkFlow(Object object) {

		System.err.println("createMessageWorkFlow Successufuly Executed");
		if (object instanceof JJRequirement) {
			JJRequirement requirement = (JJRequirement) object;
			if (requirement.getId() != null) {
				JJRequirement oldRequirement = jJRequirementService
						.findJJRequirement(requirement.getId());
				JJContact contact = ((LoginBean) LoginBean
						.findBean("loginBean")).getContact();
				if (requirement.getStatus() != null
						&& oldRequirement.getStatus() != null && !oldRequirement
								.getStatus().equals(requirement.getStatus())) {
					JJCriticity criticity = jJCriticityService
							.getCriticityByName("INFO", true);
					JJStatus status = jJStatusService.getOneStatus("NEW",
							"Message", true);

					JJMessage mes = new JJMessage();
					mes.setName(
							"Requirement :"
									+ requirement.getName().substring(0,
											Math.min(70,
													requirement.getName()
															.length()))
							+ " status changed");
					mes.setCreatedBy(contact);
					mes.setCriticity(criticity);
					mes.setStatus(status);
					mes.setCompany(contact.getCompany());
					mes.setProject(requirement.getProject());
					mes.setProduct(requirement.getProduct());
					mes.setRequirement(requirement);
					mes.setDescription("Requirement :" + requirement.getName()
							+ " status changed from "
							+ oldRequirement.getStatus().getName() + " to "
							+ requirement.getStatus().getName());
					mes.setCreationDate(new Date());
					mes.setEnabled(true);
					mes.setMessage("Requirement :" + requirement.getName()
							+ " status changed from "
							+ oldRequirement.getStatus().getName() + " to "
							+ requirement.getStatus().getName());
					jJMessageService.saveJJMessage(mes);
					((LoginBean) LoginBean.findBean("loginBean"))
							.setMessageCount(null);
				}
			}

		} else if (object instanceof JJBug) {
			JJBug bug = (JJBug) object;
			if (bug.getId() != null) {
				JJBug oldBug = jJBugService.findJJBug(bug.getId());
				JJContact contact = ((LoginBean) LoginBean
						.findBean("loginBean")).getContact();
				if (bug.getStatus() != null && oldBug.getStatus() != null
						&& !oldBug.getStatus().equals(bug.getStatus())) {
					JJCriticity criticity = jJCriticityService
							.getCriticityByName("INFO", true);
					JJStatus status = jJStatusService.getOneStatus("NEW",
							"Message", true);

					JJMessage mes = new JJMessage();
					mes.setName("Bug :"
							+ bug.getName().substring(0,
									Math.min(70, bug.getName().length()))
							+ "status changed");
					mes.setCreatedBy(contact);
					mes.setStatus(status);
					mes.setCriticity(criticity);
					mes.setCompany(contact.getCompany());
					mes.setProduct(bug.getProduct());
					mes.setProject(bug.getProject());
					mes.setBug(bug);
					mes.setDescription(
							"Bug :" + bug.getName() + " status changed from "
									+ oldBug.getStatus().getName() + " to "
									+ bug.getStatus().getName());
					mes.setCreationDate(new Date());
					mes.setEnabled(true);
					mes.setMessage(
							"Bug :" + bug.getName() + " status changed from "
									+ oldBug.getStatus().getName() + " to "
									+ bug.getStatus().getName());
					jJMessageService.saveJJMessage(mes);
					((LoginBean) LoginBean.findBean("loginBean"))
							.setMessageCount(null);
				}
			}

		} else if (object instanceof JJTask) {
			JJTask task = (JJTask) object;
			if (task.getId() != null) {
				JJTask oldTask = jJTaskService.findJJTask(task.getId());
				JJContact contact = ((LoginBean) LoginBean
						.findBean("loginBean")).getContact();
				if (task.getStatus() != null && oldTask.getStatus() != null
						&& !oldTask.getStatus().equals(task.getStatus())) {
					JJCriticity criticity = jJCriticityService
							.getCriticityByName("INFO", true);
					JJStatus status = jJStatusService.getOneStatus("NEW",
							"Message", true);

					JJMessage mes = new JJMessage();
					mes.setName("Task :"
							+ task.getName().substring(0,
									Math.min(70, task.getName().length()))
							+ "status changed");
					mes.setCreatedBy(contact);
					mes.setStatus(status);
					mes.setCriticity(criticity);
					mes.setCompany(contact.getCompany());
					mes.setProduct(task.getProduct());
					mes.setProject(task.getProject());
					mes.setTask(task);
					mes.setDescription(
							"Task :" + task.getName() + " status changed from "
									+ oldTask.getStatus().getName() + " to "
									+ task.getStatus().getName());
					mes.setCreationDate(new Date());
					mes.setEnabled(true);
					mes.setMessage(
							"Task :" + task.getName() + " status changed from "
									+ oldTask.getStatus().getName() + " to "
									+ task.getStatus().getName());
					jJMessageService.saveJJMessage(mes);
					((LoginBean) LoginBean.findBean("loginBean"))
							.setMessageCount(null);
				}
			}

		} else if (object instanceof JJBuild) {
			JJBuild build = (JJBuild) object;
			if (build.getId() != null) {
				JJBuild oldbuild = jJBuildService.findJJBuild(build.getId());
				JJContact contact = ((LoginBean) LoginBean
						.findBean("loginBean")).getContact();
				if (build.getStatus() != null && oldbuild.getStatus() != null
						&& !oldbuild.getStatus().equals(build.getStatus())) {
					JJCriticity criticity = jJCriticityService
							.getCriticityByName("INFO", true);
					JJStatus status = jJStatusService.getOneStatus("NEW",
							"Message", true);

					JJMessage mes = new JJMessage();
					mes.setName(
							"Build :"
									+ build.getName().substring(0,
											Math.min(70,
													build.getName().length()))
							+ "status changed");
					mes.setCreatedBy(contact);
					mes.setStatus(status);
					mes.setCriticity(criticity);
					mes.setCompany(contact.getCompany());
					mes.setProduct(build.getVersion().getProduct());
					mes.setProject(LoginBean.getProject());
					mes.setBuild(build);
					mes.setDescription("Build :" + build.getName()
							+ " status changed from "
							+ oldbuild.getStatus().getName() + " to "
							+ build.getStatus().getName());
					mes.setCreationDate(new Date());
					mes.setEnabled(true);
					mes.setMessage("Build :" + build.getName()
							+ " status changed from "
							+ oldbuild.getStatus().getName() + " to "
							+ build.getStatus().getName());
					jJMessageService.saveJJMessage(mes);
					((LoginBean) LoginBean.findBean("loginBean"))
							.setMessageCount(null);
				}
			}

		}
	}

	public void loggingWorkFlow(Object objet) {

		System.err.println("loggingWorkFlow Successufuly Executed");

		String longText = null;
		String obj = null;
		if (objet instanceof JJBug) {
			JJBug bug = (JJBug) objet;
			obj = "Bug";
			String afterStatus = null;

			if (bug.getStatus() != null)
				afterStatus = bug.getStatus().getName();
			else
				afterStatus = "Null";

			if (bug.getId() != null) {
				JJBug oldBug = jJBugService.findJJBug(bug.getId());
				String oldStatus = null;
				if (oldBug.getStatus() != null)
					oldStatus = oldBug.getStatus().getName();
				else
					oldStatus = "Null";

				longText = obj + " Status has changed from  :" + oldStatus
						+ " to " + afterStatus;
			} else {
				longText = "new " + obj + " creation with the status :"
						+ afterStatus;
			}

		} else if (objet instanceof JJTask) {
			JJTask task = (JJTask) objet;
			obj = "Task";
			String afterStatus = null;

			if (task.getStatus() != null)
				afterStatus = task.getStatus().getName();
			else
				afterStatus = "Null";
			if (task.getId() != null) {
				JJTask oldTask = jJTaskService.findJJTask(task.getId());
				String oldStatus = null;
				if (oldTask.getStatus() != null)
					oldStatus = oldTask.getStatus().getName();
				else
					oldStatus = "Null";
				longText = obj + " Status has changed from  :" + oldStatus
						+ " to " + afterStatus;
			} else {
				longText = "new " + obj + " creation with the status :"
						+ afterStatus;
			}

		} else if (objet instanceof JJRequirement) {

			JJRequirement requirement = (JJRequirement) objet;
			obj = "Requirement";
			String afterStatus = null;

			if (requirement.getStatus() != null)
				afterStatus = requirement.getStatus().getName();
			else
				afterStatus = "Null";

			if (requirement.getId() != null) {
				JJRequirement oldRequirement = jJRequirementService
						.findJJRequirement(requirement.getId());
				String oldStatus = null;
				if (oldRequirement.getStatus() != null)
					oldStatus = oldRequirement.getStatus().getName();
				else
					oldStatus = "Null";
				longText = obj + " Status has changed from  :" + oldStatus
						+ " to " + afterStatus;
			} else {
				longText = "new " + obj + " creation with the status :"
						+ afterStatus;
			}

		} else if (objet instanceof JJBuild) {

			JJBuild build = (JJBuild) objet;
			obj = "Build";
			String afterStatus = null;

			if (build.getStatus() != null)
				afterStatus = build.getStatus().getName();
			else
				afterStatus = "Null";

			if (build.getId() != null) {
				JJBuild oldBuild = jJBuildService.findJJBuild(build.getId());
				String oldStatus = null;
				if (oldBuild.getStatus() != null)
					oldStatus = oldBuild.getStatus().getName();
				else
					oldStatus = "Null";
				longText = obj + " Status has changed from  :" + oldStatus
						+ " to " + afterStatus;
			} else {
				longText = "new " + obj + " creation with the status :"
						+ afterStatus;
			}

		}

		if (obj != null) {
			JJAuditLog auditLog = new JJAuditLog();
			auditLog.setAuditLogDate(new Date());
			auditLog.setContact(
					((LoginBean) LoginBean.findBean("loginBean")).getContact());
			auditLog.setObjet(obj);
			auditLog.setKeyName("Status Changed for " + obj);
			auditLog.setKeyValue(longText);
			JJAuditLogBean jJAuditLogBean = ((JJAuditLogBean) LoginBean
					.findBean("jJAuditLogBean"));
			if (jJAuditLogBean == null)
				jJAuditLogBean = new JJAuditLogBean();

			jJAuditLogBean.saveJJAuditLog(auditLog);

		}

	}

	public void setBugToPlannedWorkFlow(Object objet) {

		if (objet instanceof JJTask) {
			JJTask task = (JJTask) objet;
			JJStatus status = task.getStatus();
			if (status != null && task.getBug() != null) {

				JJBug bug = jJBugService.findJJBug(task.getBug().getId());

				if ((status.getName().equalsIgnoreCase("IN PROGRESS")
						|| status.getName().equalsIgnoreCase("TODO"))
						&& (bug.getStatus() == null || !bug.getStatus()
								.getName().equalsIgnoreCase("Planned"))) {

					JJStatus bugStatus = jJStatusService.getOneStatus("Planned",
							"Bug", true);
					bug.setStatus(bugStatus);

					JJBugBean jJBugBean = (JJBugBean) LoginBean
							.findBean("jJBugBean");
					if (jJBugBean == null)
						jJBugBean = new JJBugBean();
					jJBugBean.updateJJBug(bug);
					jJBugBean.reset();

				}

			}

		}

	}

	public void setTestCaseToPlannedWorkFlow(Object objet) {

		if (objet instanceof JJTask) {
			JJTask task = (JJTask) objet;
			JJStatus status = task.getStatus();
			if (status != null && task.getTestcase() != null) {

				JJTestcase test = jJTestcaseService
						.findJJTestcase(task.getTestcase().getId());

				if ((status.getName().equalsIgnoreCase("IN PROGRESS")
						|| status.getName().equalsIgnoreCase("TODO"))
						&& (test.getStatus() == null || !test.getStatus()
								.getName().equalsIgnoreCase("Planned"))) {

					JJStatus testStatus = jJStatusService
							.getOneStatus("Planned", "TestCase", true);
					test.setStatus(testStatus);
					jJTestcaseService.updateJJTestcase(test);

				}

			}

		}

	}

	public void setTestCaseToFinishedWorkFlow(Object objet) {

		if (objet instanceof JJTask) {
			JJTask task = (JJTask) objet;
			JJStatus status = task.getStatus();
			if (status != null && task.getTestcase() != null) {

				JJTestcase test = jJTestcaseService
						.findJJTestcase(task.getTestcase().getId());

				if (status.getName().equalsIgnoreCase("Done")
						&& (test.getStatus() == null || !test.getStatus()
								.getName().equalsIgnoreCase("Finished"))) {

					JJStatus testStatus = jJStatusService
							.getOneStatus("Finished", "TestCase", true);
					test.setStatus(testStatus);
					jJTestcaseService.updateJJTestcase(test);
					JJTestcaseBean.resetTestcaseBean();
				}

			}

		}

	}

	public void setRequirementToRELEASEDWorkFlow(Object objet) {

		if (objet instanceof JJTask) {
			JJTask task = (JJTask) objet;
			JJStatus status = task.getStatus();
			if (status != null && task.getRequirement() != null) {

				JJRequirement req = jJRequirementService
						.findJJRequirement(task.getRequirement().getId());

				if (status.getName().equalsIgnoreCase("IN PROGRESS")
						&& (req.getStatus() == null || !req.getStatus()
								.getName().equalsIgnoreCase("RELEASED"))) {

					// JJRequirementBean jJRequirementBean = (JJRequirementBean)
					// LoginBean
					// .findBean("jJRequirementBean");
					JJStatus reqStatus = jJStatusService
							.getOneStatus("RELEASED", "Requirement", true);
					req.setStatus(reqStatus);
					// if (jJRequirementBean == null)
					// jJRequirementBean = new JJRequirementBean();
					jJRequirementService.updateJJRequirement(req);

					if (((RequirementBean) LoginBean
							.findBean("requirementBean")) != null)
						((RequirementBean) LoginBean
								.findBean("requirementBean")).setRootNode(null);
					JJRequirementBean jJRequirementBean = (JJRequirementBean) LoginBean
							.findBean("jJRequirementBean");
					if (jJRequirementBean != null)
						jJRequirementBean.updateDataTable(task.getRequirement(),
								task.getRequirement().getCategory(),
								JJRequirementBean.UPDATE_OPERATION);
				}

			}

		}

	}

	public void subscribeToCreatedObjectWorkFlow(Object object) {

		JJContact contact = null;

		if (object instanceof JJBug)
			contact = ((JJBug) object).getCreatedBy();
		else if (object instanceof JJRequirement)
			contact = ((JJRequirement) object).getCreatedBy();
		else if (object instanceof JJTestcase)
			contact = ((JJTestcase) object).getCreatedBy();

		if (contact == null)
			contact = ((LoginBean) LoginBean.findBean("loginBean"))
					.getContact();

		if (object instanceof JJBug) {
			contact.getBugs().add((JJBug) object);
			jJContactService.updateJJContact(contact);

		} else if (object instanceof JJRequirement) {
			contact.getRequirements().add((JJRequirement) object);
			jJContactService.updateJJContact(contact);

		} else if (object instanceof JJTestcase) {
			contact.getTestcases().add((JJTestcase) object);
			jJContactService.updateJJContact(contact);

		}

		JJContact b = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		if (contact.equals(b)) {
			b.setUpdatedBy(b);
			((LoginBean) LoginBean.findBean("loginBean")).setContact(null);
		}

	}

	public void sendMailToBusinessReqCreatorWorkFlow(Object object) {

		if (object instanceof JJTask) {
			JJTask task = (JJTask) object;
			if (task.getRequirement() != null) {
				JJRequirement requirement = task.getRequirement();
				Set<JJRequirement> businessRequirements = new HashSet<JJRequirement>();
				fillBusinessRequirements(requirement, businessRequirements);

				if (!businessRequirements.isEmpty()) {
					if (mailingService == null)
						mailingService = new MailingService();
					String taskTitle = ((JJTaskBean) LoginBean
							.findBean("jJTaskBean")).getDialogHeader(task,
									null);

					for (JJRequirement req : businessRequirements) {
						System.err.println("REQUIREMENT_" + req.getName());
						mailingService.sendMail(req, taskTitle);
					}

				}
			}

		}

	}

	private void fillBusinessRequirements(JJRequirement requirement,
			Set<JJRequirement> businessRequirements) {

		requirement = jJRequirementService
				.findJJRequirement(requirement.getId());
		if (requirement.getCategory() != null
				&& requirement.getCategory().getStage() != null
				&& requirement.getCategory().getStage() == 1)
			businessRequirements.add(requirement);
		else {
			Set<JJRequirement> linkDownRequirement = requirement
					.getRequirementLinkDown();

			for (JJRequirement req : linkDownRequirement) {
				fillBusinessRequirements(req, businessRequirements);
			}

		}
	}

	@SuppressWarnings("rawtypes")
	public void sendMailWorkFlow(Object object) {

		if (object instanceof JJTask) {
			JJTask task = (JJTask) object;
			JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
					.getContact();

			if (jJTaskService.findJJTask(task.getId())
					.getEndDateReal() == null) {
				if (mailingService == null)
					mailingService = new MailingService();

				StringReader description = null;
				if (task.getRequirement() != null)
					description = new StringReader(
							task.getRequirement().getDescription());
				else if (task.getBug() != null)
					description = new StringReader(
							task.getBug().getDescription());
				else if (task.getTestcase() != null)
					description = new StringReader(
							task.getTestcase().getDescription());

				String subject = "";
				List arrList = null;
				try {
					arrList = HTMLWorker.parseToList(description, null);
					for (int i = 0; i < arrList.size(); ++i) {
						subject = subject
								+ ((Element) arrList.get(i)).toString();
					}
				} catch (Exception e) {
					subject = description.toString();
				}

				subject = subject.replace("[", " ").replace("]", "");

				List<JJContact> contacts = new ArrayList<JJContact>();
				if (task.getSprint() != null)
					contacts = new ArrayList<JJContact>(
							task.getSprint().getContacts());
				else
					contacts = jJPermissionService.areAuthorized(
							contact.getCompany(), null, task.getProject(),
							task.getProduct(), "sprintContact");

				if (task.getProduct() != null
						&& !contacts.contains(task.getProduct().getManager()))
					contacts.add(task.getProduct().getManager());
				mailingService.sendMail(
						task.getProject().getManager().getEmail(), contacts,
						task, subject);

			}

		}
	}

	public void iterateOverWorkFlows() {

		for (JJWorkflow workFlow : this.workFlows) {
			switch (workFlow.getActionWorkflow()) {
				case "setRequirementToRELEASEDWorkFlow" :
					this.setRequirementToRELEASEDWorkFlow(object);
					break;
				case "createMessageWorkFlow" :
					this.createMessageWorkFlow(object);
					break;
				case "loggingWorkFlow" :
					this.loggingWorkFlow(object);
					break;
				case "sendMailWorkFlow" :
					this.sendMailWorkFlow(object);
					break;
				case "sendMailToBusinessReqCreatorWorkFlow" :
					this.sendMailToBusinessReqCreatorWorkFlow(object);
					break;
				case "subscribeToCreatedObjectWorkFlow" :
					this.subscribeToCreatedObjectWorkFlow(object);
					break;
				case "setBugToPlannedWorkFlow" :
					this.setBugToPlannedWorkFlow(object);
					break;
				case "setTestCaseToPlannedWorkFlow" :
					this.setTestCaseToPlannedWorkFlow(object);
					break;
				case "setTestCaseToFinishedWorkFlow" :
					this.setTestCaseToFinishedWorkFlow(object);
					break;

			}
		}

		object = null;
		workFlows = null;
	}
}

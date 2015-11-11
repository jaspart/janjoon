package com.starit.janjoonweb.ui.mb.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJBugService;
import com.starit.janjoonweb.domain.JJBuild;
import com.starit.janjoonweb.domain.JJBuildService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJCriticityService;
import com.starit.janjoonweb.domain.JJMessage;
import com.starit.janjoonweb.domain.JJMessageService;
import com.starit.janjoonweb.domain.JJProjectService;
import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJRequirementService;
import com.starit.janjoonweb.domain.JJStatusService;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJTaskService;
import com.starit.janjoonweb.domain.JJWorkflow;
import com.starit.janjoonweb.domain.JJWorkflowService;
import com.starit.janjoonweb.ui.mb.JJVersionBean;
import com.starit.janjoonweb.ui.mb.LoginBean;

@Aspect
@Configurable
public class AppLogger {

	static Logger logger = Logger.getLogger("AppLogger-Logger");

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
	private JJProjectService jJProjectService;

	@Autowired
	private JJStatusService jJStatusService;

	@Autowired
	private WorkFlowsActions workFlowsActions;

	@Autowired
	private JJWorkflowService jJWorkflowService;

	public void setjJBugService(JJBugService jJBugService) {
		this.jJBugService = jJBugService;
	}

	public void setjJBuildService(JJBuildService jJBuildService) {
		this.jJBuildService = jJBuildService;
	}

	public void setjJRequirementService(
			JJRequirementService jJRequirementService) {
		this.jJRequirementService = jJRequirementService;
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

	public void setjJProjectService(JJProjectService jJProjectService) {
		this.jJProjectService = jJProjectService;
	}

	public void setjJStatusService(JJStatusService jJStatusService) {
		this.jJStatusService = jJStatusService;
	}

	public void setjJWorkflowService(JJWorkflowService jJWorkflowService) {
		this.jJWorkflowService = jJWorkflowService;
	}

	public WorkFlowsActions getWorkFlowsActions() {
		return workFlowsActions;
	}

	public void setWorkFlowsActions(WorkFlowsActions workFlowsActions) {
		this.workFlowsActions = workFlowsActions;
	}

	@Before("execution(* com.starit.janjoonweb.ui.mb.*.updateJJ*(..))")
	public void workFlowUpdateListener(JoinPoint joinPoint) {

		if (joinPoint.getArgs() != null && joinPoint.getArgs().length == 1) {

			Object[] args = joinPoint.getArgs();
			Object object = args[0];
			List<JJWorkflow> workFlows = new ArrayList<JJWorkflow>();
			if (object instanceof JJBug) {

				HttpSession session = (HttpSession) FacesContext
						.getCurrentInstance().getExternalContext()
						.getSession(false);
				JJVersionBean jJVersionBean = (JJVersionBean) session
						.getAttribute("jJVersionBean");
				jJVersionBean.setVersionDataModelList(null);

				JJBug bug = (JJBug) object;
				if (bug.getId() != null && bug.getEnabled()) {
					JJBug oldBug = jJBugService.findJJBug(bug.getId());
					workFlows = jJWorkflowService.getObjectWorkFlows("bug",
							oldBug.getStatus(), bug.getStatus(), null, true);
				} else if (bug.getEnabled()) {
					workFlows = jJWorkflowService.getObjectWorkFlows("bug",
							jJStatusService.getOneStatus("Any", "*", true),
							bug.getStatus(), null, true);
				}
			} else if (object instanceof JJRequirement) {

				HttpSession session = (HttpSession) FacesContext
						.getCurrentInstance().getExternalContext()
						.getSession(false);
				JJVersionBean jJVersionBean = (JJVersionBean) session
						.getAttribute("jJVersionBean");
				jJVersionBean.setVersionDataModelList(null);

				JJRequirement requirement = (JJRequirement) object;
				if (requirement.getId() != null && requirement.getEnabled()) {
					JJRequirement oldRequirement = jJRequirementService
							.findJJRequirement(requirement.getId());
					workFlows = jJWorkflowService.getObjectWorkFlows(
							"requirement", oldRequirement.getStatus(),
							requirement.getStatus(), null, true);
				} else if (requirement.getEnabled()) {
					workFlows = jJWorkflowService.getObjectWorkFlows(
							"requirement",
							jJStatusService.getOneStatus("Any", "*", true),
							requirement.getStatus(), null, true);
				}
			} else if (object instanceof JJMessage) {
				JJMessage message = (JJMessage) object;
				if (message.getId() != null && message.getEnabled()) {
					JJMessage oldTestcase = jJMessageService
							.findJJMessage(message.getId());
					workFlows = jJWorkflowService.getObjectWorkFlows("message",
							oldTestcase.getStatus(), message.getStatus(), null,
							true);
				} else if (message.getEnabled()) {
					workFlows = jJWorkflowService.getObjectWorkFlows("message",
							null, message.getStatus(), null, true);
				}
			} else if (object instanceof JJBuild) {

				HttpSession session = (HttpSession) FacesContext
						.getCurrentInstance().getExternalContext()
						.getSession(false);
				JJVersionBean jJVersionBean = (JJVersionBean) session
						.getAttribute("jJVersionBean");
				jJVersionBean.setVersionDataModelList(null);

				JJBuild build = (JJBuild) object;
				if (build.getId() != null && build.getEnabled()) {
					JJBuild oldTestcase = jJBuildService.findJJBuild(build
							.getId());
					workFlows = jJWorkflowService.getObjectWorkFlows("build",
							oldTestcase.getStatus(), build.getStatus(), null,
							true);
				} else if (build.getEnabled()) {
					workFlows = jJWorkflowService.getObjectWorkFlows("build",
							null, build.getStatus(), null, true);
				}
			}

			workFlowsActions.iterateOverWorkFlows(workFlows, object);
		}

	}

	@Before("execution(* com.starit.janjoonweb.ui.mb.*.saveJJ*(..))")
	public void workFlowSaveListener(JoinPoint joinPoint) {

		if (joinPoint.getArgs() != null && joinPoint.getArgs().length == 1) {

			Object[] args = joinPoint.getArgs();
			Object object = args[0];
			List<JJWorkflow> workFlows = new ArrayList<JJWorkflow>();
			if (object instanceof JJBug) {

				HttpSession session = (HttpSession) FacesContext
						.getCurrentInstance().getExternalContext()
						.getSession(false);
				JJVersionBean jJVersionBean = (JJVersionBean) session
						.getAttribute("jJVersionBean");
				jJVersionBean.setVersionDataModelList(null);

				JJBug bug = (JJBug) object;
				if (bug.getId() != null) {
					JJBug oldBug = jJBugService.findJJBug(bug.getId());
					workFlows = jJWorkflowService.getObjectWorkFlows("bug",
							oldBug.getStatus(), bug.getStatus(), null, true);
				} else {
					workFlows = jJWorkflowService.getObjectWorkFlows("bug",
							null, bug.getStatus(), null, true);
				}
			} else if (object instanceof JJRequirement) {

				HttpSession session = (HttpSession) FacesContext
						.getCurrentInstance().getExternalContext()
						.getSession(false);
				JJVersionBean jJVersionBean = (JJVersionBean) session
						.getAttribute("jJVersionBean");
				jJVersionBean.setVersionDataModelList(null);

				JJRequirement requirement = (JJRequirement) object;
				if (requirement.getId() != null) {
					JJRequirement oldRequirement = jJRequirementService
							.findJJRequirement(requirement.getId());
					workFlows = jJWorkflowService.getObjectWorkFlows(
							"requirement", oldRequirement.getStatus(),
							requirement.getStatus(), null, true);
				} else {
					workFlows = jJWorkflowService.getObjectWorkFlows(
							"requirement", null, requirement.getStatus(), null,
							true);
				}
			} else if (object instanceof JJMessage) {
				JJMessage message = (JJMessage) object;
				if (message.getId() != null) {
					JJMessage oldMessage = jJMessageService
							.findJJMessage(message.getId());
					workFlows = jJWorkflowService.getObjectWorkFlows("message",
							oldMessage.getStatus(), message.getStatus(), null,
							true);
				} else {
					workFlows = jJWorkflowService.getObjectWorkFlows("message",
							null, message.getStatus(), null, true);
				}
			} else if (object instanceof JJBuild) {

				HttpSession session = (HttpSession) FacesContext
						.getCurrentInstance().getExternalContext()
						.getSession(false);
				JJVersionBean jJVersionBean = (JJVersionBean) session
						.getAttribute("jJVersionBean");
				jJVersionBean.setVersionDataModelList(null);

				JJBuild build = (JJBuild) object;
				if (build.getId() != null) {
					JJBuild oldBuild = jJBuildService
							.findJJBuild(build.getId());
					workFlows = jJWorkflowService
							.getObjectWorkFlows("build", oldBuild.getStatus(),
									build.getStatus(), null, true);
				} else {
					workFlows = jJWorkflowService.getObjectWorkFlows("build",
							null, build.getStatus(), null, true);
				}
			}
			workFlowsActions.iterateOverWorkFlows(workFlows, object);

		}
	}

	@Before("execution(* com.starit.janjoonweb.ui.mb.JJTaskBean.saveJJTask(..))")
	public void updateJJTaskFields(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		JJTask task = (JJTask) args[0];

		if (task.getId() == null)
			task.setCreationDate(new Date());

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJVersionBean jJVersionBean = (JJVersionBean) session
				.getAttribute("jJVersionBean");
		jJVersionBean.setVersionDataModelList(null);

		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		if (task.getId() == null) {
			task.setCreatedBy(contact);
		} else {
			task.setUpdatedBy(contact);
			task.setUpdatedDate(new Date());
		}

		ContactCalendarUtil calendarUtil;
		boolean assignedTo = false;

		if (task.getAssignedTo() != null && task.getId() != null) {
			calendarUtil = new ContactCalendarUtil(task.getAssignedTo());
			assignedTo = !task.getAssignedTo().equals(
					jJTaskService.findJJTask(task.getId()).getAssignedTo());
		} else {
			calendarUtil = new ContactCalendarUtil(jJProjectService
					.findJJProject(LoginBean.getProject().getId()).getManager()
					.getCompany());
			if (task.getId() != null)
				assignedTo = jJTaskService.findJJTask(task.getId())
						.getAssignedTo() != null;
		}

		boolean planned = false;
		boolean real = false;
		boolean revised = false;

		if (task.getStartDatePlanned() != null) {

			if (task.getId() != null) {
				if ((!task.getStartDatePlanned().equals(
						jJTaskService.findJJTask(task.getId())
								.getStartDatePlanned()))
						|| assignedTo) {
					task.setStartDatePlanned(calendarUtil.nextWorkingDate(task
							.getStartDatePlanned()));
					planned = true;
				}
			} else {
				task.setStartDatePlanned(calendarUtil.nextWorkingDate(task
						.getStartDatePlanned()));
				planned = true;
			}

		}

		if (task.getEndDatePlanned() != null) {

			if (task.getId() != null) {
				if ((!task.getEndDatePlanned().equals(
						jJTaskService.findJJTask(task.getId())
								.getEndDatePlanned()))
						|| assignedTo) {
					task.setEndDatePlanned(calendarUtil.nextWorkingDate(task
							.getEndDatePlanned()));
					planned = true;
				}
			} else {
				task.setEndDatePlanned(calendarUtil.nextWorkingDate(task
						.getEndDatePlanned()));
				planned = true;
			}

		}

		if (task.getEndDatePlanned() != null
				&& task.getStartDatePlanned() != null && planned)
			task.setWorkloadPlanned(Math.round(calendarUtil.calculateWorkLoad(
					task.getStartDatePlanned(), task.getEndDatePlanned(), null,
					null)));

		if (task.getStartDateReal() != null) {
			if (task.getId() != null) {
				if ((!task.getStartDateReal().equals(
						jJTaskService.findJJTask(task.getId())
								.getStartDateReal()))
						|| assignedTo) {
					task.setStartDateReal(calendarUtil.nextWorkingDate(task
							.getStartDateReal()));
					real = true;
				}
			} else {
				task.setStartDateReal(calendarUtil.nextWorkingDate(task
						.getStartDateReal()));
				real = true;
			}
		}

		if (task.getEndDateReal() != null) {
			if (task.getId() != null) {
				if ((!task.getEndDateReal()
						.equals(jJTaskService.findJJTask(task.getId())
								.getEndDateReal()))
						|| assignedTo) {
					task.setEndDateReal(calendarUtil.nextWorkingDate(task
							.getEndDateReal()));
					real = true;
				}
			} else {
				task.setEndDateReal(calendarUtil.nextWorkingDate(task
						.getEndDateReal()));
				System.err.println("getEndDateReal Date modified");
			}
		}

		if (task.getEndDateReal() != null && task.getStartDateReal() != null
				&& real)
			task.setWorkloadReal(Math.round(calendarUtil.calculateWorkLoad(
					task.getStartDateReal(), task.getEndDateReal(),
					jJTaskService, task)));

		if (task.getStartDateRevised() != null) {
			if (task.getId() != null) {
				if ((!task.getStartDateRevised().equals(
						jJTaskService.findJJTask(task.getId())
								.getStartDateRevised()))
						|| assignedTo) {
					task.setStartDateRevised(calendarUtil.nextWorkingDate(task
							.getStartDateRevised()));
					System.err.println("getStartDateRevised Date modified");
					revised = true;
				}
			} else {
				task.setStartDateRevised(calendarUtil.nextWorkingDate(task
						.getStartDateRevised()));
				System.err.println("getStartDateRevised Date modified");
				revised = true;
			}
		}

		if (task.getEndDateRevised() != null) {

			if (task.getId() != null) {
				if ((!task.getEndDateRevised().equals(
						jJTaskService.findJJTask(task.getId())
								.getEndDateRevised()))
						|| assignedTo) {
					task.setEndDateRevised(calendarUtil.nextWorkingDate(task
							.getEndDateRevised()));
					System.err.println("getEndDateRevised Date modified");
					revised = true;
				}
			} else {
				task.setEndDateRevised(calendarUtil.nextWorkingDate(task
						.getEndDateRevised()));
				System.err.println("getEndDateRevised Date modified");
				revised = true;
			}
		}

		if (task.getEndDateRevised() != null
				&& task.getStartDateRevised() != null && revised)
			task.setWorkloadRevised(Math.round(calendarUtil.calculateWorkLoad(
					task.getStartDateRevised(), task.getEndDateRevised(), null,
					null)));

		if (task.getStartDateReal() == null
				&& (task.getStatus() == null || !task.getStatus().getName()
						.equalsIgnoreCase("todo"))) {
			task.setStatus(jJStatusService.getOneStatus("TODO", "Task", true));
		} else if (task.getStartDateReal() != null) {
			if (task.getEndDateReal() == null
					&& (task.getStatus() == null || !task.getStatus().getName()
							.equalsIgnoreCase("IN PROGRESS"))) {
				task.setStatus(jJStatusService.getOneStatus("IN PROGRESS",
						"Task", true));
			} else if (task.getEndDateReal() != null
					&& (task.getStatus() == null || !task.getStatus().getName()
							.equalsIgnoreCase("DONE"))) {
				task.setStatus(jJStatusService.getOneStatus("DONE", "Task",
						true));
			}
		}

		logger.info("operation : " + joinPoint.getSignature().toShortString()
				+ " :successful " + task.getName());
		// callingJJTaskWorkFlows
		List<JJWorkflow> workFlows = new ArrayList<JJWorkflow>();

		if (task.getId() != null) {
			JJTask oldtask = jJTaskService.findJJTask(task.getId());
			workFlows = jJWorkflowService.getObjectWorkFlows("task",
					oldtask.getStatus(), task.getStatus(), null, true);
		} else {
			workFlows = jJWorkflowService.getObjectWorkFlows("task",
					jJStatusService.getOneStatus("Any", "*", true),
					task.getStatus(), null, true);
		}

		workFlowsActions.iterateOverWorkFlows(workFlows, task);

	}

}

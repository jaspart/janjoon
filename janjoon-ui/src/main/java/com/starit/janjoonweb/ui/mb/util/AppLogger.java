package com.starit.janjoonweb.ui.mb.util;

import java.util.Date;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJBugService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJCriticity;
import com.starit.janjoonweb.domain.JJCriticityService;
import com.starit.janjoonweb.domain.JJMessage;
import com.starit.janjoonweb.domain.JJMessageService;
import com.starit.janjoonweb.domain.JJProjectService;
import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJRequirementService;
import com.starit.janjoonweb.domain.JJStatus;
import com.starit.janjoonweb.domain.JJStatusService;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJTaskService;
import com.starit.janjoonweb.ui.mb.JJRequirementBean;
import com.starit.janjoonweb.ui.mb.LoginBean;
import com.starit.janjoonweb.ui.mb.RequirementBean;

@Aspect
@Configurable
public class AppLogger {

	static Logger logger = Logger.getLogger("ManagedBean-Logger");

	@Autowired
	JJBugService jJBugService;
	
	@Autowired
	JJRequirementService jJRequirementService;
	
	@Autowired
	JJCriticityService jJCriticityService;	
	
	
	public void setjJBugService(JJBugService jJBugService) {
		this.jJBugService = jJBugService;
	}

	public void setjJRequirementService(JJRequirementService jJRequirementService) {
		this.jJRequirementService = jJRequirementService;
	}

	@Autowired
	JJMessageService jJMessageService;
	
	
	public void setjJMessageService(JJMessageService jJMessageService) {
		this.jJMessageService = jJMessageService;
	}

	public void setjJCriticityService(JJCriticityService jJCriticityService) {
		this.jJCriticityService = jJCriticityService;
	}

	@Autowired
	JJTaskService jJTaskService;

	public void setjJTaskService(JJTaskService jJTaskService) {
		this.jJTaskService = jJTaskService;
	}

	@Autowired
	JJProjectService jJProjectService;

	public void setjJProjectService(JJProjectService jJProjectService) {
		this.jJProjectService = jJProjectService;
	}
	
	@Autowired
	JJStatusService jJStatusService;
	

	public void setjJStatusService(JJStatusService jJStatusService) {
		this.jJStatusService = jJStatusService;
	}
	
	@Before("execution(* com.starit.janjoonweb.ui.mb.JJRequirementBean.updateJJRequirement(..))")
	public void reqStatusChanged(JoinPoint joinPoint) {
		
		Object[] args = joinPoint.getArgs();
		JJRequirement requirement = (JJRequirement) args[0];
		JJRequirement oldRequirement=jJRequirementService.findJJRequirement(requirement.getId());
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		if(requirement.getStatus() != null && oldRequirement.getStatus() != null && !oldRequirement.getStatus().equals(requirement.getStatus()))
		{
			JJCriticity criticity = jJCriticityService.getCriticityByName(
					"INFO", true);
			JJStatus status = jJStatusService.getOneStatus("NEW", "Message", true);
			
			JJMessage mes = new JJMessage();
			mes.setName("Requirement :"+requirement.getName()+" status changed");
			mes.setCreatedBy(contact);
			mes.setCriticity(criticity);
			mes.setStatus(status);
			mes.setCompany(contact.getCompany());
			mes.setProject(requirement.getProject());
			mes.setProduct(requirement.getProduct());
			mes.setRequirement(requirement);
			mes.setDescription("Requirement :"+requirement.getName()+" status changed from "+oldRequirement.getStatus().getName()
					+" to "+requirement.getStatus().getName());
			mes.setCreationDate(new Date());
			mes.setEnabled(true);
			mes.setMessage("Requirement :"+requirement.getName()+" status changed from "+oldRequirement.getStatus().getName()
					+" to "+requirement.getStatus().getName());
			jJMessageService.saveJJMessage(mes);
			((LoginBean) LoginBean.findBean("loginBean")).setMessageCount(null);
		}
		
		
	}
	
	@Before("execution(* com.starit.janjoonweb.ui.mb.JJBugBean.updateJJBug(..))")
	public void bugStatusChanged(JoinPoint joinPoint) {
		
		Object[] args = joinPoint.getArgs();
		JJBug bug = (JJBug) args[0];
		JJBug oldBug=jJBugService.findJJBug(bug.getId());
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		if(bug.getStatus() != null && oldBug.getStatus() != null && !oldBug.getStatus().equals(bug.getStatus()))
		{
			JJCriticity criticity = jJCriticityService.getCriticityByName(
					"INFO", true);
			JJStatus status = jJStatusService.getOneStatus("NEW", "Message", true);
			
			JJMessage mes = new JJMessage();
			mes.setName("Bug :"+bug.getName()+"status changed");
			mes.setCreatedBy(contact);
			mes.setStatus(status);
			mes.setCriticity(criticity);
			mes.setCompany(contact.getCompany());
			mes.setProduct(bug.getProduct());
			mes.setProject(bug.getProject());
			mes.setBug(bug);
			mes.setDescription("Bug :"+bug.getName()+" status changed from "+oldBug.getStatus().getName()
					+" to "+bug.getStatus().getName());
			mes.setCreationDate(new Date());
			mes.setEnabled(true);
			mes.setMessage("Bug :"+bug.getName()+" status changed from "+oldBug.getStatus().getName()
					+" to "+bug.getStatus().getName());
			jJMessageService.saveJJMessage(mes);
			((LoginBean) LoginBean.findBean("loginBean")).setMessageCount(null);
		}
		
		
	}


	@Before("execution(* com.starit.janjoonweb.ui.mb.JJTaskBean.saveJJTask(..))")
	public void updateJJTaskFields(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		JJTask task = (JJTask) args[0];
		
		if (task.getId() == null)
			task.setCreationDate(new Date());
		
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

		if (task.getAssignedTo() != null) {
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
					System.err.println("getStartDatePlanned Date modified");
				}
			} else {
				task.setStartDatePlanned(calendarUtil.nextWorkingDate(task
						.getStartDatePlanned()));
				planned = true;
				System.err.println("getStartDatePlanned Date modified");
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
					System.err.println("getEndDatePlanned Date modified");
				}
			} else {
				task.setEndDatePlanned(calendarUtil.nextWorkingDate(task
						.getEndDatePlanned()));
				planned = true;
				System.err.println("getEndDatePlanned Date modified");
			}

		}

		if (task.getEndDatePlanned() != null
				&& task.getStartDatePlanned() != null && planned)
			task.setWorkloadPlanned(Math.round(calendarUtil.calculateWorkLoad(
					task.getStartDatePlanned(), task.getEndDatePlanned(),null,null)));

		if (task.getStartDateReal() != null) {
			if (task.getId() != null) {
				if ((!task.getStartDateReal().equals(
						jJTaskService.findJJTask(task.getId())
								.getStartDateReal()))
						|| assignedTo) {
					task.setStartDateReal(calendarUtil.nextWorkingDate(task
							.getStartDateReal()));
					System.err.println("getStartDateReal Date modified");
					real = true;
				}
			} else {
				task.setStartDateReal(calendarUtil.nextWorkingDate(task
						.getStartDateReal()));
				System.err.println("getStartDateReal Date modified");
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
					System.err.println("getEndDateReal Date modified");
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
					task.getStartDateReal(), task.getEndDateReal(),jJTaskService,task)));

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
					task.getStartDateRevised(), task.getEndDateRevised(),null,null)));

		if (task.getStartDateReal() == null
				&& (task.getStatus() == null || !task.getStatus().getName()
						.equalsIgnoreCase("todo"))){
			task.setStatus(jJStatusService.getOneStatus("TODO", "Task",
					true));
		}else if(task.getStartDateReal() != null )
		{
			if (task.getEndDateReal() == null
					&& (task.getStatus() == null || !task.getStatus().getName()
							.equalsIgnoreCase("IN PROGRESS")))
			{
				task.setStatus(jJStatusService.getOneStatus("IN PROGRESS", "Task",
						true));
			}else if(task.getEndDateReal() != null
					&& (task.getStatus() == null || !task.getStatus().getName()
					.equalsIgnoreCase("DONE")))
			{
				task.setStatus(jJStatusService.getOneStatus("DONE", "Task",
						true));
			}
		}		
		
		logger.info("operation : " + joinPoint.getSignature().toShortString()
				+ " :successful " + task.getName());
	}

}

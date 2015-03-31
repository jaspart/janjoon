package com.starit.janjoonweb.ui.mb.util;

import java.util.Date;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJProjectService;
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
	JJTaskService jJTaskService;

	public void setjJTaskService(JJTaskService jJTaskService) {
		this.jJTaskService = jJTaskService;
	}

	@Autowired
	JJProjectService jJProjectService;

	public void setjJProjectService(JJProjectService jJProjectService) {
		this.jJProjectService = jJProjectService;
	}

	@Before("execution(* com.starit.janjoonweb.ui.mb.JJTaskBean.saveJJTask(..))")
	public void updateJJTaskFields(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		JJTask task = (JJTask) args[0];
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		if (task.getId() == null) {
			task.setCreationDate(new Date());
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
			task.setWorkloadPlanned(calendarUtil.calculateWorkLoad(
					task.getStartDatePlanned(), task.getEndDatePlanned()));

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
			task.setWorkloadReal(calendarUtil.calculateWorkLoad(
					task.getStartDateReal(), task.getEndDateReal()));

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
			task.setWorkloadRevised(calendarUtil.calculateWorkLoad(
					task.getStartDateRevised(), task.getEndDateRevised()));

		logger.info("operation : " + joinPoint.getSignature().toShortString()
				+ " :successful " + task.getName());
	}

	// @Before("execution(* com.starit.janjoonweb.ui.mb.JJ*Bean.saveJJ*(..))")
	// public void setCreationDate(JoinPoint joinPoint) {
	//
	// Object[] args = joinPoint.getArgs();
	// Object b=args[0];
	// JJContact contact=(JJContact) ((HttpSession)
	// FacesContext.getCurrentInstance().getExternalContext()
	// .getSession(false)).getAttribute("JJContact");
	// if(b instanceof JJBug)
	// {
	// JJBug a=(JJBug) b;
	// a.setCreatedBy(contact);
	// a.set
	//
	// }
	//
	//
	// }

}

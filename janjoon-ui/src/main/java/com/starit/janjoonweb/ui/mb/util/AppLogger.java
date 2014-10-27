package com.starit.janjoonweb.ui.mb.util;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.starit.janjoonweb.domain.JJRequirementService;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJTaskService;
import com.starit.janjoonweb.ui.mb.JJCompanyBean;

@Aspect
@Configurable
public class AppLogger {
	
	static Logger logger = Logger.getLogger("ManagedBean-Logger");
	
	@Autowired
	JJTaskService jJTaskService;
	
	
	
	public void setjJTaskService(JJTaskService jJTaskService) {
		this.jJTaskService = jJTaskService;
	}

	@AfterReturning("execution(* com.starit.janjoonweb.ui.mb.JJProductBean.save(..))")
	public void resetVersion(JoinPoint joinPoint)
	{
		
	}
	
	@AfterReturning("execution(* com.starit.janjoonweb.ui.mb.JJProductBean.save(..))")
	public void resetProduit(JoinPoint joinPoint)
	{
		
	}
	
	@AfterReturning("execution(* com.starit.janjoonweb.ui.mb.JJProductBean.save(..))")
	public void resetProject(JoinPoint joinPoint)
	{
		
	}
	
	@Before("execution(* com.starit.janjoonweb.ui.mb.JJTaskBean.saveJJTask(..))")
	public void updateJJTaskFields(JoinPoint joinPoint)
	{
		Object[] args = joinPoint.getArgs();
		// JJStatus status=(JJStatus) args[0];
		JJTask task = (JJTask) args[0];
		//CalendarUtil calendarUtil=(CalendarUtil) args[1];
		
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
				.getSession(false);
		JJCompanyBean companyBean=(JJCompanyBean) session.getAttribute("jJCompanyBean");
		if (companyBean==null)
			companyBean=new JJCompanyBean();
		CalendarUtil calendarUtil=companyBean.getCompanyCalendar();
		
		if(task.getStartDatePlanned() != null)		
			task.setStartDatePlanned(calendarUtil.nextWorkingDate(task.getStartDatePlanned()));
		
		if(task.getEndDatePlanned() != null)		
			task.setEndDatePlanned(calendarUtil.nextWorkingDate(task.getEndDatePlanned()));	
		
		
		if(task.getStartDateReal() != null)		
			task.setStartDateReal(calendarUtil.nextWorkingDate(task.getStartDateReal()));
		
		if(task.getEndDateReal() != null)
			task.setEndDateReal(calendarUtil.nextWorkingDate(task.getEndDateReal()));
		
		if(task.getStartDateRevised() != null)
			task.setStartDateRevised(calendarUtil.nextWorkingDate(task.getStartDateRevised()));
		
		if(task.getEndDateRevised() != null)
			task.setEndDateRevised(calendarUtil.nextWorkingDate(task.getEndDateRevised()));	
		
		if(task.getEndDatePlanned() != null && task.getStartDatePlanned() != null)	
			task.setWorkloadPlanned(calendarUtil.calculateWorkLoad(task.getStartDatePlanned(), task.getEndDatePlanned()));
		
		if(task.getEndDateReal() != null && task.getStartDateReal() != null)	
			task.setWorkloadReal(calendarUtil.calculateWorkLoad(task.getStartDateReal(), task.getEndDateReal()));
		
		if(task.getEndDateRevised() != null && task.getStartDateRevised() != null)	
			task.setWorkloadRevised(calendarUtil.calculateWorkLoad(task.getStartDateRevised(), task.getEndDateRevised()));
		
		
		logger.info("operation : " + joinPoint.getSignature().toShortString()
				+ " :successful "+task.getName());
	}

}

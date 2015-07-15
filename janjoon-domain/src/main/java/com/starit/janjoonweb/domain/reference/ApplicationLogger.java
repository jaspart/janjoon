package com.starit.janjoonweb.domain.reference;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.util.StringUtils;

import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJRequirementService;
import com.starit.janjoonweb.domain.JJStatus;
import com.starit.janjoonweb.domain.JJStatusService;
import com.starit.janjoonweb.domain.JJTask;

@Aspect
@Configurable
public class ApplicationLogger {

	@Autowired
	JJRequirementService jJRequirementService;

	@Autowired
	JJStatusService jJStatusService;

	public void setjJRequirementService(
			JJRequirementService jJRequirementService) {
		this.jJRequirementService = jJRequirementService;
	}

	public void setjJStatusService(JJStatusService jJStatusService) {
		this.jJStatusService = jJStatusService;
	}

	static Logger logger = Logger.getLogger("ServiceDAOLogger");

	@AfterReturning("execution(* com.starit.janjoonweb.domain.*Service.*(..))")
	public void logAfter(JoinPoint joinPoint) {

		logger.info("operation : " + joinPoint.getSignature().toShortString()
				+ " :successful");

	}

	// @AfterReturning("execution(* com.starit.janjoonweb.domain.JJRequirement.getDescription(..))")
	// public void logAfterGetDescription(JoinPoint joinPoint) {
	//
	// logger.info("operation : " + joinPoint.getSignature().toShortString()
	// + " :successful");
	// JJRequirement requirement = (JJRequirement) joinPoint.getThis();
	// String description = requirement.getDescription();
	// if (FacesContext.getCurrentInstance() != null) {
	// int imgNumber = Math.min(StringUtils.countOccurrencesOf(
	// description, "<img"), StringUtils.countOccurrencesOf(
	// description, "/pages/ckeditor/getimage?imageId="));
	//
	// if (imgNumber > 0) {
	//
	// int k = 0;
	// HttpServletRequest request = ((HttpServletRequest) FacesContext
	// .getCurrentInstance().getExternalContext().getRequest());
	//
	// String url = "";
	// if (!request.getServerName().contains("localhost"))
	// url = "https" + "://" + request.getServerName()
	// + request.getContextPath() + "/images/";
	// else
	// url = "http" + "://" + request.getServerName() + ":"
	// + request.getServerPort()
	// + request.getContextPath() + "/images/";
	//
	// while (k < imgNumber) {
	// int kk = description.indexOf("alt=\"\" src=");
	//
	// description = description.substring(0,
	// description.indexOf("alt=\"\" src=")
	// + "alt=\"\" src=".length() + 1)
	// + url
	// + description.substring(description
	// .indexOf("?imageId=")
	// + "?imageId=".length());
	// k++;
	// }
	//
	// requirement.setDescription(description);
	// }
	//
	//
	//
	// }
	// }

	@AfterThrowing(pointcut = "execution(* com.starit.janjoonweb.domain.*Service.*(..))", throwing = "ex")
	public void logAfterEX(JoinPoint joinPoint, Throwable ex) {

		logger.error("exception throw operation : "
				+ joinPoint.getSignature().toShortString() + " :rised "
				+ ex.getMessage());
		if (ex instanceof EntityNotFoundException) {
			System.err.println("EntityNotFoundException");
			// Query query=entityManager.createQuery("");
		}

		if (ex instanceof JpaObjectRetrievalFailureException) {
			System.err.println("JpaObjectRetrievalFailureException");

			// for(String tableName:jJStatusService.getTablesName())
			// {
			// Query
			// query=entityManager.createQuery("UPDATE "+tableName+" r SET  version = NULL WHERE  r.version =0");
			// query.executeUpdate();
			// }
			//
			System.err.println(joinPoint.getThis().toString());
			System.err.println(joinPoint.getKind() + "/"
					+ joinPoint.getTarget().toString());

		}

	}

	// @After("execution(* com.starit.janjoonweb.domain.JJTask.setEndDateReal(..))")
	// public void setJJTaskWorkLoadReal(JoinPoint joinPoint)
	// {
	//
	// JJTask t=(JJTask) joinPoint.getThis();
	// if(t.getEndDateReal()!=null && t.getStartDateReal()!=null)
	// {
	// Date s=t.getStartDateReal();
	// Date f=t.getEndDateReal();
	// System.out.println(s+"/"+f);
	//
	// long w=f.getTime() - s.getTime();
	//
	// t.setWorkloadReal(Math.round(TimeUnit.MILLISECONDS.toHours(w/3)));
	// }
	//
	// }

	// @After("execution(* com.starit.janjoonweb.domain.JJTask.setEndDateRevised(..))")
	// public void setJJTaskWorkLoadRevised(JoinPoint joinPoint)
	// {
	//
	// JJTask t=(JJTask) joinPoint.getThis();
	//
	// if(t.getStartDateRevised()!=null && t.getEndDateRevised()!=null)
	// {
	// Date s=t.getStartDateRevised();
	// Date f=t.getEndDateRevised();
	// System.out.println(s+"/"+f);
	//
	// long w=f.getTime() - s.getTime();
	//
	// t.setWorkloadRevised(Math.round(TimeUnit.MILLISECONDS.toHours(w/3)));
	// }
	//
	// }

	// @After("execution(* com.starit.janjoonweb.domain.JJTaskService.updateJJTask(..))")
	public void startRequirement(JoinPoint joinPoint) {
		// JJTask task=(JJTask) joinPoint.getThis();

		Object[] args = joinPoint.getArgs();
		// JJStatus status=(JJStatus) args[0];
		JJTask task = (JJTask) args[0];
		JJStatus status = task.getStatus();

		if (status != null && task.getRequirement() != null) {

			JJRequirement req = jJRequirementService.findJJRequirement(task
					.getRequirement().getId());

			System.out.println(status.getName());

			if (status.getName().equalsIgnoreCase("IN PROGRESS")
					&& (req.getStatus() == null || !req.getStatus().getName()
							.equalsIgnoreCase("RELEASED"))) {

				JJStatus reqStatus = jJStatusService.getOneStatus("RELEASED",
						"Requirement", true);
				req.setStatus(reqStatus);
				jJRequirementService.updateJJRequirement(req);

			}

		}

	}
}

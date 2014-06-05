package com.starit.janjoonweb.domain.reference;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

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

	@AfterThrowing(pointcut = "execution(* com.starit.janjoonweb.domain.*Service.*(..))", throwing = "ex")
	public void logAfterEX(JoinPoint joinPoint, Throwable ex) {

		logger.error("exception throw operation : "
				+ joinPoint.getSignature().toShortString() + " :rised "
				+ ex.getMessage());

	}

	@After("execution(* com.starit.janjoonweb.domain.JJTaskService.updateJJTask(..))")
	public void startRequirement(JoinPoint joinPoint) {
		// JJTask task=(JJTask) joinPoint.getThis();

		Object[] args = joinPoint.getArgs();
		// JJStatus status=(JJStatus) args[0];
		JJTask task = (JJTask) args[0];
		JJStatus status = task.getStatus();

		System.out.println("setStatus");

		if (status != null) {

			System.out.println(status.getName());
			if (status.getName().equalsIgnoreCase("IN PROGRESS")) {

				JJRequirement req = task.getRequirement();

				if (req != null) {
					System.out.println(req.getName());
					if (!req.getStatus().getName().equalsIgnoreCase("RELEASED")) {
						JJStatus reqStatus = jJStatusService.getOneStatus(
								"RELEASED", "JJRequirement", true);
						req.setStatus(reqStatus);
						jJRequirementService.updateJJRequirement(req);
						System.out.println(req.getName());

					}
				} else {
					JJStatus reqStatus = jJStatusService.getOneStatus(
							"RELEASED", "JJRequirement", true);
					req.setStatus(reqStatus);
					jJRequirementService.updateJJRequirement(req);
				}
			}
		}

	}

}

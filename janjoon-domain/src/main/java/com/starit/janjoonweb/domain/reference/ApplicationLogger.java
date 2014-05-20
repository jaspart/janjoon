package com.starit.janjoonweb.domain.reference;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

@Aspect
public class ApplicationLogger {

	static Logger logger = Logger.getLogger("ServiceDAOLogger");

	@AfterReturning("execution(* com.starit.janjoonweb.domain.*Service.*(..))")
	public void logAfter(JoinPoint joinPoint) {
		
		logger.info("operation : " + joinPoint.getSignature().toShortString()
				+ " :successful");

	}	
	
	@AfterThrowing(pointcut="execution(* com.starit.janjoonweb.domain.*Service.*(..))",throwing = "ex")
	public void logAfterEX(JoinPoint joinPoint,Throwable ex) {
		
		logger.error("exception throw operation : " + joinPoint.getSignature().toShortString()
				+ " :rised "+ex.getMessage());

	}

}

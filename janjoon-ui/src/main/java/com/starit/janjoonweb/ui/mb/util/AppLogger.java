package com.starit.janjoonweb.ui.mb.util;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class AppLogger {
	
	static Logger logger = Logger.getLogger("ManagedBean-Logger");
	
	@AfterReturning("execution(* com.starit.janjoonweb.domain.*Service.*(..))")
	public void logAfter(JoinPoint joinPoint) {
		
		logger.info("operation : " + joinPoint.getSignature().toShortString()
				+ " :successful");

	}

}

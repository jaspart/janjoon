package com.starit.janjoonweb.ui.mb.util;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class AppLogger {
	
	static Logger logger = Logger.getLogger("ManagedBean-Logger");
	
//	@AfterReturning("execution(* com.starit.janjoonweb.domain.*Service.*(..))")
//	public void logAfter(JoinPoint joinPoint) {
//		
//		logger.info("operation : " + joinPoint.getSignature().toShortString()
//				+ " :successful");
//
//	}
	
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

}

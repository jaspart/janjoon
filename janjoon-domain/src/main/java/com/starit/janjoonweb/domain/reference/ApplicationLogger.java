package com.starit.janjoonweb.domain.reference;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

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
	
	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;

	}

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
		if(ex instanceof EntityNotFoundException)
		{
			System.err.println("EntityNotFoundException");
			//Query query=entityManager.createQuery("");
		}
		
		if(ex instanceof JpaObjectRetrievalFailureException)
		{
			System.err.println("JpaObjectRetrievalFailureException");
			
//			for(String tableName:jJStatusService.getTablesName())
//			{
//				Query query=entityManager.createQuery("UPDATE "+tableName+" r SET  version = NULL WHERE  r.version =0");			
//				query.executeUpdate();
//			}
//			
			System.err.println(joinPoint.getThis().toString());
			System.err.println(joinPoint.getKind()+"/"+joinPoint.getTarget().toString());		
			
			
			
			
		}

	}
	
//	@After("execution(* com.starit.janjoonweb.domain.JJTask.setEndDateReal(..))")
//	public void setJJTaskWorkLoadReal(JoinPoint joinPoint)
//	{
//	
//		JJTask t=(JJTask) joinPoint.getThis();
//		if(t.getEndDateReal()!=null && t.getStartDateReal()!=null)
//		{
//			Date s=t.getStartDateReal();
//			Date f=t.getEndDateReal();
//			System.out.println(s+"/"+f);
//			
//			long w=f.getTime() - s.getTime();
//			
//			t.setWorkloadReal(Math.round(TimeUnit.MILLISECONDS.toHours(w/3)));
//		}
//		
//	}
	
//	@After("execution(* com.starit.janjoonweb.domain.JJTask.setEndDateRevised(..))")
//	public void setJJTaskWorkLoadRevised(JoinPoint joinPoint)
//	{
//	
//		JJTask t=(JJTask) joinPoint.getThis();
//		
//		if(t.getStartDateRevised()!=null && t.getEndDateRevised()!=null)
//		{			
//			Date s=t.getStartDateRevised();
//			Date f=t.getEndDateRevised();
//			System.out.println(s+"/"+f);
//			
//			long w=f.getTime() - s.getTime();
//			
//			t.setWorkloadRevised(Math.round(TimeUnit.MILLISECONDS.toHours(w/3)));
//		}
//		
//	}

	@After("execution(* com.starit.janjoonweb.domain.JJTaskService.updateJJTask(..))")
	public void startRequirement(JoinPoint joinPoint) {
		// JJTask task=(JJTask) joinPoint.getThis();

		Object[] args = joinPoint.getArgs();
		// JJStatus status=(JJStatus) args[0];
		JJTask task = (JJTask) args[0];
		JJStatus status = task.getStatus();	
		

		if (status != null && task.getRequirement()!=null) {		
			
		
			JJRequirement req = jJRequirementService.findJJRequirement(task
					.getRequirement().getId());

			System.out.println(status.getName());
			
			if (status.getName().equalsIgnoreCase("IN PROGRESS") ) {

				JJStatus reqStatus = jJStatusService.getOneStatus("RELEASED",
						"JJRequirement", true);
				req.setStatus(reqStatus);
				jJRequirementService.updateJJRequirement(req);			

			}

		}
		
		
	}
}

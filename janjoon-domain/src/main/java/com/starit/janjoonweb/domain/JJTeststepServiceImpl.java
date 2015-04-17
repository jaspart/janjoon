package com.starit.janjoonweb.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;


public class JJTeststepServiceImpl implements JJTeststepService {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private JJTestcaseService jJTestcaseService;	
	

	public void setjJTestcaseService(JJTestcaseService jJTestcaseService) {
		this.jJTestcaseService = jJTestcaseService;
	}
	
	@Autowired
	private JJRequirementService jJRequirementService;	
	

	public void setjJRequirementService(JJRequirementService JJRequirementService) {
		this.jJRequirementService = JJRequirementService;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<JJTeststep> getTeststeps(JJTestcase testcase,
			boolean onlyActif, boolean sortedByOrder) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJTeststep> criteriaQuery = criteriaBuilder
				.createQuery(JJTeststep.class);

		Root<JJTeststep> from = criteriaQuery.from(JJTeststep.class);

		CriteriaQuery<JJTeststep> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		if (testcase != null) {
			predicates
					.add(criteriaBuilder.equal(from.get("testcase"), testcase));
		}

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		if (sortedByOrder) {
			select.orderBy(criteriaBuilder.asc(from.get("ordering")));
		}

		TypedQuery<JJTeststep> result = entityManager.createQuery(select);
		return result.getResultList();

	}

	@Override
	public void saveTeststeps(Set<JJTeststep> teststeps) {
		jJTeststepRepository.save(teststeps);
	}

	@Override
	public void updateTeststeps(Set<JJTeststep> teststeps) {
		jJTeststepRepository.save(teststeps);
	}

	public void saveJJTeststep(JJTeststep JJTeststep_) {

		jJTeststepRepository.save(JJTeststep_);
		JJTeststep_ = jJTeststepRepository.findOne(JJTeststep_.getId());
	}

	public JJTeststep updateJJTeststep(JJTeststep JJTeststep_) {
		
		jJTeststepRepository.save(JJTeststep_);
		JJTeststep_ = jJTeststepRepository.findOne(JJTeststep_.getId());
		return JJTeststep_;
	}	

	@Override
	public List<JJTeststep> getJJtestSteps(JJRequirement requirement) {
		
		List<JJTeststep> jjTeststeps=new ArrayList<JJTeststep>();
		if(requirement!=null)
		{
			List<JJTestcase> testcases=jJTestcaseService.getJJtestCases(requirement);
			
			if(testcases!=null)
			{
				for (JJTestcase jjTestcase : testcases) {
					
					List<JJTeststep> steps=getTeststeps(jjTestcase, true, true);
					if (steps!=null) {
						
						jjTeststeps.addAll(steps);
					}
					
				}
			}
		}

		return jjTeststeps;
	}

	@Override
	public List<JJTeststep> getJJtestSteps(JJProject project) {
		
		Map<JJProject, JJProduct> map = new HashMap<JJProject, JJProduct>();
		map.put(project, null);
		List<JJRequirement> requirements=jJRequirementService.getRequirements(null,map,null);
		List<JJTeststep> jjTeststeps=new ArrayList<JJTeststep>();
		if(requirements!=null)
		{
			for (JJRequirement jjRequirement : requirements) {
				jjTeststeps.addAll(getJJtestSteps(jjRequirement));
			}
		}
		return jjTeststeps;
	}
	
	
	public void deleteJJTeststep(JJTeststep JJTeststep_) {		
		//entityManager.remove(JJTeststep_);
		//JJTeststepexecution teststep	
		Query q=entityManager.createQuery("DELETE FROM JJTeststepexecution c WHERE c.teststep = :p");
		q.setParameter("p", JJTeststep_).executeUpdate();
		q=entityManager.createQuery("DELETE FROM JJBug c WHERE c.teststep = :p");
		q.setParameter("p", JJTeststep_).executeUpdate();		
		Query query=entityManager.createQuery("DELETE FROM JJTeststep c WHERE c.id = :p");
		query.setParameter("p", JJTeststep_.getId()).executeUpdate();
		
	}

	@Override
	public List<JJTeststep> getJJtestSteps(JJRequirement requirement,
			JJProject project) {
		
		if(requirement==null)
			return getJJtestSteps(project);
		else {
			return getJJtestSteps(requirement);
			
		}		
	
	}
}

package com.starit.janjoonweb.domain;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
		
		List<JJRequirement> requirements=jJRequirementService.getRequirements(project, null, null);
		List<JJTeststep> jjTeststeps=new ArrayList<JJTeststep>();
		if(requirements!=null)
		{
			for (JJRequirement jjRequirement : requirements) {
				jjTeststeps.addAll(getJJtestSteps(jjRequirement));
			}
		}
		return jjTeststeps;
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

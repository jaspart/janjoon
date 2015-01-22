package com.starit.janjoonweb.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class JJTeststepexecutionServiceImpl implements
		JJTeststepexecutionService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<JJTeststepexecution> getAllTeststepexecutions() {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJTeststepexecution> criteriaQuery = criteriaBuilder
				.createQuery(JJTeststepexecution.class);

		Root<JJTeststepexecution> from = criteriaQuery
				.from(JJTeststepexecution.class);

		CriteriaQuery<JJTeststepexecution> select = criteriaQuery.select(from);

		Predicate predicate = criteriaBuilder.equal(from.get("enabled"), true);

		select.where(predicate);

		TypedQuery<JJTeststepexecution> result = entityManager
				.createQuery(select);
		return result.getResultList();

	}

	@Override
	public List<JJTeststepexecution> getTeststepexecutions(
			JJTestcaseexecution testcaseexecution, boolean onlyActif) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJTeststepexecution> criteriaQuery = criteriaBuilder
				.createQuery(JJTeststepexecution.class);

		Root<JJTeststepexecution> from = criteriaQuery
				.from(JJTeststepexecution.class);

		CriteriaQuery<JJTeststepexecution> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();
		if (testcaseexecution != null) {
			predicates.add(criteriaBuilder.equal(from.get("testcaseexecution"),
					testcaseexecution));
		}
		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		select.where(predicates.toArray(new Predicate[] {}));
		TypedQuery<JJTeststepexecution> result = entityManager
				.createQuery(select);

		return result.getResultList();

	}

	@Override
	public JJTeststepexecution getTeststepexecutionWithTeststepAndBuild(
			JJTeststep teststep, JJBuild build) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJTeststepexecution> criteriaQuery = criteriaBuilder
				.createQuery(JJTeststepexecution.class);

		Root<JJTeststepexecution> from = criteriaQuery
				.from(JJTeststepexecution.class);

		CriteriaQuery<JJTeststepexecution> select = criteriaQuery.select(from);

		Predicate predicate1 = criteriaBuilder.equal(from.get("enabled"), true);
		Predicate predicate2 = criteriaBuilder.equal(from.get("teststep"),
				teststep);
		Predicate predicate3 = criteriaBuilder.equal(from.join("teststep").join("testcase").get("build"), build);

		select.where(criteriaBuilder.and(predicate1, predicate2, predicate3));

		TypedQuery<JJTeststepexecution> result = entityManager
				.createQuery(select);
		if (result.getResultList().size() > 0)
			return result.getResultList().get(0);
		else
			return null;

	}

	@Override
	public void saveteststepexecutions(
			List<JJTeststepexecution> teststepexecutions) {
		jJTeststepexecutionRepository.save(teststepexecutions);
	}
	
	public void saveJJTeststepexecution(JJTeststepexecution JJTeststepexecution_) {
		
        jJTeststepexecutionRepository.save(JJTeststepexecution_);
        JJTeststepexecution_=jJTeststepexecutionRepository.findOne(JJTeststepexecution_.getId());
    }
    
    public JJTeststepexecution updateJJTeststepexecution(JJTeststepexecution JJTeststepexecution_) {
        jJTeststepexecutionRepository.save(JJTeststepexecution_);
        JJTeststepexecution_=jJTeststepexecutionRepository.findOne(JJTeststepexecution_.getId());
        return JJTeststepexecution_;
    }

}

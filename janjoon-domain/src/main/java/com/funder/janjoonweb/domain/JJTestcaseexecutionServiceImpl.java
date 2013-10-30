package com.funder.janjoonweb.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class JJTestcaseexecutionServiceImpl implements
		JJTestcaseexecutionService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<JJTestcaseexecution> getAllTestcaseexecutions() {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJTestcaseexecution> criteriaQuery = criteriaBuilder
				.createQuery(JJTestcaseexecution.class);

		Root<JJTestcaseexecution> from = criteriaQuery
				.from(JJTestcaseexecution.class);

		CriteriaQuery<JJTestcaseexecution> select = criteriaQuery.select(from);

		Predicate predicate = criteriaBuilder.equal(from.get("enabled"), true);

		select.where(predicate);

		TypedQuery<JJTestcaseexecution> result = entityManager
				.createQuery(select);
		return result.getResultList();

	}

	@Override
	public JJTestcaseexecution getTestcaseexecutionWithTestcaseAndBuild(
			JJTestcase testcase, JJBuild build) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJTestcaseexecution> criteriaQuery = criteriaBuilder
				.createQuery(JJTestcaseexecution.class);

		Root<JJTestcaseexecution> from = criteriaQuery
				.from(JJTestcaseexecution.class);

		CriteriaQuery<JJTestcaseexecution> select = criteriaQuery.select(from);

		Predicate predicate1 = criteriaBuilder.equal(from.get("enabled"), true);
		Predicate predicate2 = criteriaBuilder.equal(from.get("testcase"),
				testcase);
		Predicate predicate3 = criteriaBuilder.equal(from.get("build"), build);

		select.where(criteriaBuilder.and(predicate1, predicate2, predicate3));

		TypedQuery<JJTestcaseexecution> result = entityManager
				.createQuery(select);
		if (result.getResultList().size() > 0)
			return result.getResultList().get(0);
		else
			return null;

	}
}

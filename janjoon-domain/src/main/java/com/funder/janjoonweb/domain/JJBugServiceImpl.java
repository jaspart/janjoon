package com.funder.janjoonweb.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class JJBugServiceImpl implements JJBugService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/**
	 * Get Enabled JJBuild List
	 */

	@Override
	public List<JJBug> getAllJJBugs() {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJBug> criteriaQuery = criteriaBuilder
				.createQuery(JJBug.class);

		Root<JJBug> from = criteriaQuery.from(JJBug.class);

		CriteriaQuery<JJBug> select = criteriaQuery.select(from);

		Predicate predicate = criteriaBuilder.equal(from.get("enabled"), true);

		select.where(predicate);

		TypedQuery<JJBug> result = entityManager.createQuery(select);
		return result.getResultList();

	}

	@Override
	public JJBug getBugWithTestcaseAndProject(JJTestcase testcase,
			JJProject project) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJBug> criteriaQuery = criteriaBuilder
				.createQuery(JJBug.class);

		Root<JJBug> from = criteriaQuery.from(JJBug.class);

		CriteriaQuery<JJBug> select = criteriaQuery.select(from);

		Predicate predicate1 = criteriaBuilder.equal(from.get("enabled"), true);
		Predicate predicate2 = criteriaBuilder.equal(from.get("testcase"),
				testcase);
		Predicate predicate3 = criteriaBuilder.equal(from.get("project"),
				project);

		select.where(criteriaBuilder.and(predicate1, predicate2, predicate3));

		TypedQuery<JJBug> result = entityManager.createQuery(select);
		if (result.getResultList().size() > 0)
			return result.getResultList().get(0);
		else
			return null;

	}

}

package com.starit.janjoonweb.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class JJSprintServiceImpl implements JJSprintService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/**
	 * Get Enabled JJSprint List
	 */

	@Override
	public List<JJSprint> getAllJJSprints() {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJSprint> criteriaQuery = criteriaBuilder
				.createQuery(JJSprint.class);

		Root<JJSprint> from = criteriaQuery.from(JJSprint.class);

		CriteriaQuery<JJSprint> select = criteriaQuery.select(from);

		Predicate predicate = criteriaBuilder.equal(from.get("enabled"), true);

		select.where(predicate);

		TypedQuery<JJSprint> result = entityManager.createQuery(select);
		return result.getResultList();

	}
}

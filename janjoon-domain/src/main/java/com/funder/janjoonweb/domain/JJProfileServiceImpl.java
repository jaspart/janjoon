package com.funder.janjoonweb.domain;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class JJProfileServiceImpl implements JJProfileService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public JJProfile getJJProfileWithName(String name) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJProfile> criteriaQuery = criteriaBuilder
				.createQuery(JJProfile.class);

		Root<JJProfile> from = criteriaQuery.from(JJProfile.class);

		CriteriaQuery<JJProfile> select = criteriaQuery.select(from);

		Predicate predicate = criteriaBuilder.equal(from.get("name"), name);

		select.where(predicate);

		TypedQuery<JJProfile> result = entityManager.createQuery(select);

		if (result.getResultList().size() > 0)
			return result.getResultList().get(0);

		else
			return null;

	}
}

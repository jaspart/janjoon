package com.funder.janjoonweb.domain;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class JJStatusServiceImpl implements JJStatusService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public JJStatus getJJStatusWithName(String name) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJStatus> criteriaQuery = criteriaBuilder
				.createQuery(JJStatus.class);

		Root<JJStatus> from = criteriaQuery.from(JJStatus.class);

		CriteriaQuery<JJStatus> select = criteriaQuery.select(from);
		Predicate predicate1 = criteriaBuilder.equal(from.get("name"), name);
		Predicate predicate2 = criteriaBuilder.equal(from.get("enabled"), true);

		select.where(criteriaBuilder.and(predicate1, predicate2));

		TypedQuery<JJStatus> result = entityManager.createQuery(select);
		if (result.getResultList().size() == 0)
			return null;
		else
			return result.getSingleResult();

	}
}

package com.funder.janjoonweb.domain;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class JJRightServiceImpl implements JJRightService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public JJRight getJJRightWithName(String name) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJRight> criteriaQuery = criteriaBuilder.createQuery(JJRight.class);

		Root<JJRight> from = criteriaQuery.from(JJRight.class);

		CriteriaQuery<JJRight> select = criteriaQuery.select(from);
		Predicate predicate1 = criteriaBuilder.equal(from.get("name"), name);
		Predicate predicate2 = criteriaBuilder.equal(from.get("enabled"), true);

		select.where(criteriaBuilder.and(predicate1, predicate2));

		TypedQuery<JJRight> result = entityManager.createQuery(select);
		if (result.getResultList().size() == 0)
			return null;
		else
			return result.getSingleResult();
	}

	@Override
	public List<JJRight> getAllJJRights() {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJRight> criteriaQuery = criteriaBuilder.createQuery(JJRight.class);

		Root<JJRight> from = criteriaQuery.from(JJRight.class);

		CriteriaQuery<JJRight> select = criteriaQuery.select(from);

		Predicate predicate = criteriaBuilder.equal(from.get("basic"), true);

		select.where(predicate);

		TypedQuery<JJRight> result = entityManager.createQuery(select);
		return result.getResultList();

	}

}

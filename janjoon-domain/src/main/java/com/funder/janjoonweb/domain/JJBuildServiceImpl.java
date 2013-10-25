package com.funder.janjoonweb.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


public class JJBuildServiceImpl implements JJBuildService {
	
	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	/**
	 * Get Enabled JJBuild List
	 */

	@Override
	public List<JJBuild> getAllJJBuilds() {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJBuild> criteriaQuery = criteriaBuilder
				.createQuery(JJBuild.class);

		Root<JJBuild> from = criteriaQuery.from(JJBuild.class);

		CriteriaQuery<JJBuild> select = criteriaQuery.select(from);

		Predicate predicate = criteriaBuilder.equal(from.get("enabled"), true);

		select.where(predicate);

		TypedQuery<JJBuild> result = entityManager.createQuery(select);
		return result.getResultList();

	}
}

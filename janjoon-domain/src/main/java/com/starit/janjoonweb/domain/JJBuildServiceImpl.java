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

public class JJBuildServiceImpl implements JJBuildService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;

	}

	@Override
	public List<JJBuild> getBuilds(JJVersion version, boolean withVersion,
			boolean onlyActif) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJBuild> criteriaQuery = criteriaBuilder
				.createQuery(JJBuild.class);

		Root<JJBuild> from = criteriaQuery.from(JJBuild.class);

		CriteriaQuery<JJBuild> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		if (withVersion) {
			if (version != null) {
				predicates.add(criteriaBuilder.equal(from.get("versioning"),
						version));
			} else {
				predicates.add(criteriaBuilder.isNull(from.get("versioning")));
			}
		}

		select.where(predicates.toArray(new Predicate[] {}));

		TypedQuery<JJBuild> result = entityManager.createQuery(select);
		return result.getResultList();

	}
}

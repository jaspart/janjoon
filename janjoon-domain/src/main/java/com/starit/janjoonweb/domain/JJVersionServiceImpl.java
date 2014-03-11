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

public class JJVersionServiceImpl implements JJVersionService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	// New Generic

	@Override
	public List<JJVersion> getVersions(boolean onlyActif, boolean withProduct,
			JJProduct product) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJVersion> criteriaQuery = criteriaBuilder
				.createQuery(JJVersion.class);

		Root<JJVersion> from = criteriaQuery.from(JJVersion.class);

		CriteriaQuery<JJVersion> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		if (withProduct) {
			if (product != null) {
				predicates.add(criteriaBuilder.equal(from.get("product"),
						product));
			}
		} else {
			predicates.add(criteriaBuilder.isNull(from.get("product")));
		}
		select.where(predicates.toArray(new Predicate[] {}));

		TypedQuery<JJVersion> result = entityManager.createQuery(select);
		return result.getResultList();

	}
}

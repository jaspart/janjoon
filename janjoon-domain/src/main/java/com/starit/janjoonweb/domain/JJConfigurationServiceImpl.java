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

public class JJConfigurationServiceImpl implements JJConfigurationService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<JJConfiguration> getConfigs(boolean onlyActif) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJConfiguration> criteriaQuery = criteriaBuilder
				.createQuery(JJConfiguration.class);

		Root<JJConfiguration> from = criteriaQuery.from(JJConfiguration.class);

		CriteriaQuery<JJConfiguration> select = criteriaQuery.select(from);

		if (onlyActif) {
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
			select.where(predicates.toArray(new Predicate[] {}));
		}

		TypedQuery<JJConfiguration> result = entityManager.createQuery(select);
		return result.getResultList();
	}

}

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

public class JJProfileServiceImpl implements JJProfileService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public JJProfile getOneProfile(String name, boolean onlyActif) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJProfile> criteriaQuery = criteriaBuilder
				.createQuery(JJProfile.class);

		Root<JJProfile> from = criteriaQuery.from(JJProfile.class);

		CriteriaQuery<JJProfile> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		if (name != null) {
			predicates.add(criteriaBuilder.equal(from.get("name"), name));
		}

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		TypedQuery<JJProfile> result = entityManager.createQuery(select);

		if (result.getResultList().size() > 0)
			return result.getResultList().get(0);

		else
			return null;

	}

	public List<JJProfile> getProfiles(boolean onlyActif) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJProfile> criteriaQuery = criteriaBuilder
				.createQuery(JJProfile.class);

		Root<JJProfile> from = criteriaQuery.from(JJProfile.class);

		CriteriaQuery<JJProfile> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		TypedQuery<JJProfile> result = entityManager.createQuery(select);

		return result.getResultList();

	}
}

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

public class JJCriticityServiceImpl implements JJCriticityService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;

	}

	@Override
	public List<JJCriticity> getCriticities(String object, boolean onlyActif) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJCriticity> criteriaQuery = criteriaBuilder
				.createQuery(JJCriticity.class);

		Root<JJCriticity> from = criteriaQuery.from(JJCriticity.class);

		CriteriaQuery<JJCriticity> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		if (object != null) {
			predicates.add(criteriaBuilder.equal(from.get("objet"), object));
		}

		select.where(predicates.toArray(new Predicate[] {}));

		TypedQuery<JJCriticity> result = entityManager.createQuery(select);
		return result.getResultList();

	}

	@Override
	public JJCriticity getCriticityByName(String name, boolean onlyActif) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJCriticity> criteriaQuery = criteriaBuilder
				.createQuery(JJCriticity.class);

		Root<JJCriticity> from = criteriaQuery.from(JJCriticity.class);

		CriteriaQuery<JJCriticity> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();
		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		if (name != null) {
			predicates.add(criteriaBuilder.equal(from.get("name"), name));
		}

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		TypedQuery<JJCriticity> result = entityManager.createQuery(select);
		return result.getResultList().get(0);
	}

}

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

			List<Predicate> orPredicates = new ArrayList<Predicate>();
			orPredicates.add(criteriaBuilder.equal(
					criteriaBuilder.lower(from.<String> get("objet")),
					object.toLowerCase()));
			orPredicates.add(
					criteriaBuilder.equal(
							criteriaBuilder.lower(from.<String> get("objet")),
							"jj"+object.toLowerCase()));			
			Predicate orPredicate = criteriaBuilder
					.or(orPredicates.toArray(new Predicate[]{}));

			predicates.add(orPredicate);

		}

		select.where(predicates.toArray(new Predicate[]{}));

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
			predicates.add(criteriaBuilder.equal(
					criteriaBuilder.lower(from.<String> get("name")),
					name.toLowerCase()));			
		}

		select.where(
				criteriaBuilder.and(predicates.toArray(new Predicate[]{})));

		TypedQuery<JJCriticity> result = entityManager.createQuery(select);
		return result.getResultList().get(0);
	}

	public void saveJJCriticity(JJCriticity JJCriticity_) {

		jJCriticityRepository.save(JJCriticity_);
		JJCriticity_ = jJCriticityRepository.findOne(JJCriticity_.getId());
	}

	public JJCriticity updateJJCriticity(JJCriticity JJCriticity_) {
		jJCriticityRepository.save(JJCriticity_);
		JJCriticity_ = jJCriticityRepository.findOne(JJCriticity_.getId());
		return JJCriticity_;
	}

}

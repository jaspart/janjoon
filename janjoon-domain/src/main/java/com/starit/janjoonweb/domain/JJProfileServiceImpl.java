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
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang3.mutable.MutableInt;

public class JJProfileServiceImpl implements JJProfileService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public List<JJProfile> load(MutableInt size, int first, int pageSize,
			boolean isSuperAdmin) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJProfile> criteriaQuery = criteriaBuilder
				.createQuery(JJProfile.class);

		Root<JJProfile> from = criteriaQuery.from(JJProfile.class);

		CriteriaQuery<JJProfile> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (!isSuperAdmin) {
			Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
			Root<JJRight> fromRight = subquery.from(JJRight.class);
			List<Predicate> predicatesRight = new ArrayList<Predicate>();
			subquery.select(fromRight.get("profile").<Long> get("id"));
			predicatesRight.add(criteriaBuilder.and(criteriaBuilder.equal(
					fromRight.get("x"), true), criteriaBuilder.equal(
					criteriaBuilder.lower(fromRight.<String> get("objet")),
					"company".toLowerCase())));
			predicatesRight.add(criteriaBuilder.and(criteriaBuilder.equal(
					fromRight.get("x"), true), criteriaBuilder.equal(
					criteriaBuilder.lower(fromRight.<String> get("objet")),
					"*".toLowerCase())));

			subquery.where(criteriaBuilder.and(criteriaBuilder
					.or(predicatesRight.toArray(new Predicate[] {})),
					criteriaBuilder.equal(fromRight.get("enabled"), true)));
			
			predicates.add(criteriaBuilder.in(from.get("id")).value(subquery).not());

		}
		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		TypedQuery<JJProfile> result = entityManager.createQuery(select);
		result.setFirstResult(first);
		result.setMaxResults(pageSize);

		CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
		cq.select(criteriaBuilder.count(cq.from(JJProfile.class)));
		entityManager.createQuery(cq);
		cq.where(predicates.toArray(new Predicate[] {}));
		size.setValue(entityManager.createQuery(cq).getSingleResult());

		return result.getResultList();

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

			predicates.add(criteriaBuilder.equal(
					criteriaBuilder.lower(from.<String> get("name")),
					name.toLowerCase()));
		}

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		TypedQuery<JJProfile> result = entityManager.createQuery(select);

		if (result.getResultList().size() > 0)
			return result.getResultList().get(0);

		else
			return null;

	}

	public List<JJProfile> getProfiles(boolean onlyActif, boolean isSuperAdmin) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJProfile> criteriaQuery = criteriaBuilder
				.createQuery(JJProfile.class);

		Root<JJProfile> from = criteriaQuery.from(JJProfile.class);

		CriteriaQuery<JJProfile> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (!isSuperAdmin) {
			Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
			Root<JJRight> fromRight = subquery.from(JJRight.class);
			List<Predicate> predicatesRight = new ArrayList<Predicate>();
			subquery.select(fromRight.get("profile").<Long> get("id"));
			predicatesRight.add(criteriaBuilder.and(criteriaBuilder.equal(
					fromRight.get("x"), true), criteriaBuilder.equal(
					criteriaBuilder.lower(fromRight.<String> get("objet")),
					"company".toLowerCase())));
			predicatesRight.add(criteriaBuilder.and(criteriaBuilder.equal(
					fromRight.get("x"), true), criteriaBuilder.equal(
					criteriaBuilder.lower(fromRight.<String> get("objet")),
					"*".toLowerCase())));

			subquery.where(criteriaBuilder.and(criteriaBuilder
					.or(predicatesRight.toArray(new Predicate[] {})),
					criteriaBuilder.equal(fromRight.get("enabled"), true)));

			predicates.add(criteriaBuilder.in(from.get("id")).value(subquery)
					.not());

		}

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		TypedQuery<JJProfile> result = entityManager.createQuery(select);

		return result.getResultList();

	}

	public void saveJJProfile(JJProfile JJProfile_) {

		jJProfileRepository.save(JJProfile_);
		JJProfile_ = jJProfileRepository.findOne(JJProfile_.getId());
	}

	public JJProfile updateJJProfile(JJProfile JJProfile_) {
		jJProfileRepository.save(JJProfile_);
		JJProfile_ = jJProfileRepository.findOne(JJProfile_.getId());
		return JJProfile_;
	}
}

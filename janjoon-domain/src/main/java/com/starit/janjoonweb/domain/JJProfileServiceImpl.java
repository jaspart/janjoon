package com.starit.janjoonweb.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

public class JJProfileServiceImpl implements JJProfileService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public List<JJProfile> load(MutableInt size, int first, int pageSize,
			List<SortMeta> multiSortMeta, Map<String, Object> filters,
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
			predicates.add(criteriaBuilder.in(from.get("id")).value(subquery)
					.not());

		}
		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

		if (filters != null && !filters.isEmpty()) {

			String objectComponent = (String) filters.get("objectComponent");
			Boolean rComponent = (Boolean) filters.get("rComponent");
			Boolean wComponent = (Boolean) filters.get("wComponent");
			Boolean xComponent = (Boolean) filters.get("xComponent");

			predicates.add(criteriaBuilder.like(from.<String> get("name"), "%"
					+ filters.get("globalFilter") + "%"));

			List<Predicate> allpredicates = new ArrayList<Predicate>();
			Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
			Root<JJRight> fromRight = subquery.from(JJRight.class);

			if (objectComponent != null)
				allpredicates.add(criteriaBuilder.or(criteriaBuilder.equal(
						criteriaBuilder.lower(fromRight.<String> get("objet")),
						objectComponent.toLowerCase()), criteriaBuilder.equal(
						criteriaBuilder.lower(fromRight.<String> get("objet")),
						"*".toLowerCase())));

			if (rComponent != null)
				allpredicates.add(criteriaBuilder.equal(fromRight.get("r"),
						rComponent));

			if (wComponent != null)
				allpredicates.add(criteriaBuilder.equal(fromRight.get("w"),
						wComponent));

			if (xComponent != null)
				allpredicates.add(criteriaBuilder.equal(fromRight.get("x"),
						xComponent));

			if (!allpredicates.isEmpty()) {

				List<Predicate> predicatesRight = new ArrayList<Predicate>();
				subquery.select(fromRight.get("profile").<Long> get("id"));

				predicatesRight.add(criteriaBuilder.and(allpredicates
						.toArray(new Predicate[] {})));

				// predicatesRight.add(criteriaBuilder.and(criteriaBuilder.equal(
				// fromRight.get("x"), xComponent), criteriaBuilder.equal(
				// fromRight.get("w"), wComponent), criteriaBuilder.equal(
				// fromRight.get("r"), rComponent), criteriaBuilder.equal(
				// criteriaBuilder.lower(fromRight.<String> get("objet")),
				// objectComponent.toLowerCase())));
				// predicatesRight.add(criteriaBuilder.and(criteriaBuilder.equal(
				// fromRight.get("x"), xComponent), criteriaBuilder.equal(
				// fromRight.get("w"), wComponent), criteriaBuilder.equal(
				// fromRight.get("r"), rComponent), criteriaBuilder.equal(
				// criteriaBuilder.lower(fromRight.<String> get("objet")),
				// "*".toLowerCase())));

				subquery.where(criteriaBuilder.and(criteriaBuilder
						.or(predicatesRight.toArray(new Predicate[] {})),
						criteriaBuilder.equal(fromRight.get("enabled"), true)));
				predicates.add(criteriaBuilder.in(from.get("id")).value(
						subquery));
			}

		}

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		if (multiSortMeta != null) {
			for (SortMeta sortMeta : multiSortMeta) {
				String sortField = sortMeta.getSortField();
				SortOrder sortOrder = sortMeta.getSortOrder();

				if (sortOrder.equals(SortOrder.DESCENDING))
					select.orderBy(criteriaBuilder.desc(from.get(sortField)));
				else if (sortOrder.equals(SortOrder.ASCENDING)) {
					select.orderBy(criteriaBuilder.asc(from.get(sortField)));

				}
			}
		}

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

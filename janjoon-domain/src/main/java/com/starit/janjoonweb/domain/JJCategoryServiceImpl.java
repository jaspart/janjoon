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

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import com.starit.janjoonweb.domain.reference.StrFunction;

public class JJCategoryServiceImpl implements JJCategoryService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public List<JJCategory> load(MutableInt size, int first, int pageSize,
			List<SortMeta> multiSortMeta, Map<String, Object> filters,
			JJCompany company) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJCategory> criteriaQuery = criteriaBuilder
				.createQuery(JJCategory.class);

		Root<JJCategory> from = criteriaQuery.from(JJCategory.class);

		CriteriaQuery<JJCategory> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

		if (company != null) {
			Predicate condition1 = criteriaBuilder.equal(from.get("company"),
					company);
			Predicate condition2 = criteriaBuilder.isNull(from.get("company"));
			predicates.add(criteriaBuilder.or(condition1, condition2));
		}

		if (filters != null) {
			Iterator<Entry<String, Object>> it = filters.entrySet().iterator();
			while (it.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry pairs = (Map.Entry) it.next();
				if (pairs.getKey().toString().contains("globalFilter")) {
					predicates.add(criteriaBuilder.or(
							criteriaBuilder.like(
									criteriaBuilder
											.upper(from.<String> get("name")),
									"%" + pairs.getValue() + "%"),
							criteriaBuilder.like(
									new StrFunction<Long>(criteriaBuilder,
											from.<Long> get("id")),
									"%" + pairs.getValue() + "%")));
				}
			}
		}

		select.where(
				criteriaBuilder.and(predicates.toArray(new Predicate[]{})));

		if (multiSortMeta != null) {
			for (SortMeta sortMeta : multiSortMeta) {
				String sortField = sortMeta.getSortField();
				SortOrder sortOrder = sortMeta.getSortOrder();

				if (!sortField.contains("company")) {
					if (sortOrder.equals(SortOrder.DESCENDING))
						select.orderBy(
								criteriaBuilder.desc(from.get(sortField)));
					else if (sortOrder.equals(SortOrder.ASCENDING)) {
						select.orderBy(
								criteriaBuilder.asc(from.get(sortField)));
					}
				} else {
					Join<JJCategory, JJCompany> owner = from.join("company");

					if (sortOrder.equals(SortOrder.DESCENDING))
						select.orderBy(criteriaBuilder.desc(owner.get("name")));
					else if (sortOrder.equals(SortOrder.ASCENDING)) {
						select.orderBy(criteriaBuilder.asc(owner.get("name")));
					}
				}

			}
		} else
			select.orderBy(criteriaBuilder.desc(from.get("creationDate")));

		TypedQuery<JJCategory> result = entityManager.createQuery(select);
		result.setFirstResult(first);
		result.setMaxResults(pageSize);

		CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
		cq.select(criteriaBuilder.count(cq.from(JJCategory.class)));
		cq.where(predicates.toArray(new Predicate[]{}));
		size.setValue(entityManager.createQuery(cq).getSingleResult());

		return result.getResultList();

	}

	public boolean nameExist(String name, Long id, JJCompany company) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> select = criteriaBuilder.createQuery(Long.class);

		Root<JJCategory> from = select.from(JJCategory.class);
		select.select(criteriaBuilder.count(from));

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

		if (name != null) {
			predicates.add(criteriaBuilder.equal(
					criteriaBuilder.lower(from.<String> get("name")),
					name.toLowerCase()));
		}

		if (id != null) {
			predicates.add(criteriaBuilder.notEqual(from.get("id"), id));
		}

		if (company != null) {
			Predicate condition1 = criteriaBuilder.equal(from.get("company"),
					company);
			Predicate condition2 = criteriaBuilder.isNull(from.get("company"));
			predicates.add(criteriaBuilder.or(condition1, condition2));
		}
		select.where(
				criteriaBuilder.and(predicates.toArray(new Predicate[]{})));

		return entityManager.createQuery(select).getSingleResult() > 0;

	}

	@Override
	public JJCategory getCategory(String name, JJCompany company,
			boolean onlyActif) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJCategory> criteriaQuery = criteriaBuilder
				.createQuery(JJCategory.class);

		Root<JJCategory> from = criteriaQuery.from(JJCategory.class);

		CriteriaQuery<JJCategory> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(criteriaBuilder.equal(
				criteriaBuilder.upper(from.<String> get("name")),
				name.toUpperCase()));

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		if (company != null) {
			Predicate condition1 = criteriaBuilder.equal(from.get("company"),
					company);
			Predicate condition2 = criteriaBuilder.isNull(from.get("company"));
			predicates.add(criteriaBuilder.or(condition1, condition2));
		}

		select.where(
				criteriaBuilder.and(predicates.toArray(new Predicate[]{})));

		TypedQuery<JJCategory> result = entityManager.createQuery(select);
		if (result.getResultList().size() == 0)
			return null;
		else
			return result.getSingleResult();
	}

	@Override
	public List<JJCategory> getCategories(String name, boolean withName,
			boolean onlyActif, boolean sortedByStage, JJCompany company) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJCategory> criteriaQuery = criteriaBuilder
				.createQuery(JJCategory.class);

		Root<JJCategory> from = criteriaQuery.from(JJCategory.class);

		CriteriaQuery<JJCategory> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (withName) {
			predicates.add(criteriaBuilder.equal(from.get("name"), name));
		}

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		if (company != null) {
			Predicate condition1 = criteriaBuilder.equal(from.get("company"),
					company);
			Predicate condition2 = criteriaBuilder.isNull(from.get("company"));
			predicates.add(criteriaBuilder.or(condition1, condition2));
		}

		select.where(
				criteriaBuilder.and(predicates.toArray(new Predicate[]{})));

		if (sortedByStage) {
			select.orderBy(criteriaBuilder.asc(from.get("stage")));
		}

		TypedQuery<JJCategory> result = entityManager.createQuery(select);
		return result.getResultList();
	}

	public boolean isHighLevel(JJCategory category, JJCompany company) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJCategory> criteriaQuery = criteriaBuilder
				.createQuery(JJCategory.class);

		Root<JJCategory> from = criteriaQuery.from(JJCategory.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

		if (company != null) {
			Predicate condition1 = criteriaBuilder.equal(from.get("company"),
					company);
			Predicate condition2 = criteriaBuilder.isNull(from.get("company"));
			predicates.add(criteriaBuilder.or(condition1, condition2));
		}

		predicates.add(criteriaBuilder.greaterThan(from.<Integer> get("stage"),
				category.getStage()));
		CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
		cq.select(criteriaBuilder.count(cq.from(JJCategory.class)));
		entityManager.createQuery(cq);
		cq.where(predicates.toArray(new Predicate[]{}));
		boolean have = !(entityManager.createQuery(cq).getSingleResult() > 0);
		return have;

	}

	public boolean isLowLevel(JJCategory category, JJCompany company) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJCategory> criteriaQuery = criteriaBuilder
				.createQuery(JJCategory.class);

		Root<JJCategory> from = criteriaQuery.from(JJCategory.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

		if (company != null) {
			Predicate condition1 = criteriaBuilder.equal(from.get("company"),
					company);
			Predicate condition2 = criteriaBuilder.isNull(from.get("company"));
			predicates.add(criteriaBuilder.or(condition1, condition2));
		}

		predicates.add(criteriaBuilder.lessThan(from.<Integer> get("stage"),
				category.getStage()));
		CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
		cq.select(criteriaBuilder.count(cq.from(JJCategory.class)));
		entityManager.createQuery(cq);
		cq.where(predicates.toArray(new Predicate[]{}));
		boolean have = !(entityManager.createQuery(cq).getSingleResult() > 0);
		return have;

	}

	public void saveJJCategory(JJCategory JJCategory_) {
		jJCategoryRepository.save(JJCategory_);
		JJCategory_ = jJCategoryRepository.findOne(JJCategory_.getId());
	}

	public JJCategory updateJJCategory(JJCategory JJCategory_) {
		jJCategoryRepository.save(JJCategory_);
		JJCategory_ = jJCategoryRepository.findOne(JJCategory_.getId());
		return JJCategory_;
	}

}

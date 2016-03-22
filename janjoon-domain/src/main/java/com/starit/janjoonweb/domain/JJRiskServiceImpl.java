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

public class JJRiskServiceImpl implements JJRiskService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public List<JJRisk> load(MutableInt size, int first, int pageSize,
			List<SortMeta> multiSortMeta, Map<String, Object> filters,
			JJProject project, JJProduct product, JJCompany company) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJRisk> criteriaQuery = criteriaBuilder
				.createQuery(JJRisk.class);

		Root<JJRisk> from = criteriaQuery.from(JJRisk.class);

		CriteriaQuery<JJRisk> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (project != null) {
			predicates.add(criteriaBuilder.equal(from.get("project"), project));
		} else if (company != null) {
			predicates.add(criteriaBuilder.equal(
					from.join("project").join("manager").get("company"),
					company));
		}

		if (product != null) {
			predicates.add(criteriaBuilder.equal(from.get("product"), product));
		}

		if (filters != null) {
			Iterator<Entry<String, Object>> it = filters.entrySet().iterator();
			while (it.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry pairs = (Map.Entry) it.next();
				if (pairs.getKey().toString().contains("globalFilter")) {
					predicates
							.add(criteriaBuilder.or(
									criteriaBuilder.like(
											criteriaBuilder.upper(
													from.<String> get("name")),
											"%" + pairs.getValue() + "%"),
									criteriaBuilder.like(
											new StrFunction<Long>(
													criteriaBuilder,
													from.<Long> get("id")),
											"%" + pairs.getValue() + "%")));
				}

				else if (pairs.getKey().toString().contains("product")) {
					Join<JJRisk, JJProduct> owner = from.join("product");
					predicates.add(
							criteriaBuilder.equal(owner.<String> get("name"),
									pairs.getValue().toString()));

				} else if (pairs.getKey().toString().contains("status")) {
					Join<JJRisk, JJStatus> owner = from.join("status");
					predicates.add(
							criteriaBuilder.equal(owner.<String> get("name"),
									pairs.getValue().toString()));
				} else
					predicates
							.add(criteriaBuilder.like(from.<String> get("name"),
									"%" + pairs.getValue() + "%"));

			}
		}

		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

		select.where(predicates.toArray(new Predicate[]{}));

		if (multiSortMeta != null) {
			for (SortMeta sortMeta : multiSortMeta) {
				String sortField = sortMeta.getSortField();
				SortOrder sortOrder = sortMeta.getSortOrder();
				if (!(sortField.contains("product")
						|| sortField.contains("status"))) {
					if (sortOrder.equals(SortOrder.DESCENDING))
						select.orderBy(
								criteriaBuilder.desc(from.get(sortField)));
					else if (sortOrder.equals(SortOrder.ASCENDING)) {
						select.orderBy(
								criteriaBuilder.asc(from.get(sortField)));
					}
				} else {
					if (sortField.contains("product")) {
						Join<JJRisk, JJProduct> owner = from.join("product");

						if (sortOrder.equals(SortOrder.DESCENDING))
							select.orderBy(
									criteriaBuilder.desc(owner.get("name")));
						else if (sortOrder.equals(SortOrder.ASCENDING)) {
							select.orderBy(
									criteriaBuilder.asc(owner.get("name")));
						}
					} else if (sortField.contains("status")) {
						Join<JJRisk, JJStatus> owner = from.join("status");

						if (sortOrder.equals(SortOrder.DESCENDING))
							select.orderBy(
									criteriaBuilder.desc(owner.get("name")));
						else if (sortOrder.equals(SortOrder.ASCENDING)) {
							select.orderBy(
									criteriaBuilder.asc(owner.get("name")));
						}
					}
				}

			}

		} else
			select.orderBy(criteriaBuilder.desc(from.get("creationDate")));
		TypedQuery<JJRisk> result = entityManager.createQuery(select);
		size.setValue(entityManager.createQuery(select).getResultList().size());
		result.setFirstResult(first);
		result.setMaxResults(pageSize);

		return result.getResultList();

	}

}

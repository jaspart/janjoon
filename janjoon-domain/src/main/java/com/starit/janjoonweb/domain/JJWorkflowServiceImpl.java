package com.starit.janjoonweb.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.persistence.*;
import javax.persistence.criteria.*;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import com.starit.janjoonweb.domain.reference.StrFunction;

public class JJWorkflowServiceImpl implements JJWorkflowService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JJStatusService jJStatusService;

	public void setjJStatusService(JJStatusService jJStatusService) {
		this.jJStatusService = jJStatusService;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void saveJJWorflow(JJWorkflow JJWorflow_) {

		jJWorkflowRepository.save(JJWorflow_);
		JJWorflow_ = jJWorkflowRepository.findOne(JJWorflow_.getId());
	}

	public JJWorkflow updateJJWorflow(JJWorkflow JJWorflow_) {
		jJWorkflowRepository.save(JJWorflow_);
		JJWorflow_ = jJWorkflowRepository.findOne(JJWorflow_.getId());
		return JJWorflow_;
	}

	public Set<String> getAllObject() {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<String> criteriaQuery = criteriaBuilder
				.createQuery(String.class);

		Root<JJWorkflow> from = criteriaQuery.from(JJWorkflow.class);

		CriteriaQuery<String> select = criteriaQuery
				.select(from.<String> get("objet"));

		TypedQuery<String> result = entityManager.createQuery(select);
		return new HashSet<String>(result.getResultList());

	}

	public List<JJWorkflow> getObjectWorkFlows(String object, JJStatus source,
			JJStatus target, String actionName, boolean enabled) {

		if (source != null && target != null && target.equals(source)) {
			return new ArrayList<JJWorkflow>();
		} else {
			CriteriaBuilder criteriaBuilder = entityManager
					.getCriteriaBuilder();
			CriteriaQuery<JJWorkflow> criteriaQuery = criteriaBuilder
					.createQuery(JJWorkflow.class);

			Root<JJWorkflow> from = criteriaQuery.from(JJWorkflow.class);

			CriteriaQuery<JJWorkflow> select = criteriaQuery.select(from);

			List<Predicate> predicates = new ArrayList<Predicate>();

			if (enabled)
				predicates
						.add(criteriaBuilder.equal(from.get("enabled"), true));

			if (actionName != null)
				predicates.add(criteriaBuilder.equal(
						criteriaBuilder
								.lower(from.<String> get("actionWorkflow")),
						actionName.toLowerCase()));

			if (object != null)
				predicates.add(criteriaBuilder.equal(
						criteriaBuilder.lower(from.<String> get("objet")),
						object.toLowerCase()));

			if (source != null)
				predicates.add(criteriaBuilder.or(
						criteriaBuilder.equal(from.get("source"), source),
						criteriaBuilder.equal(from.get("source"),
								jJStatusService.getOneStatus("Any", "*",
										true))));

			if (target != null)
				predicates.add(criteriaBuilder.or(
						criteriaBuilder.equal(from.get("target"), target),
						criteriaBuilder.equal(from.get("target"),
								jJStatusService.getOneStatus("Any", "*",
										true))));

			select.where(predicates.toArray(new Predicate[]{}));

			TypedQuery<JJWorkflow> result = entityManager.createQuery(select);

			return result.getResultList();
		}

	}

	public List<JJWorkflow> load(MutableInt size, int first, int pageSize,
			String object, String action, boolean onlyactif,
			List<SortMeta> multiSortMeta, Map<String, Object> filters) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJWorkflow> criteriaQuery = criteriaBuilder
				.createQuery(JJWorkflow.class);

		Root<JJWorkflow> from = criteriaQuery.from(JJWorkflow.class);

		CriteriaQuery<JJWorkflow> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (object != null) {

			predicates.add(criteriaBuilder.equal(
					criteriaBuilder.lower(from.<String> get("objet")),
					object.toLowerCase()));

		}

		if (action != null) {

			predicates.add(criteriaBuilder.equal(
					criteriaBuilder.lower(from.<String> get("actionWorkflow")),
					action.toLowerCase()));

		}

		if (onlyactif) {

			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

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
				} else if (pairs.getKey().toString().contains("target")) {

					predicates.add(criteriaBuilder.equal(
							from.get("target").<String> get("name"),
							pairs.getValue().toString()));

				} else if (pairs.getKey().toString().contains("source")) {

					predicates.add(criteriaBuilder.equal(
							from.get("source").<String> get("name"),
							pairs.getValue().toString()));

				} else if (pairs.getKey().toString().contains("objet")) {
					predicates.add(
							criteriaBuilder.like(from.<String> get("objet"),
									"%" + pairs.getValue() + "%"));
				}

			}
		}

		select.where(predicates.toArray(new Predicate[]{}));

		if (multiSortMeta != null) {
			for (SortMeta sortMeta : multiSortMeta) {
				String sortField = sortMeta.getSortField();
				SortOrder sortOrder = sortMeta.getSortOrder();
				if (!sortField.contains("source")
						&& !sortField.contains("target")) {
					if (sortOrder.equals(SortOrder.DESCENDING))
						select.orderBy(
								criteriaBuilder.desc(from.get(sortField)));
					else if (sortOrder.equals(SortOrder.ASCENDING)) {
						select.orderBy(
								criteriaBuilder.asc(from.get(sortField)));
					}
				} else if (sortField.contains("target")) {
					Join<JJWorkflow, JJStatus> owner = from.join("target");

					if (sortOrder.equals(SortOrder.DESCENDING))
						select.orderBy(criteriaBuilder.desc(owner.get("name")));
					else if (sortOrder.equals(SortOrder.ASCENDING)) {
						select.orderBy(criteriaBuilder.asc(owner.get("name")));
					}
				} else if (sortField.contains("source")) {
					Join<JJWorkflow, JJStatus> owner = from.join("source");

					if (sortOrder.equals(SortOrder.DESCENDING))
						select.orderBy(criteriaBuilder.desc(owner.get("name")));
					else if (sortOrder.equals(SortOrder.ASCENDING)) {
						select.orderBy(criteriaBuilder.asc(owner.get("name")));
					}
				}
			}
		} else
			select.orderBy(criteriaBuilder.desc(from.get("creationDate")));

		TypedQuery<JJWorkflow> result = entityManager.createQuery(select);
		result.setFirstResult(first);
		result.setMaxResults(pageSize);

		CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
		cq.select(criteriaBuilder.count(cq.from(JJWorkflow.class)));
		entityManager.createQuery(cq);
		cq.where(predicates.toArray(new Predicate[]{}));
		size.setValue(entityManager.createQuery(cq).getSingleResult());

		return result.getResultList();
	}
}

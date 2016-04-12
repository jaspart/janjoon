package com.starit.janjoonweb.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import com.starit.janjoonweb.domain.reference.StrFunction;

public class JJFlowStepServiceImpl implements JJFlowStepService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public JJFlowStep getFlowStepByLevel(Integer level, String objet,
			boolean onlyActif) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJFlowStep> criteriaQuery = criteriaBuilder
				.createQuery(JJFlowStep.class);

		Root<JJFlowStep> from = criteriaQuery.from(JJFlowStep.class);

		CriteriaQuery<JJFlowStep> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();
		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		if (objet != null) {
			predicates.add(criteriaBuilder.equal(from.get("objet"), objet));
		}

		if (level != null) {
			predicates.add(criteriaBuilder.equal(from.get("level"), level));
		}

		select.where(
				criteriaBuilder.and(predicates.toArray(new Predicate[]{})));

		TypedQuery<JJFlowStep> result = entityManager.createQuery(select);
		if (result.getResultList().isEmpty())
			return null;
		else
			return result.getResultList().get(0);

	}

	public Set<String> getAllObject() {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<String> criteriaQuery = criteriaBuilder
				.createQuery(String.class);

		Root<JJFlowStep> from = criteriaQuery.from(JJFlowStep.class);

		CriteriaQuery<String> select = criteriaQuery
				.select(from.<String> get("objet"));	
		
		select.where(criteriaBuilder.equal(from.get("enabled"), true));

		TypedQuery<String> result = entityManager.createQuery(select);
		return new HashSet<String>(result.getResultList());

	}

	public List<JJFlowStep> load(MutableInt size, int first, int pageSize,
			List<SortMeta> multiSortMeta, Map<String, Object> filters) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJFlowStep> criteriaQuery = criteriaBuilder
				.createQuery(JJFlowStep.class);

		Root<JJFlowStep> from = criteriaQuery.from(JJFlowStep.class);

		CriteriaQuery<JJFlowStep> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

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
				} else if (pairs.getKey().toString().contains("objet")) {

					predicates.add(
							criteriaBuilder.equal(from.<String> get("objet"),
									pairs.getValue().toString()));

				} else
					predicates
							.add(criteriaBuilder.like(from.<String> get("name"),
									"%" + pairs.getValue() + "%"));

			}
		}
		select.where(
				criteriaBuilder.and(predicates.toArray(new Predicate[]{})));

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
		} else
			select.orderBy(criteriaBuilder.desc(from.get("creationDate")));
		TypedQuery<JJFlowStep> result = entityManager.createQuery(select);
		result.setFirstResult(first);
		result.setMaxResults(pageSize);

		CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
		cq.select(criteriaBuilder.count(cq.from(JJFlowStep.class)));
		entityManager.createQuery(cq);
		cq.where(predicates.toArray(new Predicate[]{}));
		size.setValue(
				Math.round(entityManager.createQuery(cq).getSingleResult()));

		return result.getResultList();
	}

	// New Generic

	@Override
	public JJFlowStep getOneFlowStep(String name, String object,
			boolean onlyActif) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJFlowStep> criteriaQuery = criteriaBuilder
				.createQuery(JJFlowStep.class);

		Root<JJFlowStep> from = criteriaQuery.from(JJFlowStep.class);

		CriteriaQuery<JJFlowStep> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (name != null) {
			predicates.add(criteriaBuilder.equal(
					criteriaBuilder.lower(from.<String> get("name")),
					name.toLowerCase()));
		}

		if (object != null) {
			List<Predicate> orPredicates = new ArrayList<Predicate>();

			orPredicates.add(criteriaBuilder.equal(
					criteriaBuilder.lower(from.<String> get("objet")),
					object.toLowerCase()));

			if (!object.contains("*"))
				orPredicates.add(criteriaBuilder.equal(
						criteriaBuilder.lower(from.<String> get("objet")),
						"JJ" + object.toLowerCase()));

			Predicate orPredicate = criteriaBuilder
					.or(orPredicates.toArray(new Predicate[]{}));
			predicates.add(orPredicate);
		}

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		select.where(
				criteriaBuilder.and(predicates.toArray(new Predicate[]{})));

		TypedQuery<JJFlowStep> result = entityManager.createQuery(select);

		if (result.getResultList().size() == 0)
			return null;
		else
			return result.getSingleResult();

	}

	@Override
	public List<JJFlowStep> getFlowStep(String object, boolean onlyActif,
			List<String> names, boolean sortedByLevel) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJFlowStep> criteriaQuery = criteriaBuilder
				.createQuery(JJFlowStep.class);

		Root<JJFlowStep> from = criteriaQuery.from(JJFlowStep.class);

		CriteriaQuery<JJFlowStep> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (object != null) {
			List<Predicate> orPredicates = new ArrayList<Predicate>();
			orPredicates.add(criteriaBuilder.equal(
					criteriaBuilder.lower(from.<String> get("objet")),
					object.toLowerCase()));
			if (!object.contains("*"))
				orPredicates.add(criteriaBuilder.equal(
						criteriaBuilder.lower(from.<String> get("objet")),
						"JJ" + object.toLowerCase()));

			Predicate orPredicate = criteriaBuilder
					.or(orPredicates.toArray(new Predicate[]{}));
			predicates.add(orPredicate);
		}

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		if (names != null) {
			if (names.isEmpty()) {

				select.where(criteriaBuilder
						.and(predicates.toArray(new Predicate[]{})));
			} else {

				List<Predicate> namePredicates = new ArrayList<Predicate>();
				for (String name : names) {

					namePredicates.add(criteriaBuilder.or(
							criteriaBuilder.notEqual(from.get("name"), name)));

				}

				Predicate namePredicate = criteriaBuilder
						.and(namePredicates.toArray(new Predicate[]{}));
				predicates.add(namePredicate);

			}
		}
		select.where(
				criteriaBuilder.and(predicates.toArray(new Predicate[]{})));

		if (sortedByLevel) {
			select.orderBy(criteriaBuilder.asc(from.get("level")));
		}
		TypedQuery<JJFlowStep> result = entityManager.createQuery(select);
		return result.getResultList();
	}

	@Override
	public List<String> getTablesName() {

		Metamodel model = entityManager.getMetamodel();
		model.getEntities();

		List<String> tableNames = new ArrayList<String>();
		Set<EntityType<?>> allEntityTypes = model.getEntities();
		for (EntityType<?> entityType : allEntityTypes) {
			tableNames.add(entityType.getName().substring(2));
		}

		tableNames.add("TaskType");
		tableNames.add("RequirementState");

		return tableNames;
	}

	public void saveJJFlowStep(JJFlowStep JJFlowStep_) {

		jJFlowStepRepository.save(JJFlowStep_);
		JJFlowStep_ = jJFlowStepRepository.findOne(JJFlowStep_.getId());
	}

	public JJFlowStep updateJJFlowStep(JJFlowStep JJFlowStep_) {
		jJFlowStepRepository.save(JJFlowStep_);
		JJFlowStep_ = jJFlowStepRepository.findOne(JJFlowStep_.getId());
		return JJFlowStep_;
	}

}

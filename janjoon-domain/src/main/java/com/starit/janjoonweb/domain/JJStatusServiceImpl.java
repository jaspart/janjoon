package com.starit.janjoonweb.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

public class JJStatusServiceImpl implements JJStatusService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public List<JJStatus> load(MutableInt size,int first, int pageSize) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJStatus> criteriaQuery = criteriaBuilder
				.createQuery(JJStatus.class);

		Root<JJStatus> from = criteriaQuery.from(JJStatus.class);

		CriteriaQuery<JJStatus> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		TypedQuery<JJStatus> result = entityManager.createQuery(select);
		result.setFirstResult(first);
		result.setMaxResults(pageSize);
		
		CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
		cq.select(criteriaBuilder.count(cq.from(JJStatus.class)));
		entityManager.createQuery(cq);
		cq.where(predicates.toArray(new Predicate[] {}));
		size.setValue(Math.round(entityManager.createQuery(cq).getSingleResult()));	
		
		return result.getResultList();
	}

	// New Generic

	@Override
	public JJStatus getOneStatus(String name, String object, boolean onlyActif) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJStatus> criteriaQuery = criteriaBuilder
				.createQuery(JJStatus.class);

		Root<JJStatus> from = criteriaQuery.from(JJStatus.class);

		CriteriaQuery<JJStatus> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (name != null) {
			predicates.add(criteriaBuilder.equal(from.get("name"), name));
		}

		if (object != null) {
			List<Predicate> orPredicates = new ArrayList<Predicate>();
			
			orPredicates.add(criteriaBuilder.equal(from.get("objet"), object));			
			if(!object.contains("*"))
				orPredicates.add(criteriaBuilder.equal(from.get("objet"),
						"JJ"+object));
			
			Predicate orPredicate = criteriaBuilder.or(orPredicates
					.toArray(new Predicate[] {}));
			predicates.add(orPredicate);
		}

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		TypedQuery<JJStatus> result = entityManager.createQuery(select);		

		if (result.getResultList().size() == 0)
			return null;
		else
			return result.getSingleResult();

	}

	@Override
	public List<JJStatus> getStatus(String object, boolean onlyActif,
			List<String> names, boolean sortedByName) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJStatus> criteriaQuery = criteriaBuilder
				.createQuery(JJStatus.class);

		Root<JJStatus> from = criteriaQuery.from(JJStatus.class);

		CriteriaQuery<JJStatus> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (object != null) {
			List<Predicate> orPredicates = new ArrayList<Predicate>();
			
			orPredicates.add(criteriaBuilder.equal(from.get("objet"), object));			
			if(!object.contains("*"))
				orPredicates.add(criteriaBuilder.equal(from.get("objet"),
						"JJ"+object));
			
			Predicate orPredicate = criteriaBuilder.or(orPredicates
					.toArray(new Predicate[] {}));
			predicates.add(orPredicate);
		}

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		if (names != null) {
			if (names.isEmpty()) {

				select.where(criteriaBuilder.and(predicates
						.toArray(new Predicate[] {})));
			} else {

				List<Predicate> namePredicates = new ArrayList<Predicate>();
				for (String name : names) {

					namePredicates.add(criteriaBuilder.or(criteriaBuilder
							.notEqual(from.get("name"), name)));

				}

				Predicate namePredicate = criteriaBuilder.and(namePredicates
						.toArray(new Predicate[] {}));
				predicates.add(namePredicate);

			}
		}
		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		if (sortedByName) {
			select.orderBy(criteriaBuilder.asc(from.get("name")));
		}
		TypedQuery<JJStatus> result = entityManager.createQuery(select);
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

		return tableNames;
	}

	public void saveJJStatus(JJStatus JJStatus_) {

		jJStatusRepository.save(JJStatus_);
		JJStatus_ = jJStatusRepository.findOne(JJStatus_.getId());
	}

	public JJStatus updateJJStatus(JJStatus JJStatus_) {
		jJStatusRepository.save(JJStatus_);
		JJStatus_ = jJStatusRepository.findOne(JJStatus_.getId());
		return JJStatus_;
	}

}

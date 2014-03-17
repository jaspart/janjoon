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

public class JJRightServiceImpl implements JJRightService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public List<JJRight> getRights(JJProfile profile) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJRight> criteriaQuery = criteriaBuilder
				.createQuery(JJRight.class);
		Root<JJRight> from = criteriaQuery.from(JJRight.class);
		CriteriaQuery<JJRight> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (profile != null) {
			predicates.add(criteriaBuilder.equal(from.get("profile"), profile));
		} else {
			predicates.add(criteriaBuilder.isNull(from.get("profile")));
		}

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		TypedQuery<JJRight> result = entityManager.createQuery(select);
		return result.getResultList();
	}

	@Override
	public List<String> getTablesName() {

		Metamodel model = entityManager.getMetamodel();
		model.getEntities();

		List<String> tableNames = new ArrayList<String>();

		Set<EntityType<?>> allEntityTypes = model.getEntities();
		for (EntityType<?> entityType : allEntityTypes) {
			tableNames.add(entityType.getName());
		}

		return tableNames;
	}

}

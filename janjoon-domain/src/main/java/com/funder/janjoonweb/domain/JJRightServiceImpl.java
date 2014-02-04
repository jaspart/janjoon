package com.funder.janjoonweb.domain;

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

import org.hibernate.SessionFactory;

public class JJRightServiceImpl implements JJRightService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public List<JJRight> getObjectWriterList(String objet) {
		return getObjectWriterList(objet, true);
	}

	public List<JJRight> getObjectWriterList(String objet, boolean w) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJRight> criteriaQuery = criteriaBuilder
				.createQuery(JJRight.class);
		Root<JJRight> from = criteriaQuery.from(JJRight.class);
		CriteriaQuery<JJRight> select = criteriaQuery.select(from);

		Predicate predicate1 = criteriaBuilder.equal(from.get("w"), w);
		Predicate predicate2 = criteriaBuilder.equal(from.get("objet"), objet);

		select.where(criteriaBuilder.and(predicate1, predicate2));

		TypedQuery<JJRight> result = entityManager.createQuery(select);
		return result.getResultList();
	}

	public List<JJRight> getRightsWithoutProfile() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJRight> criteriaQuery = criteriaBuilder
				.createQuery(JJRight.class);
		Root<JJRight> from = criteriaQuery.from(JJRight.class);
		CriteriaQuery<JJRight> select = criteriaQuery.select(from);

		Predicate predicate = criteriaBuilder.isNull(from.get("profile"));

		select.where(predicate);

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

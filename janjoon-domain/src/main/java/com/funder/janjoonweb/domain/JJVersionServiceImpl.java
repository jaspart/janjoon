package com.funder.janjoonweb.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class JJVersionServiceImpl implements JJVersionService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<JJVersion> getAllJJVersionsWithProduct(JJProduct product) {

		// Query query = entityManager.createQuery("select s from JJVersion s "
		// + "join fetch s.product where s.product.name=:value");
		// query.setParameter("value", name);
		//
		// @SuppressWarnings("unchecked")
		// List<JJVersion> list = query.getResultList();
		// return list;

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJVersion> criteriaQuery = criteriaBuilder
				.createQuery(JJVersion.class);

		Root<JJVersion> from = criteriaQuery.from(JJVersion.class);

		Path<Object> path = from.join("product");

		from.fetch("product");

		CriteriaQuery<JJVersion> select = criteriaQuery.select(from);
		Predicate predicate1 = criteriaBuilder.equal(path, product);
		Predicate predicate2 = criteriaBuilder.equal(from.get("enabled"), true);

		select.where(criteriaBuilder.and(predicate1, predicate2));

		TypedQuery<JJVersion> result = entityManager.createQuery(select);
		return result.getResultList();

	}

	@Override
	public List<JJVersion> getAllJJVersion() {

		// Query query = entityManager
		// .createQuery("select s from JJVersion s where s.enabled=:value");
		// query.setParameter("value", true);
		//
		// @SuppressWarnings("unchecked")
		// List<JJVersion> list = query.getResultList();
		// return list;

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJVersion> criteriaQuery = criteriaBuilder
				.createQuery(JJVersion.class);

		Root<JJVersion> from = criteriaQuery.from(JJVersion.class);

		CriteriaQuery<JJVersion> select = criteriaQuery.select(from);

		Predicate predicate = criteriaBuilder.equal(from.get("enabled"), true);

		select.where(predicate);

		TypedQuery<JJVersion> result = entityManager.createQuery(select);
		return result.getResultList();

	}

	@Override
	public List<JJVersion> getAllJJVersionWithoutProduct() {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJVersion> criteriaQuery = criteriaBuilder
				.createQuery(JJVersion.class);

		Root<JJVersion> from = criteriaQuery.from(JJVersion.class);

		CriteriaQuery<JJVersion> select = criteriaQuery.select(from);

		Predicate predicate1 = criteriaBuilder.equal(from.get("enabled"), true);
		Predicate predicate2 = criteriaBuilder.isNull(from.get("product"));

		select.where(criteriaBuilder.and(predicate1, predicate2));

		TypedQuery<JJVersion> result = entityManager.createQuery(select);
		return result.getResultList();

	}

	// New Generic

	@Override
	public List<JJVersion> getVersions(boolean onlyActif, JJProduct product) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJVersion> criteriaQuery = criteriaBuilder
				.createQuery(JJVersion.class);

		Root<JJVersion> from = criteriaQuery.from(JJVersion.class);

		CriteriaQuery<JJVersion> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		if (product != null) {
			predicates.add(criteriaBuilder.equal(from.get("product"), product));
		}
		select.where(predicates.toArray(new Predicate[] {}));

		TypedQuery<JJVersion> result = entityManager.createQuery(select);
		return result.getResultList();

	}
}

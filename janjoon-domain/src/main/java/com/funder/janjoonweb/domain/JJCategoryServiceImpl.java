package com.funder.janjoonweb.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class JJCategoryServiceImpl implements JJCategoryService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public JJCategory getJJCategoryWithName(String name) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJCategory> criteriaQuery = criteriaBuilder
				.createQuery(JJCategory.class);

		Root<JJCategory> from = criteriaQuery.from(JJCategory.class);

		CriteriaQuery<JJCategory> select = criteriaQuery.select(from);
		Predicate predicate1 = criteriaBuilder.equal(from.get("name"), name);
		Predicate predicate2 = criteriaBuilder.equal(from.get("enabled"), true);

		select.where(criteriaBuilder.and(predicate1, predicate2));

		TypedQuery<JJCategory> result = entityManager.createQuery(select);
		if (result.getResultList().size() == 0)
			return null;
		else
			return result.getSingleResult();
	}

	@Override
	public List<JJCategory> getAllJJCategories() {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJCategory> criteriaQuery = criteriaBuilder
				.createQuery(JJCategory.class);

		Root<JJCategory> from = criteriaQuery.from(JJCategory.class);

		CriteriaQuery<JJCategory> select = criteriaQuery.select(from);

		Predicate predicate = criteriaBuilder.equal(from.get("enabled"), true);

		select.where(predicate);

		TypedQuery<JJCategory> result = entityManager.createQuery(select);
		return result.getResultList();

	}

	@Override
	public List<JJCategory> getCategories(boolean onlyActif,
			boolean sortedByStage) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJCategory> criteriaQuery = criteriaBuilder
				.createQuery(JJCategory.class);

		Root<JJCategory> from = criteriaQuery.from(JJCategory.class);

		CriteriaQuery<JJCategory> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));
		
		if (sortedByStage) {
			select.orderBy(criteriaBuilder.asc(from.get("stage")));
		}

		TypedQuery<JJCategory> result = entityManager.createQuery(select);
		return result.getResultList();
	}

}

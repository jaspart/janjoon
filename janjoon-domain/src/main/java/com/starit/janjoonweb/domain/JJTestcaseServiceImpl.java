package com.starit.janjoonweb.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class JJTestcaseServiceImpl implements JJTestcaseService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<JJTestcase> getAllJJTestcases() {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJTestcase> criteriaQuery = criteriaBuilder
				.createQuery(JJTestcase.class);

		Root<JJTestcase> from = criteriaQuery.from(JJTestcase.class);

		CriteriaQuery<JJTestcase> select = criteriaQuery.select(from);

		Predicate predicate1 = criteriaBuilder.equal(from.get("enabled"), true);

		select.where(predicate1);

		TypedQuery<JJTestcase> result = entityManager.createQuery(select);
		return result.getResultList();

	}

	@Override
	public List<JJTestcase> getAllJJTestcasesWithChapter(JJChapter chapter) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJTestcase> criteriaQuery = criteriaBuilder
				.createQuery(JJTestcase.class);

		Root<JJTestcase> from = criteriaQuery.from(JJTestcase.class);

		CriteriaQuery<JJTestcase> select = criteriaQuery.select(from);

		Predicate predicate1 = criteriaBuilder.equal(from.get("enabled"), true);
		Predicate predicate2 = criteriaBuilder.equal(from.get("chapter"),
				chapter);

		select.where(criteriaBuilder.and(predicate1, predicate2));

		TypedQuery<JJTestcase> result = entityManager.createQuery(select);
		return result.getResultList();

	}

}

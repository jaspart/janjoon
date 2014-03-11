package com.starit.janjoonweb.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


public class JJTeststepServiceImpl implements JJTeststepService {
	
	
	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Override
	public List<JJTeststep> getAllJJTeststeps() {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJTeststep> criteriaQuery = criteriaBuilder
				.createQuery(JJTeststep.class);

		Root<JJTeststep> from = criteriaQuery.from(JJTeststep.class);

		CriteriaQuery<JJTeststep> select = criteriaQuery.select(from);

		Predicate predicate1 = criteriaBuilder.equal(from.get("enabled"), true);

		select.where(predicate1);

		TypedQuery<JJTeststep> result = entityManager.createQuery(select);
		return result.getResultList();

	}
	
	@Override
	public List<JJTeststep> getJJTeststepWithTestcase(JJTestcase jJTestcase) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJTeststep> criteriaQuery = criteriaBuilder
				.createQuery(JJTeststep.class);

		Root<JJTeststep> from = criteriaQuery.from(JJTeststep.class);

		CriteriaQuery<JJTeststep> select = criteriaQuery.select(from);

		Predicate predicate1 = criteriaBuilder.equal(from.get("enabled"), true);
		Predicate predicate2 = criteriaBuilder.equal(from.get("testcase"), jJTestcase);

		select.where(predicate1,predicate2);
		criteriaQuery.orderBy(criteriaBuilder.asc(from.get("ordering")));

		TypedQuery<JJTeststep> result = entityManager.createQuery(select);
		return result.getResultList();

	}
}
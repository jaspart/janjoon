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

public class JJChapterServiceImpl implements JJChapterService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<JJChapter> getAllJJChapter() {

		// Query query = entityManager
		// .createQuery("select s from JJChapter s where s.enabled=:value");
		// query.setParameter("value", true);
		//
		// @SuppressWarnings("unchecked")
		// List<JJChapter> list = query.getResultList();
		// return list;

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJChapter> criteriaQuery = criteriaBuilder
				.createQuery(JJChapter.class);

		Root<JJChapter> from = criteriaQuery.from(JJChapter.class);

		CriteriaQuery<JJChapter> select = criteriaQuery.select(from);

		Predicate predicate = criteriaBuilder.equal(from.get("enabled"), true);

		select.where(predicate);

		TypedQuery<JJChapter> result = entityManager.createQuery(select);
		return result.getResultList();

	}

	@Override
	public List<JJProduct> getAllJJProductInJJChapterWithJJProject(
			JJProject project) {

		/*
		 * Query query =
		 * entityManager.createQuery("select s.product from JJChapter s " +
		 * "join fetch s.project where s.project=:value");
		 * query.setParameter("value", "project");
		 * 
		 * @SuppressWarnings("unchecked") List<JJProduct> list =
		 * query.getResultList(); return list;
		 */

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJChapter> criteriaQuery = criteriaBuilder
				.createQuery(JJChapter.class);

		Root<JJChapter> from = criteriaQuery.from(JJChapter.class);

		Path<Object> path = from.join("project");

		from.fetch("project");

		CriteriaQuery<JJChapter> select = criteriaQuery.select(from);
		Predicate predicate1 = criteriaBuilder.equal(path, project);
		Predicate predicate2 = criteriaBuilder.equal(from.get("enabled"), true);

		select.where(criteriaBuilder.and(predicate1, predicate2));

		TypedQuery<JJChapter> result = entityManager.createQuery(select);

		List<JJChapter> jJChapterList = result.getResultList();
		List<JJProduct> jJProduct = new ArrayList<JJProduct>();
		for (JJChapter jjChapter : jJChapterList) {
			if (!jJProduct.contains(jjChapter.getProduct()))
				jJProduct.add(jjChapter.getProduct());
		}

		return jJProduct;

	}

	@Override
	public List<JJChapter> getAllJJChaptersWithProjectAndCategory(
			JJProject project, JJCategory category) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJChapter> criteriaQuery = criteriaBuilder
				.createQuery(JJChapter.class);

		Root<JJChapter> from = criteriaQuery.from(JJChapter.class);

		CriteriaQuery<JJChapter> select = criteriaQuery.select(from);

		Predicate predicate1 = criteriaBuilder.equal(from.get("enabled"), true);
		Predicate predicate2 = criteriaBuilder.equal(from.join("project"),
				project);
		Predicate predicate3 = criteriaBuilder.equal(from.join("category"),
				category);

		select.where(criteriaBuilder.and(predicate1, predicate2, predicate3));

		TypedQuery<JJChapter> result = entityManager.createQuery(select);
		System.out.println("result.getResultList().size() "
				+ result.getResultList().size());
		return result.getResultList();

	}
}

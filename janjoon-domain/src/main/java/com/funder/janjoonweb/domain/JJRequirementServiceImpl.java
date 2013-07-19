package com.funder.janjoonweb.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class JJRequirementServiceImpl implements JJRequirementService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<JJRequirement> getAllJJRequirementsWithCategory(String name) {

		/*
		 * Query query =
		 * entityManager.createQuery("select s from JJRequirement s " +
		 * "join fetch s.category where s.category.name=:value");
		 * query.setParameter("value", "BUSINESS");
		 * 
		 * @SuppressWarnings("unchecked") List<JJRequirement> list =
		 * query.getResultList(); return list;
		 */

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJRequirement> criteriaQuery = criteriaBuilder
				.createQuery(JJRequirement.class);

		Root<JJRequirement> from = criteriaQuery.from(JJRequirement.class);

		Path<Object> path = from.join("category").get("name");

		from.fetch("category");

		CriteriaQuery<JJRequirement> select = criteriaQuery.select(from);
		Predicate predicate1 = criteriaBuilder.equal(path, name);
		Predicate predicate2 = criteriaBuilder.equal(from.get("enabled"), true);

		select.where(criteriaBuilder.and(predicate1, predicate2));

		TypedQuery<JJRequirement> result = entityManager.createQuery(select);
		return result.getResultList();

	}

	@Override
	public List<JJRequirement> getAllJJRequirementsWithCategoryAndProject(
			String name, JJProject project) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJRequirement> criteriaQuery = criteriaBuilder
				.createQuery(JJRequirement.class);

		Root<JJRequirement> from = criteriaQuery.from(JJRequirement.class);

		Path<Object> path = from.join("category").get("name");

		from.fetch("category");
		
		Path<Object> path2 = from.join("project");

		from.fetch("project");

		CriteriaQuery<JJRequirement> select = criteriaQuery.select(from);
		Predicate predicate1 = criteriaBuilder.equal(path, name);
		Predicate predicate = criteriaBuilder.equal(path2, project);
		Predicate predicate2 = criteriaBuilder.equal(from.get("enabled"), true);

		select.where(criteriaBuilder.and(predicate1, predicate2,predicate));

//		Subquery<JJRequirement> subquery = criteriaQuery
//				.subquery(JJRequirement.class);
//		Root<JJRequirement> fromJJRequirement = subquery
//				.from(JJRequirement.class);
//
//		Path<Object> path2 = fromJJRequirement.join("project");
//
//		fromJJRequirement.fetch("project");
//
//		// subquery.select(fromJJRequirement.get("project"));
//		subquery.where(criteriaBuilder.equal(path2, project));
//		select.where(criteriaBuilder.and(predicate1, predicate2).in(subquery));

		TypedQuery<JJRequirement> result = entityManager.createQuery(select);
		System.out.println("Liste of req with project "
				+ result.getResultList());
		return result.getResultList();

	}

}

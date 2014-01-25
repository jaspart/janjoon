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
	public List<JJRequirement> getAllJJRequirements() {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJRequirement> criteriaQuery = criteriaBuilder
				.createQuery(JJRequirement.class);

		Root<JJRequirement> from = criteriaQuery.from(JJRequirement.class);

		CriteriaQuery<JJRequirement> select = criteriaQuery.select(from);

		Predicate predicate = criteriaBuilder.equal(from.get("enabled"), true);

		select.where(predicate);
		select.orderBy(criteriaBuilder.desc(from.get("creationDate")));

		TypedQuery<JJRequirement> result = entityManager.createQuery(select);

		return result.getResultList();

	}

	public List<JJRequirement> getAllJJRequirementsWithProject(JJProject project) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJRequirement> criteriaQuery = criteriaBuilder
				.createQuery(JJRequirement.class);

		Root<JJRequirement> from = criteriaQuery.from(JJRequirement.class);

		CriteriaQuery<JJRequirement> select = criteriaQuery.select(from);

		Predicate predicate1 = criteriaBuilder.equal(from.get("enabled"), true);
		Predicate predicate2 = criteriaBuilder.equal(from.get("project"),
				project);

		select.where(criteriaBuilder.and(predicate1, predicate2));
		select.orderBy(criteriaBuilder.desc(from.get("creationDate")));

		TypedQuery<JJRequirement> result = entityManager.createQuery(select);
		return result.getResultList();
	}

	@Override
	public List<JJRequirement> getAllJJRequirementsWithCategory(
			String categoryName) {

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
		Predicate predicate1 = criteriaBuilder.equal(path, categoryName);
		Predicate predicate2 = criteriaBuilder.equal(from.get("enabled"), true);

		select.where(criteriaBuilder.and(predicate1, predicate2));
		select.orderBy(criteriaBuilder.desc(from.get("creationDate")));

		TypedQuery<JJRequirement> result = entityManager.createQuery(select);

		return result.getResultList();

	}

	@Override
	public List<JJRequirement> getAllJJRequirementsWithCategoryAndChapter(
			String categoryName) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJRequirement> criteriaQuery = criteriaBuilder
				.createQuery(JJRequirement.class);

		Root<JJRequirement> from = criteriaQuery.from(JJRequirement.class);

		Path<Object> path = from.join("category").get("name");

		from.fetch("category");

		CriteriaQuery<JJRequirement> select = criteriaQuery.select(from);
		Predicate predicate1 = criteriaBuilder.equal(path, categoryName);
		Predicate predicate2 = criteriaBuilder.equal(from.get("enabled"), true);
		Predicate predicate3 = criteriaBuilder.isNull(from.get("chapter"));

		select.where(criteriaBuilder.and(predicate1, predicate2, predicate3));
		select.orderBy(criteriaBuilder.desc(from.get("creationDate")));

		TypedQuery<JJRequirement> result = entityManager.createQuery(select);
		return result.getResultList();

	}

	@Override
	public List<JJRequirement> getAllJJRequirementsWithCategoryAndProject(
			String categoryName, JJProject project) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJRequirement> criteriaQuery = criteriaBuilder
				.createQuery(JJRequirement.class);

		Root<JJRequirement> from = criteriaQuery.from(JJRequirement.class);

		Path<Object> path = from.join("category").get("name");

		from.fetch("category");

		CriteriaQuery<JJRequirement> select = criteriaQuery.select(from);
		Predicate predicate1 = criteriaBuilder.equal(from.join("project"),
				project);
		Predicate predicate2 = criteriaBuilder.equal(from.get("enabled"), true);
		Predicate predicate3 = criteriaBuilder.equal(path, categoryName);

		if (!categoryName.isEmpty())
			select.where(criteriaBuilder
					.and(predicate1, predicate2, predicate3));
		else
			select.where(criteriaBuilder.and(predicate1, predicate2));

		select.orderBy(criteriaBuilder.desc(from.get("creationDate")));

		TypedQuery<JJRequirement> result = entityManager.createQuery(select);

		return result.getResultList();

	}

	@Override
	public List<JJRequirement> getAllJJRequirementsWithProjectAndChapter(
			String categoryName, JJProject project) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJRequirement> criteriaQuery = criteriaBuilder
				.createQuery(JJRequirement.class);

		Root<JJRequirement> from = criteriaQuery.from(JJRequirement.class);

		Path<Object> path = from.join("category").get("name");

		from.fetch("category");

		CriteriaQuery<JJRequirement> select = criteriaQuery.select(from);
		Predicate predicate1 = criteriaBuilder.equal(path, categoryName);
		Predicate predicate2 = criteriaBuilder.equal(from.get("enabled"), true);
		Predicate predicate3 = criteriaBuilder.equal(from.join("project"),
				project);

		Predicate predicate4 = criteriaBuilder.isNull(from.get("chapter"));

		select.where(criteriaBuilder.and(predicate1, predicate2, predicate3,
				predicate4));
		select.orderBy(criteriaBuilder.desc(from.get("creationDate")));

		TypedQuery<JJRequirement> result = entityManager.createQuery(select);

		return result.getResultList();

	}

	@Override
	public List<JJRequirement> getAllJJRequirementsWithProjectAndProduct(
			String categoryName, JJProject project, JJProduct product) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJRequirement> criteriaQuery = criteriaBuilder
				.createQuery(JJRequirement.class);

		Root<JJRequirement> from = criteriaQuery.from(JJRequirement.class);

		Path<Object> path = from.join("category").get("name");

		from.fetch("category");

		CriteriaQuery<JJRequirement> select = criteriaQuery.select(from);

		Predicate predicate1 = criteriaBuilder.equal(from.join("project"),
				project);
		Predicate predicate2 = criteriaBuilder.equal(from.get("enabled"), true);
		Predicate predicate3 = criteriaBuilder.equal(from.join("product"),
				product);
		Predicate predicate4 = criteriaBuilder.equal(path, categoryName);
		if (!categoryName.isEmpty())
			select.where(criteriaBuilder.and(predicate1, predicate2,
					predicate3, predicate4));
		else
			select.where(criteriaBuilder
					.and(predicate1, predicate2, predicate3));
		select.orderBy(criteriaBuilder.desc(from.get("creationDate")));

		TypedQuery<JJRequirement> result = entityManager.createQuery(select);

		return result.getResultList();

	}

	@Override
	public List<JJRequirement> getAllJJRequirementsWithProjectAndProductAndChapter(
			String categoryName, JJProject project, JJProduct product) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJRequirement> criteriaQuery = criteriaBuilder
				.createQuery(JJRequirement.class);

		Root<JJRequirement> from = criteriaQuery.from(JJRequirement.class);

		Path<Object> path = from.join("category").get("name");

		from.fetch("category");

		CriteriaQuery<JJRequirement> select = criteriaQuery.select(from);
		Predicate predicate1 = criteriaBuilder.equal(path, categoryName);
		Predicate predicate2 = criteriaBuilder.equal(from.get("enabled"), true);
		Predicate predicate3 = criteriaBuilder.equal(from.join("project"),
				project);
		Predicate predicate4 = criteriaBuilder.equal(from.join("product"),
				product);
		Predicate predicate5 = criteriaBuilder.isNull(from.get("chapter"));

		// select.where(criteriaBuilder.and(predicate1, predicate2, predicate3,
		// predicate4));

		select.where(criteriaBuilder.and(predicate1, predicate2, predicate3,
				predicate4, predicate5));
		select.orderBy(criteriaBuilder.desc(from.get("creationDate")));

		TypedQuery<JJRequirement> result = entityManager.createQuery(select);

		return result.getResultList();

	}

	@Override
	public List<JJRequirement> getAllJJRequirementsWithProjectAndProductAndVersion(
			String categoryName, JJProject project, JJProduct product,
			JJVersion version) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJRequirement> criteriaQuery = criteriaBuilder
				.createQuery(JJRequirement.class);

		Root<JJRequirement> from = criteriaQuery.from(JJRequirement.class);

		Path<Object> path = from.join("category").get("name");

		from.fetch("category");

		CriteriaQuery<JJRequirement> select = criteriaQuery.select(from);

		Predicate predicate1 = criteriaBuilder.equal(from.join("project"),
				project);
		Predicate predicate2 = criteriaBuilder.equal(from.get("enabled"), true);

		Predicate predicate3 = criteriaBuilder.equal(from.join("product"),
				product);
		Predicate predicate4 = criteriaBuilder.equal(from.join("versioning"),
				version);
		Predicate predicate5 = criteriaBuilder.equal(path, categoryName);
		if (!categoryName.isEmpty())
			select.where(criteriaBuilder.and(predicate1, predicate2,
					predicate3, predicate4, predicate5));
		else
			select.where(criteriaBuilder.and(predicate1, predicate2,
					predicate3, predicate4));
		select.orderBy(criteriaBuilder.desc(from.get("creationDate")));

		TypedQuery<JJRequirement> result = entityManager.createQuery(select);

		return result.getResultList();

	}

	@Override
	public List<JJRequirement> getAllJJRequirementsWithProjectAndProductAndVersionAndChapter(
			String categoryName, JJProject project, JJProduct product,
			JJVersion version) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJRequirement> criteriaQuery = criteriaBuilder
				.createQuery(JJRequirement.class);

		Root<JJRequirement> from = criteriaQuery.from(JJRequirement.class);

		Path<Object> path = from.join("category").get("name");

		from.fetch("category");

		CriteriaQuery<JJRequirement> select = criteriaQuery.select(from);
		Predicate predicate1 = criteriaBuilder.equal(path, categoryName);
		Predicate predicate2 = criteriaBuilder.equal(from.get("enabled"), true);
		Predicate predicate3 = criteriaBuilder.equal(from.join("project"),
				project);
		Predicate predicate4 = criteriaBuilder.equal(from.join("product"),
				product);
		Predicate predicate5 = criteriaBuilder.equal(from.join("versioning"),
				version);

		Predicate predicate6 = criteriaBuilder.isNull(from.get("chapter"));

		select.where(criteriaBuilder.and(predicate1, predicate2, predicate3,
				predicate4, predicate5, predicate6));
		select.orderBy(criteriaBuilder.desc(from.get("creationDate")));

		TypedQuery<JJRequirement> result = entityManager.createQuery(select);

		return result.getResultList();

	}
}

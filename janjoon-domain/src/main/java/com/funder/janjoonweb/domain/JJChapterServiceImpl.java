package com.funder.janjoonweb.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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
	public SortedMap<Integer, Object> getAllElement(JJChapter parent) {

		SortedMap<Integer, Object> elements = new TreeMap<Integer, Object>();

		CriteriaBuilder criteriaBuilderC = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJChapter> criteriaQueryC = criteriaBuilderC
				.createQuery(JJChapter.class);
		Root<JJChapter> fromC = criteriaQueryC.from(JJChapter.class);
		CriteriaQuery<JJChapter> selectC = criteriaQueryC.select(fromC);
		Predicate predicate1C = criteriaBuilderC.equal(fromC.get("enabled"),
				true);
		Predicate predicate2C = criteriaBuilderC.equal(fromC.get("parent"),
				parent);
		selectC.where(criteriaBuilderC.and(predicate1C, predicate2C));
		selectC.orderBy(criteriaBuilderC.asc(fromC.get("ordering")));
		TypedQuery<JJChapter> resultC = entityManager.createQuery(selectC);

		for (JJChapter chapter : resultC.getResultList()) {
			elements.put(chapter.getOrdering(), chapter);
		}

		CriteriaBuilder criteriaBuilderR = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJRequirement> criteriaQueryR = criteriaBuilderR
				.createQuery(JJRequirement.class);
		Root<JJRequirement> fromR = criteriaQueryR.from(JJRequirement.class);
		CriteriaQuery<JJRequirement> selectR = criteriaQueryR.select(fromR);
		Predicate predicate1R = criteriaBuilderR.equal(fromR.get("enabled"),
				true);
		Predicate predicate2R = criteriaBuilderR.equal(fromR.get("chapter"),
				parent);
		selectR.where(criteriaBuilderR.and(predicate1R, predicate2R));
		selectR.orderBy(criteriaBuilderR.asc(fromR.get("ordering")));
		TypedQuery<JJRequirement> resultR = entityManager.createQuery(selectR);

		for (JJRequirement requirement : resultR.getResultList()) {
			elements.put(requirement.getOrdering(), requirement);
		}

		System.out.println("S elements.size() " + elements.size());
		System.out.println("R elements.size() "
				+ resultR.getResultList().size());
		System.out.println("C elements.size() "
				+ resultC.getResultList().size());

		for (Object element : elements.entrySet()) {
			System.out.println("S element=" + element.getClass().getName());
		}

		return elements;

	}

	@Override
	public List<JJChapter> getAllJJChaptersWithCategory(JJCategory category) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJChapter> criteriaQuery = criteriaBuilder
				.createQuery(JJChapter.class);

		Root<JJChapter> from = criteriaQuery.from(JJChapter.class);

		CriteriaQuery<JJChapter> select = criteriaQuery.select(from);

		Predicate predicate1 = criteriaBuilder.equal(from.get("enabled"), true);

		Predicate predicate2 = criteriaBuilder.equal(from.join("category"),
				category);

		select.where(criteriaBuilder.and(predicate1, predicate2));

		TypedQuery<JJChapter> result = entityManager.createQuery(select);
		return result.getResultList();

	}

	@Override
	public List<JJChapter> getAllJJChaptersWithProject(JJProject project) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJChapter> criteriaQuery = criteriaBuilder
				.createQuery(JJChapter.class);

		Root<JJChapter> from = criteriaQuery.from(JJChapter.class);

		CriteriaQuery<JJChapter> select = criteriaQuery.select(from);

		Predicate predicate1 = criteriaBuilder.equal(from.get("enabled"), true);

		Predicate predicate2 = criteriaBuilder.equal(from.join("project"),
				project);

		select.where(criteriaBuilder.and(predicate1, predicate2));

		TypedQuery<JJChapter> result = entityManager.createQuery(select);
		return result.getResultList();

	}

	@Override
	public List<JJChapter> getAllParentJJChapterWithCategory(JJCategory category) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJChapter> criteriaQuery = criteriaBuilder
				.createQuery(JJChapter.class);

		Root<JJChapter> from = criteriaQuery.from(JJChapter.class);

		CriteriaQuery<JJChapter> select = criteriaQuery.select(from);

		Predicate predicate1 = criteriaBuilder.equal(from.get("enabled"), true);

		Predicate predicate2 = criteriaBuilder.equal(from.join("category"),
				category);
		Predicate predicate3 = criteriaBuilder.isNull(from.get("parent"));
		select.where(criteriaBuilder.and(predicate1, predicate2, predicate3));

		TypedQuery<JJChapter> result = entityManager.createQuery(select);

		return result.getResultList();

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
		return result.getResultList();

	}

	@Override
	public List<JJChapter> getAllJJChaptersWithProjectAndCategorySortedByOrder(
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
		select.orderBy(criteriaBuilder.asc(from.get("ordering")));

		TypedQuery<JJChapter> result = entityManager.createQuery(select);

		return result.getResultList();

	}

	@Override
	public List<JJChapter> getAllParentJJChapterWithProjectAndCategory(
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
		Predicate predicate4 = criteriaBuilder.isNull(from.get("parent"));

		select.where(criteriaBuilder.and(predicate1, predicate2, predicate3,
				predicate4));

		TypedQuery<JJChapter> result = entityManager.createQuery(select);
		return result.getResultList();

	}

	@Override
	public List<JJChapter> getAllParentJJChapterWithProjectAndCategorySortedByOrder(
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
		Predicate predicate4 = criteriaBuilder.isNull(from.get("parent"));

		select.where(criteriaBuilder.and(predicate1, predicate2, predicate3,
				predicate4));
		select.orderBy(criteriaBuilder.asc(from.get("ordering")));

		TypedQuery<JJChapter> result = entityManager.createQuery(select);
		return result.getResultList();

	}

	@Override
	public List<JJChapter> getAllJJChaptersWithProjectAndProductAndCategory(
			JJProject project, JJProduct product, JJCategory category) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJChapter> criteriaQuery = criteriaBuilder
				.createQuery(JJChapter.class);

		Root<JJChapter> from = criteriaQuery.from(JJChapter.class);

		CriteriaQuery<JJChapter> select = criteriaQuery.select(from);

		Predicate predicate1 = criteriaBuilder.equal(from.get("enabled"), true);
		Predicate predicate2 = criteriaBuilder.equal(from.join("project"),
				project);
		Predicate predicate3 = criteriaBuilder.equal(from.join("product"),
				product);
		Predicate predicate4 = criteriaBuilder.equal(from.join("category"),
				category);

		select.where(criteriaBuilder.and(predicate1, predicate2, predicate3,
				predicate4));

		TypedQuery<JJChapter> result = entityManager.createQuery(select);
		return result.getResultList();

	}

	@Override
	public List<JJChapter> getAllParentJJChapterWithProjectAndProductAndCategory(
			JJProject project, JJProduct product, JJCategory category) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJChapter> criteriaQuery = criteriaBuilder
				.createQuery(JJChapter.class);

		Root<JJChapter> from = criteriaQuery.from(JJChapter.class);

		CriteriaQuery<JJChapter> select = criteriaQuery.select(from);

		Predicate predicate1 = criteriaBuilder.equal(from.get("enabled"), true);
		Predicate predicate2 = criteriaBuilder.equal(from.join("project"),
				project);
		Predicate predicate3 = criteriaBuilder.equal(from.join("product"),
				product);
		Predicate predicate4 = criteriaBuilder.equal(from.join("category"),
				category);
		Predicate predicate5 = criteriaBuilder.isNull(from.get("parent"));

		select.where(criteriaBuilder.and(predicate1, predicate2, predicate3,
				predicate4, predicate5));

		TypedQuery<JJChapter> result = entityManager.createQuery(select);
		return result.getResultList();

	}

	@Override
	public List<JJChapter> getAllParentJJChapterWithProjectAndProductAndCategorySortedByOrder(
			JJProject project, JJProduct product, JJCategory category) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJChapter> criteriaQuery = criteriaBuilder
				.createQuery(JJChapter.class);

		Root<JJChapter> from = criteriaQuery.from(JJChapter.class);

		CriteriaQuery<JJChapter> select = criteriaQuery.select(from);

		Predicate predicate1 = criteriaBuilder.equal(from.get("enabled"), true);
		Predicate predicate2 = criteriaBuilder.equal(from.join("project"),
				project);
		Predicate predicate3 = criteriaBuilder.equal(from.join("product"),
				product);
		Predicate predicate4 = criteriaBuilder.equal(from.join("category"),
				category);
		Predicate predicate5 = criteriaBuilder.isNull(from.get("parent"));

		select.where(criteriaBuilder.and(predicate1, predicate2, predicate3,
				predicate4, predicate5));
		select.orderBy(criteriaBuilder.asc(from.get("ordering")));

		TypedQuery<JJChapter> result = entityManager.createQuery(select);
		return result.getResultList();

	}

	@Override
	public List<JJChapter> getAllJJChaptersWithProjectAndCategoryAndParentSortedByOrder(
			JJProject project, JJCategory category, JJChapter parent) {

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
		Predicate predicate4 = criteriaBuilder
				.equal(from.get("parent"), parent);

		select.where(criteriaBuilder.and(predicate1, predicate2, predicate3,
				predicate4));
		select.orderBy(criteriaBuilder.asc(from.get("ordering")));

		TypedQuery<JJChapter> result = entityManager.createQuery(select);
		return result.getResultList();

	}

	public List<JJChapter> getAllChildsJJChapterWithParentSortedByOrder(
			JJChapter parent) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJChapter> criteriaQuery = criteriaBuilder
				.createQuery(JJChapter.class);

		Root<JJChapter> from = criteriaQuery.from(JJChapter.class);

		CriteriaQuery<JJChapter> select = criteriaQuery.select(from);

		Predicate predicate1 = criteriaBuilder.equal(from.get("enabled"), true);
		Predicate predicate2 = criteriaBuilder
				.equal(from.get("parent"), parent);

		select.where(criteriaBuilder.and(predicate1, predicate2));
		select.orderBy(criteriaBuilder.asc(from.get("ordering")));

		TypedQuery<JJChapter> result = entityManager.createQuery(select);
		return result.getResultList();
	}

	// New generic Request

	@Override
	public List<JJChapter> getAllParentsJJChapterSortedByOrder(
			JJProject project, JJProduct product, JJCategory category) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJChapter> criteriaQuery = criteriaBuilder
				.createQuery(JJChapter.class);

		Root<JJChapter> from = criteriaQuery.from(JJChapter.class);

		CriteriaQuery<JJChapter> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		predicates.add(criteriaBuilder.isNull(from.get("parent")));

		// Adding predicates in case of parameter not being null
		if (project != null) {
			predicates.add(criteriaBuilder.equal(from.get("project"), project));
		}
		if (product != null) {
			predicates.add(criteriaBuilder.equal(from.get("product"), product));
		}
		if (category != null) {
			predicates
					.add(criteriaBuilder.equal(from.get("category"), category));
		}

		select.where(predicates.toArray(new Predicate[] {}));
		select.orderBy(criteriaBuilder.asc(from.get("ordering")));

		TypedQuery<JJChapter> result = entityManager.createQuery(select);
		return result.getResultList();
	}

	@Override
	public List<JJChapter> getAllJJChapters(JJProject project,
			JJProduct product, JJCategory category) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJChapter> criteriaQuery = criteriaBuilder
				.createQuery(JJChapter.class);

		Root<JJChapter> from = criteriaQuery.from(JJChapter.class);

		CriteriaQuery<JJChapter> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		predicates.add(criteriaBuilder.isNull(from.get("parent")));

		// Adding predicates in case of parameter not being null
		if (project != null) {
			predicates.add(criteriaBuilder.equal(from.get("project"), project));
		}
		if (product != null) {
			predicates.add(criteriaBuilder.equal(from.get("product"), product));
		}
		if (category != null) {
			predicates
					.add(criteriaBuilder.equal(from.get("category"), category));
		}

		select.where(predicates.toArray(new Predicate[] {}));

		TypedQuery<JJChapter> result = entityManager.createQuery(select);
		return result.getResultList();

	}

}

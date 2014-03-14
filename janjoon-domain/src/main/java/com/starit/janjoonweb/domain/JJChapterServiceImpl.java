package com.starit.janjoonweb.domain;

import java.util.ArrayList;
import java.util.List;

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

	// New generic Request

	@Override
	public List<JJChapter> getParentsChapter(JJProject project,
			JJProduct product, JJCategory category, boolean onlyActif,
			boolean sortedByOrder) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJChapter> criteriaQuery = criteriaBuilder
				.createQuery(JJChapter.class);

		Root<JJChapter> from = criteriaQuery.from(JJChapter.class);

		CriteriaQuery<JJChapter> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(criteriaBuilder.isNull(from.get("parent")));

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}
		// Adding predicates in case of parameter not being null
		if (project!= null) {
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

		if (sortedByOrder) {
			select.orderBy(criteriaBuilder.asc(from.get("ordering")));
		}

		TypedQuery<JJChapter> result = entityManager.createQuery(select);
		return result.getResultList();
	}

	@Override
	public List<JJChapter> getChapters(JJProject project, JJProduct product,
			JJCategory category, boolean onlyActif, List<String> ids) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJChapter> criteriaQuery = criteriaBuilder
				.createQuery(JJChapter.class);

		Root<JJChapter> from = criteriaQuery.from(JJChapter.class);

		CriteriaQuery<JJChapter> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}
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
		if (ids.isEmpty()) {

			select.where(predicates.toArray(new Predicate[] {}));

		} else {

			List<Predicate> idPredicates = new ArrayList<Predicate>();
			for (String id : ids) {

				idPredicates.add(criteriaBuilder.or(criteriaBuilder.notEqual(
						from.get("id"), Long.valueOf(id))));

			}

			Predicate idPredicate = criteriaBuilder.and(idPredicates
					.toArray(new Predicate[] {}));
			predicates.add(idPredicate);
			select.where(criteriaBuilder.and(predicates
					.toArray(new Predicate[] {})));
		}

		TypedQuery<JJChapter> result = entityManager.createQuery(select);
		return result.getResultList();

	}

	@Override
	public List<JJChapter> getChildrenOfParentChapter(JJChapter parent,
			boolean onlyActif, boolean sortedByOrder) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJChapter> criteriaQuery = criteriaBuilder
				.createQuery(JJChapter.class);

		Root<JJChapter> from = criteriaQuery.from(JJChapter.class);

		CriteriaQuery<JJChapter> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(criteriaBuilder.equal(from.get("parent"), parent));

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		if (sortedByOrder) {
			select.orderBy(criteriaBuilder.asc(from.get("ordering")));
		}

		TypedQuery<JJChapter> result = entityManager.createQuery(select);
		return result.getResultList();
	}

}

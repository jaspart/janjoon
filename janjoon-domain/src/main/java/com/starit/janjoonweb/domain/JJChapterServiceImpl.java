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

	// New generic Request

	@Override
	public List<JJChapter> getParentsChapter(JJCompany company,
			JJProject project, JJCategory category, boolean onlyActif,
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
		if (project != null) {
			predicates.add(criteriaBuilder.equal(from.get("project"), project));
		} else if (company != null) {
			predicates.add(criteriaBuilder.equal(
					from.join("project").join("manager").get("company"),
					company));
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
	public List<JJChapter> getChapters(JJCompany company, JJProject project,
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
		} else if (company != null) {
			predicates.add(criteriaBuilder.equal(
					from.join("project").join("manager").get("company"),
					company));
		}

		if (category != null) {
			predicates
					.add(criteriaBuilder.equal(from.get("category"), category));
		}
		if (ids == null || ids.isEmpty()) {

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

	public JJChapter getChapterByName(JJCategory category, String name,
			JJProject project, JJCompany company) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJChapter> criteriaQuery = criteriaBuilder
				.createQuery(JJChapter.class);

		Root<JJChapter> from = criteriaQuery.from(JJChapter.class);

		CriteriaQuery<JJChapter> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

		if (project != null) {
			predicates.add(criteriaBuilder.equal(from.get("project"), project));
		} else if (company != null) {
			predicates.add(criteriaBuilder.equal(
					from.join("project").join("manager").get("company"),
					company));
		}

		if (category != null) {
			predicates
					.add(criteriaBuilder.equal(from.get("category"), category));
		}

		if (name != null)
			predicates.add(criteriaBuilder.equal(criteriaBuilder.upper(from.<String>get("name")), name.toUpperCase()));

		select.where(predicates.toArray(new Predicate[] {}));

		TypedQuery<JJChapter> result = entityManager.createQuery(select);
		if (result.getResultList().isEmpty())
			return null;
		else
			return result.getResultList().get(0);

	}

	@Override
	public List<JJChapter> getChapters(JJCompany company, JJProject project,
			boolean sotedByDate) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJChapter> criteriaQuery = criteriaBuilder
				.createQuery(JJChapter.class);

		Root<JJChapter> from = criteriaQuery.from(JJChapter.class);

		CriteriaQuery<JJChapter> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

		// Adding predicates in case of parameter not being null
		if (project != null) {
			predicates.add(criteriaBuilder.equal(from.get("project"), project));
		} else if (company != null) {
			predicates.add(criteriaBuilder.equal(
					from.join("project").join("manager").get("company"),
					company));
		}

		select.where(predicates.toArray(new Predicate[] {}));
		if (sotedByDate) {
			select.orderBy(criteriaBuilder.asc(from.get("creationDate")));
		}
		TypedQuery<JJChapter> result = entityManager.createQuery(select);
		return result.getResultList();
	}

	@Override
	public List<JJChapter> getChapters(JJCompany company, JJProject project,
			boolean onlyActif, boolean sortedByName) {

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
		} else if (company != null) {
			predicates.add(criteriaBuilder.equal(
					from.join("project").join("manager").get("company"),
					company));
		}

		select.where(predicates.toArray(new Predicate[] {}));
		if (sortedByName) {
			select.orderBy(criteriaBuilder.asc(from.get("name")));
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

	public void saveJJChapter(JJChapter JJChapter_) {

		jJChapterRepository.save(JJChapter_);
		JJChapter_ = jJChapterRepository.findOne(JJChapter_.getId());
	}

	public JJChapter updateJJChapter(JJChapter JJChapter_) {

		jJChapterRepository.save(JJChapter_);
		JJChapter_ = jJChapterRepository.findOne(JJChapter_.getId());
		return JJChapter_;
	}

}

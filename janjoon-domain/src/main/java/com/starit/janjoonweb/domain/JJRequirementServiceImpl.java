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

public class JJRequirementServiceImpl implements JJRequirementService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
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

	// Generic Request

	@Override
	public List<JJRequirement> getRequirements(JJCategory category,
			JJProject project, JJProduct product, JJVersion version,
			JJStatus status, JJChapter chapter, boolean withChapter,
			boolean onlyActif, boolean orderByCreationdate) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJRequirement> criteriaQuery = criteriaBuilder
				.createQuery(JJRequirement.class);

		Root<JJRequirement> from = criteriaQuery.from(JJRequirement.class);

		CriteriaQuery<JJRequirement> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		if (category  != null) {
			predicates
					.add(criteriaBuilder.equal(from.get("category"), category));
		}

		if (project  != null) {
			predicates.add(criteriaBuilder.equal(from.get("project"), project));
		}

		if (product  != null) {
			predicates.add(criteriaBuilder.equal(from.get("product"), product));
		}

		if (version  != null) {
			predicates.add(criteriaBuilder.equal(from.get("versioning"),
					version));
		}

		if (status  != null) {
			predicates.add(criteriaBuilder.equal(from.get("status"), status));
		}

		if (withChapter) {
			if (chapter != null) {
				predicates.add(criteriaBuilder.equal(from.get("chapter"),
						chapter));
			} else {
				predicates.add(criteriaBuilder.isNull(from.get("chapter")));
			}

		}

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		if (orderByCreationdate) {
			select.orderBy(criteriaBuilder.desc(from.get("creationDate")));
		}

		TypedQuery<JJRequirement> result = entityManager.createQuery(select);

		return result.getResultList();
	}

	@Override
	public List<JJRequirement> getRequirementChildrenWithChapterSortedByOrder(
			JJChapter chapter, boolean onlyActif) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJRequirement> criteriaQuery = criteriaBuilder
				.createQuery(JJRequirement.class);

		Root<JJRequirement> from = criteriaQuery.from(JJRequirement.class);

		CriteriaQuery<JJRequirement> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(criteriaBuilder.equal(from.get("chapter"), chapter));

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));
		select.orderBy(criteriaBuilder.asc(from.get("ordering")));

		TypedQuery<JJRequirement> result = entityManager.createQuery(select);
		return result.getResultList();
	}

	@Override
	public List<JJRequirement> getRequirements(JJProject project,
			JJProduct product, JJVersion version) {
		return getRequirements(null, project, product, version, null, null, false, true, true);
	}
}

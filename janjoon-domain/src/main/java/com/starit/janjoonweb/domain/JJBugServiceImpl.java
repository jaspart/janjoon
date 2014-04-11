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

public class JJBugServiceImpl implements JJBugService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<JJBug> getBugs(JJProject project, JJTeststep teststep,
			JJBuild build, boolean onlyActif, boolean sortedByCreationDate) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJBug> criteriaQuery = criteriaBuilder
				.createQuery(JJBug.class);

		Root<JJBug> from = criteriaQuery.from(JJBug.class);

		CriteriaQuery<JJBug> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		if (project != null) {
			predicates.add(criteriaBuilder.equal(from.get("project"), project));
		}

		if (teststep != null) {
			predicates
					.add(criteriaBuilder.equal(from.get("teststep"), teststep));
		}

		if (build != null) {
			predicates.add(criteriaBuilder.equal(from.get("build"), build));
		}

		select.where(predicates.toArray(new Predicate[] {}));

		if (sortedByCreationDate) {
			select.orderBy(criteriaBuilder.desc(from.get("creationDate")));
		}

		TypedQuery<JJBug> result = entityManager.createQuery(select);
		return result.getResultList();

	}

}
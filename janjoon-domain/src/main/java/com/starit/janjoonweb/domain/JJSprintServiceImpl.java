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

import org.springframework.beans.factory.annotation.Autowired;

public class JJSprintServiceImpl implements JJSprintService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JJRequirementService jJRequirementService;

	public void setjJRequirementService(
			JJRequirementService jJRequirementService) {
		this.jJRequirementService = jJRequirementService;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<JJSprint> getSprints(boolean onlyActif) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJSprint> criteriaQuery = criteriaBuilder
				.createQuery(JJSprint.class);

		Root<JJSprint> from = criteriaQuery.from(JJSprint.class);

		CriteriaQuery<JJSprint> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();
		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		select.where(predicates.toArray(new Predicate[] {}));

		TypedQuery<JJSprint> result = entityManager.createQuery(select);
		return result.getResultList();

	}

	@Override
	public List<JJSprint> getSprintsByProjects(JJProject project,
			boolean onlyActif) {

		if (project == null) {
			return getSprints(onlyActif);
		} else {
			CriteriaBuilder criteriaBuilder = entityManager
					.getCriteriaBuilder();
			CriteriaQuery<JJSprint> criteriaQuery = criteriaBuilder
					.createQuery(JJSprint.class);

			Root<JJSprint> from = criteriaQuery.from(JJSprint.class);

			CriteriaQuery<JJSprint> select = criteriaQuery.select(from);

			List<Predicate> predicates = new ArrayList<Predicate>();

			predicates.add(criteriaBuilder.equal(from.get("project"), project));
			if (onlyActif) {
				predicates
						.add(criteriaBuilder.equal(from.get("enabled"), true));
			}
			select.where(predicates.toArray(new Predicate[] {}));

			TypedQuery<JJSprint> result = entityManager.createQuery(select);
			return result.getResultList();

		}

	}

	// public Integer getWorkload(JJSprint sprint, boolean onlyActif) {
	// CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
	// CriteriaQuery<Integer> criteria = criteriaBuilder
	// .createQuery(Integer.class);
	// Root<JJTask> from = criteria.from(JJTask.class);
	//
	// criteria.select(criteriaBuilder.sum(from.<Integer>get("workloadPlanned")));
	//
	// List<Predicate> predicates = new ArrayList<Predicate>();
	//
	// if (sprint != null) {
	//
	// List<JJRequirement> requirements = jJRequirementService
	// .getRequirements(sprint.getProject(), null, null);
	// for(JJRequirement req:requirements)
	// {
	// predicates.add(criteriaBuilder.equal(from.get("requirement"),
	// req));
	// }
	//
	// }
	//
	// if (onlyActif) {
	// predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
	// }
	//
	// return Integer.parseInt(entityManager.createQuery(criteria)
	// .getSingleResult().toString());
	// }
	//
	// public Integer getConsumed(JJSprint sprint, boolean onlyActif) {
	// CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
	// CriteriaQuery<Integer> criteria = criteriaBuilder
	// .createQuery(Integer.class);
	// Root<JJTask> from = criteria.from(JJTask.class);
	//
	// criteria.select(criteriaBuilder.sum(from.<Integer>get("consumed")));
	//
	// List<Predicate> predicates = new ArrayList<Predicate>();
	//
	// if (sprint != null) {
	//
	// List<JJRequirement> requirements = jJRequirementService
	// .getRequirements(sprint.getProject(), null, null);
	// for(JJRequirement req:requirements)
	// {
	// predicates.add(criteriaBuilder.equal(from.get("requirement"),
	// req));
	// }
	//
	// }
	//
	// if (onlyActif) {
	// predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
	// }
	//
	// return Integer.parseInt(entityManager.createQuery(criteria)
	// .getSingleResult().toString());
	// }
}

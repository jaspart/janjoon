package com.starit.janjoonweb.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class JJTaskServiceImpl implements JJTaskService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<JJTask> getTasks(JJProject project, JJContact contact,
			boolean onlyActif) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJTask> criteriaQuery = criteriaBuilder
				.createQuery(JJTask.class);

		Root<JJTask> from = criteriaQuery.from(JJTask.class);

		CriteriaQuery<JJTask> select = criteriaQuery.select(from);
		List<Predicate> predicates = new ArrayList<Predicate>();

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}
		if (contact != null) {
			predicates.add(criteriaBuilder.equal(from.get("assignedTo"),
					contact));
		}

		if (project != null) {
			Path<Object> path = from.join("requirement").get("project");
			predicates.add(criteriaBuilder.equal(path, project));
		}

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		TypedQuery<JJTask> result = entityManager.createQuery(select);

		return result.getResultList();

	}

	@Override
	public void saveTasks(Set<JJTask> tasks) {
		jJTaskRepository.save(tasks);
	}

	@Override
	public void updateTasks(Set<JJTask> tasks) {
		jJTaskRepository.save(tasks);
	}

}

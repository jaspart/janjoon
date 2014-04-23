package com.starit.janjoonweb.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
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
	public List<JJTask> getTasks(JJProject project, JJProduct product,
			JJContact contact, boolean onlyActif, boolean sortedByChapter) {
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

		if (product != null) {
			Path<Object> path = from.join("requirement").get("product");
			predicates.add(criteriaBuilder.equal(path, product));
		}

		if (sortedByChapter) {
			Path<Object> path = from.join("requirement").get("chapter");
			predicates.add(criteriaBuilder.isNotNull(path));
		}

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		// if (sortedByChapter) {
		// Path<Object> path = from.join("requirement").get("chapter");
		// Expression<String> expression = from.get("name");
		// select.orderBy(criteriaBuilder.desc(expression));
		// }

		TypedQuery<JJTask> result = entityManager.createQuery(select);

		return result.getResultList();

	}

	public List<JJTask> getTasksByStatus(JJStatus status, JJProject project,JJSprint sprint,
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

		if (project != null) {
			Path<Object> path = from.join("requirement").get("project");
			predicates.add(criteriaBuilder.equal(path, project));
		}

		if (status != null) {
			predicates.add(criteriaBuilder.equal(from.get("status"), status));
		}
		
		if (sprint != null) {
			predicates.add(criteriaBuilder.equal(from.get("sprint"), status));
		}

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		TypedQuery<JJTask> result = entityManager.createQuery(select);

		return result.getResultList();

	}
	
	@Override
	public List<JJTask> getSprintTasks(JJSprint sprint)
	{
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJTask> criteriaQuery = criteriaBuilder
				.createQuery(JJTask.class);

		Root<JJTask> from = criteriaQuery.from(JJTask.class);

		CriteriaQuery<JJTask> select = criteriaQuery.select(from);
		List<Predicate> predicates = new ArrayList<Predicate>();	
		
		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));	
		
		if (sprint != null) {
			predicates.add(criteriaBuilder.equal(from.get("sprint"), sprint));
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

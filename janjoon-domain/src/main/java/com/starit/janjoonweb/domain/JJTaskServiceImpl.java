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

import org.springframework.beans.factory.annotation.Autowired;

public class JJTaskServiceImpl implements JJTaskService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JJStatusService jJStatusService;

	@Autowired
	private JJRequirementService jJRequirementService;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void setjJStatusService(JJStatusService jJStatusService) {
		this.jJStatusService = jJStatusService;
	}

	public void setjJRequirementService(
			JJRequirementService jJRequirementService) {
		this.jJRequirementService = jJRequirementService;
	}

	@Override
	public List<JJTask> getTasks(JJSprint sprint, JJProject project,
			JJProduct product, JJContact contact, JJChapter chapter,
			JJRequirement requirement, JJTestcase testcase, JJBuild build,
			boolean onlyActif, boolean sortedByCreationDate, boolean withBuild,
			Boolean lookAtTestcase) {
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

		if (sprint != null) {
			predicates.add(criteriaBuilder.equal(from.get("sprint"), sprint));
		}

		if (requirement != null) {
			predicates.add(criteriaBuilder.equal(from.get("requirement"),
					requirement));
		}

		if (project != null) {
			Path<Object> path = from.join("requirement").get("project");
			predicates.add(criteriaBuilder.equal(path, project));
		}

		if (product != null) {
			Path<Object> path = from.join("requirement").get("product");
			predicates.add(criteriaBuilder.equal(path, product));
		}

		if (chapter != null && lookAtTestcase != null) {
			Path<Object> path;

			if (lookAtTestcase) {
				path = from.join("testcase").get("requirement").get("chapter");
				predicates.add(criteriaBuilder.equal(path, chapter));
			} else {
				path = from.join("requirement").get("chapter");
				predicates.add(criteriaBuilder.equal(path, chapter));
			}
		}

		if (testcase != null) {
			predicates
					.add(criteriaBuilder.equal(from.get("testcase"), testcase));
		}

		if (withBuild) {
			if (build != null) {
				predicates.add(criteriaBuilder.equal(from.get("build"), build));
			} else {
				predicates.add(criteriaBuilder.isNull(from.get("build")));
			}

		}

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		if (sortedByCreationDate) {
			select.orderBy(criteriaBuilder.asc(from.get("creationDate")));
		}

		TypedQuery<JJTask> result = entityManager.createQuery(select);

		return result.getResultList();

	}

	@Override
	public List<JJTask> getTasksByStatus(JJStatus status, JJProject project,
			JJSprint sprint, boolean onlyActif) {
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
	public List<JJTask> getSprintTasks(JJSprint sprint) {
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
	public List<JJTask> getTasksByProduct(JJProduct product, JJProject project) {
		return getTasks(null, project, product, null, null, null, null, null,
				true, false, false, null);

	}

	// public JJTask updateJJTask(JJTask task) {
	//
	// // JJStatus status = task.getStatus();
	// //
	// System.out.println("ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");
	// //
	// // if (status != null) {
	// // System.err.println(status.getName());
	// // if (status.getName().equalsIgnoreCase("IN PROGRESS")) {
	// // System.err.println("IN PROGRESS");
	// // if (task.getRequirement() != null) {
	// // System.err.println(task.getRequirement().getName());
	// // if (!task.getRequirement().getStatus().getName()
	// // .equalsIgnoreCase("RELEASED")) {
	// // JJStatus reqStatus = jJStatusService.getOneStatus(
	// // "RELEASED", "JJRequirement", true);
	// // task.getRequirement().setStatus(reqStatus);
	// // jJRequirementService.updateJJRequirement(task
	// // .getRequirement());
	// //
	// // }
	// // } else {
	// // System.err.println("RELEASED");
	// // JJStatus reqStatus = jJStatusService.getOneStatus(
	// // "RELEASED", "JJRequirement", true);
	// // task.getRequirement().setStatus(reqStatus);
	// // jJRequirementService.updateJJRequirement(task
	// // .getRequirement());
	// // }
	// // }
	// // }
	//
	// return jJTaskRepository.save(task);
	// }

	@Override
	public void saveTasks(Set<JJTask> tasks) {
		jJTaskRepository.save(tasks);
	}

	@Override
	public void updateTasks(Set<JJTask> tasks) {
		jJTaskRepository.save(tasks);
	}

}

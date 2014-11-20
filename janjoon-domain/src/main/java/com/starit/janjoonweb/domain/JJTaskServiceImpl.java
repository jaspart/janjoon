package com.starit.janjoonweb.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
			String objet) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJTask> criteriaQuery = criteriaBuilder
				.createQuery(JJTask.class);

		Root<JJTask> from = criteriaQuery.from(JJTask.class);
		
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

		if (objet != null && project != null && chapter == null) {

			if (objet.equalsIgnoreCase("bug")||objet.equalsIgnoreCase("b")) {
				List<Predicate> andPredicates = new ArrayList<Predicate>();
				andPredicates.add(criteriaBuilder.isNotNull(from.get("bug")));
				andPredicates.add(criteriaBuilder.equal(
						from.join("bug").get("project"), project));
				andPredicates.add(criteriaBuilder.equal(
						from.join("bug").get("enabled"), true));
				predicates.add(criteriaBuilder.and(andPredicates
						.toArray(new Predicate[] {})));
			} else if (objet.equalsIgnoreCase("requirement")) {
				List<Predicate> andPredicates = new ArrayList<Predicate>();
				andPredicates.add(criteriaBuilder.isNotNull(from
						.get("requirement")));
				andPredicates.add(criteriaBuilder.equal(from
						.join("requirement").get("project"), project));
				andPredicates.add(criteriaBuilder.equal(from
						.join("requirement").get("enabled"), true));
				predicates.add(criteriaBuilder.and(andPredicates
						.toArray(new Predicate[] {})));
			} else if (objet.equalsIgnoreCase("testcase")) {
				List<Predicate> andPredicates = new ArrayList<Predicate>();
				andPredicates.add(criteriaBuilder.isNotNull(from
						.get("testcase")));
				andPredicates.add(criteriaBuilder.equal(from.join("testcase")
						.join("requirement").get("project"), project));
				andPredicates.add(criteriaBuilder.equal(from.join("testcase")
						.get("enabled"), true));
				predicates.add(criteriaBuilder.and(andPredicates
						.toArray(new Predicate[] {})));
			}

		}

		if (product != null) {			
			predicates.add(criteriaBuilder.equal(from.join("requirement").get("product"), product));
		}

		if (chapter != null && objet != null) {

			if (objet.equalsIgnoreCase("JJTestcase")
					|| objet.equalsIgnoreCase("Testcase")) {
				predicates.add(criteriaBuilder.equal(from.join("testcase")
						.join("requirement").get("chapter"), chapter));
			} else if (objet.equalsIgnoreCase("JJRequirement")
					|| objet.equalsIgnoreCase("Requirement")) {

				predicates.add(criteriaBuilder.equal(from.join("requirement")
						.get("chapter"), chapter));
			} else if (objet.equalsIgnoreCase("JJBug")
					|| objet.equalsIgnoreCase("Bug")) {

				predicates.add(criteriaBuilder.equal(
						from.join("bug").join("requirement").get("chapter"),
						chapter));
			}
		} else if (objet != null && chapter == null) {
			if (objet.equalsIgnoreCase("b")) {
				predicates.add(criteriaBuilder.isNull(from.join("bug").get("requirement")));

			}else if (objet.equalsIgnoreCase("requirement")) {
				predicates.add(criteriaBuilder.isNull(from.join("requirement").get("chapter")));

			} else if (objet.equalsIgnoreCase("testcase")) {

				predicates.add(criteriaBuilder.isNull(from.join("testcase")
						.join("requirement").get("chapter")));
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
		CriteriaQuery<JJTask> select = criteriaQuery.select(from);

		if (objet != null && chapter == null && objet.equalsIgnoreCase("bug")) {		
			
			predicates.add(criteriaBuilder.isNull(from.join("bug").join("requirement").get("chapter")));
			
			
			select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

			if (sortedByCreationDate) {
				select.orderBy(criteriaBuilder.asc(from.get("creationDate")));
			}

			List<JJTask> resuList = new ArrayList<JJTask>();
			resuList.addAll(entityManager.createQuery(select).getResultList());	
			resuList.addAll(getTasks(sprint, project, product, contact, chapter, requirement, testcase, build, onlyActif, sortedByCreationDate, withBuild, "b"));
			return resuList;

		} else {
			
			select.where(criteriaBuilder.and(predicates
					.toArray(new Predicate[] {})));

			if (sortedByCreationDate) {
				select.orderBy(criteriaBuilder.asc(from.get("creationDate")));
			}

			TypedQuery<JJTask> result = entityManager.createQuery(select);

			return result.getResultList();
		}

	}

	public List<JJTask> getImportTasks(JJBug bug, JJRequirement requirement,
			JJTestcase testcase, boolean onlyActif) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJTask> criteriaQuery = criteriaBuilder
				.createQuery(JJTask.class);

		Root<JJTask> from = criteriaQuery.from(JJTask.class);

		CriteriaQuery<JJTask> select = criteriaQuery.select(from);
		List<Predicate> predicates = new ArrayList<Predicate>();

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}
		if (bug != null) {
			predicates.add(criteriaBuilder.equal(from.get("bug"), bug));
		}

		if (requirement != null) {
			predicates.add(criteriaBuilder.equal(from.get("requirement"),
					requirement));
		}

		if (testcase != null) {
			predicates
					.add(criteriaBuilder.equal(from.get("testcase"), testcase));
		}

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

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

	public List<JJTask> getToDoTasks(JJContact contact) {

		if (contact != null) {
			CriteriaBuilder criteriaBuilder = entityManager
					.getCriteriaBuilder();
			CriteriaQuery<JJTask> criteriaQuery = criteriaBuilder
					.createQuery(JJTask.class);

			Root<JJTask> from = criteriaQuery.from(JJTask.class);

			CriteriaQuery<JJTask> select = criteriaQuery.select(from);
			List<Predicate> predicates = new ArrayList<Predicate>();

			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
			predicates.add(criteriaBuilder.equal(from.get("assignedTo"),
					contact));
			predicates.add(criteriaBuilder.isNull(from.get("endDateReal")));

			select.where(criteriaBuilder.and(predicates
					.toArray(new Predicate[] {})));

			TypedQuery<JJTask> result = entityManager.createQuery(select);

			return result.getResultList();
		}

		else
			return null;

	}
	

	@Override
	public void saveTasks(Set<JJTask> tasks) {
		for(JJTask t:tasks)
		{
			saveJJTask(t);
		}	
	}

	@Override
	public void updateTasks(Set<JJTask> tasks) {
		for(JJTask t:tasks)
		{
			updateJJTask(t);
		}
	}

	public void saveJJTask(JJTask JJTask_) {

		if(JJTask_.getName().length()>100 )
		{
			JJTask_.setName(JJTask_.getName().substring(0, 99));
		}
		
		jJTaskRepository.save(JJTask_);
		JJTask_ = jJTaskRepository.findOne(JJTask_.getId());
	}

	public JJTask updateJJTask(JJTask JJTask_) {
		if(JJTask_.getName().length()>100 )
		{
			JJTask_.setName(JJTask_.getName().substring(0, 99));
		}
		jJTaskRepository.save(JJTask_);
		JJTask_ = jJTaskRepository.findOne(JJTask_.getId());
		return JJTask_;
	}

}

package com.starit.janjoonweb.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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

	@Autowired
	private JJCategoryService jJCategoryService;

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

	public void setjJCategoryService(JJCategoryService jJCategoryService) {
		this.jJCategoryService = jJCategoryService;
	}

	@Override
	public List<JJTask> getTastksByVersion(JJVersion jJversion) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJTask> criteriaQuery = criteriaBuilder
				.createQuery(JJTask.class);
		Root<JJTask> from = criteriaQuery.from(JJTask.class);
		CriteriaQuery<JJTask> select = criteriaQuery.select(from);
		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates
				.add(criteriaBuilder.equal(from.get("versioning"), jJversion));
		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));
		select.orderBy(criteriaBuilder.desc(from.get("name")));

		TypedQuery<JJTask> result = entityManager.createQuery(select);
		return result.getResultList();

	}

	public List<JJTask> getSuperimposeTasks(JJContact assignedTo,
			Date startDate, Date endDate, JJTask task) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJTask> criteriaQuery = criteriaBuilder
				.createQuery(JJTask.class);
		Root<JJTask> from = criteriaQuery.from(JJTask.class);
		List<Predicate> andPredicates = new ArrayList<Predicate>();
		List<Predicate> orPredicates = new ArrayList<Predicate>();
		CriteriaQuery<JJTask> select = criteriaQuery.select(from);

		if (assignedTo != null) {
			andPredicates.add(criteriaBuilder.equal(from.get("assignedTo"),
					assignedTo));
		}

		andPredicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		if (task != null)
			andPredicates.add(criteriaBuilder.notEqual(from.get("id"),
					task.getId()));
		andPredicates.add(criteriaBuilder.isNotNull(from.get("startDateReal")));

		andPredicates.add(criteriaBuilder.lessThan(
				from.<Date> get("startDateReal"), endDate));

		orPredicates.add(criteriaBuilder.between(
				from.<Date> get("startDateReal"), startDate, endDate));
		orPredicates.add(criteriaBuilder.equal(
				from.<Date> get("startDateReal"), startDate));

		orPredicates.add(criteriaBuilder.between(
				from.<Date> get("endDateReal"), startDate, endDate));
		orPredicates.add(criteriaBuilder.equal(from.<Date> get("endDateReal"),
				endDate));

		orPredicates.add(criteriaBuilder.and(criteriaBuilder.lessThan(
				from.<Date> get("startDateReal"), startDate), criteriaBuilder
				.isNull(from.get("endDateReal"))));

		orPredicates.add(criteriaBuilder.and(criteriaBuilder.lessThan(
				from.<Date> get("startDateReal"), startDate), criteriaBuilder
				.greaterThan(from.<Date> get("endDateReal"), endDate)));

		select.where(
				criteriaBuilder.and(andPredicates.toArray(new Predicate[] {})),
				criteriaBuilder.or(orPredicates.toArray(new Predicate[] {})));

		select.orderBy(criteriaBuilder.asc(from.get("startDateReal")));
		TypedQuery<JJTask> result = entityManager.createQuery(select);

		return result.getResultList();
	}

	@Override
	public List<JJTask> getTasks(JJProject project, JJProduct product,
			JJContact assignedTo, Date startDate, Date endDate) {

		Set<JJTask> returnedValue = new HashSet<JJTask>();

		returnedValue.addAll(new HashSet<JJTask>(getTasks(project, product,
				assignedTo, startDate, endDate, "requirement")));
		returnedValue.addAll(new HashSet<JJTask>(getTasks(project, product,
				assignedTo, startDate, endDate, "bug")));
		returnedValue.addAll(new HashSet<JJTask>(getTasks(project, product,
				assignedTo, startDate, endDate, "testcase")));

		return new ArrayList<JJTask>(returnedValue);

	}

	private List<JJTask> getTasks(JJProject project, JJProduct product,
			JJContact assignedTo, Date startDate, Date endDate, String objet) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJTask> criteriaQuery = criteriaBuilder
				.createQuery(JJTask.class);

		Root<JJTask> from = criteriaQuery.from(JJTask.class);

		List<Predicate> predicates = new ArrayList<Predicate>();
		List<Predicate> orPredicate = new ArrayList<Predicate>();

		if (assignedTo != null) {
			predicates.add(criteriaBuilder.equal(from.get("assignedTo"),
					assignedTo));
		}

		predicates.add(criteriaBuilder.isNotNull(from.get("startDateReal")));
		predicates.add(criteriaBuilder.isNotNull(from.get("endDateReal")));

		predicates.add(criteriaBuilder.lessThan(
				from.<Date> get("startDateReal"), endDate));

		orPredicate.add(criteriaBuilder.between(
				from.<Date> get("startDateReal"), startDate, endDate));
		orPredicate.add(criteriaBuilder.equal(from.<Date> get("startDateReal"),
				startDate));

		orPredicate.add(criteriaBuilder.between(from.<Date> get("endDateReal"),
				startDate, endDate));
		orPredicate.add(criteriaBuilder.equal(from.<Date> get("endDateReal"),
				endDate));

		orPredicate.add(criteriaBuilder.and(criteriaBuilder.lessThan(
				from.<Date> get("startDateReal"), startDate), criteriaBuilder
				.isNull(from.get("endDateReal"))));

		orPredicate.add(criteriaBuilder.and(criteriaBuilder.lessThan(
				from.<Date> get("startDateReal"), startDate), criteriaBuilder
				.greaterThan(from.<Date> get("endDateReal"), endDate)));

		if (objet != null && project != null) {

			if (objet.equalsIgnoreCase("bug") || objet.equalsIgnoreCase("b")) {
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

			List<Predicate> andPredicates = new ArrayList<Predicate>();

			if (objet.equalsIgnoreCase("requirement")) {
				andPredicates.add(criteriaBuilder.equal(from
						.join("requirement").get("product"), product));
				andPredicates.add(criteriaBuilder.isNotNull(from
						.get("requirement")));
				predicates.add(criteriaBuilder.and(andPredicates
						.toArray(new Predicate[] {})));
			} else if (objet.equalsIgnoreCase("bug")
					|| objet.equalsIgnoreCase("b")) {

				andPredicates.add(criteriaBuilder.equal(
						from.get("bug").get("versioning").get("product"),
						product));
				andPredicates.add(criteriaBuilder.isNotNull(from.get("bug")));

				predicates.add(criteriaBuilder.and(andPredicates
						.toArray(new Predicate[] {})));
			}

			else if (objet.equalsIgnoreCase("testcase")) {
				andPredicates.add(criteriaBuilder.equal(from.get("testcase")
						.get("requirement").get("product"), product));
				andPredicates.add(criteriaBuilder.isNotNull(from
						.get("testcase")));

				predicates.add(criteriaBuilder.and(andPredicates
						.toArray(new Predicate[] {})));
			}

		}

		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

		CriteriaQuery<JJTask> select = criteriaQuery.select(from);

		select.where(
				criteriaBuilder.and(predicates.toArray(new Predicate[] {})),
				criteriaBuilder.or(orPredicate.toArray(new Predicate[] {})));

		select.orderBy(criteriaBuilder.asc(from.get("creationDate")));

		TypedQuery<JJTask> result = entityManager.createQuery(select);

		return result.getResultList();

	}

	@Override
	public List<JJTask> getTasks(JJSprint sprint, JJProject project,
			JJProduct product, JJContact contact, JJChapter chapter,
			boolean nullChapter, JJRequirement requirement,
			JJTestcase testcase, JJBuild build, boolean onlyActif,
			boolean sortedByCreationDate, boolean withBuild, String objet) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJTask> criteriaQuery = criteriaBuilder
				.createQuery(JJTask.class);

		Root<JJTask> from = criteriaQuery.from(JJTask.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (contact != null) {
			predicates.add(criteriaBuilder.equal(from.get("assignedTo"),
					contact));
		}

		if (sprint != null) {
			predicates.add(criteriaBuilder.equal(from.get("sprint"), sprint));
		}

		if (objet != null && project != null && chapter == null) {

			if (objet.equalsIgnoreCase("bug") || objet.equalsIgnoreCase("b")) {
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

			List<Predicate> andPredicates = new ArrayList<Predicate>();

			if (objet.equalsIgnoreCase("requirement")) {
				andPredicates.add(criteriaBuilder.equal(from
						.join("requirement").get("product"), product));
				andPredicates.add(criteriaBuilder.isNotNull(from
						.get("requirement")));
				predicates.add(criteriaBuilder.and(andPredicates
						.toArray(new Predicate[] {})));
			} else if (objet.equalsIgnoreCase("bug")
					|| objet.equalsIgnoreCase("b")) {

				andPredicates.add(criteriaBuilder.equal(
						from.get("bug").get("versioning").get("product"),
						product));
				andPredicates.add(criteriaBuilder.isNotNull(from.get("bug")));

				predicates.add(criteriaBuilder.and(andPredicates
						.toArray(new Predicate[] {})));
			}

			else if (objet.equalsIgnoreCase("testcase")) {
				andPredicates.add(criteriaBuilder.equal(from.get("testcase")
						.get("requirement").get("product"), product));
				andPredicates.add(criteriaBuilder.isNotNull(from
						.get("testcase")));

				predicates.add(criteriaBuilder.and(andPredicates
						.toArray(new Predicate[] {})));
			}

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
		} else if (nullChapter && objet != null && chapter == null) {
			if (objet.equalsIgnoreCase("b")) {
				predicates.add(criteriaBuilder.isNull(from.join("bug").get(
						"requirement")));

			} else if (objet.equalsIgnoreCase("requirement")) {
				predicates.add(criteriaBuilder.isNull(from.join("requirement")
						.get("chapter")));

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
				predicates.add(criteriaBuilder.isMember(build,
						from.<Set<JJBuild>> get("builds")));
			} else {
				predicates.add(criteriaBuilder.isEmpty(from
						.<Set<JJBuild>> get("builds")));
			}
		}

		if (requirement != null) {
			predicates.add(criteriaBuilder.equal(from.get("requirement"),
					requirement));
		}

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		CriteriaQuery<JJTask> select = criteriaQuery.select(from);

		if (objet != null && chapter == null && objet.equalsIgnoreCase("bug")) {

			predicates.add(criteriaBuilder.isNull(from.join("bug")
					.join("requirement").get("chapter")));

			select.where(criteriaBuilder.and(predicates
					.toArray(new Predicate[] {})));

			if (sortedByCreationDate) {
				select.orderBy(criteriaBuilder.asc(from.get("creationDate")));
			}

			List<JJTask> resuList = new ArrayList<JJTask>();
			resuList.addAll(entityManager.createQuery(select).getResultList());
			resuList.addAll(getTasks(sprint, project, product, contact,
					chapter, nullChapter, requirement, testcase, build,
					onlyActif, sortedByCreationDate, withBuild, "b"));
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

		if (requirement != null) {
			predicates.add(criteriaBuilder.equal(from.get("requirement"),
					requirement));
		}

		if (testcase != null) {
			predicates
					.add(criteriaBuilder.equal(from.get("testcase"), testcase));
		}

		if (bug != null) {
			predicates.add(criteriaBuilder.equal(from.get("bug"), bug));
		}

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		TypedQuery<JJTask> result = entityManager.createQuery(select);

		return result.getResultList();

	}

	public boolean haveTask(Object object, boolean onlyActif, boolean finished) {
		if (object != null
				&& (object instanceof JJBug || object instanceof JJRequirement || object instanceof JJTestcase)) {
			CriteriaBuilder criteriaBuilder = entityManager
					.getCriteriaBuilder();
			CriteriaQuery<Long> select = criteriaBuilder
					.createQuery(Long.class);

			Root<JJTask> from = select.from(JJTask.class);
			select.select(criteriaBuilder.count(from));

			List<Predicate> predicates = new ArrayList<Predicate>();

			if (object instanceof JJRequirement) {
				predicates.add(criteriaBuilder.equal(from.get("requirement"),
						object));
			} else if (object instanceof JJBug) {
				predicates.add(criteriaBuilder.equal(from.get("bug"), object));
			} else if (object instanceof JJTestcase) {
				predicates.add(criteriaBuilder.equal(from.get("testcase"),
						object));
			}

			if (onlyActif) {
				predicates
						.add(criteriaBuilder.equal(from.get("enabled"), true));
			}

			select.where(criteriaBuilder.and(predicates
					.toArray(new Predicate[] {})));

			if (finished
					&& (entityManager.createQuery(select).getSingleResult() > 0 || (object instanceof JJRequirement
							&& !jJCategoryService.isHighLevel(
									((JJRequirement) object).getCategory(),
									((JJRequirement) object).getProject()
											.getCompany()) && jJRequirementService
								.haveLinkUp((JJRequirement) object))))
				return isFinished(object, onlyActif);
			else
				return entityManager.createQuery(select).getSingleResult() > 0;

		} else
			return false;
	}

	private boolean isFinished(Object object, boolean onlyActif) {
		if (object != null
				&& (object instanceof JJBug || object instanceof JJRequirement || object instanceof JJTestcase)) {
			CriteriaBuilder criteriaBuilder = entityManager
					.getCriteriaBuilder();
			CriteriaQuery<Long> select = criteriaBuilder
					.createQuery(Long.class);

			Root<JJTask> from = select.from(JJTask.class);
			select.select(criteriaBuilder.count(from));

			List<Predicate> predicates = new ArrayList<Predicate>();

			if (object instanceof JJRequirement) {
				predicates.add(criteriaBuilder.equal(from.get("requirement"),
						object));
			} else if (object instanceof JJBug) {
				predicates.add(criteriaBuilder.equal(from.get("bug"), object));
			} else if (object instanceof JJTestcase) {
				predicates.add(criteriaBuilder.equal(from.get("testcase"),
						object));
			}

			predicates.add(criteriaBuilder.notEqual(
					criteriaBuilder.lower(from.get("status").<String> get(
							"name")), "DONE".toLowerCase()));

			if (onlyActif) {
				predicates
						.add(criteriaBuilder.equal(from.get("enabled"), true));
			}

			select.where(criteriaBuilder.and(predicates
					.toArray(new Predicate[] {})));

			if (object instanceof JJRequirement
					&& entityManager.createQuery(select).getSingleResult() == 0
					&& !jJCategoryService.isHighLevel(
							((JJRequirement) object).getCategory(),
							((JJRequirement) object).getProject().getCompany())
					&& jJRequirementService.haveLinkUp((JJRequirement) object)) {
				boolean isFinished = true;
				List<JJRequirement> requirements = new ArrayList<JJRequirement>(
						jJRequirementService.findJJRequirement(
								((JJRequirement) object).getId())
								.getRequirementLinkUp());
				int i = 0;
				while (isFinished && i < requirements.size()) {
					isFinished = haveTask(requirements.get(i), onlyActif, true);
					i++;
				}
				return isFinished;
			} else
				return entityManager.createQuery(select).getSingleResult() == 0;

		} else
			return false;
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

		if (project != null) {
			Path<Object> path = from.join("requirement").get("project");
			predicates.add(criteriaBuilder.equal(path, project));
		}

		if (status != null) {
			predicates.add(criteriaBuilder.equal(from.get("status"), status));
		}

		if (sprint != null) {
			predicates.add(criteriaBuilder.equal(from.get("sprint"), sprint));
		}

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		TypedQuery<JJTask> result = entityManager.createQuery(select);

		return result.getResultList();

	}

	@Override
	public List<JJTask> getSprintTasks(JJSprint sprint, JJProduct product) {

		Set<JJTask> returnedValue = new HashSet<JJTask>();

		returnedValue.addAll(new HashSet<JJTask>(getTasks(sprint, null,
				product, null, null, false, null, null, null, true, false,
				false, "requirement")));
		returnedValue.addAll(new HashSet<JJTask>(getTasks(sprint, null,
				product, null, null, false, null, null, null, true, false,
				false, "bug")));
		returnedValue.addAll(new HashSet<JJTask>(getTasks(sprint, null,
				product, null, null, false, null, null, null, true, false,
				false, "testcase")));

		return new ArrayList<JJTask>(returnedValue);

	}

	@Override
	public List<JJTask> getTasksByProduct(JJProduct product, JJProject project) {

		Set<JJTask> returnedValue = new HashSet<JJTask>();

		returnedValue.addAll(new HashSet<JJTask>(getTasks(null, project,
				product, null, null, false, null, null, null, true, false,
				false, "requirement")));
		returnedValue.addAll(new HashSet<JJTask>(getTasks(null, project,
				product, null, null, false, null, null, null, true, false,
				false, "bug")));
		returnedValue.addAll(new HashSet<JJTask>(getTasks(null, project,
				product, null, null, false, null, null, null, true, false,
				false, "testcase")));

		return new ArrayList<JJTask>(returnedValue);

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

			predicates.add(criteriaBuilder.equal(from.get("assignedTo"),
					contact));
			predicates.add(criteriaBuilder.isNull(from.get("endDateReal")));
			predicates.add(criteriaBuilder.notEqual(
					criteriaBuilder.lower(from.get("status").<String> get(
							"name")), "DONE".toLowerCase()));
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

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
		for (JJTask t : tasks) {
			saveJJTask(t);
		}
	}

	@Override
	public void updateTasks(Set<JJTask> tasks) {
		for (JJTask t : tasks) {
			updateJJTask(t);
		}
	}

	public void saveJJTask(JJTask JJTask_) {

		if (JJTask_.getName().length() > 100) {
			JJTask_.setName(JJTask_.getName().substring(0, 99));
		}
		if (JJTask_.getWorkloadPlanned() == null)
			JJTask_.setWorkloadPlanned(0);
		jJTaskRepository.save(JJTask_);
		JJTask_ = jJTaskRepository.findOne(JJTask_.getId());
	}

	public JJTask updateJJTask(JJTask JJTask_) {

		if (JJTask_.getName().length() > 100) {
			JJTask_.setName(JJTask_.getName().substring(0, 99));
		}
		if (JJTask_.getWorkloadPlanned() == null)
			JJTask_.setWorkloadPlanned(0);
		jJTaskRepository.save(JJTask_);
		JJTask_ = jJTaskRepository.findOne(JJTask_.getId());
		return JJTask_;
	}

}

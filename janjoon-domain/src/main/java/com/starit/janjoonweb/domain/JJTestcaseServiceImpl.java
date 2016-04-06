package com.starit.janjoonweb.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
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
import javax.persistence.criteria.Subquery;

import org.springframework.beans.factory.annotation.Autowired;

import com.starit.janjoonweb.domain.reference.Contacts;

public class JJTestcaseServiceImpl implements JJTestcaseService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Autowired
	private JJBuildService jJBuildService;

	public void setjJBuildService(JJBuildService jJBuildService) {
		this.jJBuildService = jJBuildService;
	}

	@Override
	public List<JJTestcase> getTestcases(JJRequirement requirement,
			JJChapter chapter, JJVersion version, JJBuild build,
			boolean onlyActif, boolean sortedByOrder,
			boolean sortedByCreationdate) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJTestcase> criteriaQuery = criteriaBuilder
				.createQuery(JJTestcase.class);

		Root<JJTestcase> from = criteriaQuery.from(JJTestcase.class);

		CriteriaQuery<JJTestcase> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (chapter != null) {
			Path<Object> path = from.join("requirement").get("chapter");
			predicates.add(criteriaBuilder.equal(path, chapter));
		}

		// if (version != null) {
		// Path<Object> path = from.join("requirement").get("versioning");
		// predicates.add(criteriaBuilder.equal(path, version));
		// }

		if (requirement != null) {
			predicates.add(criteriaBuilder.equal(from.get("requirement"),
					requirement));
		}

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		if (build != null && (build.getAllTestcases() == null
				|| !build.getAllTestcases())) {
			predicates.add(criteriaBuilder.or(
					criteriaBuilder.isMember(build,
							from.<Set<JJBuild>> get("builds")),
					criteriaBuilder.equal(from.get("allBuilds"), true)));

			select.where(
					criteriaBuilder.and(predicates.toArray(new Predicate[]{})));

			if (sortedByOrder) {
				select.orderBy(criteriaBuilder.asc(from.get("ordering")));
			}

			if (sortedByCreationdate) {
				select.orderBy(criteriaBuilder.asc(from.get("creationDate")));
			}

			TypedQuery<JJTestcase> result = entityManager.createQuery(select);
			return new ArrayList<JJTestcase>(
					new HashSet<JJTestcase>(result.getResultList()));
		} else if (build == null && version != null
				&& !jJBuildService.haveAllTestCases(version)) {

			List<Predicate> predicates2 = new ArrayList<Predicate>(predicates);

			predicates.add(criteriaBuilder.equal(from.get("allBuilds"), true));

			select.where(
					criteriaBuilder.and(predicates.toArray(new Predicate[]{})));
			TypedQuery<JJTestcase> result = entityManager.createQuery(select);
			Set<JJTestcase> hash = new HashSet<JJTestcase>(
					result.getResultList());

			Join<JJTestcase, JJBuild> join = from.join("builds");
			join.alias("buildsAlias");
			predicates2.add(criteriaBuilder.and(
					criteriaBuilder.equal(join.get("enabled"), true),
					criteriaBuilder.equal(join.get("version"), version)));

			select.where(criteriaBuilder
					.and(predicates2.toArray(new Predicate[]{})));
			TypedQuery<JJTestcase> result2 = entityManager.createQuery(select);
			hash.addAll(result2.getResultList());
			List<JJTestcase> list = new ArrayList<JJTestcase>(hash);

			if (sortedByOrder) {
				Collections.sort(list, new Comparator<JJTestcase>() {

					@Override
					public int compare(JJTestcase o1, JJTestcase o2) {
						if (o1.getOrdering() == null
								&& o2.getOrdering() == null)
							return 0;
						else if (o1.getOrdering() == null)
							return -1;
						else if (o2.getOrdering() == null)
							return 1;
						else
							return o1.getOrdering().compareTo(o2.getOrdering());
					}
				});

			} else if (sortedByCreationdate) {
				Collections.sort(list, new Comparator<JJTestcase>() {

					@Override
					public int compare(JJTestcase o1, JJTestcase o2) {
						return o1.getCreationDate()
								.compareTo(o2.getCreationDate());
					}
				});
			}

			return list;

		} else {
			select.where(
					criteriaBuilder.and(predicates.toArray(new Predicate[]{})));

			if (sortedByOrder) {
				select.orderBy(criteriaBuilder.asc(from.get("ordering")));
			}

			if (sortedByCreationdate) {
				select.orderBy(criteriaBuilder.asc(from.get("creationDate")));
			}

			TypedQuery<JJTestcase> result = entityManager.createQuery(select);
			return new ArrayList<JJTestcase>(
					new HashSet<JJTestcase>(result.getResultList()));
		}

	}

	public List<JJContact> getTestCaseContacts(JJProject project,
			JJProduct product, JJVersion version, boolean onlyActif) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Contacts> criteriaQuery = criteriaBuilder
				.createQuery(Contacts.class);

		Root<JJTestcaseexecution> fromExec = criteriaQuery
				.from(JJTestcaseexecution.class);

		Root<JJTestcase> fromTest = criteriaQuery.from(JJTestcase.class);

		CriteriaQuery<Contacts> select = criteriaQuery.multiselect(
				fromExec.<JJContact> get("createdBy"),
				fromExec.<JJContact> get("updatedBy"),
				fromTest.<JJContact> get("createdBy"),
				fromTest.<JJContact> get("updatedBy"));

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (project != null) {
			Path<Object> path = fromExec.join("testcase").join("requirement")
					.get("project");
			predicates.add(criteriaBuilder.equal(path, project));
			predicates.add(criteriaBuilder.equal(
					fromTest.join("requirement").get("project"), project));
		}

		if (version != null) {
			Path<Object> path = fromExec.join("testcase").join("requirement")
					.get("versioning");
			predicates.add(criteriaBuilder.equal(path, version));

			predicates.add(criteriaBuilder.equal(
					fromTest.join("requirement").get("versioning"), version));

		} else if (product != null) {
			Path<Object> path = fromExec.join("testcase").join("requirement")
					.get("product");
			predicates.add(criteriaBuilder.equal(path, product));
			predicates.add(criteriaBuilder.equal(
					fromTest.join("requirement").get("product"), product));
		}

		if (onlyActif) {

			predicates
					.add(criteriaBuilder.equal(fromTest.get("enabled"), true));
			predicates.add(criteriaBuilder
					.equal(fromTest.join("requirement").get("enabled"), true));

			predicates.add(criteriaBuilder
					.equal(fromExec.join("testcase").get("enabled"), true));
			predicates.add(criteriaBuilder.equal(fromExec.join("testcase")
					.join("requirement").get("enabled"), true));
			predicates
					.add(criteriaBuilder.equal(fromExec.get("enabled"), true));
		}

		select.where(
				criteriaBuilder.and(predicates.toArray(new Predicate[]{})));
		// select.groupBy(fromExec.get("testcase"));
		// select.orderBy(criteriaBuilder.desc(fromExec.get("updatedDate")));
		//
		// // return entityManager.createQuery(select).getResultList();

		TypedQuery<Contacts> result = entityManager.createQuery(select);
		return Contacts.getJJContacts(result.getResultList());

	}

	public List<String> getReqTesCases(JJRequirement requirement) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<String> criteriaQuery = criteriaBuilder
				.createQuery(String.class);

		Root<JJTestcase> from = criteriaQuery.from(JJTestcase.class);

		List<Predicate> predicates = new ArrayList<Predicate>();
		if (requirement != null) {
			predicates.add(criteriaBuilder.equal(from.get("requirement"),
					requirement));
		}

		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

		criteriaQuery.multiselect(from.get("name"));
		criteriaQuery.where(
				criteriaBuilder.and(predicates.toArray(new Predicate[]{})));

		TypedQuery<String> result = entityManager.createQuery(criteriaQuery);
		return result.getResultList();

	}

	public Long getTestCaseCountByLastResult(JJProject project,
			JJProduct product, JJVersion version, JJBuild build,
			boolean onlyActif, Boolean passed, JJContact executor) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder
				.createQuery(Long.class);

		Root<JJTestcaseexecution> from = criteriaQuery
				.from(JJTestcaseexecution.class);

		CriteriaQuery<Long> select = criteriaQuery
				.select(criteriaBuilder.count(from));

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (project != null) {
			Path<Object> path = from.join("testcase").join("requirement")
					.get("project");
			predicates.add(criteriaBuilder.equal(path, project));
		}

		if (version != null) {
			Path<Object> path = from.join("testcase").join("requirement")
					.get("versioning");
			predicates.add(criteriaBuilder.equal(path, version));

		} else if (product != null) {
			Path<Object> path = from.join("testcase").join("requirement")
					.get("product");
			predicates.add(criteriaBuilder.equal(path, product));
		}

		if (onlyActif) {
			predicates.add(criteriaBuilder
					.equal(from.join("testcase").get("enabled"), true));
			predicates.add(criteriaBuilder.equal(
					from.join("testcase").join("requirement").get("enabled"),
					true));
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		if (passed != null)
			predicates.add(criteriaBuilder.equal(from.get("passed"), passed));
		else
			predicates.add(criteriaBuilder.isNull(from.get("passed")));

		predicates.add(criteriaBuilder.equal(from.get("build"), build));

		if (executor != null) {

			if (passed != null)
				predicates.add(
						criteriaBuilder.equal(from.get("updatedBy"), executor));
			else
				predicates.add(
						criteriaBuilder.equal(from.get("createdBy"), executor));
		}

		if (build != null && (build.getAllTestcases() == null
				|| !build.getAllTestcases())) {
			predicates
					.add(criteriaBuilder.or(
							criteriaBuilder.isMember(build,
									from.join("testcase")
											.<Set<JJBuild>> get("builds")),
							criteriaBuilder.equal(
									from.join("testcase").get("allBuilds"),
									true)));

		}

		select.where(
				criteriaBuilder.and(predicates.toArray(new Predicate[]{})));
		select.groupBy(from.get("testcase"));
		select.orderBy(criteriaBuilder.desc(from.get("updatedDate")));
		if (passed != null) {
			if (!entityManager.createQuery(select).getResultList().isEmpty())
				return entityManager.createQuery(select).getSingleResult();
			else
				return 0L;

		} else {
			Long number = 0L;

			if (!entityManager.createQuery(select).getResultList().isEmpty())
				number = entityManager.createQuery(select).getSingleResult();

			CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);

			Root<JJTestcase> fromTest = query.from(JJTestcase.class);

			CriteriaQuery<Long> selectTest = query
					.select(criteriaBuilder.count(fromTest));

			List<Predicate> predicatesTest = new ArrayList<Predicate>();

			if (project != null) {
				Path<Object> path = fromTest.join("requirement").get("project");
				predicatesTest.add(criteriaBuilder.equal(path, project));
			}

			if (version != null) {
				Path<Object> path = fromTest.join("requirement")
						.get("versioning");
				predicatesTest.add(criteriaBuilder.equal(path, version));

			} else if (product != null) {
				Path<Object> path = fromTest.join("requirement").get("product");
				predicatesTest.add(criteriaBuilder.equal(path, product));
			}

			if (onlyActif) {

				predicatesTest.add(criteriaBuilder.equal(
						fromTest.join("requirement").get("enabled"), true));
				predicatesTest.add(
						criteriaBuilder.equal(fromTest.get("enabled"), true));
			}

			if (executor != null) {
				predicatesTest.add(criteriaBuilder
						.equal(fromTest.get("createdBy"), executor));
			}

			// select from testcaseexecution
			{
				Subquery<Long> subquery = selectTest.subquery(Long.class);
				Root<JJTestcaseexecution> fromTestCaseExecution = subquery
						.from(JJTestcaseexecution.class);
				List<Predicate> predicatesExec = new ArrayList<Predicate>();
				subquery.select(
						fromTestCaseExecution.get("testcase").<Long> get("id"));

				predicatesExec.add(criteriaBuilder
						.equal(fromTestCaseExecution.get("build"), build));

				subquery.where(criteriaBuilder.and(
						criteriaBuilder
								.and(predicatesExec.toArray(new Predicate[]{})),
						criteriaBuilder.equal(
								fromTestCaseExecution.get("enabled"), true)));

				predicatesTest.add(
						criteriaBuilder.not(fromTest.get("id").in(subquery)));
				// builder.not(root.get({field_name}).in(seqs))
			}

			if (build != null && (build.getAllTestcases() == null
					|| !build.getAllTestcases())) {
				predicatesTest.add(criteriaBuilder.or(
						criteriaBuilder.isMember(build,
								fromTest.<Set<JJBuild>> get("builds")),
						criteriaBuilder.equal(fromTest.get("allBuilds"),
								true)));

			}

			selectTest.where(criteriaBuilder
					.and(predicatesTest.toArray(new Predicate[]{})));

			if (!entityManager.createQuery(selectTest).getResultList()
					.isEmpty())
				return entityManager.createQuery(selectTest).getSingleResult()
						+ number;
			else
				return number;

		}

	}

	@Override
	public List<JJTestcase> getImportTestcases(JJCategory category,
			JJProject project, JJProduct product, JJVersion version,
			JJBuild build, boolean withoutTask, boolean onlyActif,
			boolean withOutChapter) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJTestcase> criteriaQuery = criteriaBuilder
				.createQuery(JJTestcase.class);

		Root<JJTestcase> from = criteriaQuery.from(JJTestcase.class);

		CriteriaQuery<JJTestcase> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (project != null) {
			Path<Object> path = from.join("requirement").get("project");
			predicates.add(criteriaBuilder.equal(path, project));
		}

		if (withoutTask) {
			Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
			Root<JJTask> fromTask = subquery.from(JJTask.class);
			List<Predicate> predicatesTask = new ArrayList<Predicate>();
			subquery.select(fromTask.get("testcase").<Long> get("id"));
			predicatesTask.add(criteriaBuilder.isNotNull(fromTask.get("testcase")));
			predicatesTask
					.add(criteriaBuilder.equal(fromTask.get("enabled"), true));

			subquery.where(criteriaBuilder
					.and(predicatesTask.toArray(new Predicate[]{})));
			predicates.add(
					criteriaBuilder.in(from.get("id")).value(subquery).not());

		}
		
		if (product != null) {
			Path<Object> path = from.join("requirement").get("product");
			predicates.add(criteriaBuilder.equal(path, product));
		}

		if (withOutChapter) {
			predicates.add(criteriaBuilder
					.isNull(from.join("requirement").get("chapter")));

		}

		if (category != null) {
			predicates.add(criteriaBuilder
					.isNotNull(from.join("requirement").get("category")));
			predicates.add(criteriaBuilder
					.equal(from.join("requirement").get("category"), category));
			predicates.add(criteriaBuilder
					.equal(from.join("requirement").get("enabled"), true));

		}

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		if (build != null && (build.getAllTestcases() == null
				|| !build.getAllTestcases())) {
			predicates.add(criteriaBuilder.or(
					criteriaBuilder.isMember(build,
							from.<Set<JJBuild>> get("builds")),
					criteriaBuilder.equal(from.get("allBuilds"), true)));
			select.where(
					criteriaBuilder.and(predicates.toArray(new Predicate[]{})));

			TypedQuery<JJTestcase> result = entityManager.createQuery(select);
			return new ArrayList<JJTestcase>(
					new HashSet<JJTestcase>(result.getResultList()));

		} else if (build == null && version != null
				&& !jJBuildService.haveAllTestCases(version)) {

			List<Predicate> predicates2 = new ArrayList<Predicate>(predicates);

			predicates.add(criteriaBuilder.equal(from.get("allBuilds"), true));

			select.where(
					criteriaBuilder.and(predicates.toArray(new Predicate[]{})));
			TypedQuery<JJTestcase> result = entityManager.createQuery(select);
			Set<JJTestcase> hash = new HashSet<JJTestcase>(
					result.getResultList());

			Join<JJTestcase, JJBuild> join = from.join("builds");
			join.alias("buildsAlias");
			predicates2.add(criteriaBuilder.and(
					criteriaBuilder.equal(join.get("enabled"), true),
					criteriaBuilder.equal(join.get("version"), version)));

			select.where(criteriaBuilder
					.and(predicates2.toArray(new Predicate[]{})));
			TypedQuery<JJTestcase> result2 = entityManager.createQuery(select);
			hash.addAll(result2.getResultList());

			return new ArrayList<JJTestcase>(hash);
		} else {
			select.where(
					criteriaBuilder.and(predicates.toArray(new Predicate[]{})));

			TypedQuery<JJTestcase> result = entityManager.createQuery(select);
			return new ArrayList<JJTestcase>(
					new HashSet<JJTestcase>(result.getResultList()));
		}

	}

	public Integer getMaxOrdering(JJRequirement requirement) {

		Integer r = null;
		if (requirement.getChapter() != null)
			r = (Integer) entityManager
					.createQuery(
							"select max(e.ordering) from JJTestcase e Where e.requirement.chapter = :c")
					.setParameter("c", requirement.getChapter())
					.getSingleResult();
		else
			r = (Integer) entityManager
					.createQuery(
							"select max(e.ordering) from JJTestcase e Where e.requirement = :c")
					.setParameter("c", requirement).getSingleResult();

		if (r != null)
			return r + 1;
		else
			return 1;
	}

	@Override
	public void saveTestcases(Set<JJTestcase> testcases) {
		jJTestcaseRepository.save(testcases);
	}

	@Override
	public List<JJTestcase> getJJtestCases(JJRequirement requirement) {
		return getTestcases(requirement, null, null, null, true, true, true);
	}

	@Override
	public void updateTestcases(Set<JJTestcase> testcases) {
		jJTestcaseRepository.save(testcases);
	}

	public void saveJJTestcase(JJTestcase JJTestcase_) {

		jJTestcaseRepository.save(JJTestcase_);
		JJTestcase_ = jJTestcaseRepository.findOne(JJTestcase_.getId());
	}

	public JJTestcase updateJJTestcase(JJTestcase JJTestcase_) {
		jJTestcaseRepository.save(JJTestcase_);
		JJTestcase_ = jJTestcaseRepository.findOne(JJTestcase_.getId());
		return JJTestcase_;
	}
}

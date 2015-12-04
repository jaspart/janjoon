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
import javax.persistence.criteria.SetJoin;
import javax.persistence.criteria.Subquery;

import org.springframework.beans.factory.annotation.Autowired;

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

		if (build != null && (build.getAllTestcases() == null || !build.getAllTestcases())) {
			predicates.add(criteriaBuilder.or(
					criteriaBuilder.isMember(build,
							from.<Set<JJBuild>> get("builds")),
					criteriaBuilder.equal(from.get("allBuilds"), true)));

			select.where(criteriaBuilder.and(predicates
					.toArray(new Predicate[] {})));

			if (sortedByOrder) {
				select.orderBy(criteriaBuilder.asc(from.get("ordering")));
			}

			if (sortedByCreationdate) {
				select.orderBy(criteriaBuilder.asc(from.get("creationDate")));
			}

			TypedQuery<JJTestcase> result = entityManager.createQuery(select);
			return new ArrayList<JJTestcase>(new HashSet<JJTestcase>(
					result.getResultList()));
		} else if (build == null && version != null && !jJBuildService.haveAllTestCases(version)) {

			List<Predicate> predicates2 = new ArrayList<Predicate>(predicates);

			predicates.add(criteriaBuilder.equal(from.get("allBuilds"), true));

			select.where(criteriaBuilder.and(predicates
					.toArray(new Predicate[] {})));
			TypedQuery<JJTestcase> result = entityManager.createQuery(select);
			Set<JJTestcase> hash = new HashSet<JJTestcase>(
					result.getResultList());

			Join<JJTestcase, JJBuild> join = from.join("builds");
			join.alias("buildsAlias");
			predicates2.add(criteriaBuilder.and(
					criteriaBuilder.equal(join.get("enabled"), true),
					criteriaBuilder.equal(join.get("version"), version)));

			select.where(criteriaBuilder.and(predicates2
					.toArray(new Predicate[] {})));
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
						return o1.getCreationDate().compareTo(
								o2.getCreationDate());
					}
				});
			}

			return list;

		} else {
			select.where(criteriaBuilder.and(predicates
					.toArray(new Predicate[] {})));

			if (sortedByOrder) {
				select.orderBy(criteriaBuilder.asc(from.get("ordering")));
			}

			if (sortedByCreationdate) {
				select.orderBy(criteriaBuilder.asc(from.get("creationDate")));
			}

			TypedQuery<JJTestcase> result = entityManager.createQuery(select);
			return new ArrayList<JJTestcase>(new HashSet<JJTestcase>(
					result.getResultList()));
		}

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
		criteriaQuery.where(criteriaBuilder.and(predicates
				.toArray(new Predicate[] {})));

		TypedQuery<String> result = entityManager.createQuery(criteriaQuery);
		return result.getResultList();

	}

	@Override
	public List<JJTestcase> getImportTestcases(JJCategory category,
			JJProject project, JJProduct product, JJVersion version,
			JJBuild build, boolean onlyActif, boolean withOutChapter) {
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

		// if (version != null) {
		// Path<Object> path = from.join("requirement").get("versioning");
		// predicates.add(criteriaBuilder.equal(path, version));
		//
		// } else
		if (product != null) {
			Path<Object> path = from.join("requirement").get("product");
			predicates.add(criteriaBuilder.equal(path, product));
		}

		if (withOutChapter) {
			predicates.add(criteriaBuilder.isNull(from.join("requirement").get(
					"chapter")));

		}

		if (category != null) {
			predicates.add(criteriaBuilder.isNotNull(from.join("requirement")
					.get("category")));
			predicates.add(criteriaBuilder.equal(
					from.join("requirement").get("category"), category));
			predicates.add(criteriaBuilder.equal(
					from.join("requirement").get("enabled"), true));

		}

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		if (build != null && (build.getAllTestcases() == null || !build.getAllTestcases()) ) {
			predicates.add(criteriaBuilder.or(
					criteriaBuilder.isMember(build,
							from.<Set<JJBuild>> get("builds")),
					criteriaBuilder.equal(from.get("allBuilds"), true)));
			select.where(criteriaBuilder.and(predicates
					.toArray(new Predicate[] {})));

			TypedQuery<JJTestcase> result = entityManager.createQuery(select);
			return new ArrayList<JJTestcase>(new HashSet<JJTestcase>(
					result.getResultList()));

		} else if (build == null && version != null && !jJBuildService.haveAllTestCases(version)) {

			
			List<Predicate> predicates2 = new ArrayList<Predicate>(predicates);

			predicates.add(criteriaBuilder.equal(from.get("allBuilds"), true));

			select.where(criteriaBuilder.and(predicates
					.toArray(new Predicate[] {})));
			TypedQuery<JJTestcase> result = entityManager.createQuery(select);
			Set<JJTestcase> hash = new HashSet<JJTestcase>(
					result.getResultList());

			Join<JJTestcase, JJBuild> join = from.join("builds");
			join.alias("buildsAlias");
			predicates2.add(criteriaBuilder.and(
					criteriaBuilder.equal(join.get("enabled"), true),
					criteriaBuilder.equal(join.get("version"), version)));

			select.where(criteriaBuilder.and(predicates2
					.toArray(new Predicate[] {})));
			TypedQuery<JJTestcase> result2 = entityManager.createQuery(select);
			hash.addAll(result2.getResultList());

			return new ArrayList<JJTestcase>(hash);
		} else {
			select.where(criteriaBuilder.and(predicates
					.toArray(new Predicate[] {})));

			TypedQuery<JJTestcase> result = entityManager.createQuery(select);
			return new ArrayList<JJTestcase>(new HashSet<JJTestcase>(
					result.getResultList()));
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

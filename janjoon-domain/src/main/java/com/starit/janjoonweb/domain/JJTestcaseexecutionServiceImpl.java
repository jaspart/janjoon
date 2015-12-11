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
import javax.persistence.criteria.Subquery;

public class JJTestcaseexecutionServiceImpl implements
		JJTestcaseexecutionService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Boolean isPassed(JJTestcase testCase, JJBuild build,
			JJVersion version) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Boolean> criteriaQuery = criteriaBuilder
				.createQuery(Boolean.class);

		Root<JJTestcaseexecution> from = criteriaQuery
				.from(JJTestcaseexecution.class);

		CriteriaQuery<Boolean> select = criteriaQuery.select(from
				.<Boolean> get("passed"));

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (testCase != null) {
			predicates
					.add(criteriaBuilder.equal(from.get("testcase"), testCase));
		}
		if (build != null) {
			predicates.add(criteriaBuilder.equal(from.get("build"), build));
		}

		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

		select.where(predicates.toArray(new Predicate[] {}));

		select.orderBy(criteriaBuilder.desc(from.get("updatedDate")));

		TypedQuery<Boolean> result = entityManager.createQuery(select);

		if (result.getResultList() == null || result.getResultList().isEmpty())
			return null;
		else
			return result.getResultList().get(0);

	}

	public boolean haveTestcaseExec(JJTestcase testcase, JJBuild build,
			JJVersion version) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJTestcaseexecution> criteriaQuery = criteriaBuilder
				.createQuery(JJTestcaseexecution.class);

		Root<JJTestcaseexecution> from = criteriaQuery
				.from(JJTestcaseexecution.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		if (testcase != null)
			predicates
					.add(criteriaBuilder.equal(from.get("testcase"), testcase));

		if (build != null) {
			predicates.add(criteriaBuilder.equal(from.get("build"), build));
		} else if (version != null)
			predicates.add(criteriaBuilder.equal(
					from.get("build").get("version"), version));

		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
		cq.select(criteriaBuilder.count(cq.from(JJTestcaseexecution.class)));
		entityManager.createQuery(cq);
		cq.where(predicates.toArray(new Predicate[] {}));
		boolean have = entityManager.createQuery(cq).getSingleResult() > 0;
		return have;
	}

	@Override
	public List<JJTestcaseexecution> getTestcaseexecutions(JJTestcase testcase,
			JJBuild build, boolean onlyActif, boolean sortedByUpdatedDate,
			boolean sortedByCreationdDate) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJTestcaseexecution> criteriaQuery = criteriaBuilder
				.createQuery(JJTestcaseexecution.class);

		Root<JJTestcaseexecution> from = criteriaQuery
				.from(JJTestcaseexecution.class);

		CriteriaQuery<JJTestcaseexecution> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (testcase != null) {
			predicates
					.add(criteriaBuilder.equal(from.get("testcase"), testcase));
		}
		if (build != null) {
			predicates.add(criteriaBuilder.equal(from.get("build"), build));
		}

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		select.where(predicates.toArray(new Predicate[] {}));

		if (sortedByUpdatedDate) {
			select.orderBy(criteriaBuilder.desc(from.get("updatedDate")));
		}

		if (sortedByCreationdDate) {
			select.orderBy(criteriaBuilder.desc(from.get("creationDate")));
		}

		TypedQuery<JJTestcaseexecution> result = entityManager
				.createQuery(select);

		return result.getResultList();

	}

	@Override
	public Set<JJTestcaseexecution> getTestcaseexecutions(JJProject project,
			JJProduct product, JJVersion version, JJCategory category,
			JJChapter chapter, JJBuild build, boolean onlyActif,
			boolean sortedByUpdatedDate, boolean withOutChapter) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJTestcaseexecution> criteriaQuery = criteriaBuilder
				.createQuery(JJTestcaseexecution.class);

		Root<JJTestcaseexecution> from = criteriaQuery
				.from(JJTestcaseexecution.class);

		Path<Object> path = from.join("testcase").get("requirement");
		from.fetch("testcase");
		CriteriaQuery<JJTestcaseexecution> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (chapter != null && !withOutChapter) {
			Subquery<JJRequirement> subquery = criteriaQuery
					.subquery(JJRequirement.class);
			Root<JJRequirement> fromJJRequirement = subquery
					.from(JJRequirement.class);
			subquery.select(fromJJRequirement);
			subquery.where(criteriaBuilder.equal(
					fromJJRequirement.get("chapter"), chapter));

			subquery.where(criteriaBuilder.equal(
					fromJJRequirement.get("enabled"), true));
			predicates.add(criteriaBuilder.in(path).value(subquery));

		}
		predicates.add(criteriaBuilder.equal(from.get("testcase")
				.get("enabled"), true));

		if (project != null) {
			predicates.add(criteriaBuilder.equal(
					from.join("testcase").join("requirement").get("project"),
					project));
		}

		if (version != null) {
			predicates.add(criteriaBuilder
					.equal(from.join("testcase").join("requirement")
							.get("versioning"), version));

		} else if (product != null) {
			predicates.add(criteriaBuilder.equal(
					from.join("testcase").join("requirement").get("product"),
					product));
		}

		if (withOutChapter) {
			predicates.add(criteriaBuilder.isNull(from.join("testcase")
					.join("requirement").get("chapter")));

		}

		if (category != null) {
			predicates.add(criteriaBuilder.isNotNull(from.join("testcase")
					.join("requirement").get("category")));
			predicates.add(criteriaBuilder.equal(
					from.join("testcase").join("requirement").get("category"),
					category));
			predicates.add(criteriaBuilder.equal(
					from.join("testcase").join("requirement").get("enabled"),
					true));

		}

		if (build != null) {

			predicates.add(criteriaBuilder.equal(from.get("build"), build));
		}

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		select.where(predicates.toArray(new Predicate[] {}));

		if (sortedByUpdatedDate) {
			select.orderBy(criteriaBuilder.desc(from.get("updatedDate")));
		}

		TypedQuery<JJTestcaseexecution> typedQuery = entityManager
				.createQuery(select);
		return new HashSet<JJTestcaseexecution>(typedQuery.getResultList());

	}

	public void saveJJTestcaseexecution(JJTestcaseexecution JJTestcaseexecution_) {

		jJTestcaseexecutionRepository.save(JJTestcaseexecution_);
		JJTestcaseexecution_ = jJTestcaseexecutionRepository
				.findOne(JJTestcaseexecution_.getId());
	}

	public JJTestcaseexecution updateJJTestcaseexecution(
			JJTestcaseexecution JJTestcaseexecution_) {
		jJTestcaseexecutionRepository.save(JJTestcaseexecution_);
		JJTestcaseexecution_ = jJTestcaseexecutionRepository
				.findOne(JJTestcaseexecution_.getId());
		return JJTestcaseexecution_;
	}

}

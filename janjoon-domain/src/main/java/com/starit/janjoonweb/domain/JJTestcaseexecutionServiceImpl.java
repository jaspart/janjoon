package com.starit.janjoonweb.domain;

import java.util.ArrayList;
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
	
	public boolean haveTestcaseExec(JJTestcase testcase,JJBuild build,JJVersion version){
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJTestcaseexecution> criteriaQuery = criteriaBuilder
				.createQuery(JJTestcaseexecution.class);

		Root<JJTestcaseexecution> from = criteriaQuery.from(JJTestcaseexecution.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		if(testcase != null)
		predicates.add(criteriaBuilder.equal(from.get("testcase"),
				testcase));
		
		if(build != null)
		{
			predicates.add(criteriaBuilder.equal(from.get("build"),
					build));
		}else if(version != null)
			predicates.add(criteriaBuilder.equal(from.get("build").get("version"),
					version));
		
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
			predicates.add(criteriaBuilder.equal(
					from.get("build"), build));
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
	public Set<JJTestcaseexecution> getTestcaseexecutions(JJChapter chapter,
			JJBuild build, boolean onlyActif, boolean sortedByUpdatedDate) {

		Set<JJTestcaseexecution> testcaseexecutions = new HashSet<JJTestcaseexecution>();

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJTestcaseexecution> criteriaQuery = criteriaBuilder
				.createQuery(JJTestcaseexecution.class);

		Root<JJTestcaseexecution> from = criteriaQuery
				.from(JJTestcaseexecution.class);

		Path<Object> path = from.join("testcase").get("requirement");
		from.fetch("testcase");
		CriteriaQuery<JJTestcaseexecution> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (chapter != null) {

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
		List<JJTestcaseexecution> result = typedQuery.getResultList();

		for (JJTestcaseexecution testcaseexecution : result) {

			testcaseexecutions.add(testcaseexecution);
		}
		return testcaseexecutions;

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

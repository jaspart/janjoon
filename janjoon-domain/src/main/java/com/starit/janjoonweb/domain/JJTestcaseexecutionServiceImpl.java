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

public class JJTestcaseexecutionServiceImpl implements
		JJTestcaseexecutionService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<JJTestcaseexecution> getTestcaseexecutions(JJTestcase testcase,
			JJBuild build, boolean onlyActif, boolean orderByCreationdate) {

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

		if (orderByCreationdate) {
			select.orderBy(criteriaBuilder.desc(from.get("creationDate")));
		}

		TypedQuery<JJTestcaseexecution> result = entityManager
				.createQuery(select);

		return result.getResultList();

	}

}

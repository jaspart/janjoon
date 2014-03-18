package com.starit.janjoonweb.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class JJTestcaseServiceImpl implements JJTestcaseService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<JJTestcase> getTestcases(JJRequirement requirement,
			boolean onlyActif, boolean sortedByOrder) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJTestcase> criteriaQuery = criteriaBuilder
				.createQuery(JJTestcase.class);

		Root<JJTestcase> from = criteriaQuery.from(JJTestcase.class);

		CriteriaQuery<JJTestcase> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();
		if (requirement != null) {
			predicates.add(criteriaBuilder.equal(from.get("requirement"),
					requirement));
		}

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		if (sortedByOrder) {
			select.orderBy(criteriaBuilder.asc(from.get("ordering")));
		}

		TypedQuery<JJTestcase> result = entityManager.createQuery(select);
		return result.getResultList();

	}

	@Override
	public void saveTestcases(Set<JJTestcase> testcases) {
		jJTestcaseRepository.save(testcases);
	}

	@Override
	public void updateTestcases(Set<JJTestcase> testcases) {
		jJTestcaseRepository.save(testcases);
	}
}

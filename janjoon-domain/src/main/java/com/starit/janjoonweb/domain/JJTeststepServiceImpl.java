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

public class JJTeststepServiceImpl implements JJTeststepService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<JJTeststep> getTeststeps(JJTestcase testcase,
			boolean onlyActif, boolean sortedByOrder) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJTeststep> criteriaQuery = criteriaBuilder
				.createQuery(JJTeststep.class);

		Root<JJTeststep> from = criteriaQuery.from(JJTeststep.class);

		CriteriaQuery<JJTeststep> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		if (testcase != null) {
			predicates
					.add(criteriaBuilder.equal(from.get("testcase"), testcase));
		}

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		if (sortedByOrder) {
			select.orderBy(criteriaBuilder.asc(from.get("ordering")));
		}

		TypedQuery<JJTeststep> result = entityManager.createQuery(select);
		return result.getResultList();

	}

	@Override
	public void saveTeststeps(Set<JJTeststep> teststeps) {
		jJTeststepRepository.save(teststeps);
	}

	@Override
	public void updateTeststeps(Set<JJTeststep> teststeps) {
		jJTeststepRepository.save(teststeps);
	}

}

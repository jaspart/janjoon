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

public class JJImportanceServiceImpl implements JJImportanceService {

	@PersistenceContext
	private EntityManager entityManager;

	public void saveJJImportance(JJImportance JJImportance_) {

		jJImportanceRepository.save(JJImportance_);
		JJImportance_ = jJImportanceRepository.findOne(JJImportance_.getId());
	}

	public JJImportance updateJJImportance(JJImportance JJImportance_) {
		jJImportanceRepository.save(JJImportance_);
		JJImportance_ = jJImportanceRepository.findOne(JJImportance_.getId());
		return JJImportance_;
	}

	@Override
	public List<JJImportance> getBugImportance() {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJImportance> criteriaQuery = criteriaBuilder
				.createQuery(JJImportance.class);

		Root<JJImportance> from = criteriaQuery.from(JJImportance.class);

		CriteriaQuery<JJImportance> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

		List<Predicate> orPredicates = new ArrayList<Predicate>();
		orPredicates.add(criteriaBuilder.equal(from.get("objet"), "JJBug"));
		orPredicates.add(criteriaBuilder.equal(from.get("objet"), "Bug"));
		Predicate orPredicate = criteriaBuilder
				.or(orPredicates.toArray(new Predicate[]{}));

		predicates.add(orPredicate);

		select.where(
				criteriaBuilder.and(predicates.toArray(new Predicate[]{})));

		TypedQuery<JJImportance> result = entityManager.createQuery(select);
		return result.getResultList();
	}
}

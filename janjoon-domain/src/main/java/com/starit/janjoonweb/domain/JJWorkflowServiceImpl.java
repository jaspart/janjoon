package com.starit.janjoonweb.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.persistence.criteria.*;

import org.apache.commons.lang3.mutable.MutableInt;
import org.springframework.beans.factory.annotation.Autowired;

public class JJWorkflowServiceImpl implements JJWorkflowService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JJStatusService jJStatusService;

	public void setjJStatusService(JJStatusService jJStatusService) {
		this.jJStatusService = jJStatusService;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void saveJJWorflow(JJWorkflow JJWorflow_) {

		jJWorkflowRepository.save(JJWorflow_);
		JJWorflow_ = jJWorkflowRepository.findOne(JJWorflow_.getId());
	}

	public JJWorkflow updateJJWorflow(JJWorkflow JJWorflow_) {
		jJWorkflowRepository.save(JJWorflow_);
		JJWorflow_ = jJWorkflowRepository.findOne(JJWorflow_.getId());
		return JJWorflow_;
	}

	public List<JJWorkflow> getObjectWorkFlows(String object, JJStatus source,
			JJStatus target, String actionName, boolean enabled) {

		if (source != null && target != null && target.equals(source)) {
			return new ArrayList<JJWorkflow>();
		} else {
			CriteriaBuilder criteriaBuilder = entityManager
					.getCriteriaBuilder();
			CriteriaQuery<JJWorkflow> criteriaQuery = criteriaBuilder
					.createQuery(JJWorkflow.class);

			Root<JJWorkflow> from = criteriaQuery.from(JJWorkflow.class);

			CriteriaQuery<JJWorkflow> select = criteriaQuery.select(from);

			List<Predicate> predicates = new ArrayList<Predicate>();

			if (enabled)
				predicates
						.add(criteriaBuilder.equal(from.get("enabled"), true));

			if (actionName != null)
				predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(from
						.<String> get("actionWorkflow")), actionName
						.toLowerCase()));

			if (object != null)
				predicates.add(criteriaBuilder.equal(
						criteriaBuilder.lower(from.<String> get("objet")),
						object.toLowerCase()));

			if (source != null)
				predicates.add(criteriaBuilder.or(criteriaBuilder.equal(
						from.get("source"), source), criteriaBuilder.equal(
						from.get("source"),
						jJStatusService.getOneStatus("Any", "*", true))));

			if (target != null)
				predicates.add(criteriaBuilder.or(criteriaBuilder.equal(
						from.get("target"), target), criteriaBuilder.equal(
						from.get("target"),
						jJStatusService.getOneStatus("Any", "*", true))));

			select.where(predicates.toArray(new Predicate[] {}));

			TypedQuery<JJWorkflow> result = entityManager.createQuery(select);

			return result.getResultList();
		}

	}

	public List<JJWorkflow> load(MutableInt size, int first, int pageSize,
			String object, String action, boolean onlyactif) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJWorkflow> criteriaQuery = criteriaBuilder
				.createQuery(JJWorkflow.class);

		Root<JJWorkflow> from = criteriaQuery.from(JJWorkflow.class);

		CriteriaQuery<JJWorkflow> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (object != null) {

			predicates.add(criteriaBuilder.equal(
					criteriaBuilder.lower(from.<String> get("objet")),
					object.toLowerCase()));

		}

		if (action != null) {

			predicates.add(criteriaBuilder.equal(
					criteriaBuilder.lower(from.<String> get("actionWorkflow")),
					action.toLowerCase()));

		}

		if (onlyactif) {

			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

		}

		select.where(predicates.toArray(new Predicate[] {}));

		TypedQuery<JJWorkflow> result = entityManager.createQuery(select);
		result.setFirstResult(first);
		result.setMaxResults(pageSize);

		CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
		cq.select(criteriaBuilder.count(cq.from(JJWorkflow.class)));
		entityManager.createQuery(cq);
		cq.where(predicates.toArray(new Predicate[] {}));
		size.setValue(entityManager.createQuery(cq).getSingleResult());

		return result.getResultList();
	}
}

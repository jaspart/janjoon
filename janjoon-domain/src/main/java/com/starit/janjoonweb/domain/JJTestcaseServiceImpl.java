package com.starit.janjoonweb.domain;

import java.util.ArrayList;
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

public class JJTestcaseServiceImpl implements JJTestcaseService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<JJTestcase> getTestcases(JJRequirement requirement,
			JJChapter chapter, boolean onlyActif, boolean sortedByOrder,
			boolean sortedByCreationdate) {

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

		if (chapter != null) {
			Path<Object> path = from.join("requirement").get("chapter");
			predicates.add(criteriaBuilder.equal(path, chapter));
		}

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		if (sortedByOrder) {
			select.orderBy(criteriaBuilder.asc(from.get("ordering")));
		}

		if (sortedByCreationdate) {
			select.orderBy(criteriaBuilder.asc(from.get("creationDate")));
		}

		TypedQuery<JJTestcase> result = entityManager.createQuery(select);
		return result.getResultList();

	}

	@Override
	public List<JJTestcase> getImportTestcases(JJProject project,
			boolean onlyActif) {
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

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		TypedQuery<JJTestcase> result = entityManager.createQuery(select);
		return result.getResultList();

	}

	@Override
	public void saveTestcases(Set<JJTestcase> testcases) {
		jJTestcaseRepository.save(testcases);
	}
	
	@Override
	public List<JJTestcase> getJJtestCases(JJRequirement requirement)
	{
		return getTestcases(requirement, null, true, true, true);
	}

	@Override
	public void updateTestcases(Set<JJTestcase> testcases) {
		jJTestcaseRepository.save(testcases);
	}
	
public void saveJJTestcase(JJTestcase JJTestcase_) {
		
        jJTestcaseRepository.save(JJTestcase_);
        JJTestcase_=jJTestcaseRepository.findOne(JJTestcase_.getId());
    }
    
    public JJTestcase updateJJTestcase(JJTestcase JJTestcase_) {
        jJTestcaseRepository.save(JJTestcase_);
        JJTestcase_=jJTestcaseRepository.findOne(JJTestcase_.getId());
        return JJTestcase_;
    }
}

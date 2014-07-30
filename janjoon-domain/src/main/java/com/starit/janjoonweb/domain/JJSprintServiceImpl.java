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

import org.springframework.beans.factory.annotation.Autowired;

public class JJSprintServiceImpl implements JJSprintService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JJRequirementService jJRequirementService;

	public void setjJRequirementService(
			JJRequirementService jJRequirementService) {
		this.jJRequirementService = jJRequirementService;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<JJSprint> getSprints(JJProject project, boolean onlyActif) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJSprint> criteriaQuery = criteriaBuilder
				.createQuery(JJSprint.class);

		Root<JJSprint> from = criteriaQuery.from(JJSprint.class);

		CriteriaQuery<JJSprint> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (project != null) {
			predicates.add(criteriaBuilder.equal(from.get("project"), project));
		}

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		select.where(predicates.toArray(new Predicate[] {}));

		TypedQuery<JJSprint> result = entityManager.createQuery(select);
		return result.getResultList();

	}
	
public void saveJJSprint(JJSprint JJSprint_) {
		
        jJSprintRepository.save(JJSprint_);
        JJSprint_=jJSprintRepository.findOne(JJSprint_.getId());
    }
    
    public JJSprint updateJJSprint(JJSprint JJSprint_) {
        jJSprintRepository.save(JJSprint_);
        JJSprint_=jJSprintRepository.findOne(JJSprint_.getId());
        return JJSprint_;
    }

}

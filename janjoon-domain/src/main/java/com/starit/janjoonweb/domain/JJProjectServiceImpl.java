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

public class JJProjectServiceImpl implements JJProjectService {
	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	// New Generic

	@Override
	public List<JJProject> getProjects(boolean onlyActif) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJProject> criteriaQuery = criteriaBuilder
				.createQuery(JJProject.class);

		Root<JJProject> from = criteriaQuery.from(JJProject.class);

		CriteriaQuery<JJProject> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		select.where(predicates.toArray(new Predicate[] {}));

		TypedQuery<JJProject> result = entityManager.createQuery(select);
		return result.getResultList();

	}
	
public void saveJJProject(JJProject JJProject_) {
		
        jJProjectRepository.save(JJProject_);
        JJProject_=jJProjectRepository.findOne(JJProject_.getId());
    }
    
    public JJProject updateJJProject(JJProject JJProject_) {
        jJProjectRepository.save(JJProject_);
        JJProject_=jJProjectRepository.findOne(JJProject_.getId());
        return JJProject_;
    }

}

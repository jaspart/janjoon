package com.starit.janjoonweb.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.mutable.MutableInt;

public class JJProjectServiceImpl implements JJProjectService {
	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	// New Generic

	public List<JJProject> load(JJCompany company,MutableInt size, int first, int pageSize) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJProject> criteriaQuery = criteriaBuilder
				.createQuery(JJProject.class);

		Root<JJProject> from = criteriaQuery.from(JJProject.class);

		CriteriaQuery<JJProject> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		if(company != null)
			predicates.add(criteriaBuilder.equal(from.get("manager").get("company"), company));
		
		select.where(predicates.toArray(new Predicate[] {}));		
			

		TypedQuery<JJProject> result = entityManager.createQuery(select);
		result.setFirstResult(first);
		result.setMaxResults(pageSize);

		if(company != null)
		{
			String qu="SELECT COUNT(r) FROM  JJProject r Where r.manager.company = :c "
					+ "AND r.enabled = true ";				
			Query query =entityManager.createQuery(qu);		
			query.setParameter("c", company);			
			
			size.setValue(Math.round((long) query.getSingleResult()));
		}else
		{
			String qu="SELECT COUNT(r) FROM  JJProject r Where  "
					+ " r.enabled = true ";				
			Query query =entityManager.createQuery(qu);							
			
			size.setValue(Math.round((long) query.getSingleResult()));
		}


		return result.getResultList();

	}

	@Override
	public List<JJProject> getProjects(JJCompany company, boolean onlyActif) {
		
		if (company != null) {
			CriteriaBuilder criteriaBuilder = entityManager
					.getCriteriaBuilder();
			CriteriaQuery<JJProject> criteriaQuery = criteriaBuilder
					.createQuery(JJProject.class);

			Root<JJProject> from = criteriaQuery.from(JJProject.class);

			CriteriaQuery<JJProject> select = criteriaQuery.select(from);

			List<Predicate> predicates = new ArrayList<Predicate>();

			if (onlyActif) {
				predicates
						.add(criteriaBuilder.equal(from.get("enabled"), true));
			}
			predicates.add(criteriaBuilder.equal(
					from.join("manager").get("company"), company));
			select.where(predicates.toArray(new Predicate[] {}));

			TypedQuery<JJProject> result = entityManager.createQuery(select);
			return result.getResultList();
		} else
			return null;

	}

	public void saveJJProject(JJProject JJProject_) {

		jJProjectRepository.save(JJProject_);
		JJProject_ = jJProjectRepository.findOne(JJProject_.getId());
	}

	public JJProject updateJJProject(JJProject JJProject_) {
		jJProjectRepository.save(JJProject_);
		JJProject_ = jJProjectRepository.findOne(JJProject_.getId());
		return JJProject_;
	}

}

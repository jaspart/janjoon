package com.funder.janjoonweb.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

public class JJRequirementServiceImpl implements JJRequirementService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<JJRequirement> getAllSpecifiedJJRequirements(String name){
		
		/*Query query = entityManager.createQuery("select s from JJRequirement s " +
		        "join fetch s.category where s.category.name=:value");
		query.setParameter("value", "BUSINESS");
		@SuppressWarnings("unchecked")
		List<JJRequirement> list = query.getResultList();
		return list;*/
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJRequirement> query = cb.createQuery(JJRequirement.class);
		Root<JJRequirement> from = query.from(JJRequirement.class);
		
		Path<Object> path = from.join("category").get("name");
		 
		from.fetch("category");
		 
		CriteriaQuery<JJRequirement> select = query.select(from);
		select.where(cb.equal(path,name));
		 
		TypedQuery<JJRequirement> result = entityManager.createQuery(select);
		return result.getResultList();
		
	}
}

package com.funder.janjoonweb.domain;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class JJRightServiceImpl implements JJRightService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public List<JJRight> getObjectWriterList(String objet){
		return getObjectWriterList(objet, true);
	}
	
	public List<JJRight> getObjectWriterList(String objet, boolean w){
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJRight> criteriaQuery = criteriaBuilder.createQuery(JJRight.class);
		Root<JJRight> from = criteriaQuery.from(JJRight.class);
		CriteriaQuery<JJRight> select = criteriaQuery.select(from);

		Predicate predicate1 = criteriaBuilder.equal(from.get("w"), w);
		Predicate predicate2 = criteriaBuilder.equal(from.get("objet"), objet);
	
		select.where(criteriaBuilder.and(predicate1,predicate2));
		

		TypedQuery<JJRight> result = entityManager.createQuery(select);
		return result.getResultList();
	}	
}

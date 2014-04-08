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


public class JJMessageServiceImpl implements JJMessageService {
	
	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Override
	public List<JJMessage> getMessages(boolean onlyActif)
	{

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJMessage> criteriaQuery = criteriaBuilder
				.createQuery(JJMessage.class);

		Root<JJMessage> from = criteriaQuery.from(JJMessage.class);

		CriteriaQuery<JJMessage> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();
		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		select.where(predicates.toArray(new Predicate[] {}));

		TypedQuery<JJMessage> result = entityManager.createQuery(select);
		return result.getResultList();

		
	}
}

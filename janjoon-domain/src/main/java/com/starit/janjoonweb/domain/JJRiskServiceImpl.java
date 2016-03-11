package com.starit.janjoonweb.domain;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class JJRiskServiceImpl implements JJRiskService {
	
	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}

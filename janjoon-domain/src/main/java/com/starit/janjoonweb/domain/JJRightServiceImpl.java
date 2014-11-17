package com.starit.janjoonweb.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

public class JJRightServiceImpl implements JJRightService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<JJRight> getRights(JJProfile profile, boolean onlyActif) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJRight> criteriaQuery = criteriaBuilder
				.createQuery(JJRight.class);
		Root<JJRight> from = criteriaQuery.from(JJRight.class);
		CriteriaQuery<JJRight> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		if (profile != null) {
			predicates.add(criteriaBuilder.equal(from.get("profile"), profile));
		} else {
			predicates.add(criteriaBuilder.isNull(from.get("profile")));
		}

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		TypedQuery<JJRight> result = entityManager.createQuery(select);
		return result.getResultList();
	}

	@Override
	public List<String> getTablesName() {

		Metamodel model = entityManager.getMetamodel();
		model.getEntities();

		List<String> tableNames = new ArrayList<String>();
		tableNames.add("*");
		Set<EntityType<?>> allEntityTypes = model.getEntities();
		for (EntityType<?> entityType : allEntityTypes) {
			tableNames.add(entityType.getName().substring(2));
		}

		return tableNames;
	}
	
	public void saveJJRight(JJRight JJRight_) {
		
        jJRightRepository.save(JJRight_);
        JJRight_=jJRightRepository.findOne(JJRight_.getId());
    }
    
    public JJRight updateJJRight(JJRight JJRight_) {
        jJRightRepository.save(JJRight_);
        JJRight_=jJRightRepository.findOne(JJRight_.getId());
        return JJRight_;
    }

}

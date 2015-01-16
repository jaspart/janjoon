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

import org.apache.commons.lang3.mutable.MutableInt;

public class JJCategoryServiceImpl implements JJCategoryService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public List<JJCategory> load(MutableInt size,int first, int pageSize)
	{
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJCategory> criteriaQuery = criteriaBuilder
				.createQuery(JJCategory.class);

		Root<JJCategory> from = criteriaQuery.from(JJCategory.class);

		CriteriaQuery<JJCategory> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();
		
		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));	

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));
		TypedQuery<JJCategory> result = entityManager.createQuery(select);	
		result.setFirstResult(first);
		result.setMaxResults(pageSize);
		
		CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
		cq.select(criteriaBuilder.count(cq.from(JJCategory.class)));		
		cq.where(predicates.toArray(new Predicate[] {}));
		size.setValue(entityManager.createQuery(cq).getSingleResult());
		
		return result.getResultList();
	
	}

	@Override
	public JJCategory getCategory(String name, boolean onlyActif) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJCategory> criteriaQuery = criteriaBuilder
				.createQuery(JJCategory.class);

		Root<JJCategory> from = criteriaQuery.from(JJCategory.class);

		CriteriaQuery<JJCategory> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(criteriaBuilder.equal(criteriaBuilder.upper(from.<String>get("name")), name.toUpperCase()));

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		TypedQuery<JJCategory> result = entityManager.createQuery(select);
		if (result.getResultList().size() == 0)
			return null;
		else
			return result.getSingleResult();
	}

	@Override
	public List<JJCategory> getCategories(String name, boolean withName,
			boolean onlyActif, boolean sortedByStage) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJCategory> criteriaQuery = criteriaBuilder
				.createQuery(JJCategory.class);

		Root<JJCategory> from = criteriaQuery.from(JJCategory.class);

		CriteriaQuery<JJCategory> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (withName) {
			predicates.add(criteriaBuilder.equal(from.get("name"), name));
		}

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		if (sortedByStage) {
			select.orderBy(criteriaBuilder.asc(from.get("stage")));
		}

		TypedQuery<JJCategory> result = entityManager.createQuery(select);
		return result.getResultList();
	}
	
	public void saveJJCategory(JJCategory JJCategory_) {
        jJCategoryRepository.save(JJCategory_);
        JJCategory_=jJCategoryRepository.findOne(JJCategory_.getId());
    }
    
    public JJCategory updateJJCategory(JJCategory JJCategory_) {
        jJCategoryRepository.save(JJCategory_);
        JJCategory_=jJCategoryRepository.findOne(JJCategory_.getId());
        return JJCategory_;
    }

}

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

public class JJProductServiceImpl implements JJProductService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	// New Generic
	
	public List<JJProduct> load(MutableInt size,int first, int pageSize)
	{
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJProduct> criteriaQuery = criteriaBuilder
				.createQuery(JJProduct.class);

		Root<JJProduct> from = criteriaQuery.from(JJProduct.class);

		CriteriaQuery<JJProduct> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		
		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		

		select.where(predicates.toArray(new Predicate[] {}));

		TypedQuery<JJProduct> result = entityManager.createQuery(select);		
		result.setFirstResult(first);
		result.setMaxResults(pageSize);
		
		CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
		cq.select(criteriaBuilder.count(cq.from(JJProduct.class)));
		entityManager.createQuery(cq);
		cq.where(predicates.toArray(new Predicate[] {}));
		size.setValue(entityManager.createQuery(cq).getSingleResult());
		
		return result.getResultList();

	
	}

	@Override
	public List<JJProduct> getProducts(boolean onlyActif) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJProduct> criteriaQuery = criteriaBuilder
				.createQuery(JJProduct.class);

		Root<JJProduct> from = criteriaQuery.from(JJProduct.class);

		CriteriaQuery<JJProduct> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		select.where(predicates.toArray(new Predicate[] {}));

		TypedQuery<JJProduct> result = entityManager.createQuery(select);
		return result.getResultList();

	}

	@Override
	public JJProduct getJJProductWithName(String name) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJProduct> criteriaQuery = criteriaBuilder
				.createQuery(JJProduct.class);

		Root<JJProduct> from = criteriaQuery.from(JJProduct.class);

		CriteriaQuery<JJProduct> select = criteriaQuery.select(from);

		Predicate predicate1 = criteriaBuilder.equal(from.get("enabled"), true);
		Predicate predicate2 = criteriaBuilder.equal(from.get("name"), name);

		select.where(criteriaBuilder.and(predicate1, predicate2));

		TypedQuery<JJProduct> result = entityManager.createQuery(select);

		if (result.getResultList().size() > 0)
			return result.getResultList().get(0);
		else
			return null;

	}
	
public void saveJJProduct(JJProduct JJProduct_) {
		
        jJProductRepository.save(JJProduct_);
        JJProduct_=jJProductRepository.findOne(JJProduct_.getId());
    }
    
    public JJProduct updateJJProduct(JJProduct JJProduct_) {
        jJProductRepository.save(JJProduct_);
        JJProduct_=jJProductRepository.findOne(JJProduct_.getId());
        return JJProduct_;
    }

}

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

public class JJBuildServiceImpl implements JJBuildService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;

	}
	
	public List<JJBuild> getBuilds(JJProduct product,JJVersion version,boolean onlyActif){

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJBuild> criteriaQuery = criteriaBuilder
				.createQuery(JJBuild.class);

		Root<JJBuild> from = criteriaQuery.from(JJBuild.class);

		CriteriaQuery<JJBuild> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}
		
		if (version != null) {
			predicates.add(criteriaBuilder.equal(from.get("version"),
						version));			
		}else if(product != null)
		{
			List<Predicate> orPredicates = new ArrayList<Predicate>();
			product=entityManager.find(JJProduct.class, product.getId());
			for(JJVersion v:product.getVersions())
			{
				if(v.getEnabled())
					orPredicates.add(criteriaBuilder.equal(from.get("version"),
							v));
			}
			Predicate orPredicate = criteriaBuilder.or(orPredicates
					.toArray(new Predicate[] {}));
			predicates.add(orPredicate);
			
		}

		select.where(predicates.toArray(new Predicate[] {}));

		TypedQuery<JJBuild> result = entityManager.createQuery(select);
		return result.getResultList();

	
	}

	@Override
	public List<JJBuild> getBuilds(JJVersion version, boolean withVersion,
			boolean onlyActif) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJBuild> criteriaQuery = criteriaBuilder
				.createQuery(JJBuild.class);

		Root<JJBuild> from = criteriaQuery.from(JJBuild.class);

		CriteriaQuery<JJBuild> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		if (withVersion) {
			if (version != null) {
				predicates.add(criteriaBuilder.equal(from.get("version"),
						version));
			} else {
				predicates.add(criteriaBuilder.isNull(from.get("version")));
			}
		}

		select.where(predicates.toArray(new Predicate[] {}));

		TypedQuery<JJBuild> result = entityManager.createQuery(select);
		return result.getResultList();

	}
	
	 public void saveJJBuild(JJBuild JJBuild_) {
		 
	        jJBuildRepository.save(JJBuild_);
	        JJBuild_=jJBuildRepository.findOne(JJBuild_.getId());
	    }
	    
	    public JJBuild updateJJBuild(JJBuild JJBuild_) {
	    	
	        jJBuildRepository.save(JJBuild_);
	        
	        JJBuild_=jJBuildRepository.findOne(JJBuild_.getId());
	        return JJBuild_;
	    }
}

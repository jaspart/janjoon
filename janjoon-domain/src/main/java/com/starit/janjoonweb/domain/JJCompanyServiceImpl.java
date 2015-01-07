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

public class JJCompanyServiceImpl implements JJCompanyService {
	
	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;

	}

	public void saveJJCompany(JJCompany JJCompany_) {
		jJCompanyRepository.save(JJCompany_);
		JJCompany_ = jJCompanyRepository.findOne(JJCompany_.getId());
	}
	
	public Long getMaxId()
	{
		Long r=(Long) entityManager.createQuery("select max(e.id) from JJCompany e").getSingleResult();
		return r+1;
	}

	public JJCompany updateJJCompany(JJCompany JJCompany_) {

		jJCompanyRepository.save(JJCompany_);
		JJCompany_ = jJCompanyRepository.findOne(JJCompany_.getId());		
		return JJCompany_;
	}
	
	public List<JJCompany> getActifCompanies(){
		
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJCompany> criteriaQuery = criteriaBuilder
				.createQuery(JJCompany.class);

		Root<JJCompany> from = criteriaQuery.from(JJCompany.class);

		CriteriaQuery<JJCompany> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();		
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		

		

		select.where(predicates.toArray(new Predicate[] {}));

		TypedQuery<JJCompany> result = entityManager.createQuery(select);
		return result.getResultList();

	}

}

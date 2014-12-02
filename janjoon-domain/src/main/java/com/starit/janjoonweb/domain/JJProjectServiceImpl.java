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
	public List<JJProject> getProjects(JJCompany company,JJContact contact,boolean onlyActif) {
		
		if (company != null) {
			List<JJProject> projects=new ArrayList<JJProject>();
			CriteriaBuilder criteriaBuilder = entityManager
					.getCriteriaBuilder();
			if(contact != null)
			{
				CriteriaQuery<JJPermission> criteriaPermission=criteriaBuilder
						.createQuery(JJPermission.class);
				Root<JJPermission> fromPermission = criteriaPermission.from(JJPermission.class);
				List<Predicate> predicatesPermion = new ArrayList<Predicate>();
				predicatesPermion
				.add(criteriaBuilder.equal(fromPermission.get("enabled"), true));
				
				predicatesPermion
				.add(criteriaBuilder.equal(fromPermission.get("contact"), contact));
				CriteriaQuery<JJPermission> selectPermission = criteriaPermission.select(fromPermission);
				selectPermission.where(predicatesPermion.toArray(new Predicate[] {}));

				TypedQuery<JJPermission> resultPermission = entityManager.createQuery(selectPermission);
				
				for(JJPermission permission:resultPermission.getResultList())
				{
					if(permission.getProject() != null)
					{
						projects.add(permission.getProject());
					}else
					{
						projects=new ArrayList<JJProject>();
						break;
					}
				}			
				
			}
			if(projects.isEmpty())
			{
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
				projects=result.getResultList();
			}
			
			return projects;
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

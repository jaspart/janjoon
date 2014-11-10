package com.starit.janjoonweb.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

public class JJBugServiceImpl implements JJBugService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<JJBug> getBugs(JJProject project, JJTeststep teststep,
			JJBuild build, boolean onlyActif, boolean sortedByCreationDate) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJBug> criteriaQuery = criteriaBuilder
				.createQuery(JJBug.class);

		Root<JJBug> from = criteriaQuery.from(JJBug.class);

		CriteriaQuery<JJBug> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		if (project != null) {
			predicates.add(criteriaBuilder.equal(from.get("project"), project));
		}

		if (teststep != null) {
			predicates
					.add(criteriaBuilder.equal(from.get("teststep"), teststep));
		}

		if (build != null) {
			predicates.add(criteriaBuilder.equal(from.get("build"), build));
		}
		

		select.where(predicates.toArray(new Predicate[] {}));

		if (sortedByCreationDate) {
			select.orderBy(criteriaBuilder.desc(from.get("creationDate")));
		}

		TypedQuery<JJBug> result = entityManager.createQuery(select);
		return result.getResultList();

	}

	@Override
	public List<JJBug> getImportBugs(JJProject project, JJVersion version,
			JJCategory category, JJStatus status, boolean onlyActif) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJBug> criteriaQuery = criteriaBuilder
				.createQuery(JJBug.class);
		
		Root<JJBug> from = criteriaQuery.from(JJBug.class);

		CriteriaQuery<JJBug> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (project != null) {
			predicates.add(criteriaBuilder.equal(from.get("project"), project));
		}

		if (version != null) {
			predicates.add(criteriaBuilder.equal(from.get("versioning"),
					version));
		}

		if (category != null) {
			predicates
					.add(criteriaBuilder.equal(from.get("category"), category));
		}

		if (status != null) {
			predicates.add(criteriaBuilder.equal(from.get("status"), status));
		}

		//predicates.add(criteriaBuilder.isNotNull(from.get("requirement")));

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		select.where(predicates.toArray(new Predicate[] {}));

		TypedQuery<JJBug> result = entityManager.createQuery(select);
		return result.getResultList();

	}
	
	public List<JJBug> load(int first, int pageSize,List<SortMeta> multiSortMeta, Map<String, String> filters,JJProject project)
	{
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJBug> criteriaQuery = criteriaBuilder
				.createQuery(JJBug.class);

		Root<JJBug> from = criteriaQuery.from(JJBug.class);

		CriteriaQuery<JJBug> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();		
		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));	

		if (project != null) {
			predicates.add(criteriaBuilder.equal(from.get("project"), project));
		}	
		

		if(filters != null)
		{
			 Iterator<Entry<String, String>> it = filters.entrySet().iterator();
			 while(it.hasNext())
			 {
				 @SuppressWarnings("rawtypes")
				Map.Entry pairs = (Map.Entry)it.next();
				 if(pairs.getKey().toString().contains("globalFilter"))
				 {
					predicates.add(criteriaBuilder.like(from.<String>get("name"), "%"+pairs.getValue()+"%"));					
				 }
					 
			
				 else if(pairs.getKey().toString().contains("importance"))
					{
						Join<JJBug, JJImportance> owner = from.join("importance");					
						predicates.add(criteriaBuilder.equal(owner.<String>get("name"), pairs.getValue().toString()));					

						
					}else if(pairs.getKey().toString().contains("criticity"))
					{
						Join<JJBug, JJCriticity> owner = from.join("criticity");					
						predicates.add(criteriaBuilder.equal(owner.<String>get("name"), pairs.getValue().toString()));
					}else if(pairs.getKey().toString().contains("status"))
					{
						Join<JJBug, JJStatus> owner = from.join("status");					
						predicates.add(criteriaBuilder.equal(owner.<String>get("name"), pairs.getValue().toString()));
					}else
						
				 predicates.add(criteriaBuilder.like(from.<String>get("name"),"%"+pairs.getValue()+"%"));
				 
			 }
		}

		if (project != null) {
			predicates.add(criteriaBuilder.equal(from.get("project"), project));
		}

		select.where(predicates.toArray(new Predicate[] {}));

		if(multiSortMeta != null)
		{
			for(SortMeta sortMeta:multiSortMeta)
			{
				String sortField=sortMeta.getSortField();
				SortOrder sortOrder=sortMeta.getSortOrder();
				if(!(sortField.contains("importance") || sortField.contains("criticity") || sortField.contains("status")))
				{
					if(sortOrder.equals(SortOrder.DESCENDING))
						select.orderBy(criteriaBuilder.desc(from.get(sortField)));
					else if(sortOrder.equals(SortOrder.ASCENDING))
					{
						select.orderBy(criteriaBuilder.asc(from.get(sortField)));
					}
				}else
				{				
					if(sortField.contains("importance"))
					{					
						Join<JJBug, JJImportance> owner = from.join("importance");
						
						if(sortOrder.equals(SortOrder.DESCENDING))
							select.orderBy(criteriaBuilder.desc(owner.get("name")));
						else if(sortOrder.equals(SortOrder.ASCENDING))
						{
							select.orderBy(criteriaBuilder.asc(owner.get("name")));
						}
					}else if(sortField.contains("criticity"))
					{
						Join<JJBug, JJCriticity> owner = from.join("criticity");
						
						if(sortOrder.equals(SortOrder.DESCENDING))
							select.orderBy(criteriaBuilder.desc(owner.get("name")));
						else if(sortOrder.equals(SortOrder.ASCENDING))
						{
							select.orderBy(criteriaBuilder.asc(owner.get("name")));
						}
						
					}else if(sortField.contains("status"))
					{
						Join<JJBug, JJStatus> owner = from.join("status");
					
						if(sortOrder.equals(SortOrder.DESCENDING))
							select.orderBy(criteriaBuilder.desc(owner.get("name")));
						else if(sortOrder.equals(SortOrder.ASCENDING))
						{
							select.orderBy(criteriaBuilder.asc(owner.get("name")));
						}
					}
				}
				
			}
			
				
		}		
		

		TypedQuery<JJBug> result = entityManager.createQuery(select);
		result.setFirstResult(first);
		result.setMaxResults(pageSize);
		return result.getResultList();
		
	}

	@Override
	public List<JJBug> getBugs(JJProject project) {
		return getBugs(project, null, null, true, false);
	}
	
	public void saveJJBug(JJBug JJBug_) {
        jJBugRepository.save(JJBug_);
        JJBug_= jJBugRepository.findOne(JJBug_.getId());
    }
    
    public JJBug updateJJBug(JJBug JJBug_) {
        jJBugRepository.save(JJBug_);
        JJBug_= jJBugRepository.findOne(JJBug_.getId());
        return JJBug_;
    }

}
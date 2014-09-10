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

public class JJRequirementServiceImpl implements JJRequirementService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	// Generic Request

	@Override
	public List<JJRequirement> getRequirements(JJCategory category,
			JJProject project, JJProduct product, JJVersion version,
			JJStatus status, JJChapter chapter, boolean withChapter,
			boolean onlyActif, boolean orderByCreationdate) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJRequirement> criteriaQuery = criteriaBuilder
				.createQuery(JJRequirement.class);

		Root<JJRequirement> from = criteriaQuery.from(JJRequirement.class);

		CriteriaQuery<JJRequirement> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		if (category != null) {
			predicates
					.add(criteriaBuilder.equal(from.get("category"), category));
		}

		if (project != null) {
			predicates.add(criteriaBuilder.equal(from.get("project"), project));
		}

		if (product != null) {
			predicates.add(criteriaBuilder.equal(from.get("product"), product));
		}

		if (version != null) {
			predicates.add(criteriaBuilder.equal(from.get("versioning"),
					version));
		}

		if (status != null) {
			predicates.add(criteriaBuilder.equal(from.get("status"), status));
		}

		if (withChapter) {
			if (chapter != null) {
				predicates.add(criteriaBuilder.equal(from.get("chapter"),
						chapter));
			} else {
				predicates.add(criteriaBuilder.isNull(from.get("chapter")));
			}

		}

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		if (orderByCreationdate) {
			select.orderBy(criteriaBuilder.desc(from.get("creationDate")));
		}

		TypedQuery<JJRequirement> result = entityManager.createQuery(select);

		return result.getResultList();
	}

	@Override
	public List<JJRequirement> getRequirementChildrenWithChapterSortedByOrder(
			JJChapter chapter, boolean onlyActif) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJRequirement> criteriaQuery = criteriaBuilder
				.createQuery(JJRequirement.class);

		Root<JJRequirement> from = criteriaQuery.from(JJRequirement.class);

		CriteriaQuery<JJRequirement> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(criteriaBuilder.equal(from.get("chapter"), chapter));

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));
		select.orderBy(criteriaBuilder.asc(from.get("ordering")));

		TypedQuery<JJRequirement> result = entityManager.createQuery(select);
		return result.getResultList();
	}

	@Override
	public List<JJRequirement> getRequirements(JJProject project,
			JJProduct product, JJVersion version) {
		return getRequirements(null, project, product, version, null, null,
				false, true, true);
	}

	@Override
	public List<JJRequirement> getRequirements(JJStatus status) {

		return getRequirements(null, null, null, null, status, null, false,
				true, false);
	}

	@Override
	public Long getReqCountByStaus(JJProject project, JJProduct product,
			JJVersion version, JJStatus status, boolean onlyActif) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> select = criteriaBuilder.createQuery(Long.class);
		Root<JJRequirement> from = select.from(JJRequirement.class);
		select.select(criteriaBuilder.count(from));
		List<Predicate> predicates = new ArrayList<Predicate>();

		if (project != null) {
			predicates.add(criteriaBuilder.equal(from.get("project"), project));
		}

		if (product != null) {
			predicates.add(criteriaBuilder.equal(from.get("product"), product));
		}

		if (version != null) {
			predicates.add(criteriaBuilder.equal(from.get("versioning"),
					version));
		}

		if (status != null) {
			predicates.add(criteriaBuilder.equal(from.get("status"), status));
		}

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}
		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));
		return entityManager.createQuery(select).getSingleResult();

	}

	@Override
	public List<JJRequirement> getMineRequirements(JJContact creator,
			JJProduct product, JJProject project, JJCategory category,JJVersion version,
			boolean onlyActif, boolean orderByCreationdate) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJRequirement> criteriaQuery = criteriaBuilder
				.createQuery(JJRequirement.class);

		Root<JJRequirement> from = criteriaQuery.from(JJRequirement.class);

		CriteriaQuery<JJRequirement> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		if (category != null) {
			predicates
					.add(criteriaBuilder.equal(from.get("category"), category));
		}

		if (project != null) {
			predicates.add(criteriaBuilder.equal(from.get("project"), project));
		}

		if (product != null) {
			predicates.add(criteriaBuilder.equal(from.get("product"), product));
		}

		if (version != null) {
			predicates.add(criteriaBuilder.equal(from.get("versioning"),
					version));			
		}		
		
		if(creator !=null)
		{
			Predicate condition1 = criteriaBuilder.equal(from.get("createdBy"), creator);
		    Predicate condition2 = criteriaBuilder.equal(from.get("updatedBy"), creator);
			predicates.add(criteriaBuilder.or(condition1,condition2));			
		}

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		if (orderByCreationdate) {
			select.orderBy(criteriaBuilder.desc(from.get("creationDate")));
		}

		TypedQuery<JJRequirement> result = entityManager.createQuery(select);

		System.out.println("RESULT LIST SIZE"+result.getResultList().size());
		return result.getResultList();

	}

	@Override
	public void refreshRequirement(JJRequirement requirement) {
		entityManager.refresh(requirement);
	}

	public void saveJJRequirement(JJRequirement JJRequirement_) {

		jJRequirementRepository.save(JJRequirement_);
		JJRequirement_ = jJRequirementRepository
				.findOne(JJRequirement_.getId());
	}

	public JJRequirement updateJJRequirement(JJRequirement JJRequirement_) {
		jJRequirementRepository.save(JJRequirement_);
		JJRequirement_ = jJRequirementRepository
				.findOne(JJRequirement_.getId());
		return JJRequirement_;
	}

}

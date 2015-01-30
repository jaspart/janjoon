package com.starit.janjoonweb.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.CollectionAttribute;

import org.hibernate.Hibernate;

public class JJRequirementServiceImpl implements JJRequirementService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	// Generic Request

	public JJRequirement getRequirementByName(JJCategory category,
			JJProject project, JJProduct product, String name, JJCompany company) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJRequirement> criteriaQuery = criteriaBuilder
				.createQuery(JJRequirement.class);

		Root<JJRequirement> from = criteriaQuery.from(JJRequirement.class);

		CriteriaQuery<JJRequirement> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

		if (category != null) {
			predicates
					.add(criteriaBuilder.equal(from.get("category"), category));
		}

		if (project != null) {
			predicates.add(criteriaBuilder.equal(from.get("project"), project));
		} else {
			predicates.add(criteriaBuilder.equal(
					from.join("project").join("manager").get("company"),
					company));
		}

		if (product != null) {
			predicates.add(criteriaBuilder.equal(from.get("product"), product));
		}

		if (name != null) {
			predicates.add(criteriaBuilder.equal(
					criteriaBuilder.upper(from.<String> get("name")),
					name.toUpperCase()));
		}

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		TypedQuery<JJRequirement> result = entityManager.createQuery(select);

		if (result.getResultList() != null)
			return result.getResultList().get(0);
		else
			return null;

	}

	public boolean haveTestcase(JJRequirement requirement) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJTestcase> criteriaQuery = criteriaBuilder
				.createQuery(JJTestcase.class);

		Root<JJTestcase> from = criteriaQuery.from(JJTestcase.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(criteriaBuilder.equal(from.get("requirement"),
				requirement));
		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
		cq.select(criteriaBuilder.count(cq.from(JJTestcase.class)));
		entityManager.createQuery(cq);
		cq.where(predicates.toArray(new Predicate[] {}));
		boolean have = entityManager.createQuery(cq).getSingleResult() > 0;
		return have;
	}

	public List<JJRequirement> getRequirementsWithOutChapter(JJCompany company,
			JJCategory category, JJProject project, JJProduct product,
			JJVersion version, JJStatus status, boolean onlyActif,
			boolean orderByCreationdate) {

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
		} else {
			predicates.add(criteriaBuilder.equal(
					from.join("project").join("manager").get("company"),
					company));
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

		predicates.add(criteriaBuilder.isNull(from.get("chapter")));
		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		if (orderByCreationdate) {
			select.orderBy(criteriaBuilder.desc(from.get("creationDate")));
		}

		TypedQuery<JJRequirement> result = entityManager.createQuery(select);

		return result.getResultList();
	}

	@Override
	public List<JJRequirement> getRequirements(JJCompany company,
			JJCategory category, JJProject project, JJProduct product,
			JJVersion version, JJStatus status, JJChapter chapter,
			boolean withChapter, boolean onlyActif, boolean orderByCreationdate) {

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
		} else {
			predicates.add(criteriaBuilder.equal(
					from.join("project").join("manager").get("company"),
					company));
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
			JJCompany company, JJChapter chapter, boolean onlyActif) {
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
	public List<JJRequirement> getRequirements(JJCompany company,
			JJProject project, JJProduct product, JJVersion version) {
		return getRequirements(company, null, project, product, version, null,
				null, false, true, true);
	}

	@Override
	public List<JJRequirement> getRequirements(JJCompany company,
			JJStatus status) {

		return getRequirements(company, null, null, null, null, status, null,
				false, true, false);
	}

	@Override
	public Long getReqCountByStaus(JJCompany company, JJProject project,
			JJProduct product, JJVersion version, JJStatus status,
			boolean onlyActif) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> select = criteriaBuilder.createQuery(Long.class);
		Root<JJRequirement> from = select.from(JJRequirement.class);
		select.select(criteriaBuilder.count(from));
		List<Predicate> predicates = new ArrayList<Predicate>();

		if (project != null) {
			predicates.add(criteriaBuilder.equal(from.get("project"), project));
		} else {
			predicates.add(criteriaBuilder.equal(
					from.join("project").join("manager").get("company"),
					company));
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
	public List<JJRequirement> getMineRequirements(JJCompany company,
			JJContact creator, JJProduct product, JJProject project,
			JJCategory category, JJVersion version, boolean onlyActif,
			boolean orderByCreationdate) {
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
		} else {
			predicates.add(criteriaBuilder.equal(
					from.join("project").join("manager").get("company"),
					company));
		}

		if (product != null) {
			predicates.add(criteriaBuilder.equal(from.get("product"), product));
		}

		if (version != null) {
			predicates.add(criteriaBuilder.equal(from.get("versioning"),
					version));
		}

		if (creator != null) {
			Predicate condition1 = criteriaBuilder.equal(from.get("createdBy"),
					creator);
			Predicate condition2 = criteriaBuilder.equal(from.get("updatedBy"),
					creator);
			predicates.add(criteriaBuilder.or(condition1, condition2));
		}

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		if (orderByCreationdate) {
			select.orderBy(criteriaBuilder.desc(from.get("creationDate")));
		}

		TypedQuery<JJRequirement> result = entityManager.createQuery(select);
		return result.getResultList();

	}

	public List<JJRequirement> getNonCouvredRequirements(JJCompany company,
			Map<JJProject, JJProduct> map) {
		if (map == null) {
			String qu = "SELECT r FROM  JJRequirement r Where r.project.manager.company = :c AND r.enabled = true AND r.category != null and r.requirementLinkDown IS empty and r.requirementLinkUp IS empty";
			Query query = entityManager.createQuery(qu, JJRequirement.class);
			query.setParameter("c", company);

			List<JJRequirement> list = ((List<JJRequirement>) query
					.getResultList());
			return list;
		} else {
			CriteriaBuilder criteriaBuilder = entityManager
					.getCriteriaBuilder();
			CriteriaQuery<JJRequirement> criteriaQuery = criteriaBuilder
					.createQuery(JJRequirement.class);

			Root<JJRequirement> from = criteriaQuery.from(JJRequirement.class);

			CriteriaQuery<JJRequirement> select = criteriaQuery.select(from);

			List<Predicate> predicates = new ArrayList<Predicate>();

			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
			predicates.add(criteriaBuilder.equal(
					from.join("project").join("manager").get("company"),
					company));

			predicates.add(criteriaBuilder.isEmpty(from
					.<List<JJRequirement>> get("requirementLinkDown")));
			predicates.add(criteriaBuilder.isEmpty(from
					.<List<JJRequirement>> get("requirementLinkUp")));

			if (!map.isEmpty()) {
				List<Predicate> orPredicates = new ArrayList<Predicate>();

				for (Map.Entry<JJProject, JJProduct> entry : map.entrySet()) {

					if (entry.getValue() != null) {
						List<Predicate> andPredicates = new ArrayList<Predicate>();

						andPredicates.add(criteriaBuilder.equal(
								from.get("project"), entry.getKey()));

						andPredicates.add(criteriaBuilder.equal(
								from.get("product"), entry.getValue()));

						orPredicates.add(criteriaBuilder.and(andPredicates
								.toArray(new Predicate[] {})));
					}else
						orPredicates.add(criteriaBuilder.equal(
								from.get("project"), entry.getKey()));

				}
				predicates.add(criteriaBuilder.or(orPredicates
						.toArray(new Predicate[] {})));

				select.where(criteriaBuilder.and(predicates
						.toArray(new Predicate[] {})));

				TypedQuery<JJRequirement> result = entityManager
						.createQuery(select);
				return result.getResultList();
			} else {
				return null;
			}

		}

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

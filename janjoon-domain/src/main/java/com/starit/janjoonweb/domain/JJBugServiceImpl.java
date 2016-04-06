package com.starit.janjoonweb.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import com.starit.janjoonweb.domain.reference.StrFunction;

public class JJBugServiceImpl implements JJBugService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public List<JJStatus> getBugsStatus(JJCompany company, JJProject project,
			JJProduct product, JJVersion version) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJStatus> criteriaQuery = criteriaBuilder
				.createQuery(JJStatus.class);
		Root<JJBug> from = criteriaQuery.from(JJBug.class);

		CriteriaQuery<JJStatus> select = criteriaQuery
				.select(from.<JJStatus> get("status"));

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (project != null) {
			predicates.add(criteriaBuilder.equal(from.get("project"), project));
		}

		if (version != null) {
			predicates.add(
					criteriaBuilder.equal(from.get("versioning"), version));
		} else if (product != null) {
			predicates.add(criteriaBuilder
					.equal(from.join("versioning").get("product"), product));
		} else if (company != null) {
			predicates.add(criteriaBuilder.equal(from.join("versioning")
					.join("product").join("manager").get("company"), company));
		}

		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

		select.orderBy(criteriaBuilder.desc(from.get("creationDate")));
		select.where(predicates.toArray(new Predicate[]{}));
		
		TypedQuery<JJStatus> result = entityManager.createQuery(select);
		return new ArrayList<JJStatus>(
				new HashSet<JJStatus>(result.getResultList()));

	}

	public List<JJContact> getBugsContacts(JJCompany company, JJProject project,
			JJProduct product, JJVersion version) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJContact> criteriaQuery = criteriaBuilder
				.createQuery(JJContact.class);
		Root<JJBug> from = criteriaQuery.from(JJBug.class);

		CriteriaQuery<JJContact> select = criteriaQuery
				.select(from.<JJContact> get("createdBy"));

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (project != null) {
			predicates.add(criteriaBuilder.equal(from.get("project"), project));
		}

		if (version != null) {
			predicates.add(
					criteriaBuilder.equal(from.get("versioning"), version));
		} else if (product != null) {
			predicates.add(criteriaBuilder
					.equal(from.join("versioning").get("product"), product));
		} else if (company != null) {
			predicates.add(criteriaBuilder.equal(from.join("versioning")
					.join("product").join("manager").get("company"), company));
		}

		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

		select.orderBy(criteriaBuilder.desc(from.get("creationDate")));
		select.where(predicates.toArray(new Predicate[]{}));

		TypedQuery<JJContact> result = entityManager.createQuery(select);
		return new ArrayList<JJContact>(
				new HashSet<JJContact>(result.getResultList()));

	}

	public Long getBugsCountByStaus(JJCompany company, JJContact createdBy,
			JJProject project, JJProduct product, JJVersion version,
			JJStatus status, boolean onlyActif) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> select = criteriaBuilder.createQuery(Long.class);
		Root<JJBug> from = select.from(JJBug.class);
		select.select(criteriaBuilder.count(from));
		List<Predicate> predicates = new ArrayList<Predicate>();

		if (status != null) {
			predicates.add(criteriaBuilder.equal(from.get("status"), status));
		}

		if (createdBy != null) {
			predicates.add(
					criteriaBuilder.equal(from.get("createdBy"), createdBy));
		}

		if (project != null) {
			predicates.add(criteriaBuilder.equal(from.get("project"), project));
		}

		if (version != null) {
			predicates.add(
					criteriaBuilder.equal(from.get("versioning"), version));
		} else if (product != null) {
			predicates.add(criteriaBuilder
					.equal(from.join("versioning").get("product"), product));
		} else if (company != null) {
			predicates.add(criteriaBuilder.equal(from.join("versioning")
					.join("product").join("manager").get("company"), company));
		}

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}
		select.where(
				criteriaBuilder.and(predicates.toArray(new Predicate[]{})));
		return entityManager.createQuery(select).getSingleResult();

	}

	@Override
	public List<JJBug> getBugs(JJCompany company, JJProject project,
			JJProduct product, JJTeststep teststep, JJBuild build,
			boolean onlyActif, boolean sortedByCreationDate) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJBug> criteriaQuery = criteriaBuilder
				.createQuery(JJBug.class);

		Root<JJBug> from = criteriaQuery.from(JJBug.class);

		CriteriaQuery<JJBug> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (project != null) {
			predicates.add(criteriaBuilder.equal(from.get("project"), project));
		}

		if (teststep != null) {
			predicates
					.add(criteriaBuilder.equal(from.get("teststep"), teststep));
		}

		if (build != null) {
			predicates.add(criteriaBuilder.equal(from.get("build"), build));
		} else if (product != null) {
			predicates.add(criteriaBuilder
					.equal(from.join("versioning").get("product"), product));
		} else if (company != null) {
			predicates.add(criteriaBuilder.equal(from.join("versioning")
					.join("product").join("manager").get("company"), company));
		}

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		select.where(predicates.toArray(new Predicate[]{}));

		if (sortedByCreationDate) {
			select.orderBy(criteriaBuilder.desc(from.get("creationDate")));
		}

		TypedQuery<JJBug> result = entityManager.createQuery(select);
		return result.getResultList();

	}

	@Override
	public List<JJBug> getImportBugs(JJCompany company, JJProject project,
			JJProduct product, JJVersion version, JJCategory category,
			JJStatus status,boolean withoutTask,boolean onlyActif) {
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJBug> criteriaQuery = criteriaBuilder
				.createQuery(JJBug.class);

		Root<JJBug> from = criteriaQuery.from(JJBug.class);

		CriteriaQuery<JJBug> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (category != null) {
			predicates
					.add(criteriaBuilder.equal(from.get("category"), category));
		}

		if (project != null) {
			predicates.add(criteriaBuilder.equal(from.get("project"), project));
		}

		if (version != null) {
			predicates.add(
					criteriaBuilder.equal(from.get("versioning"), version));
		} else if (product != null) {
			predicates.add(criteriaBuilder
					.equal(from.join("versioning").get("product"), product));
		} else if (company != null) {
			predicates.add(criteriaBuilder.equal(from.join("versioning")
					.join("product").join("manager").get("company"), company));
		}

		if (status != null) {
			predicates.add(criteriaBuilder.equal(from.get("status"), status));
		}		

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}
		
		if(withoutTask)
		{
			Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
			Root<JJTask> fromTask = subquery.from(JJTask.class);
			List<Predicate> predicatesTask = new ArrayList<Predicate>();
			subquery.select(fromTask.get("bug").<Long> get("id"));
			predicatesTask.add(criteriaBuilder.isNotNull(fromTask.get("bug")));
			predicatesTask.add(criteriaBuilder.equal(fromTask.get("enabled"), true));
	
			subquery.where(criteriaBuilder.and(predicatesTask.toArray(new Predicate[]{})));
			predicates.add(criteriaBuilder.in(from.get("id")).value(subquery).not());
		
		}

		select.orderBy(criteriaBuilder.desc(from.get("creationDate")));

		select.where(predicates.toArray(new Predicate[]{}));

		TypedQuery<JJBug> result = entityManager.createQuery(select);
		return result.getResultList();

	}

	public Long requirementBugCount(JJRequirement requirement) {

		if (requirement != null) {
			CriteriaBuilder criteriaBuilder = entityManager
					.getCriteriaBuilder();
			CriteriaQuery<Long> select = criteriaBuilder
					.createQuery(Long.class);
			Root<JJBug> from = select.from(JJBug.class);
			select.select(criteriaBuilder.count(from));
			List<Predicate> predicates = new ArrayList<Predicate>();

			predicates.add(criteriaBuilder.equal(from.get("requirement"),
					requirement));

			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

			select.where(
					criteriaBuilder.and(predicates.toArray(new Predicate[]{})));
			return entityManager.createQuery(select).getSingleResult();
		} else
			return 0L;

	}

	public List<JJBug> load(JJContact contact, JJCompany company,
			MutableInt size, int first, int pageSize,
			List<SortMeta> multiSortMeta, Map<String, Object> filters,
			JJProject project, JJProduct product, JJVersion version) {
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
			predicates.add(
					criteriaBuilder.equal(from.get("versioning"), version));
		} else if (product != null) {
			predicates.add(criteriaBuilder
					.equal(from.join("versioning").get("product"), product));
		} else if (company != null) {
			predicates.add(criteriaBuilder.equal(from.join("versioning")
					.join("product").join("manager").get("company"), company));
		}

		if (contact != null) {
			Predicate condition1 = criteriaBuilder.equal(from.get("createdBy"),
					contact);
			Predicate condition2 = criteriaBuilder.equal(from.get("updatedBy"),
					contact);
			predicates.add(criteriaBuilder.or(condition1, condition2));
		}

		if (filters != null) {
			Iterator<Entry<String, Object>> it = filters.entrySet().iterator();
			while (it.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry pairs = (Map.Entry) it.next();
				if (pairs.getKey().toString().contains("globalFilter")) {
					predicates
							.add(criteriaBuilder.or(
									criteriaBuilder.like(
											criteriaBuilder.upper(
													from.<String> get("name")),
											"%" + pairs.getValue() + "%"),
									criteriaBuilder.like(
											new StrFunction<Long>(
													criteriaBuilder,
													from.<Long> get("id")),
											"%" + pairs.getValue() + "%")));
				}

				else if (pairs.getKey().toString().contains("importance")) {
					Join<JJBug, JJImportance> owner = from.join("importance");
					predicates.add(
							criteriaBuilder.equal(owner.<String> get("name"),
									pairs.getValue().toString()));

				} else if (pairs.getKey().toString().contains("criticity")) {
					Join<JJBug, JJCriticity> owner = from.join("criticity");
					predicates.add(
							criteriaBuilder.equal(owner.<String> get("name"),
									pairs.getValue().toString()));
				} else if (pairs.getKey().toString().contains("status")) {
					Join<JJBug, JJStatus> owner = from.join("status");
					predicates.add(
							criteriaBuilder.equal(owner.<String> get("name"),
									pairs.getValue().toString()));
				} else

					predicates
							.add(criteriaBuilder.like(from.<String> get("name"),
									"%" + pairs.getValue() + "%"));

			}
		}

		if (project != null) {
			predicates.add(criteriaBuilder.equal(from.get("project"), project));
		}

		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

		select.where(predicates.toArray(new Predicate[]{}));

		if (multiSortMeta != null) {
			for (SortMeta sortMeta : multiSortMeta) {
				String sortField = sortMeta.getSortField();
				SortOrder sortOrder = sortMeta.getSortOrder();
				if (!(sortField.contains("importance")
						|| sortField.contains("criticity")
						|| sortField.contains("status"))) {
					if (sortOrder.equals(SortOrder.DESCENDING))
						select.orderBy(
								criteriaBuilder.desc(from.get(sortField)));
					else if (sortOrder.equals(SortOrder.ASCENDING)) {
						select.orderBy(
								criteriaBuilder.asc(from.get(sortField)));
					}
				} else {
					if (sortField.contains("importance")) {
						Join<JJBug, JJImportance> owner = from
								.join("importance");

						if (sortOrder.equals(SortOrder.DESCENDING))
							select.orderBy(
									criteriaBuilder.desc(owner.get("name")));
						else if (sortOrder.equals(SortOrder.ASCENDING)) {
							select.orderBy(
									criteriaBuilder.asc(owner.get("name")));
						}
					} else if (sortField.contains("criticity")) {
						Join<JJBug, JJCriticity> owner = from.join("criticity");

						if (sortOrder.equals(SortOrder.DESCENDING))
							select.orderBy(
									criteriaBuilder.desc(owner.get("name")));
						else if (sortOrder.equals(SortOrder.ASCENDING)) {
							select.orderBy(
									criteriaBuilder.asc(owner.get("name")));
						}

					} else if (sortField.contains("status")) {
						Join<JJBug, JJStatus> owner = from.join("status");

						if (sortOrder.equals(SortOrder.DESCENDING))
							select.orderBy(
									criteriaBuilder.desc(owner.get("name")));
						else if (sortOrder.equals(SortOrder.ASCENDING)) {
							select.orderBy(
									criteriaBuilder.asc(owner.get("name")));
						}
					}
				}

			}

		} else
			select.orderBy(criteriaBuilder.desc(from.get("creationDate")));
		TypedQuery<JJBug> result = entityManager.createQuery(select);
		size.setValue(entityManager.createQuery(select).getResultList().size());
		result.setFirstResult(first);
		result.setMaxResults(pageSize);

		if (size.getValue() == 0 && multiSortMeta != null)
			return load(contact, company, size, first, pageSize, null, filters,
					project, product, version);
		else
			return result.getResultList();

	}

	@Override
	public List<JJBug> getBugs(JJCompany company, JJProject project,
			JJProduct product, JJVersion version) {
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
			predicates.add(
					criteriaBuilder.equal(from.get("versioning"), version));
		} else if (product != null) {
			predicates.add(criteriaBuilder
					.equal(from.join("versioning").get("product"), product));
		} else if (company != null) {
			predicates.add(criteriaBuilder.equal(from.join("versioning")
					.join("product").join("manager").get("company"), company));
		}

		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

		select.orderBy(criteriaBuilder.desc(from.get("creationDate")));
		select.where(predicates.toArray(new Predicate[]{}));

		TypedQuery<JJBug> result = entityManager.createQuery(select);
		return result.getResultList();

	}

	public void saveJJBug(JJBug JJBug_) {
		if (JJBug_.getVersioning() == null && JJBug_.getBuild() != null)
			JJBug_.setVersioning(JJBug_.getBuild().getVersion());
		jJBugRepository.save(JJBug_);
		JJBug_ = jJBugRepository.findOne(JJBug_.getId());
	}

	public JJBug updateJJBug(JJBug JJBug_) {

		if (JJBug_.getVersioning() == null && JJBug_.getBuild() != null)
			JJBug_.setVersioning(JJBug_.getBuild().getVersion());
		jJBugRepository.save(JJBug_);
		JJBug_ = jJBugRepository.findOne(JJBug_.getId());
		return JJBug_;
	}

}
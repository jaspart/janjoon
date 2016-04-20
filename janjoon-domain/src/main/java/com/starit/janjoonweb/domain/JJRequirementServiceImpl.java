package com.starit.janjoonweb.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.beans.factory.annotation.Autowired;

public class JJRequirementServiceImpl implements JJRequirementService {

	private static final String jJRequirement_Specified = "Specified";
	private static final String jJRequirement_UnLinked = "UnLinked";
	private static final String jJRequirement_InProgress = "InProgress";
	private static final String jJRequirement_Finished = "Finished";
	private static final String jJRequirement_InTesting = "InTesting";

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JJTaskService jJTaskService;

	@Autowired
	private JJCategoryService jJCategoryService;

	@Autowired
	private JJTestcaseService jJTestcaseService;

	@Autowired
	private JJTestcaseexecutionService jJTestcaseexecutionService;

	@Autowired
	private JJStatusService jJStatusService;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void setjJTaskService(JJTaskService jJTaskService) {
		this.jJTaskService = jJTaskService;
	}

	public void setjJCategoryService(JJCategoryService jJCategoryService) {
		this.jJCategoryService = jJCategoryService;
	}

	public void setjJTestcaseService(JJTestcaseService jJTestcaseService) {
		this.jJTestcaseService = jJTestcaseService;
	}

	public void setjJTestcaseexecutionService(
			JJTestcaseexecutionService jJTestcaseexecutionService) {
		this.jJTestcaseexecutionService = jJTestcaseexecutionService;
	}

	public void setjJStatusService(JJStatusService jJStatusService) {
		this.jJStatusService = jJStatusService;
	}

	public boolean haveLinkDown(JJRequirement requirement) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJRequirement> criteriaQuery = criteriaBuilder
				.createQuery(JJRequirement.class);

		Root<JJRequirement> from = criteriaQuery.from(JJRequirement.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(criteriaBuilder.isMember(requirement,
				from.<Set<JJRequirement>> get("requirementLinkUp")));
		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
		cq.select(criteriaBuilder.count(cq.from(JJRequirement.class)));
		entityManager.createQuery(cq);
		cq.where(predicates.toArray(new Predicate[]{}));
		boolean have = entityManager.createQuery(cq).getSingleResult() > 0;
		return have;

	}

	public boolean haveLinkUp(JJRequirement requirement) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJRequirement> criteriaQuery = criteriaBuilder
				.createQuery(JJRequirement.class);

		Root<JJRequirement> from = criteriaQuery.from(JJRequirement.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(criteriaBuilder.isMember(requirement,
				from.<Set<JJRequirement>> get("requirementLinkDown")));
		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

		CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
		cq.select(criteriaBuilder.count(cq.from(JJRequirement.class)));
		entityManager.createQuery(cq);
		cq.where(predicates.toArray(new Predicate[]{}));
		boolean have = entityManager.createQuery(cq).getSingleResult() > 0;
		return have;

	}

	public JJRequirement getRequirementByName(JJCategory category,
			JJProject project, JJProduct product, String name,
			JJCompany company) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJRequirement> criteriaQuery = criteriaBuilder
				.createQuery(JJRequirement.class);

		Root<JJRequirement> from = criteriaQuery.from(JJRequirement.class);

		CriteriaQuery<JJRequirement> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

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

		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

		if (name != null) {
			predicates.add(criteriaBuilder.equal(
					criteriaBuilder.upper(from.<String> get("name")),
					name.toUpperCase()));
		}

		select.where(
				criteriaBuilder.and(predicates.toArray(new Predicate[]{})));

		TypedQuery<JJRequirement> result = entityManager.createQuery(select);

		if (result.getResultList() != null && !result.getResultList().isEmpty())
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

		predicates.add(
				criteriaBuilder.equal(from.get("requirement"), requirement));
		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
		cq.select(criteriaBuilder.count(cq.from(JJTestcase.class)));
		entityManager.createQuery(cq);
		cq.where(predicates.toArray(new Predicate[]{}));
		boolean have = entityManager.createQuery(cq).getSingleResult() > 0;
		return have;
	}

	public List<JJRequirement> getRequirementsWithOutChapter(JJCompany company,
			JJCategory category, Map<JJProject, JJProduct> map,
			JJVersion version, JJStatus status, boolean onlyActif,
			boolean orderByCreationdate) {

		if (map != null) {
			CriteriaBuilder criteriaBuilder = entityManager
					.getCriteriaBuilder();
			CriteriaQuery<JJRequirement> criteriaQuery = criteriaBuilder
					.createQuery(JJRequirement.class);

			Root<JJRequirement> from = criteriaQuery.from(JJRequirement.class);

			CriteriaQuery<JJRequirement> select = criteriaQuery.select(from);

			List<Predicate> predicates = new ArrayList<Predicate>();

			if (category != null) {
				predicates.add(
						criteriaBuilder.equal(from.get("category"), category));
			}

			if (status != null) {
				predicates
						.add(criteriaBuilder.equal(from.get("status"), status));
			}

			if (map != null && !map.isEmpty()) {
				List<Predicate> orPredicates = new ArrayList<Predicate>();

				for (Map.Entry<JJProject, JJProduct> entry : map.entrySet()) {

					if (entry.getKey() != null) {
						if (entry.getValue() != null) {
							List<Predicate> andPredicates = new ArrayList<Predicate>();

							andPredicates.add(criteriaBuilder.equal(
									from.get("project"), entry.getKey()));

							andPredicates.add(criteriaBuilder.equal(
									from.get("product"), entry.getValue()));

							orPredicates.add(criteriaBuilder.and(
									andPredicates.toArray(new Predicate[]{})));
						} else
							orPredicates.add(criteriaBuilder.equal(
									from.get("project"), entry.getKey()));
					} else {
						if (entry.getValue() != null) {
							orPredicates.add(criteriaBuilder.equal(
									from.get("product"), entry.getValue()));
						}
					}

				}
				if (!orPredicates.isEmpty())
					predicates.add(criteriaBuilder
							.or(orPredicates.toArray(new Predicate[]{})));
				else
					predicates.add(criteriaBuilder.equal(
							from.join("project").join("manager").get("company"),
							company));
			} else {
				predicates.add(criteriaBuilder.equal(
						from.join("project").join("manager").get("company"),
						company));
			}

			if (version != null) {
				predicates.add(
						criteriaBuilder.equal(from.get("versioning"), version));
			}

			if (onlyActif) {
				predicates
						.add(criteriaBuilder.equal(from.get("enabled"), true));
			}

			predicates.add(criteriaBuilder.isNull(from.get("chapter")));
			select.where(
					criteriaBuilder.and(predicates.toArray(new Predicate[]{})));

			if (orderByCreationdate) {
				select.orderBy(criteriaBuilder.desc(from.get("creationDate")));
			}

			TypedQuery<JJRequirement> result = entityManager
					.createQuery(select);
			return new ArrayList<JJRequirement>(
					new HashSet<JJRequirement>(result.getResultList()));
		} else
			return new ArrayList<JJRequirement>();
	}

	public List<JJRequirement> getRequirementsByFlowStep(JJCompany company,
			Map<JJProject, JJProduct> map, JJVersion version,
			JJFlowStep flowStep, boolean firstStage, boolean onlyActif) {

		if (map != null) {
			CriteriaBuilder criteriaBuilder = entityManager
					.getCriteriaBuilder();
			CriteriaQuery<JJRequirement> criteriaQuery = criteriaBuilder
					.createQuery(JJRequirement.class);

			Root<JJRequirement> from = criteriaQuery.from(JJRequirement.class);

			CriteriaQuery<JJRequirement> select = criteriaQuery.select(from);

			List<Predicate> predicates = new ArrayList<Predicate>();
			if (firstStage)
				predicates.add(criteriaBuilder
						.equal(from.get("category").get("stage"), 1));

			if (map != null && !map.isEmpty()) {
				List<Predicate> orPredicates = new ArrayList<Predicate>();

				for (Map.Entry<JJProject, JJProduct> entry : map.entrySet()) {

					if (entry.getKey() != null) {
						if (entry.getValue() != null) {
							List<Predicate> andPredicates = new ArrayList<Predicate>();

							andPredicates.add(criteriaBuilder.equal(
									from.get("project"), entry.getKey()));

							andPredicates.add(criteriaBuilder.equal(
									from.get("product"), entry.getValue()));

							orPredicates.add(criteriaBuilder.and(
									andPredicates.toArray(new Predicate[]{})));
						} else
							orPredicates.add(criteriaBuilder.equal(
									from.get("project"), entry.getKey()));
					} else {
						if (entry.getValue() != null) {
							orPredicates.add(criteriaBuilder.equal(
									from.get("product"), entry.getValue()));
						} else {
							predicates.add(criteriaBuilder
									.equal(from.join("project").join("manager")
											.get("company"), company));
						}
					}

				}
				if (!orPredicates.isEmpty())
					predicates.add(criteriaBuilder
							.or(orPredicates.toArray(new Predicate[]{})));
				else
					predicates.add(criteriaBuilder.equal(
							from.join("project").join("manager").get("company"),
							company));
			} else {
				predicates.add(criteriaBuilder.equal(
						from.join("project").join("manager").get("company"),
						company));
			}

			if (version != null) {
				predicates.add(
						criteriaBuilder.equal(from.get("versioning"), version));
			}

			if (flowStep != null) {
				if (flowStep.getLevel() != 0)
					predicates.add(criteriaBuilder.equal(from.get("flowStep"),
							flowStep));
				else {
					predicates.add(criteriaBuilder.or(
							criteriaBuilder.equal(from.get("flowStep"),
									flowStep),
							criteriaBuilder.isNull(from.get("flowStep"))));
				}
			}

			if (onlyActif) {
				predicates
						.add(criteriaBuilder.equal(from.get("enabled"), true));
			}

			select.where(
					criteriaBuilder.and(predicates.toArray(new Predicate[]{})));

			TypedQuery<JJRequirement> result = entityManager
					.createQuery(select);
			return new ArrayList<JJRequirement>(
					new HashSet<JJRequirement>(result.getResultList()));

		} else
			return new ArrayList<JJRequirement>();
	}

	@Override
	public List<JJRequirement> getRequirements(JJCompany company,
			JJCategory category, Map<JJProject, JJProduct> map,
			JJVersion version, JJStatus status, JJChapter chapter,
			boolean withoutTask, boolean withChapter, boolean onlyActif,
			boolean orderByCreationdate, boolean mine, JJContact contact) {

		if (map != null) {
			CriteriaBuilder criteriaBuilder = entityManager
					.getCriteriaBuilder();
			CriteriaQuery<JJRequirement> criteriaQuery = criteriaBuilder
					.createQuery(JJRequirement.class);

			Root<JJRequirement> from = criteriaQuery.from(JJRequirement.class);

			CriteriaQuery<JJRequirement> select = criteriaQuery.select(from);

			List<Predicate> predicates = new ArrayList<Predicate>();

			if (category != null) {
				predicates.add(
						criteriaBuilder.equal(from.get("category"), category));
			}

			if (status != null) {
				predicates
						.add(criteriaBuilder.equal(from.get("status"), status));
			}

			if (contact != null && mine) {
				Predicate condition1 = criteriaBuilder
						.equal(from.get("createdBy"), contact);
				Predicate condition2 = criteriaBuilder
						.equal(from.get("updatedBy"), contact);
				predicates.add(criteriaBuilder.or(condition1, condition2));
			}

			if (withoutTask) {
				Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
				Root<JJTask> fromTask = subquery.from(JJTask.class);
				List<Predicate> predicatesTask = new ArrayList<Predicate>();
				subquery.select(fromTask.get("requirement").<Long> get("id"));
				predicatesTask.add(
						criteriaBuilder.isNotNull(fromTask.get("requirement")));
				predicatesTask.add(
						criteriaBuilder.equal(fromTask.get("enabled"), true));

				subquery.where(criteriaBuilder
						.and(predicatesTask.toArray(new Predicate[]{})));
				predicates.add(criteriaBuilder.in(from.get("id"))
						.value(subquery).not());

			}

			if (map != null && !map.isEmpty()) {
				List<Predicate> orPredicates = new ArrayList<Predicate>();

				for (Map.Entry<JJProject, JJProduct> entry : map.entrySet()) {

					if (entry.getKey() != null) {
						if (entry.getValue() != null) {
							List<Predicate> andPredicates = new ArrayList<Predicate>();

							andPredicates.add(criteriaBuilder.equal(
									from.get("project"), entry.getKey()));

							andPredicates.add(criteriaBuilder.equal(
									from.get("product"), entry.getValue()));

							orPredicates.add(criteriaBuilder.and(
									andPredicates.toArray(new Predicate[]{})));
						} else
							orPredicates.add(criteriaBuilder.equal(
									from.get("project"), entry.getKey()));
					} else {
						if (entry.getValue() != null) {
							orPredicates.add(criteriaBuilder.equal(
									from.get("product"), entry.getValue()));
						} else {
							predicates.add(criteriaBuilder
									.equal(from.join("project").join("manager")
											.get("company"), company));
						}
					}

				}
				if (!orPredicates.isEmpty())
					predicates.add(criteriaBuilder
							.or(orPredicates.toArray(new Predicate[]{})));
				else
					predicates.add(criteriaBuilder.equal(
							from.join("project").join("manager").get("company"),
							company));
			} else {
				predicates.add(criteriaBuilder.equal(
						from.join("project").join("manager").get("company"),
						company));
			}

			if (version != null) {
				predicates.add(
						criteriaBuilder.equal(from.get("versioning"), version));
			}

			if (withChapter) {
				if (chapter != null) {
					predicates.add(criteriaBuilder.equal(from.get("chapter"),
							chapter));
				} else {
					predicates.add(criteriaBuilder.isNull(from.get("chapter")));
				}

			}

			if (onlyActif) {
				predicates
						.add(criteriaBuilder.equal(from.get("enabled"), true));
			}

			select.where(
					criteriaBuilder.and(predicates.toArray(new Predicate[]{})));

			if (orderByCreationdate) {
				select.orderBy(criteriaBuilder.desc(from.get("creationDate")));
			}

			TypedQuery<JJRequirement> result = entityManager
					.createQuery(select);

			return new ArrayList<JJRequirement>(
					new HashSet<JJRequirement>(result.getResultList()));
		} else
			return new ArrayList<JJRequirement>();
	}

	@Override
	public List<JJRequirement> getRequirementChildrenWithChapterSortedByOrder(
			JJCompany company, JJChapter chapter, JJProduct product,
			JJVersion version, boolean onlyActif) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJRequirement> criteriaQuery = criteriaBuilder
				.createQuery(JJRequirement.class);

		Root<JJRequirement> from = criteriaQuery.from(JJRequirement.class);

		CriteriaQuery<JJRequirement> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(criteriaBuilder.equal(from.get("chapter"), chapter));

		if (version != null) {
			predicates.add(
					criteriaBuilder.equal(from.get("versioning"), version));
		} else if (product != null) {
			predicates.add(criteriaBuilder.equal(from.get("product"), product));
		}

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		select.where(
				criteriaBuilder.and(predicates.toArray(new Predicate[]{})));
		select.orderBy(criteriaBuilder.asc(from.get("ordering")));

		TypedQuery<JJRequirement> result = entityManager.createQuery(select);
		return new ArrayList<JJRequirement>(
				new HashSet<JJRequirement>(result.getResultList()));
	}

	@Override
	public List<JJRequirement> getRequirements(JJCompany company,
			Map<JJProject, JJProduct> map, JJVersion version,
			JJCategory category) {
		return getRequirements(company, category, map, version, null, null,
				false, false, true, true, false, null);
	}

	@Override
	public List<JJRequirement> getRequirements(JJCompany company,
			JJStatus status) {

		return getRequirements(company, null,
				new HashMap<JJProject, JJProduct>(), null, status, null, false,
				false, true, false, false, null);
	}

	public List<JJContact> getReqContacts(JJCompany company,
			Map<JJProject, JJProduct> map, JJVersion version) {

		if (map != null) {
			CriteriaBuilder criteriaBuilder = entityManager
					.getCriteriaBuilder();

			CriteriaQuery<JJContact> select = criteriaBuilder
					.createQuery(JJContact.class);

			Root<JJRequirement> from = select.from(JJRequirement.class);
			select.select(from.<JJContact> get("createdBy"));
			List<Predicate> predicates = new ArrayList<Predicate>();

			if (map != null && !map.isEmpty()) {
				List<Predicate> orPredicates = new ArrayList<Predicate>();

				for (Map.Entry<JJProject, JJProduct> entry : map.entrySet()) {

					if (entry.getKey() != null) {
						if (entry.getValue() != null) {
							List<Predicate> andPredicates = new ArrayList<Predicate>();

							andPredicates.add(criteriaBuilder.equal(
									from.get("project"), entry.getKey()));

							andPredicates.add(criteriaBuilder.equal(
									from.get("product"), entry.getValue()));

							orPredicates.add(criteriaBuilder.and(
									andPredicates.toArray(new Predicate[]{})));
						} else
							orPredicates.add(criteriaBuilder.equal(
									from.get("project"), entry.getKey()));
					} else {
						if (entry.getValue() != null) {
							orPredicates.add(criteriaBuilder.equal(
									from.get("product"), entry.getValue()));
						} else {
							predicates.add(criteriaBuilder
									.equal(from.join("project").join("manager")
											.get("company"), company));
						}
					}

				}
				if (!orPredicates.isEmpty())
					predicates.add(criteriaBuilder
							.or(orPredicates.toArray(new Predicate[]{})));
				else
					predicates.add(criteriaBuilder.equal(
							from.join("project").join("manager").get("company"),
							company));
			} else {
				predicates.add(criteriaBuilder.equal(
						from.join("project").join("manager").get("company"),
						company));
			}

			if (version != null) {
				predicates.add(
						criteriaBuilder.equal(from.get("versioning"), version));
			}

			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

			select.where(
					criteriaBuilder.and(predicates.toArray(new Predicate[]{})));

			TypedQuery<JJContact> result = entityManager.createQuery(select);
			return new ArrayList<JJContact>(
					new HashSet<JJContact>(result.getResultList()));
		} else
			return new ArrayList<JJContact>();
	}

	@Override
	public Long getReqCount(JJCompany company, JJProject project,
			JJProduct product, JJVersion version, JJStatus status,
			JJCategory category, JJStatus state, JJContact createdBy,
			boolean onlyActif) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> select = criteriaBuilder.createQuery(Long.class);
		Root<JJRequirement> from = select.from(JJRequirement.class);
		select.select(criteriaBuilder.count(from));
		List<Predicate> predicates = new ArrayList<Predicate>();

		if (status != null) {
			predicates.add(criteriaBuilder.equal(from.get("status"), status));
		}

		if (createdBy != null) {
			predicates.add(
					criteriaBuilder.equal(from.get("createdBy"), createdBy));
		}

		if (state != null) {
			predicates.add(criteriaBuilder.equal(from.get("state"), state));
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
			predicates.add(
					criteriaBuilder.equal(from.get("versioning"), version));
		}

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}
		select.where(
				criteriaBuilder.and(predicates.toArray(new Predicate[]{})));
		return entityManager.createQuery(select).getSingleResult();

	}

	@Override
	public List<JJRequirement> getMineRequirements(JJCompany company,
			JJContact creator, Map<JJProject, JJProduct> map,
			JJCategory category, JJVersion version, boolean onlyActif,
			boolean orderByCreationdate) {

		if (map != null) {
			CriteriaBuilder criteriaBuilder = entityManager
					.getCriteriaBuilder();
			CriteriaQuery<JJRequirement> criteriaQuery = criteriaBuilder
					.createQuery(JJRequirement.class);

			Root<JJRequirement> from = criteriaQuery.from(JJRequirement.class);

			CriteriaQuery<JJRequirement> select = criteriaQuery.select(from);

			List<Predicate> predicates = new ArrayList<Predicate>();

			if (category != null) {
				predicates.add(
						criteriaBuilder.equal(from.get("category"), category));
			}

			if (creator != null) {
				Predicate condition1 = criteriaBuilder
						.equal(from.get("createdBy"), creator);
				Predicate condition2 = criteriaBuilder
						.equal(from.get("updatedBy"), creator);
				predicates.add(criteriaBuilder.or(condition1, condition2));
			}

			if (map != null && !map.isEmpty()) {
				List<Predicate> orPredicates = new ArrayList<Predicate>();

				for (Map.Entry<JJProject, JJProduct> entry : map.entrySet()) {

					if (entry.getKey() != null) {
						if (entry.getValue() != null) {
							List<Predicate> andPredicates = new ArrayList<Predicate>();

							andPredicates.add(criteriaBuilder.equal(
									from.get("project"), entry.getKey()));

							andPredicates.add(criteriaBuilder.equal(
									from.get("product"), entry.getValue()));

							orPredicates.add(criteriaBuilder.and(
									andPredicates.toArray(new Predicate[]{})));
						} else
							orPredicates.add(criteriaBuilder.equal(
									from.get("project"), entry.getKey()));
					} else {
						if (entry.getValue() != null) {
							orPredicates.add(criteriaBuilder.equal(
									from.get("product"), entry.getValue()));
						}
					}

				}
				if (!orPredicates.isEmpty())
					predicates.add(criteriaBuilder
							.or(orPredicates.toArray(new Predicate[]{})));
				else
					predicates.add(criteriaBuilder.equal(
							from.join("project").join("manager").get("company"),
							company));

			} else {
				predicates.add(criteriaBuilder.equal(
						from.join("project").join("manager").get("company"),
						company));
			}

			if (version != null) {
				predicates.add(
						criteriaBuilder.equal(from.get("versioning"), version));
			}

			if (onlyActif) {
				predicates
						.add(criteriaBuilder.equal(from.get("enabled"), true));
			}

			select.where(
					criteriaBuilder.and(predicates.toArray(new Predicate[]{})));

			if (orderByCreationdate) {
				select.orderBy(criteriaBuilder.desc(from.get("creationDate")));
			}

			TypedQuery<JJRequirement> result = entityManager
					.createQuery(select);
			return new ArrayList<JJRequirement>(
					new HashSet<JJRequirement>(result.getResultList()));

		} else
			return new ArrayList<JJRequirement>();

	}

	@SuppressWarnings("unchecked")
	public List<JJRequirement> getNonCouvredRequirements(JJCompany company,
			Map<JJProject, JJProduct> map) {
		if (map == null) {
			String qu = "SELECT r FROM  JJRequirement r Where r.project.manager.company = :c AND r.category != null AND r.enabled = true AND r.requirementLinkDown IS empty and r.requirementLinkUp IS empty";
			Query query = entityManager.createQuery(qu, JJRequirement.class);
			query.setParameter("c", company);

			return ((List<JJRequirement>) query.getResultList());
		} else {
			CriteriaBuilder criteriaBuilder = entityManager
					.getCriteriaBuilder();
			CriteriaQuery<JJRequirement> criteriaQuery = criteriaBuilder
					.createQuery(JJRequirement.class);

			Root<JJRequirement> from = criteriaQuery.from(JJRequirement.class);

			CriteriaQuery<JJRequirement> select = criteriaQuery.select(from);

			List<Predicate> predicates = new ArrayList<Predicate>();

			predicates.add(criteriaBuilder.equal(
					from.join("project").join("manager").get("company"),
					company));

			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
			predicates.add(criteriaBuilder.isEmpty(
					from.<List<JJRequirement>> get("requirementLinkDown")));
			predicates.add(criteriaBuilder.isEmpty(
					from.<List<JJRequirement>> get("requirementLinkUp")));

			if (!map.isEmpty()) {
				List<Predicate> orPredicates = new ArrayList<Predicate>();

				for (Map.Entry<JJProject, JJProduct> entry : map.entrySet()) {

					if (entry.getValue() != null) {
						List<Predicate> andPredicates = new ArrayList<Predicate>();

						andPredicates.add(criteriaBuilder
								.equal(from.get("project"), entry.getKey()));

						andPredicates.add(criteriaBuilder
								.equal(from.get("product"), entry.getValue()));

						orPredicates.add(criteriaBuilder
								.and(andPredicates.toArray(new Predicate[]{})));
					} else
						orPredicates.add(criteriaBuilder
								.equal(from.get("project"), entry.getKey()));

				}
				predicates.add(criteriaBuilder
						.or(orPredicates.toArray(new Predicate[]{})));

				select.where(criteriaBuilder
						.and(predicates.toArray(new Predicate[]{})));

				TypedQuery<JJRequirement> result = entityManager
						.createQuery(select);
				return result.getResultList();
			} else {
				return null;
			}

		}

	}

	public JJStatus getRequirementState(JJRequirement requirement,
			JJCompany company) {

		boolean UP = haveLinkUp(requirement) || jJCategoryService
				.isHighLevel(requirement.getCategory(), company);

		boolean DOWN = haveLinkDown(requirement) || jJCategoryService
				.isLowLevel(requirement.getCategory(), company);

		// requirement = findJJRequirement(requirement.getId());

		boolean TASK = true;
		boolean FINIS = true;

		TASK = jJTaskService.haveTask(requirement, true, false, true);
		if (UP && DOWN && TASK) {
			FINIS = jJTaskService.haveTask(requirement, true, true, true);
		}

		String rowStyleClass = "";

		if (UP && DOWN && TASK) {

			if (!FINIS) {
				rowStyleClass = jJRequirement_InProgress;
			} else if (FINIS) {

				List<JJTestcase> testcases = jJTestcaseService.getTestcases(
						requirement, null, null, null, true, false, false);

				boolean SUCCESS = true;
				int i = 0;

				while (SUCCESS && i < testcases.size()) {
					SUCCESS = jJTestcaseexecutionService
							.checkIfSuccess(testcases.get(i));
					i++;
				}

				if (SUCCESS) {
					rowStyleClass = jJRequirement_Finished;
				} else {
					rowStyleClass = jJRequirement_InTesting;
				}

			}

		} else if (UP && DOWN && !TASK) {
			rowStyleClass = jJRequirement_Specified;
		} else {
			if (!(UP && DOWN && TASK))
				rowStyleClass = jJRequirement_UnLinked;
		}

		if (rowStyleClass.isEmpty())
			rowStyleClass = jJRequirement_UnLinked;

		return jJStatusService.getOneStatus(rowStyleClass, "RequirementState",
				true);
	}

	public void saveJJRequirement(JJRequirement JJRequirement_) {

		jJRequirementRepository.save(JJRequirement_);
		JJRequirement_ = jJRequirementRepository
				.findOne(JJRequirement_.getId());
	}

	public JJRequirement updateJJRequirement(JJRequirement JJRequirement_) {
		jJRequirementRepository.save(JJRequirement_);
		JJRequirement_ = findJJRequirement(JJRequirement_.getId());
		return JJRequirement_;
	}

}

package com.starit.janjoonweb.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

public class JJMessageServiceImpl implements JJMessageService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public List<JJMessage> getCommMessages(Object field) {
		if (field != null) {
			CriteriaBuilder criteriaBuilder = entityManager
					.getCriteriaBuilder();
			CriteriaQuery<JJMessage> criteriaQuery = criteriaBuilder
					.createQuery(JJMessage.class);

			Root<JJMessage> from = criteriaQuery.from(JJMessage.class);
			CriteriaQuery<JJMessage> select = criteriaQuery.select(from);
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

			if (field instanceof JJBug)
				predicates.add(criteriaBuilder.equal(from.get("bug"),
						(JJBug) field));

			else if (field instanceof JJRequirement)
				predicates.add(criteriaBuilder.equal(from.get("requirement"),
						(JJRequirement) field));

			else if (field instanceof JJTestcase)
				predicates.add(criteriaBuilder.equal(from.get("testcase"),
						(JJTestcase) field));

			select.where(predicates.toArray(new Predicate[] {}));
			select.orderBy(criteriaBuilder.desc(from.get("creationDate")));

			TypedQuery<JJMessage> result = entityManager.createQuery(select);
			return result.getResultList();

		} else
			return new ArrayList<JJMessage>();

	}

	public void updateAll(JJCompany company) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaUpdate<JJMessage> update = criteriaBuilder
				.createCriteriaUpdate(JJMessage.class);
		Root<JJMessage> from = update.from(JJMessage.class);

		update.set("company", company);
		update.where(criteriaBuilder.equal(from.get("company").get("id"), 0));
		entityManager.createQuery(update).executeUpdate();
	}

	public Integer getMessagesCount(JJProject project, JJProduct product,
			JJCompany company, JJContact contact) {
		long r = 0;
		if (project != null && product != null) {
			String query = "select count(e.id) from JJMessage e Where e.enabled = true and (e.project = :proj or e.project = null) and (e.product = :prod or e.product = null) and "
					+ "((e.bug = null and e.requirement = null and e.testcase = null)";

			if (contact.getBugs() != null && !contact.getBugs().isEmpty())
				query = query + " or e.bug IN (:bugs)";

			if (contact.getTestcases() != null
					&& !contact.getTestcases().isEmpty())
				query = query + " or e.testcase IN (:testcases)";

			if (contact.getRequirements() != null
					&& !contact.getRequirements().isEmpty())
				query = query + " or e.requirement IN (:reqs)";

			query = query + ")";

			Query q = entityManager.createQuery(query);

			if (contact.getBugs() != null && !contact.getBugs().isEmpty())
				q.setParameter("bugs", contact.getBugs());

			if (contact.getTestcases() != null
					&& !contact.getTestcases().isEmpty())
				q.setParameter("testcases", contact.getTestcases());

			if (contact.getRequirements() != null
					&& !contact.getRequirements().isEmpty())
				q.setParameter("reqs", contact.getRequirements());

			r = (long) q.setParameter("proj", project)
					.setParameter("prod", product).getSingleResult();
		}

		else {
			if (project != null) {
				String query = "select count(e.id) from JJMessage e Where e.enabled = true and (e.project = :proj or e.project = null) and "
						+ "((e.bug = null and e.requirement = null and e.testcase = null)";

				if (contact.getBugs() != null && !contact.getBugs().isEmpty())
					query = query + " or e.bug IN (:bugs)";

				if (contact.getTestcases() != null
						&& !contact.getTestcases().isEmpty())
					query = query + " or e.testcase IN (:testcases)";

				if (contact.getRequirements() != null
						&& !contact.getRequirements().isEmpty())
					query = query + " or e.requirement IN (:reqs)";

				query = query + ")";

				Query q = entityManager.createQuery(query);

				if (contact.getBugs() != null && !contact.getBugs().isEmpty())
					q.setParameter("bugs", contact.getBugs());

				if (contact.getTestcases() != null
						&& !contact.getTestcases().isEmpty())
					q.setParameter("testcases", contact.getTestcases());

				if (contact.getRequirements() != null
						&& !contact.getRequirements().isEmpty())
					q.setParameter("reqs", contact.getRequirements());

				r = (long) q.setParameter("proj", project).getSingleResult();

			} else if (product != null) {
				String query = "select count(e.id) from JJMessage e Where e.enabled = true and (e.product = :prod or e.product = null) and "
						+ "((e.bug = null and e.requirement = null and e.testcase = null)";

				if (contact.getBugs() != null && !contact.getBugs().isEmpty())
					query = query + " or e.bug IN (:bugs)";

				if (contact.getTestcases() != null
						&& !contact.getTestcases().isEmpty())
					query = query + " or e.testcase IN (:testcases)";

				if (contact.getRequirements() != null
						&& !contact.getRequirements().isEmpty())
					query = query + " or e.requirement IN (:reqs)";

				query = query + ")";

				Query q = entityManager.createQuery(query);

				if (contact.getBugs() != null && !contact.getBugs().isEmpty())
					q.setParameter("bugs", contact.getBugs());

				if (contact.getTestcases() != null
						&& !contact.getTestcases().isEmpty())
					q.setParameter("testcases", contact.getTestcases());

				if (contact.getRequirements() != null
						&& !contact.getRequirements().isEmpty())
					q.setParameter("reqs", contact.getRequirements());

				r = (long) q.setParameter("prod", product).getSingleResult();
			}

			else if (company != null) {

				String query = "select count(e.id) from JJMessage e Where e.enabled = true and e.company = :comp and "
						+ "((e.bug = null and e.requirement = null and e.testcase = null)";

				if (contact.getBugs() != null && !contact.getBugs().isEmpty())
					query = query + " or e.bug IN (:bugs)";

				if (contact.getTestcases() != null
						&& !contact.getTestcases().isEmpty())
					query = query + " or e.testcase IN (:testcases)";

				if (contact.getRequirements() != null
						&& !contact.getRequirements().isEmpty())
					query = query + " or e.requirement IN (:reqs)";

				query = query + ")";

				Query q = entityManager.createQuery(query);

				if (contact.getBugs() != null && !contact.getBugs().isEmpty())
					q.setParameter("bugs", contact.getBugs());

				if (contact.getTestcases() != null
						&& !contact.getTestcases().isEmpty())
					q.setParameter("testcases", contact.getTestcases());

				if (contact.getRequirements() != null
						&& !contact.getRequirements().isEmpty())
					q.setParameter("reqs", contact.getRequirements());

				r = (long) q.setParameter("comp", company).getSingleResult();
			} else
				r = (long) entityManager
						.createQuery(
								"select count(e.id) from JJMessage e Where e.enabled = true")
						.getSingleResult();
		}

		return safeLongToInt(r);

	}

	public static Integer safeLongToInt(long l) {
		if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
			throw new IllegalArgumentException(l
					+ " cannot be cast to int without changing its value.");
		}
		return (int) l;
	}

	public List<JJMessage> getActifMessages(MutableInt size, int first,
			int pageSize, List<SortMeta> multiSortMeta,
			Map<String, Object> filters, JJProject project, JJProduct product,
			JJCompany company, JJContact contact) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJMessage> criteriaQuery = criteriaBuilder
				.createQuery(JJMessage.class);

		Root<JJMessage> from = criteriaQuery.from(JJMessage.class);
		CriteriaQuery<JJMessage> select = criteriaQuery.select(from);
		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

		if (product != null) {
			Predicate condition1 = criteriaBuilder.equal(from.get("product"),
					product);
			Predicate condition2 = criteriaBuilder.isNull(from.get("product"));
			predicates.add(criteriaBuilder.or(condition1, condition2));
		}

		if (project != null) {
			Predicate condition1 = criteriaBuilder.equal(from.get("project"),
					project);
			Predicate condition2 = criteriaBuilder.isNull(from.get("project"));
			predicates.add(criteriaBuilder.or(condition1, condition2));
		}

		if (project == null && product == null && company != null) {
			predicates.add(criteriaBuilder.equal(from.get("company"), company));
		}

		if (filters != null) {
			Iterator<Entry<String, Object>> it = filters.entrySet().iterator();
			while (it.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry pairs = (Map.Entry) it.next();
				if (pairs.getKey().toString().contains("globalFilter")) {
					predicates.add(criteriaBuilder.like(
							from.<String> get("name"), "%" + pairs.getValue()
									+ "%"));
				}

				else if (pairs.getKey().toString().contains("product")) {
					Join<JJMessage, JJProduct> owner = from.join("product");
					predicates
							.add(criteriaBuilder.equal(owner
									.<String> get("name"), pairs.getValue()
									.toString()));

				} else if (pairs.getKey().toString().contains("project")) {
					Join<JJMessage, JJProject> owner = from.join("project");
					predicates
							.add(criteriaBuilder.equal(owner
									.<String> get("name"), pairs.getValue()
									.toString()));
				} else if (pairs.getKey().toString().contains("status")) {
					Join<JJMessage, JJStatus> owner = from.join("status");
					predicates
							.add(criteriaBuilder.equal(owner
									.<String> get("name"), pairs.getValue()
									.toString()));
				} else if (pairs.getKey().toString().contains("createdBy")) {
					Join<JJMessage, JJContact> owner = from.join("createdBy");
					predicates
							.add(criteriaBuilder.equal(owner
									.<String> get("name"), pairs.getValue()
									.toString()));
				} else if (pairs.getKey().toString().contains("criticity")) {
					Join<JJMessage, JJCriticity> owner = from.join("criticity");
					predicates
							.add(criteriaBuilder.equal(owner
									.<String> get("name"), pairs.getValue()
									.toString()));
				} else
					predicates.add(criteriaBuilder.like(
							from.<String> get("name"), "%" + pairs.getValue()
									+ "%"));

			}
		}

		List<Predicate> oRPredicate = new ArrayList<Predicate>();
		if (contact != null) {
			oRPredicate.add(criteriaBuilder.and(
					criteriaBuilder.isNull(from.get("bug")),
					criteriaBuilder.isNull(from.get("testcase")),
					criteriaBuilder.isNull(from.get("requirement"))));

			for (JJTestcase test : contact.getTestcases())
				oRPredicate.add(criteriaBuilder.equal(from.get("testcase"),
						test));

			for (JJBug bug : contact.getBugs())
				oRPredicate.add(criteriaBuilder.equal(from.get("bug"), bug));

			for (JJRequirement req : contact.getRequirements())
				oRPredicate.add(criteriaBuilder.equal(from.get("requirement"),
						req));

		}

		if (multiSortMeta != null) {
			for (SortMeta sortMeta : multiSortMeta) {
				String sortField = sortMeta.getSortField();
				SortOrder sortOrder = sortMeta.getSortOrder();
				if (!(sortField.contains("createdBy")
						|| sortField.contains("project")
						|| sortField.contains("product")
						|| sortField.contains("criticity") || sortField
							.contains("status"))) {
					if (sortOrder.equals(SortOrder.DESCENDING))
						select.orderBy(criteriaBuilder.desc(from.get(sortField)));
					else if (sortOrder.equals(SortOrder.ASCENDING)) {
						select.orderBy(criteriaBuilder.asc(from.get(sortField)));
					}
				} else {
					if (sortField.contains("project")) {
						Join<JJMessage, JJProject> owner = from.join("project");

						if (sortOrder.equals(SortOrder.DESCENDING))
							select.orderBy(criteriaBuilder.desc(owner
									.get("name")));
						else if (sortOrder.equals(SortOrder.ASCENDING)) {
							select.orderBy(criteriaBuilder.asc(owner
									.get("name")));
						}
					} else if (sortField.contains("criticity")) {
						Join<JJMessage, JJCriticity> owner = from
								.join("criticity");

						if (sortOrder.equals(SortOrder.DESCENDING))
							select.orderBy(criteriaBuilder.desc(owner
									.get("name")));
						else if (sortOrder.equals(SortOrder.ASCENDING)) {
							select.orderBy(criteriaBuilder.asc(owner
									.get("name")));
						}

					} else if (sortField.contains("product")) {
						Join<JJMessage, JJProduct> owner = from.join("product");

						if (sortOrder.equals(SortOrder.DESCENDING))
							select.orderBy(criteriaBuilder.desc(owner
									.get("name")));
						else if (sortOrder.equals(SortOrder.ASCENDING)) {
							select.orderBy(criteriaBuilder.asc(owner
									.get("name")));
						}

					} else if (sortField.contains("createdBy")) {
						Join<JJMessage, JJContact> owner = from
								.join("createdBy");

						if (sortOrder.equals(SortOrder.DESCENDING))
							select.orderBy(criteriaBuilder.desc(owner
									.get("name")));
						else if (sortOrder.equals(SortOrder.ASCENDING)) {
							select.orderBy(criteriaBuilder.asc(owner
									.get("name")));
						}

					} else if (sortField.contains("status")) {
						Join<JJMessage, JJStatus> owner = from.join("status");

						if (sortOrder.equals(SortOrder.DESCENDING))
							select.orderBy(criteriaBuilder.desc(owner
									.get("name")));
						else if (sortOrder.equals(SortOrder.ASCENDING)) {
							select.orderBy(criteriaBuilder.asc(owner
									.get("name")));
						}
					}
				}

			}

		} else {
			select.orderBy(criteriaBuilder.desc(from.get("creationDate")));
		}

		select.where(
				criteriaBuilder.and(predicates.toArray(new Predicate[] {})),
				criteriaBuilder.or(oRPredicate.toArray(new Predicate[] {})));

		TypedQuery<JJMessage> result = entityManager.createQuery(select);
		size.setValue(result.getResultList().size());
		result.setFirstResult(first);
		result.setMaxResults(pageSize);

		return result.getResultList();
	}

	public List<JJMessage> getAlertMessages(JJProject project,
			JJProduct product, JJCompany company, JJContact contact) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJMessage> criteriaQuery = criteriaBuilder
				.createQuery(JJMessage.class);

		Root<JJMessage> from = criteriaQuery.from(JJMessage.class);

		CriteriaQuery<JJMessage> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		predicates.add(criteriaBuilder.equal(
				from.join("criticity").get("name"), "ALERT"));

		if (product != null) {
			Predicate condition1 = criteriaBuilder.equal(from.get("product"),
					product);
			Predicate condition2 = criteriaBuilder.isNull(from.get("product"));
			predicates.add(criteriaBuilder.or(condition1, condition2));
		}

		if (project != null) {
			Predicate condition1 = criteriaBuilder.equal(from.get("project"),
					project);
			Predicate condition2 = criteriaBuilder.isNull(from.get("project"));
			predicates.add(criteriaBuilder.or(condition1, condition2));
		}

		if (project == null && product == null && company != null) {
			predicates.add(criteriaBuilder.equal(from.get("company"), company));
		}

		List<Predicate> oRPredicate = new ArrayList<Predicate>();
		if (contact != null) {
			oRPredicate.add(criteriaBuilder.and(
					criteriaBuilder.isNull(from.get("bug")),
					criteriaBuilder.isNull(from.get("testcase")),
					criteriaBuilder.isNull(from.get("requirement"))));

			for (JJTestcase test : contact.getTestcases())
				oRPredicate.add(criteriaBuilder.equal(from.get("testcase"),
						test));

			for (JJBug bug : contact.getBugs())
				oRPredicate.add(criteriaBuilder.equal(from.get("bug"), bug));

			for (JJRequirement req : contact.getRequirements())
				oRPredicate.add(criteriaBuilder.equal(from.get("requirement"),
						req));

		}

		select.where(
				criteriaBuilder.and(predicates.toArray(new Predicate[] {})),
				criteriaBuilder.or(oRPredicate.toArray(new Predicate[] {})));

		TypedQuery<JJMessage> result = entityManager.createQuery(select);
		return result.getResultList();

	}

	@Override
	public List<JJMessage> getMessages(boolean onlyActif, JJCompany company) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJMessage> criteriaQuery = criteriaBuilder
				.createQuery(JJMessage.class);

		Root<JJMessage> from = criteriaQuery.from(JJMessage.class);

		CriteriaQuery<JJMessage> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();
		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		if (company != null) {
			predicates.add(criteriaBuilder.equal(from.get("company"), company));
		}

		select.where(predicates.toArray(new Predicate[] {}));

		TypedQuery<JJMessage> result = entityManager.createQuery(select);
		return result.getResultList();

	}

	public List<JJMessage> getActifMessages(JJProject project,
			JJProduct product, JJCompany company) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJMessage> criteriaQuery = criteriaBuilder
				.createQuery(JJMessage.class);

		Root<JJMessage> from = criteriaQuery.from(JJMessage.class);
		CriteriaQuery<JJMessage> select = criteriaQuery.select(from);
		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

		if (product != null) {
			Predicate condition1 = criteriaBuilder.equal(from.get("product"),
					product);
			Predicate condition2 = criteriaBuilder.isNull(from.get("product"));
			predicates.add(criteriaBuilder.or(condition1, condition2));
		}

		if (project != null) {
			Predicate condition1 = criteriaBuilder.equal(from.get("project"),
					project);
			Predicate condition2 = criteriaBuilder.isNull(from.get("project"));
			predicates.add(criteriaBuilder.or(condition1, condition2));
		}

		if (project == null && product == null && company != null) {
			predicates.add(criteriaBuilder.equal(from.get("company"), company));
		}

		select.where(predicates.toArray(new Predicate[] {}));
		TypedQuery<JJMessage> result = entityManager.createQuery(select);

		return result.getResultList();
	}

	public void addTwoList(List<JJMessage> list1, List<JJMessage> list2) {

		for (JJMessage message : list2) {
			if (!contain(list1, message)) {
				list1.add(message);
			}
		}
	}

	public boolean contain(List<JJMessage> list, JJMessage message) {
		boolean contain = false;
		int i = 0;
		while (i < list.size() && !contain) {
			contain = (list.get(i).equals(message));
			i++;
		}
		return contain;
	}

	public void saveJJMessage(JJMessage JJMessage_) {

		jJMessageRepository.save(JJMessage_);
		JJMessage_ = jJMessageRepository.findOne(JJMessage_.getId());
	}

	public JJMessage updateJJMessage(JJMessage JJMessage_) {
		jJMessageRepository.save(JJMessage_);
		JJMessage_ = jJMessageRepository.findOne(JJMessage_.getId());
		return JJMessage_;
	}

}

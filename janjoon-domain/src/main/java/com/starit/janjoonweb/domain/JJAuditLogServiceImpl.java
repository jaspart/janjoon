package com.starit.janjoonweb.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

public class JJAuditLogServiceImpl implements JJAuditLogService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public List<JJAuditLog> getAuditLogByObject(String objet,
			JJContact contact, JJCompany company, String keyName, int first,
			int pageSize, MutableInt size, List<SortMeta> multiSortMeta,
			Map<String, Object> filters) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJAuditLog> criteriaQuery = criteriaBuilder
				.createQuery(JJAuditLog.class);

		Root<JJAuditLog> from = criteriaQuery.from(JJAuditLog.class);

		CriteriaQuery<JJAuditLog> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (contact != null) {
			predicates.add(criteriaBuilder.equal(from.get("contact"), contact));
		} else if (company != null) {
			predicates.add(criteriaBuilder.isNotNull(from.get("contact")));
			predicates.add(criteriaBuilder.equal(
					from.get("contact").get("company"), company));
		}

		if (objet != null) {
			predicates.add(criteriaBuilder.equal(
					criteriaBuilder.lower(from.<String> get("objet")),
					objet.toLowerCase()));
		}

		if (keyName != null) {
			predicates.add(criteriaBuilder.equal(
					criteriaBuilder.lower(from.<String> get("keyName")),
					keyName.toLowerCase()));
			if (keyName.equalsIgnoreCase("login_date")
					|| keyName.equalsIgnoreCase("logout_date")
					&& (company == null && contact == null)) {
				predicates.add(criteriaBuilder.isNotNull(from.get("contact")));
			}
		}

		if (filters != null) {
			Iterator<Entry<String, Object>> it = filters.entrySet().iterator();
			while (it.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry pairs = (Map.Entry) it.next();
				if (pairs.getKey().toString().contains("globalFilter")) {
					predicates
							.add(criteriaBuilder.like(from.get("contact")
									.<String> get("name"),
									"%" + pairs.getValue() + "%"));
				} else if (pairs.getKey().toString().contains("company")) {

					predicates.add(criteriaBuilder.equal(from.get("contact")
							.get("company").<String> get("name"), pairs
							.getValue().toString()));
				}

			}
		}

		select.where(predicates.toArray(new Predicate[] {}));

		if (multiSortMeta != null) {
			for (SortMeta sortMeta : multiSortMeta) {
				String sortField = sortMeta.getSortField();
				SortOrder sortOrder = sortMeta.getSortOrder();
				if (sortField.contains("loginDate")) {
					if (sortOrder.equals(SortOrder.DESCENDING))
						select.orderBy(criteriaBuilder.desc(from
								.get("auditLogDate")));
					else if (sortOrder.equals(SortOrder.ASCENDING)) {
						select.orderBy(criteriaBuilder.asc(from
								.get("auditLogDate")));
					}
				} else if (sortField.contains("company")) {
					Join<JJContact, JJCompany> owner = from.join("contact")
							.join("company");

					if (sortOrder.equals(SortOrder.DESCENDING))
						select.orderBy(criteriaBuilder.desc(owner.get("name")));
					else if (sortOrder.equals(SortOrder.ASCENDING)) {
						select.orderBy(criteriaBuilder.asc(owner.get("name")));
					}
				} else if (sortField.contains("contact")) {
					Join<JJContact, JJCompany> owner = from.join("contact");

					if (sortOrder.equals(SortOrder.DESCENDING))
						select.orderBy(criteriaBuilder.desc(owner.get("name")));
					else if (sortOrder.equals(SortOrder.ASCENDING)) {
						select.orderBy(criteriaBuilder.asc(owner.get("name")));
					}
				}
			}
		} else
			select.orderBy(criteriaBuilder.desc(from.get("auditLogDate")));

		TypedQuery<JJAuditLog> result = entityManager.createQuery(select);
		result.setFirstResult(first);
		result.setMaxResults(pageSize);

		CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
		cq.select(criteriaBuilder.count(cq.from(JJAuditLog.class)));
		entityManager.createQuery(cq);
		cq.where(predicates.toArray(new Predicate[] {}));
		size.setValue(entityManager.createQuery(cq).getSingleResult());

		return result.getResultList();

	}

	public JJAuditLog getLogoutAuditLog(JJContact contact, Date loginDate) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJAuditLog> criteriaQuery = criteriaBuilder
				.createQuery(JJAuditLog.class);

		Root<JJAuditLog> from = criteriaQuery.from(JJAuditLog.class);

		CriteriaQuery<JJAuditLog> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (contact != null) {
			predicates.add(criteriaBuilder.equal(from.get("contact"), contact));
		}

		if (loginDate != null) {
			predicates.add(criteriaBuilder.greaterThanOrEqualTo(
					from.<Date> get("auditLogDate"), loginDate));
		}

		predicates.add(criteriaBuilder.equal(
				criteriaBuilder.lower(from.<String> get("keyName")),
				"logout_date".toLowerCase()));

		predicates.add(criteriaBuilder.equal(
				criteriaBuilder.lower(from.<String> get("objet")),
				"jjcontact".toLowerCase()));

		select.orderBy(criteriaBuilder.asc(from.get("auditLogDate")));

		select.where(predicates.toArray(new Predicate[] {}));

		TypedQuery<JJAuditLog> result = entityManager.createQuery(select);
		result.setFirstResult(0);
		result.setMaxResults(1);

		if (result.getResultList() != null && !result.getResultList().isEmpty())
			return result.getResultList().get(0);
		else
			return null;

	}

	public void saveJJAuditLog(JJAuditLog JJAuditLog_) {
		jJAuditLogRepository.save(JJAuditLog_);
		JJAuditLog_ = jJAuditLogRepository.findOne(JJAuditLog_.getId());
	}

	public JJAuditLog updateJJAuditLog(JJAuditLog JJAuditLog_) {
		jJAuditLogRepository.save(JJAuditLog_);
		JJAuditLog_ = jJAuditLogRepository.findOne(JJAuditLog_.getId());
		return JJAuditLog_;
	}
}

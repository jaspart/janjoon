package com.starit.janjoonweb.domain;

import java.util.ArrayList;
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
import org.springframework.beans.factory.annotation.Autowired;

import com.starit.janjoonweb.domain.reference.StrFunction;

public class JJContactServiceImpl implements JJContactService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JJPermissionService jJPermissionService;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public JJPermissionService getjJPermissionService() {
		return jJPermissionService;
	}

	public void setjJPermissionService(
			JJPermissionService jJPermissionService) {
		this.jJPermissionService = jJPermissionService;
	}

	@Override
	public JJContact getContactByEmail(String email, boolean onlyActif) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJContact> criteriaQuery = criteriaBuilder
				.createQuery(JJContact.class);

		Root<JJContact> from = criteriaQuery.from(JJContact.class);

		CriteriaQuery<JJContact> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();
		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		if (email != null) {
			predicates.add(criteriaBuilder.equal(from.get("email"),
					email.toLowerCase()));
		}

		select.where(
				criteriaBuilder.and(predicates.toArray(new Predicate[]{})));

		TypedQuery<JJContact> result = entityManager.createQuery(select);
		if (result.getResultList().isEmpty())
			return null;
		else
			return result.getResultList().get(0);

	}

	@Override
	public JJContact getContactById(Long id) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJContact> criteriaQuery = criteriaBuilder
				.createQuery(JJContact.class);

		Root<JJContact> from = criteriaQuery.from(JJContact.class);

		CriteriaQuery<JJContact> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(criteriaBuilder.equal(from.get("id"), id));

		select.where(
				criteriaBuilder.and(predicates.toArray(new Predicate[]{})));

		TypedQuery<JJContact> result = entityManager.createQuery(select);
		if (result.getResultList().isEmpty())
			return null;
		else
			return result.getResultList().get(0);

	}

	@Override
	public List<JJContact> getContacts(boolean onlyActif, JJCompany company,
			JJContact contact) {

		if (company != null || jJPermissionService.isSuperAdmin(contact)) {
			CriteriaBuilder criteriaBuilder = entityManager
					.getCriteriaBuilder();
			CriteriaQuery<JJContact> criteriaQuery = criteriaBuilder
					.createQuery(JJContact.class);

			Root<JJContact> from = criteriaQuery.from(JJContact.class);

			CriteriaQuery<JJContact> select = criteriaQuery.select(from);

			List<Predicate> predicates = new ArrayList<Predicate>();

			if (company != null)
				predicates.add(
						criteriaBuilder.equal(from.get("company"), company));

			predicates
					.add(criteriaBuilder.equal(from.get("enabled"), onlyActif));

			select.where(
					criteriaBuilder.and(predicates.toArray(new Predicate[]{})));

			TypedQuery<JJContact> result = entityManager.createQuery(select);

			return result.getResultList();
		} else
			return new ArrayList<JJContact>();

	}

	@Override
	public boolean saveJJContactTransaction(JJContact contact) {

		if (getContactByEmail(contact.getEmail(), false) == null) {

			saveJJContact(contact);
			return true;
		} else {
			return false;
		}

	}

	public List<JJContact> load(JJCompany company, MutableInt size, int first,
			int pageSize, List<SortMeta> multiSortMeta,
			Map<String, Object> filters) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJContact> criteriaQuery = criteriaBuilder
				.createQuery(JJContact.class);

		Root<JJContact> from = criteriaQuery.from(JJContact.class);

		CriteriaQuery<JJContact> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (company != null)
			predicates.add(criteriaBuilder.equal(from.get("company"), company));

		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

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
											criteriaBuilder.upper(from
													.<String> get("firstname")),
									"%" + pairs.getValue() + "%"),
							criteriaBuilder.like(
									criteriaBuilder
											.upper(from.<String> get("email")),
									"%" + pairs.getValue() + "%"),
							criteriaBuilder.like(
									new StrFunction<Long>(criteriaBuilder,
											from.<Long> get("id")),
									"%" + pairs.getValue() + "%")));
				} else if (pairs.getKey().toString().contains("company")) {

					predicates.add(criteriaBuilder.equal(
							from.get("company").<String> get("name"),
							pairs.getValue().toString()));

				} else
					predicates
							.add(criteriaBuilder.like(from.<String> get("name"),
									"%" + pairs.getValue() + "%"));

			}
		}

		select.where(
				criteriaBuilder.and(predicates.toArray(new Predicate[]{})));

		if (multiSortMeta != null) {
			for (SortMeta sortMeta : multiSortMeta) {
				String sortField = sortMeta.getSortField();
				SortOrder sortOrder = sortMeta.getSortOrder();
				if (!sortField.contains("company")) {
					if (sortOrder.equals(SortOrder.DESCENDING))
						select.orderBy(
								criteriaBuilder.desc(from.get(sortField)));
					else if (sortOrder.equals(SortOrder.ASCENDING)) {
						select.orderBy(
								criteriaBuilder.asc(from.get(sortField)));
					}
				} else if (sortField.contains("company")) {
					Join<JJContact, JJCompany> owner = from.join("company");

					if (sortOrder.equals(SortOrder.DESCENDING))
						select.orderBy(criteriaBuilder.desc(owner.get("name")));
					else if (sortOrder.equals(SortOrder.ASCENDING)) {
						select.orderBy(criteriaBuilder.asc(owner.get("name")));
					}
				}
			}
		} else
			select.orderBy(criteriaBuilder.desc(from.get("creationDate")));

		TypedQuery<JJContact> result = entityManager.createQuery(select);
		result.setFirstResult(first);
		result.setMaxResults(pageSize);

		CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
		cq.select(criteriaBuilder.count(cq.from(JJContact.class)));
		entityManager.createQuery(cq);
		cq.where(predicates.toArray(new Predicate[]{}));
		size.setValue(entityManager.createQuery(cq).getSingleResult());

		return result.getResultList();

	}

	@Override
	public boolean updateJJContactTransaction(JJContact contact) {

		JJContact ctc = getContactByEmail(contact.getEmail(), false);

		if (ctc == null) {

			updateJJContact(contact);
			return true;

		} else {

			if (ctc.getId().equals(contact.getId())) {
				updateJJContact(contact);
				return true;
			} else
				return false;

		}
	}

	public void saveJJContact(JJContact JJContact_) {

		JJContact_.setEmail(JJContact_.getEmail().toLowerCase());
		jJContactRepository.save(JJContact_);
		JJContact_ = jJContactRepository.findOne(JJContact_.getId());
	}

	public JJContact updateJJContact(JJContact JJContact_) {

		jJContactRepository.save(JJContact_);
		JJContact_ = jJContactRepository.findOne(JJContact_.getId());
		return JJContact_;
	}

}

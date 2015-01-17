package com.starit.janjoonweb.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

import org.apache.commons.lang3.mutable.MutableInt;

public class JJContactServiceImpl implements JJContactService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
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

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

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

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		TypedQuery<JJContact> result = entityManager.createQuery(select);
		if (result.getResultList().isEmpty())
			return null;
		else
			return result.getResultList().get(0);

	}

	@Override
	public List<JJContact> getContacts(boolean onlyActif) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJContact> criteriaQuery = criteriaBuilder
				.createQuery(JJContact.class);

		Root<JJContact> from = criteriaQuery.from(JJContact.class);

		CriteriaQuery<JJContact> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();
		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		TypedQuery<JJContact> result = entityManager.createQuery(select);

		return result.getResultList();
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
	
	public List<JJContact> load(JJCompany company,MutableInt size,int first, int pageSize) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJContact> criteriaQuery = criteriaBuilder
				.createQuery(JJContact.class);

		Root<JJContact> from = criteriaQuery.from(JJContact.class);

		CriteriaQuery<JJContact> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();
		
		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		
		if(company != null)
		predicates.add(criteriaBuilder.equal(from.get("company"), company));
		

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		TypedQuery<JJContact> result = entityManager.createQuery(select);
		result.setFirstResult(first);
		result.setMaxResults(pageSize);
		
		CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
		cq.select(criteriaBuilder.count(cq.from(JJContact.class)));
		entityManager.createQuery(cq);
		cq.where(predicates.toArray(new Predicate[] {}));
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

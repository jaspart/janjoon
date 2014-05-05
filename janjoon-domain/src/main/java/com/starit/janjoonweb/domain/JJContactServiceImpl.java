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

public class JJContactServiceImpl implements JJContactService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<JJContact> getContacts(String email, boolean onlyActif) {

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
		return result.getResultList();

	}

	@Override
	public boolean saveJJContactTransaction(JJContact contact) {

		if (getContacts(contact.getEmail(), false).isEmpty()) {
			saveJJContact(contact);
			return true;
		} else {
			return false;
		}

	}

	@Override
	public boolean updateJJContactTransaction(JJContact contact) {

		List<JJContact> ctcs = getContacts(contact.getEmail(), false);

		if (ctcs.isEmpty()) {

			updateJJContact(contact);
			return true;

		}
		else if (ctcs.size() == 1)
		 {
				if(ctcs.get(0).getId().equals(contact.getId()))
				{
					updateJJContact(contact);
					return true;
				}else
					return false;


		} else {
			return false;
		}
	}

}

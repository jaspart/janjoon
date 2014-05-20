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

public class JJMessageServiceImpl implements JJMessageService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<JJMessage> getMessages(boolean onlyActif) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJMessage> criteriaQuery = criteriaBuilder
				.createQuery(JJMessage.class);

		Root<JJMessage> from = criteriaQuery.from(JJMessage.class);

		CriteriaQuery<JJMessage> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();
		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		select.where(predicates.toArray(new Predicate[] {}));

		TypedQuery<JJMessage> result = entityManager.createQuery(select);
		return result.getResultList();

	}

	public List<JJMessage> getActifMessages(JJProject project, JJProduct product) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJMessage> criteriaQuery = criteriaBuilder
				.createQuery(JJMessage.class);

		Root<JJMessage> from = criteriaQuery.from(JJMessage.class);

		List<JJMessage> resultset = null;

		if (product == null && project == null) {

			return getMessages(true);
		} else {
			if (product != null) {
				CriteriaQuery<JJMessage> select = criteriaQuery.select(from);
				List<Predicate> predicates = new ArrayList<Predicate>();
				predicates
						.add(criteriaBuilder.equal(from.get("enabled"), true));
				predicates.add(criteriaBuilder.equal(from.get("product"),
						product));
				select.where(predicates.toArray(new Predicate[] {}));
				TypedQuery<JJMessage> result = entityManager
						.createQuery(select);
				resultset = result.getResultList();
			}

			if (project != null) {
				CriteriaQuery<JJMessage> select = criteriaQuery.select(from);
				List<Predicate> predicates = new ArrayList<Predicate>();
				predicates
						.add(criteriaBuilder.equal(from.get("enabled"), true));
				predicates.add(criteriaBuilder.equal(from.get("project"),
						project));
				select.where(predicates.toArray(new Predicate[] {}));
				TypedQuery<JJMessage> result = entityManager
						.createQuery(select);

				if (resultset != null)
					addTwoList(resultset, result.getResultList());
				else
					resultset = result.getResultList();
			}

			return resultset;
		}

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

}

package com.starit.janjoonweb.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

public class JJPermissionServiceImpl implements JJPermissionService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Set<JJContact> getManagers(String objet) {

		Set<JJContact> contacts = new HashSet<JJContact>();

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJPermission> criteriaQuery = criteriaBuilder
				.createQuery(JJPermission.class);
		Root<JJPermission> from = criteriaQuery.from(JJPermission.class);

		Path<Object> path = from.join("profile");
		from.fetch("profile");
		CriteriaQuery<JJPermission> select = criteriaQuery.select(from);

		Subquery<JJRight> subquery = criteriaQuery.subquery(JJRight.class);
		Root fromRight = subquery.from(JJRight.class);
		subquery.select(fromRight.get("profile"));

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(criteriaBuilder.equal(fromRight.get("w"), true));
		predicates.add(criteriaBuilder.equal(fromRight.get("objet"), objet));

		subquery.where(criteriaBuilder.and(predicates
				.toArray(new Predicate[] {})));

		predicates = new ArrayList<Predicate>();
		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		predicates.add(criteriaBuilder.in(path).value(subquery));

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		TypedQuery<JJPermission> result = entityManager.createQuery(select);

		List<JJPermission> permissions = result.getResultList();
		for (JJPermission permission : permissions) {
			if (permission.getContact().getEnabled()) {
				contacts.add(permission.getContact());
			}
		}

		return contacts;

	}

	@Override
	public List<JJPermission> getPermissions(JJContact contact,
			boolean onlyContact, JJProfile profile, JJProject project,
			JJProduct product) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJPermission> criteriaQuery = criteriaBuilder
				.createQuery(JJPermission.class);

		Root<JJPermission> from = criteriaQuery.from(JJPermission.class);

		CriteriaQuery<JJPermission> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

		predicates.add(criteriaBuilder.equal(from.get("contact"), contact));

		if (!onlyContact) {

			predicates.add(criteriaBuilder.equal(from.get("profile"), profile));

			if (project != null) {
				predicates.add(criteriaBuilder.equal(from.get("project"),
						project));
			} else {
				predicates.add(criteriaBuilder.isNull(from.get("project")));
			}

			if (product != null) {
				predicates.add(criteriaBuilder.equal(from.get("product"),
						product));
			} else {
				predicates.add(criteriaBuilder.isNull(from.get("product")));
			}

		}

		select.where(predicates.toArray(new Predicate[] {}));

		TypedQuery<JJPermission> result = entityManager.createQuery(select);

		return result.getResultList();

	}
}

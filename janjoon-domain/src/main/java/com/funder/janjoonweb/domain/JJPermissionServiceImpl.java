package com.funder.janjoonweb.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class JJPermissionServiceImpl implements JJPermissionService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<JJPermission> getManagerPermissions(JJProfile profile) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJPermission> criteriaQuery = criteriaBuilder
				.createQuery(JJPermission.class);

		Root<JJPermission> from = criteriaQuery.from(JJPermission.class);

		CriteriaQuery<JJPermission> select = criteriaQuery.select(from);

		Predicate predicate = criteriaBuilder.equal(from.get("profile"),
				profile);

		select.where(predicate);

		TypedQuery<JJPermission> result = entityManager.createQuery(select);
		return result.getResultList();
	}

	@Override
	public List<JJPermission> getJJPermissionsWithProfileAndContact(
			JJProfile profile, JJContact contact) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJPermission> criteriaQuery = criteriaBuilder
				.createQuery(JJPermission.class);

		Root<JJPermission> from = criteriaQuery.from(JJPermission.class);

		CriteriaQuery<JJPermission> select = criteriaQuery.select(from);

		Predicate predicate1 = criteriaBuilder.equal(from.get("profile"),
				profile);
		Predicate predicate2 = criteriaBuilder.equal(from.get("contact"),
				profile);

		select.where(criteriaBuilder.and(predicate1, predicate2));

		TypedQuery<JJPermission> result = entityManager.createQuery(select);
		return result.getResultList();
	}

	@Override
	public List<JJPermission> getJJPermissionsWithContact(JJContact contact) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJPermission> criteriaQuery = criteriaBuilder
				.createQuery(JJPermission.class);

		Root<JJPermission> from = criteriaQuery.from(JJPermission.class);

		CriteriaQuery<JJPermission> select = criteriaQuery.select(from);

		Predicate predicate = criteriaBuilder.equal(from.get("contact"),
				contact);

		select.where(predicate);

		TypedQuery<JJPermission> result = entityManager.createQuery(select);
		return result.getResultList();
	}
}

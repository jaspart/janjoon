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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean isAuthorized(JJContact contact, JJProject project,
			JJProduct product, String objet, JJCategory category, Boolean r,
			Boolean w, Boolean x) {

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
		List<Predicate> orPredicates = new ArrayList<Predicate>();

		predicates.add(criteriaBuilder.equal(fromRight.get("enabled"), true));

		if (objet != null) {
			orPredicates.add(criteriaBuilder.equal(fromRight.get("objet"),
					objet));
			if(!objet.contains("*"))
				orPredicates.add(criteriaBuilder.equal(fromRight.get("objet"),
						"JJ"+objet));
			orPredicates
					.add(criteriaBuilder.equal(fromRight.get("objet"), "*"));

		} else
			orPredicates
					.add(criteriaBuilder.equal(fromRight.get("objet"), "*"));

		Predicate orPredicate = criteriaBuilder.or(orPredicates
				.toArray(new Predicate[] {}));
		predicates.add(orPredicate);

		orPredicates = new ArrayList<Predicate>();

		if (category != null)

		{
			orPredicates.add(criteriaBuilder.equal(fromRight.get("category"),
					category));
			orPredicates.add(criteriaBuilder.isNull(fromRight.get("category")));
		} else
			orPredicates.add(criteriaBuilder.isNull(fromRight.get("category")));

		orPredicate = criteriaBuilder.or(orPredicates
				.toArray(new Predicate[] {}));
		predicates.add(orPredicate);

		if (r != null) {
			predicates.add(criteriaBuilder.equal(fromRight.get("r"), r));

		}

		if (w != null) {
			predicates.add(criteriaBuilder.equal(fromRight.get("w"), w));

		}

		if (x != null) {
			predicates.add(criteriaBuilder.equal(fromRight.get("x"), x));

		}

		subquery.where(criteriaBuilder.and(predicates
				.toArray(new Predicate[] {})));

		predicates = new ArrayList<Predicate>();
		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

		if (contact != null) {
			predicates.add(criteriaBuilder.equal(from.get("contact"), contact));
		}
		orPredicates = new ArrayList<Predicate>();

		if (project != null) {
			orPredicates
					.add(criteriaBuilder.equal(from.get("project"), project));
			orPredicates.add(criteriaBuilder.isNull(from.get("project")));
		} else
			orPredicates.add(criteriaBuilder.isNull(from.get("project")));

		orPredicate = criteriaBuilder.or(orPredicates
				.toArray(new Predicate[] {}));
		predicates.add(criteriaBuilder.and(orPredicate));

		orPredicates = new ArrayList<Predicate>();

		if (product != null) {
			orPredicates
					.add(criteriaBuilder.equal(from.get("product"), product));
			orPredicates.add(criteriaBuilder.isNull(from.get("product")));
		} else
			orPredicates.add(criteriaBuilder.isNull(from.get("product")));

		orPredicate = criteriaBuilder.or(orPredicates
				.toArray(new Predicate[] {}));

		predicates.add(criteriaBuilder.and(orPredicate));

		predicates.add(criteriaBuilder.equal(path.get("enabled"), true));

		predicates.add(criteriaBuilder.in(path).value(subquery));

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		TypedQuery<JJPermission> result = entityManager.createQuery(select);

		List<JJPermission> permissions = result.getResultList();

		if (permissions.isEmpty()) {
			return false;
		} else {
			return true;
		}

	}

	@Override
	public boolean isAuthorized(JJContact contact, String objet,
			JJCategory category, Boolean r, Boolean w, Boolean x) {
		return isAuthorized(contact, null, null, objet, category, r, w, x);
	}

	@Override
	public boolean isAuthorized(JJContact contact, String objet, Boolean r,
			Boolean w, Boolean x) {
		return isAuthorized(contact, null, null, objet, null, r, w, x);
	}

	@Override
	public boolean isAuthorized(JJContact contact, JJProject project,
			JJProduct product, String objet, JJCategory category) {
		return isAuthorized(contact, project, product, objet, category, null,
				null, null);
	}

	@Override
	public boolean isAuthorized(JJContact contact, JJProject project,
			JJProduct product, String objet) {
		return isAuthorized(contact, project, product, objet, null, null, null,
				null);
	}

	@Override
	public boolean isAuthorized(JJContact contact, JJProject project,
			JJProduct product) {
		return isAuthorized(contact, project, product, null, null, null, null,
				null);
	}

	@Override
	public boolean isAuthorized(JJContact contact, JJProject project) {
		return isAuthorized(contact, project, null, null, null, null, null,
				null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Set<JJContact> areAuthorized(JJProject project, JJProduct product,
			String objet, JJCategory category, Boolean r, Boolean w, Boolean x) {

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
		List<Predicate> orPredicates = new ArrayList<Predicate>();

		predicates.add(criteriaBuilder.equal(fromRight.get("enabled"), true));

		if (objet != null) {
			orPredicates.add(criteriaBuilder.equal(fromRight.get("objet"),
					objet));
			if(!objet.contains("*"))
				orPredicates.add(criteriaBuilder.equal(fromRight.get("objet"),
						"JJ"+objet));
			orPredicates
					.add(criteriaBuilder.equal(fromRight.get("objet"), "*"));
		} else
			orPredicates
					.add(criteriaBuilder.equal(fromRight.get("objet"), "*"));

		Predicate orPredicate = criteriaBuilder.or(orPredicates
				.toArray(new Predicate[] {}));
		predicates.add(orPredicate);

		orPredicates = new ArrayList<Predicate>();

		if (category != null)

			orPredicates.add(criteriaBuilder.equal(fromRight.get("category"),
					category));
		
			orPredicates.add(criteriaBuilder.isNull(fromRight.get("category")));

		orPredicate = criteriaBuilder.or(orPredicates
				.toArray(new Predicate[] {}));
		predicates.add(orPredicate);

		if (r != null) {
			predicates.add(criteriaBuilder.equal(fromRight.get("r"), r));

		}

		if (w != null) {
			predicates.add(criteriaBuilder.equal(fromRight.get("w"), w));

		}

		if (x != null) {
			predicates.add(criteriaBuilder.equal(fromRight.get("x"), x));

		}

		subquery.where(criteriaBuilder.and(predicates
				.toArray(new Predicate[] {})));

		predicates = new ArrayList<Predicate>();
		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

		orPredicates = new ArrayList<Predicate>();

		if (project != null)
			orPredicates
					.add(criteriaBuilder.equal(from.get("project"), project));
		
			orPredicates.add(criteriaBuilder.isNull(from.get("project")));

		orPredicate = criteriaBuilder.or(orPredicates
				.toArray(new Predicate[] {}));

		predicates.add(criteriaBuilder.and(orPredicate));

		orPredicates = new ArrayList<Predicate>();
		if (product != null)
			orPredicates
					.add(criteriaBuilder.equal(from.get("product"), product));
		
			orPredicates.add(criteriaBuilder.isNull(from.get("product")));

		orPredicate = criteriaBuilder.or(orPredicates
				.toArray(new Predicate[] {}));

		predicates.add(criteriaBuilder.and(orPredicate));

		predicates.add(criteriaBuilder.equal(path.get("enabled"), true));

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
	public Set<JJContact> getManagers(String objet) {
		return areAuthorized(null, null, objet, null, null, true, null);
	}

	@Override
	public Set<JJContact> areAuthorized(JJProject project, JJProduct product,String objet) {
		return areAuthorized(project, product, objet, null, null, null, null);
	}

	public void saveJJPermission(JJPermission JJPermission_) {

		jJPermissionRepository.save(JJPermission_);
		JJPermission_ = jJPermissionRepository.findOne(JJPermission_.getId());
	}

	public JJPermission updateJJPermission(JJPermission JJPermission_) {
		jJPermissionRepository.save(JJPermission_);
		JJPermission_ = jJPermissionRepository.findOne(JJPermission_.getId());
		return JJPermission_;
	}

}

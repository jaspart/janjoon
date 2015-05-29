package com.starit.janjoonweb.domain;

import java.util.*;
import java.util.Map.Entry;

import javax.persistence.*;
import javax.persistence.criteria.*;

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

		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

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
			orPredicates.add(criteriaBuilder.equal(criteriaBuilder.lower(fromRight.<String>get("objet")),
					objet.toLowerCase()));
			if (!objet.contains("*"))
				orPredicates.add(criteriaBuilder.equal(criteriaBuilder.lower(fromRight.<String>get("objet")),
						"JJ" + objet.toLowerCase()));
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
	public List<JJContact> areAuthorized(JJCompany company, JJContact contact,
			JJProject project, JJProduct product, String objet,
			JJCategory category, Boolean r, Boolean w, Boolean x) {

		List<JJContact> contacts = new ArrayList<JJContact>();

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

		if (objet != null && !objet.equalsIgnoreCase("sprintContact")) {
			orPredicates.add(criteriaBuilder.equal(criteriaBuilder.lower(fromRight.<String>get("objet")),
					objet.toLowerCase()));
			if (!objet.contains("*"))
				orPredicates.add(criteriaBuilder.equal(criteriaBuilder.lower(fromRight.<String>get("objet")),
						"JJ" + objet.toLowerCase()));
		} else {
			if (objet != null && objet.equalsIgnoreCase("sprintContact")) {
				orPredicates.add(criteriaBuilder.equal(criteriaBuilder.lower(fromRight.<String>get("objet")),
						"Task".toLowerCase()));
				orPredicates.add(criteriaBuilder.equal(criteriaBuilder.lower(fromRight.<String>get("objet")),
						"JJTask".toLowerCase()));
			}

		}

		orPredicates.add(criteriaBuilder.equal(fromRight.get("objet"), "*"));

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

		if (!(objet != null && objet.equalsIgnoreCase("sprintContact"))) {
			orPredicates = new ArrayList<Predicate>();
			if (product != null)
				orPredicates.add(criteriaBuilder.equal(from.get("product"),
						product));

			orPredicates.add(criteriaBuilder.isNull(from.get("product")));

			orPredicate = criteriaBuilder.or(orPredicates
					.toArray(new Predicate[] {}));

			predicates.add(criteriaBuilder.and(orPredicate));
		}

		predicates.add(criteriaBuilder.equal(path.get("enabled"), true));
		predicates.add(criteriaBuilder.equal(from.join("contact")
				.get("enabled"), true));

		predicates.add(criteriaBuilder.in(path).value(subquery));

		select.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

		TypedQuery<JJPermission> result = entityManager.createQuery(select);

		List<JJPermission> permissions = result.getResultList();

		if (company == null)
			for (JJPermission permission : permissions) {
				if (!contacts.contains(permission.getContact())) {
					contacts.add(permission.getContact());
				}
			}
		else {
			if (contact == null)
				for (JJPermission permission : permissions) {
					if (permission.getContact().getCompany().equals(company)
							&& !contacts.contains(permission.getContact())) {
						contacts.add(permission.getContact());
					}
				}
			else {
				if (isSuperAdmin(contact)) {
					if (objet != null
							&& objet.equalsIgnoreCase("sprintContact")) {
						for (JJPermission permission : permissions) {
							if (permission.getContact().getCompany()
									.equals(company)
									&& !contacts.contains(permission
											.getContact())) {
								contacts.add(permission.getContact());
							}
						}

					} else
						for (JJPermission permission : permissions) {
							if (!contacts.contains(permission.getContact())) {
								contacts.add(permission.getContact());
							}
						}
				} else
					for (JJPermission permission : permissions) {
						if (permission.getContact().getCompany()
								.equals(company)
								&& !contacts.contains(permission.getContact())) {
							contacts.add(permission.getContact());
						}
					}

			}
		}

		return contacts;

	}

	@SuppressWarnings("unchecked")
	public JJProject getDefaultProject(JJContact contact) {
		String qu = "SELECT r FROM  JJPermission r Where r.project IS NOT NULL AND r.project.manager.company = :c "
				+ "AND r.enabled = true " + "AND r.contact = :contact";

		Query query = entityManager.createQuery(qu, JJPermission.class);
		query.setParameter("c", contact.getCompany());
		query.setParameter("contact", contact);

		List<JJPermission> permissions = ((List<JJPermission>) query
				.getResultList());

		if (!permissions.isEmpty()) {
			List<JJProject> projects = new ArrayList<JJProject>();

			for (JJPermission permission : permissions) {
				if (permission.getProject() != null)
					projects.add(permission.getProject());
			}

			Map<JJProject, Integer> map = new HashMap<>();

			for (JJProject t : projects) {
				Integer val = map.get(t);
				map.put(t, val == null ? 1 : val + 1);
			}

			Entry<JJProject, Integer> max = null;

			for (Entry<JJProject, Integer> e : map.entrySet()) {
				if (max == null || e.getValue() > max.getValue())
					max = e;
			}

			return max.getKey();
		} else {
			List<JJProject> projects = new ArrayList<JJProject>();
			CriteriaBuilder criteriaBuilder = entityManager
					.getCriteriaBuilder();

			CriteriaQuery<JJProject> criteriaQuery = criteriaBuilder
					.createQuery(JJProject.class);

			Root<JJProject> from = criteriaQuery.from(JJProject.class);

			CriteriaQuery<JJProject> select = criteriaQuery.select(from);

			List<Predicate> predicates = new ArrayList<Predicate>();

			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

			predicates.add(criteriaBuilder.equal(
					from.join("manager").get("company"), contact.getCompany()));
			select.where(predicates.toArray(new Predicate[] {}));

			TypedQuery<JJProject> result = entityManager.createQuery(select);

			for (JJProject proj : result.getResultList()) {
				if (!projects.contains(proj))
					projects.add(proj);
			}

			if (!projects.isEmpty())
				return projects.get(0);
			else
				return null;
		}

	}

	@SuppressWarnings("unchecked")
	public JJCategory getDefaultCategory(JJContact contact) {

		String qu = "SELECT r FROM  JJPermission r Where "
				+ "r.enabled = true " + "AND r.contact = :contact";

		List<JJCategory> categories = new ArrayList<JJCategory>();
		Query query = entityManager.createQuery(qu, JJPermission.class);
		query.setParameter("contact", contact);

		List<JJPermission> permissions = ((List<JJPermission>) query
				.getResultList());

		if (!permissions.isEmpty()) {
			List<JJRight> rights = new ArrayList<JJRight>();

			for (JJPermission permission : permissions) {
				if (permission.getProfile().getRights() != null)
					rights.addAll(new ArrayList<JJRight>(permission
							.getProfile().getRights()));
			}
			for (JJRight right : rights) {
				if (right.getCategory() != null)
					categories.add(right.getCategory());
			}
			Map<JJCategory, Integer> map = new HashMap<>();

			if (!categories.isEmpty()) {
				for (JJCategory t : categories) {
					Integer val = map.get(t);
					map.put(t, val == null ? 1 : val + 1);
				}
				categories = new ArrayList<JJCategory>();

				Entry<JJCategory, Integer> max = null;

				for (Entry<JJCategory, Integer> e : map.entrySet()) {

					if (max == null || e.getValue() > max.getValue())
						max = e;

				}
				return max.getKey();
			} else {
				int i = 1;

				while (categories.size() < 3) {
					qu = "SELECT r FROM  JJCategory r Where "
							+ "r.enabled = true " + "AND r.stage =" + i;
					query = entityManager.createQuery(qu, JJCategory.class);

					if (!query.getResultList().isEmpty()) {
						categories.add((JJCategory) query.getResultList()
								.get(0));

					}
					i++;
				}
				return categories.get(0);
			}

		} else {
			int i = 1;

			while (categories.size() < 1) {
				qu = "SELECT r FROM  JJCategory r Where " + "r.enabled = true "
						+ "AND r.stage =" + i;
				query = entityManager.createQuery(qu, JJCategory.class);

				if (!query.getResultList().isEmpty()) {
					categories.add((JJCategory) query.getResultList().get(0));

				}
				i++;
			}
			return categories.get(0);
		}

	}

	@SuppressWarnings("unchecked")
	public List<JJCategory> getDefaultCategories(JJContact contact) {

		String qu = "SELECT r FROM  JJPermission r Where "
				+ "r.enabled = true " + "AND r.contact = :contact";

		Query query = entityManager.createQuery(qu, JJPermission.class);
		query.setParameter("contact", contact);

		List<JJPermission> permissions = ((List<JJPermission>) query
				.getResultList());
		List<JJCategory> categories = new ArrayList<JJCategory>();

		if (!permissions.isEmpty()) {
			List<JJRight> rights = new ArrayList<JJRight>();

			for (JJPermission permission : permissions) {
				if (permission.getProfile().getRights() != null)
					rights.addAll(new ArrayList<JJRight>(permission
							.getProfile().getRights()));
			}

			for (JJRight right : rights) {
				if (right.getCategory() != null)
					categories.add(right.getCategory());
			}
			Map<JJCategory, Integer> map = new HashMap<>();

			if (!categories.isEmpty()) {
				for (JJCategory t : categories) {
					Integer val = map.get(t);
					map.put(t, val == null ? 1 : val + 1);
				}
				categories = new ArrayList<JJCategory>();

				int i = 0;
				for (i = 0; i < 3; i++) {
					Entry<JJCategory, Integer> max = null;

					for (Entry<JJCategory, Integer> e : map.entrySet()) {
						if (!categories.contains(e.getKey())) {
							if (max == null || e.getValue() > max.getValue())
								max = e;
						}
					}
					if (max != null) {
						categories.add(max.getKey());
						map.remove(max);
					}

				}

				i = 1;

				while (categories.size() < 3) {
					qu = "SELECT r FROM  JJCategory r Where "
							+ "r.enabled = true " + "AND r.stage =" + i;
					query = entityManager.createQuery(qu, JJCategory.class);

					if (!query.getResultList().isEmpty()) {
						categories.add((JJCategory) query.getResultList()
								.get(0));

					}
					i++;
				}

			} else {
				int i = 1;

				while (categories.size() < 3) {
					qu = "SELECT r FROM  JJCategory r Where "
							+ "r.enabled = true " + "AND r.stage =" + i;
					query = entityManager.createQuery(qu, JJCategory.class);

					if (!query.getResultList().isEmpty()) {
						categories.add((JJCategory) query.getResultList()
								.get(0));

					}
					i++;
				}
			}

			return categories;

		} else {
			int i = 1;

			while (categories.size() < 3) {

				qu = "SELECT r FROM  JJCategory r Where " + "r.enabled = true "
						+ "AND r.stage =" + i;
				query = entityManager.createQuery(qu, JJCategory.class);

				if (!query.getResultList().isEmpty()) {
					categories.add((JJCategory) query.getResultList().get(0));

				}
				i++;
			}
			return categories;
		}
	}

	@SuppressWarnings("unchecked")
	public JJProduct getDefaultProduct(JJContact contact) {

		String qu = "SELECT r FROM  JJPermission r Where r.product IS NOT NULL AND r.product.manager.company = :c "
				+ "AND r.enabled = true " + "AND r.contact = :contact";

		Query query = entityManager.createQuery(qu, JJPermission.class);
		query.setParameter("c", contact.getCompany());
		query.setParameter("contact", contact);

		List<JJPermission> permissions = ((List<JJPermission>) query
				.getResultList());

		if (!permissions.isEmpty()) {
			List<JJProduct> products = new ArrayList<JJProduct>();

			for (JJPermission permission : permissions) {
				if (permission.getProduct() != null)
					products.add(permission.getProduct());
			}

			Map<JJProduct, Integer> map = new HashMap<>();

			for (JJProduct t : products) {
				Integer val = map.get(t);
				map.put(t, val == null ? 1 : val + 1);
			}

			Entry<JJProduct, Integer> max = null;

			for (Entry<JJProduct, Integer> e : map.entrySet()) {
				if (max == null || e.getValue() > max.getValue())
					max = e;
			}

			return max.getKey();
		} else {

			List<JJProduct> products = new ArrayList<JJProduct>();
			CriteriaBuilder criteriaBuilder = entityManager
					.getCriteriaBuilder();

			CriteriaQuery<JJProduct> criteriaQuery = criteriaBuilder
					.createQuery(JJProduct.class);

			Root<JJProduct> from = criteriaQuery.from(JJProduct.class);

			CriteriaQuery<JJProduct> select = criteriaQuery.select(from);

			List<Predicate> predicates = new ArrayList<Predicate>();

			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

			predicates.add(criteriaBuilder.equal(
					from.join("manager").get("company"), contact.getCompany()));
			select.where(predicates.toArray(new Predicate[] {}));

			TypedQuery<JJProduct> result = entityManager.createQuery(select);

			for (JJProduct proj : result.getResultList()) {
				if (!products.contains(proj))
					products.add(proj);
			}

			if (!products.isEmpty())
				return products.get(0);
			else
				return null;
		}

	}

	public boolean isSuperAdmin(JJContact contact) {
		return isAuthorized(contact, null, null, "Company", null, null, null,
				true);
	}

	@Override
	public List<JJContact> getManagers(JJCompany company, JJContact contact,
			String objet) {
		return areAuthorized(company, contact, null, null, objet, null, null,
				true, null);
	}

	@Override
	public List<JJContact> areAuthorized(JJCompany company, JJContact contact,
			JJProject project, JJProduct product, String objet) {
		return areAuthorized(company, contact, project, product, objet, null,
				null, null, null);
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

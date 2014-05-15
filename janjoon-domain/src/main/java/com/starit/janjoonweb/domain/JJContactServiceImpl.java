package com.starit.janjoonweb.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

	@Override
	public boolean updateJJContactTransaction(JJContact contact) {

		JJContact ctcs = getContactByEmail(contact.getEmail(), false);

		if (ctcs == null) {

			updateJJContact(contact);
			return true;

		} else {

			if (ctcs.getId().equals(contact.getId())) {
				updateJJContact(contact);
				return true;
			} else
				return false;

		}
	}

	public List<JJRight> getContactAuthorization(String object,
			JJContact contact, JJProduct product, JJProject project,
			JJCategory category) {

		List<JJRight> rights = new ArrayList<JJRight>();

		List<JJPermission> permissions = getContactPermission(contact);
		List<JJPermission> permissions1 = new ArrayList<JJPermission>();

		for (JJPermission perm : permissions) {

			boolean add = (product == null && project == null);

			if (!add && product == null) {
				if (perm.getProject() != null) {
					System.out.println("perm.getProject().equals(project)"
							+ perm.getProject());
					add = (perm.getProject().equals(project));
				}

				else
					add = true;

			}

			if (!add && project == null) {

				if (perm.getProduct() != null) {
					add = (perm.getProduct().equals(product));
					System.out.println("perm.getProduct().equals(product)"
							+ perm.getProfile());
				}

				else
					add = true;

			}

			if (!add && product != null && project != null) {

				add = (perm.getProduct() == null && perm.getProject() == null);
				System.out
						.println("perm.getProduct() == null && perm.getProject() == null"
								+ add);

				if (!add) {
					if (perm.getProduct() == null) {
						add = (perm.getProject().equals(project));

					} else if (perm.getProject() == null) {
						add = (perm.getProduct().equals(product));

					} else {
						add = ((perm.getProduct().equals(product)) && (perm
								.getProject().equals(project)));

					}
				}

			}

			if (add)
				add = !containPerm(permissions1, perm);

			if (add) {
				permissions1.add(perm);
			}
		}

		for (JJPermission perm : permissions1) {
			for (JJRight right : perm.getProfile().getRights()) {

				boolean add = (right.getCategory() == null);

				if (!add)
					add = (category == null)
							|| (right.getCategory().equals(category));

				add = add && !containRight(rights, right);

				if (add) {

					if (object != null) {
						if (right.getObjet() != null) {
							if (right.getObjet().equalsIgnoreCase(object))
								rights.add(right);
						} else
							rights.add(right);
					} else
						rights.add(right);
				}
			}
		}

		return rights;
	}

	private boolean containPerm(List<JJPermission> list, JJPermission object) {

		boolean contain = false;
		int i = 0;
		while (i < list.size() && !contain) {
			contain = (list.get(i).equals(object));
			i++;
		}
		return contain;
	}

	@Override
	public List<JJPermission> getContactPermission(JJContact contact) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJPermission> criteriaQuery = criteriaBuilder
				.createQuery(JJPermission.class);

		Root<JJPermission> from = criteriaQuery.from(JJPermission.class);

		CriteriaQuery<JJPermission> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(criteriaBuilder.equal(from.get("contact"), contact));

		select.where(predicates.toArray(new Predicate[] {}));

		TypedQuery<JJPermission> result = entityManager.createQuery(select);

		return result.getResultList();

	}

	private boolean containRight(List<JJRight> list, JJRight object) {
		boolean contain = false;
		int i = 0;
		while (i < list.size() && !contain) {
			contain = (list.get(i).equals(object));
			i++;
		}
		return contain;
	}

	@Override
	public List<JJRight> getContactAuthorization(JJContact contact,
			JJProduct product) {

		return getContactAuthorization(null, contact, product, null, null);

	}

	@Override
	public List<JJRight> getContactAuthorization(JJContact contact,
			JJProduct product, JJProject project) {

		return getContactAuthorization(null, contact, product, project, null);
	}

	@Override
	public List<JJRight> getContactAuthorization(JJContact contact,
			JJProject project) {

		return getContactAuthorization(null, contact, null, project, null);
	}

}

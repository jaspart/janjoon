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

		} else if (ctcs.size() == 1) {
			if (ctcs.get(0).getId().equals(contact.getId())) {
				updateJJContact(contact);
				return true;
			} else
				return false;

		} else {
			return false;
		}
	}

	public List<JJRight> getContactAuthorization(JJContact contact,
			JJProduct product, JJProject project, JJCategory category) {

		List<JJRight> rights = new ArrayList<JJRight>();

		//Set<JJPermission> permissions = contact.getPermissions();
		List<JJPermission> permissions = getContactPermission(contact);
		List<JJPermission> permissions1 = new ArrayList<JJPermission>();
		

		for (JJPermission perm : permissions) {

			boolean add = (product == null && project == null);
			System.out.println("product == null && project == null " + add);
			if (!add && product == null) {
				if (perm.getProject() != null)
					add = (perm.getProject().equals(project));
				else
					add = true;

				System.out.println("perm.getProject().equals(project)" + add);
			}

			if (!add && project == null) {

				if (perm.getProduct() != null)
					add = (perm.getProduct().equals(product));
				else
					add = true;

				System.out.println("perm.getProduct().equals(product)" + add);
			}

			if (!add && product != null && project != null) {

				add = (perm.getProduct() == null && perm.getProject() == null);

				System.out
						.println("perm.getProduct()==null && perm.getProject()==null "
								+ add);

				if (!add) {
					if (perm.getProduct() == null) {
						add = (perm.getProject().equals(project));
						System.out.println("perm.getProject().equals(project)"
								+ add);

					} else if (perm.getProject() == null) {
						add = (perm.getProduct().equals(product));
						System.out.println("perm.getProduct().equals(product)"
								+ add);
					} else {
						add = ((perm.getProduct().equals(product)) && (perm
								.getProject().equals(project)));
						System.out
								.println("(perm.getProduct().equals(product)) && (perm.getProject().equals(project))"
										+ add);
					}

					System.out.println(add);
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
					rights.add(right);
					System.out.println(right.getProfile().getName());
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

		return getContactAuthorization(contact, product, null, null);

	}

	@Override
	public List<JJRight> getContactAuthorization(JJContact contact,
			JJProduct product, JJProject project) {

		return getContactAuthorization(contact, product, project, null);
	}

	@Override
	public List<JJRight> getContactAuthorization(JJContact contact,
			JJProject project) {

		return getContactAuthorization(contact, null, project, null);
	}

}

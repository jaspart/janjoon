package com.starit.janjoonweb.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.mutable.MutableInt;

public class JJProductServiceImpl implements JJProductService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	// New Generic
	public List<JJProduct> getAdminListProducts() {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJProduct> criteriaQuery = criteriaBuilder
				.createQuery(JJProduct.class);

		Root<JJProduct> from = criteriaQuery.from(JJProduct.class);

		CriteriaQuery<JJProduct> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

		select.where(predicates.toArray(new Predicate[] {}));

		TypedQuery<JJProduct> result = entityManager.createQuery(select);

		return result.getResultList();

	}
	public List<JJProduct> load(JJCompany company, MutableInt size, int first,
			int pageSize) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJProduct> criteriaQuery = criteriaBuilder
				.createQuery(JJProduct.class);

		Root<JJProduct> from = criteriaQuery.from(JJProduct.class);

		CriteriaQuery<JJProduct> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

		if (company != null)
			predicates.add(criteriaBuilder.equal(
					from.join("manager").get("company"), company));

		select.where(predicates.toArray(new Predicate[] {}));

		TypedQuery<JJProduct> result = entityManager.createQuery(select);
		result.setFirstResult(first);
		result.setMaxResults(pageSize);

		if (company != null) {
			String qu = "SELECT COUNT(r) FROM  JJProduct r Where r.manager.company = :c "
					+ "AND r.enabled = true ";

			Query query = entityManager.createQuery(qu);
			query.setParameter("c", company);
			size.setValue(Math.round((long) query.getSingleResult()));
		} else {
			String qu = "SELECT COUNT(r) FROM  JJProduct r Where"
					+ " r.enabled = true ";

			Query query = entityManager.createQuery(qu);

			size.setValue(Math.round((long) query.getSingleResult()));
		}

		return result.getResultList();

	}

	@Override
	public List<JJProduct> getProducts(JJCompany company, JJContact contact,
			boolean onlyActif) {

		if (company != null) {

			List<JJProduct> products = new ArrayList<JJProduct>();
			CriteriaBuilder criteriaBuilder = entityManager
					.getCriteriaBuilder();
			boolean all = false;

			if (contact != null) {
				CriteriaQuery<JJPermission> criteriaPermission = criteriaBuilder
						.createQuery(JJPermission.class);
				Root<JJPermission> fromPermission = criteriaPermission
						.from(JJPermission.class);
				List<Predicate> predicatesPermion = new ArrayList<Predicate>();
				predicatesPermion.add(criteriaBuilder.equal(
						fromPermission.get("enabled"), true));

				predicatesPermion.add(criteriaBuilder.equal(
						fromPermission.get("contact"), contact));
				CriteriaQuery<JJPermission> selectPermission = criteriaPermission
						.select(fromPermission);
				selectPermission.where(predicatesPermion
						.toArray(new Predicate[] {}));

				TypedQuery<JJPermission> resultPermission = entityManager
						.createQuery(selectPermission);

				for (JJPermission permission : resultPermission.getResultList()) {
					if (permission.getProject() != null) {
						products.add(permission.getProduct());
					} else {
						all = true;
					}
				}

			}
			if (all) {
				CriteriaQuery<JJProduct> criteriaQuery = criteriaBuilder
						.createQuery(JJProduct.class);

				Root<JJProduct> from = criteriaQuery.from(JJProduct.class);

				CriteriaQuery<JJProduct> select = criteriaQuery.select(from);

				List<Predicate> predicates = new ArrayList<Predicate>();

				if (onlyActif) {
					predicates.add(criteriaBuilder.equal(from.get("enabled"),
							true));
				}
				predicates.add(criteriaBuilder.equal(
						from.join("manager").get("company"), company));

				select.where(predicates.toArray(new Predicate[] {}));

				TypedQuery<JJProduct> result = entityManager
						.createQuery(select);
				for (JJProduct prod : result.getResultList()) {
					if(!products.contains(prod))
						products.add(prod);
				}

			}

			return products;
		} else
			return null;

	}

	@Override
	public JJProduct getJJProductWithName(String name) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJProduct> criteriaQuery = criteriaBuilder
				.createQuery(JJProduct.class);

		Root<JJProduct> from = criteriaQuery.from(JJProduct.class);

		CriteriaQuery<JJProduct> select = criteriaQuery.select(from);

		Predicate predicate1 = criteriaBuilder.equal(from.get("enabled"), true);
		Predicate predicate2 = criteriaBuilder.equal(from.get("name"), name);

		select.where(criteriaBuilder.and(predicate1, predicate2));

		TypedQuery<JJProduct> result = entityManager.createQuery(select);

		if (result.getResultList().size() > 0)
			return result.getResultList().get(0);
		else
			return null;

	}

	public void saveJJProduct(JJProduct JJProduct_) {

		jJProductRepository.save(JJProduct_);
		JJProduct_ = jJProductRepository.findOne(JJProduct_.getId());
	}

	public JJProduct updateJJProduct(JJProduct JJProduct_) {
		jJProductRepository.save(JJProduct_);
		JJProduct_ = jJProductRepository.findOne(JJProduct_.getId());
		return JJProduct_;
	}

}

package com.starit.janjoonweb.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import com.starit.janjoonweb.domain.reference.StrFunction;

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

		select.where(predicates.toArray(new Predicate[]{}));

		TypedQuery<JJProduct> result = entityManager.createQuery(select);

		return result.getResultList();

	}

	public List<JJProduct> getProducts(JJCompany company, JJProject project) {
		if (project == null)
			return getProducts(company, null, true, true);
		else {
			CriteriaBuilder criteriaBuilder = entityManager
					.getCriteriaBuilder();
			CriteriaQuery<JJProduct> criteriaQuery = criteriaBuilder
					.createQuery(JJProduct.class);

			Root<JJRequirement> from = criteriaQuery.from(JJRequirement.class);

			CriteriaQuery<JJProduct> select = criteriaQuery
					.select(from.<JJProduct> get("product"));

			List<Predicate> predicates = new ArrayList<Predicate>();

			predicates.add(criteriaBuilder.equal(from.get("project"), project));
			predicates.add(criteriaBuilder.isNotNull(from.get("product")));
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

			select.where(predicates.toArray(new Predicate[]{}));

			TypedQuery<JJProduct> result = entityManager.createQuery(select);
			HashSet<JJProduct> products = new HashSet<JJProduct>(
					result.getResultList());

			Root<JJBug> from2 = criteriaQuery.from(JJBug.class);

			select = criteriaQuery.select(
					from2.join("versioning").<JJProduct> get("product"));

			predicates = new ArrayList<Predicate>();

			predicates
					.add(criteriaBuilder.equal(from2.get("project"), project));
			predicates.add(criteriaBuilder.isNotNull(from2.get("versioning")));
			predicates.add(criteriaBuilder.equal(from2.get("enabled"), true));

			select.where(predicates.toArray(new Predicate[]{}));

			TypedQuery<JJProduct> result2 = entityManager.createQuery(select);
			products.addAll(result2.getResultList());

			return new ArrayList<JJProduct>(products);

		}
	}

	public List<JJProduct> load(JJCompany company, MutableInt size, int first,
			int pageSize, List<SortMeta> multiSortMeta,
			Map<String, Object> filters) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJProduct> criteriaQuery = criteriaBuilder
				.createQuery(JJProduct.class);

		Root<JJProduct> from = criteriaQuery.from(JJProduct.class);

		CriteriaQuery<JJProduct> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (company != null)
			predicates.add(criteriaBuilder
					.equal(from.get("manager").get("company"), company));

		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

		if (filters != null) {
			Iterator<Entry<String, Object>> it = filters.entrySet().iterator();
			while (it.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry pairs = (Map.Entry) it.next();
				if (pairs.getKey().toString().contains("globalFilter")) {
					predicates.add(
							criteriaBuilder.or(
									criteriaBuilder.like(criteriaBuilder.upper(
											from.<String> get("name")),
											"%" + pairs.getValue() + "%"),
									criteriaBuilder.like(
											criteriaBuilder.upper(
													from.<String> get(
															"extname")),
											"%" + pairs.getValue() + "%"),
									criteriaBuilder.like(
											new StrFunction<Long>(
													criteriaBuilder,
													from.<Long> get("id")),
											"%" + pairs.getValue() + "%")));
				} else if (pairs.getKey().toString().contains("company")) {

					predicates
							.add(criteriaBuilder.equal(
									from.get("manager").get("company")
											.<String> get("name"),
									pairs.getValue().toString()));

				} else
					predicates
							.add(criteriaBuilder.like(from.<String> get("name"),
									"%" + pairs.getValue() + "%"));

			}
		}

		select.where(predicates.toArray(new Predicate[]{}));

		if (multiSortMeta != null) {
			for (SortMeta sortMeta : multiSortMeta) {
				String sortField = sortMeta.getSortField();
				SortOrder sortOrder = sortMeta.getSortOrder();
				if (!sortField.contains("company")) {
					if (sortOrder.equals(SortOrder.DESCENDING))
						select.orderBy(
								criteriaBuilder.desc(from.get(sortField)));
					else if (sortOrder.equals(SortOrder.ASCENDING)) {
						select.orderBy(
								criteriaBuilder.asc(from.get(sortField)));
					}
				} else if (sortField.contains("company")) {
					Join<JJContact, JJCompany> owner = from.join("manager")
							.join("company");

					if (sortOrder.equals(SortOrder.DESCENDING))
						select.orderBy(criteriaBuilder.desc(owner.get("name")));
					else if (sortOrder.equals(SortOrder.ASCENDING)) {
						select.orderBy(criteriaBuilder.asc(owner.get("name")));
					}
				}
			}
		} else
			select.orderBy(criteriaBuilder.desc(from.get("creationDate")));

		TypedQuery<JJProduct> result = entityManager.createQuery(select);
		result.setFirstResult(first);
		result.setMaxResults(pageSize);

		CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
		cq.select(criteriaBuilder.count(cq.from(JJProduct.class)));
		entityManager.createQuery(cq);
		cq.where(predicates.toArray(new Predicate[]{}));
		size.setValue(entityManager.createQuery(cq).getSingleResult());

		// if (company != null) {
		// String qu =
		// "SELECT COUNT(r) FROM JJProduct r Where r.manager.company = :c "
		// + "AND r.enabled = true ";
		//
		// Query query = entityManager.createQuery(qu);
		// query.setParameter("c", company);
		// size.setValue(Math.round((long) query.getSingleResult()));
		// } else {
		// String qu = "SELECT COUNT(r) FROM JJProduct r Where"
		// + " r.enabled = true ";
		//
		// Query query = entityManager.createQuery(qu);
		//
		// size.setValue(Math.round((long) query.getSingleResult()));
		// }

		return result.getResultList();

	}

	@Override
	public List<JJProduct> getProducts(JJCompany company, JJContact contact,
			boolean onlyActif, boolean all) {

		if (company != null) {

			List<JJProduct> products = new ArrayList<JJProduct>();
			CriteriaBuilder criteriaBuilder = entityManager
					.getCriteriaBuilder();

			if (contact != null && !all) {
				CriteriaQuery<JJPermission> criteriaPermission = criteriaBuilder
						.createQuery(JJPermission.class);
				Root<JJPermission> fromPermission = criteriaPermission
						.from(JJPermission.class);
				List<Predicate> predicatesPermion = new ArrayList<Predicate>();

				predicatesPermion.add(criteriaBuilder
						.equal(fromPermission.get("contact"), contact));

				predicatesPermion.add(criteriaBuilder
						.equal(fromPermission.get("enabled"), true));

				CriteriaQuery<JJPermission> selectPermission = criteriaPermission
						.select(fromPermission);
				selectPermission
						.where(predicatesPermion.toArray(new Predicate[]{}));

				TypedQuery<JJPermission> resultPermission = entityManager
						.createQuery(selectPermission);

				for (JJPermission permission : resultPermission
						.getResultList()) {
					if (permission.getProduct() != null) {
						if (!products.contains(permission.getProduct()))
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
					predicates.add(
							criteriaBuilder.equal(from.get("enabled"), true));
				}
				predicates.add(criteriaBuilder
						.equal(from.join("manager").get("company"), company));

				select.where(predicates.toArray(new Predicate[]{}));

				TypedQuery<JJProduct> result = entityManager
						.createQuery(select);
				for (JJProduct prod : result.getResultList()) {
					if (!products.contains(prod))
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
		Predicate predicate2 = criteriaBuilder.equal(
				criteriaBuilder.lower(from.<String> get("name")),
				name.toLowerCase());

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

package com.starit.janjoonweb.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import com.starit.janjoonweb.domain.reference.StrFunction;

public class JJCompanyServiceImpl implements JJCompanyService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;

	}

	public void saveJJCompany(JJCompany JJCompany_) {
		jJCompanyRepository.save(JJCompany_);
		JJCompany_ = jJCompanyRepository.findOne(JJCompany_.getId());
	}

	public Long getMaxId() {
		Long r = (Long) entityManager
				.createQuery("select max(e.id) from JJCompany e")
				.getSingleResult();
		return r + 1;
	}

	@Override
	public List<JJCompany> load(MutableInt size, int first, int pageSize,
			List<SortMeta> multiSortMeta, Map<String, Object> filters) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJCompany> criteriaQuery = criteriaBuilder
				.createQuery(JJCompany.class);

		Root<JJCompany> from = criteriaQuery.from(JJCompany.class);

		CriteriaQuery<JJCompany> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

		if (filters != null) {
			Iterator<Entry<String, Object>> it = filters.entrySet().iterator();
			while (it.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry pairs = (Map.Entry) it.next();
				if (pairs.getKey().toString().contains("globalFilter")) {
					predicates
							.add(criteriaBuilder.or(
									criteriaBuilder.like(
											criteriaBuilder.upper(
													from.<String> get("name")),
											"%" + pairs.getValue() + "%"),
									criteriaBuilder.like(
											new StrFunction<Long>(
													criteriaBuilder,
													from.<Long> get("id")),
											"%" + pairs.getValue() + "%")));
				}
			}
		}

		select.where(
				criteriaBuilder.and(predicates.toArray(new Predicate[]{})));

		if (multiSortMeta != null) {
			for (SortMeta sortMeta : multiSortMeta) {
				String sortField = sortMeta.getSortField();
				SortOrder sortOrder = sortMeta.getSortOrder();

				if (sortOrder.equals(SortOrder.DESCENDING))
					select.orderBy(criteriaBuilder.desc(from.get(sortField)));
				else if (sortOrder.equals(SortOrder.ASCENDING)) {
					select.orderBy(criteriaBuilder.asc(from.get(sortField)));
				}

			}
		} else
			select.orderBy(criteriaBuilder.desc(from.get("creationDate")));

		TypedQuery<JJCompany> result = entityManager.createQuery(select);
		result.setFirstResult(first);
		result.setMaxResults(pageSize);

		CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
		cq.select(criteriaBuilder.count(cq.from(JJCompany.class)));
		cq.where(predicates.toArray(new Predicate[]{}));
		size.setValue(entityManager.createQuery(cq).getSingleResult());

		return result.getResultList();

	}

	public JJCompany updateJJCompany(JJCompany JJCompany_) {

		jJCompanyRepository.save(JJCompany_);
		JJCompany_ = jJCompanyRepository.findOne(JJCompany_.getId());
		return JJCompany_;
	}

	public JJCompany getCompanyByName(String name) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJCompany> criteriaQuery = criteriaBuilder
				.createQuery(JJCompany.class);

		Root<JJCompany> from = criteriaQuery.from(JJCompany.class);

		CriteriaQuery<JJCompany> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

		predicates.add(criteriaBuilder.equal(
				criteriaBuilder.lower(from.<String> get("name")),
				name.toLowerCase()));

		select.where(predicates.toArray(new Predicate[]{}));

		TypedQuery<JJCompany> result = entityManager.createQuery(select);

		if (result.getResultList() != null && !result.getResultList().isEmpty())
			return result.getResultList().get(0);
		else
			return null;

	}

	public List<JJCompany> getActifCompanies() {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJCompany> criteriaQuery = criteriaBuilder
				.createQuery(JJCompany.class);

		Root<JJCompany> from = criteriaQuery.from(JJCompany.class);

		CriteriaQuery<JJCompany> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

		select.where(predicates.toArray(new Predicate[]{}));

		TypedQuery<JJCompany> result = entityManager.createQuery(select);
		return result.getResultList();

	}

}

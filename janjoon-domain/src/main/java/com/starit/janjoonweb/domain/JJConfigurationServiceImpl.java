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

public class JJConfigurationServiceImpl implements JJConfigurationService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public List<JJConfiguration> load(MutableInt size, int first, int pageSize,
			String name, String param, boolean onlyactif,
			List<SortMeta> multiSortMeta, Map<String, Object> filters) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJConfiguration> criteriaQuery = criteriaBuilder
				.createQuery(JJConfiguration.class);

		Root<JJConfiguration> from = criteriaQuery.from(JJConfiguration.class);

		CriteriaQuery<JJConfiguration> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (name != null) {

			predicates.add(criteriaBuilder.equal(from.get("name"), name));

		}

		if (param != null) {

			predicates.add(criteriaBuilder.equal(from.get("param"), param));

		}

		if (onlyactif) {

			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

		}

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
													from.<String> get("param")),
											"%" + pairs.getValue() + "%"),
									criteriaBuilder.like(
											criteriaBuilder.upper(
													from.<String> get("name")),
											"%" + pairs.getValue() + "%"),
							criteriaBuilder.like(
									new StrFunction<Long>(criteriaBuilder,
											from.<Long> get("id")),
									"%" + pairs.getValue() + "%")));
				}
			}
		}
		select.where(predicates.toArray(new Predicate[]{}));

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

		TypedQuery<JJConfiguration> result = entityManager.createQuery(select);
		result.setFirstResult(first);
		result.setMaxResults(pageSize);

		CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
		cq.select(criteriaBuilder.count(cq.from(JJConfiguration.class)));
		entityManager.createQuery(cq);
		cq.where(predicates.toArray(new Predicate[]{}));
		size.setValue(entityManager.createQuery(cq).getSingleResult());

		return result.getResultList();
	}

	@Override
	public List<JJConfiguration> getConfigurations(String name, String param,
			boolean onlyActif) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJConfiguration> criteriaQuery = criteriaBuilder
				.createQuery(JJConfiguration.class);

		Root<JJConfiguration> from = criteriaQuery.from(JJConfiguration.class);

		CriteriaQuery<JJConfiguration> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (name != null) {
			predicates.add(criteriaBuilder.equal(from.get("name"), name));
		}

		if (param != null) {

			predicates.add(criteriaBuilder.equal(from.get("param"), param));

		}

		if (onlyActif) {

			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

		}

		select.where(predicates.toArray(new Predicate[]{}));

		TypedQuery<JJConfiguration> result = entityManager.createQuery(select);
		return result.getResultList();
	}

	public boolean getDialogConfig(String name, String param) {

		List<JJConfiguration> configurations = getConfigurations(name, param,
				true);

		if (configurations.isEmpty())
			return true;
		else {
			JJConfiguration configuration = configurations.get(0);
			String val = configuration.getVal();
			if (val != null && val.length() > 0) {
				if (val.toLowerCase().equalsIgnoreCase("true")) {
					return true;
				} else if (val.toLowerCase().equalsIgnoreCase("false")) {
					return false;
				} else
					return true;
			} else
				return true;
		}

	}

	public void saveJJConfiguration(JJConfiguration JJConfiguration_) {

		jJConfigurationRepository.save(JJConfiguration_);
		JJConfiguration_ = jJConfigurationRepository
				.findOne(JJConfiguration_.getId());
	}

	public JJConfiguration updateJJConfiguration(
			JJConfiguration JJConfiguration_) {
		jJConfigurationRepository.save(JJConfiguration_);
		JJConfiguration_ = jJConfigurationRepository
				.findOne(JJConfiguration_.getId());
		return JJConfiguration_;
	}

}

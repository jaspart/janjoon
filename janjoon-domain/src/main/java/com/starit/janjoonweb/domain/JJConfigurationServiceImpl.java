package com.starit.janjoonweb.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.mutable.MutableInt;

public class JJConfigurationServiceImpl implements JJConfigurationService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public List<JJConfiguration> load(MutableInt size,int first, int pageSize, String name,
			String param, boolean onlyactif) {

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

		select.where(predicates.toArray(new Predicate[] {}));

		TypedQuery<JJConfiguration> result = entityManager.createQuery(select);
		result.setFirstResult(first);
		result.setMaxResults(pageSize);
		
		CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
		cq.select(criteriaBuilder.count(cq.from(JJConfiguration.class)));
		entityManager.createQuery(cq);
		cq.where(predicates.toArray(new Predicate[] {}));
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

		select.where(predicates.toArray(new Predicate[] {}));

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
		JJConfiguration_ = jJConfigurationRepository.findOne(JJConfiguration_
				.getId());
	}

	public JJConfiguration updateJJConfiguration(
			JJConfiguration JJConfiguration_) {
		jJConfigurationRepository.save(JJConfiguration_);
		JJConfiguration_ = jJConfigurationRepository.findOne(JJConfiguration_
				.getId());
		return JJConfiguration_;
	}

}

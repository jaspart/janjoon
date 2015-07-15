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

public class JJVersionServiceImpl implements JJVersionService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<JJVersion> getVersions(boolean onlyActif, boolean withProduct,
			JJProduct product, JJCompany company, boolean sortedbydate) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJVersion> criteriaQuery = criteriaBuilder
				.createQuery(JJVersion.class);

		Root<JJVersion> from = criteriaQuery.from(JJVersion.class);

		CriteriaQuery<JJVersion> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (withProduct) {
			if (product != null)
				predicates.add(criteriaBuilder.equal(from.get("product"),
						product));
			else {
				predicates.add(criteriaBuilder.isNotNull(from.get("product")));
				if (company != null)
					predicates.add(criteriaBuilder.equal(from.join("product")
							.join("manager").get("company"), company));
			}
		} else if (company != null)
			predicates.add(criteriaBuilder.equal(
					from.join("product").join("manager").get("company"),
					company));

		if (onlyActif) {
			predicates.add(criteriaBuilder.equal(from.get("enabled"), true));
		}

		select.where(predicates.toArray(new Predicate[] {}));

		if (sortedbydate) {
			select.orderBy(criteriaBuilder.desc(from.get("creationDate")));
		}

		TypedQuery<JJVersion> result = entityManager.createQuery(select);
		return result.getResultList();

	}

	public JJVersion getVersionByName(String jJversion, JJProduct product) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJVersion> criteriaQuery = criteriaBuilder
				.createQuery(JJVersion.class);

		Root<JJVersion> from = criteriaQuery.from(JJVersion.class);

		CriteriaQuery<JJVersion> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (product != null)
			predicates.add(criteriaBuilder.equal(from.get("product"), product));

		predicates.add(criteriaBuilder.equal(
				criteriaBuilder.lower(from.<String> get("name")),
				jJversion.toLowerCase()));

		predicates.add(criteriaBuilder.equal(from.get("enabled"), true));

		select.where(predicates.toArray(new Predicate[] {}));

		TypedQuery<JJVersion> result = entityManager.createQuery(select);

		if (result.getResultList() != null && !result.getResultList().isEmpty())
			return result.getResultList().get(0);
		else
			return null;

	}

	public void saveJJVersion(JJVersion JJVersion_) {

		jJVersionRepository.save(JJVersion_);
		JJVersion_ = jJVersionRepository.findOne(JJVersion_.getId());
	}

	public JJVersion updateJJVersion(JJVersion JJVersion_) {
		jJVersionRepository.save(JJVersion_);
		JJVersion_ = jJVersionRepository.findOne(JJVersion_.getId());
		return JJVersion_;
	}

}

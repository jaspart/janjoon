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

public class JJConnectionStatisticsServiceImpl implements
		JJConnectionStatisticsService {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public List<JJConnectionStatistics> load(JJCompany company, MutableInt size, int first,
			int pageSize) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JJConnectionStatistics> criteriaQuery = criteriaBuilder
				.createQuery(JJConnectionStatistics.class);

		Root<JJConnectionStatistics> from = criteriaQuery.from(JJConnectionStatistics.class);

		CriteriaQuery<JJConnectionStatistics> select = criteriaQuery.select(from);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (company != null)
			predicates.add(criteriaBuilder.equal(
					from.join("contact").get("company"), company));		

		select.where(predicates.toArray(new Predicate[] {}));
		select.orderBy(criteriaBuilder.desc(from.get("creationDate")));

		TypedQuery<JJConnectionStatistics> result = entityManager.createQuery(select);
		result.setFirstResult(first);
		result.setMaxResults(pageSize);

		if (company != null) {
			String qu = "SELECT COUNT(r) FROM  JJConnectionStatistics r Where r.contact.company = :c ";

			Query query = entityManager.createQuery(qu);
			query.setParameter("c", company);
			size.setValue(Math.round((long) query.getSingleResult()));
		} else {
			String qu = "SELECT COUNT(r) FROM  JJConnectionStatistics r ";

			Query query = entityManager.createQuery(qu);

			size.setValue(Math.round((long) query.getSingleResult()));
		}

		return result.getResultList();

	}


	public void saveJJConnectionStatistics(
			JJConnectionStatistics JJConnectionStatistics_) {		
		
		final long MINUTE_IN_MILLIS = 60 * 1000;
		JJConnectionStatistics_.setDuration(Math.round(JJConnectionStatistics_.getLogoutDate().getTime()/MINUTE_IN_MILLIS-
				JJConnectionStatistics_.getLoginDate().getTime()/MINUTE_IN_MILLIS));
		jJConnectionStatisticsRepository.save(JJConnectionStatistics_);
		JJConnectionStatistics_ = jJConnectionStatisticsRepository
				.findOne(JJConnectionStatistics_.getId());
	}

	public JJConnectionStatistics updateJJConnectionStatistics(
			JJConnectionStatistics JJConnectionStatistics_) {
		jJConnectionStatisticsRepository.save(JJConnectionStatistics_);
		JJConnectionStatistics_ = jJConnectionStatisticsRepository
				.findOne(JJConnectionStatistics_.getId());
		return JJConnectionStatistics_;
	}

}

package com.starit.janjoonweb.domain;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJVersion.class })
public interface JJVersionService {

	// New Generic
	public List<JJVersion> getVersions(boolean onlyActif, boolean withProduct,
			JJProduct product);	
	
	public List<JJTask> getTastksByVersion(JJVersion jJversion) ;
}

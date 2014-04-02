package com.starit.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJVersion.class })
public interface JJVersionService {

	// New Generic
	public List<JJVersion> getVersions(boolean onlyActif, boolean withProduct,
			JJProduct product);

	public List<JJTask> getTastksByVersion(JJVersion jJversion);
}

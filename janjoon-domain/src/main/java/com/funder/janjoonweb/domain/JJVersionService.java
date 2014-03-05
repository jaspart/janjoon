package com.funder.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.funder.janjoonweb.domain.JJVersion.class })
public interface JJVersionService {

	// New Generic
	public List<JJVersion> getVersions(boolean onlyActif, boolean withProduct,
			JJProduct product);
}

package com.starit.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJBuild.class })
public interface JJBuildService {

	public List<JJBuild> getBuilds(JJVersion version, boolean withVersion,
			boolean onlyActif);

	public List<JJBuild> getBuilds(JJProduct product, JJVersion version,
			boolean onlyActif);

	public JJBuild getBuildByName(JJVersion version, String buildName);
	
	public boolean haveAllTestCases(JJVersion version);
}

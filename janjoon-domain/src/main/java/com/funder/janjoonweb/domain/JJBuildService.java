package com.funder.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.funder.janjoonweb.domain.JJBuild.class })
public interface JJBuildService {
	public List<JJBuild> getAllJJBuilds();
}

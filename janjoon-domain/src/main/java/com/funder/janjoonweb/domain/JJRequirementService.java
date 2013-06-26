package com.funder.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.funder.janjoonweb.domain.JJRequirement.class })
public interface JJRequirementService {
	public List<JJRequirement> getAllJJRequirementsWithCategory(String name);
}

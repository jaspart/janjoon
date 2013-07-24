package com.funder.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.funder.janjoonweb.domain.JJRequirement.class })
public interface JJRequirementService {
	public List<JJRequirement> getAllJJRequirementsWithCategory(
			String categoryName);

	public List<JJRequirement> getAllJJRequirementsWithProject(
			String categoryName, JJProject project);

	public List<JJRequirement> getAllJJRequirementsWithProjectAndProduct(
			String categoryName, JJProject project, JJProduct product);

	public List<JJRequirement> getAllJJRequirementsWithProjectAndProductAndVersion(
			String categoryName, JJProject project, JJProduct product,
			JJVersion version);
}

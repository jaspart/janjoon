package com.starit.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJRequirement.class })
public interface JJRequirementService {

	public List<JJRequirement> getAllJJRequirementsWithProject(JJProject project);

	// Generic request

	public List<JJRequirement> getRequirements(JJCategory category,
			JJProject project, JJProduct product, JJVersion version,
			JJStatus status, JJChapter chapter, boolean withChapter,
			boolean onlyActif, boolean orderByCreationdate);
	
	public List<JJRequirement> getRequirements(JJProject project, JJProduct product, JJVersion version);

	public List<JJRequirement> getRequirementChildrenWithChapterSortedByOrder(
			JJChapter chapter, boolean onlyActif);
	
	

}

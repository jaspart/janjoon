package com.starit.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJRequirement.class })
public interface JJRequirementService {

	// Generic request

	public List<JJRequirement> getRequirements(JJCompany company,JJCategory category,
			JJProject project, JJProduct product, JJVersion version,
			JJStatus status, JJChapter chapter, boolean withChapter,
			boolean onlyActif, boolean orderByCreationdate);

	public List<JJRequirement> getRequirements(JJCompany company,JJProject project,
			JJProduct product, JJVersion version);

	public List<JJRequirement> getRequirementChildrenWithChapterSortedByOrder(
			JJCompany company,JJChapter chapter, boolean onlyActif);
	
	public List<JJRequirement> getMineRequirements(JJCompany company,JJContact creator,JJProduct produit,JJProject projet,JJCategory category,JJVersion version,
			boolean onlyActif, boolean orderByCreationdate);

	public List<JJRequirement> getRequirements(JJCompany company,JJStatus status);

	public Long getReqCountByStaus(JJCompany company,JJProject project, JJProduct product,
			JJVersion version,JJStatus status, boolean onlyActif);
	
	public void refreshRequirement(JJRequirement requirement);
	public List<JJRequirement> getNonCouvredRequirements(JJCompany company);

}

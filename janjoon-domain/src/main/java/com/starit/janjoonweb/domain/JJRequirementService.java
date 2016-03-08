package com.starit.janjoonweb.domain;

import java.util.List;
import java.util.Map;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = {com.starit.janjoonweb.domain.JJRequirement.class})
public interface JJRequirementService {

	// Generic request

	public List<JJRequirement> getRequirements(JJCompany company,
			JJCategory category, Map<JJProject, JJProduct> map,
			JJVersion version, JJStatus status, JJChapter chapter,
			boolean withChapter, boolean onlyActif, boolean orderByCreationdate,
			boolean mine, JJContact contact);

	public List<JJRequirement> getRequirementsWithOutChapter(JJCompany company,
			JJCategory category, Map<JJProject, JJProduct> map,
			JJVersion version, JJStatus status, boolean onlyActif,
			boolean orderByCreationdate);

	public boolean haveTestcase(JJRequirement requirement);

	public List<JJRequirement> getRequirements(JJCompany company,
			Map<JJProject, JJProduct> map, JJVersion version,
			JJCategory category);

	public List<JJRequirement> getRequirementChildrenWithChapterSortedByOrder(
			JJCompany company, JJChapter chapter, JJProduct product,
			JJVersion version, boolean onlyActif);

	public List<JJRequirement> getMineRequirements(JJCompany company,
			JJContact creator, Map<JJProject, JJProduct> map,
			JJCategory category, JJVersion version, boolean onlyActif,
			boolean orderByCreationdate);

	public List<JJRequirement> getRequirements(JJCompany company,
			JJStatus status);

	// public List<JJRequirement> getInfinishedRequirement(JJCompany
	// company,Map<JJProject, JJProduct> map,JJVersion version);

	public Long getReqCount(JJCompany company, JJProject project,
			JJProduct product, JJVersion version, JJStatus status,
			JJCategory category, JJStatus state, JJContact createdBy,
			boolean onlyActif);

	public List<JJContact> getReqContacts(JJCompany company,
			Map<JJProject, JJProduct> map, JJVersion version);

	public void refreshRequirement(JJRequirement requirement);

	public JJStatus getRequirementState(JJRequirement requirement,
			JJCompany company);

	public boolean haveLinkUp(JJRequirement requirement);

	public boolean haveLinkDown(JJRequirement requirement);

	public JJRequirement getRequirementByName(JJCategory catgory,
			JJProject project, JJProduct produit, String name,
			JJCompany company);

	public List<JJRequirement> getNonCouvredRequirements(JJCompany company,
			Map<JJProject, JJProduct> map);

}

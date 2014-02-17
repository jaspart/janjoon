package com.funder.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.funder.janjoonweb.domain.JJRequirement.class })
public interface JJRequirementService {
	public List<JJRequirement> getAllJJRequirementsWithCategory(
			String categoryName);

	public List<JJRequirement> getAllJJRequirements();

	public List<JJRequirement> getAllJJRequirementsWithProject(JJProject project);

	public List<JJRequirement> getAllJJRequirementsWithCategoryAndChapter(
			String categoryName);

	public List<JJRequirement> getAllJJRequirementsWithCategoryAndProject(
			String categoryName, JJProject project);

	public List<JJRequirement> getAllJJRequirementsWithProjectAndChapter(
			String categoryName, JJProject project);

	public List<JJRequirement> getAllJJRequirementsWithProjectAndProduct(
			String categoryName, JJProject project, JJProduct product);

	public List<JJRequirement> getAllJJRequirementsWithProjectAndProductAndChapter(
			String categoryName, JJProject project, JJProduct product);

	public List<JJRequirement> getAllJJRequirementsWithProjectAndProductAndVersion(
			String categoryName, JJProject project, JJProduct product,
			JJVersion version);

	public List<JJRequirement> getAllJJRequirementsWithProjectAndProductAndVersionAndChapter(
			String categoryName, JJProject project, JJProduct product,
			JJVersion version);

	// Generic request

	public List<JJRequirement> getRequirements(JJCategory category,
			JJProject project, JJProduct product, JJVersion version,
			JJChapter chapter, boolean withChapter, boolean onlyActif,
			boolean orderByCreationdate);

	public List<JJRequirement> getRequirementChildrenWithChapterSortedByOrder(
			JJChapter chapter, boolean onlyActif);

}

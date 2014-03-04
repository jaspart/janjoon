package com.funder.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.funder.janjoonweb.domain.JJChapter.class })
public interface JJChapterService {

	public List<JJChapter> getAllJJChaptersWithProjectAndCategory(
			JJProject project, JJCategory category);

	public List<JJChapter> getAllParentJJChapterWithProjectAndCategory(
			JJProject project, JJCategory category);

	public List<JJChapter> getAllParentJJChapterWithProjectAndCategorySortedByOrder(
			JJProject project, JJCategory category);

	public List<JJChapter> getAllJJChaptersWithProjectAndCategoryAndParentSortedByOrder(
			JJProject project, JJCategory category, JJChapter parent);

	// New generic
	public List<JJChapter> getParentsChapter(JJProject project,
			JJProduct product, JJCategory category, boolean onlyActif,
			boolean sortedByOrder);

	public List<JJChapter> getChapters(JJProject project, JJProduct product,
			JJCategory category, boolean onlyActif, List<String> ids);

	public List<JJChapter> getChildrenOfParentChapter(JJChapter parent,
			boolean onlyActif, boolean sortedByOrder);

}

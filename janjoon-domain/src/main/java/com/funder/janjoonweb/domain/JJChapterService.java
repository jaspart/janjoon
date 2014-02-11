package com.funder.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.funder.janjoonweb.domain.JJChapter.class })
public interface JJChapterService {
	public List<JJChapter> getAllJJChapter();

	public List<JJChapter> getAllJJChaptersWithCategory(JJCategory category);

	public List<JJChapter> getAllJJChaptersWithProject(JJProject project);

	public List<JJChapter> getAllParentJJChapterWithCategory(JJCategory category);

	public List<JJChapter> getAllJJChaptersWithProjectAndCategory(
			JJProject project, JJCategory category);

	public List<JJChapter> getAllJJChaptersWithProjectAndCategorySortedByOrder(
			JJProject project, JJCategory category);

	public List<JJChapter> getAllParentJJChapterWithProjectAndCategory(
			JJProject project, JJCategory category);

	public List<JJChapter> getAllParentJJChapterWithProjectAndCategorySortedByOrder(
			JJProject project, JJCategory category);

	public List<JJChapter> getAllJJChaptersWithProjectAndProductAndCategory(
			JJProject project, JJProduct product, JJCategory category);

	public List<JJChapter> getAllParentJJChapterWithProjectAndProductAndCategory(
			JJProject project, JJProduct product, JJCategory category);

	public List<JJChapter> getAllParentJJChapterWithProjectAndProductAndCategorySortedByOrder(
			JJProject project, JJProduct product, JJCategory category);

	public List<JJChapter> getAllJJChaptersWithProjectAndCategoryAndParentSortedByOrder(
			JJProject project, JJCategory category, JJChapter parent);

	// public List<JJChapter> getAllChildsJJChapterWithParentSortedByOrder(
	// JJChapter parent);

	// New generic
	public List<JJChapter> getAllParentsJJChapterSortedByOrder(
			JJProject project, JJProduct product, JJCategory category,
			boolean onlyActif);

	public List<JJChapter> getAllJJChapters(JJProject project,
			JJProduct product, JJCategory category);

	public List<JJChapter> getAllChildsJJChapterWithParentSortedByOrder(
			JJChapter parent, boolean onlyActif);

}

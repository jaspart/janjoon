package com.funder.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.funder.janjoonweb.domain.JJChapter.class })
public interface JJChapterService {
	public List<JJChapter> getAllJJChapter();

	public List<JJChapter> getAllJJChaptersWithCategory(JJCategory category);

	public List<JJChapter> getAllParentJJChapterWithCategory(JJCategory category);

	public List<JJChapter> getAllJJChaptersWithProjectAndCategory(
			JJProject project, JJCategory category);

	public List<JJChapter> getAllParentJJChapterWithProjectAndCategory(
			JJProject project, JJCategory category);

	public List<JJChapter> getAllJJChaptersWithProjectAndProductAndCategory(
			JJProject project, JJProduct product, JJCategory category);

	public List<JJChapter> getAllParentJJChapterWithProjectAndProductAndCategory(
			JJProject project, JJProduct product, JJCategory category);
}

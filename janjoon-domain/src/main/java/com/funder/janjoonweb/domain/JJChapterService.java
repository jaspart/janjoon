package com.funder.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.funder.janjoonweb.domain.JJChapter.class })
public interface JJChapterService {
	public List<JJChapter> getAllJJChapter();

	public List<JJChapter> getAllJJChaptersWithCategory(JJCategory category);

	public JJChapter getParentJJChapterWithCategory(JJCategory category);

	public List<JJChapter> getAllJJChaptersWithProjectAndCategory(
			JJProject project, JJCategory category);

	public JJChapter getParentJJChapterWithProjectAndCategory(
			JJProject project, JJCategory category);

	public List<JJChapter> getAllJJChaptersWithProjectAndProductAndCategory(
			JJProject project, JJProduct product, JJCategory category);

	public JJChapter getParentJJChapterWithProjectAndProductAndCategory(
			JJProject project, JJProduct product, JJCategory category);
}

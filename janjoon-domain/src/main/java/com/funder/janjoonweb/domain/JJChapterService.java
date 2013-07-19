package com.funder.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.funder.janjoonweb.domain.JJChapter.class })
public interface JJChapterService {
	public List<JJChapter> getAllJJChapter();

	public List<JJProduct> getAllJJProductInJJChapterWithJJProject(
			JJProject project);
}

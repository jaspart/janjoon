package com.starit.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJChapter.class })
public interface JJChapterService {

	// New generic
	public List<JJChapter> getParentsChapter(JJProject project,
			JJCategory category, boolean onlyActif, boolean sortedByOrder);

	public List<JJChapter> getChapters(JJProject project, JJCategory category,
			boolean onlyActif, List<String> ids);

	public List<JJChapter> getChapters(JJProject project, boolean onlyActif,
			boolean sortedByName);

	public List<JJChapter> getChildrenOfParentChapter(JJChapter parent,
			boolean onlyActif, boolean sortedByOrder);

}

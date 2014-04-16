package com.starit.janjoonweb.domain;

import java.util.List;
import java.util.Set;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJTestcase.class })
public interface JJTestcaseService {
	public List<JJTestcase> getTestcases(JJRequirement requirement,
			JJChapter chapter, boolean onlyActif, boolean sortedByOrder,
			boolean sortedByCreationdate);

	public void saveTestcases(Set<JJTestcase> testcases);

	public void updateTestcases(Set<JJTestcase> testcases);
}

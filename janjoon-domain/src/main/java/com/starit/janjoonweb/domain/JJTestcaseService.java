package com.starit.janjoonweb.domain;

import java.util.List;
import java.util.Set;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJTestcase.class })
public interface JJTestcaseService {
	public List<JJTestcase> getTestcases(JJRequirement requirement,
			JJChapter chapter, boolean onlyActif, boolean sortedByOrder,
			boolean sortedByCreationdate);

	public List<JJTestcase> getImportTestcases(JJCategory category,JJProject project,
			boolean onlyActif);

	public void saveTestcases(Set<JJTestcase> testcases);

	public void updateTestcases(Set<JJTestcase> testcases);
	
	public List<JJTestcase> getJJtestCases(JJRequirement requirement);
	
	public List<String> getReqTesCases(JJRequirement requirement);
}

package com.starit.janjoonweb.domain;

import java.util.List;
import java.util.Set;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = {com.starit.janjoonweb.domain.JJTestcase.class})
public interface JJTestcaseService {

	public List<JJTestcase> getTestcases(JJRequirement requirement,
			JJChapter chapter, JJVersion version, JJBuild build,
			boolean onlyActif, boolean sortedByOrder,
			boolean sortedByCreationdate);

	public List<JJTestcase> getImportTestcases(JJCategory category,
			JJProject project, JJProduct product, JJVersion version,
			JJBuild build, boolean onlyActif, boolean withOutChapter);

	public Long getTestCaseCountByLastResult(JJProject project,
			JJProduct product, JJVersion version, JJBuild build,
			boolean onlyActif, Boolean passed,JJContact executor);
	
	public List<JJContact> getTestCaseContacts(JJProject project, JJProduct product,
			JJVersion version,boolean onlyActif);

	public void saveTestcases(Set<JJTestcase> testcases);

	public void updateTestcases(Set<JJTestcase> testcases);

	public List<JJTestcase> getJJtestCases(JJRequirement requirement);

	public List<String> getReqTesCases(JJRequirement requirement);

	public Integer getMaxOrdering(JJRequirement requirement);
}

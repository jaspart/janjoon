package com.starit.janjoonweb.domain;

import java.util.List;
import java.util.Set;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = {
		com.starit.janjoonweb.domain.JJTestcaseexecution.class})
public interface JJTestcaseexecutionService {

	public List<JJTestcaseexecution> getTestcaseexecutions(JJTestcase testcase,
			JJBuild build, boolean onlyActif, boolean sortedByUpdatedDate,
			boolean sortedByCreationdDate);

	public Set<JJTestcaseexecution> getTestcaseexecutions(JJProject project,
			JJProduct product, JJVersion version, JJCategory category,
			JJChapter chapter, JJBuild build, boolean onlyActif,
			boolean sortedByUpdatedDate, boolean withOutChapter);


	public boolean checkIfSuccess(JJRequirement requirement);

	public boolean checkIfSuccess(JJTestcase testcase);

	public boolean haveTestcaseExec(JJTestcase testcase, JJBuild build,
			JJVersion version);

	public Boolean isPassed(JJTestcase testCase, JJBuild build,
			JJVersion version);
}

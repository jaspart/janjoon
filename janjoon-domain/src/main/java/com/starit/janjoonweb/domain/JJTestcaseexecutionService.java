package com.starit.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJTestcaseexecution.class })
public interface JJTestcaseexecutionService {

	public List<JJTestcaseexecution> getTestcaseexecutions(JJTestcase testcase,
			JJBuild build, boolean onlyActif, boolean orderByCreationdate);
}

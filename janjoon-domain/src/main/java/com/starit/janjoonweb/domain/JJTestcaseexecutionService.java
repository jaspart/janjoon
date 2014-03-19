package com.starit.janjoonweb.domain;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJTestcaseexecution.class })
public interface JJTestcaseexecutionService {

	public JJTestcaseexecution getTestcaseexecution(JJTestcase testcase,
			JJBuild build, boolean onlyActif);
}

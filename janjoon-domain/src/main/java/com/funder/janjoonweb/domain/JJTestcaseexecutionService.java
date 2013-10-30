package com.funder.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.funder.janjoonweb.domain.JJTestcaseexecution.class })
public interface JJTestcaseexecutionService {

	public List<JJTestcaseexecution> getAllTestcaseexecutions();

	public JJTestcaseexecution getTestcaseexecutionWithTestcaseAndBuild(
			JJTestcase testcase, JJBuild build);
}

package com.funder.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.funder.janjoonweb.domain.JJBug.class })
public interface JJBugService {
	public List<JJBug> getAllJJBugs();

	public JJBug getBugWithTestcaseAndProject(JJTestcase testcase,
			JJProject project);
}

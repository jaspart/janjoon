package com.starit.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJBug.class })
public interface JJBugService {
	public List<JJBug> getAllJJBugs();

	public JJBug getBugWithTeststepAndProject(JJTeststep teststep,
			JJProject project);
}

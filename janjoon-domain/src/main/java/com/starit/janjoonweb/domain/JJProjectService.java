package com.starit.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJProject.class })
public interface JJProjectService {
	
	public JJProject getJJProjectWithName(String name);

	// New Generic
	public List<JJProject> getProjects(boolean onlyActif);
}

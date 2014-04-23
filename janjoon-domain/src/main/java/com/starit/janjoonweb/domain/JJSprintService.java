package com.starit.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJSprint.class })
public interface JJSprintService {
	
	public List<JJSprint> getSprints(boolean onlyActif);
	
	public List<JJSprint> getSprintsByProjects(JJProject project,boolean onlyActif);
	
}

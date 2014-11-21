package com.starit.janjoonweb.domain;

import java.util.List;

import org.apache.commons.lang3.mutable.MutableInt;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJProject.class })
public interface JJProjectService {

	// New Generic
	public List<JJProject> getProjects(boolean onlyActif);
	public List<JJProject> load(MutableInt size,int first, int pageSize);
}

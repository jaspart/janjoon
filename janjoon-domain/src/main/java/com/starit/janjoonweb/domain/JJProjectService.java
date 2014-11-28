package com.starit.janjoonweb.domain;

import java.util.List;

import org.apache.commons.lang3.mutable.MutableInt;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJProject.class })
public interface JJProjectService {

	// New Generic
	public List<JJProject> getProjects(JJCompany company,boolean onlyActif);
	public List<JJProject> load(JJCompany company,MutableInt size,int first, int pageSize);
}

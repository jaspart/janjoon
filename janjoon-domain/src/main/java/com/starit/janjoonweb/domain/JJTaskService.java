package com.starit.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJTask.class })
public interface JJTaskService {

	public List<JJTask> getTasks(JJProject project, JJContact contact);
}

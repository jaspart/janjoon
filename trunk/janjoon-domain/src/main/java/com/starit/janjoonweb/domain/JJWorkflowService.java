package com.starit.janjoonweb.domain;

import java.util.List;

import org.apache.commons.lang3.mutable.MutableInt;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJWorkflow.class })
public interface JJWorkflowService {

	public List<JJWorkflow> getObjectWorkFlows(String object, JJStatus source,
			JJStatus target, String actionName, boolean enabled);

	public List<JJWorkflow> load(MutableInt size, int first, int pageSize,
			String object, String action, boolean onlyactif);
}

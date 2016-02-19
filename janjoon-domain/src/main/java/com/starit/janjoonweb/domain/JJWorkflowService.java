package com.starit.janjoonweb.domain;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.model.SortMeta;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = {com.starit.janjoonweb.domain.JJWorkflow.class})
public interface JJWorkflowService {

	public List<JJWorkflow> getObjectWorkFlows(String object, JJStatus source,
			JJStatus target, String actionName, boolean enabled);

	public Set<String> getAllObject();

	public List<JJWorkflow> load(MutableInt size, int first, int pageSize,
			String object, String action, boolean onlyactif,
			List<SortMeta> multiSortMeta, Map<String, Object> filters);
}

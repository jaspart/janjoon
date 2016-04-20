package com.starit.janjoonweb.domain;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.model.SortMeta;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = {com.starit.janjoonweb.domain.JJFlowStep.class})
public interface JJFlowStepService {

	// New Generic

	public JJFlowStep getOneFlowStep(String name, String object,
			boolean onlyActif);

	public Set<String> getAllObject();

	public JJFlowStep getFlowStepByLevel(Integer level, String object,
			boolean onlyActif);

	public List<JJFlowStep> getFlowStep(String object, boolean onlyActif,
			List<String> names, boolean sortedByLevel);

	public List<String> getTablesName();

	public List<JJFlowStep> load(MutableInt size, int first, int pageSize,
			List<SortMeta> multiSortMeta, Map<String, Object> filters);
}

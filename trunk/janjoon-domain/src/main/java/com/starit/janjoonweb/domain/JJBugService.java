package com.starit.janjoonweb.domain;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJBug.class })
public interface JJBugService {
	public List<JJBug> getBugs(JJCompany company,JJProject project, JJTeststep teststep,
			JJBuild build, boolean onlyActif, boolean sortedByCreationDate);

	public List<JJBug> getImportBugs(JJCompany company,JJProject project, JJVersion version,
			JJCategory category, JJStatus status, boolean onlyActif);

	public List<JJBug> getBugs(JJCompany company,JJProject project,JJProduct product,JJVersion version);
	
	public Long requirementBugCount(JJRequirement requirement);	
	
	public List<JJBug> load(JJCompany company,MutableInt size,int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters,JJProject project,JJProduct product,JJVersion version);
}

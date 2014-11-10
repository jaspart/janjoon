package com.starit.janjoonweb.domain;

import java.util.List;
import java.util.Map;

import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJBug.class })
public interface JJBugService {
	public List<JJBug> getBugs(JJProject project, JJTeststep teststep,
			JJBuild build, boolean onlyActif, boolean sortedByCreationDate);

	public List<JJBug> getImportBugs(JJProject project, JJVersion version,
			JJCategory category, JJStatus status, boolean onlyActif);

	public List<JJBug> getBugs(JJProject project);
	
	public List<JJBug> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, String> filters,JJProject project);
}

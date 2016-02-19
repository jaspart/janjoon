package com.starit.janjoonweb.domain;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.model.SortMeta;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = {com.starit.janjoonweb.domain.JJProject.class})
public interface JJProjectService {

	// New Generic
	public List<JJProject> getProjects(JJCompany company, JJContact contact,
			boolean onlyActif, boolean all);

	public List<JJProject> getAdminListProjects();

	public List<JJProject> getProjectList(boolean enabled, JJCompany company,
			JJContact contact);

	public List<JJProject> load(JJCompany company, MutableInt size, int first,
			int pageSize, List<SortMeta> multiSortMeta,
			Map<String, Object> filters);
}

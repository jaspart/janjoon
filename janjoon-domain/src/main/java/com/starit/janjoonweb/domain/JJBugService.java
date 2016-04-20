package com.starit.janjoonweb.domain;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.model.SortMeta;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = {com.starit.janjoonweb.domain.JJBug.class})
public interface JJBugService {

	public List<JJBug> getBugs(JJCompany company, JJProject project,
			JJProduct product, JJTeststep teststep, JJBuild build,
			boolean onlyActif, boolean sortedByCreationDate);

	public List<JJContact> getBugsContacts(JJCompany company, JJProject project,
			JJProduct product, JJVersion version);

	public List<JJStatus> getBugsStatus(JJCompany company, JJProject project,
			JJProduct product, JJVersion version);

	public List<JJBug> getImportBugs(JJCompany company, JJProject project,
			JJProduct product, JJVersion version, JJCategory category,
			JJStatus status, boolean withoutTask, boolean onlyActif);

	public List<JJBug> getBugs(JJCompany company, JJProject project,
			JJProduct product, JJVersion version);

	public Long getBugsCountByStaus(JJCompany company, JJContact createdBy,
			JJProject project, JJProduct product, JJVersion version,
			JJStatus status, boolean onlyActif);

	public Long requirementBugCount(JJRequirement requirement);

	public List<JJBug> getRequirementBugs(JJRequirement requirement,
			JJCompany company, JJProject project, JJProduct product,
			JJVersion version);

	public List<JJBug> load(JJContact contact, JJCompany company,
			MutableInt size, int first, int pageSize,
			List<SortMeta> multiSortMeta, Map<String, Object> filters,
			JJProject project, JJProduct product, JJVersion version);
}

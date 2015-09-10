package com.starit.janjoonweb.domain;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.model.SortMeta;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJCompany.class })
public interface JJCompanyService {

	public List<JJCompany> getActifCompanies();

	public JJCompany getCompanyByName(String name);

	public Long getMaxId();

	public List<JJCompany> load(MutableInt size, int first, int pageSize,
			List<SortMeta> multiSortMeta, Map<String, Object> filters);
}

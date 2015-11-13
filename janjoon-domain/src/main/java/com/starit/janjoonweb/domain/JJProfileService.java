package com.starit.janjoonweb.domain;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.model.SortMeta;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJProfile.class })
public interface JJProfileService {

	public JJProfile getOneProfile(String name, JJCompany company,
			boolean onlyActif);

	public List<JJProfile> getProfiles(boolean onlyActif, boolean isSuperAdmin,
			JJCompany company);

	public List<JJProfile> load(MutableInt size, int first, int pageSize,
			List<SortMeta> multiSortMeta, Map<String, Object> filters,
			boolean isSuperAdmin, JJCompany company);
}

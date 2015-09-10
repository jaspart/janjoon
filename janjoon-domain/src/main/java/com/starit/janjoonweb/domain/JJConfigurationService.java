package com.starit.janjoonweb.domain;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.model.SortMeta;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJConfiguration.class })
public interface JJConfigurationService {

	public List<JJConfiguration> getConfigurations(String name, String param,
			boolean onlyactif);

	public boolean getDialogConfig(String name, String param);

	public List<JJConfiguration> load(MutableInt size, int first, int pageSize,
			String name, String param, boolean onlyactif,
			List<SortMeta> multiSortMeta, Map<String, Object> filters);

}

package com.starit.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJStatus.class })
public interface JJStatusService {

	// New Generic

	public JJStatus getOneStatus(String name, String object, boolean onlyActif);

	public List<JJStatus> getStatus(String object, boolean onlyActif,
			List<String> names, boolean sortedByName);

	public List<String> getTablesName();
}

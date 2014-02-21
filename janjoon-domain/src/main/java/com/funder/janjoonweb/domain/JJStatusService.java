package com.funder.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.funder.janjoonweb.domain.JJStatus.class })
public interface JJStatusService {

	public List<JJStatus> getAllJJStatuses();

	public JJStatus getJJStatusWithName(String name);

	// New Generic

	public List<JJStatus> getStatusSortedByName(boolean onlyActif,
			List<String> names);
}

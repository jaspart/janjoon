package com.funder.janjoonweb.domain;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.funder.janjoonweb.domain.JJStatus.class })
public interface JJStatusService {

	public JJStatus getJJStatusWithName(String name);
}

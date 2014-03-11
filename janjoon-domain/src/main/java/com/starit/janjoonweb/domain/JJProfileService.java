package com.starit.janjoonweb.domain;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJProfile.class })
public interface JJProfileService {
	public JJProfile getJJProfileWithName(String name);
}

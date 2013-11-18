package com.funder.janjoonweb.domain;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.funder.janjoonweb.domain.JJProfile.class })
public interface JJProfileService {
	public JJProfile getJJProfileWithName(String name);
}

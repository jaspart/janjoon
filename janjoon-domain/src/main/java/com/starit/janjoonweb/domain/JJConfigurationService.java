package com.starit.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJConfiguration.class })
public interface JJConfigurationService {

	public List<JJConfiguration> getConfigurations(String name, String param,
			boolean onlyactif);

}

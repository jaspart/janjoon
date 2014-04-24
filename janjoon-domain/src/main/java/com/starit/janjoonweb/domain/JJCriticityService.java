package com.starit.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJCriticity.class })
public interface JJCriticityService {

	public List<JJCriticity> getCriticities(String object, boolean onlyActif);

	public JJCriticity getCriticityByName(String name, boolean onlyActif);
}

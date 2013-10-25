package com.funder.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.funder.janjoonweb.domain.JJSprint.class })
public interface JJSprintService {
	public List<JJSprint> getAllJJSprints();
}

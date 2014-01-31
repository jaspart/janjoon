package com.funder.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.funder.janjoonweb.domain.JJVersion.class })
public interface JJVersionService {
	public List<JJVersion> getAllJJVersionsWithProduct(JJProduct product);

	public List<JJVersion> getAllJJVersion();

	public List<JJVersion> getAllJJVersionWithoutProduct();
}

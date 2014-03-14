package com.starit.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJRight.class })
public interface JJRightService {

	public List<JJRight> getRights(JJProfile profile);

	public List<String> getTablesName();

}

package com.starit.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = {com.starit.janjoonweb.domain.JJImportance.class})
public interface JJImportanceService {

	public List<JJImportance> getBugImportance();
}

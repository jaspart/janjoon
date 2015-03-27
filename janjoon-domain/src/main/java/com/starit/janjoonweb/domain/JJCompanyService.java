package com.starit.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJCompany.class })
public interface JJCompanyService {
	
	public List<JJCompany> getActifCompanies();
	
	public JJCompany getCompanyByName(String name);
	
	public Long getMaxId();
}

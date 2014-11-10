package com.starit.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJProduct.class })
public interface JJProductService {
	// New Generic
	public List<JJProduct> getProducts(boolean onlyActif);
	
	public List<JJProduct> load(int first, int pageSize);

	public JJProduct getJJProductWithName(String name);
}

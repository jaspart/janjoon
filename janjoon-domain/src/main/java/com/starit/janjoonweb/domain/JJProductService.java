package com.starit.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJProduct.class })
public interface JJProductService {
	// New Generic
	public List<JJProduct> getProducts(boolean onlyActif);
}

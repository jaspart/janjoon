package com.starit.janjoonweb.domain;

import java.util.List;

import org.apache.commons.lang3.mutable.MutableInt;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJProduct.class })
public interface JJProductService {
	// New Generic
	public List<JJProduct> getProducts(JJCompany company,JJContact contact,boolean onlyActif);
	
	public List<JJProduct> load(JJCompany company,MutableInt size,int first, int pageSize);

	public JJProduct getJJProductWithName(String name);
	
	public List<JJProduct> getAdminListProducts();
}

package com.starit.janjoonweb.domain;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.model.SortMeta;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJProduct.class })
public interface JJProductService {
	// New Generic
	public List<JJProduct> getProducts(JJCompany company, JJContact contact,
			boolean onlyActif, boolean all);

	public List<JJProduct> load(JJCompany company, MutableInt size, int first,
			int pageSize, List<SortMeta> multiSortMeta,
			Map<String, Object> filters);

	public JJProduct getJJProductWithName(String name);

	public List<JJProduct> getAdminListProducts();

	public List<JJProduct> getProducts(JJCompany company, JJProject project);
}

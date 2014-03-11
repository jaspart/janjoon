package com.starit.janjoonweb.domain;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJCategory.class })
public interface JJCategoryService {
	public JJCategory getCategoryWithName(String name, boolean onlyActif);

	public List<JJCategory> getCategories(String name, boolean withName,
			boolean onlyActif, boolean sortedByStage);
}
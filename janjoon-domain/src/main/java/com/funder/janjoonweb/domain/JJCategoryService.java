package com.funder.janjoonweb.domain;

import java.util.List;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.funder.janjoonweb.domain.JJCategory.class })
public interface JJCategoryService {
	public JJCategory getJJCategoryWithName(String name);
	public List<JJCategory> getAllJJCategories();
}

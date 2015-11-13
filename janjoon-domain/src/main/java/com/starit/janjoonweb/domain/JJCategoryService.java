package com.starit.janjoonweb.domain;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.model.SortMeta;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJCategory.class })
public interface JJCategoryService {

	public JJCategory getCategory(String name, JJCompany company,
			boolean onlyActif);

	public List<JJCategory> getCategories(String name, boolean withName,
			boolean onlyActif, boolean sortedByStage, JJCompany company);

	public List<JJCategory> load(MutableInt size, int first, int pageSize,
			List<SortMeta> multiSortMeta, Map<String, Object> filters,
			JJCompany company);

	public boolean isHighLevel(JJCategory category, JJCompany company);

	public boolean isLowLevel(JJCategory category, JJCompany company);

	public boolean nameExist(String name, Long id, JJCompany company);
}

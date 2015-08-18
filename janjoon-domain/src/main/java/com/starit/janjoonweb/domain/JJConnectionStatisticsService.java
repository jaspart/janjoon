package com.starit.janjoonweb.domain;

import java.util.List;

import org.apache.commons.lang3.mutable.MutableInt;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJConnectionStatistics.class })
public interface JJConnectionStatisticsService {

	List<JJConnectionStatistics> load(JJCompany company, MutableInt size,
			int first, int pageSize);

}

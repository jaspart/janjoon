package com.starit.janjoonweb.ui.mb;

import java.util.Date;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJCompany;
import com.starit.janjoonweb.domain.JJConnectionStatistics;
import com.starit.janjoonweb.ui.mb.lazyLoadingDataTable.LazyConnectionStatistiquesDataModel;

@RooSerializable
@RooJsfManagedBean(entity = JJConnectionStatistics.class, beanName = "jJConnectionStatisticsBean")
public class JJConnectionStatisticsBean {

	public void saveJJConnectionStatistics(JJConnectionStatistics b) {
		b.setCreationDate(new Date());
		jJConnectionStatisticsService.saveJJConnectionStatistics(b);
	}

	public void updateJJConnectionStatistics(JJConnectionStatistics b) {
		jJConnectionStatisticsService.updateJJConnectionStatistics(b);
	}
}

package com.starit.janjoonweb.ui.mb;

import java.io.Serializable;
import java.util.Date;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;

import com.starit.janjoonweb.domain.JJConnectionStatistics;

@RooJsfManagedBean(entity = JJConnectionStatistics.class, beanName = "jJConnectionStatisticsBean")
public class JJConnectionStatisticsBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void saveJJConnectionStatistics(JJConnectionStatistics b) {
		b.setCreationDate(new Date());
		jJConnectionStatisticsService.saveJJConnectionStatistics(b);
	}

	public void updateJJConnectionStatistics(JJConnectionStatistics b) {
		jJConnectionStatisticsService.updateJJConnectionStatistics(b);
	}
}

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
	
	public LazyConnectionStatistiquesDataModel getConnectionStatistiquesListTable() {

		LoginBean loginBean = (LoginBean) LoginBean.findBean("loginBean");
		JJCompany company = null;
		if (!loginBean.getAuthorisationService().isAdminCompany())
			company = loginBean.getContact().getCompany();
		LazyConnectionStatistiquesDataModel connectionStatistiquesListTable = null;
		if (connectionStatistiquesListTable == null)
			connectionStatistiquesListTable = new LazyConnectionStatistiquesDataModel(
					jJConnectionStatisticsService, company);
		return connectionStatistiquesListTable;
	}

	public void saveJJConnectionStatistics(JJConnectionStatistics b) {
		b.setCreationDate(new Date());
		jJConnectionStatisticsService.saveJJConnectionStatistics(b);
	}

	public void updateJJConnectionStatistics(JJConnectionStatistics b) {
		jJConnectionStatisticsService.updateJJConnectionStatistics(b);
	}
}

package com.starit.janjoonweb.ui.mb;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJAuditLog;
import com.starit.janjoonweb.domain.JJCompany;
import com.starit.janjoonweb.ui.mb.lazyLoadingDataTable.LazyConnectionStatistiquesDataModel;

@RooSerializable
@RooJsfManagedBean(entity = JJAuditLog.class, beanName = "jJAuditLogBean")
public class JJAuditLogBean {

	private LazyConnectionStatistiquesDataModel connectionStatistiquesListTable;

	public LazyConnectionStatistiquesDataModel getConnectionStatistiquesListTable() {

		// connectionStatistiquesListTable = null;
		if (connectionStatistiquesListTable == null) {
			LoginBean loginBean = (LoginBean) LoginBean.findBean("loginBean");
			JJCompany company = null;
			if (!loginBean.getAuthorisationService().isAdminCompany())
				company = LoginBean.getCompany();
			connectionStatistiquesListTable = new LazyConnectionStatistiquesDataModel(
					jJAuditLogService, company);
		}
		return connectionStatistiquesListTable;
	}

	public void setConnectionStatistiquesListTable(
			LazyConnectionStatistiquesDataModel connectionStatistiquesListTable) {
		this.connectionStatistiquesListTable = connectionStatistiquesListTable;
	}

	public void saveJJAuditLog(JJAuditLog b) {
		jJAuditLogService.saveJJAuditLog(b);
	}

	public void updateJJAuditLog(JJAuditLog b) {

		jJAuditLogService.updateJJAuditLog(b);
	}
}

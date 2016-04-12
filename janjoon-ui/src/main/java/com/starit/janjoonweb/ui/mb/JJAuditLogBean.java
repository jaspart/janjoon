package com.starit.janjoonweb.ui.mb;

import java.io.Serializable;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;

import com.starit.janjoonweb.domain.JJAuditLog;
import com.starit.janjoonweb.domain.JJCompany;
import com.starit.janjoonweb.ui.mb.lazyLoadingDataTable.LazyConnectionStatistiquesDataModel;

@RooJsfManagedBean(entity = JJAuditLog.class, beanName = "jJAuditLogBean")
public class JJAuditLogBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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

package com.starit.janjoonweb.ui.mb;

import java.util.Date;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJAuditLog;
import com.starit.janjoonweb.domain.JJCompany;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.ui.mb.lazyLoadingDataTable.LazyConnectionStatistiquesDataModel;

@RooSerializable
@RooJsfManagedBean(entity = JJAuditLog.class, beanName = "jJAuditLogBean")
public class JJAuditLogBean {

	
	private LazyConnectionStatistiquesDataModel connectionStatistiquesListTable;
	
	public LazyConnectionStatistiquesDataModel getConnectionStatistiquesListTable() {

		LoginBean loginBean = (LoginBean) LoginBean.findBean("loginBean");
		JJCompany company = null;
		if (!loginBean.getAuthorisationService().isAdminCompany())
			company = loginBean.getContact().getCompany();
		connectionStatistiquesListTable = null;
		if (connectionStatistiquesListTable == null)
			connectionStatistiquesListTable = new LazyConnectionStatistiquesDataModel(
					jJAuditLogService, company);
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

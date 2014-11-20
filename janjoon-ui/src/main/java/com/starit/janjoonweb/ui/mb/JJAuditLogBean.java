package com.starit.janjoonweb.ui.mb;

import java.util.Date;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJAuditLog;
import com.starit.janjoonweb.domain.JJContact;

@RooSerializable
@RooJsfManagedBean(entity = JJAuditLog.class, beanName = "jJAuditLogBean")
public class JJAuditLogBean {
	
	public void saveJJAuditLog(JJAuditLog b)
	{	
		jJAuditLogService.saveJJAuditLog(b);
	}
	
	public void updateJJAuditLog(JJAuditLog b)
	{
		
		jJAuditLogService.updateJJAuditLog(b);
	}
}

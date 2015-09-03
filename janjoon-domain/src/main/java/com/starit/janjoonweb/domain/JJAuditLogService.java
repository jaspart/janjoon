package com.starit.janjoonweb.domain;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.model.SortMeta;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.starit.janjoonweb.domain.JJAuditLog.class })
public interface JJAuditLogService {
	
	public List<JJAuditLog> getAuditLogByObject(String object,JJContact contact,JJCompany company,String keyName,
			int first,int pageSize,MutableInt size, List<SortMeta> multiSortMeta,
			Map<String, Object> filters);
	 
	public JJAuditLog getLogoutAuditLog(JJContact contact,Date loginDate);
}

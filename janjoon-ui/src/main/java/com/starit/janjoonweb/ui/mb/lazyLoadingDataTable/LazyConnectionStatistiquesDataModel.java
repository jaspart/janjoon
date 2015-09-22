package com.starit.janjoonweb.ui.mb.lazyLoadingDataTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Join;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import com.starit.janjoonweb.domain.JJAuditLog;
import com.starit.janjoonweb.domain.JJAuditLogService;
import com.starit.janjoonweb.domain.JJCompany;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.ui.mb.util.ConnectionStatistics;

public class LazyConnectionStatistiquesDataModel extends
		LazyDataModel<ConnectionStatistics> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JJAuditLogService jJAuditLogService;
	private JJCompany company;

	public LazyConnectionStatistiquesDataModel(
			JJAuditLogService auditLogService, JJCompany company) {

		this.company = company;

		this.jJAuditLogService = auditLogService;
	}

	public JJCompany getCompany() {
		return company;
	}

	public void setCompany(JJCompany company) {
		this.company = company;
	}

	@Override
	public ConnectionStatistics getRowData(String rowKey) {

		return getConnectionStatic(jJAuditLogService.findJJAuditLog(Long
				.parseLong(rowKey)));
	}

	@Override
	public Object getRowKey(ConnectionStatistics connectionStatistics) {

		return connectionStatistics.getId();
	}

	@Override
	public List<ConnectionStatistics> load(int first, int pageSize,
			List<SortMeta> multiSortMeta, Map<String, Object> filters) {

		List<ConnectionStatistics> data = new ArrayList<ConnectionStatistics>();
		MutableInt size = new MutableInt(0);
		
		data = load(size, first, pageSize, multiSortMeta, filters);
		setRowCount(size.getValue());		
		int dataSize = data.size();

		if (dataSize > pageSize) {
			try {
				return data.subList(first, first + pageSize);
			} catch (IndexOutOfBoundsException e) {
				return data.subList(first, first + (dataSize % pageSize));
			}
		} else {
			return data;
		}
	}

	public List<ConnectionStatistics> load(MutableInt size, int first,
			int pageSize, List<SortMeta> multiSortMeta,
			Map<String, Object> filters) {

		List<JJAuditLog> loginLogs = jJAuditLogService.getAuditLogByObject(
				"JJContact", null, company, ConnectionStatistics.LOGIN_OBJECT,
				first, pageSize, size, multiSortMeta, filters);
		List<ConnectionStatistics> connectionStatistics = new ArrayList<ConnectionStatistics>();

		for (JJAuditLog loginLog : loginLogs) {
			ConnectionStatistics connStat = new ConnectionStatistics(loginLog,
					jJAuditLogService.getLogoutAuditLog(loginLog.getContact(),
							loginLog.getAuditLogDate()));
			if (connStat.getContact() != null)
				connectionStatistics.add(connStat);
		}

		return connectionStatistics;

	}

	public ConnectionStatistics getConnectionStatic(JJAuditLog loginLog) {
		ConnectionStatistics connStat = new ConnectionStatistics(loginLog,
				jJAuditLogService.getLogoutAuditLog(loginLog.getContact(),
						loginLog.getAuditLogDate()));
		return connStat;
	}

}

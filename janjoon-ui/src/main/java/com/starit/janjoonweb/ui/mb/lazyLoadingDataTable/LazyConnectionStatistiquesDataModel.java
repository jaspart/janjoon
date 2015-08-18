package com.starit.janjoonweb.ui.mb.lazyLoadingDataTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.starit.janjoonweb.domain.JJCompany;
import com.starit.janjoonweb.domain.JJConnectionStatistics;
import com.starit.janjoonweb.domain.JJConnectionStatisticsService;
import com.starit.janjoonweb.domain.JJProduct;

public class LazyConnectionStatistiquesDataModel extends LazyDataModel<JJConnectionStatistics>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JJConnectionStatisticsService connectionStatisticsService;
	private JJCompany company;

	public LazyConnectionStatistiquesDataModel(JJConnectionStatisticsService connectionStatisticsService,
			JJCompany company) {

		this.company = company;

		this.connectionStatisticsService = connectionStatisticsService;
	}

	public JJConnectionStatisticsService getProductService() {
		return connectionStatisticsService;
	}

	public void setProductService(JJConnectionStatisticsService connectionStatisticsService) {
		this.connectionStatisticsService = connectionStatisticsService;
	}

	public JJCompany getCompany() {
		return company;
	}

	public void setCompany(JJCompany company) {
		this.company = company;
	}

	@Override
	public JJConnectionStatistics getRowData(String rowKey) {

		return connectionStatisticsService.findJJConnectionStatistics(Long.parseLong(rowKey));
	}

	@Override
	public Object getRowKey(JJConnectionStatistics connectionStatistics) {
		return connectionStatistics.getId();
	}

	@Override
	public List<JJConnectionStatistics> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, Object> filters) {

		List<JJConnectionStatistics> data = new ArrayList<JJConnectionStatistics>();
		MutableInt size = new MutableInt(0);
		data = connectionStatisticsService.load(company, size, first, pageSize);
		setRowCount(size.getValue());
		System.err.println("SIZE :" + data.size());

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

}

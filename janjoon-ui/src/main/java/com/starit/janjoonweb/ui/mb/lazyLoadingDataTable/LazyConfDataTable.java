package com.starit.janjoonweb.ui.mb.lazyLoadingDataTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.starit.janjoonweb.domain.JJConfiguration;
import com.starit.janjoonweb.domain.JJConfigurationService;

public class LazyConfDataTable extends LazyDataModel<JJConfiguration> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JJConfigurationService configurationService;	

	public LazyConfDataTable(JJConfigurationService configurationService) {
		
		this.configurationService = configurationService;
	}

	@Override
	public JJConfiguration getRowData(String rowKey) {

		return configurationService.findJJConfiguration(Long.parseLong(rowKey));
	}

	@Override
	public Object getRowKey(JJConfiguration bug) {
		return bug.getId();
	}

	@Override
	public List<JJConfiguration> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,String> filters) {

		List<JJConfiguration> data = new ArrayList<JJConfiguration>();

		data = configurationService.load(first, pageSize, null, null, true);
		setRowCount(configurationService.getConfigurations(null, null, true).size());
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
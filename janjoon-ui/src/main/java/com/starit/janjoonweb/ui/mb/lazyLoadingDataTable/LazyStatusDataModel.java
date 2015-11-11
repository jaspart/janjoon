package com.starit.janjoonweb.ui.mb.lazyLoadingDataTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import com.starit.janjoonweb.domain.JJStatus;
import com.starit.janjoonweb.domain.JJStatusService;

public class LazyStatusDataModel extends LazyDataModel<JJStatus> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JJStatusService statusService;

	public LazyStatusDataModel(JJStatusService statusService) {

		this.statusService = statusService;
	}

	@Override
	public JJStatus getRowData(String rowKey) {

		return statusService.findJJStatus(Long.parseLong(rowKey));
	}

	@Override
	public Object getRowKey(JJStatus bug) {
		return bug.getId();
	}

	@Override
	public List<JJStatus> load(int first, int pageSize,
			List<SortMeta> multiSortMeta, Map<String, Object> filters) {

		List<JJStatus> data = new ArrayList<JJStatus>();
		MutableInt size = new MutableInt(0);
		data = statusService
				.load(size, first, pageSize, multiSortMeta, filters);
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
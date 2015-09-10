package com.starit.janjoonweb.ui.mb.lazyLoadingDataTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import com.starit.janjoonweb.domain.JJWorkflow;
import com.starit.janjoonweb.domain.JJWorkflowService;

public class LazyWorkFlowDataTable extends LazyDataModel<JJWorkflow> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JJWorkflowService workFlowService;

	public LazyWorkFlowDataTable(JJWorkflowService workFlowService) {

		this.workFlowService = workFlowService;
	}

	@Override
	public JJWorkflow getRowData(String rowKey) {

		return workFlowService.findJJWorkflow(Long.parseLong(rowKey));
	}

	@Override
	public Object getRowKey(JJWorkflow bug) {
		return bug.getId();
	}

	@Override
	public List<JJWorkflow> load(int first, int pageSize,
			List<SortMeta> multiSortMeta, Map<String, Object> filters) {

		List<JJWorkflow> data = new ArrayList<JJWorkflow>();
		MutableInt size = new MutableInt(0);
		data = workFlowService.load(size, first, pageSize, null, null, true,multiSortMeta,filters);
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
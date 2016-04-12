package com.starit.janjoonweb.ui.mb.lazyLoadingDataTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import com.starit.janjoonweb.domain.JJFlowStep;
import com.starit.janjoonweb.domain.JJFlowStepService;

public class LazyFlowStepDataModel extends LazyDataModel<JJFlowStep> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JJFlowStepService flowStepService;

	public LazyFlowStepDataModel(JJFlowStepService flowStepService) {

		this.flowStepService = flowStepService;
	}

	@Override
	public JJFlowStep getRowData(String rowKey) {

		return flowStepService.findJJFlowStep(Long.parseLong(rowKey));
	}

	@Override
	public Object getRowKey(JJFlowStep bug) {
		return bug.getId();
	}

	@Override
	public List<JJFlowStep> load(int first, int pageSize,
			List<SortMeta> multiSortMeta, Map<String, Object> filters) {

		List<JJFlowStep> data = new ArrayList<JJFlowStep>();
		MutableInt size = new MutableInt(0);
		data = flowStepService.load(size, first, pageSize, multiSortMeta,
				filters);
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

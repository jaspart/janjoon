package com.starit.janjoonweb.ui.mb.lazyLoadingDataTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import com.starit.janjoonweb.domain.*;

public class LazyCategoryDataTable extends LazyDataModel<JJCategory> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JJCategoryService jJCategoryService;
	JJCompany company;

	public LazyCategoryDataTable(JJCategoryService jJCategoryService,
			JJCompany company) {

		this.jJCategoryService = jJCategoryService;
		this.company = company;
	}

	@Override
	public JJCategory getRowData(String rowKey) {

		return jJCategoryService.findJJCategory(Long.parseLong(rowKey));
	}

	@Override
	public Object getRowKey(JJCategory bug) {
		return bug.getId();
	}

	@Override
	public List<JJCategory> load(int first, int pageSize,
			List<SortMeta> multiSortMeta, Map<String, Object> filters) {

		List<JJCategory> data = new ArrayList<JJCategory>();
		MutableInt size = new MutableInt(0);
		data = jJCategoryService.load(size, first, pageSize, multiSortMeta,
				filters, company);
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
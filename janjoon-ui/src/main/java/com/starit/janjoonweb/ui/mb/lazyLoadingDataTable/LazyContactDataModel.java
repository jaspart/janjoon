package com.starit.janjoonweb.ui.mb.lazyLoadingDataTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJContactService;

public class LazyContactDataModel extends LazyDataModel<JJContact> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JJContactService contactService;	

	public LazyContactDataModel(JJContactService contactService) {
		
		this.contactService = contactService;
	}

	@Override
	public JJContact getRowData(String rowKey) {

		return contactService.findJJContact(Long.parseLong(rowKey));
	}

	@Override
	public Object getRowKey(JJContact bug) {
		return bug.getId();
	}

	@Override
	public List<JJContact> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,String> filters) {

		List<JJContact> data = new ArrayList<JJContact>();
		MutableInt size=new MutableInt(0);
		data = contactService.load(size,first, pageSize);
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
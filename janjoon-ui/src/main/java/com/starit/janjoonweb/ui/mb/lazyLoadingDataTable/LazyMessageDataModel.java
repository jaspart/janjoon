package com.starit.janjoonweb.ui.mb.lazyLoadingDataTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import com.starit.janjoonweb.domain.JJCompany;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJMessage;
import com.starit.janjoonweb.domain.JJMessageService;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProject;

public class LazyMessageDataModel extends LazyDataModel<JJMessage> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JJMessageService messageService;
	JJProject project;
	JJProduct product;
	JJCompany company;
	JJContact contact;

	public LazyMessageDataModel(JJMessageService messageService, JJProject project,JJProduct product,JJCompany company,JJContact contact) {
		this.project = project;
		this.product=product;
		this.company=company;
		this.contact=contact;
		this.messageService = messageService;
	}

	@Override
	public JJMessage getRowData(String rowKey) {

		return messageService.findJJMessage(Long.parseLong(rowKey));
	}

	@Override
	public Object getRowKey(JJMessage bug) {
		return bug.getId();
	}
	
	@Override
	public List<JJMessage> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, Object> filters) {

		List<JJMessage> data = new ArrayList<JJMessage>();

		List<SortMeta> multiSortMeta =null;
		if(sortOrder != null && sortField != null)
		{
			multiSortMeta=new ArrayList<SortMeta>();
			SortMeta sort=new SortMeta();
			sort.setSortField(sortField);
			sort.setSortOrder(sortOrder);
			multiSortMeta.add(sort);
		}
		
		MutableInt size=new MutableInt(0);
		data = messageService.getActifMessages(size,first, pageSize, multiSortMeta,filters, project,product,company,contact);
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

	@Override
	public List<JJMessage> load(int first, int pageSize,
			List<SortMeta> multiSortMeta, Map<String, Object> filters) {

		List<JJMessage> data = new ArrayList<JJMessage>();
		MutableInt size=new MutableInt(0);
		data = messageService.getActifMessages(size,first, pageSize, multiSortMeta,filters, project,product,company,contact);
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

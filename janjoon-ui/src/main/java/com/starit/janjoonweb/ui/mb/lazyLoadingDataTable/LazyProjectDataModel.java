package com.starit.janjoonweb.ui.mb.lazyLoadingDataTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.starit.janjoonweb.domain.JJCompany;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJProjectService;

public class LazyProjectDataModel extends LazyDataModel<JJProject> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JJProjectService projectService;	
	private JJCompany company;

	public LazyProjectDataModel(JJProjectService projectService,JJCompany company) {
		
		this.company=company;			
		this.projectService = projectService;
	}

	public JJProjectService getProjectService() {
		return projectService;
	}

	public void setProjectService(JJProjectService projectService) {
		this.projectService = projectService;
	}

	public JJCompany getCompany() {
		return company;
	}

	public void setCompany(JJCompany company) {
		this.company = company;
	}

	@Override
	public JJProject getRowData(String rowKey) {

		return projectService.findJJProject(Long.parseLong(rowKey));
	}

	@Override
	public Object getRowKey(JJProject bug) {
		return bug.getId();
	}

	@Override
	public List<JJProject> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,String> filters) {

		List<JJProject> data = new ArrayList<JJProject>();
		MutableInt size=new MutableInt(0);
		data = projectService.load(company,size,first, pageSize);
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
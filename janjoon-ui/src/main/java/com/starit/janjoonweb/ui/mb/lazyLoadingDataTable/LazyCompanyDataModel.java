package com.starit.janjoonweb.ui.mb.lazyLoadingDataTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import com.starit.janjoonweb.domain.JJCompany;
import com.starit.janjoonweb.domain.JJCompanyService;
import com.starit.janjoonweb.ui.mb.LoginBean;

public class LazyCompanyDataModel extends LazyDataModel<JJCompany> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JJCompanyService jJCompanyService;

	public LazyCompanyDataModel(JJCompanyService jJCompanyService) {

		this.jJCompanyService = jJCompanyService;
	}

	public JJCompanyService getjJCompanyService() {
		return jJCompanyService;
	}

	public void setjJCompanyService(JJCompanyService jJCompanyService) {
		this.jJCompanyService = jJCompanyService;
	}

	@Override
	public JJCompany getRowData(String rowKey) {

		return jJCompanyService.findJJCompany(Long.parseLong(rowKey));
	}

	@Override
	public Object getRowKey(JJCompany company) {
		return company.getId();
	}

	@Override
	public List<JJCompany> load(int first, int pageSize,
			List<SortMeta> multiSortMeta, Map<String, Object> filters) {

		System.err.println("FIRST " + first);
		List<JJCompany> data = new ArrayList<JJCompany>();
		MutableInt size = new MutableInt(1);
		if (((LoginBean) LoginBean.findBean("loginBean"))
				.getAuthorisationService().isAdminCompany())
			data = jJCompanyService.load(size, first, pageSize, multiSortMeta,
					filters);
		else
			data.add(LoginBean.getCompany());

		System.err.println("SIZE :" + data.size());
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

}

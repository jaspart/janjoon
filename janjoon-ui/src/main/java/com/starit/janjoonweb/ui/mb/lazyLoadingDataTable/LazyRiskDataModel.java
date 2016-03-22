package com.starit.janjoonweb.ui.mb.lazyLoadingDataTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.component.inputswitch.InputSwitch;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJRisk;
import com.starit.janjoonweb.domain.JJRiskService;
import com.starit.janjoonweb.ui.mb.LoginBean;

public class LazyRiskDataModel extends LazyDataModel<JJRisk> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JJRiskService jJRiskService;
	private JJProject project;
	private JJProduct product;

	public LazyRiskDataModel(JJRiskService jJRiskService, JJProject project,
			JJProduct product) {
		this.jJRiskService = jJRiskService;
		this.project = project;
		this.product = product;
	}
	

	public JJProject getProject() {
		return project;
	}



	public void setProject(JJProject project) {
		this.project = project;
	}



	public JJProduct getProduct() {
		return product;
	}



	public void setProduct(JJProduct product) {
		this.product = product;
	}



	@Override
	public JJRisk getRowData(String rowKey) {

		return jJRiskService.findJJRisk(Long.parseLong(rowKey));
	}

	@Override
	public Object getRowKey(JJRisk risk) {
		return risk.getId();
	}

	@Override
	public List<JJRisk> load(int first, int pageSize,
			List<SortMeta> multiSortMeta, Map<String, Object> filters) {

		List<JJRisk> data = new ArrayList<JJRisk>();
		MutableInt size = new MutableInt(0);

		data = jJRiskService.load(size, first, pageSize, multiSortMeta, filters,
				project, product, LoginBean.getCompany());
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

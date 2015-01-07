package com.starit.janjoonweb.ui.mb.lazyLoadingDataTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJBugService;
import com.starit.janjoonweb.domain.JJCompany;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.ui.mb.JJBugBean;
import com.starit.janjoonweb.ui.mb.JJProjectBean;
import com.starit.janjoonweb.ui.mb.LoginBean;

public class LazyBugDataModel extends LazyDataModel<JJBug> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JJBugService bugService;
	JJProject project;
	JJProduct product;
	JJVersion version;	

	public LazyBugDataModel(JJBugService bugService, JJProject project,JJProduct product,JJVersion version) {
		this.project = project;
		this.product=product;
		this.version=version;
		this.bugService = bugService;
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

	public JJVersion getVersion() {
		return version;
	}

	public void setVersion(JJVersion version) {
		this.version = version;
	}

	@Override
	public JJBug getRowData(String rowKey) {

		return bugService.findJJBug(Long.parseLong(rowKey));
	}

	@Override
	public Object getRowKey(JJBug bug) {
		return bug.getId();
	}

	@Override
	public List<JJBug> load(int first, int pageSize,
			List<SortMeta> multiSortMeta, Map<String, Object> filters) {

		System.err.println("FIRST "+first);
		List<JJBug> data = new ArrayList<JJBug>();		
		MutableInt size=new MutableInt(0);
		data = bugService.load(((LoginBean) LoginBean.findBean("loginBean")).getContact().getCompany(),
				size,first, pageSize, multiSortMeta,filters, project,product,version);
			setRowCount(size.getValue());
			System.err.println("SIZE :" + data.size());

			int dataSize = data.size();			
			((HttpSession) FacesContext.getCurrentInstance().getExternalContext().
			getSession(false)).setAttribute("bugDataTableOptions",new BugDataTableOptions(first, multiSortMeta, filters));
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

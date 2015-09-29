package com.starit.janjoonweb.ui.mb.lazyLoadingDataTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJBugService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJVersion;
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

	public LazyBugDataModel(JJBugService bugService, JJProject project,
			JJProduct product, JJVersion version) {
		this.project = project;
		this.product = product;
		this.version = version;
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

		System.err.println("FIRST " + first);
		List<JJBug> data = new ArrayList<JJBug>();
		MutableInt size = new MutableInt(0);
		SelectOneMenu mineComponent = (SelectOneMenu) LoginBean
				.findComponent("mineBugs");
		JJContact contact=null;		
		System.out.println("VALUE: "+mineComponent.getValue());
		if(mineComponent.getValue() instanceof Boolean)
		{
			Boolean mine= (Boolean) mineComponent.getValue();
			if(mine != null && mine)
				contact=((LoginBean) LoginBean.findBean("loginBean"))
						.getContact();
			else if(mine == null)
			{
				BugDataTableOptions bugDataTableOptions = ((BugDataTableOptions) LoginBean.findBean("bugDataTableOptions"));
				if(bugDataTableOptions.isMine())
					contact=((LoginBean) LoginBean.findBean("loginBean"))
					.getContact();
			}
		}else if(mineComponent.getValue() instanceof String)
		{
			String mine= (String) mineComponent.getValue();
			if(mine != null && mine.equalsIgnoreCase("true"))
				contact=((LoginBean) LoginBean.findBean("loginBean"))
						.getContact();
			else if(mine == null)
			{
				BugDataTableOptions bugDataTableOptions = ((BugDataTableOptions) LoginBean.findBean("bugDataTableOptions"));
				if(bugDataTableOptions.isMine())
					contact=((LoginBean) LoginBean.findBean("loginBean"))
					.getContact();
			}
		}
		
			
		data = bugService.load(contact,((LoginBean) LoginBean.findBean("loginBean"))
				.getContact().getCompany(), size, first, pageSize,
				multiSortMeta, filters, project, product, version);
		setRowCount(size.getValue());
		System.err.println("SIZE :" + data.size());

		int dataSize = data.size();
		((HttpSession) FacesContext.getCurrentInstance().getExternalContext()
				.getSession(false)).setAttribute("bugDataTableOptions",
				new BugDataTableOptions(first, multiSortMeta, filters,contact != null));
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

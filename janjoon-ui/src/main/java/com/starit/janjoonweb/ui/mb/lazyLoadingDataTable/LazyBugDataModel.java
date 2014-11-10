package com.starit.janjoonweb.ui.mb.lazyLoadingDataTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJBugService;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.ui.mb.JJBugBean;
import com.starit.janjoonweb.ui.mb.LoginBean;

public class LazyBugDataModel extends LazyDataModel<JJBug> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JJBugService bugService;
	JJProject project;

	public LazyBugDataModel(JJBugService bugService, JJProject project) {
		this.project = project;
		this.bugService = bugService;
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
			List<SortMeta> multiSortMeta, Map<String, String> filters) {

		List<JJBug> data = new ArrayList<JJBug>();

		data = bugService.load(first, pageSize, multiSortMeta,filters, project);
		setRowCount(bugService.getBugs(project).size());
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

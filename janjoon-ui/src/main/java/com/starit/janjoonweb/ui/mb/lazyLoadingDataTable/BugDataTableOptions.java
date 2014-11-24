package com.starit.janjoonweb.ui.mb.lazyLoadingDataTable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.event.ComponentSystemEvent;
import javax.persistence.criteria.Join;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.model.SortMeta;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJCriticity;
import com.starit.janjoonweb.domain.JJImportance;
import com.starit.janjoonweb.domain.JJStatus;

@Scope("session")
@Component("bugDataTableOptions")
public class BugDataTableOptions {

	private int first;
	private List<SortMeta> multiSortMeta;
	private Map<String, String> filters;
	private DataTable dataTable;
	private String importance;
	private String status;
	private String criticity;

	public String getImportance() {
		return importance;
	}

	public void setImportance(String importance) {
		this.importance = importance;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCriticity() {
		return criticity;
	}

	public void setCriticity(String criticity) {
		this.criticity = criticity;
	}

	public DataTable getDataTable() {
		return dataTable;
	}

	public void setDataTable(DataTable dataTable) {
		this.dataTable = dataTable;
	}

	public BugDataTableOptions() {

		if (dataTable != null) {
			this.first = dataTable.getFirst();
			this.multiSortMeta = dataTable.getMultiSortMeta();
			this.filters = dataTable.getFilters();
		}

	}

	public BugDataTableOptions(int first, List<SortMeta> multiSortMeta,
			Map<String, String> filters) {

		this.first = first;
		this.multiSortMeta = multiSortMeta;
		this.filters = filters;
		importance="selected";
		status="selected";
		criticity="selected";
		if (filters != null) {
			Iterator<Entry<String, String>> it = filters.entrySet().iterator();
			while (it.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry pairs = (Map.Entry) it.next();
				if (pairs.getKey().toString().contains("importance")) {
					
					importance=pairs.getValue().toString().toUpperCase();
				} else if (pairs.getKey().toString().contains("criticity")) {
					
					criticity=pairs.getValue().toString().toUpperCase();
				} else if (pairs.getKey().toString().contains("status")) {
					
					status=pairs.getValue().toString().toUpperCase();
				}
			}
		}
	}

	public void dataTableInit(ComponentSystemEvent e) {
		System.err.println(e.getComponent().getId());
		dataTable.setFirst(first);
		dataTable.setFilters(filters);
		dataTable.setMultiSortMeta(multiSortMeta);
		if(importance != null)
			RequestContext.getCurrentInstance().execute("preRenderDataTable()");
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public List<SortMeta> getMultiSortMeta() {
		return multiSortMeta;
	}

	public void setMultiSortMeta(List<SortMeta> multiSortMeta) {
		this.multiSortMeta = multiSortMeta;
	}

	public Map<String, String> getFilters() {
		return filters;
	}

	public void setFilters(Map<String, String> filters) {
		this.filters = filters;
	}

}

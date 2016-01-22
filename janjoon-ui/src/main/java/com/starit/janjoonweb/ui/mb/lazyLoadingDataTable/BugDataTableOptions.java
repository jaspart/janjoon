package com.starit.janjoonweb.ui.mb.lazyLoadingDataTable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.event.ComponentSystemEvent;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.inputswitch.InputSwitch;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.model.SortMeta;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("session")
@Component("bugDataTableOptions")
public class BugDataTableOptions {

	private int first;
	private List<SortMeta> multiSortMeta;
	private Map<String, Object> filters;
	private String importance;
	private String status;
	private String criticity;
	private boolean mine;

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

	public BugDataTableOptions() {

	}

	public BugDataTableOptions(int first, List<SortMeta> multiSortMeta,
			Map<String, Object> filters, boolean mine) {

		this.first = first;
		this.multiSortMeta = multiSortMeta;
		this.filters = filters;
		this.mine = mine;
		importance = "selected";
		status = "selected";
		criticity = "selected";
		if (filters != null) {
			Iterator<Entry<String, Object>> it = filters.entrySet().iterator();
			while (it.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry pairs = (Map.Entry) it.next();
				if (pairs.getKey().toString().contains("importance")) {

					importance = pairs.getValue().toString().toUpperCase();
				} else if (pairs.getKey().toString().contains("criticity")) {

					criticity = pairs.getValue().toString().toUpperCase();
				} else if (pairs.getKey().toString().contains("status")) {

					status = pairs.getValue().toString().toUpperCase();
				}
			}
		}
	}

	public void dataTableInit(DataTable dataTable, InputSwitch mineBugs,
			InputText glbFilter, SelectOneMenu criticitySelect,
			SelectOneMenu importanceSelect, SelectOneMenu statusSelect) {

		dataTable.setFirst(first);
		dataTable.setFilters(filters);
		dataTable.setMultiSortMeta(multiSortMeta);
		mineBugs.setValue(mine);
		if (filters != null) {
			glbFilter.setValue(filters.get("globalFilter"));
			criticitySelect.setValue(filters.get("criticity.name"));
			importanceSelect.setValue(filters.get("importance.name"));
			statusSelect.setValue(filters.get("status.name"));
		}

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

	public Map<String, Object> getFilters() {
		return filters;
	}

	public void setFilters(Map<String, Object> filters) {
		this.filters = filters;
	}

	public boolean isMine() {
		return mine;
	}

	public void setMine(boolean mine) {
		this.mine = mine;
	}

}

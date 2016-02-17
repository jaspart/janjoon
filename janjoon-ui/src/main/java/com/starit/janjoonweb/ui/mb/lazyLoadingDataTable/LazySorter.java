package com.starit.janjoonweb.ui.mb.lazyLoadingDataTable;

import java.util.Comparator;
import org.primefaces.model.SortOrder;
import com.starit.janjoonweb.domain.JJBug;

public class LazySorter implements Comparator<JJBug> {

	private String		sortField;

	private SortOrder	sortOrder;

	public LazySorter(String sortField, SortOrder sortOrder) {
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	public int compare(JJBug bug1, JJBug bug2) {
		try {
			Object value1 = JJBug.class.getField(this.sortField).get(bug1);
			Object value2 = JJBug.class.getField(this.sortField).get(bug2);

			int value = ((Comparable) value1).compareTo(value2);

			return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}
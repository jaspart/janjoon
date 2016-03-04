package com.starit.janjoonweb.ui.mb.util.renderer;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.component.api.UIColumn;
import org.primefaces.component.columns.Columns;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.datatable.DataTableRenderer;
import org.primefaces.component.datatable.feature.DataTableFeatureKey;
import org.primefaces.component.datatable.feature.SortFeature;

import com.starit.janjoonweb.ui.mb.JJTaskBean;
import com.starit.janjoonweb.ui.mb.LoginBean;

public class DataTableRendererImpl extends DataTableRenderer {

	@Override
	protected void encodeCell(FacesContext context, DataTable table,
			UIColumn column, String clientId, boolean selected)
			throws IOException {
		if (!column.isRendered()) {
			return;
		}

		ResponseWriter writer = context.getResponseWriter();
		boolean selectionEnabled = column.getSelectionMode() != null;
		String style = column.getStyle();
		String styleClass = selectionEnabled
				? DataTable.SELECTION_COLUMN_CLASS
				: (column.getCellEditor() != null)
						? DataTable.EDITABLE_COLUMN_CLASS
						: null;
		String userStyleClass = column.getStyleClass();
		styleClass = userStyleClass == null
				? styleClass
				: (styleClass == null)
						? userStyleClass
						: styleClass + " " + userStyleClass;

		writer.startElement("td", null);
		writer.writeAttribute("role", "gridcell", null);
		if (column.getColspan() != 1)
			writer.writeAttribute("colspan", column.getColspan(), null);
		if (style != null)
			writer.writeAttribute("style", style, null);
		if (styleClass != null)
			writer.writeAttribute("class", styleClass, null);

		if (selectionEnabled)
			encodeColumnSelection(context, table, clientId, column, selected);
		else
			column.encodeAll(context);

		writer.endElement("td");
	}

	protected void preRender(FacesContext context, DataTable table) {
		if (table.isLazy()) {
			if (table.isLiveScroll())
				table.loadLazyScrollData(0, table.getScrollRows());
			else
				table.loadLazyData();
		}

		boolean page = FacesContext.getCurrentInstance().getViewRoot()
				.getViewId().contains("planning")
				&& (((JJTaskBean) LoginBean.findBean("jJTaskBean"))
						.getSortMode() != null)
				&& (!((JJTaskBean) LoginBean.findBean("jJTaskBean"))
						.getSortMode().equalsIgnoreCase("chapter"));
		if (page) {
			table.setValueExpression("sortBy", null);
			table.setSortBy(null);
		}

		boolean defaultSorted = (table.getValueExpression("sortBy") != null
				|| table.getSortBy() != null);
		if (defaultSorted && !table.isLazy()) {
			SortFeature sortFeature = (SortFeature) table
					.getFeature(DataTableFeatureKey.SORT);

			if (table.isMultiSort())
				sortFeature.multiSort(context, table);
			else
				sortFeature.singleSort(context, table);

			table.setRowIndex(-1);
		}

		if (table.isPaginator()) {
			table.calculateFirst();
		}

		Columns dynamicCols = table.getDynamicColumns();
		if (dynamicCols != null) {
			dynamicCols.setRowIndex(-1);
		}
	}

}
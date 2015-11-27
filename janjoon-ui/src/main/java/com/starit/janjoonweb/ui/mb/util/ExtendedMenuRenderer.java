package com.starit.janjoonweb.ui.mb.util;

import java.io.IOException;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;

import org.primefaces.component.column.Column;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.component.selectonemenu.SelectOneMenuRenderer;

public class ExtendedMenuRenderer extends SelectOneMenuRenderer {

	protected void encodeOptionsAsTable(FacesContext context,
			SelectOneMenu menu, List<SelectItem> selectItems)
			throws IOException {

		ResponseWriter writer = context.getResponseWriter();
		String var = menu.getVar();
		List<Column> columns = menu.getColums();

		for (SelectItem selectItem : selectItems) {
			String itemLabel = selectItem.getLabel();
			itemLabel = isValueBlank(itemLabel) ? "&nbsp;" : itemLabel;
			Object itemValue = selectItem.getValue();

			String itemStyleClass = SelectOneMenu.ROW_CLASS;

			if (itemValue != null
					&& menu.getAttributes().get("optionClasses") != null)
				itemStyleClass = itemStyleClass
						.replace("ui-widget-content", "")
						+ " "
						+ (String) itemValue;

			if (selectItem.isNoSelectionOption()) {
				itemStyleClass = itemStyleClass + " ui-noselection-option";
			}

			context.getExternalContext().getRequestMap()
					.put(var, selectItem.getValue());

			writer.startElement("tr", null);
			writer.writeAttribute("class", itemStyleClass, null);
			if (itemValue == null
					&& menu.getAttributes().get("optionClasses") != null)
				writer.writeAttribute("style", "text-align: center;", null);

			writer.writeAttribute("data-label", itemLabel, null);

			if (selectItem.getDescription() != null) {
				writer.writeAttribute("title", selectItem.getDescription(),
						null);
			}

			if (itemValue instanceof String) {
				writer.startElement("td", null);
				writer.writeAttribute("colspan", columns.size(), null);
				writer.writeText(selectItem.getLabel(), null);
				writer.endElement("td");
			} else {
				for (Column column : columns) {
					writer.startElement("td", null);
					renderChildren(context, column);
					writer.endElement("td");
				}
			}

			writer.endElement("tr");
		}

		context.getExternalContext().getRequestMap().put(var, null);
	}
}

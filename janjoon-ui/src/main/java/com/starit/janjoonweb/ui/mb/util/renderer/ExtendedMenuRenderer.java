package com.starit.janjoonweb.ui.mb.util.renderer;

import java.io.IOException;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;

import org.primefaces.component.column.Column;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.component.selectonemenu.SelectOneMenuRenderer;
import org.primefaces.util.ComponentUtils;

public class ExtendedMenuRenderer extends SelectOneMenuRenderer {

	protected void encodeLabel(FacesContext context, SelectOneMenu menu,
			List<SelectItem> selectItems) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String valueToRender = ComponentUtils.getValueToRender(context, menu);

		if (menu.isEditable()) {
			writer.startElement("input", null);
			writer.writeAttribute("type", "text", null);
			writer.writeAttribute("name", menu.getClientId(context)
					+ "_editableInput", null);
			writer.writeAttribute("class", SelectOneMenu.LABEL_CLASS, null);

			if (menu.getTabindex() != null) {
				writer.writeAttribute("tabindex", menu.getTabindex(), null);
			}

			if (menu.isDisabled()) {
				writer.writeAttribute("disabled", "disabled", null);
			}

			if (valueToRender != null) {
				writer.writeAttribute("value", valueToRender, null);
			}

			if (menu.getMaxlength() != Integer.MAX_VALUE) {
				writer.writeAttribute("maxlength", menu.getMaxlength(), null);
			}

			writer.endElement("input");
		} else {
			writer.startElement("label", null);
			writer.writeAttribute("id", menu.getClientId(context) + "_label",
					null);

			if (valueToRender != null
					&& menu.getAttributes().get("optionClasses") != null)
				writer.writeAttribute("class", SelectOneMenu.LABEL_CLASS
						+ " Fs14 " + valueToRender.toString() + "_select", null);
			else if (menu.getAttributes().get("optionClasses") != null
					|| menu.getAttributes().get("fontSize") != null)
				writer.writeAttribute("class", SelectOneMenu.LABEL_CLASS
						+ " Fs14", null);
			else
				writer.writeAttribute("class", SelectOneMenu.LABEL_CLASS, null);
			writer.write("&nbsp;");
			writer.endElement("label");
		}
	}

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

			if (itemValue != null && !itemValue.toString().isEmpty()
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
			if ((itemValue == null || itemValue.toString().isEmpty())
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
				if (itemValue != null && !itemValue.toString().isEmpty()
						&& menu.getAttributes().get("optionClasses") != null)
					writer.writeAttribute("style", "color: white;", null);
				if (menu.getAttributes().get("optionClasses") != null
						|| menu.getAttributes().get("fontSize") != null)
					writer.writeAttribute("class", "Fs14", null);
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

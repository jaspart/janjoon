package com.starit.janjoonweb.ui.mb.util.renderer;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.extensions.component.timepicker.TimePicker;

import com.starit.janjoonweb.ui.mb.LoginBean;

public class TimePickerRenderer extends org.primefaces.extensions.component.timepicker.TimePickerRenderer {

	protected void encodeMarkup(final FacesContext fc, final TimePicker timepicker, final String value)
	        throws IOException {
		ResponseWriter writer = fc.getResponseWriter();
		String clientId = timepicker.getClientId(fc);
		String inputId = clientId + "_input";

		writer.startElement("span", timepicker);
		writer.writeAttribute("id", clientId, null);
		writer.writeAttribute("class", TimePicker.CONTAINER_CLASS, null);

		if (timepicker.isInline()) {
			// inline container
			writer.startElement("div", null);
			writer.writeAttribute("id", clientId + "_inline", null);
			writer.endElement("div");
		}

		writer.startElement("input", null);
		writer.writeAttribute("id", inputId, null);
		writer.writeAttribute("name", inputId, null);
		writer.writeAttribute("type", timepicker.isInline() ? "hidden" : "text", null);
		writer.writeAttribute("autocomplete", "off", null);

		if ((LoginBean.findBean("loginBean") != null && ((LoginBean) LoginBean.findBean("loginBean")).isMobile()))
			writer.writeAttribute("readonly", "readonly", null);

		if (StringUtils.isNotBlank(value)) {
			writer.writeAttribute("value", value, null);
		}

		if (!timepicker.isInline()) {
			String styleClass = timepicker.getStyleClass();
			styleClass = (styleClass == null ? TimePicker.INPUT_CLASS : TimePicker.INPUT_CLASS + " " + styleClass);
			if (!timepicker.isValid()) {
				styleClass = styleClass + " ui-state-error";
			}

			writer.writeAttribute("class", styleClass, null);

			if (timepicker.getStyle() != null) {
				writer.writeAttribute("style", timepicker.getStyle(), null);
			}

			renderPassThruAttributes(fc, timepicker, TimePicker.INPUT_TEXT_ATTRS);
		}

		writer.endElement("input");

		if (timepicker.isSpinner()) {
			boolean disabled = timepicker.isDisabled() || timepicker.isReadonly();
			encodeSpinnerButton(fc, TimePicker.UP_BUTTON_CLASS, TimePicker.UP_ICON_CLASS, disabled);
			encodeSpinnerButton(fc, TimePicker.DOWN_BUTTON_CLASS, TimePicker.DOWN_ICON_CLASS, disabled);
		}

		if (!"focus".equals(timepicker.getShowOn())) {
			writer.startElement("button", null);
			writer.writeAttribute("class", TimePicker.BUTTON_TRIGGER_CLASS, null);
			writer.writeAttribute("type", "button", null);
			writer.writeAttribute("role", "button", null);

			writer.startElement("span", null);
			writer.writeAttribute("class", TimePicker.BUTTON_TRIGGER_ICON_CLASS, null);
			writer.endElement("span");

			writer.startElement("span", null);
			writer.writeAttribute("class", TimePicker.BUTTON_TRIGGER_TEXT_CLASS, null);
			writer.write("ui-button");
			writer.endElement("span");

			writer.endElement("button");
		}

		writer.endElement("span");
	}

}

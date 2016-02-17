package com.starit.janjoonweb.ui.mb.util.renderer;

import java.io.IOException;
import java.util.Map;
import com.starit.janjoonweb.ui.mb.LoginBean;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.context.RequestContext;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.HTML;

public class AutoCompleteRenderer extends org.primefaces.component.autocomplete.AutoCompleteRenderer {

	protected void encodeInput(FacesContext context, AutoComplete ac, String clientId) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		boolean disabled = ac.isDisabled();
		String var = ac.getVar();
		String itemLabel;
		String defaultStyleClass = ac.isDropdown() ? AutoComplete.INPUT_WITH_DROPDOWN_CLASS : AutoComplete.INPUT_CLASS;
		String styleClass = disabled ? defaultStyleClass + " ui-state-disabled" : defaultStyleClass;
		styleClass = ac.isValid() ? styleClass : styleClass + " ui-state-error";
		String inputStyle = ac.getInputStyle();
		String inputStyleClass = ac.getInputStyleClass();
		inputStyleClass = (inputStyleClass == null) ? styleClass : styleClass + " " + inputStyleClass;
		String labelledBy = ac.getLabelledBy();

		writer.startElement("input", null);
		writer.writeAttribute("id", clientId + "_input", null);
		writer.writeAttribute("name", clientId + "_input", null);
		writer.writeAttribute("type", ac.getType(), null);
		writer.writeAttribute("class", inputStyleClass, null);
		writer.writeAttribute("autocomplete", "off", null);

		if (labelledBy != null) {
			writer.writeAttribute("aria-labelledby", labelledBy, null);
		}

		if (inputStyle != null) {
			writer.writeAttribute("style", inputStyle, null);
		}

		renderPassThruAttributes(context, ac, HTML.INPUT_TEXT_ATTRS_WITHOUT_EVENTS);
		renderDomEvents(context, ac, HTML.INPUT_TEXT_EVENTS);

		if (var == null) {
			itemLabel = ComponentUtils.getValueToRender(context, ac);

			if (itemLabel != null) {
				writer.writeAttribute("value", itemLabel, null);
			}
		} else {
			Map<String, Object> requestMap = context.getExternalContext().getRequestMap();

			if (ac.isValid()) {
				requestMap.put(var, ac.getValue());
				itemLabel = ac.getItemLabel();
			} else {
				Object submittedValue = ac.getSubmittedValue();
				itemLabel = (submittedValue == null) ? null : String.valueOf(submittedValue);

				Object value = ac.getValue();

				if (itemLabel == null && value != null) {
					requestMap.put(var, value);
					itemLabel = ac.getItemLabel();
				}
			}

			if (itemLabel != null) {
				writer.writeAttribute("value", itemLabel, null);
			}

			requestMap.remove(var);
		}

		if (disabled)
			writer.writeAttribute("disabled", "disabled", null);
		if (ac.isReadonly() || (LoginBean.findBean("loginBean") != null
		        && ((LoginBean) LoginBean.findBean("loginBean")).isMobile()))
			writer.writeAttribute("readonly", "readonly", null);
		if (ac.isRequired())
			writer.writeAttribute("aria-required", "true", null);

		if (RequestContext.getCurrentInstance().getApplicationContext().getConfig().isClientSideValidationEnabled()) {
			renderValidationMetadata(context, ac);
		}

		writer.endElement("input");
	}
}

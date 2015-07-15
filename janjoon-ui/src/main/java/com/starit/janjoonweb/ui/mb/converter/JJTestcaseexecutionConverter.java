package com.starit.janjoonweb.ui.mb.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.starit.janjoonweb.domain.JJSprint;
import com.starit.janjoonweb.domain.JJTestcaseexecution;

import org.springframework.roo.addon.jsf.converter.RooJsfConverter;

@RooJsfConverter(entity = JJTestcaseexecution.class)
public class JJTestcaseexecutionConverter {

	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		return (value instanceof JJTestcaseexecution && ((JJTestcaseexecution) value)
				.getId() != null) ? ((JJTestcaseexecution) value).getId()
				.toString() : "";
	}
}

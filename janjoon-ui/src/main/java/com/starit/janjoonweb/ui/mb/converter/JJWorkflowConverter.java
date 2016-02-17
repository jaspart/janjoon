package com.starit.janjoonweb.ui.mb.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.starit.janjoonweb.domain.JJWorkflow;

import org.springframework.roo.addon.jsf.converter.RooJsfConverter;

@RooJsfConverter(entity = JJWorkflow.class)
public class JJWorkflowConverter {

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return (value instanceof JJWorkflow && ((JJWorkflow) value).getId() != null)
		        ? ((JJWorkflow) value).getId().toString() : "";
	}
}

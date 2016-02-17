package com.starit.janjoonweb.ui.mb.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.domain.JJWorkflow;

import org.springframework.roo.addon.jsf.converter.RooJsfConverter;

@RooJsfConverter(entity = JJVersion.class)
public class JJVersionConverter {

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return (value instanceof JJVersion && ((JJVersion) value).getId() != null)
		        ? ((JJVersion) value).getId().toString() : "";
	}
}

package com.starit.janjoonweb.ui.mb.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.starit.janjoonweb.domain.JJAuditLog;
import com.starit.janjoonweb.domain.JJBug;

import org.springframework.roo.addon.jsf.converter.RooJsfConverter;

@RooJsfConverter(entity = JJBug.class)
public class JJBugConverter {

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return (value instanceof JJBug && ((JJBug) value).getId() != null) ? ((JJBug) value).getId().toString() : "";
	}
}

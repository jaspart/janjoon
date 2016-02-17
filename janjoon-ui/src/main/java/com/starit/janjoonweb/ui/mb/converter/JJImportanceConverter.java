package com.starit.janjoonweb.ui.mb.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.starit.janjoonweb.domain.JJHardware;
import com.starit.janjoonweb.domain.JJImportance;

import org.springframework.roo.addon.jsf.converter.RooJsfConverter;

@RooJsfConverter(entity = JJImportance.class)
public class JJImportanceConverter {
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return (value instanceof JJImportance && ((JJImportance) value).getId() != null)
		        ? ((JJImportance) value).getId().toString() : "";
	}
}

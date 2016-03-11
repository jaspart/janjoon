package com.starit.janjoonweb.ui.mb.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.springframework.roo.addon.jsf.converter.RooJsfConverter;

import com.starit.janjoonweb.domain.JJRisk;

@RooJsfConverter(entity = JJRisk.class)
public class JJRiskConverter {

	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		return (value instanceof JJRisk && ((JJRisk) value).getId() != null)
				? ((JJRisk) value).getId().toString()
				: "";
	}
}

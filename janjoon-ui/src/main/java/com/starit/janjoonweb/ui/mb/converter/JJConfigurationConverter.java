package com.starit.janjoonweb.ui.mb.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.starit.janjoonweb.domain.JJAuditLog;
import com.starit.janjoonweb.domain.JJConfiguration;

import org.springframework.roo.addon.jsf.converter.RooJsfConverter;

@RooJsfConverter(entity = JJConfiguration.class)
public class JJConfigurationConverter {

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return (value instanceof JJConfiguration && ((JJConfiguration) value).getId() != null)
		        ? ((JJConfiguration) value).getId().toString() : "";
	}
}

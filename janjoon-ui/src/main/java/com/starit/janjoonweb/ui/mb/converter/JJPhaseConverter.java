package com.starit.janjoonweb.ui.mb.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.starit.janjoonweb.domain.JJPermission;
import com.starit.janjoonweb.domain.JJPhase;

import org.springframework.roo.addon.jsf.converter.RooJsfConverter;

@RooJsfConverter(entity = JJPhase.class)
public class JJPhaseConverter {

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return (value instanceof JJPhase && ((JJPhase) value).getId() != null) ? ((JJPhase) value).getId().toString()
		        : "";
	}
}

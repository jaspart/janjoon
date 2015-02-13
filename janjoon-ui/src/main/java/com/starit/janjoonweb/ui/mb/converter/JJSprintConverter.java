package com.starit.janjoonweb.ui.mb.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.starit.janjoonweb.domain.JJSoftware;
import com.starit.janjoonweb.domain.JJSprint;

import org.springframework.roo.addon.jsf.converter.RooJsfConverter;

@RooJsfConverter(entity = JJSprint.class)
public class JJSprintConverter {
	
	public String getAsString(FacesContext context, UIComponent component, Object value) {
        return (value instanceof JJSprint && ((JJSprint) value).getId() != null) ? ((JJSprint) value).getId().toString() : "";
    }
}

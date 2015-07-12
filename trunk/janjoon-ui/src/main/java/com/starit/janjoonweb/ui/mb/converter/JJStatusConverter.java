package com.starit.janjoonweb.ui.mb.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.starit.janjoonweb.domain.JJSprint;
import com.starit.janjoonweb.domain.JJStatus;

import org.springframework.roo.addon.jsf.converter.RooJsfConverter;

@RooJsfConverter(entity = JJStatus.class)
public class JJStatusConverter {
	
	public String getAsString(FacesContext context, UIComponent component, Object value) {
        return (value instanceof JJStatus && ((JJStatus) value).getId() != null) ? ((JJStatus) value).getId().toString() : "";
    }
}

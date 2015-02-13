package com.starit.janjoonweb.ui.mb.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.starit.janjoonweb.domain.JJRight;
import com.starit.janjoonweb.domain.JJSoftware;

import org.springframework.roo.addon.jsf.converter.RooJsfConverter;

@RooJsfConverter(entity = JJSoftware.class)
public class JJSoftwareConverter {
	
	public String getAsString(FacesContext context, UIComponent component, Object value) {
        return (value instanceof JJSoftware && ((JJSoftware) value).getId() != null) ? ((JJSoftware) value).getId().toString() : "";
    }
}

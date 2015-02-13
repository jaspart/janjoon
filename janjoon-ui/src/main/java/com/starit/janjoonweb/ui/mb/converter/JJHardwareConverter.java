package com.starit.janjoonweb.ui.mb.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.starit.janjoonweb.domain.JJCriticity;
import com.starit.janjoonweb.domain.JJHardware;

import org.springframework.roo.addon.jsf.converter.RooJsfConverter;

@RooJsfConverter(entity = JJHardware.class)
public class JJHardwareConverter {
	
	public String getAsString(FacesContext context, UIComponent component, Object value) {
        return (value instanceof JJHardware && ((JJHardware) value).getId() != null) ? ((JJHardware) value).getId().toString() : "";
    }
}

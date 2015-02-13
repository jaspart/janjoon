package com.starit.janjoonweb.ui.mb.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.starit.janjoonweb.domain.JJProfile;
import com.starit.janjoonweb.domain.JJProject;

import org.springframework.roo.addon.jsf.converter.RooJsfConverter;

@RooJsfConverter(entity = JJProject.class)
public class JJProjectConverter {
	
	public String getAsString(FacesContext context, UIComponent component, Object value) {
        return (value instanceof JJProject && ((JJProject) value).getId() != null) ? ((JJProject) value).getId().toString() : "";
    }
}

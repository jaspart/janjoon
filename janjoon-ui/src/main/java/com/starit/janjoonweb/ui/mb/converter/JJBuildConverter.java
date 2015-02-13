package com.starit.janjoonweb.ui.mb.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJBuild;

import org.springframework.roo.addon.jsf.converter.RooJsfConverter;

@RooJsfConverter(entity = JJBuild.class)
public class JJBuildConverter {
	
	public String getAsString(FacesContext context, UIComponent component, Object value) {
        return (value instanceof JJBuild && ((JJBuild) value).getId() != null) ? ((JJBuild) value).getId().toString() : "";
    }
}

package com.starit.janjoonweb.ui.mb.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.starit.janjoonweb.domain.JJSprint;
import com.starit.janjoonweb.domain.JJTestcase;

import org.springframework.roo.addon.jsf.converter.RooJsfConverter;

@RooJsfConverter(entity = JJTestcase.class)
public class JJTestcaseConverter {
	
	public String getAsString(FacesContext context, UIComponent component, Object value) {
        return (value instanceof JJTestcase && ((JJTestcase) value).getId() != null) ? ((JJTestcase) value).getId().toString() : "";
    }
}

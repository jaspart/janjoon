package com.starit.janjoonweb.ui.mb.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.starit.janjoonweb.domain.JJSprint;
import com.starit.janjoonweb.domain.JJTeststepexecution;

import org.springframework.roo.addon.jsf.converter.RooJsfConverter;

@RooJsfConverter(entity = JJTeststepexecution.class)
public class JJTeststepexecutionConverter {
	
	public String getAsString(FacesContext context, UIComponent component, Object value) {
        return (value instanceof JJTeststepexecution && ((JJTeststepexecution) value).getId() != null) ? ((JJTeststepexecution) value).getId().toString() : "";
    }
}

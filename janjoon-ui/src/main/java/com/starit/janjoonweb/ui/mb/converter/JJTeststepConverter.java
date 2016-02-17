package com.starit.janjoonweb.ui.mb.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.starit.janjoonweb.domain.JJSprint;
import com.starit.janjoonweb.domain.JJTeststep;

import org.springframework.roo.addon.jsf.converter.RooJsfConverter;

@RooJsfConverter(entity = JJTeststep.class)
public class JJTeststepConverter {

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return (value instanceof JJTeststep && ((JJTeststep) value).getId() != null)
		        ? ((JJTeststep) value).getId().toString() : "";
	}
}

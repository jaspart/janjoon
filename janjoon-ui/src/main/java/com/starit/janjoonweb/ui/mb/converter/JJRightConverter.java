package com.starit.janjoonweb.ui.mb.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJRight;

import org.springframework.roo.addon.jsf.converter.RooJsfConverter;

@RooJsfConverter(entity = JJRight.class)
public class JJRightConverter {

	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		return (value instanceof JJRight && ((JJRight) value).getId() != null) ? ((JJRight) value)
				.getId().toString() : "";
	}
}

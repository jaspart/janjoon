package com.starit.janjoonweb.ui.mb.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.starit.janjoonweb.domain.JJContact;

import org.springframework.roo.addon.jsf.converter.RooJsfConverter;

@RooJsfConverter(entity = JJContact.class)
public class JJContactConverter {

	public String getAsString(FacesContext context, UIComponent component,
			Object value) {

		return (value instanceof JJContact && ((JJContact) value).getId() != null) ? ((JJContact) value)
				.getId().toString() : "";

	}
}

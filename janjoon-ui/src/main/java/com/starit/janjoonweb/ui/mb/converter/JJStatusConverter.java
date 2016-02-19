package com.starit.janjoonweb.ui.mb.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.springframework.roo.addon.jsf.converter.RooJsfConverter;

import com.starit.janjoonweb.domain.JJStatus;

@RooJsfConverter(entity = JJStatus.class)
public class JJStatusConverter {

	public String getAsString(final FacesContext context,
			final UIComponent component, final Object value) {
		return (value instanceof JJStatus && ((JJStatus) value).getId() != null)
				? ((JJStatus) value).getId().toString()
				: "";
	}
}

package com.starit.janjoonweb.ui.mb.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.starit.janjoonweb.domain.JJRequirement;

import org.springframework.roo.addon.jsf.converter.RooJsfConverter;

@RooJsfConverter(entity = JJRequirement.class)
public class JJRequirementConverter {

	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		return (value instanceof JJRequirement
				&& ((JJRequirement) value).getId() != null)
						? ((JJRequirement) value).getId().toString()
						: "";
	}
}

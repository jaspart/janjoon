package com.starit.janjoonweb.ui.mb.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.starit.janjoonweb.domain.JJImportance;
import com.starit.janjoonweb.domain.JJImputation;

import org.springframework.roo.addon.jsf.converter.RooJsfConverter;

@RooJsfConverter(entity = JJImputation.class)
public class JJImputationConverter {

	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		return (value instanceof JJImputation && ((JJImputation) value).getId() != null) ? ((JJImputation) value)
				.getId().toString() : "";
	}
}

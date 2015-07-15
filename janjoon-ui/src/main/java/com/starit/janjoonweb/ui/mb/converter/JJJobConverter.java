package com.starit.janjoonweb.ui.mb.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.starit.janjoonweb.domain.JJImputation;
import com.starit.janjoonweb.domain.JJJob;

import org.springframework.roo.addon.jsf.converter.RooJsfConverter;

@RooJsfConverter(entity = JJJob.class)
public class JJJobConverter {

	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		return (value instanceof JJJob && ((JJJob) value).getId() != null) ? ((JJJob) value)
				.getId().toString() : "";
	}
}

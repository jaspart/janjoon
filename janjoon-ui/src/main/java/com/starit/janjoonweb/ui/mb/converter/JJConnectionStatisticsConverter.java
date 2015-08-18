package com.starit.janjoonweb.ui.mb.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.springframework.roo.addon.jsf.converter.RooJsfConverter;

import com.starit.janjoonweb.domain.JJConnectionStatistics;

@RooJsfConverter(entity = JJConnectionStatistics.class)
public class JJConnectionStatisticsConverter {

	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		return (value instanceof JJConnectionStatistics && ((JJConnectionStatistics) value)
				.getId() != null) ? ((JJConnectionStatistics) value).getId()
				.toString() : "";
	}

}

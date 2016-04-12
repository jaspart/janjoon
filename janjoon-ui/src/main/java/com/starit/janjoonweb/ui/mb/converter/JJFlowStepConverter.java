package com.starit.janjoonweb.ui.mb.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.springframework.roo.addon.jsf.converter.RooJsfConverter;

import com.starit.janjoonweb.domain.JJFlowStep;

@RooJsfConverter(entity = JJFlowStep.class)
public class JJFlowStepConverter {

	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		return (value instanceof JJFlowStep
				&& ((JJFlowStep) value).getId() != null)
						? ((JJFlowStep) value).getId().toString()
						: "";
	}

}

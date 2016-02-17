package com.starit.janjoonweb.ui.mb.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.starit.janjoonweb.domain.JJPhase;
import com.starit.janjoonweb.domain.JJProduct;

import org.springframework.roo.addon.jsf.converter.RooJsfConverter;

@RooJsfConverter(entity = JJProduct.class)
public class JJProductConverter {

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return (value instanceof JJProduct && ((JJProduct) value).getId() != null)
		        ? ((JJProduct) value).getId().toString() : "";
	}
}

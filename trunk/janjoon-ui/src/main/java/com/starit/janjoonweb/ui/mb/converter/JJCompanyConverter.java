package com.starit.janjoonweb.ui.mb.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.starit.janjoonweb.domain.JJChapter;
import com.starit.janjoonweb.domain.JJCompany;

import org.springframework.roo.addon.jsf.converter.RooJsfConverter;

@RooJsfConverter(entity = JJCompany.class)
public class JJCompanyConverter {
	
	public String getAsString(FacesContext context, UIComponent component, Object value) {
        return (value instanceof JJCompany && ((JJCompany) value).getId() != null) ? ((JJCompany) value).getId().toString() : "";
    }
}

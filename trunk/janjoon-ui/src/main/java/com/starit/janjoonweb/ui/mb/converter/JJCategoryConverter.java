package com.starit.janjoonweb.ui.mb.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.starit.janjoonweb.domain.JJBuild;
import com.starit.janjoonweb.domain.JJCategory;

import org.springframework.roo.addon.jsf.converter.RooJsfConverter;

@RooJsfConverter(entity = JJCategory.class)
public class JJCategoryConverter {
	
	public String getAsString(FacesContext context, UIComponent component, Object value) {
        return (value instanceof JJCategory && ((JJCategory) value).getId() != null) ? ((JJCategory) value).getId().toString() : "";
    }
}

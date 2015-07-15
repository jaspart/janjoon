package com.starit.janjoonweb.ui.mb.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.starit.janjoonweb.domain.JJMessage;
import com.starit.janjoonweb.domain.JJPermission;

import org.springframework.roo.addon.jsf.converter.RooJsfConverter;

@RooJsfConverter(entity = JJPermission.class)
public class JJPermissionConverter {

	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		return (value instanceof JJPermission && ((JJPermission) value).getId() != null) ? ((JJPermission) value)
				.getId().toString() : "";
	}
}

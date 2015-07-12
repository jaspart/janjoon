package com.starit.janjoonweb.ui.mb.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.springframework.roo.addon.jsf.converter.RooJsfConverter;

import com.starit.janjoonweb.domain.JJAuditLog;

@RooJsfConverter(entity = JJAuditLog.class)
public class JJAuditLogConverter {
	
	public String getAsString(FacesContext context, UIComponent component, Object value) {
        return (value instanceof JJAuditLog && ((JJAuditLog) value).getId() != null) ? ((JJAuditLog) value).getId().toString() : "";
    }
	
}

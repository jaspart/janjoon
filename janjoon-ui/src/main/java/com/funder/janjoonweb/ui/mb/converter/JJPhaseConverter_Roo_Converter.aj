// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.ui.mb.converter;

import com.funder.janjoonweb.domain.JJPhase;
import com.funder.janjoonweb.domain.JJPhaseService;
import com.funder.janjoonweb.ui.mb.converter.JJPhaseConverter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.springframework.beans.factory.annotation.Autowired;

privileged aspect JJPhaseConverter_Roo_Converter {
    
    declare parents: JJPhaseConverter implements Converter;
    
    declare @type: JJPhaseConverter: @FacesConverter("com.funder.janjoonweb.ui.mb.converter.JJPhaseConverter");
    
    @Autowired
    JJPhaseService JJPhaseConverter.jJPhaseService;
    
    public Object JJPhaseConverter.getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        Long id = Long.parseLong(value);
        return jJPhaseService.findJJPhase(id);
    }
    
    public String JJPhaseConverter.getAsString(FacesContext context, UIComponent component, Object value) {
        return value instanceof JJPhase ? ((JJPhase) value).getId().toString() : "";
    }
    
}

// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.ui.mb.converter;

import com.funder.janjoonweb.domain.JJJob;
import com.funder.janjoonweb.domain.JJJobService;
import com.funder.janjoonweb.ui.mb.converter.JJJobConverter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.springframework.beans.factory.annotation.Autowired;

privileged aspect JJJobConverter_Roo_Converter {
    
    declare parents: JJJobConverter implements Converter;
    
    declare @type: JJJobConverter: @FacesConverter("com.funder.janjoonweb.ui.mb.converter.JJJobConverter");
    
    @Autowired
    JJJobService JJJobConverter.jJJobService;
    
    public Object JJJobConverter.getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        Long id = Long.parseLong(value);
        return jJJobService.findJJJob(id);
    }
    
    public String JJJobConverter.getAsString(FacesContext context, UIComponent component, Object value) {
        return value instanceof JJJob ? ((JJJob) value).getId().toString() : "";
    }
    
}

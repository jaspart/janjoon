// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.ui.mb.converter;

import com.starit.janjoonweb.domain.JJTeststepexecution;
import com.starit.janjoonweb.domain.JJTeststepexecutionService;
import com.starit.janjoonweb.ui.mb.converter.JJTeststepexecutionConverter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.springframework.beans.factory.annotation.Autowired;

privileged aspect JJTeststepexecutionConverter_Roo_Converter {
    
    declare parents: JJTeststepexecutionConverter implements Converter;
    
    declare @type: JJTeststepexecutionConverter: @FacesConverter("com.starit.janjoonweb.ui.mb.converter.JJTeststepexecutionConverter");
    
    @Autowired
    JJTeststepexecutionService JJTeststepexecutionConverter.jJTeststepexecutionService;
    
    public Object JJTeststepexecutionConverter.getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        Long id = Long.parseLong(value);
        return jJTeststepexecutionService.findJJTeststepexecution(id);
    }
    
    public String JJTeststepexecutionConverter.getAsString(FacesContext context, UIComponent component, Object value) {
        return value instanceof JJTeststepexecution ? ((JJTeststepexecution) value).getId().toString() : "";
    }
    
}
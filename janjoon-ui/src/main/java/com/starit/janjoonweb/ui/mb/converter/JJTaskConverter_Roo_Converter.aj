// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.ui.mb.converter;

import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJTaskService;
import com.starit.janjoonweb.ui.mb.converter.JJTaskConverter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.springframework.beans.factory.annotation.Autowired;

privileged aspect JJTaskConverter_Roo_Converter {
    
    declare parents: JJTaskConverter implements Converter;
    
    declare @type: JJTaskConverter: @FacesConverter("com.starit.janjoonweb.ui.mb.converter.JJTaskConverter");
    
    @Autowired
    JJTaskService JJTaskConverter.jJTaskService;
    
    public Object JJTaskConverter.getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        Long id = Long.parseLong(value);
        return jJTaskService.findJJTask(id);
    }
    
}

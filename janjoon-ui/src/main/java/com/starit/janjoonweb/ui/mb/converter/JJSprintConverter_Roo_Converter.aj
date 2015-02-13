// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.ui.mb.converter;

import com.starit.janjoonweb.domain.JJSprint;
import com.starit.janjoonweb.domain.JJSprintService;
import com.starit.janjoonweb.ui.mb.converter.JJSprintConverter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.springframework.beans.factory.annotation.Autowired;

privileged aspect JJSprintConverter_Roo_Converter {
    
    declare parents: JJSprintConverter implements Converter;
    
    declare @type: JJSprintConverter: @FacesConverter("com.starit.janjoonweb.ui.mb.converter.JJSprintConverter");
    
    @Autowired
    JJSprintService JJSprintConverter.jJSprintService;
    
    public Object JJSprintConverter.getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        Long id = Long.parseLong(value);
        return jJSprintService.findJJSprint(id);
    }
    
}

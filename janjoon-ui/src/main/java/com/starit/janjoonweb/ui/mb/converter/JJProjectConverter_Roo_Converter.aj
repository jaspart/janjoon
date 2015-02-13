// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.ui.mb.converter;

import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJProjectService;
import com.starit.janjoonweb.ui.mb.converter.JJProjectConverter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.springframework.beans.factory.annotation.Autowired;

privileged aspect JJProjectConverter_Roo_Converter {
    
    declare parents: JJProjectConverter implements Converter;
    
    declare @type: JJProjectConverter: @FacesConverter("com.starit.janjoonweb.ui.mb.converter.JJProjectConverter");
    
    @Autowired
    JJProjectService JJProjectConverter.jJProjectService;
    
    public Object JJProjectConverter.getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        Long id = Long.parseLong(value);
        return jJProjectService.findJJProject(id);
    }
    
}

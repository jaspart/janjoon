// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.ui.mb.converter;

import com.starit.janjoonweb.domain.JJProfile;
import com.starit.janjoonweb.domain.JJProfileService;
import com.starit.janjoonweb.ui.mb.converter.JJProfileConverter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.springframework.beans.factory.annotation.Autowired;

privileged aspect JJProfileConverter_Roo_Converter {
    
    declare parents: JJProfileConverter implements Converter;
    
    declare @type: JJProfileConverter: @FacesConverter("com.starit.janjoonweb.ui.mb.converter.JJProfileConverter");
    
    @Autowired
    JJProfileService JJProfileConverter.jJProfileService;
    
    public Object JJProfileConverter.getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        Long id = Long.parseLong(value);
        return jJProfileService.findJJProfile(id);
    }
    
    
}

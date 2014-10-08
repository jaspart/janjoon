// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.ui.mb.converter;

import com.starit.janjoonweb.domain.JJWorkflow;
import com.starit.janjoonweb.domain.JJWorkflowService;
import com.starit.janjoonweb.ui.mb.converter.JJWorkflowConverter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.springframework.beans.factory.annotation.Autowired;

privileged aspect JJWorkflowConverter_Roo_Converter {
    
    declare parents: JJWorkflowConverter implements Converter;
    
    declare @type: JJWorkflowConverter: @FacesConverter("com.starit.janjoonweb.ui.mb.converter.JJWorkflowConverter");
    
    @Autowired
    JJWorkflowService JJWorkflowConverter.jJWorkflowService;
    
    public Object JJWorkflowConverter.getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        Long id = Long.parseLong(value);
        return jJWorkflowService.findJJWorkflow(id);
    }
    
    public String JJWorkflowConverter.getAsString(FacesContext context, UIComponent component, Object value) {
        return value instanceof JJWorkflow ? ((JJWorkflow) value).getId().toString() : "";
    }
    
}

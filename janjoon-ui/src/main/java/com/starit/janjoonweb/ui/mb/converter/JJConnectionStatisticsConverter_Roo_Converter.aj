// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.ui.mb.converter;

import com.starit.janjoonweb.domain.JJConnectionStatistics;
import com.starit.janjoonweb.domain.JJConnectionStatisticsService;
import com.starit.janjoonweb.ui.mb.converter.JJConnectionStatisticsConverter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.springframework.beans.factory.annotation.Autowired;

privileged aspect JJConnectionStatisticsConverter_Roo_Converter {
    
    declare parents: JJConnectionStatisticsConverter implements Converter;
    
    declare @type: JJConnectionStatisticsConverter: @FacesConverter("com.starit.janjoonweb.ui.mb.converter.JJConnectionStatisticsConverter");
    
    @Autowired
    JJConnectionStatisticsService JJConnectionStatisticsConverter.jJConnectionStatisticsService;
    
    public Object JJConnectionStatisticsConverter.getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        Long id = Long.parseLong(value);
        return jJConnectionStatisticsService.findJJConnectionStatistics(id);
    }  
    
    
}

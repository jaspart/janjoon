// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.ui.mb.converter;

import com.starit.janjoonweb.domain.JJStatusService;
import com.starit.janjoonweb.ui.mb.converter.JJStatusConverter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.springframework.beans.factory.annotation.Autowired;

privileged aspect JJStatusConverter_Roo_Converter{

declare parents:JJStatusConverter implements Converter;

declare @type:JJStatusConverter:@FacesConverter("com.starit.janjoonweb.ui.mb.converter.JJStatusConverter");

@Autowired JJStatusService JJStatusConverter.jJStatusService;

public Object JJStatusConverter.getAsObject(FacesContext context,UIComponent component,String value){if(value==null||value.length()==0){return null;}Long id=Long.parseLong(value);return jJStatusService.findJJStatus(id);}

}

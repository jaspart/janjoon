// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.ui.mb.converter;

import com.starit.janjoonweb.domain.JJHardwareService;
import com.starit.janjoonweb.ui.mb.converter.JJHardwareConverter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.springframework.beans.factory.annotation.Autowired;

privileged aspect JJHardwareConverter_Roo_Converter{

declare parents:JJHardwareConverter implements Converter;

declare @type:JJHardwareConverter:@FacesConverter("com.starit.janjoonweb.ui.mb.converter.JJHardwareConverter");

@Autowired JJHardwareService JJHardwareConverter.jJHardwareService;

public Object JJHardwareConverter.getAsObject(FacesContext context,UIComponent component,String value){if(value==null||value.length()==0){return null;}Long id=Long.parseLong(value);return jJHardwareService.findJJHardware(id);}

}

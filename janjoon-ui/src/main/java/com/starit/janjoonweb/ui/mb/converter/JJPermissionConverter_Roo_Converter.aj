// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.ui.mb.converter;

import com.starit.janjoonweb.domain.JJPermissionService;
import com.starit.janjoonweb.ui.mb.converter.JJPermissionConverter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.springframework.beans.factory.annotation.Autowired;

privileged aspect JJPermissionConverter_Roo_Converter{

declare parents:JJPermissionConverter implements Converter;

declare @type:JJPermissionConverter:@FacesConverter("com.starit.janjoonweb.ui.mb.converter.JJPermissionConverter");

@Autowired JJPermissionService JJPermissionConverter.jJPermissionService;

public Object JJPermissionConverter.getAsObject(FacesContext context,UIComponent component,String value){if(value==null||value.length()==0){return null;}Long id=Long.parseLong(value);return jJPermissionService.findJJPermission(id);}

}

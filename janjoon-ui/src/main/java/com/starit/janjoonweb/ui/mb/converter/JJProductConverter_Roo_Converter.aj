// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.ui.mb.converter;

import com.starit.janjoonweb.domain.JJProductService;
import com.starit.janjoonweb.ui.mb.converter.JJProductConverter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.springframework.beans.factory.annotation.Autowired;

privileged aspect JJProductConverter_Roo_Converter{

declare parents:JJProductConverter implements Converter;

declare @type:JJProductConverter:@FacesConverter("com.starit.janjoonweb.ui.mb.converter.JJProductConverter");

@Autowired JJProductService JJProductConverter.jJProductService;

public Object JJProductConverter.getAsObject(FacesContext context,UIComponent component,String value){if(value==null||value.length()==0){return null;}Long id=Long.parseLong(value);return jJProductService.findJJProduct(id);}

}

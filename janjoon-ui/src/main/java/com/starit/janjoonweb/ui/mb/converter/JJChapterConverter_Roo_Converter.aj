// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.ui.mb.converter;

import com.starit.janjoonweb.domain.JJChapterService;
import com.starit.janjoonweb.ui.mb.converter.JJChapterConverter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.springframework.beans.factory.annotation.Autowired;

privileged aspect JJChapterConverter_Roo_Converter{

declare parents:JJChapterConverter implements Converter;

declare @type:JJChapterConverter:@FacesConverter("com.starit.janjoonweb.ui.mb.converter.JJChapterConverter");

@Autowired JJChapterService JJChapterConverter.jJChapterService;

public Object JJChapterConverter.getAsObject(FacesContext context,UIComponent component,String value){if(value==null||value.length()==0){return null;}Long id=Long.parseLong(value);return jJChapterService.findJJChapter(id);}

}

package com.starit.janjoonweb.ui.mb.converterInputTexte;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.starit.janjoonweb.domain.JJJob;

@FacesConverter("JJJobConverter")
public class JJJobConverter implements Converter{

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	  public String getAsString(FacesContext context, UIComponent component, Object value) {
      return value instanceof JJJob ? ((JJJob) value).getName() : "";
  }
}
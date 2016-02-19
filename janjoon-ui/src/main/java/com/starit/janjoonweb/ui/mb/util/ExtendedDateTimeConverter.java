package com.starit.janjoonweb.ui.mb.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.FacesConverter;

@FacesConverter("ExtendedDateTimeConverter")
public class ExtendedDateTimeConverter extends DateTimeConverter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		setPattern((String) component.getAttributes().get("pattern"));
		// setTimeZone(TimeZone.getTimeZone("CET"));
		return super.getAsObject(context, component, value);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		setPattern((String) component.getAttributes().get("pattern"));

		// for (String s : TimeZone.getAvailableIDs())
		// System.err.println(s);
		// setTimeZone(TimeZone.getTimeZone("CET"));
		return super.getAsString(context, component, value);
	}

}

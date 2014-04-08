package com.starit.janjoonweb.ui.mb.util;

import java.util.TimeZone;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.FacesConverter;

@FacesConverter("extendedDateTimeConverter")
public class ExtendedDateTimeConverter extends DateTimeConverter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        setPattern((String) component.getAttributes().get("pattern"));
        setTimeZone(TimeZone.getTimeZone((String) component.getAttributes().get("timeZone")));
        return super.getAsObject(context, component, value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        setPattern((String) component.getAttributes().get("pattern"));
        setTimeZone(TimeZone.getTimeZone((String) component.getAttributes().get("timeZone")));
        return super.getAsString(context, component, value);
    }

}

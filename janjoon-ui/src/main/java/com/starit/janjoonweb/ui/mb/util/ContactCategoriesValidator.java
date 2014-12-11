package com.starit.janjoonweb.ui.mb.util;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("listCategoriesValidator")
public class ContactCategoriesValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
       
    	List<Object> selectedItemscheckbox = (List<Object>) value;
       
    	if(selectedItemscheckbox.size() >3)
    		throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,"Max 3 item allowed",null));
    }

	
}
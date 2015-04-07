package com.starit.janjoonweb.ui.mb.util;

import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import com.starit.janjoonweb.domain.JJCategory;

@FacesValidator("listCategoriesValidator")
public class ContactCategoriesValidator implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {

		try {
			List<Object> selectedItemscheckbox = (List<Object>) value;

			if (component.getId().contains("categories")
					&& selectedItemscheckbox.size() > 3) {
				
				FacesMessage facesMessage = MessageFactory.getMessage(
     					"validator_contact_max3Item", "Contact");
     			facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);				
     			throw new ValidatorException(facesMessage);				

			} else if (selectedItemscheckbox == null
					|| selectedItemscheckbox.isEmpty()) {
				FacesMessage facesMessage = MessageFactory.getMessage(
     					"validator_contact_shouldSelectContat", "Contact");
     			facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);				
     			throw new ValidatorException(facesMessage);	
			}

		} catch (ClassCastException e) {

			Set<Object> selectedItemscheckbox = (Set<Object>) value;

			if (component.getId().contains("categories")
					&& selectedItemscheckbox.size() > 3) {

				FacesMessage facesMessage = MessageFactory.getMessage(
     					"validator_contact_max3Item", "Contact");
     			facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);				
     			throw new ValidatorException(facesMessage);			

			} else if (selectedItemscheckbox == null
					|| selectedItemscheckbox.isEmpty()) {
				FacesMessage facesMessage = MessageFactory.getMessage(
     					"validator_contact_shouldSelectContat", "Contact");
     			facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);				
     			throw new ValidatorException(facesMessage);	
			}

		}

	}
}
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

			if(component.getId().contains("categories"))
			{
				if (selectedItemscheckbox.size() > 3)
					throw new ValidatorException(
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Max 3 item allowed", null));
			}else 
			{
				if (selectedItemscheckbox == null
						|| selectedItemscheckbox.isEmpty())
					throw new ValidatorException(new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"You Should select a contact", null));
			}
			

		} catch (ClassCastException e) {
			
			Set<Object> selectedItemscheckbox = (Set<Object>) value;

			if(component.getId().contains("categories"))
			{
				if (selectedItemscheckbox.size() > 3)
					throw new ValidatorException(
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Max 3 item allowed", null));
			}else 
			{
				if (selectedItemscheckbox == null
						|| selectedItemscheckbox.isEmpty())
					throw new ValidatorException(new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"You Should select a contact", null));
			}
		}

	}

}
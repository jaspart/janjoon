package com.starit.janjoonweb.ui.mb.util;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import com.starit.janjoonweb.domain.JJVersion;

@FacesValidator("bugValidator")
public class BugValidator implements Validator {

	@Override
	public void validate(FacesContext facesContext, UIComponent component, Object object)
			throws ValidatorException {
		
		JJVersion version=(JJVersion) component.getAttributes().get("jJbugVersion");
		if(version == null)
		{
			if(object == null)
			{
				throw new ValidatorException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"You Should Set one value from Build or Version",
						null));
			}
		}
		
		
	}

}

package com.starit.janjoonweb.ui.mb.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;

import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@FacesValidator("passwordValidator")
public class PasswordValidator implements Validator {

	@Override
	public void validate(FacesContext facesContext, UIComponent component,
			Object object) throws ValidatorException {

		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);

		if (!((String) object)
				.equals((String) session.getAttribute("password"))) {
			FacesMessage facesMessage = MessageFactory
					.getMessage("contact_passwordDialog_error", "Contact");
			facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(facesMessage);
		}

	}

}

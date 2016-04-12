package com.starit.janjoonweb.ui.mb.validator;

import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@FacesValidator("listCategoriesValidator")
public class ContactCategoriesValidator implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {

		try {
			@SuppressWarnings("unchecked")
			List<Object> selectedItemscheckbox = (List<Object>) value;

			if (component.getId().contains("categories")
					&& selectedItemscheckbox.size() > 3) {

				FacesMessage facesMessage = MessageFactory
						.getMessage("validator_contact_max3Item", "Contact");
				facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(facesMessage);

			} else if ((selectedItemscheckbox == null
					|| selectedItemscheckbox.isEmpty())
					&& !context.getExternalContext().getRequestParameterMap()
							.get("javax.faces.source").contains("sprintcts")) {
				FacesMessage facesMessage = MessageFactory.getMessage(
						"validator_contact_shouldSelectContat", "Contact");
				facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(facesMessage);
			}

		} catch (ClassCastException e) {

			@SuppressWarnings("unchecked")
			Set<Object> selectedItemscheckbox = (Set<Object>) value;

			if (component.getId().contains("categories")
					&& selectedItemscheckbox.size() > 3) {

				FacesMessage facesMessage = MessageFactory
						.getMessage("validator_contact_max3Item", "Contact");
				facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(facesMessage);
			} else if ((selectedItemscheckbox == null
					|| selectedItemscheckbox.isEmpty())
					&& !context.getExternalContext().getRequestParameterMap()
							.get("javax.faces.source").contains("sprintcts")) {
				FacesMessage facesMessage = MessageFactory.getMessage(
						"validator_contact_shouldSelectContat", "Contact");
				facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(facesMessage);
			}

		}

	}
}
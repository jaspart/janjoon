package com.starit.janjoonweb.ui.mb.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.ui.mb.JJContactBean;
import com.starit.janjoonweb.ui.mb.LoginBean;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@FacesValidator("emailValidator")
public class EmailValidator implements Validator {

	private final static String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	private final static Pattern EMAIL_COMPILED_PATTERN = Pattern
			.compile(EMAIL_PATTERN);

	/**
	 * Validate method.
	 */
	public void validate(FacesContext fc, UIComponent c, Object o)
			throws ValidatorException {
		// No value is not ok
		if (o == null || "".equals((String) o)) {

			FacesMessage facesMessage = MessageFactory
					.getMessage("validator_email_emailRequired", "Email");
			facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(facesMessage);

		}

		// The email matcher
		Matcher matcher = EMAIL_COMPILED_PATTERN.matcher(((String) o).trim());

		if (!matcher.matches()) { // Email doesn't match

			FacesMessage facesMessage = MessageFactory
					.getMessage("validator_email_emailInvalid", "Email");
			facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(facesMessage);
		}
		JJContact con = (JJContact) c.getAttributes().get("contact");
		if (con != null) {
			if (LoginBean.findBean("jJContactBean") == null) {
				HttpSession session = (HttpSession) fc.getExternalContext()
						.getSession(false);
				session.setAttribute("jJContactBean", new JJContactBean());
			}
			if (!((JJContactBean) LoginBean.findBean("jJContactBean"))
					.emailValid(o.toString().trim(), con)) {
				FacesMessage facesMessage = MessageFactory
						.getMessage("validator_email_emailExist", "Email");
				facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(facesMessage);
			}
		}
	}
}
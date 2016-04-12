package com.starit.janjoonweb.ui.mb.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;

import com.starit.janjoonweb.domain.JJFlowStep;
import com.starit.janjoonweb.ui.mb.JJFlowStepBean;
import com.starit.janjoonweb.ui.mb.LoginBean;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@FacesValidator("flowStepLevelValidator")
public class FlowStepLevelValidator implements Validator {

	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object arg2)
			throws ValidatorException {
		
		JJFlowStep flowStep = (JJFlowStep) arg1.getAttributes().get("flowStep");
		if (flowStep != null) {
			if (LoginBean.findBean("jJFlowStepBean") == null) {
				HttpSession session = (HttpSession) arg0.getExternalContext()
						.getSession(false);
				session.setAttribute("jJFlowStepBean", new JJFlowStepBean());
			}
			if (!((JJFlowStepBean) LoginBean.findBean("jJFlowStepBean"))
					.levelValid(Integer.parseInt(arg2.toString().trim()), flowStep)) {
				FacesMessage facesMessage = MessageFactory
						.getMessage("validator_email_emailExist", "Email");
				facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(facesMessage);
			}
		}
		
	}

}

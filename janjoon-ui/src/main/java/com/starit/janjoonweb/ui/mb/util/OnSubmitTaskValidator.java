package com.starit.janjoonweb.ui.mb.util;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import com.starit.janjoonweb.ui.mb.JJSprintBean;
import com.starit.janjoonweb.ui.mb.JJTaskBean;
import com.starit.janjoonweb.ui.mb.LoginBean;

@FacesValidator("onSubmitTaskValidator")
public class OnSubmitTaskValidator implements Validator {

	@Override
	public void validate(FacesContext arg0, UIComponent component, Object value)
			throws ValidatorException {

		String type = (String) component.getAttributes().get("type");
		if (type.equalsIgnoreCase("workload")) {
			Integer workload = (Integer) value;
			if (workload == null) {
				FacesMessage facesMessage = MessageFactory
						.getMessage("validator_task_workloadRequired", "Task");
				facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(facesMessage);

			}
			if (workload <= 0) {
				FacesMessage facesMessage = MessageFactory
						.getMessage("validator_task_workloadNegatif", "Task");
				facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(facesMessage);
			}

		}

		if (type.equalsIgnoreCase("startDate")) {
			Date startDate = (Date) value;
			if (startDate == null) {
				FacesMessage facesMessage = MessageFactory
						.getMessage("validator_task_startDateRequired", "Task");
				facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(facesMessage);

			} else {
				if (((JJTaskBean) LoginBean.findBean("jJTaskBean")).getMode()
						.equalsIgnoreCase("scrum")) {
					if (startDate.before(
							((JJSprintBean) LoginBean.findBean("jJSprintBean"))
									.getSprintUtil().getSprint()
									.getStartDate())) {
						FacesMessage facesMessage = MessageFactory.getMessage(
								"validator_task_startBeforeSprintStart",
								"Task");
						facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
						throw new ValidatorException(facesMessage);
					} else if (startDate.after(
							((JJSprintBean) LoginBean.findBean("jJSprintBean"))
									.getSprintUtil().getSprint()
									.getEndDate())) {
						FacesMessage facesMessage = MessageFactory.getMessage(
								"validator_task_endAfterSprintEnd", "Task");
						facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
						throw new ValidatorException(facesMessage);
					}
				} else {
					if (((JJTaskBean) LoginBean.findBean("jJTaskBean"))
							.getSprint() != null) {
						if (startDate.before(
								((JJTaskBean) LoginBean.findBean("jJTaskBean"))
										.getSprint().getStartDate())) {
							FacesMessage facesMessage = MessageFactory
									.getMessage(
											"validator_task_startBeforeSprintStart",
											"Task");
							facesMessage
									.setSeverity(FacesMessage.SEVERITY_ERROR);
							throw new ValidatorException(facesMessage);
						} else if (startDate.after(
								((JJTaskBean) LoginBean.findBean("jJTaskBean"))
										.getSprint().getEndDate())) {
							FacesMessage facesMessage = MessageFactory
									.getMessage(
											"validator_task_endAfterSprintEnd",
											"Task");
							facesMessage
									.setSeverity(FacesMessage.SEVERITY_ERROR);
							throw new ValidatorException(facesMessage);
						}

					}
				}

			}

		}

	}

}

package com.starit.janjoonweb.ui.mb.util;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.primefaces.component.calendar.Calendar;

import com.starit.janjoonweb.domain.JJSprint;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.ui.mb.JJSprintBean;
import com.starit.janjoonweb.ui.mb.JJTaskBean.TaskData;
import com.starit.janjoonweb.ui.mb.LoginBean;

@FacesValidator("dateRangeValidator")
public class DateRangeValidator implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		if (value == null) {
			return; // Let required="true" handle.
		}

		Date endDate = (Date) value;
		TaskData taskData = (TaskData) component.getAttributes()
				.get("taskData");
		JJTask task = (JJTask) component.getAttributes().get("task");
		String edition = (String) component.getAttributes().get("edition");
		UIInput startDateComponent = (UIInput) component.getAttributes().get(
				"startDateComponent");

		if (taskData == null && startDateComponent != null) {

			if (!startDateComponent.isValid()) {
				return; // Already invalidated. Don't care about it then.
			}

			Date startDate = (Date) startDateComponent.getValue();

			if (startDate == null) {
				return; // Let required="true" handle.
			}

			if (startDate.after(endDate)) {
				startDateComponent.setValid(false);
				throw new ValidatorException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Start date may not be after end date.", null));
			}

		} else {

			if (edition != null) {
				if (task == null) {
					if (edition.equalsIgnoreCase("endDateReal")) {
						Date starDate = taskData.getTask().getStartDateReal();

						if (starDate == null) {
							return; // Let required="true" handle.
						}

						if (starDate.after(endDate)) {
							throw new ValidatorException(
									new FacesMessage(
											FacesMessage.SEVERITY_ERROR,
											"Start date Real may not be after end date Real.",
											null));
						}

						if (taskData.getTask().getSprint() != null)
							if (endDate.after(taskData.getTask().getSprint()
									.getEndDate())) {
								throw new ValidatorException(
										new FacesMessage(
												FacesMessage.SEVERITY_ERROR,
												"End Date Real may not be after Sprint end Date.",
												null));
							}

					}
					if (edition.equalsIgnoreCase("startDate")) {
						Date end = taskData.getEndDate();

						if (endDate.after(end)) {
							throw new ValidatorException(new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"Start date may not be after end date.",
									null));
						}

						if (taskData.getTask().getSprint() != null)
							if (endDate.before(taskData.getTask().getSprint()
									.getStartDate())) {
								throw new ValidatorException(
										new FacesMessage(
												FacesMessage.SEVERITY_ERROR,
												"Start Date may not be before Sprint Start Date.",
												null));
							}

					}
					if (edition.equalsIgnoreCase("endDate")) {
						Date start = taskData.getStartDate();

						if (start.after(endDate)) {
							throw new ValidatorException(new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"End date may not be Before Start date.",
									null));
						}

						if (taskData.getTask().getSprint() != null)
							if (endDate.after(taskData.getTask().getSprint()
									.getEndDate())) {
								throw new ValidatorException(
										new FacesMessage(
												FacesMessage.SEVERITY_ERROR,
												"End Date may not be after Sprint end Date.",
												null));
							}
					}
					if (edition.equalsIgnoreCase("startDateReal")) {
						Date end = taskData.getEndDate();

						if (end.before(endDate)) {
							throw new ValidatorException(
									new FacesMessage(
											FacesMessage.SEVERITY_ERROR,
											"Start date Real may not be after end date Real.",
											null));
						}
						if (taskData.getTask().getSprint() != null)
							if (endDate.before(taskData.getTask().getSprint()
									.getStartDate())) {
								throw new ValidatorException(
										new FacesMessage(
												FacesMessage.SEVERITY_ERROR,
												"Start Date may not be before Sprint Start Date.",
												null));
							}
					}
				} else {
					// View Task ValidatOr
					UIInput sprintComponent = (UIInput) component
							.getAttributes().get("sprintComponent");
					JJSprint sprint = (JJSprint) sprintComponent.getValue();

					if (edition.equalsIgnoreCase("startDatePlanned")) {

						Calendar taskEndDatePlanned = (Calendar) component
								.getAttributes().get("taskEndDatePlanned");
						Date end = (Date) taskEndDatePlanned.getValue();

						if (endDate == null) {
							return; // Let required="true" handle.
						}

						if (endDate.after(end)) {

							throw new ValidatorException(new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"Start date may not be after end date .",
									null));

						}

						if (sprint != null)

							if (endDate.before(sprint.getStartDate())) {
								throw new ValidatorException(
										new FacesMessage(
												FacesMessage.SEVERITY_ERROR,
												"Start date may not be Before Sprint start Date.",
												null));
							}

					}
					if (edition.equalsIgnoreCase("endDatePlanned")) {

						Calendar taskStartDatePlanned = (Calendar) component
								.getAttributes().get("taskStartDatePlanned");

						Date start = (Date) taskStartDatePlanned.getValue();

						if (endDate == null) {
							return; // Let required="true" handle.
						}

						if (endDate.before(start)) {
							throw new ValidatorException(new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"End Date may not be Before Start Date.",
									null));
						}

						if (sprint != null)
							if (endDate.after(sprint.getEndDate())) {

								throw new ValidatorException(
										new FacesMessage(
												FacesMessage.SEVERITY_ERROR,
												"End Date may not be after Sprint End Date.",
												null));
							}

					}

				}

			} else {
				String mode = (String) component.getAttributes().get("mode");
				if (mode.equalsIgnoreCase("scrum")) {
					JJSprintBean sprintBean = (JJSprintBean) LoginBean
							.findBean("jJSprintBean");
					if (sprintBean.getSprintUtil().getSprint() != null)
						if (endDate.before(sprintBean.getSprintUtil()
								.getSprint().getStartDate())) {
							throw new ValidatorException(
									new FacesMessage(
											FacesMessage.SEVERITY_ERROR,
											"Start Date may not be before Sprint Start Date.",
											null));
						}

				} else {
					JJSprint sprint = (JJSprint) component.getAttributes().get(
							"sprint");

					if (sprint != null)
						if (endDate.before(sprint.getStartDate())) {
							throw new ValidatorException(
									new FacesMessage(
											FacesMessage.SEVERITY_ERROR,
											"Start Date may not be before Sprint Start Date.",
											null));
						}

				}

			}

		}

	}

}
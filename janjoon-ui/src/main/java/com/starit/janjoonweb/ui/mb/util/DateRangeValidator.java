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

import com.starit.janjoonweb.domain.JJProject;
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

		Date testedDate = (Date) value;
		TaskData taskData = (TaskData) component.getAttributes()
				.get("taskData");
		JJTask task = (JJTask) component.getAttributes().get("task");
		String edition = (String) component.getAttributes().get("edition");
		String sprintMode = (String) component.getAttributes()
				.get("sprintMode");
		UIInput startDateComponent = (UIInput) component.getAttributes().get(
				"startDateComponent");
		UIInput sprintComponent = (UIInput) component.getAttributes().get(
				"sprintComponent");

		if (taskData == null && startDateComponent == null
				&& sprintComponent == null) {
			// sprintStartDate
			if (sprintMode != null
					&& LoginBean.getProject().getStartDate() != null
					&& LoginBean.getProject().getStartDate().after(testedDate)) {

				FacesMessage facesMessage = MessageFactory.getMessage(
						"validator_date_startBeforeStart", "Sprint", "Project");
				facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(facesMessage);
			} else if (sprintMode != null
					&& LoginBean.getProject().getEndDate() != null
					&& LoginBean.getProject().getEndDate().before(testedDate)) {

				FacesMessage facesMessage = MessageFactory.getMessage(
						"validator_date_endAfterEnd", "Sprint", "Project");
				facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(facesMessage);
			}

		} else if (taskData == null && startDateComponent != null) {

			// sprintEndDate&&ProjectEndDate
			if (!startDateComponent.isValid()) {
				return; // Already invalidated. Don't care about it then.
			}

			Date startDate = (Date) startDateComponent.getValue();

			if (startDate == null) {
				return; // Let required="true" handle.
			}

			// sprintEndDate
			if (sprintMode != null) {
				if (LoginBean.getProject().getEndDate() != null
						&& LoginBean.getProject().getEndDate()
								.before(testedDate)) {
					FacesMessage facesMessage = MessageFactory.getMessage(
							"validator_date_endAfterEnd", "Sprint", "Project");
					facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
					throw new ValidatorException(facesMessage);
				} else if (LoginBean.getProject().getStartDate() != null
						&& LoginBean.getProject().getStartDate()
								.after(testedDate)) {
					FacesMessage facesMessage = MessageFactory.getMessage(
							"validator_date_endBeforeStart", "Sprint",
							"Project");
					facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
					throw new ValidatorException(facesMessage);
				} else if (startDate.after(testedDate)) {
					startDateComponent.setValid(false);
					FacesMessage facesMessage = MessageFactory.getMessage(
							"validator_date_startAfterEnd", "Sprint", "Sprint");
					facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
					throw new ValidatorException(facesMessage);
				}
				// ProjectEndDate
			} else if (startDate.after(testedDate)) {
				startDateComponent.setValid(false);
				FacesMessage facesMessage = MessageFactory.getMessage(
						"validator_date_startAfterEnd", "Project", "Project");
				facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(facesMessage);
			}

		} else {

			if (edition != null) {
				if (task == null) {
					if (edition.equalsIgnoreCase("endDateReal")) {
						Date starDate = taskData.getTask().getStartDateReal();

						if (starDate == null) {
							return; // Let required="true" handle.
						}

						if (starDate != null && starDate.after(testedDate)) {
							FacesMessage facesMessage = MessageFactory
									.getMessage("validator_date_startAfterEnd",
											"Task", "Task");
							facesMessage
									.setSeverity(FacesMessage.SEVERITY_ERROR);
							throw new ValidatorException(facesMessage);
						}

						if (taskData.getTask().getSprint() != null) {
							if (testedDate.compareTo(taskData.getTask()
									.getSprint().getStartDate())
									* taskData.getTask().getSprint()
											.getEndDate().compareTo(testedDate) < 0) {							
								FacesMessage facesMessage = MessageFactory.getMessage(
										"validator_date_endAfterEndOREndBeforeStart", "Task",
										"Sprint");
								facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
								throw new ValidatorException(facesMessage);
							}
						} else {
							if (taskData.getTask().getProject() != null
									&& taskData.getTask().getProject()
											.getEndDate() != null
									&& taskData.getTask().getProject()
											.getEndDate().before(testedDate)) {
								FacesMessage facesMessage = MessageFactory.getMessage(
										"validator_date_endAfterEnd", "Task", "Project");
								facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
								throw new ValidatorException(facesMessage);
							} else if (taskData.getTask().getProject() != null
									&& taskData.getTask().getProject()
											.getStartDate() != null
									&& taskData.getTask().getProject()
											.getStartDate().after(testedDate)) {
								
								FacesMessage facesMessage = MessageFactory.getMessage(
										"validator_date_endBeforeStart", "Task", "Project");
								facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
								throw new ValidatorException(facesMessage);
								
							}
						}

					}
					if (edition.equalsIgnoreCase("startDate")) {
						Date end = taskData.getEndDate();

						if (end != null && testedDate.after(end)) {
							FacesMessage facesMessage = MessageFactory
									.getMessage("validator_date_startAfterEnd",
											"Task", "Task");
							facesMessage
									.setSeverity(FacesMessage.SEVERITY_ERROR);
							throw new ValidatorException(facesMessage);
						}

						if (taskData.getTask().getSprint() != null) {
							if (testedDate.compareTo(taskData.getTask()
									.getSprint().getStartDate())
									* taskData.getTask().getSprint()
											.getEndDate().compareTo(testedDate) < 0) {
								FacesMessage facesMessage = MessageFactory.getMessage(
										"validator_date_StartAfterEndORStartBeforeStart", "Task",
										"Sprint");
								facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
								throw new ValidatorException(facesMessage);
							}
						} else {
							if (taskData.getTask().getProject() != null
									&& taskData.getTask().getProject()
											.getStartDate() != null
									&& taskData.getTask().getProject()
											.getStartDate().after(testedDate)) {
								FacesMessage facesMessage = MessageFactory.getMessage(
										"validator_date_startBeforeStart", "Task", "Project");
								facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
								throw new ValidatorException(facesMessage);
							} else if (taskData.getTask().getProject() != null
									&& taskData.getTask().getProject()
											.getEndDate() != null
									&& taskData.getTask().getProject()
											.getEndDate().before(testedDate)) {
								FacesMessage facesMessage = MessageFactory.getMessage(
										"validator_date_startAfterEnd", "Task", "Project");
								facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
								throw new ValidatorException(facesMessage);
							}
						}

					}
					if (edition.equalsIgnoreCase("endDate")) {
						Date start = taskData.getStartDate();

						if (start != null && start.after(testedDate)) {
							FacesMessage facesMessage = MessageFactory
									.getMessage("validator_date_endBeforeStart",
											"Task", "Task");
							facesMessage
									.setSeverity(FacesMessage.SEVERITY_ERROR);
							throw new ValidatorException(facesMessage);
						}

						if (taskData.getTask().getSprint() != null) {
							if (testedDate.compareTo(taskData.getTask()
									.getSprint().getStartDate())
									* taskData.getTask().getSprint()
											.getEndDate().compareTo(testedDate) < 0) {
								FacesMessage facesMessage = MessageFactory.getMessage(
										"validator_date_endAfterEndOREndBeforeStart", "Task",
										"Sprint");
								facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
								throw new ValidatorException(facesMessage);
							}
						} else {
							if (taskData.getTask().getProject() != null
									&& taskData.getTask().getProject()
											.getEndDate() != null
									&& taskData.getTask().getProject()
											.getEndDate().before(testedDate)) {
								FacesMessage facesMessage = MessageFactory.getMessage(
										"validator_date_endAfterEnd", "Task", "Project");
								facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
								throw new ValidatorException(facesMessage);
							} else if (taskData.getTask().getProject() != null
									&& taskData.getTask().getProject()
											.getStartDate() != null
									&& taskData.getTask().getProject()
											.getStartDate().after(testedDate)) {
								FacesMessage facesMessage = MessageFactory.getMessage(
										"validator_date_endBeforeStart", "Task", "Project");
								facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
								throw new ValidatorException(facesMessage);
							}
						}

					}
					if (edition.equalsIgnoreCase("startDateReal")) {
						Date end = taskData.getTask().getEndDateReal();

						if (end != null && end.before(testedDate)) {
							FacesMessage facesMessage = MessageFactory
									.getMessage("validator_date_startAfterEnd",
											"Task", "Task");
							facesMessage
									.setSeverity(FacesMessage.SEVERITY_ERROR);
							throw new ValidatorException(facesMessage);
						}
						if (taskData.getTask().getSprint() != null) {
							if (testedDate.compareTo(taskData.getTask()
									.getSprint().getStartDate())
									* taskData.getTask().getSprint()
											.getEndDate().compareTo(testedDate) < 0) {
								FacesMessage facesMessage = MessageFactory.getMessage(
										"validator_date_startAfterEndORStartBeforeStart", "Task",
										"Sprint");
								facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
								throw new ValidatorException(facesMessage);
							}
						} else {
							if (taskData.getTask().getProject() != null
									&& taskData.getTask().getProject()
											.getStartDate() != null
									&& taskData.getTask().getProject()
											.getStartDate().after(testedDate)) {
								FacesMessage facesMessage = MessageFactory.getMessage(
										"validator_date_startBeforeStart", "Task", "Project");
								facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
								throw new ValidatorException(facesMessage);
							} else if (taskData.getTask().getProject() != null
									&& taskData.getTask().getProject()
											.getEndDate() != null
									&& taskData.getTask().getProject()
											.getEndDate().before(testedDate)) {
								FacesMessage facesMessage = MessageFactory.getMessage(
										"validator_date_startAfterEnd", "Task", "Project");
								facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
								throw new ValidatorException(facesMessage);
							}
						}

					}
				} else {
					// View Task ValidatOr
					JJSprint sprint = (JJSprint) sprintComponent.getValue();

					if (edition.equalsIgnoreCase("startDatePlanned")) {

						Calendar taskEndDatePlanned = (Calendar) component
								.getAttributes().get("taskEndDatePlanned");
						Date end = (Date) taskEndDatePlanned.getValue();

						if (testedDate == null) {
							return; // Let required="true" handle.
						}

						if (testedDate.after(end)) {

							FacesMessage facesMessage = MessageFactory.getMessage(
			     					"validator_date_startAfterEnd", "Task","Task");
			     			facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);				
			     			throw new ValidatorException(facesMessage);	
						}

						if (sprint != null) {
							if (testedDate.compareTo(sprint.getStartDate())
									* sprint.getEndDate().compareTo(testedDate) < 0) {
								FacesMessage facesMessage = MessageFactory.getMessage(
										"validator_date_startAfterEndORStartBeforeStart", "Task",
										"Sprint");
								facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
								throw new ValidatorException(facesMessage);
							}
						} else {
							if (task.getProject() != null
									&& task.getProject().getStartDate() != null
									&& task.getProject().getStartDate()
											.after(testedDate)) {
								FacesMessage facesMessage = MessageFactory.getMessage(
										"validator_date_startBeforeStart", "Task", "Project");
								facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
								throw new ValidatorException(facesMessage);

							} else if (task.getProject() != null
									&& task.getProject().getEndDate() != null
									&& task.getProject().getEndDate()
											.before(testedDate)) {
								FacesMessage facesMessage = MessageFactory.getMessage(
										"validator_date_startAfterEnd", "Task", "Project");
								facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
								throw new ValidatorException(facesMessage);

							}
						}

					}
					if (edition.equalsIgnoreCase("endDatePlanned")) {

						Calendar taskStartDatePlanned = (Calendar) component
								.getAttributes().get("taskStartDatePlanned");

						Date start = (Date) taskStartDatePlanned.getValue();

						if (testedDate == null) {
							return; // Let required="true" handle.
						}

						if (testedDate.before(start)) {
							FacesMessage facesMessage = MessageFactory
									.getMessage("validator_date_endBeforeStart",
											"Task", "Task");
							facesMessage
									.setSeverity(FacesMessage.SEVERITY_ERROR);
							throw new ValidatorException(facesMessage);
						}

						if (sprint != null) {
							if (testedDate.compareTo(sprint.getStartDate())
									* sprint.getEndDate().compareTo(testedDate) < 0) {
								FacesMessage facesMessage = MessageFactory.getMessage(
										"validator_date_endAfterEndOREndBeforeStart", "Task",
										"Sprint");
								facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
								throw new ValidatorException(facesMessage);
							}
						} else {
							if (task.getProject() != null
									&& task.getProject().getEndDate() != null
									&& task.getProject().getEndDate()
											.before(testedDate)) {
								FacesMessage facesMessage = MessageFactory.getMessage(
										"validator_date_endAfterEnd", "Task", "Project");
								facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
								throw new ValidatorException(facesMessage);
							} else if (task.getProject() != null
									&& task.getProject().getStartDate() != null
									&& task.getProject().getStartDate()
											.after(testedDate)) {
								FacesMessage facesMessage = MessageFactory.getMessage(
										"validator_date_endBeforeStart", "Task", "Project");
								facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
								throw new ValidatorException(facesMessage);
							}

						}

					}
				}

			} else {
				String mode = (String) component.getAttributes().get("mode");
				if (mode.equalsIgnoreCase("scrum")) {
					JJSprintBean sprintBean = (JJSprintBean) LoginBean
							.findBean("jJSprintBean");
					if (sprintBean.getSprintUtil().getSprint() != null) {
						if (testedDate.before(sprintBean.getSprintUtil()
								.getSprint().getStartDate())) {
							FacesMessage facesMessage = MessageFactory.getMessage(
									"validator_date_startBeforeStart", "Task", "Sprint");
							facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
							throw new ValidatorException(facesMessage);
						}
					}

				} else {
					JJSprint sprint = (JJSprint) component.getAttributes().get(
							"sprint");

					if (sprint != null) {
						if (testedDate.before(sprint.getStartDate())) {
							FacesMessage facesMessage = MessageFactory.getMessage(
									"validator_date_startBeforeStart", "Task", "Sprint");
							facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
							throw new ValidatorException(facesMessage);
						}
					}

				}

			}

		}

	}

}
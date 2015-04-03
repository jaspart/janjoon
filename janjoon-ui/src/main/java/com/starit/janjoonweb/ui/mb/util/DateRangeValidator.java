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
				throw new ValidatorException(
						new FacesMessage(
								FacesMessage.SEVERITY_ERROR,
								"Sprint Start date may not be before Project Start date.",
								null));
			} else if (sprintMode != null
					&& LoginBean.getProject().getEndDate() != null
					&& LoginBean.getProject().getEndDate().before(testedDate)) {
				throw new ValidatorException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Sprint Start date may not be after Project end date.",
						null));
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
					throw new ValidatorException(
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"Sprint End date may not be after Project end date.",
									null));
				} else if (LoginBean.getProject().getStartDate() != null
						&& LoginBean.getProject().getStartDate()
								.after(testedDate)) {
					throw new ValidatorException(
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"Sprint End date may not be before Project start date.",
									null));
				} else if (startDate.after(testedDate)) {
					startDateComponent.setValid(false);
					throw new ValidatorException(new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Start date may not be after end date.", null));
				}
				// ProjectEndDate
			} else if (startDate.after(testedDate)) {
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

						if (starDate != null && starDate.after(testedDate)) {
							throw new ValidatorException(
									new FacesMessage(
											FacesMessage.SEVERITY_ERROR,
											"Start date Real may not be after end date Real.",
											null));
						}

						if (taskData.getTask().getSprint() != null) {
							if (testedDate.compareTo(taskData.getTask().getSprint().getStartDate())
									* taskData.getTask().getSprint().getEndDate().compareTo(testedDate) < 0) {
								throw new ValidatorException(
										new FacesMessage(
												FacesMessage.SEVERITY_ERROR,
												"End Date Real may not be after Sprint end Date or Before Sprint Start date.",
												null));
							}
						} else {
							if (taskData.getTask().getProject() != null
									&& taskData.getTask().getProject()
											.getEndDate() != null
									&& taskData.getTask().getProject()
											.getEndDate().before(testedDate)) {
								throw new ValidatorException(
										new FacesMessage(
												FacesMessage.SEVERITY_ERROR,
												"End Date Real may not be after Project end Date.",
												null));
							} else if (taskData.getTask().getProject() != null
									&& taskData.getTask().getProject()
											.getStartDate() != null
									&& taskData.getTask().getProject()
											.getStartDate().after(testedDate)) {
								throw new ValidatorException(
										new FacesMessage(
												FacesMessage.SEVERITY_ERROR,
												"End Date Real may not be before Project start Date.",
												null));
							}
						}

					}
					if (edition.equalsIgnoreCase("startDate")) {
						Date end = taskData.getEndDate();

						if (end != null && testedDate.after(end)) {
							throw new ValidatorException(new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"Start date may not be after end date.",
									null));
						}

						if (taskData.getTask().getSprint() != null) {
							if (testedDate.compareTo(taskData.getTask().getSprint().getStartDate())
									* taskData.getTask().getSprint().getEndDate().compareTo(testedDate) < 0) {
								throw new ValidatorException(
										new FacesMessage(
												FacesMessage.SEVERITY_ERROR,
												"Start Date may not be before Sprint Start Date or After Sprint End date.",
												null));
							}
						} else {
							if (taskData.getTask().getProject() != null
									&& taskData.getTask().getProject()
											.getStartDate() != null
									&& taskData.getTask().getProject()
											.getStartDate().after(testedDate)) {
								throw new ValidatorException(
										new FacesMessage(
												FacesMessage.SEVERITY_ERROR,
												"Start Date may not be before Project Start Date.",
												null));
							} else if (taskData.getTask().getProject() != null
									&& taskData.getTask().getProject()
											.getEndDate() != null
									&& taskData.getTask().getProject()
											.getEndDate().before(testedDate)) {
								throw new ValidatorException(
										new FacesMessage(
												FacesMessage.SEVERITY_ERROR,
												"Start Date may not be after Project End Date.",
												null));
							}
						}

					}
					if (edition.equalsIgnoreCase("endDate")) {
						Date start = taskData.getStartDate();

						if (start != null && start.after(testedDate)) {
							throw new ValidatorException(new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"End date may not be Before Start date.",
									null));
						}

						if (taskData.getTask().getSprint() != null) {
							if (testedDate.compareTo(taskData.getTask().getSprint().getStartDate())
									* taskData.getTask().getSprint().getEndDate().compareTo(testedDate) < 0) {
								throw new ValidatorException(
										new FacesMessage(
												FacesMessage.SEVERITY_ERROR,
												"End Date may not be after Sprint end Date or before Sprint Start Date.",
												null));
							}
						} else{ if (taskData.getTask().getProject() != null
								&& taskData.getTask().getProject().getEndDate() != null
								&& taskData.getTask().getProject().getEndDate()
										.before(testedDate)) {
							throw new ValidatorException(
									new FacesMessage(
											FacesMessage.SEVERITY_ERROR,
											"End Date may not be after Project end Date.",
											null));
						}else if (taskData.getTask().getProject() != null
								&& taskData.getTask().getProject().getStartDate() != null
								&& taskData.getTask().getProject().getStartDate()
										.after(testedDate)) {
							throw new ValidatorException(
									new FacesMessage(
											FacesMessage.SEVERITY_ERROR,
											"End Date may not be before Project Start Date.",
											null));
						}}

					}
					if (edition.equalsIgnoreCase("startDateReal")) {
						Date end = taskData.getTask().getEndDateReal();

						if (end != null && end.before(testedDate)) {
							throw new ValidatorException(
									new FacesMessage(
											FacesMessage.SEVERITY_ERROR,
											"Start date Real may not be after end date Real.",
											null));
						}
						if (taskData.getTask().getSprint() != null) {
							if (testedDate.compareTo(taskData.getTask().getSprint().getStartDate())
									* taskData.getTask().getSprint().getEndDate().compareTo(testedDate) < 0) {
								throw new ValidatorException(
										new FacesMessage(
												FacesMessage.SEVERITY_ERROR,
												"Start Date Real may not be before Sprint Start Date or After Sprint end Date.",
												null));
							}
						} else { if (taskData.getTask().getProject() != null
								&& taskData.getTask().getProject()
										.getStartDate() != null
								&& taskData.getTask().getProject()
										.getStartDate().after(testedDate)) {
							throw new ValidatorException(
									new FacesMessage(
											FacesMessage.SEVERITY_ERROR,
											"Start Date Real may not be before Project Start Date.",
											null));
						}else if (taskData.getTask().getProject() != null
								&& taskData.getTask().getProject()
								.getEndDate() != null
						&& taskData.getTask().getProject()
								.getEndDate().before(testedDate)) {
					throw new ValidatorException(
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"Start Date Real may not be after Project End Date.",
									null));
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

							throw new ValidatorException(new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"Start date may not be after end date .",
									null));

						}

						if (sprint != null) {
							if (testedDate.compareTo(sprint.getStartDate())
									* sprint.getEndDate().compareTo(testedDate) < 0) {
								throw new ValidatorException(
										new FacesMessage(
												FacesMessage.SEVERITY_ERROR,
												"Start date may not be Before Sprint start Date or after sprint end Date.",
												null));
							}
						} else {
							if (task.getProject() != null
									&& task.getProject().getStartDate() != null
									&& task.getProject().getStartDate()
											.after(testedDate)) {
								throw new ValidatorException(
										new FacesMessage(
												FacesMessage.SEVERITY_ERROR,
												"Start date may not be Before Project start Date.",
												null));

							} else if (task.getProject() != null
									&& task.getProject().getEndDate() != null
									&& task.getProject().getEndDate()
											.before(testedDate)) {
								throw new ValidatorException(
										new FacesMessage(
												FacesMessage.SEVERITY_ERROR,
												"Start date may not be after Project end Date.",
												null));

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
							throw new ValidatorException(new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"End Date may not be Before Start Date.",
									null));
						}

						if (sprint != null) {
							if (testedDate.compareTo(sprint.getStartDate())
									* sprint.getEndDate().compareTo(testedDate) < 0) {

								throw new ValidatorException(
										new FacesMessage(
												FacesMessage.SEVERITY_ERROR,
												"End Date may not be after Sprint End Date or before sprint Start date.",
												null));
							}
						} else {
							if (task.getProject() != null
									&& task.getProject().getEndDate() != null
									&& task.getProject().getEndDate()
											.before(testedDate)) {
								throw new ValidatorException(
										new FacesMessage(
												FacesMessage.SEVERITY_ERROR,
												"End date may not be After Project End Date.",
												null));
							} else if (task.getProject() != null
									&& task.getProject().getStartDate() != null
									&& task.getProject().getStartDate()
											.after(testedDate)) {
								throw new ValidatorException(
										new FacesMessage(
												FacesMessage.SEVERITY_ERROR,
												"end date may not be before Project start Date.",
												null));
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
							throw new ValidatorException(
									new FacesMessage(
											FacesMessage.SEVERITY_ERROR,
											"Start Date may not be before Sprint Start Date.",
											null));
						}
					}

				} else {
					JJSprint sprint = (JJSprint) component.getAttributes().get(
							"sprint");

					if (sprint != null) {
						if (testedDate.before(sprint.getStartDate())) {
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

}
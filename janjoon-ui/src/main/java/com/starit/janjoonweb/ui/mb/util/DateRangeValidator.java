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
import org.primefaces.component.spinner.Spinner;

import com.starit.janjoonweb.domain.JJSprint;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.ui.mb.JJSprintBean;
import com.starit.janjoonweb.ui.mb.JJTaskBean;
import com.starit.janjoonweb.ui.mb.JJTaskBean.TaskData;
import com.starit.janjoonweb.ui.mb.LoginBean;

@FacesValidator("dateRangeValidator")
public class DateRangeValidator implements Validator {

	private void validateSprintStartDate(Date testedDate) {

		if (LoginBean.getProject().getStartDate() != null && LoginBean.getProject().getStartDate().after(testedDate)) {

			FacesMessage facesMessage = MessageFactory.getMessage("validator_date_startBeforeStart", "Sprint",
			        MessageFactory.getMessage("label_project", "").getDetail());
			facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(facesMessage);
		} else if (LoginBean.getProject().getEndDate() != null
		        && CalendarUtil.getAfterDay(LoginBean.getProject().getEndDate()).before(testedDate)) {

			FacesMessage facesMessage = MessageFactory.getMessage("validator_date_endAfterEnd", "Sprint",
			        MessageFactory.getMessage("label_project", "").getDetail());
			facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(facesMessage);
		}

	}

	private void validateSprintEndDate(UIInput startDateComponent, String sprintMode, Date testedDate) {

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
			        && CalendarUtil.getAfterDay(LoginBean.getProject().getEndDate()).before(testedDate)) {
				FacesMessage facesMessage = MessageFactory.getMessage("validator_date_endAfterEnd", "Sprint",
				        MessageFactory.getMessage("label_project", "").getDetail());
				facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(facesMessage);
			} else if (LoginBean.getProject().getStartDate() != null
			        && LoginBean.getProject().getStartDate().after(testedDate)) {
				FacesMessage facesMessage = MessageFactory.getMessage("validator_date_endBeforeStart", "Sprint",
				        MessageFactory.getMessage("label_project", "").getDetail());
				facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(facesMessage);
			} else if (startDate.after(testedDate)) {
				startDateComponent.setValid(false);
				FacesMessage facesMessage = MessageFactory.getMessage("validator_date_startAfterEnd", "Sprint",
				        "Sprint");
				facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(facesMessage);
			}
			// ProjectEndDate
		} else if (startDate.after(testedDate)) {
			startDateComponent.setValid(false);
			FacesMessage facesMessage = MessageFactory.getMessage("validator_date_startAfterEnd",
			        MessageFactory.getMessage("label_project", "").getDetail(),
			        MessageFactory.getMessage("label_project", "").getDetail());
			facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(facesMessage);
		}

	}

	private void validateTaskDates_Sprint(UIComponent component, Date testedDate) {

		String mode = (String) component.getAttributes().get("mode");
		if (mode.equalsIgnoreCase("scrum")) {
			JJSprintBean sprintBean = (JJSprintBean) LoginBean.findBean("jJSprintBean");
			if (sprintBean.getSprintUtil().getSprint() != null) {
				if (testedDate.before(sprintBean.getSprintUtil().getSprint().getStartDate())) {
					FacesMessage facesMessage = MessageFactory.getMessage("validator_date_startBeforeStart",
					        MessageFactory.getMessage("label_task", "").getDetail(), "Sprint");
					facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
					throw new ValidatorException(facesMessage);
				}
			}

		} else {
			JJSprint sprint = (JJSprint) component.getAttributes().get("sprint");

			if (sprint != null) {
				if (testedDate.before(sprint.getStartDate())) {
					FacesMessage facesMessage = MessageFactory.getMessage("validator_date_startBeforeStart",
					        MessageFactory.getMessage("label_task", "").getDetail(), "Sprint");
					facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
					throw new ValidatorException(facesMessage);
				}
			}

		}

	}

	private void validateTaskData_startDate(TaskData taskData, Date testedDate) {
		Date end = taskData.getEndDate();

		if (end != null && testedDate.after(end)) {
			FacesMessage facesMessage = MessageFactory.getMessage("validator_date_startAfterEnd",
			        MessageFactory.getMessage("label_task", "").getDetail(),
			        MessageFactory.getMessage("label_task", "").getDetail());
			facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(facesMessage);
		}

		if (taskData.getTask().getSprint() != null) {
			// if (testedDate.compareTo(taskData.getTask().getSprint()
			// .getStartDate())
			// * taskData.getTask().getSprint().getEndDate()
			// .compareTo(testedDate) < 0) {
			// FacesMessage facesMessage = MessageFactory
			// .getMessage(
			// "validator_date_StartAfterEndORStartBeforeStart",
			// MessageFactory.getMessage("label_task", "")
			// .getDetail(), "Sprint");
			// facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
			// throw new ValidatorException(facesMessage);
			// }
		} else {
			if (taskData.getTask().getProject() != null && taskData.getTask().getProject().getStartDate() != null
			        && taskData.getTask().getProject().getStartDate().after(testedDate)) {
				FacesMessage facesMessage = MessageFactory.getMessage("validator_date_startBeforeStart",
				        MessageFactory.getMessage("label_task", "").getDetail(),
				        MessageFactory.getMessage("label_project", "").getDetail());
				facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(facesMessage);
			} else if (taskData.getTask().getProject() != null && taskData.getTask().getProject().getEndDate() != null
			        && taskData.getTask().getProject().getEndDate().before(testedDate)) {
				FacesMessage facesMessage = MessageFactory.getMessage("validator_date_startAfterEnd",
				        MessageFactory.getMessage("label_task", "").getDetail(),
				        MessageFactory.getMessage("label_project", "").getDetail());
				facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(facesMessage);
			}
		}

	}

	private void validateTaskData_startDateReal(TaskData taskData, Date testedDate) {
		Date end = taskData.getTask().getEndDateReal();

		if (end != null && end.before(testedDate)) {
			FacesMessage facesMessage = MessageFactory.getMessage("validator_date_startAfterEnd",
			        MessageFactory.getMessage("label_task", "").getDetail(),
			        MessageFactory.getMessage("label_task", "").getDetail());
			facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(facesMessage);
		}
		if (taskData.getTask().getSprint() != null) {
			// if (testedDate.compareTo(taskData.getTask().getSprint()
			// .getStartDate())
			// * taskData.getTask().getSprint().getEndDate()
			// .compareTo(testedDate) < 0) {
			// FacesMessage facesMessage = MessageFactory
			// .getMessage(
			// "validator_date_startAfterEndORStartBeforeStart",
			// MessageFactory.getMessage("label_task", "")
			// .getDetail(), "Sprint");
			// facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
			// throw new ValidatorException(facesMessage);
			// }
		} else {
			if (taskData.getTask().getProject() != null && taskData.getTask().getProject().getStartDate() != null
			        && taskData.getTask().getProject().getStartDate().after(testedDate)) {
				FacesMessage facesMessage = MessageFactory.getMessage("validator_date_startBeforeStart",
				        MessageFactory.getMessage("label_task", "").getDetail(),
				        MessageFactory.getMessage("label_project", "").getDetail());
				facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(facesMessage);
			} else if (taskData.getTask().getProject() != null && taskData.getTask().getProject().getEndDate() != null
			        && taskData.getTask().getProject().getEndDate().before(testedDate)) {
				FacesMessage facesMessage = MessageFactory.getMessage("validator_date_startAfterEnd",
				        MessageFactory.getMessage("label_task", "").getDetail(),
				        MessageFactory.getMessage("label_project", "").getDetail());
				facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(facesMessage);
			}
		}

	}

	private void validateTaskData_endDate(TaskData taskData, Date testedDate) {
		Date start = taskData.getStartDate();

		if (start != null && start.after(testedDate)) {
			FacesMessage facesMessage = MessageFactory.getMessage("validator_date_endBeforeStart",
			        MessageFactory.getMessage("label_task", "").getDetail(),
			        MessageFactory.getMessage("label_task", "").getDetail());
			facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(facesMessage);
		}

		if (taskData.getTask().getSprint() != null) {
			// if (testedDate.compareTo(taskData.getTask().getSprint()
			// .getStartDate())
			// * taskData.getTask().getSprint().getEndDate()
			// .compareTo(testedDate) < 0) {
			// FacesMessage facesMessage = MessageFactory
			// .getMessage(
			// "validator_date_endAfterEndOREndBeforeStart",
			// MessageFactory.getMessage("label_task", "")
			// .getDetail(), "Sprint");
			// facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
			// throw new ValidatorException(facesMessage);
			// }
		} else {
			if (taskData.getTask().getProject() != null && taskData.getTask().getProject().getEndDate() != null
			        && taskData.getTask().getProject().getEndDate().before(testedDate)) {
				FacesMessage facesMessage = MessageFactory.getMessage("validator_date_endAfterEnd",
				        MessageFactory.getMessage("label_task", "").getDetail(),
				        MessageFactory.getMessage("label_project", "").getDetail());
				facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(facesMessage);
			} else if (taskData.getTask().getProject() != null && taskData.getTask().getProject().getStartDate() != null
			        && taskData.getTask().getProject().getStartDate().after(testedDate)) {
				FacesMessage facesMessage = MessageFactory.getMessage("validator_date_endBeforeStart",
				        MessageFactory.getMessage("label_task", "").getDetail(),
				        MessageFactory.getMessage("label_project", "").getDetail());
				facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(facesMessage);
			}
		}

	}

	private void validateTaskData_endDateReal(TaskData taskData, Date testedDate) {
		Date starDate = taskData.getTask().getStartDateReal();

		if (starDate == null) {
			return; // Let required="true" handle.
		}

		if (starDate != null && starDate.after(testedDate)) {
			FacesMessage facesMessage = MessageFactory.getMessage("validator_date_startAfterEnd",
			        MessageFactory.getMessage("label_task", "").getDetail(),
			        MessageFactory.getMessage("label_task", "").getDetail());
			facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(facesMessage);
		}

		if (taskData.getTask().getSprint() != null) {
			// if (testedDate.compareTo(taskData.getTask().getSprint()
			// .getStartDate())
			// * taskData.getTask().getSprint().getEndDate()
			// .compareTo(testedDate) < 0) {
			// FacesMessage facesMessage = MessageFactory
			// .getMessage(
			// "validator_date_endAfterEndOREndBeforeStart",
			// MessageFactory.getMessage("label_task", "")
			// .getDetail(), "Sprint");
			// facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
			// throw new ValidatorException(facesMessage);
			// }
		} else {
			if (taskData.getTask().getProject() != null && taskData.getTask().getProject().getEndDate() != null
			        && taskData.getTask().getProject().getEndDate().before(testedDate)) {
				FacesMessage facesMessage = MessageFactory.getMessage("validator_date_endAfterEnd",
				        MessageFactory.getMessage("label_task", "").getDetail(),
				        MessageFactory.getMessage("label_project", "").getDetail());
				facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(facesMessage);
			} else if (taskData.getTask().getProject() != null && taskData.getTask().getProject().getStartDate() != null
			        && taskData.getTask().getProject().getStartDate().after(testedDate)) {

				FacesMessage facesMessage = MessageFactory.getMessage("validator_date_endBeforeStart",
				        MessageFactory.getMessage("label_task", "").getDetail(),
				        MessageFactory.getMessage("label_project", "").getDetail());
				facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(facesMessage);

			}
		}

	}

	private void validateTask_startDatePlanned(UIComponent component, Date testedDate, JJSprint sprint, JJTask task) {

		Calendar taskEndDatePlanned = (Calendar) component.getAttributes().get("taskEndDatePlanned");
		Date end = (Date) taskEndDatePlanned.getValue();

		if (testedDate == null) {
			return; // Let required="true" handle.
		}

		if (testedDate.after(end)) {

			FacesMessage facesMessage = MessageFactory.getMessage("validator_date_startAfterEnd",
			        MessageFactory.getMessage("label_task", "").getDetail(),
			        MessageFactory.getMessage("label_task", "").getDetail());
			facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(facesMessage);
		}

		if (sprint != null) {
			// if (testedDate.compareTo(sprint.getStartDate())
			// * CalendarUtil.getAfterDay(sprint.getEndDate()).compareTo(
			// testedDate) < 0) {
			// // FacesMessage facesMessage = MessageFactory
			// // .getMessage(
			// // "validator_date_startAfterEndORStartBeforeStart",
			// // MessageFactory.getMessage("label_task", "")
			// // .getDetail(), "Sprint");
			// // facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
			// // throw new ValidatorException(facesMessage);
			//
			// testedDate = sprint.getStartDate();
			// ((Calendar) component).setValue(testedDate);
			// task.setStartDatePlanned(testedDate);
			// }
		} else {
			if (task.getProject() != null && task.getProject().getStartDate() != null
			        && task.getProject().getStartDate().after(testedDate)) {
				FacesMessage facesMessage = MessageFactory.getMessage("validator_date_startBeforeStart",
				        MessageFactory.getMessage("label_task", "").getDetail(),
				        MessageFactory.getMessage("label_project", "").getDetail());
				facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(facesMessage);

			} else if (task.getProject() != null && task.getProject().getEndDate() != null
			        && CalendarUtil.getAfterDay(task.getProject().getEndDate()).before(testedDate)) {
				FacesMessage facesMessage = MessageFactory.getMessage("validator_date_startAfterEnd",
				        MessageFactory.getMessage("label_task", "").getDetail(),
				        MessageFactory.getMessage("label_project", "").getDetail());
				facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(facesMessage);

			}
		}

	}

	private void validateTask_endDatePlanned(UIComponent component, Date testedDate, JJSprint sprint, JJTask task) {

		Calendar taskStartDatePlanned = (Calendar) component.getAttributes().get("taskStartDatePlanned");
		Spinner workload = (Spinner) component.getAttributes().get("workload");

		Date start = (Date) taskStartDatePlanned.getValue();

		if (testedDate == null) {
			return; // Let required="true" handle.
		}

		if (testedDate.before(start)) {
			FacesMessage facesMessage = MessageFactory.getMessage("validator_date_endBeforeStart",
			        MessageFactory.getMessage("label_task", "").getDetail(),
			        MessageFactory.getMessage("label_task", "").getDetail());
			facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(facesMessage);
		}

		if (sprint != null) {
			// if (testedDate.compareTo(sprint.getStartDate())
			// * CalendarUtil.getAfterDay(sprint.getEndDate()).compareTo(
			// testedDate) < 0) {
			// // FacesMessage facesMessage = MessageFactory
			// // .getMessage(
			// // "validator_date_endAfterEndOREndBeforeStart",
			// // MessageFactory.getMessage("label_task", "")
			// // .getDetail(), "Sprint");
			// // facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
			// // throw new ValidatorException(facesMessage);
			// ContactCalendarUtil calendarUtil;
			// if (task.getAssignedTo() != null) {
			// calendarUtil = new ContactCalendarUtil(task.getAssignedTo());
			//
			// } else {
			// calendarUtil = new ContactCalendarUtil(task.getProject()
			// .getManager().getCompany());
			//
			// }
			//
			// if (workload.getValue() != null)
			// task.setWorkloadPlanned((Integer) workload.getValue());
			//
			// calendarUtil.getEndDate(task, JJTaskBean.Planned, null);
			// testedDate = task.getEndDatePlanned();
			// ((Calendar) component).setValue(testedDate);
			//
			// ((JJTaskBean) LoginBean.findBean("jJTaskBean")).setTask(task);
			//
			// }
		} else {
			if (task.getProject() != null && task.getProject().getEndDate() != null
			        && CalendarUtil.getAfterDay(task.getProject().getEndDate()).before(testedDate)) {
				FacesMessage facesMessage = MessageFactory.getMessage("validator_date_endAfterEnd",
				        MessageFactory.getMessage("label_task", "").getDetail(),
				        MessageFactory.getMessage("label_project", "").getDetail());
				facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(facesMessage);
			} else if (task.getProject() != null && task.getProject().getStartDate() != null
			        && task.getProject().getStartDate().after(testedDate)) {
				FacesMessage facesMessage = MessageFactory.getMessage("validator_date_endBeforeStart",
				        MessageFactory.getMessage("label_task", "").getDetail(),
				        MessageFactory.getMessage("label_project", "").getDetail());
				facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(facesMessage);
			}

		}

	}

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		if (value == null) {
			return; // Let required="true" handle.
		}

		Date testedDate = (Date) value;
		TaskData taskData = (TaskData) component.getAttributes().get("taskData");
		JJTask task = (JJTask) component.getAttributes().get("task");
		String edition = (String) component.getAttributes().get("edition");
		String sprintMode = (String) component.getAttributes().get("sprintMode");
		UIInput startDateComponent = (UIInput) component.getAttributes().get("startDateComponent");
		UIInput sprintComponent = (UIInput) component.getAttributes().get("sprintComponent");

		if (taskData == null && startDateComponent == null && sprintComponent == null) {
			if (sprintMode != null)
				validateSprintStartDate(testedDate);
		} else if (taskData == null && startDateComponent != null) {
			validateSprintEndDate(startDateComponent, sprintMode, testedDate);
		} else {

			if (edition != null) {
				if (task == null) {
					if (edition.equalsIgnoreCase("endDateReal")) {
						validateTaskData_endDateReal(taskData, testedDate);
					}
					if (edition.equalsIgnoreCase("startDate")) {
						validateTaskData_startDate(taskData, testedDate);
					}
					if (edition.equalsIgnoreCase("endDate")) {
						validateTaskData_endDate(taskData, testedDate);
					}
					if (edition.equalsIgnoreCase("startDateReal")) {
						validateTaskData_startDateReal(taskData, testedDate);
					}
				} else {
					// View Task ValidatOr
					JJSprint sprint = (JJSprint) sprintComponent.getValue();
					if (edition.equalsIgnoreCase("startDatePlanned")) {
						validateTask_startDatePlanned(component, testedDate, sprint, task);
					}
					if (edition.equalsIgnoreCase("endDatePlanned")) {
						validateTask_endDatePlanned(component, testedDate, sprint, task);
					}
				}

			} else {
				validateTaskDates_Sprint(component, testedDate);
			}

		}

	}

}
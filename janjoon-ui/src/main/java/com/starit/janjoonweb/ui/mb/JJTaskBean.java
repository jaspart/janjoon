package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.ListDataModel;
import javax.servlet.http.HttpSession;

import org.primefaces.event.RowEditEvent;
import org.primefaces.extensions.model.timeline.TimelineEvent;
import org.primefaces.extensions.model.timeline.TimelineModel;
import org.primefaces.model.SelectableDataModel;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJTask.class, beanName = "jJTaskBean")
public class JJTaskBean {

	private List<TaskData> tasksData;

	private TimelineModel model;
	private Date start;
	private Date end;

	private List<JJContact> contacts;

	private JJProject project;

	public List<TaskData> getTasksData() {
		return tasksData;
	}

	public void setTasksData(List<TaskData> tasksData) {
		this.tasksData = tasksData;
	}

	public TimelineModel getModel() {
		return model;
	}

	public Date getStart() {
		return start;
	}

	public Date getEnd() {
		return end;
	}

	public List<JJContact> getContacts() {
		contacts = jJContactService.getContacts(null, true);
		return contacts;
	}

	public void setContacts(List<JJContact> contacts) {
		this.contacts = contacts;
	}

	public JJProject getProject() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJProjectBean jJProjectBean = (JJProjectBean) session
				.getAttribute("jJProjectBean");
		this.project = jJProjectBean.getProject();
		return project;
	}

	public void setProject(JJProject project) {
		this.project = project;
	}

	public void loadData() {
		getProject();

		List<JJTask> tasks = jJTaskService.getTasks(project, null, null, true,
				true);
		Date startDate = new Date();

		tasksData = new ArrayList<TaskData>();

		for (JJTask task : tasks) {
			// task.setStartDatePlanned(startDate);
			// Date endDate = new Date(startDate.getTime()
			// + task.getWorkloadPlanned() * 60 * 60 * 1000);
			// task.setEndDatePlanned(endDate);

			// TaskData taskData = new TaskData(task, startDate, endDate);
			TaskData taskData;
			if (task.getStartDateReal() != null) {
				taskData = new TaskData(task, task.getStartDateReal(),
						task.getEndDateReal(), true);
			} else {
				taskData = new TaskData(task, task.getStartDatePlanned(),
						task.getEndDatePlanned(), false);
			}
			tasksData.add(taskData);
		}

		createTimeLineModel(startDate, tasks);
	}

	private void createTimeLineModel(Date now, List<JJTask> tasks) {
		// Set initial start / end dates for the axis of the timeline
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

		// Before 4 hours for now
		cal.setTimeInMillis(now.getTime() - 4 * 60 * 60 * 1000);
		start = cal.getTime();

		// After 8 hours for now
		cal.setTimeInMillis(now.getTime() + 8 * 60 * 60 * 1000);
		end = cal.getTime();

		// Create timeline model
		model = new TimelineModel();

		List<TimelineEvent> timelineEvents = new ArrayList<TimelineEvent>();

		for (JJTask task : tasks) {
			// Date start = task.getCreationDate();
			// Date end = new Date(now.getTime() + task.getWorkloadPlanned() *
			// 60
			// * 60 * 1000);
			// timelineEvents.add(new TimelineEvent(task, now, end, true, task
			// .getName()));

			timelineEvents.add(new TimelineEvent("",
					task.getStartDatePlanned(), task.getEndDatePlanned(), true,
					task.getName(), "planned"));

			if (task.getStartDateReal() != null) {
				timelineEvents.add(new TimelineEvent("", task
						.getStartDateReal(), task.getEndDateReal(), true, task
						.getName(), "real"));
			}

			// model.add(new TimelineEvent(task, now, end, true,
			// task.getName()));
		}

		model.addAll(timelineEvents);
	}

	public void onRowEdit(RowEditEvent event) {

		String message = "";
		FacesMessage facesMessage = null;

		System.out.println("row select "
				+ ((TaskData) event.getObject()).getTask().getId());

		TaskData taskData = (TaskData) event.getObject();

		Date startDate = taskData.getStartDate();
		Date endDate = taskData.getEndDate();

		long startTime = startDate.getTime();
		long endTime = endDate.getTime();

		long str = endTime - startTime;
		System.out.println("str " + str);

		if (str > 0) {
			Date SD;
			Date ED;

			JJTask task = taskData.getTask();

			if (taskData.realDateType) {
				SD = task.getStartDateReal();
				ED = task.getEndDateReal();
			} else {
				SD = task.getStartDatePlanned();
				ED = task.getEndDatePlanned();
			}

			long ST = SD.getTime();
			long ET = ED.getTime();

			System.out.println("startTime :" + startTime + " ST " + ST);
			System.out.println("endTime :" + endTime + " ET " + ET);

			if ((startTime == ST) && (endTime == ET)) {
				System.out.println("Same Date");
			} else {
				task.setStartDateReal(startDate);
				task.setEndDateReal(endDate);

				int workloadReal = (int) (str / 3600000);
				System.out.println("workloadReal " + workloadReal);

				task.setWorkloadReal(workloadReal);
			}

			task.setUpdatedDate(new Date());
			jJTaskService.updateJJTask(task);

			loadData();

			message = "Success Update";
			facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO,
					message, "JJTask");

		} else {
			message = "The End Date must be greater than the Start Date";
			facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					message, "JJTask");
		}

		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}

	public void onCancel(RowEditEvent event) {

		FacesMessage facesMessage = MessageFactory.getMessage("Cancel Edit",
				"JJask");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}

	@SuppressWarnings("unchecked")
	public class TaskDataModel extends ListDataModel<TaskData> implements
			SelectableDataModel<TaskData> {

		public TaskDataModel(List<TaskData> data) {
			super(data);
		}

		@Override
		public TaskData getRowData(String rowKey) {
			// In a real app, a more efficient way like a query by rowKey should
			// be implemented to deal with huge data

			List<TaskData> tasksData = (List<TaskData>) getWrappedData();

			for (TaskData taskData : tasksData) {
				if (taskData.getTask().getName().equals(rowKey))
					return taskData;
			}

			return null;
		}

		@Override
		public Object getRowKey(TaskData taskData) {
			return taskData.getTask().getName();
		}
	}

	public class TaskData {
		JJTask task;
		Date startDate;
		Date endDate;
		boolean realDateType;

		public TaskData(JJTask task, Date startDate, Date endDate,
				boolean realDateType) {
			super();
			this.task = task;
			this.startDate = startDate;
			this.endDate = endDate;
			this.realDateType = realDateType;
		}

		public JJTask getTask() {
			return task;
		}

		public void setTask(JJTask task) {
			this.task = task;
		}

		public Date getStartDate() {
			return startDate;
		}

		public void setStartDate(Date startDate) {
			this.startDate = startDate;
		}

		public Date getEndDate() {
			return endDate;
		}

		public void setEndDate(Date endDate) {
			this.endDate = endDate;
		}

		public boolean getRealDateType() {
			return realDateType;
		}

		public void setRealDateType(boolean realDateType) {
			this.realDateType = realDateType;
		}
	}

}

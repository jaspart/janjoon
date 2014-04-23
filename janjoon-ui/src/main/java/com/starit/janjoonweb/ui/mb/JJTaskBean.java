package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.ListDataModel;
import javax.servlet.http.HttpSession;

import org.primefaces.component.api.UIColumn;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.extensions.event.timeline.TimelineModificationEvent;
import org.primefaces.extensions.event.timeline.TimelineRangeEvent;
import org.primefaces.extensions.model.timeline.TimelineEvent;
import org.primefaces.extensions.model.timeline.TimelineModel;
import org.primefaces.model.SelectableDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJChapter;
import com.starit.janjoonweb.domain.JJChapterService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJTask.class, beanName = "jJTaskBean")
public class JJTaskBean {

	@Autowired
	JJChapterService jJChapterService;

	public void setJjChapterService(JJChapterService jJChapterService) {
		this.jJChapterService = jJChapterService;
	}

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
		contacts = jJContactService.getContacts(null, false);
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

		// Set initial start / end dates for the axis of the timeline
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

		Date now = new Date();

		// Before 4 hours for now
		cal.setTimeInMillis(now.getTime() - 4 * 60 * 60 * 1000);
		start = cal.getTime();

		// After 8 hours for now
		cal.setTimeInMillis(now.getTime() + 8 * 60 * 60 * 1000);
		end = cal.getTime();

		// Create timeline model
		model = new TimelineModel();

		List<TimelineEvent> timelineEvents = new ArrayList<TimelineEvent>();

		getProject();
		tasksData = new ArrayList<TaskData>();

		// 65 = ascii A
		int k = 65;

		List<JJChapter> chapters = jJChapterService.getChapters(project, true,
				true);

		for (JJChapter chapter : chapters) {
			Map<Date, String> min = new TreeMap<Date, String>();
			Map<Date, String> max = new TreeMap<Date, String>();

			List<JJTask> tasks = jJTaskService.getTasks(project, null, null,
					chapter, true, true);

			for (JJTask task : tasks) {

				char c = (char) k;

				String group = "<span style=display:none>" + c + "</span>"
						+ task.getName();

				TimelineEvent event = new TimelineEvent(task,
						task.getStartDatePlanned(), task.getEndDatePlanned(),
						true, group, "planned");
				model.add(event);
				if (task.getStartDateReal() != null) {
					event = new TimelineEvent(task, task.getStartDateReal(),
							task.getEndDateReal(), true, group, "real");
					model.add(event);
				}

				TaskData taskData;

				if (task.getStartDateRevised() != null) {
					taskData = new TaskData(task, task.getStartDateRevised(),
							task.getEndDateRevised(),
							task.getWorkloadRevised(), true);
				} else {
					taskData = new TaskData(task, task.getStartDatePlanned(),
							task.getEndDatePlanned(),
							task.getWorkloadPlanned(), false);
				}

				tasksData.add(taskData);

				if (task.getStartDatePlanned() != null) {
					min.put(task.getStartDatePlanned(), task
							.getStartDatePlanned().toString());
				}

				if (task.getStartDateRevised() != null) {
					min.put(task.getStartDateRevised(), task
							.getStartDateRevised().toString());
				}

				if (task.getStartDateReal() != null) {
					min.put(task.getStartDateReal(), task.getStartDateReal()
							.toString());
				}

				if (task.getEndDatePlanned() != null) {
					max.put(task.getEndDatePlanned(), task.getEndDatePlanned()
							.toString());
				}

				if (task.getEndDateRevised() != null) {
					max.put(task.getEndDateRevised(), task.getEndDateRevised()
							.toString());
				}

				if (task.getEndDateReal() != null) {
					max.put(task.getEndDateReal(), task.getEndDateReal()
							.toString());
				}

				k++;
			}

			if (!min.isEmpty() && !max.isEmpty()) {

				Date end = null;
				Set<Date> dates = min.keySet();

				Date start = dates.iterator().next();
				System.out.println("start " + start);

				dates = max.keySet();
				for (Date date : dates) {
					end = date;
				}

				System.out.println("end " + end);

				JJTask task = new JJTask();
				task.setName(chapter.getName());

				char c = (char) k;

				String group = "<span style=display:none>" + c + "</span>"
						+ task.getName();
				TimelineEvent event = new TimelineEvent(task, start, end, true,
						group, "chapter");
				k++;

				model.add(event);
			}

		}

	}

	// public void onChange(TimelineRangeEvent e){
	// System.out.println("toto");
	// }

	public void onChange(TimelineModificationEvent e) {
		System.out.println("vovo");
		// get clone of the TimelineEvent to be changed with new start / end
		// dates

		// event = e.getTimelineEvent();

		// update booking in DB...

		// if everything was ok, no UI update is required. Only the model should
		// be updated
		// model.update(event);

		// FacesMessage msg =
		// new FacesMessage(FacesMessage.SEVERITY_INFO, "The booking dates " +
		// getRoom() + " have been updated", null);
		// FacesContext.getCurrentInstance().addMessage(null, msg);

		// otherwise (if DB operation failed) a rollback can be done with the
		// same response as follows:
		// TimelineEvent oldEvent = model.getEvent(model.getIndex(event));
		// TimelineUpdater timelineUpdater =
		// TimelineUpdater.getCurrentInstance(":mainForm:timeline");
		// model.update(oldEvent, timelineUpdater);
	}

	public void onCellEdit(CellEditEvent event) {
		UIColumn column = event.getColumn();
		String headerText = column.getHeaderText();
		System.out.println("headerText " + headerText);
		System.out.println(event.getSource());
		if (event.getSource() instanceof DataTable) {
			DataTable dataTable = (DataTable) event.getSource();
			if (dataTable.getRowData() instanceof TaskData) {
				System.out.println("yes");
			}
		}
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

			if (taskData.revisedDate) {
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
			reset();

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
		int workload;
		boolean revisedDate;

		public TaskData(JJTask task, Date startDate, Date endDate,
				int workload, boolean revisedDate) {
			super();
			this.task = task;
			this.startDate = startDate;
			this.endDate = endDate;
			this.workload = workload;
			this.revisedDate = revisedDate;
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

		public int getWorkload() {
			return workload;
		}

		public void setWorkload(int workload) {
			this.workload = workload;
		}

		public boolean getRevisedDate() {
			return revisedDate;
		}

		public void setRevisedDate(boolean revisedDate) {
			this.revisedDate = revisedDate;
		}
	}
	
	public void reset() {
		
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJSprintBean sprintBean=(JJSprintBean) session.getAttribute("jJSprintBean");
		
		if(sprintBean!=null)
		{
			if(getJJTask_().getSprint()!=null)
			{
				if(getJJTask_().getSprint().getProject()!=null && sprintBean.getProject()!=null)
				{
					if(sprintBean.getProject().equals(getJJTask_().getSprint().getProject()))
					{
						session.setAttribute("jJSprintBean", new JJSprintBean());
					}
				}
			}
		}
		
		setJJTask_(null);
        setSelectedMessages (null);
        setCreateDialogVisible(false);
    }

}

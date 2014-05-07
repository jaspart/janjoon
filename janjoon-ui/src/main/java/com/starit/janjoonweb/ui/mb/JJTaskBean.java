package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;
import javax.servlet.http.HttpSession;

import org.primefaces.component.api.UIColumn;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.selectmanymenu.SelectManyMenu;
import org.primefaces.component.tree.Tree;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.extensions.model.timeline.TimelineEvent;
import org.primefaces.extensions.model.timeline.TimelineModel;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJChapter;
import com.starit.janjoonweb.domain.JJChapterService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJSprint;
import com.starit.janjoonweb.domain.JJStatus;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJTestcase;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.ui.mb.converter.JJBugConverter;
import com.starit.janjoonweb.ui.mb.converter.JJContactConverter;
import com.starit.janjoonweb.ui.mb.converter.JJRequirementConverter;
import com.starit.janjoonweb.ui.mb.converter.JJSprintConverter;
import com.starit.janjoonweb.ui.mb.converter.JJStatusConverter;
import com.starit.janjoonweb.ui.mb.converter.JJTaskConverter;
import com.starit.janjoonweb.ui.mb.converter.JJTestcaseConverter;
import com.starit.janjoonweb.ui.mb.converter.JJVersionConverter;

@RooSerializable
@RooJsfManagedBean(entity = JJTask.class, beanName = "jJTaskBean")
public class JJTaskBean {

	@Autowired
	JJChapterService jJChapterService;

	private TreeNode taskTreeNode;

	public TreeNode getTaskTreeNode() {
		return taskTreeNode;
	}

	public void setTaskTreeNode(TreeNode taskTreeNode) {
		this.taskTreeNode = taskTreeNode;
	}

	private HtmlPanelGrid viewPanel;

	public HtmlPanelGrid getViewPanel() {
		return populateViewPanelGrid();
	}

	public void setViewPanel(HtmlPanelGrid viewPanel) {
		this.viewPanel = viewPanel;
	}

	public void setJjChapterService(JJChapterService jJChapterService) {
		this.jJChapterService = jJChapterService;
	}

	private List<TaskData> tasksData;
	private JJTask task;

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

	public JJTask getTask() {
		return task;
	}

	public void setTask(JJTask task) {
		this.task = task;
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

		System.out.println("loadData");
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

				if (task.getStartDateReal() != null) {
					TimelineEvent event = new TimelineEvent(task,
							task.getStartDateReal(), task.getEndDateReal(),
							true, group, "real");
					model.add(event);
				}

				TaskData taskData;

				if (task.getStartDateRevised() != null) {

					TimelineEvent event = new TimelineEvent(task,
							task.getStartDateRevised(),
							task.getEndDateRevised(), true, group, "planned");
					model.add(event);

					taskData = new TaskData(task, task.getStartDateRevised(),
							task.getEndDateRevised(),
							task.getWorkloadRevised(), true);
				} else {

					TimelineEvent event = new TimelineEvent(task,
							task.getStartDatePlanned(),
							task.getEndDatePlanned(), true, group, "planned");
					model.add(event);

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

		task = null;
	}

	public void onCellEdit(CellEditEvent event) {
		UIColumn column = event.getColumn();
		String headerText = column.getHeaderText();
		System.out.println("headerText " + headerText);

		DataTable dataTable = (DataTable) event.getSource();
		TaskData taskData = (TaskData) dataTable.getRowData();
		JJTask task = taskData.getTask();

		Object newValue = event.getNewValue();

		String message = "";
		FacesMessage facesMessage = null;

		boolean valid = true;

		if (headerText.equalsIgnoreCase("Start Date Planned/Revised")) {
			if (newValue != null) {
				Date date = (Date) newValue;
				if (taskData.getEndDate() != null) {
					long startTime = date.getTime();
					long endTime = taskData.getEndDate().getTime();
					long str = endTime - startTime;
					if (str >= 0) {
						int workloadRevised = (int) (str / 3600000);
						task.setStartDateRevised(date);
						task.setWorkloadRevised(workloadRevised);
						task.setEndDateRevised(taskData.getEndDate());
					} else {
						valid = false;
						message += "\nStart Date Revised doit être < End Date Revised";
					}
				} else {
					if (taskData.getWorkload() != null) {
						Date endDateRevised = new Date(date.getTime()
								+ taskData.getWorkload() * 3600000);
						task.setEndDateRevised(endDateRevised);
						task.setWorkloadRevised(taskData.getWorkload());
						task.setStartDateRevised(date);
					}
				}
			}

		} else if (headerText.equalsIgnoreCase("End Date Planned/Revised")) {
			if (newValue != null) {
				Date date = (Date) newValue;
				if (taskData.getStartDate() != null) {
					long startTime = taskData.getStartDate().getTime();
					long endTime = date.getTime();
					long str = endTime - startTime;
					if (str >= 0) {
						int workloadRevised = (int) (str / 3600000);
						task.setEndDateRevised(date);
						task.setWorkloadRevised(workloadRevised);
						task.setStartDateRevised(taskData.getStartDate());
					} else {
						valid = false;
						message += "\nEnd Date Revised doit être > Start Date Revised";
					}
				} else {
					if (taskData.getWorkload() != null) {
						Date startDateRevised = new Date(date.getTime()
								- taskData.getWorkload() * 3600000);

						task.setStartDateRevised(startDateRevised);
						task.setWorkloadRevised(taskData.getWorkload());
						task.setEndDateRevised(date);
					}
				}
			}

		} else if (headerText.equalsIgnoreCase("Workload Planned/Revised")) {

			if (newValue != null) {
				int workloadRevised = (int) newValue;
				task.setWorkloadRevised(workloadRevised);
				if ((taskData.getStartDate() != null && taskData.getEndDate() != null)
						|| (taskData.getStartDate() != null)) {
					Date endDateRevised = new Date(taskData.getStartDate()
							.getTime() + workloadRevised * 3600000);

					task.setEndDateRevised(endDateRevised);
					task.setStartDateRevised(taskData.getStartDate());

				} else if (taskData.getEndDate() != null) {
					Date startDateRevised = new Date(taskData.getEndDate()
							.getTime() - workloadRevised * 3600000);
					task.setStartDateRevised(startDateRevised);
					task.setEndDateRevised(taskData.getEndDate());
				}

			}

		} else if (headerText.equalsIgnoreCase("Start Date Real")) {
			if (newValue != null) {
				Date date = (Date) newValue;
				if (task.getEndDateReal() != null) {
					long startTime = date.getTime();
					long endTime = task.getEndDateReal().getTime();
					long str = endTime - startTime;
					if (str >= 0) {
						int workloadReal = (int) (str / 3600000);
						task.setStartDateReal(date);
						task.setWorkloadReal(workloadReal);
					} else {
						valid = false;
						message += "\nStart Date Real doit être < End Date Real";
					}
				} else {
					if (task.getWorkloadReal() != null) {
						Date endDateReal = new Date(task.getStartDateReal()
								.getTime() + task.getWorkloadReal() * 3600000);
						task.setEndDateReal(endDateReal);
					}
				}
			}

		} else if (headerText.equalsIgnoreCase("End Date Real")) {
			if (newValue != null) {
				Date date = (Date) newValue;
				if (task.getStartDateReal() != null) {
					long startTime = task.getStartDateReal().getTime();
					long endTime = date.getTime();
					long str = endTime - startTime;
					if (str >= 0) {
						int workloadReal = (int) (str / 3600000);
						task.setEndDateReal(date);
						task.setWorkloadReal(workloadReal);
					} else {
						valid = false;
						message += "\nEnd Date Real doit être > Start Date Real";
					}
				} else {
					if (task.getWorkloadReal() != null) {
						Date startDateReal = new Date(task.getEndDateReal()
								.getTime() - task.getWorkloadReal() * 3600000);
						task.setStartDateReal(startDateReal);
					}
				}
			}
		} else if (headerText.equalsIgnoreCase("Workload Real")) {
			if (newValue != null) {
				int workloadReal = (int) newValue;
				task.setWorkloadReal(workloadReal);
				if ((task.getStartDateReal() != null && task.getEndDateReal() != null)
						|| (task.getStartDateReal() != null)) {
					Date endDateReal = new Date(task.getStartDateReal()
							.getTime() + task.getWorkloadReal() * 3600000);
					task.setEndDateReal(endDateReal);

				} else if (task.getEndDateReal() != null) {
					Date startDateReal = new Date(task.getEndDateReal()
							.getTime() - task.getWorkloadReal() * 3600000);
					task.setStartDateReal(startDateReal);
				}

			}
		} else if (headerText.equalsIgnoreCase("Before Task")) {

			List<JJTask> newList = new ArrayList<JJTask>();
			List<JJTask> oldList = taskData.getStoreTasks();

			List<String> list = taskData.getSelectedTasks();
			for (String string : list) {
				long id = Long.valueOf(taskData.splitString(string, "-", 0));
				JJTask Task = jJTaskService.findJJTask(id);
				newList.add(Task);
			}

			if (!newList.isEmpty() && !oldList.isEmpty()) {

				for (JJTask Task : oldList) {
					if (!newList.contains(Task)) {
						Task.getAfterTasks().remove(task);
						task.getBeforeTasks().remove(Task);
					}
				}

				for (JJTask Task : newList) {
					if (!oldList.contains(Task)) {
						Task.getAfterTasks().add(task);
						task.getBeforeTasks().add(Task);
					}
				}

			} else if (oldList.isEmpty()) {

				for (JJTask Task : newList) {
					Task.getAfterTasks().add(task);
					task.getBeforeTasks().add(Task);
				}

			} else {
				for (JJTask Task : oldList) {
					Task.getAfterTasks().remove(task);
					task.getBeforeTasks().remove(Task);
				}

			}

		}

		if (valid) {
			task.setUpdatedDate(new Date());
			jJTaskService.updateJJTask(task);
			reset();

			message = "Success Update";
			facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO,
					message, "JJTask");
		} else {
			facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					message, "JJTask");
		}

		loadData();

		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}

	public void duplicateTask() {
		System.out.println("in duplicate");

		task = jJTaskService.findJJTask(task.getId());

		JJTask duplicatedTask = new JJTask();
		duplicatedTask.setParent(task);

		task.getTasks().add(duplicatedTask);

		duplicatedTask.setName(task.getName() + " duplicated");
		duplicatedTask.setDescription(task.getDescription());
		duplicatedTask.setCreationDate(task.getCreationDate());
		duplicatedTask.setCreatedBy(task.getCreatedBy());
		duplicatedTask.setUpdatedDate(task.getUpdatedDate());
		duplicatedTask.setUpdatedBy(task.getUpdatedBy());
		duplicatedTask.setEnabled(true);

		duplicatedTask.setCompleted(task.getCompleted());
		duplicatedTask.setStatus(task.getStatus());

		duplicatedTask.setStartDatePlanned(task.getStartDatePlanned());
		duplicatedTask.setStartDateReal(task.getStartDateReal());
		duplicatedTask.setStartDateRevised(task.getStartDateRevised());

		duplicatedTask.setEndDatePlanned(task.getEndDatePlanned());
		duplicatedTask.setEndDateReal(task.getEndDateReal());
		duplicatedTask.setEndDateRevised(task.getEndDateRevised());

		duplicatedTask.setWorkloadPlanned(task.getWorkloadPlanned());
		duplicatedTask.setWorkloadReal(task.getWorkloadReal());
		duplicatedTask.setWorkloadRevised(task.getWorkloadRevised());

		duplicatedTask.setConsumed(task.getConsumed());

		if (task.getRequirement() != null) {
			JJRequirement requirement = jJRequirementService
					.findJJRequirement(task.getRequirement().getId());
			duplicatedTask.setRequirement(requirement);
			requirement.getTasks().add(duplicatedTask);
		}

		if (task.getTestcase() != null) {
			JJTestcase testcase = jJTestcaseService.findJJTestcase(task
					.getTestcase().getId());
			duplicatedTask.setTestcase(testcase);
			testcase.getTasks().add(duplicatedTask);
		}

		if (task.getSprint() != null) {
			JJSprint sprint = jJSprintService.findJJSprint(task.getSprint()
					.getId());
			duplicatedTask.setSprint(sprint);
			sprint.getTasks().add(duplicatedTask);
		}

		if (task.getBug() != null) {
			JJBug bug = jJBugService.findJJBug(task.getBug().getId());
			duplicatedTask.setBug(bug);
			bug.getTasks().add(duplicatedTask);
		}

		if (task.getVersioning() != null) {
			JJVersion version = jJVersionService.findJJVersion(task
					.getVersioning().getId());
			duplicatedTask.setVersioning(version);
			version.getTasks().add(duplicatedTask);
		}

		jJTaskService.saveJJTask(duplicatedTask);

		duplicatedTask = null;
		loadData();

		reset();

		System.out.println("end duplication");
	}

	public class TaskData {
		private JJTask task;
		private Date startDate;
		private Date endDate;
		private Integer workload;
		private boolean revisedDate;

		private List<String> tasks;
		private List<String> selectedTasks;

		private List<JJTask> storeTasks;

		public TaskData(JJTask task, Date startDate, Date endDate,
				Integer workload, boolean revisedDate) {
			super();
			this.task = task;
			this.startDate = startDate;
			this.endDate = endDate;
			this.workload = workload;
			this.revisedDate = revisedDate;

			storeTasks = new ArrayList<JJTask>();
			Set<JJTask> taskList = task.getBeforeTasks();
			for (JJTask Task : taskList) {
				if (Task.getEnabled()
						&& Task.getRequirement().getChapter()
								.equals(task.getRequirement().getChapter())) {
					storeTasks.add(Task);
				}
			}

			List<JJTask> list = new ArrayList<JJTask>();
			list.addAll(jJTaskService.getTasks(project, null, null, task
					.getRequirement().getChapter(), true, false));

			list.remove(task);

			tasks = convertTaskListToStringList(list);

			selectedTasks = convertTaskListToStringList(storeTasks);
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

		public Integer getWorkload() {
			return workload;
		}

		public void setWorkload(Integer workload) {
			this.workload = workload;
		}

		public boolean getRevisedDate() {
			return revisedDate;
		}

		public void setRevisedDate(boolean revisedDate) {
			this.revisedDate = revisedDate;
		}

		public List<String> getTasks() {
			return tasks;
		}

		public void setTasks(List<String> tasks) {
			this.tasks = tasks;
		}

		public List<String> getSelectedTasks() {
			return selectedTasks;
		}

		public void setSelectedTasks(List<String> selectedTasks) {
			this.selectedTasks = selectedTasks;
		}

		public List<JJTask> getStoreTasks() {
			return storeTasks;
		}

		public void setStoreTasks(List<JJTask> storeTasks) {
			this.storeTasks = storeTasks;
		}

		private String splitString(String s, String regex, int index) {
			String[] result = s.split(regex);
			return result[index];
		}

		private List<String> convertTaskListToStringList(List<JJTask> tasks) {
			List<String> list = new ArrayList<String>();
			for (JJTask task : tasks) {
				String entry = task.getId() + "-" + task.getName();
				list.add(entry);
			}

			return list;
		}
	}

	public void reset() {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJSprintBean sprintBean = (JJSprintBean) session
				.getAttribute("jJSprintBean");

		if (sprintBean != null) {
			if (getJJTask_().getSprint() != null) {
				if (getJJTask_().getSprint().getProject() != null
						&& sprintBean.getProject() != null) {
					if (sprintBean.getProject().equals(
							getJJTask_().getSprint().getProject())) {
						session.setAttribute("jJSprintBean", new JJSprintBean());
					}
				}
			}
		}

		setJJTask_(null);
		setSelectedMessages(null);
		setCreateDialogVisible(false);
	}

	public HtmlPanelGrid populateViewPanelGrid() {
		
		
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Application application = facesContext
				.getApplication();
		ExpressionFactory expressionFactory = application
				.getExpressionFactory();
		ELContext elContext = facesContext.getELContext();

		HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application
				.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
		
		setJJTask_((JJTask) expressionFactory.createValueExpression(elContext,"#{jJDevelopment.task}", JJTask.class).getValue(elContext));
		taskTreeNode=null;
				

		HtmlOutputText nameLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		nameLabel.setId("nameLabel");
		nameLabel.setValue("Name:");
		htmlPanelGrid.getChildren().add(nameLabel);

		InputTextarea nameValue = (InputTextarea) application
				.createComponent(InputTextarea.COMPONENT_TYPE);
		nameValue.setId("nameValue");
		nameValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{jJTaskBean.JJTask_.name}",
						String.class));
		nameValue.setReadonly(true);
		nameValue.setDisabled(true);
		htmlPanelGrid.getChildren().add(nameValue);

		HtmlOutputText descriptionLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		descriptionLabel.setId("descriptionLabel");
		descriptionLabel.setValue("Description:");
		htmlPanelGrid.getChildren().add(descriptionLabel);

		InputTextarea descriptionValue = (InputTextarea) application
				.createComponent(InputTextarea.COMPONENT_TYPE);
		descriptionValue.setId("descriptionValue");
		descriptionValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJTaskBean.JJTask_.description}", String.class));
		descriptionValue.setReadonly(true);
		descriptionValue.setDisabled(true);
		htmlPanelGrid.getChildren().add(descriptionValue);

		HtmlOutputText startDatePlannedLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		startDatePlannedLabel.setId("startDatePlannedLabel");
		startDatePlannedLabel.setValue("Start Date Planned:");
		htmlPanelGrid.getChildren().add(startDatePlannedLabel);

		HtmlOutputText startDatePlannedValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		startDatePlannedValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJTaskBean.JJTask_.startDatePlanned}", Date.class));
		DateTimeConverter startDatePlannedValueConverter = (DateTimeConverter) application
				.createConverter(DateTimeConverter.CONVERTER_ID);
		startDatePlannedValueConverter.setPattern("dd/MM/yyyy");
		startDatePlannedValue.setConverter(startDatePlannedValueConverter);
		htmlPanelGrid.getChildren().add(startDatePlannedValue);

		HtmlOutputText endDatePlannedLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		endDatePlannedLabel.setId("endDatePlannedLabel");
		endDatePlannedLabel.setValue("End Date Planned:");
		htmlPanelGrid.getChildren().add(endDatePlannedLabel);

		HtmlOutputText endDatePlannedValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		endDatePlannedValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJTaskBean.JJTask_.endDatePlanned}", Date.class));
		DateTimeConverter endDatePlannedValueConverter = (DateTimeConverter) application
				.createConverter(DateTimeConverter.CONVERTER_ID);
		endDatePlannedValueConverter.setPattern("dd/MM/yyyy");
		endDatePlannedValue.setConverter(endDatePlannedValueConverter);
		htmlPanelGrid.getChildren().add(endDatePlannedValue);

		HtmlOutputText workloadPlannedLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		workloadPlannedLabel.setId("workloadPlannedLabel");
		workloadPlannedLabel.setValue("Workload Planned:");
		htmlPanelGrid.getChildren().add(workloadPlannedLabel);

		HtmlOutputText workloadPlannedValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		workloadPlannedValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJTaskBean.JJTask_.workloadPlanned}", String.class));
		htmlPanelGrid.getChildren().add(workloadPlannedValue);

		HtmlOutputText startDateRevisedLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		startDateRevisedLabel.setId("startDateRevisedLabel");
		startDateRevisedLabel.setValue("Start Date Revised:");
		htmlPanelGrid.getChildren().add(startDateRevisedLabel);

		HtmlOutputText startDateRevisedValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		startDateRevisedValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJTaskBean.JJTask_.startDateRevised}", Date.class));
		DateTimeConverter startDateRevisedValueConverter = (DateTimeConverter) application
				.createConverter(DateTimeConverter.CONVERTER_ID);
		startDateRevisedValueConverter.setPattern("dd/MM/yyyy");
		startDateRevisedValue.setConverter(startDateRevisedValueConverter);
		htmlPanelGrid.getChildren().add(startDateRevisedValue);

		HtmlOutputText endDateRevisedLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		endDateRevisedLabel.setId("endDateRevisedLabel");
		endDateRevisedLabel.setValue("End Date Revised:");
		htmlPanelGrid.getChildren().add(endDateRevisedLabel);

		HtmlOutputText endDateRevisedValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		endDateRevisedValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJTaskBean.JJTask_.endDateRevised}", Date.class));
		DateTimeConverter endDateRevisedValueConverter = (DateTimeConverter) application
				.createConverter(DateTimeConverter.CONVERTER_ID);
		endDateRevisedValueConverter.setPattern("dd/MM/yyyy");
		endDateRevisedValue.setConverter(endDateRevisedValueConverter);
		htmlPanelGrid.getChildren().add(endDateRevisedValue);

		HtmlOutputText workloadRevisedLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		workloadRevisedLabel.setId("workloadRevisedLabel");
		workloadRevisedLabel.setValue("Workload Revised:");
		htmlPanelGrid.getChildren().add(workloadRevisedLabel);

		HtmlOutputText workloadRevisedValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		workloadRevisedValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJTaskBean.JJTask_.workloadRevised}", String.class));
		htmlPanelGrid.getChildren().add(workloadRevisedValue);

		HtmlOutputText startDateRealLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		startDateRealLabel.setId("startDateRealLabel");
		startDateRealLabel.setValue("Start Date Real:");
		htmlPanelGrid.getChildren().add(startDateRealLabel);

		HtmlOutputText startDateRealValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		startDateRealValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJTaskBean.JJTask_.startDateReal}", Date.class));
		DateTimeConverter startDateRealValueConverter = (DateTimeConverter) application
				.createConverter(DateTimeConverter.CONVERTER_ID);
		startDateRealValueConverter.setPattern("dd/MM/yyyy");
		startDateRealValue.setConverter(startDateRealValueConverter);
		htmlPanelGrid.getChildren().add(startDateRealValue);

		HtmlOutputText endDateRealLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		endDateRealLabel.setId("endDateRealLabel");
		endDateRealLabel.setValue("End Date Real:");
		htmlPanelGrid.getChildren().add(endDateRealLabel);

		HtmlOutputText endDateRealValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		endDateRealValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJTaskBean.JJTask_.endDateReal}", Date.class));
		DateTimeConverter endDateRealValueConverter = (DateTimeConverter) application
				.createConverter(DateTimeConverter.CONVERTER_ID);
		endDateRealValueConverter.setPattern("dd/MM/yyyy");
		endDateRealValue.setConverter(endDateRealValueConverter);
		htmlPanelGrid.getChildren().add(endDateRealValue);

		HtmlOutputText workloadRealLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		workloadRealLabel.setId("workloadRealLabel");
		workloadRealLabel.setValue("Workload Real:");
		htmlPanelGrid.getChildren().add(workloadRealLabel);

		HtmlOutputText workloadRealValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		workloadRealValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJTaskBean.JJTask_.workloadReal}", String.class));
		htmlPanelGrid.getChildren().add(workloadRealValue);

		HtmlOutputText consumedLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		consumedLabel.setId("consumedLabel");
		consumedLabel.setValue("Consumed:");
		htmlPanelGrid.getChildren().add(consumedLabel);

		HtmlOutputText consumedValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		consumedValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJTaskBean.JJTask_.consumed}", String.class));
		htmlPanelGrid.getChildren().add(consumedValue);

		HtmlOutputText bugLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		bugLabel.setId("bugLabel");
		bugLabel.setValue("Bug:");
		htmlPanelGrid.getChildren().add(bugLabel);

		HtmlOutputText bugValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		bugValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJTaskBean.JJTask_.bug.name}", String.class));
		// bugValue.setConverter(new JJBugConverter());
		htmlPanelGrid.getChildren().add(bugValue);		

		if (getJJTask_() != null) {
			if (getJJTask_().getRequirement() != null) {


				
				System.out.println("1");
				taskTreeNode = new DefaultTreeNode("Requirement", null);				
				JJRequirement requirement=(JJRequirement) expressionFactory.createValueExpression(elContext,"#{jJTaskBean.JJTask_.requirement}", JJRequirement.class).getValue(elContext);
				DefaultTreeNode tree=new DefaultTreeNode(requirement.getName(),taskTreeNode);
				reqTreeNode(tree,requirement);	
				expressionFactory.createValueExpression(elContext, "#{jJTaskBean.taskTreeNode}", TreeNode.class).setValue(elContext, taskTreeNode);
				

			}

		}

		HtmlOutputText testcaseLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		testcaseLabel.setId("testcaseLabel");
		testcaseLabel.setValue("Testcase:");
		htmlPanelGrid.getChildren().add(testcaseLabel);

		HtmlOutputText testcaseValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		testcaseValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJTaskBean.JJTask_.testcase.name}", String.class));
		// testcaseValue.setConverter(new JJTestcaseConverter());
		htmlPanelGrid.getChildren().add(testcaseValue);

		HtmlOutputText sprintLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		sprintLabel.setId("sprintLabel");
		sprintLabel.setValue("Sprint:");
		htmlPanelGrid.getChildren().add(sprintLabel);

		HtmlOutputText sprintValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		sprintValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJTaskBean.JJTask_.sprint.name}", String.class));
		// sprintValue.setConverter(new JJSprintConverter());
		htmlPanelGrid.getChildren().add(sprintValue);

		HtmlOutputText assignedToLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		assignedToLabel.setId("assignedToLabel");
		assignedToLabel.setValue("Assigned To:");
		htmlPanelGrid.getChildren().add(assignedToLabel);

		HtmlOutputText assignedToValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		assignedToValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJTaskBean.JJTask_.assignedTo.name}", String.class));
		// assignedToValue.setConverter(new JJContactConverter());
		htmlPanelGrid.getChildren().add(assignedToValue);

		HtmlOutputText statusLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		statusLabel.setId("statusLabel");
		statusLabel.setValue("Status:");
		htmlPanelGrid.getChildren().add(statusLabel);

		HtmlOutputText statusValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		statusValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJTaskBean.JJTask_.status.name}", String.class));
		// statusValue.setConverter(new JJStatusConverter());
		htmlPanelGrid.getChildren().add(statusValue);

		HtmlOutputText completedLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		completedLabel.setId("completedLabel");
		completedLabel.setValue("Completed:");
		htmlPanelGrid.getChildren().add(completedLabel);

		HtmlOutputText completedValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		completedValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJTaskBean.JJTask_.completed}", String.class));
		htmlPanelGrid.getChildren().add(completedValue);

		HtmlOutputText parentLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		parentLabel.setId("parentLabel");
		parentLabel.setValue("Parent:");
		htmlPanelGrid.getChildren().add(parentLabel);

		HtmlOutputText parentValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		parentValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJTaskBean.JJTask_.parent.name}", String.class));
		parentValue.setConverter(new JJTaskConverter());
		htmlPanelGrid.getChildren().add(parentValue);
		

		return htmlPanelGrid;
	}

	private void reqTreeNode(TreeNode tree, JJRequirement req) {

		
		for (JJRequirement r : req.getRequirementLinkDown()) {

			DefaultTreeNode s = new DefaultTreeNode(r.getName(), tree);
			if (r.getRequirementLinkDown() != null)
				reqTreeNode(s, r);
		}

	}

}

package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.*;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpSession;
import javax.swing.text.StyledEditorKit.BoldAction;

import org.aspectj.weaver.World;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.extensions.component.timeline.Timeline;
import org.primefaces.extensions.event.timeline.*;
import org.primefaces.extensions.model.timeline.*;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.*;
import com.starit.janjoonweb.ui.mb.converter.JJTaskConverter;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;
import com.starit.janjoonweb.ui.mb.util.SprintUtil;

@RooSerializable
@RooJsfManagedBean(entity = JJTask.class, beanName = "jJTaskBean")
public class JJTaskBean {

	@Autowired
	JJChapterService jJChapterService;

	TaskData selectedTaskData;

	public void setJjChapterService(JJChapterService jJChapterService) {
		this.jJChapterService = jJChapterService;
	}

	@Autowired
	JJPermissionService jJPermissionService;

	public void setjJPermissionService(JJPermissionService jJPermissionService) {
		this.jJPermissionService = jJPermissionService;
	}

	@Autowired
	JJCategoryService jJCategoryService;

	public void setjJCategoryService(JJCategoryService jJCategoryService) {
		this.jJCategoryService = jJCategoryService;
	}

	private List<TaskData> tasksData;
	private JJTask task;

	private TimelineModel model;

	private Date start;
	private Date end;
	private long zoomMin;

	private Set<JJContact> contacts;

	private JJProject project;
	private JJProduct product;
	private JJVersion version;

	private JJSprint sprint;
	private List<JJSprint> sprints;

	private boolean disabledImportButton;
	private boolean disabledFilter;
	private boolean checkAll;

	private JJSprint importSprint;
	private String mode;

	private JJCategory importCategory;
	private List<JJCategory> importCategoryList;

	private JJStatus importStatus;
	private List<JJStatus> importStatusList;

	private String objet;
	private List<String> objets;

	private List<ImportFormat> importFormats;

	private boolean copyObjets;

	public List<TaskData> getTasksData() {

		// HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
		// .getExternalContext().getSession(false);
		// JJProjectBean jJProjectBean = (JJProjectBean) session
		// .getAttribute("jJProjectBean");
		//
		// if (jJProjectBean.getProject() == null) {
		//
		// if (tasksData == null) {
		// project = null;
		// loadData();
		// } else if (project != null) {
		//
		// project = null;
		// loadData();
		// }
		//
		// } else if (project != null) {
		//
		// if (!project.equals(jJProjectBean.getProject())) {
		// project = jJProjectBean.getProject();
		// loadData();
		// }
		// } else {
		// project = jJProjectBean.getProject();
		// loadData();
		// }

		return tasksData;
	}

	public void setTasksData(List<TaskData> tasksData) {
		this.tasksData = tasksData;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public JJTask getTask() {

		return task;
	}

	public void setTask(JJTask task) {

		taskTreeNode = null;
		selectedReq = null;
		selectedTree = null;

		this.task = task;
	}

	public TimelineModel getModel() {
		return model;
	}

	public void setModel(TimelineModel model) {
		this.model = model;
	}

	public Date getStart() {
		return start;
	}

	public Date getEnd() {
		return end;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public long getZoomMin() {
		return zoomMin;
	}

	public void setZoomMin(long zoomMin) {
		this.zoomMin = zoomMin;
	}

	public Set<JJContact> getContacts() {
		getProject();

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJProductBean jJProductBean = (JJProductBean) session
				.getAttribute("jJProductBean");
		JJProduct product = jJProductBean.getProduct();

		contacts = jJPermissionService.areAuthorized(project, product);

		return contacts;
	}

	public void setContacts(Set<JJContact> contacts) {
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

	public JJProduct getProduct() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJProductBean jJProductBean = (JJProductBean) session
				.getAttribute("jJProductBean");
		this.product = jJProductBean.getProduct();

		return product;
	}

	public void setProduct(JJProduct product) {
		this.product = product;
	}

	public JJVersion getVersion() {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJVersionBean jJVersionBean = (JJVersionBean) session
				.getAttribute("jJVersionBean");

		this.version = jJVersionBean.getVersion();

		return version;
	}

	public void setVersion(JJVersion version) {
		this.version = version;
	}

	public JJSprint getSprint() {
		return sprint;
	}

	public void setSprint(JJSprint sprint) {
		this.sprint = sprint;
	}

	public List<JJSprint> getSprints() {

		getProject();
		sprints = jJSprintService.getSprints(project, true);
		sprint = null;
		return sprints;
	}

	public void setSprints(List<JJSprint> sprints) {
		this.sprints = sprints;
	}

	public boolean getDisabledImportButton() {
		return disabledImportButton;
	}

	public void setDisabledImportButton(boolean disabledImportButton) {
		this.disabledImportButton = disabledImportButton;
	}

	public boolean getDisabledFilter() {
		return disabledFilter;
	}

	public void setDisabledFilter(boolean disabledFilter) {
		this.disabledFilter = disabledFilter;
	}

	public boolean getCheckAll() {
		return checkAll;
	}

	public void setCheckAll(boolean checkAll) {
		this.checkAll = checkAll;
	}

	public JJCategory getImportCategory() {
		return importCategory;
	}

	public void setImportCategory(JJCategory importCategory) {
		this.importCategory = importCategory;
	}

	public List<JJCategory> getImportCategoryList() {

		if (objet != null) {

			importCategoryList = jJCategoryService.getCategories(null, false,
					true, true);

		} else {
			importCategoryList = null;
		}
		return importCategoryList;
	}

	public void setImportCategoryList(List<JJCategory> importCategoryList) {
		this.importCategoryList = importCategoryList;
	}

	public JJStatus getImportStatus() {
		return importStatus;
	}

	public void setImportStatus(JJStatus importStatus) {
		this.importStatus = importStatus;
	}

	public List<JJStatus> getImportStatusList() {
		if (objet != null) {
			List<String> names = new ArrayList<String>();
			if (objet.equalsIgnoreCase("JJBug")) {
				names.add("CLOSED");
			} else if (objet.equalsIgnoreCase("JJRequirement")) {
				names.add("DELETED");
				names.add("CANCELED");
			} else if (objet.equalsIgnoreCase("JJTestcase")) {
				return null;
			}
			importStatusList = jJStatusService.getStatus(objet, true, names,
					true);
			return importStatusList;
		} else {
			return null;
		}
	}

	public void setImportStatusList(List<JJStatus> importStatusList) {
		this.importStatusList = importStatusList;
	}

	public String getObjet() {
		return objet;
	}

	public void setObjet(String objet) {
		this.objet = objet;
	}

	public List<String> getObjets() {

		objets = new ArrayList<String>();
		objets.add("JJBug");
		objets.add("JJRequirement");
		objets.add("JJTestcase");

		return objets;
	}

	public void setObjets(List<String> objets) {
		this.objets = objets;
	}

	public List<ImportFormat> getImportFormats() {
		return importFormats;
	}

	public void setImportFormats(List<ImportFormat> importFormats) {
		this.importFormats = importFormats;
	}

	public boolean getCopyObjets() {
		return copyObjets;
	}

	public void setCopyObjets(boolean copyObjets) {
		this.copyObjets = copyObjets;
	}

	public void loadingData() {

		if (tasksData == null) {
			getProject();
			loadData();
		} else {
			if (tasksData.size() != 0) {
				if (!tasksData.get(0).getChapter().getProject().equals(project)) {
					loadData();
				}
			} else {
				loadData();
			}
		}

	}

	public void loadData() {

		// Set initial start / end dates for the axis of the timeline
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

		Date now = new Date();

		// Before 4 hours for now
		cal.setTimeInMillis(now.getTime() - 24 * 60 * 60 * 1000);
		start = cal.getTime();

		// After 8 hours for now
		cal.setTimeInMillis(now.getTime() + 24 * 60 * 60 * 1000);
		end = cal.getTime();

		// one day in milliseconds for zoomMin
		zoomMin = 1000L * 60 * 60 * 24;

		// Create timeline model
		model = new TimelineModel();

		getProject();
		tasksData = new ArrayList<TaskData>();

		// 65 = ASCII A
		int k = 65;

		List<JJChapter> chapters = jJChapterService.getChapters(project, true);

		for (JJChapter chapter : chapters) {
			Map<Date, String> min = new TreeMap<Date, String>();
			Map<Date, String> max = new TreeMap<Date, String>();

			List<JJTask> tasks = new ArrayList<JJTask>();
			tasks.addAll(jJTaskService.getTasks(sprint, null, null, null,
					chapter, null, null, null, true, true, false,
					"JJRequirement"));

			tasks.addAll(jJTaskService.getTasks(sprint, null, null, null,
					chapter, null, null, null, true, true, false, "JJTestcase"));

			tasks.addAll(jJTaskService.getTasks(sprint, null, null, null,
					chapter, null, null, null, true, true, false, "JJBug"));

			TreeMap<String, JJTask> Tasks = new TreeMap<String, JJTask>();

			for (JJTask tt : tasks) {

				// Tasks.put(task.getName(), task);
				Tasks.put(tt.getId() + "", tt);
			}

			// Iterate over HashMap
			for (String key : Tasks.keySet()) {

				if (k == 92)
					k++;

				JJTask tt = Tasks.get(key);

				char c = (char) k;

				System.err.println(k + ":" + c + ":" + tt.getName());
				String group = "<span style=display:none>" + c + "</span>";
				// + task.getName();

				TaskData taskData = null;
				boolean add = false;

				if (tt.getStartDateReal() != null) {

					Date endDate;
					if (tt.getEndDateReal() == null)
						endDate = tt.getEndDatePlanned();
					else
						endDate = tt.getEndDateReal();

					TimelineEvent event = new TimelineEvent(tt,
							tt.getStartDateReal(), endDate, true, group, "real");

					model.add(event);

					taskData = new TaskData(tt, chapter, tt.getStartDateReal(),
							endDate, tt.getWorkloadReal(), false);
					add = true;
				}
				if (tt.getStartDateRevised() != null) {

					Date endDate;
					if (tt.getEndDateRevised() == null)
						endDate = tt.getEndDatePlanned();
					else
						endDate = tt.getEndDateRevised();

					TimelineEvent event = new TimelineEvent(tt,
							tt.getStartDateRevised(), endDate, true, group,
							"revised");
					model.add(event);

					if (!add)
						taskData = new TaskData(tt, chapter,
								tt.getStartDateRevised(), endDate,
								tt.getWorkloadRevised(), true);
				} else {

					TimelineEvent event = new TimelineEvent(tt,
							tt.getStartDatePlanned(), tt.getEndDatePlanned(),
							true, group, "planned");
					model.add(event);

					if (!add)
						taskData = new TaskData(tt, chapter,
								tt.getStartDatePlanned(),
								tt.getEndDatePlanned(),
								tt.getWorkloadPlanned(), false);
				}

				tasksData.add(taskData);

				if (tt.getStartDatePlanned() != null) {
					min.put(tt.getStartDatePlanned(), tt.getStartDatePlanned()
							.toString());
				}

				if (tt.getStartDateRevised() != null) {
					min.put(tt.getStartDateRevised(), tt.getStartDateRevised()
							.toString());
				}

				if (tt.getStartDateReal() != null) {
					min.put(tt.getStartDateReal(), tt.getStartDateReal()
							.toString());
				}

				if (tt.getEndDatePlanned() != null) {
					max.put(tt.getEndDatePlanned(), tt.getEndDatePlanned()
							.toString());
				}

				if (tt.getEndDateRevised() != null) {
					max.put(tt.getEndDateRevised(), tt.getEndDateRevised()
							.toString());
				}

				if (tt.getEndDateReal() != null) {
					max.put(tt.getEndDateReal(), tt.getEndDateReal().toString());
				}

				k++;
			}

			if (!min.isEmpty() && !max.isEmpty()) {

				if (k == 92)
					k++;

				Date end = null;
				Set<Date> dates = min.keySet();

				Date start = dates.iterator().next();

				dates = max.keySet();
				for (Date date : dates) {
					end = date;
				}
				JJTask task = new JJTask();
				// task.setName(chapter.getName());
				task.setName(" ");

				char c = (char) k;

				String group = "<span style=display:none>" + c + "</span>";
				// + task.getName();
				TimelineEvent event = new TimelineEvent(chapter, start, end,
						false, group, "chapter");
				k++;

				model.add(event);
			}

		}

		task = null;
	}

	public void onCellEdit(CellEditEvent event) {
		UIColumn column = event.getColumn();

		String columnKey = column.getColumnKey();

		DataTable dataTable = (DataTable) event.getSource();
		TaskData taskData = (TaskData) dataTable.getRowData();
		JJTask task = taskData.getTask();

		Object newValue = event.getNewValue();

		String message = "";
		FacesMessage facesMessage = null;

		boolean valid = true;

		if (columnKey.contains("sdpr")) {
			if (newValue != null) {
				Date date = (Date) newValue;
				if (taskData.getEndDate() != null) {

					long str = taskData.getEndDate().getTime() - date.getTime();
					if (str >= 0) {
						task.setStartDateRevised(date);
						task.setEndDateRevised(taskData.getEndDate());
					} else {
						valid = false;
						message += "\nStart Date Revised doit être < End Date Revised";
					}
				} else {
					if (taskData.getWorkload() != null) {

						task.setStartDateRevised(date);
						Date endDateRevised = new Date(date.getTime()
								+ taskData.getWorkload() * 3600000);
						task.setEndDateRevised(endDateRevised);

					}
				}
			}

		} else if (columnKey.contains("edpr")) {
			if (newValue != null) {
				Date date = (Date) newValue;
				if (taskData.getStartDate() != null) {
					long str = date.getTime()
							- taskData.getStartDate().getTime();
					if (str >= 0) {

						task.setStartDateRevised(taskData.getStartDate());
						task.setEndDateRevised(date);

					} else {
						valid = false;
						message += "\nEnd Date Revised doit être > Start Date Revised";
					}
				} else {
					if (taskData.getWorkload() != null) {
						Date startDateRevised = new Date(date.getTime()
								- taskData.getWorkload() * 3600000);

						task.setStartDateRevised(startDateRevised);
						task.setEndDateRevised(date);
					}
				}
			}

		} else if (columnKey.contains("wpr")) {

			if (newValue != null) {
				int workloadRevised = (int) newValue;

				if ((taskData.getStartDate() != null && taskData.getEndDate() != null)
						|| (taskData.getStartDate() != null)) {
					Date endDateRevised = new Date(taskData.getStartDate()
							.getTime() + workloadRevised * 3600000);

					task.setStartDateRevised(taskData.getStartDate());
					task.setEndDateRevised(endDateRevised);
					task.setWorkloadRevised(workloadRevised);

				} else if (taskData.getEndDate() != null) {
					Date startDateRevised = new Date(taskData.getEndDate()
							.getTime() - workloadRevised * 3600000);
					task.setStartDateRevised(startDateRevised);
					task.setEndDateRevised(taskData.getEndDate());
					task.setWorkloadRevised(workloadRevised);
				}

			}

		} else if (columnKey.contains("sdr")) {
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

		} else if (columnKey.contains("edr")) {
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
		} else if (columnKey.contains("wr")) {
			if (newValue != null) {
				int workloadReal = (int) newValue;

				if ((task.getStartDateReal() != null && task.getEndDateReal() != null)
						|| (task.getStartDateReal() != null)) {
					Date endDateReal = new Date(task.getStartDateReal()
							.getTime() + task.getWorkloadReal() * 3600000);
					task.setEndDateReal(endDateReal);
					task.setWorkloadReal(workloadReal);

				} else if (task.getEndDateReal() != null) {
					Date startDateReal = new Date(task.getEndDateReal()
							.getTime() - task.getWorkloadReal() * 3600000);
					task.setStartDateReal(startDateReal);
					task.setWorkloadReal(workloadReal);
				}

			}
		} else if (columnKey.contains("bt")) {

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
			task = jJTaskService.findJJTask(task.getId());

			// if (columnKey.contains("ts")) {
			// System.err.println("UPADTE CONACTS");
			// jJSprintService.findJJSprint(task.getSprint().getId())
			// .getContacts();
			// }

			// int i = containTaskData(task.getId());
			// if (i != -1) {
			// TaskData tskst = new TaskData(task, tasksData.get(i)
			// .getChapter(), task.getStartDatePlanned(),
			// task.getEndDatePlanned(), task.getWorkloadPlanned(),
			// false);
			//
			// tasksData.set(i, tskst);
			// replaceTimeLineEvent(task);
			// }
			updateView(task, false);

			if (task.getSprint() != null) {

				HttpSession session = (HttpSession) FacesContext
						.getCurrentInstance().getExternalContext()
						.getSession(false);
				JJSprintBean jJSprintBean = (JJSprintBean) session
						.getAttribute("jJSprintBean");
				SprintUtil s = SprintUtil.getSprintUtil(task.getSprint()
						.getId(), jJSprintBean.getSprintList());
				if (s != null) {
					s = new SprintUtil(jJSprintService.findJJSprint(task
							.getSprint().getId()),
							jJTaskService.getSprintTasks(jJSprintService
									.findJJSprint(task.getSprint().getId())));

					// sprintUtil.setRenderTaskForm(false);
					jJSprintBean.getSprintList().set(
							jJSprintBean.contains(s.getSprint().getId()), s);
				}
			}
			reset();

			message = "Success Update";
			facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO,
					message, "JJTask");

		} else {
			facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					message, "JJTask");
		}

		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}

	public int containTaskData(long id) {
		int i = 0;
		int j = -1;

		if (tasksData != null) {
			while (i < tasksData.size()) {
				if (tasksData.get(i).getTask().getId().equals(id)) {
					j = i;
					i = tasksData.size();
				} else
					i++;
			}
		}

		return j;
	}

	public void duplicateTask() {

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
	}

	public void loadImportFormat(ActionEvent e) {

		this.mode = (String) e.getComponent().getAttributes().get("mode");
		disabledImportButton = true;
		disabledFilter = true;

		checkAll = false;
		copyObjets = false;

		if (mode.equalsIgnoreCase("planning")) {

			importSprint = null;
		} else if (mode.equalsIgnoreCase("scrum")) {

			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);
			JJSprintBean jJSprintBean = (JJSprintBean) session
					.getAttribute("jJSprintBean");
			jJSprintBean.attrListener(e);

			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("taskImportDialogWidget.show()");
		}

		getProject();
		getProduct();
		getVersion();

		importFormats = new ArrayList<ImportFormat>();

	}

	public void checkAll() {
		copyObjets = false;
		disabledImportButton = true;

		fillTableImport();
	}

	public void fillTableImport() {

		importFormats = new ArrayList<ImportFormat>();
		List<JJTask> tasks;
		copyObjets = false;
		disabledImportButton = true;

		if (objet != null) {

			if (objet.equalsIgnoreCase("JJBug")) {

				for (JJBug bug : jJBugService.getImportBugs(project, version,
						importCategory, importStatus, true)) {

					if (!checkAll) {

						tasks = jJTaskService.getImportTasks(bug, null, null,
								true);
						if (tasks.isEmpty()) {
							importFormats.add(new ImportFormat(bug.getName(),
									bug, copyObjets));
						}

					} else {
						importFormats.add(new ImportFormat(bug.getName(), bug,
								copyObjets));
					}
				}

			} else if (objet.equalsIgnoreCase("JJRequirement")) {

				for (JJRequirement requirement : jJRequirementService
						.getRequirements(importCategory, project, product,
								version, importStatus, null, false, true, false)) {

					if (!checkAll) {

						tasks = jJTaskService.getImportTasks(null, requirement,
								null, true);

						if (tasks.isEmpty()) {
							importFormats.add(new ImportFormat(requirement
									.getName(), requirement, copyObjets));
						}
					} else {

						importFormats.add(new ImportFormat(requirement
								.getName(), requirement, copyObjets));
					}
				}

			} else if (objet.equalsIgnoreCase("JJTestcase")) {

				for (JJTestcase testcase : jJTestcaseService
						.getImportTestcases(project, true)) {

					if (!checkAll) {
						tasks = jJTaskService.getImportTasks(null, null,
								testcase, true);
						if (tasks.isEmpty()) {
							importFormats.add(new ImportFormat(testcase
									.getName(), testcase, copyObjets));
						}
					} else {

						importFormats.add(new ImportFormat(testcase.getName(),
								testcase, copyObjets));
					}
				}
			}

		} else {
			importFormats = new ArrayList<ImportFormat>();
		}
	}

	public void importTask() {

		for (ImportFormat format : importFormats) {

			if (format.getCopyObjet()) {

				String name = null;
				if (mode == null)
					mode = "planning";

				JJTask task = new JJTask();
				task.setEnabled(true);
				task.setCreationDate(new Date());
				if (mode.equalsIgnoreCase("planning"))
					task.setSprint(this.sprint);

				if (format.getStartDate() == null) {
					task.setStartDatePlanned(new Date());
				} else {
					task.setStartDatePlanned(format.getStartDate());
				}

				if (format.getWorkload() != null) {
					task.setWorkloadPlanned(format.getWorkload());

					task.setEndDatePlanned(new Date(task.getStartDatePlanned()
							.getTime()
							+ task.getWorkloadPlanned()
							* 60
							* 60
							* 1000));
				}

				if (format.getObject() instanceof JJRequirement) {

					JJRequirement requirement = (JJRequirement) format
							.getObject();

					name = requirement.getName() + "("
							+ task.getCreationDate().getTime() + ")";

					requirement = jJRequirementService
							.findJJRequirement(requirement.getId());

					requirement.getTasks().add(task);

					task.setRequirement(requirement);

				} else if (format.getObject() instanceof JJBug) {

					JJBug bug = (JJBug) format.getObject();

					name = bug.getName() + "("
							+ task.getCreationDate().getTime() + ")";

					bug = jJBugService.findJJBug(bug.getId());
					bug.getTasks().add(task);

					task.setBug(bug);

				} else if (format.getObject() instanceof JJTestcase) {

					JJTestcase testcase = (JJTestcase) format.getObject();

					name = testcase.getName() + "("
							+ task.getCreationDate().getTime() + ")";

					testcase = jJTestcaseService.findJJTestcase(testcase
							.getId());

					testcase.getTasks().add(task);

					task.setTestcase(testcase);
				}

				task.setName(name);
				task.setDescription("This is task " + task.getName());

				if (mode.equalsIgnoreCase("scrum")) {

					HttpSession session = (HttpSession) FacesContext
							.getCurrentInstance().getExternalContext()
							.getSession(false);
					JJSprintBean jJSprintBean = (JJSprintBean) session
							.getAttribute("jJSprintBean");
					JJContact contact = (JJContact) session
							.getAttribute("JJContact");
					JJStatus status = jJStatusService.getOneStatus("TODO",
							"JJTask", true);
					if (status != null)
						task.setStatus(status);
					task.setEnabled(true);
					task.setSprint(jJSprintBean.getSprintUtil().getSprint());
					task.setEndDatePlanned(jJSprintBean.getSprintUtil()
							.getSprint().getEndDate());
					task.setCreatedBy(contact);
					task.setCreationDate(new Date());

				}

				jJTaskService.saveJJTask(task);

			}

		}

		if (mode.equalsIgnoreCase("planning")) {

			project = null;
			tasksData = null;
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("taskImportDialogWidget.hide()");

		} else if (mode.equalsIgnoreCase("scrum")) {

			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);
			JJSprintBean jJSprintBean = (JJSprintBean) session
					.getAttribute("jJSprintBean");

			if (!jJSprintBean.getSprintUtil().isRender()) {

				JJSprint sprint = jJSprintService.findJJSprint(jJSprintBean
						.getSprintUtil().getSprint().getId());
				SprintUtil sprintUtil = new SprintUtil(sprint,
						new ArrayList<JJTask>(sprint.getTasks()));
				jJSprintBean.setSprintUtil(sprintUtil);
				jJSprintBean.getSprintList().set(
						jJSprintBean.contains(sprint.getId()), sprintUtil);

			}

			project = null;
			tasksData = null;
			closeDialogImport();
			String message = "message_successfully_created";

			FacesMessage facesMessage = MessageFactory.getMessage(message,
					"JJTask");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
			RequestContext context = RequestContext.getCurrentInstance();

			context.execute("projectTabView.select(" + 1 + ")");
			context.execute("SprintTab.select("
					+ jJSprintBean.contains(jJSprintBean.getSprintUtil()
							.getSprint().getId()) + ")");

			context.execute("taskImportDialogWidget.hide()");

		}

	}

	public void closeDialogImport() {

		importSprint = null;
		importCategory = null;
		importStatus = null;

		importCategoryList = null;
		importStatusList = null;

		objet = null;
		objets = null;

		importFormats = null;
		mode = null;

	}

	public void handleSelectObjet() {

		importCategory = null;
		importStatus = null;

		getImportCategory();
		getImportStatus();

		disabledImportButton = true;
		copyObjets = false;

		if (objet != null) {
			if (objet.equalsIgnoreCase("JJTestcase")) {
				disabledFilter = true;
			} else {
				disabledFilter = false;
			}
			fillTableImport();
		} else {
			disabledFilter = true;
			importFormats = new ArrayList<ImportFormat>();

		}

	}

	public void handleSelectStatus() {
		getImportStatus();
		disabledImportButton = true;
		copyObjets = false;

		if (objet != null) {
			fillTableImport();
		} else {
			importFormats = new ArrayList<ImportFormat>();

		}
	}

	public void handleSelectCategory() {
		getImportCategory();

		disabledImportButton = true;
		copyObjets = false;

		if (objet != null) {
			fillTableImport();
		} else {
			importFormats = new ArrayList<ImportFormat>();

		}
	}
	
	public void sprintSelectionChanged(final AjaxBehaviorEvent event)
	{
		tasksData=null;
	}

	public void copyObjets() {

		for (ImportFormat importFormat : importFormats) {
			importFormat.setCopyObjet(copyObjets);
		}

		disabledImportButton = !copyObjets;

	}

	public void copyObjet() {

		boolean copyAll = true;
		for (ImportFormat importFormat : importFormats) {
			if (!importFormat.getCopyObjet()) {
				copyAll = false;
				break;
			}

		}

		copyObjets = copyAll;

		for (ImportFormat importFormat : importFormats) {
			if (importFormat.getCopyObjet()) {
				disabledImportButton = false;
				break;
			} else {
				disabledImportButton = true;
			}

		}
	}

	public class ImportFormat {

		private String name;
		private Object object;
		private boolean copyObjet;
		private Date startDate;
		private Integer workload;

		public ImportFormat() {
			super();
		}

		public ImportFormat(String name, Object object, boolean copyObjet) {
			super();
			this.name = name;
			this.object = object;
			this.copyObjet = copyObjet;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Object getObject() {
			return object;
		}

		public void setObject(Object object) {
			this.object = object;
		}

		public boolean getCopyObjet() {
			return copyObjet;
		}

		public void setCopyObjet(boolean copyObjet) {
			this.copyObjet = copyObjet;
		}

		public Date getStartDate() {
			return startDate;
		}

		public void setStartDate(Date startDate) {
			this.startDate = startDate;
		}

		public Integer getWorkload() {
			return workload;
		}

		public void setWorkload(Integer workload) {
			this.workload = workload;
		}

	}

	public class TaskData {
		private JJTask task;
		private JJChapter chapter;
		private Date startDate;
		private Date endDate;
		private Integer workload;
		private boolean revisedDate;

		private List<String> tasks;
		private List<String> selectedTasks;

		private List<JJTask> storeTasks;

		public TaskData(JJTask task, JJChapter chapter, Date startDate,
				Date endDate, Integer workload, boolean revisedDate) {
			super();
			this.task = task;

			this.chapter = chapter;
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
			list.addAll(jJTaskService.getTasks(sprint, null, null, null,
					chapter, null, null, null, true, false, false,
					"JJRequirement"));

			list.addAll(jJTaskService
					.getTasks(sprint, null, null, null, chapter, null, null,
							null, true, false, false, "JJTestcase"));

			list.addAll(jJTaskService.getTasks(sprint, null, null, null,
					chapter, null, null, null, true, false, false, "JJBug"));

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

		public JJChapter getChapter() {
			return chapter;
		}

		public void setChapter(JJChapter chapter) {
			this.chapter = chapter;
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

	public void deleteTaskData() {

		RequestContext context = RequestContext.getCurrentInstance();
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJSprintBean jJSprintBean = (JJSprintBean) session
				.getAttribute("jJSprintBean");
		JJTask tJjTask = null;

		if (!mode.equalsIgnoreCase("scrum")) {

			tJjTask = jJTaskService.findJJTask(selectedTaskData.getTask()
					.getId());
			tJjTask.setEnabled(false);
			jJTaskService.updateJJTask(tJjTask);
			updateView(tJjTask, true);

			if (tJjTask.getSprint() != null) {
				SprintUtil s = SprintUtil.getSprintUtil(tJjTask.getSprint()
						.getId(), jJSprintBean.getSprintList());
				if (s != null) {
					s = new SprintUtil(jJSprintService.findJJSprint(tJjTask
							.getSprint().getId()),
							jJTaskService.getSprintTasks(jJSprintService
									.findJJSprint(tJjTask.getSprint().getId())));

					// sprintUtil.setRenderTaskForm(false);
					jJSprintBean.getSprintList().set(
							jJSprintBean.contains(s.getSprint().getId()), s);
				}
			}
			FacesMessage facesMessage = MessageFactory.getMessage(
					"message_successfully_deleted", "JJTask");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
			context.execute("deleteDialogWidget.hide()");
		} else {

			tJjTask = jJSprintBean.getTask();
			jJSprintBean.deleteTask();
			updateView(tJjTask, true);

		}

	}

	public TaskData getSelectedTaskData() {
		return selectedTaskData;
	}

	public void setSelectedTaskData(TaskData selectedTaskData) {
		this.selectedTaskData = selectedTaskData;
	}

	private HtmlPanelGrid viewPanel;

	public HtmlPanelGrid getViewPanel() {
		return populateViewPanelGrid();
	}

	public void setViewPanel(HtmlPanelGrid viewPanel) {
		this.viewPanel = viewPanel;
	}

	private TreeNode selectedTree;
	private JJRequirement selectedReq;

	public JJRequirement getSelectedReq() {
		return selectedReq;
	}

	public void setSelectedReq(JJRequirement selectedReq) {
		if (selectedReq == null)
			selectedReq = (JJRequirement) selectedTree.getData();
		this.selectedReq = selectedReq;
	}

	public TreeNode getSelectedTree() {

		if (selectedTree == null)
			selectedTree = taskTreeNode;

		return selectedTree;
	}

	public void setSelectedTree(TreeNode selectedTree) {
		this.selectedTree = selectedTree;
	}

	public void onSelectTreeNode(NodeSelectEvent event) {

		selectedTree = event.getTreeNode();
		selectedReq = (JJRequirement) selectedTree.getData();
	}

	private TreeNode taskTreeNode;

	public TreeNode getTaskTreeNode() {

		if (taskTreeNode == null) {

			if (task != null) {
				if (task.getRequirement() != null) {

					System.out.println("TreeNode "
							+ task.getRequirement().getName());

					taskTreeNode = new DefaultTreeNode("Requirement :", null);
					DefaultTreeNode tree = new DefaultTreeNode(
							task.getRequirement(), taskTreeNode);

					System.out.println("TreeNode "
							+ task.getRequirement().getName());

					if (task.getRequirement().getRequirementLinkDown() != null) {

						System.out.println("TreeNode Before "
								+ task.getRequirement().getName());
						reqTreeNode(tree, task.getRequirement());

						System.out.println("TreeNode after "
								+ task.getRequirement().getName());
					}

					selectedTree = taskTreeNode;
					selectedReq = task.getRequirement();
					System.out.println("TreeNode Last "
							+ task.getRequirement().getName());
				}

			}
		}
		return taskTreeNode;
	}

	public void setTaskTreeNode(TreeNode taskTreeNode) {
		this.taskTreeNode = taskTreeNode;
	}

	public void gettingTaskValue(ActionEvent ev) {

		task = (JJTask) ev.getComponent().getAttributes().get("taskValue");
		taskTreeNode = null;
		selectedReq = null;
		selectedTree = null;

	}

	private void reqTreeNode(TreeNode tree, JJRequirement req) {

		req = jJRequirementService.findJJRequirement(req.getId());

		for (JJRequirement r : req.getRequirementLinkDown()) {

			DefaultTreeNode s = new DefaultTreeNode(r, tree);

			if (r.getRequirementLinkDown() != null) {

				reqTreeNode(s, r);
			}

		}

	}

	public void initiateReqTreeNode() {

		getTaskTreeNode();

		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("viewTaskDialogWidget.show()");

	}

	// Timeline operation
	public int findInEventTimeLine(JJTask t, boolean real) {

		int j = -1;
		int i = 0;
		while (i < model.getEvents().size()) {

			System.out.println(model.getEvent(i).getStyleClass()
					+ ":"
					+ !(model.getEvent(i).getStyleClass()
							.equalsIgnoreCase("real") ^ real) + ":" + real);

			if (model.getEvent(i).getData() instanceof JJTask
					&& !(model.getEvent(i).getStyleClass()
							.equalsIgnoreCase("real") ^ real)) {
				JJTask tt = (JJTask) model.getEvent(i).getData();
				if (t.equals(tt)) {

					j = i;
					i = model.getEvents().size();

				}
			}
			i++;

		}

		return j;
	}

	public void updateChapterTimeLineEvent(JJChapter chapter) {

		List<JJTask> listTasks = new ArrayList<JJTask>();

		int i = 0;
		while (i < model.getEvents().size()) {

			if (model.getEvent(i).getData() instanceof JJTask
					&& !model.getEvent(i).getStyleClass()
							.equalsIgnoreCase("real")) {
				if (((JJTask) model.getEvent(i).getData()).getRequirement()
						.getChapter().equals(chapter))
					listTasks.add((JJTask) model.getEvent(i).getData());

			}
			i++;
		}

		Map<Date, String> min = new TreeMap<Date, String>();
		Map<Date, String> max = new TreeMap<Date, String>();

		for (JJTask t : listTasks) {

			if (t.getStartDatePlanned() != null) {
				min.put(t.getStartDatePlanned(), t.getStartDatePlanned()
						.toString());
			}

			if (t.getStartDateRevised() != null) {
				min.put(t.getStartDateRevised(), t.getStartDateRevised()
						.toString());
			}

			if (t.getStartDateReal() != null) {
				min.put(t.getStartDateReal(), t.getStartDateReal().toString());
			}

			if (t.getEndDatePlanned() != null) {
				max.put(t.getEndDatePlanned(), t.getEndDatePlanned().toString());
			}

			if (t.getEndDateRevised() != null) {
				max.put(t.getEndDateRevised(), t.getEndDateRevised().toString());
			}

			if (t.getEndDateReal() != null) {
				max.put(t.getEndDateReal(), t.getEndDateReal().toString());
			}
		}

		// findTimelineChapter
		i = 0;
		int j = -1;
		String group = "";
		while (i < model.getEvents().size()) {
			if (model.getEvent(i).getStyleClass().equalsIgnoreCase("chapter")) {

				if (((JJChapter) model.getEvent(i).getData()).equals(chapter)) {
					j = i;
					i = model.getEvents().size();
					group = model.getEvent(j).getGroup();

				}
			}
			i++;
		}

		if (j != -1) {
			if (!min.isEmpty() && !max.isEmpty()) {

				Date end = null;
				Set<Date> dates = min.keySet();

				Date start = dates.iterator().next();

				dates = max.keySet();
				for (Date date : dates) {
					end = date;
				}

				model.delete(model.getEvent(j));
				TimelineEvent event = new TimelineEvent(chapter, start, end,
						false, group, "chapter");

				model.add(event);

			}

		}
		this.start = null;
		this.end = null;

	}

	public void onDeleteTimelineEvent(TimelineModificationEvent e) {
		// get clone of the TimelineEvent to be deleted

		selectedTaskData = tasksData.get(containTaskData(((JJTask) e
				.getTimelineEvent().getData()).getId()));
		mode = "planning";
	}

	public void onCreateTimelineEvent(TimelineAddEvent event) {

		this.mode = "planning";
		disabledImportButton = true;
		disabledFilter = true;
		checkAll = false;
		copyObjets = false;
		importSprint = null;
		getProject();
		getProduct();
		getVersion();

		importFormats = new ArrayList<ImportFormat>();
	}

	public void onEditTimelineEvent(TimelineModificationEvent e) {
		this.task = (JJTask) e.getTimelineEvent().getData();
		taskTreeNode = null;
		selectedReq = null;
		selectedTree = null;
		this.mode = "planning";
		initiateReqTreeNode();
	}

	public void onChangeTimelineEvent(TimelineModificationEvent ev) {

		JJTask tt = (JJTask) ev.getTimelineEvent().getData();
		tt = jJTaskService.findJJTask(tt.getId());
		String group = ev.getTimelineEvent().getStyleClass().toUpperCase();

		if (group.equalsIgnoreCase("Real")) {

			tt.setStartDateReal(ev.getTimelineEvent().getStartDate());
			tt.setEndDateReal(ev.getTimelineEvent().getEndDate());
			tt.setUpdatedDate(new Date());
			jJTaskService.updateJJTask(tt);
			tt = jJTaskService.findJJTask(tt.getId());
			updateView(tt, false);

		} else {

			tt.setStartDateRevised(ev.getTimelineEvent().getStartDate());
			tt.setEndDateRevised(ev.getTimelineEvent().getEndDate());
			tt.setUpdatedDate(new Date());
			jJTaskService.updateJJTask(tt);
			tt = jJTaskService.findJJTask(tt.getId());
			updateView(tt, false);

		}

		if (tt.getSprint() != null) {

			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);
			JJSprintBean jJSprintBean = (JJSprintBean) session
					.getAttribute("jJSprintBean");
			SprintUtil s = SprintUtil.getSprintUtil(tt.getSprint().getId(),
					jJSprintBean.getSprintList());
			if (s != null) {
				s = new SprintUtil(jJSprintService.findJJSprint(tt.getSprint()
						.getId()), jJTaskService.getSprintTasks(jJSprintService
						.findJJSprint(tt.getSprint().getId())));
				jJSprintBean.getSprintList().set(
						jJSprintBean.contains(s.getSprint().getId()), s);
			}
		}

		reset();
		String message = "";
		FacesMessage facesMessage = null;

		message = "Success Update " + group + " Date";
		facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, message,
				"JJTask");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}

	public void onSprintUpdate(JJSprint sprint) {

		for (JJTask tt : jJTaskService.getSprintTasks(jJSprintService
				.findJJSprint(sprint.getId()))) {
			
			if (containTaskData(tt.getId()) != -1)
				updateView(tt, false);
		}

	}

	public void replaceTaskData(JJTask tt, boolean delete) {

		int i = containTaskData(tt.getId());

		if (i != -1) {
			if (!delete) {
				Date startDate = null, endDate = null;
				int workLoad = 0;
				if (tt.getStartDateReal() != null) {
					startDate = tt.getStartDateReal();
					if (tt.getWorkloadReal() == null)
						workLoad = tt.getWorkloadPlanned();
					else
						workLoad = tt.getWorkloadReal();
					if (tt.getEndDateReal() != null)
						endDate = tt.getEndDateReal();
					else
						endDate = tt.getEndDatePlanned();
				} else {
					if (tt.getStartDateRevised() != null) {
						startDate = tt.getStartDateRevised();
						if (tt.getWorkloadRevised() == null)
							workLoad = tt.getWorkloadPlanned();
						else
							workLoad = tt.getWorkloadRevised();
						if (tt.getEndDateRevised() != null)
							endDate = tt.getEndDateRevised();
						else
							endDate = tt.getEndDatePlanned();
					} else {
						startDate = tt.getStartDatePlanned();
						endDate = tt.getEndDatePlanned();
						workLoad = tt.getWorkloadPlanned();
					}
				}

				TaskData tskst = new TaskData(tt,
						tasksData.get(i).getChapter(), startDate, endDate,
						workLoad, tt.getStartDateRevised() != null);

				tasksData.set(i, tskst);
			} else
				tasksData.remove(i);

		}

	}

	public void replaceRealTimelineEvent(JJTask tt, boolean delete) {

		int i = findInEventTimeLine(tt, true);
		if (i != -1) {

			String group = model.getEvent(i).getGroup();
			model.delete(model.getEvent(i));
			if (!delete) {

				Date startDate = null, endDate = null;

				if (tt.getStartDateReal() != null) {

					startDate = tt.getStartDateReal();

					if (tt.getEndDateReal() != null)
						endDate = tt.getEndDateReal();
					else
						endDate = tt.getEndDatePlanned();
				}

				if (startDate != null)
					model.add(new TimelineEvent(tt, startDate, endDate, true,
							group, "real"));

			}
		} else if (!delete && tt.getStartDateReal() != null) {

			Date startDate = null, endDate = null;

			startDate = tt.getStartDateReal();

			if (tt.getEndDateReal() != null)
				endDate = tt.getEndDateReal();
			else
				endDate = tt.getEndDatePlanned();
			int j = findInEventTimeLine(tt, false);
			if (j != -1) {
				String group = model.getEvent(j).getGroup();
				model.add(new TimelineEvent(tt, startDate, endDate, true,
						group, "real"));
			}

		}

	}

	public void updateView(JJTask tt, boolean delete) {

		replaceTaskData(tt, delete);
		replaceRealTimelineEvent(tt, delete);

		int j = findInEventTimeLine(tt, false);
		String styleClass = "";
		Date startDate = null, endDate = null;

		if (!delete && j != -1) {

			if (tt.getStartDateRevised() != null) {

				styleClass = "revised";
				startDate = tt.getStartDateRevised();
				if (tt.getEndDateRevised() == null)
					endDate = tt.getEndDatePlanned();
				else
					endDate = tt.getEndDateRevised();

			} else {

				styleClass = "planned";
				endDate = tt.getEndDatePlanned();
				startDate = tt.getStartDatePlanned();

			}
			String group = model.getEvent(j).getGroup();
			model.delete(model.getEvent(j));
			model.add(new TimelineEvent(tt, startDate, endDate, true, group,
					styleClass));

		} else if (j != -1) {
			model.delete(model.getEvent(j));
		}

		updateChapterTimeLineEvent(tt.getRequirement().getChapter());
	}

	// }
	//
	// public void replaceTimeLineEventReal(JJTask tt, boolean delete) {
	// int i = containTaskData(tt.getId());
	//
	// int workLoad = 0;
	// Date startDate = null, endDate = null;
	// int j = findInEventTimeLine(tt, true);
	//
	// if (!delete && i != -1) {
	// if (tt.getStartDateReal() != null) {
	//
	// startDate = tt.getStartDateReal();
	// if (tt.getEndDateReal() == null)
	// endDate = tt.getEndDatePlanned();
	// else
	// endDate = tt.getEndDateReal();
	// workLoad = tt.getWorkloadReal();
	//
	// }
	// }
	//
	// if (j != -1) {
	//
	// String style = model.getEvent(j).getStyleClass();
	// String group = model.getEvent(j).getGroup();
	// model.delete(model.getEvent(j));
	//
	// if (!delete && startDate != null)
	// model.add(new TimelineEvent(tt, startDate, endDate, true,
	// group, style));
	//
	// int in = findInEventTimeLine(tt, false);
	//
	// if (in != -1) {
	//
	// model.delete(model.getEvent(in));
	//
	// if (!delete) {
	//
	// startDate = null;
	// endDate = null;
	//
	// if (!delete && i != -1) {
	// if (tt.getStartDateRevised() != null) {
	//
	// startDate = tt.getStartDateRevised();
	// style = "revised";
	// if (tt.getEndDateRevised() == null)
	// endDate = tt.getEndDatePlanned();
	// else
	// endDate = tt.getEndDateRevised();
	// workLoad = tt.getWorkloadRevised();
	//
	// } else {
	//
	// style = "planned";
	// endDate = tt.getEndDatePlanned();
	// startDate = tt.getStartDatePlanned();
	// workLoad = tt.getWorkloadPlanned();
	//
	// }
	// }
	//
	// TimelineEvent event = new TimelineEvent(tt, startDate,
	// endDate, true, group, style);
	//
	// model.add(event);
	// }
	// }
	//
	// }
	//
	// if (i != -1) {
	// if (!delete) {
	// TaskData tskst = new TaskData(tt,
	// tasksData.get(i).getChapter(), startDate, endDate,
	// workLoad, tt.getStartDateRevised() != null);
	//
	// tasksData.set(i, tskst);
	// } else
	// tasksData.remove(i);
	//
	// }
	//
	// }
	//
	// public void replaceTimeLineEvent(JJTask tt, boolean delete,
	// String styleClass) {
	//
	// int i = containTaskData(tt.getId());
	//
	// int workLoad = 0;
	// Date startDate = null, endDate = null;
	//
	// int j = findInEventTimeLine(tt, styleClass.equalsIgnoreCase("real"));
	//
	// if (!delete && i != -1) {
	// if (tt.getStartDateRevised() != null) {
	//
	// styleClass = "revised";
	// startDate = tt.getStartDateRevised();
	// if (tt.getEndDateRevised() == null)
	// endDate = tt.getEndDatePlanned();
	// else
	// endDate = tt.getEndDateRevised();
	// workLoad = tt.getWorkloadRevised();
	//
	// } else {
	//
	// styleClass = "planned";
	// endDate = tt.getEndDatePlanned();
	// startDate = tt.getStartDatePlanned();
	// workLoad = tt.getWorkloadPlanned();
	//
	// }
	// }
	//
	// if (j != -1) {
	//
	// String group = model.getEvent(j).getGroup();
	// model.delete(model.getEvent(j));
	// if (!delete)
	// model.add(new TimelineEvent(tt, startDate, endDate, true,
	// group, styleClass));
	// int in = findInEventTimeLine(tt, true);
	// if (in != -1)
	// model.delete(model.getEvent(in));
	//
	// if (tt.getStartDateReal() != null && !delete) {
	//
	// Date endDatereal;
	//
	// if (tt.getEndDateReal() == null)
	// endDatereal = tt.getEndDatePlanned();
	// else
	// endDatereal = tt.getEndDateReal();
	//
	// TimelineEvent event = new TimelineEvent(tt,
	// tt.getStartDateReal(), endDatereal, true, group, "real");
	//
	// model.add(event);
	//
	// }
	//
	// }
	//
	// if (i != -1) {
	// if (!delete) {
	// TaskData tskst = new TaskData(tt,
	// tasksData.get(i).getChapter(), startDate, endDate,
	// workLoad, tt.getStartDateRevised() != null);
	//
	// tasksData.set(i, tskst);
	// } else
	// tasksData.remove(i);
	//
	// }
	//
	// }

	public HtmlPanelGrid populateViewPanelGrid() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		Application application = facesContext.getApplication();
		ExpressionFactory expressionFactory = application
				.getExpressionFactory();
		ELContext elContext = facesContext.getELContext();

		HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application
				.createComponent(HtmlPanelGrid.COMPONENT_TYPE);

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
						"#{jJTasnullkBean.JJTask_.description}", String.class));
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

}
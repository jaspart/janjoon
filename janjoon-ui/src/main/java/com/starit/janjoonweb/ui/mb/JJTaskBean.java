package com.starit.janjoonweb.ui.mb;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.schedule.Schedule;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.extensions.event.timeline.TimelineAddEvent;
import org.primefaces.extensions.event.timeline.TimelineModificationEvent;
import org.primefaces.extensions.model.layout.LayoutOptions;
import org.primefaces.extensions.model.timeline.TimelineEvent;
import org.primefaces.extensions.model.timeline.TimelineModel;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJCategoryService;
import com.starit.janjoonweb.domain.JJChapter;
import com.starit.janjoonweb.domain.JJChapterService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJPermissionService;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJProjectService;
import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJSprint;
import com.starit.janjoonweb.domain.JJStatus;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJTestcase;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.ui.mb.util.CalendarUtil;
import com.starit.janjoonweb.ui.mb.util.ContactCalendarUtil;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;
import com.starit.janjoonweb.ui.mb.util.SprintUtil;

@RooJsfManagedBean(entity = JJTask.class, beanName = "jJTaskBean")
public class JJTaskBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(JJTaskBean.class);
	public static String Planned = "planned";
	public static String Real = "real";
	public static String Revised = "revised";
	public static final String UPDATE_OPERATION = "update";
	public static final String DELETE_OPERATION = "delete";
	public static final String ADD_OPERATION = "add";

	@Autowired
	private JJChapterService jJChapterService;

	private TaskData selectedTaskData;

	@Autowired
	private JJPermissionService jJPermissionService;

	@Autowired
	private JJProjectService jJProjectService;

	public void setjJProjectService(JJProjectService jJProjectService) {
		this.jJProjectService = jJProjectService;
	}

	@Autowired
	JJCategoryService jJCategoryService;

	public void setJjChapterService(JJChapterService jJChapterService) {
		this.jJChapterService = jJChapterService;
	}

	public void setjJPermissionService(
			JJPermissionService jJPermissionService) {
		this.jJPermissionService = jJPermissionService;
	}

	public void setjJCategoryService(JJCategoryService jJCategoryService) {
		this.jJCategoryService = jJCategoryService;
	}

	private List<JJTask> allTasks;
	private List<TaskData> tasksData;
	private JJTask task;

	private TimelineModel model;

	private Date start;
	private Date end;
	private long zoomMin;
	private long zoomMax;
	private String sortMode;

	private JJProject project;
	private JJProduct product;
	private JJVersion version;

	private JJSprint sprint;
	private List<JJSprint> sprints;
	private List<JJTask> toDoTasks;

	private boolean disabledImportButton;
	private boolean disabledFilter;
	private boolean checkAll;

	// private JJSprint importSprint;
	private String mode;

	private JJCategory importCategory;
	private List<JJCategory> importCategoryList;

	private JJStatus importStatus;
	private List<JJStatus> importStatusList;

	private String objet;
	private List<String> objets;

	private List<ImportFormat> importFormats;
	private List<ImportFormat> selectedImportFormat;

	private List<JJStatus> taskStatus;
	private List<JJStatus> taskTypeStatus;

	public String getSortMode() {
		return sortMode;
	}

	public void setSortMode(String sortMode) {
		this.sortMode = sortMode;
	}

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

	public List<JJTask> getAllTasks() {
		return allTasks;
	}

	public void setAllTasks(List<JJTask> allTasks) {
		this.allTasks = allTasks;
	}

	public List<JJTask> getToDoTasks() {
		return toDoTasks;
	}

	public void setToDoTasks(List<JJTask> toDoTasks) {
		this.toDoTasks = toDoTasks;
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

	public long getZoomMax() {
		return zoomMax;
	}

	public void setZoomMax(long zoomMax) {
		this.zoomMax = zoomMax;
	}

	// public List<JJContact> getContacts() {
	//
	// getProject();
	// JJProduct product = LoginBean.getProduct();
	//
	// contacts = jJPermissionService.areAuthorized(project.getManager()
	// .getCompany(), null, project, product, "Task");
	//
	// return contacts;
	// }

	// public void setContacts(List<JJContact> contacts) {
	// this.contacts = contacts;
	// }

	public JJProject getProject() {
		this.project = LoginBean.getProject();
		return project;
	}

	public void setProject(JJProject project) {
		this.project = project;
	}

	public JJProduct getProduct() {

		this.product = LoginBean.getProduct();

		return product;
	}

	public void setProduct(JJProduct product) {
		this.product = product;
	}

	public JJVersion getVersion() {

		this.version = LoginBean.getVersion();
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

		if (sprints == null) {
			getProject();
			sprints = jJSprintService.getSprints(project, true);
		}
		return sprints;
	}

	public void setSprints(List<JJSprint> sprints) {
		this.sprints = sprints;
	}

	public List<JJStatus> getTaskTypeStatus() {
		if (taskTypeStatus == null)
			taskTypeStatus = jJStatusService.getStatus("TaskType", true, null,
					true);
		return taskTypeStatus;
	}

	public void setTaskTypeStatus(List<JJStatus> taskTypeStatus) {
		this.taskTypeStatus = taskTypeStatus;
	}

	public List<JJStatus> getTaskStatus() {
		if (taskStatus == null)
			taskStatus = jJStatusService.getStatus("Task", true, null, true);
		return taskStatus;
	}

	public void setTaskStatus(List<JJStatus> taskStatus) {
		this.taskStatus = taskStatus;
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
					true, true, LoginBean.getCompany());

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
			if (objet.equalsIgnoreCase("bug")) {
				names.add("CLOSED");
			} else if (objet.equalsIgnoreCase("requirement")) {
				names.add("DELETED");
				names.add("CANCELED");
			} else if (objet.equalsIgnoreCase("testcase")) {
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
		objets.add("bug");
		objets.add("requirement");
		objets.add("testcase");

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

	public List<ImportFormat> getSelectedImportFormat() {
		return selectedImportFormat;
	}

	public void setSelectedImportFormat(
			List<ImportFormat> selectedImportFormat) {
		this.selectedImportFormat = selectedImportFormat;
	}

	// public boolean getCopyObjets() {
	// return copyObjets;
	// }
	//
	// public void setCopyObjets(boolean copyObjets) {
	// this.copyObjets = copyObjets;
	// }

	public String getDialogHeader(JJTask ttt, String scrum) {

		// LoginBean loginBean = (LoginBean) LoginBean.findBean("loginBean");
		if (ttt != null) {
			if (ttt.getRequirement() != null)
				return "TASK[" + ttt.getId() + "]-REQ["
						+ ttt.getRequirement().getId() + "] "
						+ ttt.getName().replace("\'", " ");
			else if (ttt.getBug() != null)
				return "TASK[" + ttt.getId() + "]-BUG[" + ttt.getBug().getId()
						+ "] " + ttt.getName().replace("\'", " ");
			else if (ttt.getTestcase() != null)
				return "TASK[" + ttt.getId() + "]-TESTCASE["
						+ ttt.getTestcase().getId() + "] "
						+ ttt.getName().replace("\'", " ");
			else
				return "";
			// } else if (ttt != null && scrum != null && loginBean.isMobile())
			// {
			// if (ttt.getRequirement() != null)
			// return "TASK[" + ttt.getId() + "]-REQ[" +
			// ttt.getRequirement().getId() + "] "
			// + ttt.getName().replace("\'", " ").substring(0, Math.min(10,
			// ttt.getName().length()));
			// else if (ttt.getBug() != null)
			// return "TASK[" + ttt.getId() + "]-BUG[" + ttt.getBug().getId() +
			// "] "
			// + ttt.getName().replace("\'", " ").substring(0, Math.min(10,
			// ttt.getName().length()));
			// else if (ttt.getTestcase() != null)
			// return "TASK[" + ttt.getId() + "]-TESTCASE[" +
			// ttt.getTestcase().getId() + "] "
			// + ttt.getName().replace("\'", " ").substring(0, Math.min(10,
			// ttt.getName().length()));
			// else
			// return "";
		} else
			return "";

	}

	public void updateTask(String operation) {

		boolean validation_error = validateTaskFields();
		JJTask beforeUpdateTT = jJTaskService.findJJTask(task.getId());
		JJSprint ss = beforeUpdateTT.getSprint();
		ContactCalendarUtil calendarUtil;

		if (task.getAssignedTo() != null) {
			calendarUtil = new ContactCalendarUtil(task.getAssignedTo());

		} else {

			calendarUtil = new ContactCalendarUtil(jJProjectService
					.findJJProject(LoginBean.getProject().getId()).getManager()
					.getCompany());

		}

		if ((beforeUpdateTT.getWorkloadPlanned() == null
				&& task.getWorkloadPlanned() != null)
				|| (task.getWorkloadPlanned() != null
						&& !task.getWorkloadPlanned()
								.equals(beforeUpdateTT.getWorkloadPlanned())))
			calendarUtil.getEndDate(task, Planned, jJTaskService);
		else {
			if ((beforeUpdateTT.getEndDatePlanned() == null
					&& task.getEndDatePlanned() != null)
					|| (task.getEndDatePlanned() != null
							&& !task.getEndDatePlanned().equals(
									beforeUpdateTT.getEndDatePlanned())))
				calendarUtil.getStartDate(task, Planned, jJTaskService);
			else {
				if ((beforeUpdateTT.getStartDatePlanned() == null
						&& task.getStartDatePlanned() != null)
						|| (task.getStartDatePlanned() != null
								&& !task.getStartDatePlanned().equals(
										beforeUpdateTT.getStartDatePlanned())))
					calendarUtil.getEndDate(task, Planned, jJTaskService);
			}
		}

		if (task.getStatus() != null) {
			if (beforeUpdateTT.getStatus() == null
					|| !beforeUpdateTT.getStatus().equals(task.getStatus())) {
				if (task.getStatus().getName().equalsIgnoreCase("todo")) {
					task.setStartDateReal(null);
					task.setEndDateReal(null);
					task.setWorkloadReal(null);
				} else if (task.getStatus().getName()
						.equalsIgnoreCase("in progress")) {
					task.setEndDateReal(null);
					task.setWorkloadReal(null);
					if (task.getStartDateReal() == null)
						task.setStartDateReal(new Date());
				} else if (task.getStatus().getName()
						.equalsIgnoreCase("done")) {
					if (task.getStartDateReal() == null)
						task.setStartDateReal(new Date());

					if (task.getEndDateReal() == null)
						task.setEndDateReal(new Date());

				}
			}
		} else {
			task.setStatus(jJStatusService.getOneStatus("TODO", "Task", true));
			if (beforeUpdateTT.getStatus() == null
					|| !beforeUpdateTT.getStatus().equals(task.getStatus())) {

				task.setStartDateReal(null);
				task.setEndDateReal(null);
				task.setWorkloadReal(null);
			}

		}

		saveJJTask(task, true, new MutableInt(0));
		// task = jJTaskService.findJJTask(task.getId());

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);

		if ((ss != null || task.getSprint() != null)
				&& session.getAttribute("jJSprintBean") != null) {

			JJSprintBean jJSprintBean = (JJSprintBean) session
					.getAttribute("jJSprintBean");

			if (ss != null && task.getSprint() != null) {
				SprintUtil s = SprintUtil.getSprintUtil(
						task.getSprint().getId(), jJSprintBean.getSprintList());

				if (s != null) {
					s = new SprintUtil(
							jJSprintService
									.findJJSprint(task.getSprint().getId()),
							jJTaskService.getSprintTasks(
									jJSprintService.findJJSprint(
											task.getSprint().getId()),
									LoginBean.getProduct()),
							jJContactService, jJTaskService);

					// sprintUtil.setRenderTaskForm(false);
					jJSprintBean.getSprintList().set(
							jJSprintBean.contains(s.getSprint().getId()), s);
				}
				if (!ss.equals(task.getSprint())) {
					s = SprintUtil.getSprintUtil(ss.getId(),
							jJSprintBean.getSprintList());

					if (s != null) {
						s = new SprintUtil(
								jJSprintService.findJJSprint(ss.getId()),
								jJTaskService.getSprintTasks(
										jJSprintService
												.findJJSprint(ss.getId()),
										LoginBean.getProduct()),
								jJContactService, jJTaskService);

						// sprintUtil.setRenderTaskForm(false);
						jJSprintBean.getSprintList().set(
								jJSprintBean.contains(s.getSprint().getId()),
								s);
					}
				}
			} else {
				if (task.getSprint() != null) {

					SprintUtil s = SprintUtil.getSprintUtil(
							task.getSprint().getId(),
							jJSprintBean.getSprintList());

					if (s != null) {
						s = new SprintUtil(
								jJSprintService
										.findJJSprint(task.getSprint().getId()),
								jJTaskService.getSprintTasks(
										jJSprintService.findJJSprint(
												task.getSprint().getId()),
										LoginBean.getProduct()),
								jJContactService, jJTaskService);

						// sprintUtil.setRenderTaskForm(false);
						jJSprintBean.getSprintList().set(
								jJSprintBean.contains(s.getSprint().getId()),
								s);
					}

				} else {
					SprintUtil s = SprintUtil.getSprintUtil(ss.getId(),
							jJSprintBean.getSprintList());

					if (s != null) {
						s = new SprintUtil(
								jJSprintService.findJJSprint(ss.getId()),
								jJTaskService.getSprintTasks(
										jJSprintService
												.findJJSprint(ss.getId()),
										LoginBean.getProduct()),
								jJContactService, jJTaskService);

						// sprintUtil.setRenderTaskForm(false);
						jJSprintBean.getSprintList().set(
								jJSprintBean.contains(s.getSprint().getId()),
								s);
					}
				}
			}
		}

		updateView(jJTaskService.findJJTask(task.getId()), UPDATE_OPERATION);
		task = jJTaskService.findJJTask(task.getId());
		// taskTreeNode = null;
		// selectedReq = null;
		// selectedTree = null;
		// getTaskTreeNode();

		if (operation.equalsIgnoreCase("main")) {
			toDoTasks = null;
			initToDoTasks(null);
		} else if (operation.equalsIgnoreCase("dev")
				&& LoginBean.findBean("jJDevelopment") != null) {
			((DevelopmentBean) LoginBean.findBean("jJDevelopment"))
					.setTask(task);
			((DevelopmentBean) LoginBean.findBean("jJDevelopment")).setTasks(
					jJTaskService.getTasksByProduct(LoginBean.getProduct(),
							LoginBean.getProject(), true));
		} else if (!validation_error
				&& operation.equalsIgnoreCase("planning")) {
			RequestContext.getCurrentInstance().update("projecttabview");
		}

		if (!validation_error) {
			FacesMessage facesMessage = MessageFactory.getMessage(
					"message_successfully_updated",
					MessageFactory.getMessage("label_task", "").getDetail(),
					"e");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);

			RequestContext.getCurrentInstance()
					.execute("PF('viewTaskDialogWidget').hide()");
			RequestContext.getCurrentInstance().update("growlForm");
		}

	}

	private boolean validateTaskDataFields(boolean start, String field,
			Date date, JJTask tt) {

		boolean error = false;

		if (tt.getSprint() != null) {

			if (date.compareTo(tt.getSprint().getStartDate())
					* CalendarUtil.getAfterDay(tt.getSprint().getEndDate())
							.compareTo(date) < 0) {

				error = true;

				FacesMessage facesMessage = null;

				if (field.equals(Revised)) {
					if (start) {
						tt.setStartDateRevised(tt.getSprint().getStartDate());
						facesMessage = MessageFactory.getMessage(
								"validator_date_startAfterEndORStartBeforeStart",
								MessageFactory.getMessage("label_task", "")
										.getDetail(),
								"Sprint");
						facesMessage.setSeverity(FacesMessage.SEVERITY_WARN);
					} else {

						facesMessage = MessageFactory.getMessage(
								"validator_date_endAfterEndOREndBeforeStart",
								MessageFactory.getMessage("label_task", "")
										.getDetail(),
								"Sprint");
						facesMessage.setSeverity(FacesMessage.SEVERITY_WARN);

						ContactCalendarUtil calendarUtil;
						if (tt.getAssignedTo() != null) {
							calendarUtil = new ContactCalendarUtil(
									tt.getAssignedTo());

						} else {
							calendarUtil = new ContactCalendarUtil(
									tt.getProject().getManager().getCompany());
						}
						calendarUtil.getEndDate(tt, JJTaskBean.Revised,
								jJTaskService);
					}
				} else if (field.equals(Real)) {
					if (start) {
						tt.setStartDateReal(tt.getSprint().getStartDate());
						facesMessage = MessageFactory.getMessage(
								"validator_date_startAfterEndORStartBeforeStart",
								MessageFactory.getMessage("label_task", "")
										.getDetail(),
								"Sprint");
						facesMessage.setSeverity(FacesMessage.SEVERITY_WARN);
					} else {

						facesMessage = MessageFactory.getMessage(
								"validator_date_endAfterEndOREndBeforeStart",
								MessageFactory.getMessage("label_task", "")
										.getDetail(),
								"Sprint");
						facesMessage.setSeverity(FacesMessage.SEVERITY_WARN);

						ContactCalendarUtil calendarUtil;
						if (tt.getAssignedTo() != null) {
							calendarUtil = new ContactCalendarUtil(
									tt.getAssignedTo());

						} else {
							calendarUtil = new ContactCalendarUtil(
									tt.getProject().getManager().getCompany());
						}
						calendarUtil.getEndDate(tt, JJTaskBean.Real,
								jJTaskService);

					}
				}
				if (facesMessage != null)
					FacesContext.getCurrentInstance().addMessage(null,
							facesMessage);
			}
		}
		return error;

	}

	private boolean validateTaskFields() {

		boolean error = false;

		if (task.getSprint() != null) {

			if (task.getStartDatePlanned()
					.compareTo(task.getSprint().getStartDate())
					* CalendarUtil.getAfterDay(task.getSprint().getEndDate())
							.compareTo(task.getStartDatePlanned()) < 0) {
				FacesMessage facesMessage = MessageFactory.getMessage(
						"validator_date_startAfterEndORStartBeforeStart",
						MessageFactory.getMessage("label_task", "").getDetail(),
						"Sprint");
				facesMessage.setSeverity(FacesMessage.SEVERITY_WARN);
				error = true;

				task.setStartDatePlanned(task.getSprint().getStartDate());
				FacesContext.getCurrentInstance().addMessage(null,
						facesMessage);
			}

			if (task.getEndDatePlanned()
					.compareTo(task.getSprint().getStartDate())
					* CalendarUtil.getAfterDay(task.getSprint().getEndDate())
							.compareTo(task.getEndDatePlanned()) < 0) {
				FacesMessage facesMessage = MessageFactory.getMessage(
						"validator_date_endAfterEndOREndBeforeStart",
						MessageFactory.getMessage("label_task", "").getDetail(),
						"Sprint");
				facesMessage.setSeverity(FacesMessage.SEVERITY_WARN);
				error = true;

				ContactCalendarUtil calendarUtil;
				if (task.getAssignedTo() != null) {
					calendarUtil = new ContactCalendarUtil(
							task.getAssignedTo());

				} else {
					calendarUtil = new ContactCalendarUtil(
							task.getProject().getManager().getCompany());
				}

				calendarUtil.getEndDate(task, JJTaskBean.Planned,
						jJTaskService);
				FacesContext.getCurrentInstance().addMessage(null,
						facesMessage);

			}
		}
		return error;

	}

	public void closeEvent(String operation) {
		if (operation.equalsIgnoreCase("main"))
			toDoTasks = null;

	}

	public List<JJStatus> completeStatusTask(String query) {
		List<JJStatus> suggestions = new ArrayList<JJStatus>();
		suggestions.add(null);
		for (JJStatus jJStatus : getTaskStatus()) {
			String jJCriticityStr = String.valueOf(jJStatus.getName());
			if (jJCriticityStr.toLowerCase().startsWith(query.toLowerCase())) {
				suggestions.add(jJStatus);
			}
		}
		return suggestions;
	}

	public List<JJStatus> completeTaskType(String query) {
		List<JJStatus> suggestions = new ArrayList<JJStatus>();
		suggestions.add(null);
		for (JJStatus jJStatus : getTaskTypeStatus()) {
			String jJCriticityStr = String.valueOf(jJStatus.getName());
			if (jJCriticityStr.toLowerCase().startsWith(query.toLowerCase())) {
				suggestions.add(jJStatus);
			}
		}
		return suggestions;
	}

	public List<JJContact> completeAssignedToTask(String query) {

		List<JJContact> suggestions = new ArrayList<JJContact>();
		suggestions.add(null);
		if (task.getSprint() == null) {
			JJProject proj = task.getProject();

			for (JJContact jJContact : jJPermissionService.areAuthorized(
					proj.getManager().getCompany(), null, proj, null,
					"sprintContact", null, true, null, true)) {
				String jJCriticityStr = String.valueOf(jJContact.getName());
				if (jJCriticityStr.toLowerCase()
						.startsWith(query.toLowerCase())) {
					suggestions.add(jJContact);
				}
			}
		} else {
			JJSprint sp = jJSprintService
					.findJJSprint(task.getSprint().getId());
			for (JJContact jJContact : sp.getContacts()) {
				String jJCriticityStr = String.valueOf(jJContact.getName());
				if (jJCriticityStr.toLowerCase()
						.startsWith(query.toLowerCase())) {
					suggestions.add(jJContact);
				}
			}
		}

		return suggestions;
	}

	public List<JJSprint> completeSprintTask(String query) {
		List<JJSprint> suggestions = new ArrayList<JJSprint>();
		suggestions.add(null);

		JJProject proj = (JJProject) UIComponent
				.getCurrentComponent(FacesContext.getCurrentInstance())
				.getAttributes().get("project");

		for (JJSprint jJSprint : jJSprintService.getSprints(proj, true)) {
			String jJCriticityStr = String.valueOf(jJSprint.getName());
			if (jJCriticityStr.toLowerCase().startsWith(query.toLowerCase())) {
				suggestions.add(jJSprint);
			}
		}
		return suggestions;
	}

	public void loadingData() {

		initLayoutOptions();
		if (tasksData == null) {
			getProject();
			loadData();
		} else {
			if (tasksData.size() != 0) {
				if (!project.equals(LoginBean.getProject())) {
					loadData();
				}
			} else {
				loadData();
			}
		}

	}

	public void loadData() {

		long t = System.currentTimeMillis();
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		LoginBean loginBean = (LoginBean) session.getAttribute("loginBean");

		if (loginBean.getAuthorisationService().isrProject()) {
			// Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

			if (allTasks == null)
				allTasks = jJTaskService.getPlannedTaks(sprint, project,
						LoginBean.getProduct(), null, LoginBean.getCompany());

			// Date now = new Date();
			if (sortMode == null) {
				sortMode = "chapter";

			} else if (sortMode.isEmpty()) {
				sortMode = "chapter";
			}

			if (sprint == null) {
				// Before 4 hours for now
				// cal.setTimeInMillis(LoginBean.getProject().getStartDate().getTime());
				start = LoginBean.getProject().getStartDate();

				// After 8 hours for now
				// cal.setTimeInMillis(now.getTime() + 24 * 60 * 60 * 1000);
				end = LoginBean.getProject().getEndDate();
			} else {
				start = sprint.getStartDate();
				end = sprint.getEndDate();
			}

			// seven day in milliseconds for zoomMin
			zoomMin = 1000L * 60 * 60 * 24 * 3;

			// 3 months in milliseconds for zoomMax
			zoomMax = 1000L * 60 * 60 * 24 * 31 * 3;

			// Create timeline model
			model = new TimelineModel();

			getProject();
			tasksData = new ArrayList<TaskData>();

			// 65 = ASCII A
			int k = 65;

			// List<JJTask> allJJtask = null;
			// if (!sortMode.equalsIgnoreCase("chapter"))
			// allJJtask = new ArrayList<JJTask>();
			if (sortMode.equalsIgnoreCase("chapter")) {

				List<JJChapter> chapters = jJChapterService
						.getChapters(LoginBean.getCompany(), project, true);

				for (JJChapter chapter : chapters) {
					Map<Date, String> min = new TreeMap<Date, String>();
					Map<Date, String> max = new TreeMap<Date, String>();

					// List<JJTask> tasks = new ArrayList<JJTask>();
					// tasks.addAll(jJTaskService.getTasks(sprint, null,
					// LoginBean.getProduct(), null, null, null, chapter, true,
					// null, null, null, null, true, true, false,
					// "Requirement"));
					//
					// tasks.addAll(jJTaskService.getTasks(sprint, null,
					// LoginBean.getProduct(), null, null, null, chapter, true,
					// null, null, null, null, true, true, false, "Testcase"));
					//
					// tasks.addAll(jJTaskService.getTasks(sprint, null,
					// LoginBean.getProduct(), null, null, null, chapter, true,
					// null, null, null, null, true, true, false, "Bug"));

					TreeMap<String, JJTask> Tasks = new TreeMap<String, JJTask>();

					for (JJTask tt : allTasks) {
						if (tt.getChapter() != null
								&& tt.getChapter().equals(chapter))
							if ((tt.getSprint() != null && sprint != null
									&& sprint.equals(tt.getSprint()))
									|| sprint == null)
								Tasks.put(tt.getId() + "", tt);
					}

					// if (!sortMode.equalsIgnoreCase("chapter"))
					// for (String key : Tasks.keySet())
					// allJJtask.add(Tasks.get(key));

					// Iterate over HashMap
					// if (sortMode.equalsIgnoreCase("chapter")) {
					for (String key : Tasks.keySet()) {

						if (k == 92)
							k++;

						JJTask tt = Tasks.get(key);

						char c = (char) k;
						String group = "<span style=display:none>" + c
								+ "</span>";
						// + task.getName();
						TaskData taskData = null;
						boolean add = false;

						new GregorianCalendar(2010, 1, 1).getTime();
						model.add(
								new TimelineEvent(tt,
										new GregorianCalendar(
												Calendar.getInstance()
														.get(Calendar.YEAR) - 5,
												1, 1).getTime(),
										new GregorianCalendar(
												Calendar.getInstance()
														.get(Calendar.YEAR) + 5,
												1, 1).getTime(),
										false, group, "invisible"));

						if (tt.getStartDateReal() != null) {

							Date endDate;
							if (tt.getEndDateReal() == null)
								endDate = tt.getEndDatePlanned();
							else
								endDate = tt.getEndDateReal();

							TimelineEvent event = new TimelineEvent(tt,
									tt.getStartDateReal(), endDate, true, group,
									Real);

							model.add(event);

						}
						if (tt.getStartDateRevised() != null) {

							Date endDate;
							String styleClass;

							if (tt.getStartDateReal() == null)
								styleClass = "revised1";
							else
								styleClass = "revised2";

							if (tt.getEndDateRevised() == null)
								endDate = tt.getEndDatePlanned();
							else
								endDate = tt.getEndDateRevised();

							TimelineEvent event = new TimelineEvent(tt,
									tt.getStartDateRevised(), endDate, true,
									group, styleClass);
							model.add(event);

							int workload = 0;

							if (tt.getWorkloadRevised() == null) {
								if (tt.getWorkloadPlanned() != null)
									workload = tt.getWorkloadPlanned();
							} else
								workload = tt.getWorkloadRevised();

							if (!add)
								taskData = new TaskData(tt, chapter,
										tt.getStartDateRevised(), endDate,
										workload, true);
						} else {

							String style = null;
							if (tt.getStartDateReal() == null)
								style = "planned1";
							else
								style = "planned2";
							TimelineEvent event = new TimelineEvent(tt,
									tt.getStartDatePlanned(),
									tt.getEndDatePlanned(), true, group, style);
							model.add(event);

							if (!add)
								taskData = new TaskData(tt, chapter,
										tt.getStartDatePlanned(),
										tt.getEndDatePlanned(),
										tt.getWorkloadPlanned(), false);
						}

						tasksData.add(taskData);

						if (tt.getStartDatePlanned() != null) {
							min.put(tt.getStartDatePlanned(),
									tt.getStartDatePlanned().toString());
						}

						if (tt.getStartDateRevised() != null) {
							min.put(tt.getStartDateRevised(),
									tt.getStartDateRevised().toString());
						}

						if (tt.getStartDateReal() != null) {
							min.put(tt.getStartDateReal(),
									tt.getStartDateReal().toString());
						}

						if (tt.getEndDatePlanned() != null) {
							max.put(tt.getEndDatePlanned(),
									tt.getEndDatePlanned().toString());
						}

						if (tt.getEndDateRevised() != null) {
							max.put(tt.getEndDateRevised(),
									tt.getEndDateRevised().toString());
						}

						if (tt.getEndDateReal() != null) {
							max.put(tt.getEndDateReal(),
									tt.getEndDateReal().toString());
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

						String group = "<span style=display:none>" + c
								+ "</span>";
						// + task.getName();
						TimelineEvent event = new TimelineEvent(chapter, start,
								end, false, group, "chapter");
						model.add(
								new TimelineEvent(chapter,
										new GregorianCalendar(
												Calendar.getInstance()
														.get(Calendar.YEAR) - 5,
												1, 1).getTime(),
										new GregorianCalendar(
												Calendar.getInstance()
														.get(Calendar.YEAR) + 5,
												1, 1).getTime(),
										false, group, "invisible"));

						model.add(event);
						k++;

					}
					// }
				}
			} else
				loadSortedData(k);

			task = null;
		} else {
			String path = FacesContext.getCurrentInstance().getExternalContext()
					.getRequestContextPath();

			try {
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect(path + "/pages/main.jsf?faces-redirect=true");
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		logger.info("TaskTracker=" + (System.currentTimeMillis() - t));

	}

	public void loadSortedData(int k) {

		// allTasks.addAll(jJTaskService.getTasks(sprint, project,
		// LoginBean.getProduct(), null, null, null, null, true, null,
		// null, null, null, true, false, false, "requirement"));
		// allTasks.addAll(jJTaskService.getTasks(sprint, project,
		// LoginBean.getProduct(), null, null, null, null, true, null,
		// null, null, null, true, false, false, "bug"));
		// allTasks.addAll(jJTaskService.getTasks(sprint, project,
		// LoginBean.getProduct(), null, null, null, null, true, null,
		// null, null, null, true, false, false, "Testcase"));

		Collections.sort(allTasks, new Comparator<JJTask>() {

			@Override
			public int compare(JJTask o1, JJTask o2) {
				if (sortMode.equalsIgnoreCase("StartDatePlanned")) {
					return o1.getStartDatePlanned()
							.compareTo(o2.getStartDatePlanned());

				} else if (sortMode.equalsIgnoreCase("StartDateReal")) {
					Date o1Date, o2Date;
					if (o1.getStartDateReal() == null)
						o1Date = o1.getStartDatePlanned();
					else
						o1Date = o1.getStartDateReal();

					if (o2.getStartDateReal() == null)
						o2Date = o2.getStartDatePlanned();
					else
						o2Date = o2.getStartDateReal();

					return o1Date.compareTo(o2Date);
				} else
					return 0;

			}
		});

		for (JJTask tt : allTasks) {
			if ((tt.getSprint() != null && sprint != null
					&& sprint.equals(tt.getSprint())) || sprint == null) {
				if (k == 92)
					k++;

				char c = (char) k;
				String group = "<span style=display:none>" + c + "</span>";
				boolean add = false;
				JJChapter chapter = tt.getChapter();

				TaskData taskData = null;
				// System.err.println(group + "/" + tt.getName());

				model.add(new TimelineEvent(tt,
						new GregorianCalendar(
								Calendar.getInstance().get(Calendar.YEAR) - 5,
								1, 1)
										.getTime(),
						new GregorianCalendar(
								Calendar.getInstance().get(Calendar.YEAR) + 5,
								1, 1).getTime(),
						false, group, "invisible"));

				if (tt.getStartDateReal() != null) {

					Date endDate;
					if (tt.getEndDateReal() == null)
						endDate = tt.getEndDatePlanned();
					else
						endDate = tt.getEndDateReal();

					TimelineEvent event = new TimelineEvent(tt,
							tt.getStartDateReal(), endDate, true, group, Real);

					model.add(event);

				}
				if (tt.getStartDateRevised() != null) {

					Date endDate;
					if (tt.getEndDateRevised() == null)
						endDate = tt.getEndDatePlanned();
					else
						endDate = tt.getEndDateRevised();

					String styleClass;

					if (tt.getStartDateReal() == null)
						styleClass = "revised1";
					else
						styleClass = "revised2";

					TimelineEvent event = new TimelineEvent(tt,
							tt.getStartDateRevised(), endDate, true, group,
							styleClass);
					model.add(event);

					int workload = 0;

					if (tt.getWorkloadRevised() == null) {
						if (tt.getWorkloadPlanned() != null)
							workload = tt.getWorkloadPlanned();
					} else
						workload = tt.getWorkloadRevised();

					if (!add)
						taskData = new TaskData(tt, chapter,
								tt.getStartDateRevised(), endDate, workload,
								true);
				} else {

					String style = null;
					if (tt.getStartDateReal() == null)
						style = "planned1";
					else
						style = "planned2";

					TimelineEvent event = new TimelineEvent(tt,
							tt.getStartDatePlanned(), tt.getEndDatePlanned(),
							true, group, style);
					model.add(event);

					if (!add)
						taskData = new TaskData(tt, chapter,
								tt.getStartDatePlanned(),
								tt.getEndDatePlanned(), tt.getWorkloadPlanned(),
								false);
				}

				tasksData.add(taskData);
				k++;

			}

		}

	}

	public void onCellEdit(CellEditEvent event) {
		UIColumn column = event.getColumn();

		String columnKey = column.getColumnKey();

		DataTable dataTable = (DataTable) event.getSource();
		TaskData taskData = (TaskData) dataTable.getRowData();
		JJTask task = taskData.getTask();
		ContactCalendarUtil calendarUtil;
		boolean validation_error = false;

		if (task.getAssignedTo() != null) {
			calendarUtil = new ContactCalendarUtil(task.getAssignedTo());

		} else {

			calendarUtil = new ContactCalendarUtil(jJProjectService
					.findJJProject(LoginBean.getProject().getId()).getManager()
					.getCompany());

		}
		Object newValue = event.getNewValue();
		FacesMessage facesMessage = null;
		boolean valid = true;

		if (columnKey.contains("sdpr")) {
			if (newValue != null) {
				Date date = (Date) newValue;
				task.setStartDateRevised(date);
				validation_error = validateTaskDataFields(true, Revised, date,
						task);

				if (taskData.getWorkload() != null) {

					calendarUtil.getEndDate(task, Revised, jJTaskService);
				} else
					task.setEndDateRevised(taskData.getEndDate());
			}

		} else if (columnKey.contains("edpr")) {
			if (newValue != null) {
				Date date = (Date) newValue;
				task.setEndDateRevised(date);
				validation_error = validateTaskDataFields(false, Revised, date,
						task);

				if (taskData.getStartDate() != null) {
					task.setStartDateRevised(taskData.getStartDate());

				} else {
					if (taskData.getWorkload() != null) {
						calendarUtil.getStartDate(task, Revised, jJTaskService);
					}
				}
			}

		} else if (columnKey.contains("wpr")) {

			if (newValue != null) {
				int workloadRevised = (int) newValue;
				task.setWorkloadRevised(workloadRevised);
				if (taskData.getStartDate() != null) {

					task.setStartDateRevised(taskData.getStartDate());

					calendarUtil.getEndDate(task, Revised, jJTaskService);

				} else if (taskData.getEndDate() != null) {
					calendarUtil.getStartDate(task, Revised, jJTaskService);
				}

			}

		} else if (columnKey.contains("sdr")) {
			if (newValue != null) {
				Date date = (Date) newValue;
				task.setStartDateReal(date);
				validation_error = validateTaskDataFields(true, Real, date,
						task);

				if (task.getWorkloadReal() != null) {
					calendarUtil.getEndDate(task, Real, jJTaskService);
				}

			}

		} else if (columnKey.contains("edr")) {
			if (newValue != null) {
				Date date = (Date) newValue;
				task.setEndDateReal(date);
				validation_error = validateTaskDataFields(false, Real, date,
						task);

				if (task.getStartDateReal() == null
						&& task.getWorkloadReal() != null)
					calendarUtil.getStartDate(task, Real, jJTaskService);
			}
		} else if (columnKey.contains("wr")) {
			if (newValue != null) {
				int workloadReal = (int) newValue;
				task.setWorkloadReal(workloadReal);
				if (task.getStartDateReal() != null) {

					calendarUtil.getEndDate(task, Real, jJTaskService);

				} else if (task.getEndDateReal() != null) {
					calendarUtil.getStartDate(task, Real, jJTaskService);
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

			String[] keys = columnKey.split(":");
			RequestContext.getCurrentInstance()
					.execute("setRowIndex(" + keys[keys.length - 2] + ")");

			saveJJTask(task, true, new MutableInt(0));
			task = jJTaskService.findJJTask(task.getId());
			updateView(task, UPDATE_OPERATION);
			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);

			if (task.getSprint() != null
					&& session.getAttribute("jJSprintBean") != null) {

				JJSprintBean jJSprintBean = (JJSprintBean) session
						.getAttribute("jJSprintBean");
				SprintUtil s = SprintUtil.getSprintUtil(
						task.getSprint().getId(), jJSprintBean.getSprintList());
				if (s != null) {
					s = new SprintUtil(
							jJSprintService
									.findJJSprint(task.getSprint().getId()),
							jJTaskService.getSprintTasks(
									jJSprintService.findJJSprint(
											task.getSprint().getId()),
									LoginBean.getProduct()),
							jJContactService, jJTaskService);

					// sprintUtil.setRenderTaskForm(false);
					jJSprintBean.getSprintList().set(
							jJSprintBean.contains(s.getSprint().getId()), s);
				}
			}
			reset();

			if (!validation_error)
				facesMessage = MessageFactory.getMessage(
						"message_successfully_updated",
						MessageFactory.getMessage("label_task", "").getDetail(),
						"e");
			// RequestContext.getCurrentInstance().equals("onCellEditTableComplete");

		} else {
			facesMessage = MessageFactory.getMessage(
					"message_unsuccessfully_updated",
					MessageFactory.getMessage("label_task", "").getDetail(),
					"de la");
			facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
		}
		if (facesMessage != null)
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

		// task.getTasks().add(duplicatedTask);

		duplicatedTask.setName(task.getName() + " duplicated");
		duplicatedTask.setDescription(task.getDescription());
		duplicatedTask.setEnabled(true);

		// duplicatedTask.setCompleted(task.getCompleted());
		// duplicatedTask.setStatus(task.getStatus());

		duplicatedTask.setStartDatePlanned(task.getStartDatePlanned());
		// duplicatedTask.setStartDateReal(task.getStartDateReal());
		// duplicatedTask.setStartDateRevised(task.getStartDateRevised());

		duplicatedTask.setEndDatePlanned(task.getEndDatePlanned());
		// duplicatedTask.setEndDateReal(task.getEndDateReal());
		// duplicatedTask.setEndDateRevised(task.getEndDateRevised());

		duplicatedTask.setWorkloadPlanned(task.getWorkloadPlanned());
		// duplicatedTask.setWorkloadReal(task.getWorkloadReal());
		// duplicatedTask.setWorkloadRevised(task.getWorkloadRevised());

		duplicatedTask.setConsumed(task.getConsumed());

		if (task.getRequirement() != null) {
			// JJRequirement requirement = jJRequirementService
			// .findJJRequirement(task.getRequirement().getId());
			duplicatedTask.setRequirement(task.getRequirement());
			// requirement.getTasks().add(duplicatedTask);
		}

		if (task.getTestcase() != null) {
			// JJTestcase testcase = jJTestcaseService.findJJTestcase(task
			// .getTestcase().getId());
			duplicatedTask.setTestcase(task.getTestcase());
			// testcase.getTasks().add(duplicatedTask);
		}

		if (task.getSprint() != null) {
			// JJSprint sprint = jJSprintService.findJJSprint(task.getSprint()
			// .getId());
			duplicatedTask.setSprint(task.getSprint());
		}

		if (task.getBug() != null) {
			// JJBug bug = jJBugService.findJJBug(task.getBug().getId());
			duplicatedTask.setBug(task.getBug());
			// bug.getTasks().add(duplicatedTask);
		}

		if (task.getVersioning() != null) {
			// JJVersion version = jJVersionService.findJJVersion(task
			// .getVersioning().getId());
			duplicatedTask.setVersioning(task.getVersioning());
			// version.getTasks().add(duplicatedTask);
		}

		saveJJTask(duplicatedTask, false, new MutableInt(0));

		// updateView(tJjTask, false);
		//
		if (duplicatedTask.getSprint() != null) {

			JJSprintBean jJSprintBean = (JJSprintBean) LoginBean
					.findBean("jJSprintBean");
			if (jJSprintBean != null && jJSprintBean
					.contains(duplicatedTask.getSprint().getId()) != -1) {
				SprintUtil s = SprintUtil.getSprintUtil(
						duplicatedTask.getSprint().getId(),
						jJSprintBean.getSprintList());
				if (s != null) {
					s = new SprintUtil(
							jJSprintService.findJJSprint(
									duplicatedTask.getSprint().getId()),
							jJTaskService.getSprintTasks(
									jJSprintService.findJJSprint(
											duplicatedTask.getSprint().getId()),
									LoginBean.getProduct()),
							jJContactService, jJTaskService);

					// sprintUtil.setRenderTaskForm(false);
					jJSprintBean.getSprintList().set(
							jJSprintBean.contains(s.getSprint().getId()), s);
				}
			}

		}

		duplicatedTask = null;
		loadData();

		reset();
	}

	public void loadImportFormat(ActionEvent e) {

		this.mode = (String) e.getComponent().getAttributes().get("mode");
		disabledImportButton = true;
		disabledFilter = true;

		checkAll = false;
		// copyObjets = false;
		// oldCopyObjects = false;

		if (mode.equalsIgnoreCase("scrum")) {

			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);
			JJSprintBean jJSprintBean = (JJSprintBean) session
					.getAttribute("jJSprintBean");
			jJSprintBean.attrListener(e);

			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('taskImportDialogWidget').show()");
		}

		getProject();
		getProduct();
		getVersion();

		importFormats = new ArrayList<ImportFormat>();
		selectedImportFormat = new ArrayList<ImportFormat>();

	}

	public void validateTaskField() {
		boolean validationFailed = false;
		int i = 0;
		FacesMessage message = null;
		while (i < selectedImportFormat.size() && !validationFailed) {
			ImportFormat format = selectedImportFormat.get(i);
			Date startDate = format.getStartDate();
			if (format.getStartDate() == null) {
				validationFailed = true;
				message = MessageFactory.getMessage(
						"validator_task_startDateRequired",
						FacesMessage.SEVERITY_ERROR, "ttt");

			} else {
				if (mode.equalsIgnoreCase("scrum")) {
					if (startDate.before(
							((JJSprintBean) LoginBean.findBean("jJSprintBean"))
									.getSprintUtil().getSprint()
									.getStartDate())) {

						format.setStartDate(((JJSprintBean) LoginBean
								.findBean("jJSprintBean")).getSprintUtil()
										.getSprint().getStartDate());

					} else if (startDate.after(
							((JJSprintBean) LoginBean.findBean("jJSprintBean"))
									.getSprintUtil().getSprint()
									.getEndDate())) {

						format.setStartDate(((JJSprintBean) LoginBean
								.findBean("jJSprintBean")).getSprintUtil()
										.getSprint().getStartDate());
					}
				} else {
					if (sprint != null) {
						if (startDate.before(sprint.getStartDate())) {

							format.setStartDate(sprint.getStartDate());
						} else if (startDate.after(sprint.getEndDate())) {

							format.setStartDate(sprint.getStartDate());
						}

					}
				}

			}
			if (!validationFailed) {

				Integer workload = format.getWorkload();
				if (workload == null) {

					validationFailed = true;
					message = MessageFactory
							.getMessage("validator_task_workloadRequired");
					message.setSeverity(FacesMessage.SEVERITY_ERROR);
				} else if (workload <= 0) {

					validationFailed = true;
					message = MessageFactory
							.getMessage("validator_task_workloadNegatif");
					message.setSeverity(FacesMessage.SEVERITY_ERROR);
				}

			}

			i++;
		}
		if (!validationFailed)
			RequestContext.getCurrentInstance().execute("importTask()");
		else {
			FacesContext.getCurrentInstance().addMessage(null, message);
			RequestContext.getCurrentInstance().execute(
					"PF('taskImportDialogWidget').jq.effect('shake', {times:5}, 100)");
		}

	}

	public void checkAll() {
		// copyObjets = false;
		// oldCopyObjects = false;
		disabledImportButton = true;

		fillTableImport();
	}

	//
	// public void changeSpecValueListener(ImportFormat format)
	// {
	// format.setSpecification(!format.getSpecification());
	// }

	public void fillTableImport() {

		importFormats = new ArrayList<ImportFormat>();
		selectedImportFormat = new ArrayList<ImportFormat>();
		// copyObjets = false;
		// oldCopyObjects = false;
		disabledImportButton = true;

		if (objet != null) {

			if (objet.equalsIgnoreCase("Bug")) {

				for (JJBug bug : jJBugService.getImportBugs(
						LoginBean.getCompany(), project, LoginBean.getProduct(),
						version, importCategory, importStatus, !checkAll,
						true)) {

					// if (!checkAll) {
					//
					// if (!jJTaskService.haveTask(bug, true, false, false)) {
					importFormats.add(new ImportFormat(bug.getName(), bug));
					// }
					//
					// } else {
					// importFormats.add(new ImportFormat(bug.getName(), bug));
					// }
				}

			} else if (objet.equalsIgnoreCase("Requirement")) {
				LoginBean loginBean = (LoginBean) LoginBean
						.findBean("loginBean");
				for (JJRequirement requirement : jJRequirementService
						.getRequirements(LoginBean.getCompany(), importCategory,
								loginBean.getAuthorizedMap("Requirement",
										project, product),
								version, importStatus, null, !checkAll, false,
								true, false, false, null)) {

					// if (!checkAll) {
					//
					// if (!jJTaskService.haveTask(requirement, true, false,
					// true)) {
					importFormats.add(new ImportFormat(requirement.getName(),
							requirement));
					// }
					// } else {
					//
					// importFormats.add(new ImportFormat(
					// requirement.getName(), requirement));
					// }
				}

			} else if (objet.equalsIgnoreCase("Testcase")) {

				for (JJTestcase testcase : jJTestcaseService.getImportTestcases(
						null, project, LoginBean.getProduct(),
						LoginBean.getVersion(), null, !checkAll, true, false)) {
					//
					// if (!checkAll) {
					//
					// if (!jJTaskService.haveTask(testcase, true, false,
					// true)) {
					importFormats.add(
							new ImportFormat(testcase.getName(), testcase));
					// }
					// } else {
					//
					// importFormats.add(
					// new ImportFormat(testcase.getName(), testcase));
					// }
				}
			}

		} else {
			importFormats = new ArrayList<ImportFormat>();
			selectedImportFormat = new ArrayList<ImportFormat>();
		}
	}

	// public void fillInDates() {
	// for (ImportFormat format : selectedImportFormat) {
	// // if (format.getCopyObjet()) {
	// if (this.sprint != null && mode.equalsIgnoreCase("planning"))
	// format.setStartDate(sprint.getStartDate());
	// else if (mode.equalsIgnoreCase("planning"))
	// format.setStartDate(new Date());
	// else
	// format.setStartDate(((JJSprintBean) LoginBean
	// .findBean("jJSprintBean")).getSprintUtil().getSprint()
	// .getStartDate());
	// // }
	// }
	// }

	// private boolean fillInDate;

	// public boolean isFillInDate() {
	//
	// fillInDate = true;
	// for (ImportFormat format : selectedImportFormat) {
	// //if (format.getCopyObjet()) {
	// fillInDate = false;
	// break;
	// //}
	// }
	// return fillInDate;
	// }
	//
	// public void setFillInDate(boolean fillInDate) {
	// this.fillInDate = fillInDate;
	// }

	public void importTask() {

		long startTime = System.nanoTime();
		JJContact c = jJProjectService
				.findJJProject(LoginBean.getProject().getId()).getManager();
		if (c == null)
			c = ((LoginBean) LoginBean.findBean("loginBean")).getContact();

		ContactCalendarUtil calendarUtil = new ContactCalendarUtil(
				c.getCompany());
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH");

		for (ImportFormat format : selectedImportFormat) {

			String name = null;
			if (mode == null)
				mode = "planning";

			JJTask task = new JJTask();
			task.setEnabled(true);
			task.setTaskType(format.getTaskType());
			if (mode.equalsIgnoreCase("planning"))
				task.setSprint(this.sprint);

			if (format.getStartDate() == null) {
				if (this.sprint != null && mode.equalsIgnoreCase("planning"))
					task.setStartDatePlanned(calendarUtil
							.nextWorkingDate(sprint.getStartDate()));
				else
					task.setStartDatePlanned(
							calendarUtil.nextWorkingDate(new Date()));

			} else {
				task.setStartDatePlanned(
						calendarUtil.nextWorkingDate(format.getStartDate()));
			}

			if (format.getWorkload() != null) {
				task.setWorkloadPlanned(format.getWorkload());

				if (task.getStartDatePlanned() != null)
					calendarUtil.getEndDate(task, Planned, jJTaskService);
			}

			if (format.getObject() instanceof JJRequirement) {

				JJRequirement requirement = (JJRequirement) format.getObject();

				name = requirement.getName() + " (" + df.format(new Date())
						+ "h)";

				task.setRequirement(requirement);

			} else if (format.getObject() instanceof JJBug) {

				JJBug bug = (JJBug) format.getObject();

				name = bug.getName() + " (" + df.format(new Date()) + "h)";
				task.setBug(bug);

			} else if (format.getObject() instanceof JJTestcase) {

				JJTestcase testcase = (JJTestcase) format.getObject();

				name = testcase.getName() + " (" + df.format(new Date()) + "h)";
				task.setTestcase(testcase);
			}

			if (name.length() > 100) {
				name = name.substring(0, 99);
			}

			task.setName(name);
			task.setDescription("This is task " + task.getName());
			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);

			if (mode.equalsIgnoreCase("scrum")) {

				JJSprintBean jJSprintBean = (JJSprintBean) session
						.getAttribute("jJSprintBean");
				task.setCreationDate(new Date());
				// exception name size
				task.setCreatedBy(((LoginBean) LoginBean.findBean("loginBean"))
						.getContact());
				JJStatus status = jJStatusService.getOneStatus("TODO", "Task",
						true);
				if (status != null)
					task.setStatus(status);
				task.setEnabled(true);
				task.setSprint(jJSprintBean.getSprintUtil().getSprint());

				if (format.getStartDate() == null) {
					task.setStartDatePlanned(jJSprintBean.getSprintUtil()
							.getSprint().getStartDate());
					if (task.getWorkloadPlanned() != null)
						calendarUtil.getEndDate(task, Planned, jJTaskService);
				}

			}

			if (task.getWorkloadPlanned() == null) {
				task.setWorkloadPlanned(3);
				calendarUtil.getEndDate(task, Planned, jJTaskService);
			}
			saveJJTask(task, false, new MutableInt(0));
			updateView(task, ADD_OPERATION);

		}

		if (mode.equalsIgnoreCase("planning")) {
			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);
			if (sprint != null
					&& session.getAttribute("jJSprintBean") != null) {

				sprint = jJSprintService.findJJSprint(sprint.getId());

				JJSprintBean jJSprintBean = (JJSprintBean) session
						.getAttribute("jJSprintBean");
				jJSprintBean.getSprintList().set(
						jJSprintBean.contains(sprint.getId()),
						new SprintUtil(sprint,
								jJTaskService.getSprintTasks(sprint,
										LoginBean.getProduct()),
								jJContactService, jJTaskService));

			}
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('taskImportDialogWidget').hide()");

			if (sprint != null)
				Hibernate.initialize(sprint.getContacts());

		} else if (mode.equalsIgnoreCase("scrum")) {

			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);
			JJSprintBean jJSprintBean = (JJSprintBean) session
					.getAttribute("jJSprintBean");

			if (!jJSprintBean.getSprintUtil().isRender()) {

				JJSprint sprint = jJSprintService.findJJSprint(
						jJSprintBean.getSprintUtil().getSprint().getId());
				SprintUtil sprintUtil = new SprintUtil(sprint,
						jJTaskService.getSprintTasks(sprint,
								LoginBean.getProduct()),
						jJContactService, jJTaskService);
				jJSprintBean.setSprintUtil(sprintUtil);
				jJSprintBean.getSprintList()
						.set(jJSprintBean.contains(sprint.getId()), sprintUtil);

			}

			// project = null;
			// tasksData = null;

			String message = "message_successfully_created";

			FacesMessage facesMessage = MessageFactory.getMessage(message,
					MessageFactory.getMessage("label_task", "").getDetail(),
					"e");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('taskImportDialogWidget').hide()");

		}
		long endTime = System.nanoTime();

		long duration = (endTime - startTime);
		System.err.println("duration :" + (double) duration / 1000000000.0);

	}

	public void closeDialogImport() {

		importCategory = null;
		importStatus = null;

		importCategoryList = null;
		importStatusList = null;

		objet = null;
		objets = null;

		importFormats = null;
		selectedImportFormat = null;
		mode = null;

	}

	public void handleSelectObjet() {

		importCategory = null;
		importStatus = null;

		getImportCategory();
		getImportStatus();

		disabledImportButton = true;
		// copyObjets = false;
		// oldCopyObjects = false;

		if (objet != null) {
			if (objet.equalsIgnoreCase("testcase")) {
				disabledFilter = true;
			} else {
				disabledFilter = false;
			}
			fillTableImport();
		} else {
			disabledFilter = true;
			importFormats = new ArrayList<ImportFormat>();
			selectedImportFormat = new ArrayList<ImportFormat>();

		}

	}

	public void handleSelectStatus() {
		getImportStatus();
		disabledImportButton = true;
		// copyObjets = false;
		// oldCopyObjects = false;

		if (objet != null) {
			fillTableImport();
		} else {
			importFormats = new ArrayList<ImportFormat>();
			selectedImportFormat = new ArrayList<ImportFormat>();

		}
	}

	public void handleSelectCategory() {
		getImportCategory();

		disabledImportButton = true;
		// copyObjets = false;
		// oldCopyObjects = false;

		if (objet != null) {
			fillTableImport();
		} else {
			importFormats = new ArrayList<ImportFormat>();
			selectedImportFormat = new ArrayList<ImportFormat>();

		}
	}

	public void sprintSelectionChanged(final AjaxBehaviorEvent event) {
		tasksData = null;
		model = null;
	}

	public void saveJJTask(JJTask ttt, boolean update,
			MutableInt updateObject) {

		if (update) {
			JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
					.getContact();
			ttt.setUpdatedBy(contact);
			ttt.setUpdatedDate(new Date());
			ttt = jJTaskService.updateJJTask(ttt);
		} else {

			ttt.setCreationDate(new Date());
			JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
					.getContact();
			ttt.setCreatedBy(contact);
			jJTaskService.saveJJTask(ttt);
			ttt = jJTaskService.findJJTask(ttt.getId());

		}
		if (updateObject.intValue() == 1) {
			JJRequirementBean.updateRowState(
					jJRequirementService
							.findJJRequirement(ttt.getRequirement().getId()),
					jJRequirementService, ttt);
		}

		JJStatusBean jJStatusBean = (JJStatusBean) LoginBean
				.findBean("jJStatusBean");
		if (jJStatusBean != null) {
			jJStatusBean.setKpiLineModel(null);
			jJStatusBean.setKpiBarModel(null);
		}

	}

	public void copyObjetsListener(ToggleSelectEvent event) {

		for (ImportFormat importFormat : importFormats) {

			if (event.isSelected() && importFormat.getStartDate() == null) {

				if (this.sprint != null && mode.equalsIgnoreCase("planning"))
					importFormat.setStartDate(sprint.getStartDate());
				else if (mode.equalsIgnoreCase("planning"))
					importFormat.setStartDate(new Date());
				else
					importFormat.setStartDate(
							((JJSprintBean) LoginBean.findBean("jJSprintBean"))
									.getSprintUtil().getSprint()
									.getStartDate());

			} else if (!event.isSelected()) {
				importFormat.setStartDate(null);
			}
		}
	}

	public void copyObjetListener(SelectEvent event) {

		ImportFormat importFormat = (ImportFormat) event.getObject();

		if (this.sprint != null && mode.equalsIgnoreCase("planning"))
			importFormat.setStartDate(sprint.getStartDate());
		else if (mode.equalsIgnoreCase("planning"))
			importFormat.setStartDate(new Date());
		else
			importFormat.setStartDate(
					((JJSprintBean) LoginBean.findBean("jJSprintBean"))
							.getSprintUtil().getSprint().getStartDate());
	}

	public void copyObjetListener(UnselectEvent event) {
		((ImportFormat) event.getObject()).setStartDate(null);

	}

	public class ImportFormat {

		private String name;
		private Object object;
		private Date startDate;
		private Integer workload;
		private JJStatus taskType;

		public ImportFormat() {
			super();
		}

		public ImportFormat(String name, Object object) {
			super();
			this.name = name;
			this.object = object;
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

		public JJStatus getTaskType() {
			return taskType;
		}

		public void setTaskType(JJStatus taskType) {
			this.taskType = taskType;
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

		@Override
		public boolean equals(Object object) {
			return (object instanceof ImportFormat) && (getObject() != null)
					? getObject().equals(((ImportFormat) object).getObject())
					: (object == this);
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
		private String icone;

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
			for (JJTask tt : taskList) {
				if (tt.getEnabled() && (tt.getChapter() != null)
						&& (task.getChapter() != null)
						&& tt.getChapter().equals(task.getChapter())) {
					storeTasks.add(tt);
				}
			}
			List<JJTask> list = new ArrayList<JJTask>();
			if (chapter != null) {

				// list.addAll(jJTaskService.getTasks(sprint, null,
				// LoginBean.getProduct(), null, null, null, chapter, true,
				// null, null, null, null, true, false, false,
				// "Requirement"));
				//
				// list.addAll(jJTaskService.getTasks(sprint, null,
				// LoginBean.getProduct(), null, null, null, chapter, true,
				// null, null, null, null, true, false, false,
				// "Testcase"));
				//
				// list.addAll(jJTaskService.getTasks(sprint, null,
				// LoginBean.getProduct(), null, null, null, chapter, true,
				// null, null, null, null, true, false, false, "Bug"));
			}

			list.remove(task);

			tasks = convertTaskListToStringList(list);
			this.icone = "";
			selectedTasks = convertTaskListToStringList(storeTasks);

			if (this.task.getRequirement() != null)
				this.icone = "function";
			else if (this.task.getBug() != null)
				this.icone = "bug";
			else if (this.task.getTestcase() != null)
				this.icone = "test";
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

		public String getIcone() {

			if (this.icone.isEmpty()) {
				icone = "";
				if (this.task.getRequirement() != null)
					this.icone = "function";
				else if (this.task.getBug() != null)
					this.icone = "bug";
				else if (this.task.getTestcase() != null)
					this.icone = "test";
			}
			return icone;
		}

		public void setIcone(String icone) {
			this.icone = icone;
		}

		private String splitString(String s, String regex, int index) {
			String[] result = s.split(regex);
			return result[index];
		}

		private List<String> convertTaskListToStringList(List<JJTask> tasks) {
			List<String> list = new ArrayList<String>();
			for (JJTask task : tasks) {
				String entry = task.getId() + "-" + task.getName().substring(0,
						Math.min(10, task.getName().length()));
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
					if (sprintBean.getProject()
							.equals(getJJTask_().getSprint().getProject())) {
						session.setAttribute("jJSprintBean",
								new JJSprintBean());
					}
				}
			}
		}

		setJJTask_(null);
		setCreateDialogVisible(false);
	}

	public void copyName(JJTask ttt) {
		StringSelection stringSelection = new StringSelection(
				this.getDialogHeader(ttt, null));
		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		clpbrd.setContents(stringSelection, null);
	}

	public void deleteTaskData() {

		RequestContext context = RequestContext.getCurrentInstance();
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJSprintBean jJSprintBean = (JJSprintBean) session
				.getAttribute("jJSprintBean");
		JJTask tJjTask = null;

		if (!mode.equalsIgnoreCase("scrum")) {

			tJjTask = jJTaskService
					.findJJTask(selectedTaskData.getTask().getId());
			tJjTask.setEnabled(false);
			saveJJTask(tJjTask, true, new MutableInt(0));
			updateView(tJjTask, DELETE_OPERATION);

			if (tJjTask.getSprint() != null && jJSprintBean
					.contains(tJjTask.getSprint().getId()) != -1) {
				SprintUtil s = SprintUtil.getSprintUtil(
						tJjTask.getSprint().getId(),
						jJSprintBean.getSprintList());
				if (s != null) {
					s = new SprintUtil(
							jJSprintService
									.findJJSprint(tJjTask.getSprint().getId()),
							jJTaskService.getSprintTasks(
									jJSprintService.findJJSprint(
											tJjTask.getSprint().getId()),
									LoginBean.getProduct()),
							jJContactService, jJTaskService);

					// sprintUtil.setRenderTaskForm(false);
					jJSprintBean.getSprintList().set(
							jJSprintBean.contains(s.getSprint().getId()), s);
				}
			}
			FacesMessage facesMessage = MessageFactory.getMessage(
					"message_successfully_deleted",
					MessageFactory.getMessage("label_task", "").getDetail(),
					"e");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
			context.execute("PF('deleteDialogWidget').hide()");
		} else {

			tJjTask = jJSprintBean.getTask();
			jJSprintBean.deleteTask();
			updateView(jJTaskService.findJJTask(tJjTask.getId()),
					DELETE_OPERATION);

		}

	}

	public TaskData getSelectedTaskData() {
		return selectedTaskData;
	}

	public void setSelectedTaskData(TaskData selectedTaskData) {
		this.selectedTaskData = selectedTaskData;
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

					// System.out.println("TreeNode "
					// + task.getRequirement().getName());

					taskTreeNode = new DefaultTreeNode("Requirement :", null);
					DefaultTreeNode tree = new DefaultTreeNode(
							task.getRequirement(), taskTreeNode);

					// System.out.println("TreeNode "
					// + task.getRequirement().getName());

					if (task.getRequirement()
							.getRequirementLinkDown() != null) {

						// System.out.println("TreeNode Before "
						// + task.getRequirement().getName());
						reqTreeNode(tree, task.getRequirement());

						// System.out.println("TreeNode after "
						// + task.getRequirement().getName());
					}

					selectedTree = taskTreeNode;
					selectedReq = task.getRequirement();
					// System.out.println("TreeNode Last "
					// + task.getRequirement().getName());
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
		context.execute("PF('viewTaskDialogWidget').show()");

	}

	public StreamedContent getFile() {

		DateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat f2 = new SimpleDateFormat("hh:mm:ss");

		Date startDateDay = LoginBean.getProject().getStartDate();
		Date endDateDay = LoginBean.getProject().getEndDate();

		if (startDateDay == null)
			startDateDay = new Date();
		if (endDateDay == null)
			endDateDay = new Date();

		String buffer = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
				+ System.getProperty("line.separator")
				+ "<Project xmlns=\"http://schemas.microsoft.com/project\">"
				+ System.getProperty("line.separator")
				+ "<SaveVersion>9</SaveVersion>"
				+ System.getProperty("line.separator") + "<Title>"
				+ LoginBean.getProject().getName() + "</Title>"
				+ System.getProperty("line.separator")
				+ "<ScheduleFromStart>1</ScheduleFromStart>"
				+ System.getProperty("line.separator") + "<StartDate>"
				+ f1.format(startDateDay) + "T" + f2.format(startDateDay)
				+ "</StartDate>" + System.getProperty("line.separator")
				+ " <FinishDate>" + System.getProperty("line.separator")
				+ f1.format(endDateDay) + "T" + f2.format(endDateDay)
				+ "</FinishDate>" + System.getProperty("line.separator")
				+ "<DefaultStartTime>09:00:00</DefaultStartTime>"
				+ System.getProperty("line.separator")
				+ "<MinutesPerDay>480</MinutesPerDay>"
				+ System.getProperty("line.separator")
				+ "<MinutesPerWeek>2400</MinutesPerWeek>"
				+ System.getProperty("line.separator")
				+ "<DaysPerMonth>20</DaysPerMonth>"
				+ System.getProperty("line.separator")
				+ "<DurationFormat>7</DurationFormat>"
				+ System.getProperty("line.separator")
				+ "<WorkFormat>2</WorkFormat>"
				+ System.getProperty("line.separator")
				+ "<WeekStartDay>1</WeekStartDay>"
				+ System.getProperty("line.separator") + "<CurrentDate>"
				+ f1.format(new Date()) + "T" + f2.format(new Date())
				+ "</CurrentDate>" + System.getProperty("line.separator")
				+ "<Tasks>";
		for (TaskData tt : tasksData) {

			buffer = buffer + System.getProperty("line.separator") + "<Task>"
					+ "<UID>1</UID>" + System.getProperty("line.separator")
					+ "<ID>1</ID>" + System.getProperty("line.separator")
					+ "<Name>" + tt.getTask().getName() + "</Name>"
					+ System.getProperty("line.separator") + "<Type>0</Type>"
					+ System.getProperty("line.separator")
					+ "<IsNull>0</IsNull>"
					+ System.getProperty("line.separator") + "<WBS>1</WBS>"
					+ System.getProperty("line.separator")
					+ "<OutlineNumber>1</OutlineNumber>"
					+ System.getProperty("line.separator")
					+ "<OutlineLevel>1</OutlineLevel>"
					+ System.getProperty("line.separator")
					+ "<Priority>500</Priority>"
					+ System.getProperty("line.separator") + "<Start>"
					+ f1.format(tt.getTask().getStartDatePlanned()) + "T"
					+ f2.format(tt.getTask().getStartDatePlanned()) + "</Start>"
					+ System.getProperty("line.separator") + "<Finish>"
					+ f1.format(tt.getTask().getEndDatePlanned()) + "T"
					+ f2.format(tt.getTask().getEndDatePlanned()) + "</Finish>"
					+ System.getProperty("line.separator")
					+ "<DurationFormat>7</DurationFormat>"
					+ System.getProperty("line.separator") + "</Task>";

		}
		buffer = buffer + System.getProperty("line.separator") + "</Tasks>"
				+ System.getProperty("line.separator") + "<Resources/>"
				+ System.getProperty("line.separator") + "<Assignments/>"
				+ System.getProperty("line.separator") + "</Project>";
		InputStream stream = new ByteArrayInputStream(buffer.getBytes());
		return new DefaultStreamedContent(stream, "xml",
				LoginBean.getProject().getName().toUpperCase()
						+ "-Planning.xml");
	}

	// Timeline operation
	public int findInEventTimeLine(List<TimelineEvent> events, JJTask t,
			boolean real) {

		int j = -1;
		int i = 0;
		while (i < events.size()) {

			// System.out
			// .println(events.get(i).getStyleClass()
			// + ":"
			// + !(events.get(i).getStyleClass()
			// .equalsIgnoreCase(Real) ^ real) + ":"
			// + real);

			if (events.get(i).getData() instanceof JJTask
					&& !(events.get(i).getStyleClass().equalsIgnoreCase(Real)
							^ real)
					&& !events.get(i).getStyleClass()
							.equalsIgnoreCase("invisible")) {
				JJTask tt = (JJTask) events.get(i).getData();
				if (t.equals(tt)) {

					j = i;
					i = events.size();

				}
			}
			i++;

		}
		return j;
	}

	public void updateChapterTimeLineEvent(JJChapter chapter) {

		List<TimelineEvent> events = model.getEvents();

		// model.updateAll(model.getEvents(),
		// TimelineUpdater.getCurrentInstance(":projecttabview:planningForm:timeline"));
		List<JJTask> listTasks = new ArrayList<JJTask>();

		int i = 0;

		while (i < tasksData.size()) {
			if (tasksData.get(i).getChapter().equals(chapter))
				listTasks.add(tasksData.get(i).getTask());
			i++;
		}

		Map<Date, String> min = new TreeMap<Date, String>();
		Map<Date, String> max = new TreeMap<Date, String>();

		for (JJTask t : listTasks) {

			if (t.getStartDateRevised() != null) {
				min.put(t.getStartDateRevised(),
						t.getStartDateRevised().toString());
			} else if (t.getStartDatePlanned() != null) {
				min.put(t.getStartDatePlanned(),
						t.getStartDatePlanned().toString());
			}

			if (t.getStartDateReal() != null) {
				min.put(t.getStartDateReal(), t.getStartDateReal().toString());
			}

			if (t.getEndDateRevised() != null) {
				max.put(t.getEndDateRevised(),
						t.getEndDateRevised().toString());
			} else if (t.getEndDatePlanned() != null) {
				max.put(t.getEndDatePlanned(),
						t.getEndDatePlanned().toString());
			}

			if (t.getEndDateReal() != null) {
				max.put(t.getEndDateReal(), t.getEndDateReal().toString());
			}
		}

		i = 0;
		int j = -1;
		String group = "";
		while (i < events.size()) {
			if (events.get(i).getStyleClass().equalsIgnoreCase("chapter")) {

				if (((JJChapter) events.get(i).getData()).equals(chapter)) {
					j = i;
					group = events.get(j).getGroup();
					i = events.size();

				}
			}
			i++;
		}

		if (j != -1) {

			// TimelineUpdater timelineUpdater =
			// TimelineUpdater.getCurrentInstance(":projecttabview:planningForm:timeline");
			events.remove(j);
			// RequestContext.getCurrentInstance().execute("PF('timelineWdgt').deleteEvent("+j+")");
			if (!min.isEmpty() && !max.isEmpty() && !listTasks.isEmpty()) {
				Date end = null;
				Set<Date> dates = min.keySet();

				Date start = dates.iterator().next();

				dates = max.keySet();
				for (Date date : dates) {
					end = date;
				}

				TimelineEvent event = new TimelineEvent(chapter, start, end,
						false, group, "chapter");

				events.add(event);

				// this.start = start;
				// this.end = end;

			} else {
				int j1 = -1;
				int i1 = 0;
				while (i1 < events.size()) {

					if (events.get(i1).getData() instanceof JJChapter
							&& (events.get(i1).getStyleClass()
									.equalsIgnoreCase("invisible"))) {

						if (chapter.equals((JJChapter) model.getEvents().get(i1)
								.getData())) {
							j1 = i1;
							i1 = events.size();
						}
					}
					i1++;

				}
				if (j1 != -1)
					events.remove(j1);
				// model.addAll(events,TimelineUpdater.getCurrentInstance(":projecttabview:planningForm:timeline"));
			}
			model.setEvents(events);

		}

	}

	public void onDeleteTimelineEvent(TimelineModificationEvent e) {
		// get clone of the TimelineEvent to be deleted

		selectedTaskData = tasksData.get(containTaskData(
				((JJTask) e.getTimelineEvent().getData()).getId()));
		mode = "planning";
	}

	public void onCreateTimelineEvent(TimelineAddEvent ev) {

		Calendar cal = Calendar.getInstance();

		cal.setTime(ev.getStartDate());
		cal.add(Calendar.MONTH, -1);
		this.start = cal.getTime();

		cal.setTime(ev.getEndDate());
		cal.add(Calendar.MONTH, 1);
		this.end = cal.getTime();

		this.mode = "planning";
		disabledImportButton = true;
		disabledFilter = true;
		checkAll = false;

		getProject();
		getProduct();
		getVersion();

		importFormats = new ArrayList<ImportFormat>();
		selectedImportFormat = new ArrayList<ImportFormat>();
	}

	public void onEditTimelineEvent(TimelineModificationEvent ev) {

		Calendar cal = Calendar.getInstance();

		cal.setTime(ev.getTimelineEvent().getStartDate());
		cal.add(Calendar.MONTH, -1);
		this.start = cal.getTime();

		cal.setTime(ev.getTimelineEvent().getEndDate());
		cal.add(Calendar.MONTH, 1);
		this.end = cal.getTime();

		this.task = (JJTask) ev.getTimelineEvent().getData();
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

		// this.start = ev.getTimelineEvent().getStartDate();
		// this.end = ev.getTimelineEvent().getEndDate();

		// ev.getComponent().

		ContactCalendarUtil calendarUtil;

		if (tt.getAssignedTo() != null) {
			calendarUtil = new ContactCalendarUtil(tt.getAssignedTo());

		} else {

			calendarUtil = new ContactCalendarUtil(jJProjectService
					.findJJProject(LoginBean.getProject().getId()).getManager()
					.getCompany());

		}

		if (group.equalsIgnoreCase(Real)) {

			tt.setStartDateReal(calendarUtil
					.nextWorkingDate(ev.getTimelineEvent().getStartDate()));
			tt.setEndDateReal(calendarUtil
					.nextWorkingDate(ev.getTimelineEvent().getEndDate()));

			tt.setWorkloadReal(Math
					.round(calendarUtil.calculateWorkLoad(tt.getStartDateReal(),
							tt.getEndDateReal(), jJTaskService, tt)));

			saveJJTask(tt, true, new MutableInt(0));
			// tt = jJTaskService.findJJTask(tt.getId());

			Calendar cal = Calendar.getInstance();

			cal.setTime(tt.getStartDateReal());
			cal.add(Calendar.MONTH, -1);
			this.start = cal.getTime();

			cal.setTime(tt.getEndDateReal());
			cal.add(Calendar.MONTH, 1);
			this.end = cal.getTime();

			updateView(tt, UPDATE_OPERATION);

		} else if (group.toLowerCase().contains(Planned.toLowerCase())
				|| group.toLowerCase().contains(Revised.toLowerCase())) {

			tt.setStartDateRevised(calendarUtil
					.nextWorkingDate(ev.getTimelineEvent().getStartDate()));
			tt.setEndDateRevised(calendarUtil
					.nextWorkingDate(ev.getTimelineEvent().getEndDate()));

			tt.setWorkloadRevised(Math.round(
					calendarUtil.calculateWorkLoad(tt.getStartDateRevised(),
							tt.getEndDateRevised(), null, null)));
			saveJJTask(tt, true, new MutableInt(0));
			// tt = jJTaskService.findJJTask(tt.getId());

			Calendar cal = Calendar.getInstance();

			cal.setTime(tt.getStartDateRevised());
			cal.add(Calendar.MONTH, -1);
			this.start = cal.getTime();

			cal.setTime(tt.getEndDateRevised());
			cal.add(Calendar.MONTH, 1);
			this.end = cal.getTime();

			updateView(tt, UPDATE_OPERATION);

		} else {
			tt = null;
		}

		if (tt != null && tt.getSprint() != null) {

			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);
			JJSprintBean jJSprintBean = (JJSprintBean) session
					.getAttribute("jJSprintBean");
			if (jJSprintBean != null
					&& jJSprintBean.contains(tt.getSprint().getId()) != -1) {
				SprintUtil s = SprintUtil.getSprintUtil(tt.getSprint().getId(),
						jJSprintBean.getSprintList());
				if (s != null) {
					s = new SprintUtil(
							jJSprintService
									.findJJSprint(tt.getSprint().getId()),
							jJTaskService.getSprintTasks(
									jJSprintService.findJJSprint(
											tt.getSprint().getId()),
									LoginBean.getProduct()),
							jJContactService, jJTaskService);
					jJSprintBean.getSprintList().set(
							jJSprintBean.contains(s.getSprint().getId()), s);
				}
			}

		}

		reset();
		if (tt != null) {

			FacesMessage facesMessage = MessageFactory.getMessage(
					"message_successfully_updated",
					MessageFactory.getMessage("label_task", "").getDetail(),
					"e");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}

	}

	public void onSprintUpdate(JJSprint sprint) {

		for (JJTask tt : jJTaskService.getSprintTasks(
				jJSprintService.findJJSprint(sprint.getId()),
				LoginBean.getProduct())) {

			if (containTaskData(tt.getId()) != -1)
				updateView(tt, UPDATE_OPERATION);
		}

	}

	public JJChapter replaceTaskData(JJTask tt, String operation) {

		int i = containTaskData(tt.getId());
		JJChapter chapter = null;
		if (i != -1) {
			chapter = tasksData.get(i).getChapter();
			if (!operation.equalsIgnoreCase(DELETE_OPERATION)) {
				Date startDate = null, endDate = null;
				int workLoad = 0;

				if (tt.getStartDateRevised() != null) {
					startDate = tt.getStartDateRevised();

					if (tt.getEndDateRevised() != null)
						endDate = tt.getEndDateRevised();
					else
						endDate = tt.getEndDatePlanned();

					if (tt.getWorkloadRevised() == null) {
						if (tt.getWorkloadPlanned() != null)
							workLoad = tt.getWorkloadPlanned();
					} else
						workLoad = tt.getWorkloadRevised();
				} else {
					startDate = tt.getStartDatePlanned();
					endDate = tt.getEndDatePlanned();
					if (tt.getWorkloadPlanned() != null)
						workLoad = tt.getWorkloadPlanned();
				}

				TaskData tskst = new TaskData(tt, tasksData.get(i).getChapter(),
						startDate, endDate, workLoad,
						tt.getStartDateRevised() != null);

				tasksData.set(i, tskst);
				allTasks.set(allTasks.indexOf(tt), tt);
			} else {
				tasksData.remove(i);
				allTasks.remove(tt);
			}

		} else if (operation.equalsIgnoreCase(ADD_OPERATION)
				&& tasksData != null) {
			if (sortMode == null || sortMode.equalsIgnoreCase("chapter")) {
				if (tt.getChapter() != null && (this.sprint == null
						|| (this.sprint.equals(tt.getSprint())))) {
					Date startDate = null, endDate = null;
					int workLoad = 0;

					if (tt.getStartDateRevised() != null) {
						startDate = tt.getStartDateRevised();

						if (tt.getEndDateRevised() != null)
							endDate = tt.getEndDateRevised();
						else
							endDate = tt.getEndDatePlanned();

						if (tt.getWorkloadRevised() == null) {
							if (tt.getWorkloadPlanned() != null)
								workLoad = tt.getWorkloadPlanned();
						} else
							workLoad = tt.getWorkloadRevised();
					} else {
						startDate = tt.getStartDatePlanned();
						endDate = tt.getEndDatePlanned();
						if (tt.getWorkloadPlanned() != null)
							workLoad = tt.getWorkloadPlanned();
					}

					TaskData tskst = new TaskData(tt, tt.getChapter(),
							startDate, endDate, workLoad,
							tt.getStartDateRevised() != null);
					tasksData.add(tskst);
					// allTasks.add(tt);
					chapter = tt.getChapter();
				}
			} else if (this.sprint == null
					|| (this.sprint.equals(tt.getSprint()))) {
				Date startDate = null, endDate = null;
				int workLoad = 0;

				if (tt.getStartDateRevised() != null) {
					startDate = tt.getStartDateRevised();

					if (tt.getEndDateRevised() != null)
						endDate = tt.getEndDateRevised();
					else
						endDate = tt.getEndDatePlanned();

					if (tt.getWorkloadRevised() == null) {
						if (tt.getWorkloadPlanned() != null)
							workLoad = tt.getWorkloadPlanned();
					} else
						workLoad = tt.getWorkloadRevised();
				} else {
					startDate = tt.getStartDatePlanned();
					endDate = tt.getEndDatePlanned();
					if (tt.getWorkloadPlanned() != null)
						workLoad = tt.getWorkloadPlanned();
				}

				TaskData tskst = new TaskData(tt, tt.getChapter(), startDate,
						endDate, workLoad, tt.getStartDateRevised() != null);
				tasksData.add(tskst);

			}
			allTasks.add(tt);
		}
		return chapter;

	}

	public void replaceRealTimelineEvent(List<TimelineEvent> events, JJTask tt,
			String operation) {

		int i = findInEventTimeLine(events, tt, true);
		if (i != -1) {

			String group = events.get(i).getGroup();
			events.remove(i);
			if (!operation.equalsIgnoreCase(DELETE_OPERATION)) {

				Date startDate = null, endDate = null;

				if (tt.getStartDateReal() != null) {

					startDate = tt.getStartDateReal();

					if (tt.getEndDateReal() != null)
						endDate = tt.getEndDateReal();
					else
						endDate = tt.getEndDatePlanned();
				}

				if (startDate != null)
					events.add(new TimelineEvent(tt, startDate, endDate, true,
							group, Real));

			}
		} else if (!operation.equalsIgnoreCase(DELETE_OPERATION)
				&& tt.getStartDateReal() != null) {

			Date startDate = null, endDate = null;

			startDate = tt.getStartDateReal();

			if (tt.getEndDateReal() != null)
				endDate = tt.getEndDateReal();
			else
				endDate = tt.getEndDatePlanned();
			int j = findInEventTimeLine(events, tt, false);
			if (j != -1) {
				String group = events.get(j).getGroup();
				events.add(new TimelineEvent(tt, startDate, endDate, true,
						group, Real));
			}

		}

	}

	public void updateView(JJTask tt, String operation) {

		JJChapter chapter = replaceTaskData(tt, operation);

		if (model != null && !operation.equalsIgnoreCase(ADD_OPERATION)) {
			List<TimelineEvent> events = model.getEvents();
			replaceRealTimelineEvent(events, tt, operation);

			if (operation.equalsIgnoreCase(DELETE_OPERATION)) {
				int j1 = -1;
				int i1 = 0;
				while (i1 < events.size()) {

					if (events.get(i1).getData() instanceof JJTask
							&& (events.get(i1).getStyleClass()
									.equalsIgnoreCase("invisible"))) {

						if (tt.equals((JJTask) events.get(i1).getData())) {
							j1 = i1;
							i1 = events.size();

						}
					}
					i1++;

				}

				if (j1 != -1)
					events.remove(j1);
			}

			int j = findInEventTimeLine(events, tt, false);
			String styleClass = "";
			Date startDate = null, endDate = null;

			if (!operation.equalsIgnoreCase(DELETE_OPERATION) && j != -1) {
				if (tt.getStartDateRevised() != null) {
					if (tt.getStartDateReal() == null)
						styleClass = "revised1";
					else
						styleClass = "revised2";

					startDate = tt.getStartDateRevised();
					if (tt.getEndDateRevised() == null)
						endDate = tt.getEndDatePlanned();
					else
						endDate = tt.getEndDateRevised();

				} else {

					if (tt.getStartDateReal() == null)
						styleClass = "planned1";
					else
						styleClass = "planned2";

					endDate = tt.getEndDatePlanned();
					startDate = tt.getStartDatePlanned();

				}
				String group = events.get(j).getGroup();
				events.remove(j);
				events.add(new TimelineEvent(tt, startDate, endDate, true,
						group, styleClass));

			} else if (j != -1) {
				events.remove(j);
			}

			model.setEvents(events);
			if (sortMode.equalsIgnoreCase("chapter") && chapter != null)
				updateChapterTimeLineEvent(chapter);

		} else if (model != null && operation.equalsIgnoreCase(ADD_OPERATION)
				&& (sprint == null || (sprint.equals(tt.getSprint())))) {
			String group = null;
			List<TimelineEvent> events = model.getEvents();
			/* Get Chapter Group */
			if (sortMode.equalsIgnoreCase("chapter")
					&& tt.getChapter() != null) {
				int i = 0;
				int j = -1;
				while (i < events.size()) {
					if (events.get(i).getStyleClass()
							.equalsIgnoreCase("chapter")) {

						if (((JJChapter) events.get(i).getData())
								.equals(chapter)) {
							j = i;
							group = events.get(j).getGroup();
							i = events.size();

						}
					}
					i++;
				}
				int k = 0;
				if (group == null) {

					k = 65;
					if (!events.isEmpty()) {
						group = events.get(events.size() - 1).getGroup();
						k = (int) group.trim().charAt(25);
						k++;
					}

				} else {
					i = 0;
					k = (int) group.trim().charAt(25);
					while (i < events.size()) {

						TimelineEvent event = events.get(i);
						int kd = (int) event.getGroup().trim().charAt(25);
						if (kd >= k) {
							kd++;
							if (kd == 92)
								kd++;

							char c = (char) kd;
							event.setGroup("<span style=display:none>" + c
									+ "</span>");
							events.set(i, event);
						}

						i++;

					}
				}

				Map<Date, String> min = new TreeMap<Date, String>();
				Map<Date, String> max = new TreeMap<Date, String>();

				if (k == 92)
					k++;

				char c = (char) k;
				group = "<span style=display:none>" + c + "</span>";
				new GregorianCalendar(2010, 1, 1).getTime();
				events.add(new TimelineEvent(tt,
						new GregorianCalendar(
								Calendar.getInstance().get(Calendar.YEAR) - 5,
								1, 1)
										.getTime(),
						new GregorianCalendar(
								Calendar.getInstance().get(Calendar.YEAR) + 5,
								1, 1).getTime(),
						false, group, "invisible"));

				if (tt.getStartDateReal() != null) {

					Date endDate;
					if (tt.getEndDateReal() == null)
						endDate = tt.getEndDatePlanned();
					else
						endDate = tt.getEndDateReal();

					TimelineEvent event = new TimelineEvent(tt,
							tt.getStartDateReal(), endDate, true, group, Real);

					events.add(event);

				}
				if (tt.getStartDateRevised() != null) {

					Date endDate;
					String styleClass;

					if (tt.getStartDateReal() == null)
						styleClass = "revised1";
					else
						styleClass = "revised2";

					if (tt.getEndDateRevised() == null)
						endDate = tt.getEndDatePlanned();
					else
						endDate = tt.getEndDateRevised();

					TimelineEvent event = new TimelineEvent(tt,
							tt.getStartDateRevised(), endDate, true, group,
							styleClass);
					events.add(event);
				} else {

					String style = null;
					if (tt.getStartDateReal() == null)
						style = "planned1";
					else
						style = "planned2";
					TimelineEvent event = new TimelineEvent(tt,
							tt.getStartDatePlanned(), tt.getEndDatePlanned(),
							true, group, style);
					events.add(event);

				}

				if (tt.getStartDatePlanned() != null) {
					min.put(tt.getStartDatePlanned(),
							tt.getStartDatePlanned().toString());
				}

				if (tt.getStartDateRevised() != null) {
					min.put(tt.getStartDateRevised(),
							tt.getStartDateRevised().toString());
				}

				if (tt.getStartDateReal() != null) {
					min.put(tt.getStartDateReal(),
							tt.getStartDateReal().toString());
				}

				if (tt.getEndDatePlanned() != null) {
					max.put(tt.getEndDatePlanned(),
							tt.getEndDatePlanned().toString());
				}

				if (tt.getEndDateRevised() != null) {
					max.put(tt.getEndDateRevised(),
							tt.getEndDateRevised().toString());
				}

				if (tt.getEndDateReal() != null) {
					max.put(tt.getEndDateReal(),
							tt.getEndDateReal().toString());
				}

				k++;

				if (!min.isEmpty() && !max.isEmpty() && j == -1) {

					if (k == 92)
						k++;

					Date end = null;
					Set<Date> dates = min.keySet();

					Date start = dates.iterator().next();

					dates = max.keySet();
					for (Date date : dates) {
						end = date;
					}
					c = (char) k;
					group = "<span style=display:none>" + c + "</span>";
					// + task.getName();
					TimelineEvent event = new TimelineEvent(chapter, start, end,
							false, group, "chapter");
					events.add(
							new TimelineEvent(chapter,
									new GregorianCalendar(
											Calendar.getInstance()
													.get(Calendar.YEAR) - 5,
											1, 1).getTime(),
									new GregorianCalendar(
											Calendar.getInstance()
													.get(Calendar.YEAR) + 5,
											1, 1).getTime(),
									false, group, "invisible"));

					events.add(event);

				}
				updateChapterTimeLineEvent(tt.getChapter());

			} else if (!sortMode.equalsIgnoreCase("chapter")) {
				int k = 65;
				if (!events.isEmpty()) {
					group = events.get(events.size() - 1).getGroup();
					k = (int) group.trim().charAt(25);
					k++;
					if (k == 92)
						k++;
				}

				char c = (char) k;
				group = "<span style=display:none>" + c + "</span>";

				events.add(new TimelineEvent(tt,
						new GregorianCalendar(
								Calendar.getInstance().get(Calendar.YEAR) - 5,
								1, 1)
										.getTime(),
						new GregorianCalendar(
								Calendar.getInstance().get(Calendar.YEAR) + 5,
								1, 1).getTime(),
						false, group, "invisible"));

				if (tt.getStartDateReal() != null) {

					Date endDate;
					if (tt.getEndDateReal() == null)
						endDate = tt.getEndDatePlanned();
					else
						endDate = tt.getEndDateReal();

					TimelineEvent event = new TimelineEvent(tt,
							tt.getStartDateReal(), endDate, true, group, Real);

					events.add(event);

				}
				if (tt.getStartDateRevised() != null) {

					Date endDate;
					if (tt.getEndDateRevised() == null)
						endDate = tt.getEndDatePlanned();
					else
						endDate = tt.getEndDateRevised();

					String styleClass;

					if (tt.getStartDateReal() == null)
						styleClass = "revised1";
					else
						styleClass = "revised2";

					TimelineEvent event = new TimelineEvent(tt,
							tt.getStartDateRevised(), endDate, true, group,
							styleClass);
					events.add(event);

				} else {

					String style = null;
					if (tt.getStartDateReal() == null)
						style = "planned1";
					else
						style = "planned2";

					TimelineEvent event = new TimelineEvent(tt,
							tt.getStartDatePlanned(), tt.getEndDatePlanned(),
							true, group, style);
					events.add(event);
				}
			}

			model.setEvents(events);
		}

	}

	public void SortBySelectionChanged(final AjaxBehaviorEvent event) {
		// System.err.println("sortMode " + sortMode);
		tasksData = null;
		model = null;
	}

	public int sortFunction(Object taskData1, Object taskData2) {

		if (sortMode.equalsIgnoreCase("chapter")) {

			return ((JJChapter) taskData1).getCreationDate()
					.compareTo(((JJChapter) taskData2).getCreationDate());

		} else
			return 1;

	}

	// ToDoTask Layout
	public void initToDoTasks(ComponentSystemEvent e) {

		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();

		if (contact != null)
			toDoTasks = jJTaskService.getToDoTasks(contact);

	}

	// shedule imputaion
	private ScheduleModel lazyEventModel;
	private Date scheduleInitialDate;
	private String scheduleInitialView;
	private int activeTabTeamIndex;

	public ScheduleModel getLazyEventModel() {
		if (lazyEventModel == null)
			lazyEventModel = new LazyScheduleModel() {

				private static final long serialVersionUID = 1L;

				@Override
				public void loadEvents(Date start, Date end) {
					HttpSession session = (HttpSession) FacesContext
							.getCurrentInstance().getExternalContext()
							.getSession(false);
					LoginBean loginBean = (LoginBean) session
							.getAttribute("loginBean");
					List<JJTask> tasks = jJTaskService.getTasks(
							LoginBean.getProject(), LoginBean.getProduct(),
							loginBean.getContact(), start, end);

					for (JJTask t : tasks) {
						Date s, e;

						Calendar calendar = Calendar.getInstance();

						calendar.setTime(t.getStartDateReal());
						calendar.set(Calendar.SECOND, 0);
						calendar.set(Calendar.MILLISECOND, 0);

						s = calendar.getTime();

						calendar = Calendar.getInstance();

						calendar.setTime(t.getEndDateReal() != null
								? t.getEndDateReal()
								: end);
						calendar.set(Calendar.SECOND, 0);
						calendar.set(Calendar.MILLISECOND, 0);

						e = calendar.getTime();

						DefaultScheduleEvent ev = new DefaultScheduleEvent(
								t.getName(), s, e, t);
						ev.setDescription(getDialogHeader(t, null));
						addEvent(ev);
					}
					scheduleInitialDate = new Date(
							(end.getTime() + start.getTime()) / 2);
				}
			};
		return lazyEventModel;
	}

	public void setLazyEventModel(ScheduleModel lazyEventModel) {
		this.lazyEventModel = lazyEventModel;
	}

	public Date getScheduleInitialDate() {
		return scheduleInitialDate;
	}

	public void setScheduleInitialDate(Date scheduleInitialDate) {
		this.scheduleInitialDate = scheduleInitialDate;
	}

	public String getScheduleInitialView() {

		if (scheduleInitialView == null)
			scheduleInitialView = "month";
		return scheduleInitialView;
	}

	public void setScheduleInitialView(String scheduleInitialView) {
		this.scheduleInitialView = scheduleInitialView;
	}

	public int getActiveTabTeamIndex() {
		return activeTabTeamIndex;
	}

	public void setActiveTabTeamIndex(int activeTabTeamIndex) {
		this.activeTabTeamIndex = activeTabTeamIndex;
	}

	public void onEventSelect(SelectEvent selectEvent) {
		task = (JJTask) ((ScheduleEvent) selectEvent.getObject()).getData();
		scheduleInitialDate = task.getStartDateReal();
		task = jJTaskService.findJJTask(task.getId());
		taskTreeNode = null;
		selectedReq = null;
		selectedTree = null;
		initiateReqTreeNode();
	}

	public void onViewChange(SelectEvent selectEvent) {

		scheduleInitialDate = (Date) ((Schedule) selectEvent.getComponent())
				.getInitialDate();
		scheduleInitialView = ((Schedule) selectEvent.getComponent()).getView();
	}

	public void onEventMove(ScheduleEntryMoveEvent event) {

		JJTask tt = (JJTask) event.getScheduleEvent().getData();
		tt = jJTaskService.findJJTask(tt.getId());

		tt.setStartDateReal(event.getScheduleEvent().getStartDate());
		tt.setEndDateReal(event.getScheduleEvent().getEndDate());
		saveJJTask(tt, true, new MutableInt(0));
		// tt = jJTaskService.findJJTask(tt.getId());
		updateView(tt, UPDATE_OPERATION);

		if (tt.getSprint() != null) {

			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);
			JJSprintBean jJSprintBean = (JJSprintBean) session
					.getAttribute("jJSprintBean");
			if (jJSprintBean != null
					&& jJSprintBean.contains(tt.getSprint().getId()) != -1) {
				SprintUtil s = SprintUtil.getSprintUtil(tt.getSprint().getId(),
						jJSprintBean.getSprintList());
				if (s != null) {
					s = new SprintUtil(
							jJSprintService
									.findJJSprint(tt.getSprint().getId()),
							jJTaskService.getSprintTasks(
									jJSprintService.findJJSprint(
											tt.getSprint().getId()),
									LoginBean.getProduct()),
							jJContactService, jJTaskService);
					jJSprintBean.getSprintList().set(
							jJSprintBean.contains(s.getSprint().getId()), s);
				}
			}

		}

		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_updated",
				MessageFactory.getMessage("label_task", "").getDetail(), "e");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}

	public void onEventResize(ScheduleEntryResizeEvent event) {

		JJTask tt = (JJTask) event.getScheduleEvent().getData();
		tt = jJTaskService.findJJTask(tt.getId());

		tt.setStartDateReal(event.getScheduleEvent().getStartDate());
		tt.setEndDateReal(event.getScheduleEvent().getEndDate());
		saveJJTask(tt, true, new MutableInt(0));
		// tt = jJTaskService.findJJTask(tt.getId());
		updateView(tt, UPDATE_OPERATION);

		if (tt.getSprint() != null) {

			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);
			JJSprintBean jJSprintBean = (JJSprintBean) session
					.getAttribute("jJSprintBean");
			if (jJSprintBean != null
					&& jJSprintBean.contains(tt.getSprint().getId()) != -1) {
				SprintUtil s = SprintUtil.getSprintUtil(tt.getSprint().getId(),
						jJSprintBean.getSprintList());
				if (s != null) {
					s = new SprintUtil(
							jJSprintService
									.findJJSprint(tt.getSprint().getId()),
							jJTaskService.getSprintTasks(
									jJSprintService.findJJSprint(
											tt.getSprint().getId()),
									LoginBean.getProduct()),
							jJContactService, jJTaskService);
					jJSprintBean.getSprintList().set(
							jJSprintBean.contains(s.getSprint().getId()), s);
				}
			}

		}
		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_updated",
				MessageFactory.getMessage("label_task", "").getDetail(), "e");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}

	private ScheduleModel lazyEventModelAll;
	private Date scheduleInitialDateAll;
	private String scheduleInitialViewAll;

	public ScheduleModel getLazyEventModelAll() {
		if (lazyEventModelAll == null)
			lazyEventModelAll = new LazyScheduleModel() {

				private static final long serialVersionUID = 1L;

				@Override
				public void loadEvents(Date start, Date end) {

					List<JJTask> tasks = jJTaskService.getTasks(
							LoginBean.getProject(), LoginBean.getProduct(),
							null, start, end);
					HttpSession session = (HttpSession) FacesContext
							.getCurrentInstance().getExternalContext()
							.getSession(false);
					LoginBean loginBean = (LoginBean) session
							.getAttribute("loginBean");

					for (JJTask t : tasks) {
						Date s, e;

						Calendar calendar = Calendar.getInstance();

						calendar.setTime(t.getStartDateReal());
						calendar.set(Calendar.SECOND, 0);
						calendar.set(Calendar.MILLISECOND, 0);

						s = calendar.getTime();

						calendar = Calendar.getInstance();

						calendar.setTime(t.getEndDateReal() != null
								? t.getEndDateReal()
								: end);
						calendar.set(Calendar.SECOND, 0);
						calendar.set(Calendar.MILLISECOND, 0);

						e = calendar.getTime();
						DefaultScheduleEvent ev = new DefaultScheduleEvent(
								t.getName(), s, e, t);
						ev.setDescription(getDialogHeader(t, null));

						if (t.getAssignedTo() == null || !t.getAssignedTo()
								.equals(loginBean.getContact())) {
							ev.setStyleClass("scheduleNotMine");
						}
						addEvent(ev);
					}
					scheduleInitialDateAll = new Date(
							(end.getTime() + start.getTime()) / 2);
				}
			};
		return lazyEventModelAll;
	}

	public void setLazyEventModelAll(ScheduleModel lazyEventModel) {
		this.lazyEventModelAll = lazyEventModel;
	}

	public Date getScheduleInitialDateAll() {
		return scheduleInitialDateAll;
	}

	public void setScheduleInitialDateAll(Date scheduleInitialDate) {
		this.scheduleInitialDateAll = scheduleInitialDate;
	}

	public String getScheduleInitialViewAll() {

		if (scheduleInitialViewAll == null)
			scheduleInitialViewAll = "month";
		return scheduleInitialViewAll;
	}

	public void setScheduleInitialViewAll(String scheduleInitialView) {
		this.scheduleInitialViewAll = scheduleInitialView;
	}

	public void onEventSelectAll(SelectEvent selectEvent) {
		task = (JJTask) ((ScheduleEvent) selectEvent.getObject()).getData();
		scheduleInitialDateAll = task.getStartDateReal();
		task = jJTaskService.findJJTask(task.getId());
		taskTreeNode = null;
		selectedReq = null;
		selectedTree = null;
		initiateReqTreeNode();
	}

	public void onViewChangeAll(SelectEvent selectEvent) {

		scheduleInitialDateAll = (Date) ((Schedule) selectEvent.getComponent())
				.getInitialDate();
		scheduleInitialViewAll = ((Schedule) selectEvent.getComponent())
				.getView();
	}

	public void onTabTeamChange() {

		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, String> paramMap = context.getExternalContext()
				.getRequestParameterMap();
		if (paramMap.get("activeTeamIndex") != null) {
			String paramIndex = paramMap.get("activeTeamIndex");
			setActiveTabTeamIndex(Integer.valueOf(paramIndex));

		}
	}

	// layout options
	private LayoutOptions layoutOptionsOne;
	private String stateOne;

	public LayoutOptions getLayoutOptionsOne() {
		return layoutOptionsOne;
	}

	public void setLayoutOptionsOne(LayoutOptions layoutOptionsOne) {
		this.layoutOptionsOne = layoutOptionsOne;
	}

	public String getStateOne() {
		return stateOne;
	}

	public void setStateOne(String stateOne) {
		this.stateOne = stateOne;
	}

	public void initLayoutOptions() {
		LayoutOptions panes = new LayoutOptions();
		layoutOptionsOne = new LayoutOptions();
		panes.addOption("slidable", true);
		panes.addOption("closable", true);
		panes.addOption("resizeWhileDragging", true);
		layoutOptionsOne.setPanesOptions(panes);
		panes = new LayoutOptions();
		panes.addOption("size", "50%");
		panes.addOption("maxSize", 1200);
		panes.addOption("minsize", 300);
		layoutOptionsOne.setEastOptions(panes);
		panes = new LayoutOptions();
		panes.addOption("size", "50%");
		panes.addOption("closable", true);
		panes.addOption("maxSize", 1000);
		panes.addOption("minsize", 300);
		layoutOptionsOne.setCenterOptions(panes);

	}

	// taskDialog.xhtml

	public int getWidth() {

		if (taskTreeNode != null)
			return 970;
		else
			return 810;

	}

	public int getHeight() {

		if (taskTreeNode != null)
			return 490;
		else
			return 410;
	}

	public String getActiveIndex() {

		return ((LoginBean) LoginBean.findBean("loginBean")).isMobile()
				? "-1"
				: "0";

	}

	public void setActiveIndex(String index) {

	}

	public String getTaskIcone(JJTask tt) {

		String icon = "";

		if (tt.getRequirement() != null)
			icon = "function";
		else if (tt.getBug() != null)
			icon = "bug";
		else if (tt.getTestcase() != null)
			icon = "test";

		return icon;
	}

	public HtmlPanelGrid populateViewPanelGrid() {
		return (HtmlPanelGrid) FacesContext.getCurrentInstance()
				.getApplication().createComponent(HtmlPanelGrid.COMPONENT_TYPE);
	}

}
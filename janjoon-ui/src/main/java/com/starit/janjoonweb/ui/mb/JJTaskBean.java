package com.starit.janjoonweb.ui.mb;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import java.util.TimeZone;
import java.util.TreeMap;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.extensions.event.timeline.TimelineAddEvent;
import org.primefaces.extensions.event.timeline.TimelineModificationEvent;
import org.primefaces.extensions.model.layout.LayoutOptions;
import org.primefaces.extensions.model.timeline.TimelineEvent;
import org.primefaces.extensions.model.timeline.TimelineModel;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

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
import com.starit.janjoonweb.ui.mb.converter.JJTaskConverter;
import com.starit.janjoonweb.ui.mb.util.ContactCalendarUtil;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;
import com.starit.janjoonweb.ui.mb.util.SprintUtil;

@RooSerializable
@RooJsfManagedBean(entity = JJTask.class, beanName = "jJTaskBean")
public class JJTaskBean {

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

	public void setjJPermissionService(JJPermissionService jJPermissionService) {
		this.jJPermissionService = jJPermissionService;
	}

	public void setjJCategoryService(JJCategoryService jJCategoryService) {
		this.jJCategoryService = jJCategoryService;
	}

	private List<TaskData> tasksData;
	private JJTask task;

	private TimelineModel model;

	private Date start;
	private Date end;
	private long zoomMin;
	private long zoomMax;
	private String sortMode;
	// private List<JJContact> contacts;

	private JJProject project;
	private JJProduct product;
	private JJVersion version;

	private JJSprint sprint;
	private List<JJSprint> sprints;
	private List<JJTask> toDoTasks;

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
	private boolean oldCopyObjects;

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
			if (objet.equalsIgnoreCase("Bug")) {
				names.add("CLOSED");
			} else if (objet.equalsIgnoreCase("Requirement")) {
				names.add("DELETED");
				names.add("CANCELED");
			} else if (objet.equalsIgnoreCase("Testcase")) {
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
		objets.add("Bug");
		objets.add("Requirement");
		objets.add("Testcase");

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

	public String getDialogHeader(JJTask ttt) {

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
		} else
			return "";

	}

	public void updateTask(JJTask tt, String operation) {
		JJTask beforeUpdateTT = jJTaskService.findJJTask(tt.getId());
		JJSprint ss = beforeUpdateTT.getSprint();
		ContactCalendarUtil calendarUtil;

		if (tt.getAssignedTo() != null) {
			calendarUtil = new ContactCalendarUtil(tt.getAssignedTo());

		} else {

			calendarUtil = new ContactCalendarUtil(jJProjectService
					.findJJProject(LoginBean.getProject().getId()).getManager()
					.getCompany());

		}

		if ((beforeUpdateTT.getWorkloadPlanned() == null && tt
				.getWorkloadPlanned() != null)
				|| (tt.getWorkloadPlanned() != null && !tt.getWorkloadPlanned()
						.equals(beforeUpdateTT.getWorkloadPlanned())))
			calendarUtil.getEndDate(tt, Planned, null);
		else {
			if ((beforeUpdateTT.getEndDatePlanned() == null && tt
					.getEndDatePlanned() != null)
					|| (tt.getEndDatePlanned() != null && !tt
							.getEndDatePlanned().equals(
									beforeUpdateTT.getEndDatePlanned())))
				calendarUtil.getStartDate(tt, Planned, null);
			else {
				if ((beforeUpdateTT.getStartDatePlanned() == null && tt
						.getStartDatePlanned() != null)
						|| (tt.getStartDatePlanned() != null && !tt
								.getStartDatePlanned().equals(
										beforeUpdateTT.getStartDatePlanned())))
					calendarUtil.getEndDate(tt, Planned, null);
			}
		}

		if (tt.getStatus() != null) {
			if (beforeUpdateTT.getStatus() == null
					|| !beforeUpdateTT.getStatus().equals(tt.getStatus())) {
				if (tt.getStatus().getName().equalsIgnoreCase("todo")) {
					tt.setStartDateReal(null);
					tt.setEndDateReal(null);
					tt.setWorkloadReal(null);
				} else if (tt.getStatus().getName()
						.equalsIgnoreCase("in progress")) {
					tt.setEndDateReal(null);
					tt.setWorkloadReal(null);
					if (tt.getStartDateReal() == null)
						tt.setStartDateReal(new Date());
				} else if (tt.getStatus().getName().equalsIgnoreCase("done")) {
					if (tt.getStartDateReal() == null)
						tt.setStartDateReal(new Date());

					if (tt.getEndDateReal() == null)
						tt.setEndDateReal(new Date());

				}
			}
		} else {
			tt.setStatus(jJStatusService.getOneStatus("TODO", "Task", true));
			if (beforeUpdateTT.getStatus() == null
					|| !beforeUpdateTT.getStatus().equals(tt.getStatus())) {

				tt.setStartDateReal(null);
				tt.setEndDateReal(null);
				tt.setWorkloadReal(null);
			}

		}

		saveJJTask(tt, true);
		task = tt;
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);

		if ((ss != null || task.getSprint() != null)
				&& session.getAttribute("jJSprintBean") != null) {

			JJSprintBean jJSprintBean = (JJSprintBean) session
					.getAttribute("jJSprintBean");

			if (ss != null && task.getSprint() != null) {
				SprintUtil s = SprintUtil.getSprintUtil(task.getSprint()
						.getId(), jJSprintBean.getSprintList());

				if (s != null) {
					s = new SprintUtil(jJSprintService.findJJSprint(task
							.getSprint().getId()),
							jJTaskService.getSprintTasks(jJSprintService
									.findJJSprint(task.getSprint().getId()),
									LoginBean.getProduct()), jJContactService);

					// sprintUtil.setRenderTaskForm(false);
					jJSprintBean.getSprintList().set(
							jJSprintBean.contains(s.getSprint().getId()), s);
				}
				if (!ss.equals(task.getSprint())) {
					s = SprintUtil.getSprintUtil(ss.getId(),
							jJSprintBean.getSprintList());

					if (s != null) {
						s = new SprintUtil(jJSprintService.findJJSprint(ss
								.getId()), jJTaskService.getSprintTasks(
								jJSprintService.findJJSprint(ss.getId()),
								LoginBean.getProduct()), jJContactService);

						// sprintUtil.setRenderTaskForm(false);
						jJSprintBean.getSprintList()
								.set(jJSprintBean.contains(s.getSprint()
										.getId()), s);
					}
				}
			} else {
				if (task.getSprint() != null) {

					SprintUtil s = SprintUtil.getSprintUtil(task.getSprint()
							.getId(), jJSprintBean.getSprintList());

					if (s != null) {
						s = new SprintUtil(jJSprintService.findJJSprint(task
								.getSprint().getId()),
								jJTaskService.getSprintTasks(
										jJSprintService.findJJSprint(task
												.getSprint().getId()),
										LoginBean.getProduct()),
								jJContactService);

						// sprintUtil.setRenderTaskForm(false);
						jJSprintBean.getSprintList()
								.set(jJSprintBean.contains(s.getSprint()
										.getId()), s);
					}

				} else {
					SprintUtil s = SprintUtil.getSprintUtil(ss.getId(),
							jJSprintBean.getSprintList());

					if (s != null) {
						s = new SprintUtil(jJSprintService.findJJSprint(ss
								.getId()), jJTaskService.getSprintTasks(
								jJSprintService.findJJSprint(ss.getId()),
								LoginBean.getProduct()), jJContactService);

						// sprintUtil.setRenderTaskForm(false);
						jJSprintBean.getSprintList()
								.set(jJSprintBean.contains(s.getSprint()
										.getId()), s);
					}
				}
			}
		}

		updateView(jJTaskService.findJJTask(tt.getId()), UPDATE_OPERATION);
		task = jJTaskService.findJJTask(tt.getId());
		taskTreeNode = null;
		selectedReq = null;
		selectedTree = null;
		getTaskTreeNode();
		// FaceletContext faceletContext = (FaceletContext)
		// FacesContext.getCurrentInstance().getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY);
		// faceletContext.setAttribute("JJTask_", task);

		if (operation.equalsIgnoreCase("main")) {
			toDoTasks = null;
			initToDoTasks(null);
		} else if (operation.equalsIgnoreCase("dev")
				&& LoginBean.findBean("jJDevelopment") != null) {
			((DevelopmentBean) LoginBean.findBean("jJDevelopment"))
					.setTask(task);
			((DevelopmentBean) LoginBean.findBean("jJDevelopment"))
					.setTasks(jJTaskService.getTasksByProduct(
							LoginBean.getProduct(), LoginBean.getProject()));
		}

		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_updated", "Task");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}

	public void closeEvent(String operation) {

		// System.err.println("dfgdfgdfg");
		// //Long id = null;
		// if (task.getSprint() != null)
		// id = task.getSprint().getId();
		//
		// task = null;

		// if (mode != null && mode.equalsIgnoreCase("scrum")
		// && operation.equalsIgnoreCase("planning")) {
		// HttpSession session = (HttpSession) FacesContext
		// .getCurrentInstance().getExternalContext()
		// .getSession(false);
		// JJSprintBean jJSprintBean = (JJSprintBean) session
		// .getAttribute("jJSprintBean");
		// RequestContext context = RequestContext.getCurrentInstance();
		// int i = 0;
		// if (((LoginBean) LoginBean.findBean("loginBean")).isRenderGantt())
		// i = 1;
		// else
		// i = 0;
		//
		// context.execute("PF('projectTabView').select(" + i + ")");
		// jJSprintBean.setUpdate(false);
		// if (id != null)
		// context.execute("PF('SprintTab').select("
		// + jJSprintBean.contains(id) + ")");
		// } else {
		if (operation.equalsIgnoreCase("main"))
			toDoTasks = null;
		// }
	}

	public List<JJStatus> completeStatusTask(String query) {
		List<JJStatus> suggestions = new ArrayList<JJStatus>();
		suggestions.add(null);
		for (JJStatus jJStatus : jJStatusService.getStatus("Task", true, null,
				true)) {
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

			for (JJContact jJContact : jJPermissionService.areAuthorized(proj
					.getManager().getCompany(), null, proj, null,
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
		for (JJSprint jJSprint : jJSprintService.getSprints(
				LoginBean.getProject(), true)) {
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

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		LoginBean loginBean = (LoginBean) session.getAttribute("loginBean");

		if (loginBean.getAuthorisationService().isrProject()) {
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

			Date now = new Date();
			if (sortMode == null) {
				sortMode = "chapter";

			} else if (sortMode.isEmpty()) {
				sortMode = "chapter";

			}

			if (sprint == null) {
				// Before 4 hours for now
				cal.setTimeInMillis(now.getTime() - 24 * 60 * 60 * 1000);
				start = cal.getTime();

				// After 8 hours for now
				cal.setTimeInMillis(now.getTime() + 24 * 60 * 60 * 1000);
				end = cal.getTime();
			} else {
				start = sprint.getStartDate();
				end = sprint.getEndDate();
			}

			// one day in milliseconds for zoomMin
			zoomMin = 1000L * 60 * 60 * 24;

			// one day in milliseconds for zoomMax
			zoomMax = 1000L * 60 * 60 * 24 * 31 * 5;

			// Create timeline model
			model = new TimelineModel();

			getProject();
			tasksData = new ArrayList<TaskData>();

			// 65 = ASCII A
			int k = 65;

			List<JJChapter> chapters = jJChapterService.getChapters(
					((LoginBean) LoginBean.findBean("loginBean")).getContact()
							.getCompany(), project, true);
			List<JJTask> allJJtask = null;
			if (!sortMode.equalsIgnoreCase("chapter"))
				allJJtask = new ArrayList<JJTask>();

			for (JJChapter chapter : chapters) {
				Map<Date, String> min = new TreeMap<Date, String>();
				Map<Date, String> max = new TreeMap<Date, String>();

				List<JJTask> tasks = new ArrayList<JJTask>();
				tasks.addAll(jJTaskService.getTasks(sprint, null,
						LoginBean.getProduct(), null, chapter, true, null,
						null, null, true, true, false, "Requirement"));

				tasks.addAll(jJTaskService.getTasks(sprint, null,
						LoginBean.getProduct(), null, chapter, true, null,
						null, null, true, true, false, "Testcase"));

				tasks.addAll(jJTaskService.getTasks(sprint, null,
						LoginBean.getProduct(), null, chapter, true, null,
						null, null, true, true, false, "Bug"));

				TreeMap<String, JJTask> Tasks = new TreeMap<String, JJTask>();
				// TreeMap<String, JJTask> Tasks = new TreeMap<String,
				// JJTask>(new
				// Comparator<JJTask>() {
				// public int compare(JJTask t1, JJTask t2) {
				// return
				// t1.getStartDatePlanned().compareTo(t2.getStartDatePlanned());
				// }
				// });

				for (JJTask tt : tasks) {

					// Tasks.put(task.getName(), task);
					Tasks.put(tt.getId() + "", tt);
				}

				if (!sortMode.equalsIgnoreCase("chapter"))
					for (String key : Tasks.keySet())
						allJJtask.add(Tasks.get(key));

				// Iterate over HashMap
				if (sortMode.equalsIgnoreCase("chapter")) {
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
						model.add(new TimelineEvent(tt, new GregorianCalendar(
								Calendar.getInstance().get(Calendar.YEAR) - 5,
								1, 1).getTime(), new GregorianCalendar(Calendar
								.getInstance().get(Calendar.YEAR) + 5, 1, 1)
								.getTime(), false, group, "invisible"));

						if (tt.getStartDateReal() != null) {

							Date endDate;
							if (tt.getEndDateReal() == null)
								endDate = tt.getEndDatePlanned();
							else
								endDate = tt.getEndDateReal();

							TimelineEvent event = new TimelineEvent(tt,
									tt.getStartDateReal(), endDate, true,
									group, Real);

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
							min.put(tt.getStartDatePlanned(), tt
									.getStartDatePlanned().toString());
						}

						if (tt.getStartDateRevised() != null) {
							min.put(tt.getStartDateRevised(), tt
									.getStartDateRevised().toString());
						}

						if (tt.getStartDateReal() != null) {
							min.put(tt.getStartDateReal(), tt
									.getStartDateReal().toString());
						}

						if (tt.getEndDatePlanned() != null) {
							max.put(tt.getEndDatePlanned(), tt
									.getEndDatePlanned().toString());
						}

						if (tt.getEndDateRevised() != null) {
							max.put(tt.getEndDateRevised(), tt
									.getEndDateRevised().toString());
						}

						if (tt.getEndDateReal() != null) {
							max.put(tt.getEndDateReal(), tt.getEndDateReal()
									.toString());
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
						model.add(new TimelineEvent(chapter,
								new GregorianCalendar(Calendar.getInstance()
										.get(Calendar.YEAR) - 5, 1, 1)
										.getTime(), new GregorianCalendar(
										Calendar.getInstance().get(
												Calendar.YEAR) + 5, 1, 1)
										.getTime(), false, group, "invisible"));

						model.add(event);
						k++;

					}
				}

			}
			if (!sortMode.equalsIgnoreCase("chapter"))
				loadSortedData(allJJtask, k);

			task = null;
		} else {
			String path = FacesContext.getCurrentInstance()
					.getExternalContext().getRequestContextPath();

			try {
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect(path + "/pages/main.jsf?faces-redirect=true");
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

	}

	public void loadSortedData(List<JJTask> allTasks, int k) {

		allTasks.addAll(jJTaskService.getTasks(sprint, project,
				LoginBean.getProduct(), null, null, true, null, null, null,
				true, false, false, "requirement"));
		allTasks.addAll(jJTaskService.getTasks(sprint, project,
				LoginBean.getProduct(), null, null, true, null, null, null,
				true, false, false, "bug"));
		allTasks.addAll(jJTaskService.getTasks(sprint, project,
				LoginBean.getProduct(), null, null, true, null, null, null,
				true, false, false, "testcase"));

		Collections.sort(allTasks, new Comparator<JJTask>() {

			@Override
			public int compare(JJTask o1, JJTask o2) {
				if (sortMode.equalsIgnoreCase("StartDatePlanned")) {
					return o1.getStartDatePlanned().compareTo(
							o2.getStartDatePlanned());

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
			if (k == 92)
				k++;

			char c = (char) k;
			String group = "<span style=display:none>" + c + "</span>";
			boolean add = false;
			JJChapter chapter = tt.getChapter();

			TaskData taskData = null;
			// System.err.println(group + "/" + tt.getName());

			model.add(new TimelineEvent(tt, new GregorianCalendar(Calendar
					.getInstance().get(Calendar.YEAR) - 5, 1, 1).getTime(),
					new GregorianCalendar(Calendar.getInstance().get(
							Calendar.YEAR) + 5, 1, 1).getTime(), false, group,
					"invisible"));

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
							tt.getStartDateRevised(), endDate, workload, true);
			} else {

				String style = null;
				if (tt.getStartDateReal() == null)
					style = "planned1";
				else
					style = "planned2";

				TimelineEvent event = new TimelineEvent(tt,
						tt.getStartDatePlanned(), tt.getEndDatePlanned(), true,
						group, style);
				model.add(event);

				if (!add)
					taskData = new TaskData(tt, chapter,
							tt.getStartDatePlanned(), tt.getEndDatePlanned(),
							tt.getWorkloadPlanned(), false);
			}

			tasksData.add(taskData);
			k++;
		}

	}

	public void onCellEdit(CellEditEvent event) {
		UIColumn column = event.getColumn();

		String columnKey = column.getColumnKey();

		DataTable dataTable = (DataTable) event.getSource();
		TaskData taskData = (TaskData) dataTable.getRowData();
		JJTask task = taskData.getTask();
		ContactCalendarUtil calendarUtil;

		if (task.getAssignedTo() != null) {
			calendarUtil = new ContactCalendarUtil(task.getAssignedTo());

		} else {

			calendarUtil = new ContactCalendarUtil(jJProjectService
					.findJJProject(LoginBean.getProject().getId()).getManager()
					.getCompany());

		}
		Object newValue = event.getNewValue();
		String message = "";
		FacesMessage facesMessage = null;

		boolean valid = true;

		if (columnKey.contains("sdpr")) {
			if (newValue != null) {
				Date date = (Date) newValue;
				task.setStartDateRevised(date);

				if (taskData.getWorkload() != null) {

					calendarUtil.getEndDate(task, Revised, null);
				} else
					task.setEndDateRevised(taskData.getEndDate());
			}

		} else if (columnKey.contains("edpr")) {
			if (newValue != null) {
				Date date = (Date) newValue;
				task.setEndDateRevised(date);
				if (taskData.getStartDate() != null) {
					task.setStartDateRevised(taskData.getStartDate());

				} else {
					if (taskData.getWorkload() != null) {
						calendarUtil.getStartDate(task, Revised, null);
					}
				}
			}

		} else if (columnKey.contains("wpr")) {

			if (newValue != null) {
				int workloadRevised = (int) newValue;
				task.setWorkloadRevised(workloadRevised);
				if (taskData.getStartDate() != null) {

					task.setStartDateRevised(taskData.getStartDate());

					calendarUtil.getEndDate(task, Revised, null);

				} else if (taskData.getEndDate() != null) {
					calendarUtil.getStartDate(task, Revised, null);
				}

			}

		} else if (columnKey.contains("sdr")) {
			if (newValue != null) {
				Date date = (Date) newValue;
				task.setStartDateReal(date);

				if (task.getWorkloadReal() != null) {
					calendarUtil.getEndDate(task, Real, jJTaskService);
				}

			}

		} else if (columnKey.contains("edr")) {
			if (newValue != null) {
				Date date = (Date) newValue;
				task.setEndDateReal(date);
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
			saveJJTask(task, true);
			task = jJTaskService.findJJTask(task.getId());
			updateView(task, UPDATE_OPERATION);

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
									.findJJSprint(task.getSprint().getId()),
									LoginBean.getProduct()), jJContactService);

					// sprintUtil.setRenderTaskForm(false);
					jJSprintBean.getSprintList().set(
							jJSprintBean.contains(s.getSprint().getId()), s);
				}
			}
			reset();

			message = "Success Update";
			facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO,
					message, "Task");
			// RequestContext.getCurrentInstance().equals("onCellEditTableComplete");

		} else {
			facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					message, "Task");
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

		saveJJTask(duplicatedTask, false);

		// updateView(tJjTask, false);
		//
		if (duplicatedTask.getSprint() != null) {

			JJSprintBean jJSprintBean = (JJSprintBean) LoginBean
					.findBean("jJSprintBean");
			if (jJSprintBean.contains(duplicatedTask.getSprint().getId()) != -1) {
				SprintUtil s = SprintUtil.getSprintUtil(duplicatedTask
						.getSprint().getId(), jJSprintBean.getSprintList());
				if (s != null) {
					s = new SprintUtil(
							jJSprintService.findJJSprint(duplicatedTask
									.getSprint().getId()),
							jJTaskService.getSprintTasks(jJSprintService
									.findJJSprint(duplicatedTask.getSprint()
											.getId()), LoginBean.getProduct()),
							jJContactService);

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
		copyObjets = false;
		oldCopyObjects = false;

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
			context.execute("PF('taskImportDialogWidget').show()");
		}

		getProject();
		getProduct();
		getVersion();

		importFormats = new ArrayList<ImportFormat>();

	}

	public void validateTaskField() {
		boolean validationFailed = false;
		int i = 0;
		FacesMessage message = null;
		while (i < importFormats.size() && !validationFailed) {
			ImportFormat format = importFormats.get(i);
			if (format.getCopyObjet()) {
				Date startDate = format.getStartDate();
				if (format.getStartDate() == null) {
					validationFailed = true;
					message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"start date is requierd", null);

				} else {
					if (mode.equalsIgnoreCase("scrum")) {
						if (startDate.before(((JJSprintBean) LoginBean
								.findBean("jJSprintBean")).getSprintUtil()
								.getSprint().getStartDate())) {
							validationFailed = true;
							message = new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"Start Date may not be before Sprint Start Date.",
									null);
						} else if (startDate.after(((JJSprintBean) LoginBean
								.findBean("jJSprintBean")).getSprintUtil()
								.getSprint().getEndDate())) {
							validationFailed = true;

							message = new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"Start Date may not be after Sprint end Date.",
									null);
						}
					} else {
						if (sprint != null) {
							if (startDate.before(sprint.getStartDate())) {
								validationFailed = true;

								message = new FacesMessage(
										FacesMessage.SEVERITY_ERROR,
										"Start Date may not be before Sprint Start Date.",
										null);
							} else if (startDate.after(sprint.getEndDate())) {
								validationFailed = true;
								message = new FacesMessage(
										FacesMessage.SEVERITY_ERROR,
										"Start Date may not be after Sprint end Date.",
										null);
							}

						}
					}

				}
				if (!validationFailed) {

					Integer workload = format.getWorkload();
					if (workload == null) {

						validationFailed = true;
						message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Workload is requierd", null);
					} else if (workload <= 0) {

						validationFailed = true;
						message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Please set Workload More than 0", null);
					}

				}

			}
			i++;
		}
		if (!validationFailed)
			RequestContext.getCurrentInstance().execute("importTask()");
		else {
			FacesContext.getCurrentInstance().addMessage(null, message);
			RequestContext
					.getCurrentInstance()
					.execute(
							"PF('taskImportDialogWidget').jq.effect('shake', {times:5}, 100)");
		}

	}

	public void checkAll() {
		copyObjets = false;
		oldCopyObjects = false;
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
		List<JJTask> tasks;
		copyObjets = false;
		oldCopyObjects = false;
		disabledImportButton = true;

		if (objet != null) {

			if (objet.equalsIgnoreCase("Bug")) {

				for (JJBug bug : jJBugService.getImportBugs(
						((LoginBean) LoginBean.findBean("loginBean"))
								.getContact().getCompany(), project, version,
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

			} else if (objet.equalsIgnoreCase("Requirement")) {
				LoginBean loginBean = (LoginBean) LoginBean
						.findBean("loginBean");
				for (JJRequirement requirement : jJRequirementService
						.getRequirements(((LoginBean) LoginBean
								.findBean("loginBean")).getContact()
								.getCompany(), importCategory, loginBean
								.getAuthorizedMap("Requirement", project,
										product), version, importStatus, null,
								false, true, false, false, null)) {

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

			} else if (objet.equalsIgnoreCase("Testcase")) {

				for (JJTestcase testcase : jJTestcaseService
						.getImportTestcases(null, project,
								LoginBean.getProduct(), true, false)) {

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

	public void fillInDates() {
		for (ImportFormat format : importFormats) {
			if (format.getCopyObjet()) {
				if (this.sprint != null && mode.equalsIgnoreCase("planning"))
					format.setStartDate(sprint.getStartDate());
				else if (mode.equalsIgnoreCase("planning"))
					format.setStartDate(new Date());
				else
					format.setStartDate(((JJSprintBean) LoginBean
							.findBean("jJSprintBean")).getSprintUtil()
							.getSprint().getStartDate());
			}
		}
	}

	private boolean fillInDate;

	public boolean isFillInDate() {

		fillInDate = true;
		for (ImportFormat format : importFormats) {
			if (format.getCopyObjet()) {
				fillInDate = false;
				break;
			}
		}
		return fillInDate;
	}

	public void setFillInDate(boolean fillInDate) {
		this.fillInDate = fillInDate;
	}

	public void importTask() {

		long startTime = System.nanoTime();
		JJContact c = jJProjectService.findJJProject(
				LoginBean.getProject().getId()).getManager();
		if (c == null)
			c = ((LoginBean) LoginBean.findBean("loginBean")).getContact();

		ContactCalendarUtil calendarUtil = new ContactCalendarUtil(
				c.getCompany());
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH");

		for (ImportFormat format : importFormats) {

			if (format.getCopyObjet()) {

				String name = null;
				if (mode == null)
					mode = "planning";

				JJTask task = new JJTask();
				task.setEnabled(true);
				task.setSpecification(format.getSpecification());
				if (mode.equalsIgnoreCase("planning"))
					task.setSprint(this.sprint);

				if (format.getStartDate() == null) {
					if (this.sprint != null
							&& mode.equalsIgnoreCase("planning"))
						task.setStartDatePlanned(calendarUtil
								.nextWorkingDate(sprint.getStartDate()));
					else
						task.setStartDatePlanned(calendarUtil
								.nextWorkingDate(new Date()));

				} else {
					task.setStartDatePlanned(calendarUtil
							.nextWorkingDate(format.getStartDate()));
				}

				if (format.getWorkload() != null) {
					task.setWorkloadPlanned(format.getWorkload());

					if (task.getStartDatePlanned() != null)
						calendarUtil.getEndDate(task, Planned, null);
				}

				if (format.getObject() instanceof JJRequirement) {

					JJRequirement requirement = (JJRequirement) format
							.getObject();

					name = requirement.getName() + " (" + df.format(new Date())
							+ "h)";

					requirement = jJRequirementService
							.findJJRequirement(requirement.getId());

					requirement.getTasks().add(task);

					task.setRequirement(requirement);

				} else if (format.getObject() instanceof JJBug) {

					JJBug bug = (JJBug) format.getObject();

					name = bug.getName() + " (" + df.format(new Date()) + "h)";

					bug = jJBugService.findJJBug(bug.getId());
					bug.getTasks().add(task);

					task.setBug(bug);

				} else if (format.getObject() instanceof JJTestcase) {

					JJTestcase testcase = (JJTestcase) format.getObject();

					name = testcase.getName() + " (" + df.format(new Date())
							+ "h)";

					testcase = jJTestcaseService.findJJTestcase(testcase
							.getId());

					testcase.getTasks().add(task);

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
					task.setCreatedBy(((LoginBean) LoginBean
							.findBean("loginBean")).getContact());
					JJStatus status = jJStatusService.getOneStatus("TODO",
							"Task", true);
					if (status != null)
						task.setStatus(status);
					task.setEnabled(true);
					task.setSprint(jJSprintBean.getSprintUtil().getSprint());

					if (format.getStartDate() == null) {
						task.setStartDatePlanned(jJSprintBean.getSprintUtil()
								.getSprint().getStartDate());
						if (task.getWorkloadPlanned() != null)
							calendarUtil.getEndDate(task, Planned, null);
					}

				}

				if (task.getWorkloadPlanned() == null) {
					task.setWorkloadPlanned(3);
					calendarUtil.getEndDate(task, Planned, null);
				}
				saveJJTask(task, false);
				updateView(task, ADD_OPERATION);

			}

		}

		if (mode.equalsIgnoreCase("planning")) {

			// project = null;
			// tasksData = null;
			if (sprint != null) {

				sprint = jJSprintService.findJJSprint(sprint.getId());
				HttpSession session = (HttpSession) FacesContext
						.getCurrentInstance().getExternalContext()
						.getSession(false);
				JJSprintBean jJSprintBean = (JJSprintBean) session
						.getAttribute("jJSprintBean");
				jJSprintBean.getSprintList().set(
						jJSprintBean.contains(sprint.getId()),
						new SprintUtil(sprint, jJTaskService.getSprintTasks(
								sprint, LoginBean.getProduct()),
								jJContactService));

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

				JJSprint sprint = jJSprintService.findJJSprint(jJSprintBean
						.getSprintUtil().getSprint().getId());
				SprintUtil sprintUtil = new SprintUtil(sprint,
						jJTaskService.getSprintTasks(sprint,
								LoginBean.getProduct()), jJContactService);
				jJSprintBean.setSprintUtil(sprintUtil);
				jJSprintBean.getSprintList().set(
						jJSprintBean.contains(sprint.getId()), sprintUtil);

			}

			// project = null;
			// tasksData = null;

			String message = "message_successfully_created";

			FacesMessage facesMessage = MessageFactory.getMessage(message,
					"Task");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('taskImportDialogWidget').hide()");

		}
		long endTime = System.nanoTime();

		long duration = (endTime - startTime);
		System.err.println("duration :" + (double) duration / 1000000000.0);

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
		oldCopyObjects = false;

		if (objet != null) {
			if (objet.equalsIgnoreCase("Testcase")) {
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
		oldCopyObjects =false;

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
		oldCopyObjects = false;

		if (objet != null) {
			fillTableImport();
		} else {
			importFormats = new ArrayList<ImportFormat>();

		}
	}

	public void sprintSelectionChanged(final AjaxBehaviorEvent event) {
		tasksData = null;
		model = null;
	}

	public void copyObjetsListener() {

		if (copyObjets == oldCopyObjects)
			copyObjets = !copyObjets;
		for (ImportFormat importFormat : importFormats) {
			importFormat.setCopyObjet(copyObjets);
		}

		disabledImportButton = !copyObjets;
		oldCopyObjects = copyObjets;

	}

	public void saveJJTask(JJTask ttt, boolean update) {

		if (update) {
			JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
					.getContact();
			ttt.setUpdatedBy(contact);
			ttt.setUpdatedDate(new Date());
			jJTaskService.updateJJTask(ttt);
		} else {

			ttt.setCreationDate(new Date());
			JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
					.getContact();
			ttt.setCreatedBy(contact);
			jJTaskService.saveJJTask(ttt);
		}

		ttt = jJTaskService.findJJTask(ttt.getId());

		if (((JJRequirementBean) LoginBean.findBean("jJRequirementBean")) != null
				&& ttt.getRequirement() != null) {

			((JJRequirementBean) LoginBean.findBean("jJRequirementBean"))
					.updateDataTable(ttt.getRequirement(),
							JJRequirementBean.UPDATE_OPERATION, false);
		}
	}

	public void copyObjetListener() {

		boolean copyAll = true;
		for (ImportFormat importFormat : importFormats) {
			if (!importFormat.getCopyObjet()) {
				copyAll = false;
				break;
			}

		}

		copyObjets = copyAll;
		oldCopyObjects = copyAll;

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
		private boolean specification;

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

		public boolean getSpecification() {
			return specification;
		}

		public void setSpecification(boolean specification) {
			this.specification = specification;
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
			for (JJTask Task : taskList) {
				if (Task.getEnabled()
						&& Task.getRequirement().getChapter()
								.equals(task.getRequirement().getChapter())) {
					storeTasks.add(Task);
				}
			}

			List<JJTask> list = new ArrayList<JJTask>();
			list.addAll(jJTaskService.getTasks(sprint, null,
					LoginBean.getProduct(), null, chapter, true, null, null,
					null, true, false, false, "Requirement"));

			list.addAll(jJTaskService.getTasks(sprint, null,
					LoginBean.getProduct(), null, chapter, true, null, null,
					null, true, false, false, "Testcase"));

			list.addAll(jJTaskService.getTasks(sprint, null,
					LoginBean.getProduct(), null, chapter, true, null, null,
					null, true, false, false, "Bug"));

			list.remove(task);

			tasks = convertTaskListToStringList(list);
			this.icone = "";
			selectedTasks = convertTaskListToStringList(storeTasks);

			if (this.task.getRequirement() != null)
				this.icone = "function.ico";
			else if (this.task.getBug() != null)
				this.icone = "bug.ico";
			else if (this.task.getTestcase() != null)
				this.icone = "test.ico";
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
					this.icone = "function.ico";
				else if (this.task.getBug() != null)
					this.icone = "bug.ico";
				else if (this.task.getTestcase() != null)
					this.icone = "test.ico";
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
		setCreateDialogVisible(false);
	}

	public void copyName(JJTask ttt) {
		StringSelection stringSelection = new StringSelection(
				this.getDialogHeader(ttt));
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

			tJjTask = jJTaskService.findJJTask(selectedTaskData.getTask()
					.getId());
			tJjTask.setEnabled(false);
			saveJJTask(tJjTask, true);
			updateView(tJjTask, DELETE_OPERATION);

			if (tJjTask.getSprint() != null
					&& jJSprintBean.contains(tJjTask.getSprint().getId()) != -1) {
				SprintUtil s = SprintUtil.getSprintUtil(tJjTask.getSprint()
						.getId(), jJSprintBean.getSprintList());
				if (s != null) {
					s = new SprintUtil(jJSprintService.findJJSprint(tJjTask
							.getSprint().getId()),
							jJTaskService.getSprintTasks(jJSprintService
									.findJJSprint(tJjTask.getSprint().getId()),
									LoginBean.getProduct()), jJContactService);

					// sprintUtil.setRenderTaskForm(false);
					jJSprintBean.getSprintList().set(
							jJSprintBean.contains(s.getSprint().getId()), s);
				}
			}
			FacesMessage facesMessage = MessageFactory.getMessage(
					"message_successfully_deleted", "Task");
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

					// System.out.println("TreeNode "
					// + task.getRequirement().getName());

					taskTreeNode = new DefaultTreeNode("Requirement :", null);
					DefaultTreeNode tree = new DefaultTreeNode(
							task.getRequirement(), taskTreeNode);

					// System.out.println("TreeNode "
					// + task.getRequirement().getName());

					if (task.getRequirement().getRequirementLinkDown() != null) {

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
		String buffer = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
				+ System.getProperty("line.separator")
				+ "<Project xmlns=\"http://schemas.microsoft.com/project\">"
				+ System.getProperty("line.separator")
				+ "<SaveVersion>9</SaveVersion>"
				+ System.getProperty("line.separator")
				+ "<Title>"
				+ LoginBean.getProject().getName()
				+ "</Title>"
				+ System.getProperty("line.separator")
				+ "<ScheduleFromStart>1</ScheduleFromStart>"
				+ System.getProperty("line.separator")
				+ "<StartDate>"
				+ f1.format(LoginBean.getProject().getStartDate())
				+ "T"
				+ f2.format(LoginBean.getProject().getStartDate())
				+ "</StartDate>"
				+ System.getProperty("line.separator")
				+ " <FinishDate>"
				+ System.getProperty("line.separator")
				+ f1.format(LoginBean.getProject().getEndDate())
				+ "T"
				+ f2.format(LoginBean.getProject().getEndDate())
				+ "</FinishDate>"
				+ System.getProperty("line.separator")
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
				+ System.getProperty("line.separator")
				+ "<CurrentDate>"
				+ f1.format(new Date())
				+ "T"
				+ f2.format(new Date())
				+ "</CurrentDate>"
				+ System.getProperty("line.separator")
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
					+ f2.format(tt.getTask().getStartDatePlanned())
					+ "</Start>" + System.getProperty("line.separator")
					+ "<Finish>" + f1.format(tt.getTask().getEndDatePlanned())
					+ "T" + f2.format(tt.getTask().getEndDatePlanned())
					+ "</Finish>" + System.getProperty("line.separator")
					+ "<DurationFormat>7</DurationFormat>"
					+ System.getProperty("line.separator") + "</Task>";

		}
		buffer = buffer + System.getProperty("line.separator") + "</Tasks>"
				+ System.getProperty("line.separator") + "<Resources/>"
				+ System.getProperty("line.separator") + "<Assignments/>"
				+ System.getProperty("line.separator") + "</Project>";
		InputStream stream = new ByteArrayInputStream(buffer.getBytes());
		return new DefaultStreamedContent(stream, "xml", LoginBean.getProject()
				.getName().toUpperCase()
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
					&& !(events.get(i).getStyleClass().equalsIgnoreCase(Real) ^ real)
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
				// model.addAll(events,TimelineUpdater.getCurrentInstance(":projecttabview:planningForm:timeline"));
				// model.add(event,TimelineUpdater.getCurrentInstance(":projecttabview:planningForm:timeline"));
			} else {
				int j1 = -1;
				int i1 = 0;
				while (i1 < events.size()) {

					if (events.get(i1).getData() instanceof JJChapter
							&& (events.get(i1).getStyleClass()
									.equalsIgnoreCase("invisible"))) {

						if (chapter.equals((JJChapter) model.getEvents()
								.get(i1).getData())) {
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
		oldCopyObjects = false;
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

		if (group.equalsIgnoreCase(Real)) {

			tt.setStartDateReal(ev.getTimelineEvent().getStartDate());
			tt.setEndDateReal(ev.getTimelineEvent().getEndDate());
			saveJJTask(tt, true);
			tt = jJTaskService.findJJTask(tt.getId());
			updateView(tt, UPDATE_OPERATION);

		} else {

			tt.setStartDateRevised(ev.getTimelineEvent().getStartDate());
			tt.setEndDateRevised(ev.getTimelineEvent().getEndDate());
			saveJJTask(tt, true);
			tt = jJTaskService.findJJTask(tt.getId());
			updateView(tt, UPDATE_OPERATION);

		}

		if (tt.getSprint() != null) {

			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);
			JJSprintBean jJSprintBean = (JJSprintBean) session
					.getAttribute("jJSprintBean");
			if (jJSprintBean.contains(tt.getSprint().getId()) != -1) {
				SprintUtil s = SprintUtil.getSprintUtil(tt.getSprint().getId(),
						jJSprintBean.getSprintList());
				if (s != null) {
					s = new SprintUtil(jJSprintService.findJJSprint(tt
							.getSprint().getId()),
							jJTaskService.getSprintTasks(jJSprintService
									.findJJSprint(tt.getSprint().getId()),
									LoginBean.getProduct()), jJContactService);
					jJSprintBean.getSprintList().set(
							jJSprintBean.contains(s.getSprint().getId()), s);
				}
			}

		}

		reset();
		String message = "";
		FacesMessage facesMessage = null;

		message = "Success Update " + group + " Date";
		facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, message,
				"Task");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
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

				TaskData tskst = new TaskData(tt,
						tasksData.get(i).getChapter(), startDate, endDate,
						workLoad, tt.getStartDateRevised() != null);

				tasksData.set(i, tskst);
			} else
				tasksData.remove(i);

		} else if (operation.equalsIgnoreCase(ADD_OPERATION)
				&& tasksData != null) {
			if (sortMode == null || sortMode.equalsIgnoreCase("chapter")) {
				if (tt.getChapter() != null
						&& (this.sprint == null || (this.sprint.equals(tt
								.getSprint())))) {
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
			if (sortMode.equalsIgnoreCase("chapter") && tt.getChapter() != null) {
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
				events.add(new TimelineEvent(tt, new GregorianCalendar(Calendar
						.getInstance().get(Calendar.YEAR) - 5, 1, 1).getTime(),
						new GregorianCalendar(Calendar.getInstance().get(
								Calendar.YEAR) + 5, 1, 1).getTime(), false,
						group, "invisible"));

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
					TimelineEvent event = new TimelineEvent(chapter, start,
							end, false, group, "chapter");
					events.add(new TimelineEvent(chapter,
							new GregorianCalendar(Calendar.getInstance().get(
									Calendar.YEAR) - 5, 1, 1).getTime(),
							new GregorianCalendar(Calendar.getInstance().get(
									Calendar.YEAR) + 5, 1, 1).getTime(), false,
							group, "invisible"));

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

				events.add(new TimelineEvent(tt, new GregorianCalendar(Calendar
						.getInstance().get(Calendar.YEAR) - 5, 1, 1).getTime(),
						new GregorianCalendar(Calendar.getInstance().get(
								Calendar.YEAR) + 5, 1, 1).getTime(), false,
						group, "invisible"));

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

			return ((JJChapter) taskData1).getCreationDate().compareTo(
					((JJChapter) taskData2).getCreationDate());

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
		panes.addOption("size", 800);
		panes.addOption("maxSize", 1200);
		panes.addOption("minsize", 300);
		layoutOptionsOne.setWestOptions(panes);
		panes = new LayoutOptions();
		panes.addOption("size", 500);
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
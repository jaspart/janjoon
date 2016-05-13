package com.starit.janjoonweb.ui.mb;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.mutable.MutableInt;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.DragDropEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJBugService;
import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJCategoryService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJContactService;
import com.starit.janjoonweb.domain.JJPermissionService;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJSprint;
import com.starit.janjoonweb.domain.JJSprintService;
import com.starit.janjoonweb.domain.JJStatus;
import com.starit.janjoonweb.domain.JJStatusService;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJTaskService;
import com.starit.janjoonweb.domain.JJTestcase;
import com.starit.janjoonweb.ui.mb.util.ContactCalendarUtil;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;
import com.starit.janjoonweb.ui.mb.util.PlanningConfiguration;
import com.starit.janjoonweb.ui.mb.util.SimulatorUtil;
import com.starit.janjoonweb.ui.mb.util.SprintUtil;

@RooJsfManagedBean(entity = JJSprint.class, beanName = "jJSprintBean")
public class JJSprintBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private JJTaskService jJTaskService;

	@Autowired
	private JJPermissionService jJPermissionService;

	@Autowired
	private JJCategoryService jJCategoryService;

	@Autowired
	private JJStatusService jJStatusService;

	@Autowired
	private JJBugService jJBugService;

	public void setjJBugService(JJBugService jJBugService) {
		this.jJBugService = jJBugService;
	}

	private List<JJCategory> categoryList;
	private List<JJContact> contacts;
	private SimulatorUtil simulator;
	private JJCategory category;
	private JJBug bug;
	private JJProject project;
	private List<SprintUtil> sprintList;
	private SprintUtil sprintUtil;
	private JJTask task;
	// private boolean update = true;
	private int activeTabSprintIndex;
	private int activeTabGantIndex;

	public int getActiveTabSprintIndex() {
		return activeTabSprintIndex;
	}

	public void setActiveTabSprintIndex(int activeTabSprintIndex) {
		this.activeTabSprintIndex = activeTabSprintIndex;
	}

	public int getActiveTabGantIndex() {
		return activeTabGantIndex;
	}

	public void setActiveTabGantIndex(int activeTabGantIndex) {
		this.activeTabGantIndex = activeTabGantIndex;
	}

	public JJTaskBean getJJTaskBean() {
		JJTaskBean jJTaskBean = ((JJTaskBean) LoginBean.findBean("jJTaskBean"));
		if (jJTaskBean == null)
			jJTaskBean = new JJTaskBean();
		return jJTaskBean;
	}

	public void setjJTaskService(JJTaskService jJTaskService) {
		this.jJTaskService = jJTaskService;
	}

	public void setjJPermissionService(
			JJPermissionService jJPermissionService) {
		this.jJPermissionService = jJPermissionService;
	}

	public void setjJCategoryService(JJCategoryService jJCategoryService) {
		this.jJCategoryService = jJCategoryService;
	}

	public void setjJStatusService(JJStatusService jJStatusService) {
		this.jJStatusService = jJStatusService;
	}

	public JJSprintService getJJSprintService() {
		return jJSprintService;
	}

	public JJContactService getJJContactService() {
		return jJContactService;
	}

	public List<JJCategory> getCategoryList() {

		if (categoryList == null)
			categoryList = jJCategoryService.getCategories(null, false, true,
					false, LoginBean.getCompany());

		return categoryList;
	}

	public void setCategoryList(List<JJCategory> categoryList) {
		this.categoryList = categoryList;
	}

	public JJCategory getCategory() {
		return category;
	}

	public void setCategory(JJCategory category) {
		this.category = category;
	}

	public JJBug getBug() {
		return bug;
	}

	public void setBug(JJBug bug) {
		this.bug = bug;
	}

	public JJProject getProject() {
		return project;
	}

	public void setProject(JJProject project) {
		this.project = project;
	}

	// public boolean isDisableDragDrop(SprintUtil jJSprint) {
	// JJContact loginContact = ((LoginBean) LoginBean.findBean("loginBean"))
	// .getContact();
	// return !jJSprint.getContacts().contains(loginContact);
	//
	// }

	public JJTask getTask() {
		if (task == null) {
			task = new JJTask();
			task.setWorkloadPlanned(4);
		}
		return task;
	}

	public void setTask(JJTask task) {

		this.task = task;
	}

	public void loadingsprintPage(ComponentSystemEvent e) {
		category = null;
	}

	public List<SprintUtil> getSprintList() {

		if (LoginBean.getProject() == null) {

			if (sprintList == null) {
				project = null;
				initJJSprintPage(null);
			} else if (project != null) {

				project = null;
				initJJSprintPage(null);
			}

		} else if (project != null) {

			if (!project.equals(LoginBean.getProject())) {
				project = LoginBean.getProject();
				initJJSprintPage(project);
			}
		} else {
			project = LoginBean.getProject();
			initJJSprintPage(project);
		}

		return sprintList;
	}

	public void setSprintList(List<SprintUtil> sprintList) {
		this.sprintList = sprintList;
	}

	public void iniSprintChart() {
		if (LoginBean.getProject() != null)
			getSprintList();
	}

	public SprintUtil getSprintUtil() {
		return sprintUtil;
	}

	public void setSprintUtil(SprintUtil sprintUtil) {
		this.sprintUtil = sprintUtil;
	}

	public List<JJContact> getContacts() {

		if (contacts == null) {

			contacts = jJPermissionService.areAuthorized(LoginBean.getCompany(),
					null, LoginBean.getProject(), LoginBean.getProduct(),
					"sprintContact", null, true, null, true);
		}
		return contacts;
	}

	public void setContacts(List<JJContact> contacts) {
		this.contacts = contacts;
	}

	// public boolean isUpdate() {
	// return update;
	// }
	//
	// public void setUpdate(boolean update) {
	// this.update = update;
	// }

	public SimulatorUtil getSimulator() {

		LoginBean loginBean = ((LoginBean) LoginBean.findBean("loginBean"));
		if (simulator == null && loginBean.getPlanningConfiguration()
				.getSimultor_Tab().equals(getActiveTabGantIndex()))
		// if (simulator == null)
		{
			JJRequirementBean jjRequirementBean = (JJRequirementBean) LoginBean
					.findBean("jJRequirementBean");
			JJTestcaseBean jjTestcaseBean = (JJTestcaseBean) LoginBean
					.findBean("jJTestcaseBean");
			if (jjTestcaseBean == null)
				jjTestcaseBean = new JJTestcaseBean();
			if (jjRequirementBean == null)
				jjRequirementBean = new JJRequirementBean();
			JJCategory cat = jJCategoryService.getCategory("BUSINESS",
					LoginBean.getCompany(), true);
			simulator = new SimulatorUtil(
					jjRequirementBean.jJRequirementService,
					jjTestcaseBean.jJTestcaseService, jJBugService, cat);
		}

		return simulator;
	}

	public void setSimulator(SimulatorUtil simulator) {
		this.simulator = simulator;
	}

	public void attrListener(ActionEvent event) {

		sprintUtil = (SprintUtil) event.getComponent().getAttributes()
				.get("sprintUtilValue");
		System.out.println("SPRINT  :" + sprintUtil.getSprint().getName());
		String create = (String) event.getComponent().getAttributes()
				.get("create");
		if (create != null)
			if (create.equalsIgnoreCase("create"))
				RequestContext.getCurrentInstance().execute("createSprint()");
			else if (create.equalsIgnoreCase("edit"))
				RequestContext.getCurrentInstance().execute("editSprint()");
	}

	public void calclutateSimulatorWorkload(NodeSelectEvent select) {

		TreeNode node = select.getTreeNode();
		if (node.getChildren().isEmpty()) {
			if (node.getData() instanceof JJRequirement
					&& ((JJRequirement) node.getData()).getWorkload() != null)
				simulator.setWorkloadAccumulated(simulator
						.getWorkloadAccumulated()
						+ ((JJRequirement) node.getData()).getWorkload());
			else if (node.getData() instanceof JJBug
					&& ((JJBug) node.getData()).getWorkload() != null)
				simulator.setWorkloadAccumulated(
						simulator.getWorkloadAccumulated()
								+ ((JJBug) node.getData()).getWorkload());
			else if (node.getData() instanceof JJTestcase
					&& ((JJTestcase) node.getData()).getWorkload() != null)
				simulator.setWorkloadAccumulated(
						simulator.getWorkloadAccumulated()
								+ ((JJTestcase) node.getData()).getWorkload());
		} else {
			for (TreeNode child : node.getChildren()) {
				calclutateSimulatorWorkload(child, true);
			}
		}
	}

	private void calclutateSimulatorWorkload(TreeNode node, boolean b) {

		if (node.getChildren().isEmpty()) {
			if (b) {
				if (node.getData() instanceof JJRequirement
						&& ((JJRequirement) node.getData())
								.getWorkload() != null)
					simulator.setWorkloadAccumulated(simulator
							.getWorkloadAccumulated()
							+ ((JJRequirement) node.getData()).getWorkload());
				else if (node.getData() instanceof JJBug
						&& ((JJBug) node.getData()).getWorkload() != null)
					simulator.setWorkloadAccumulated(
							simulator.getWorkloadAccumulated()
									+ ((JJBug) node.getData()).getWorkload());
				else if (node.getData() instanceof JJTestcase
						&& ((JJTestcase) node.getData()).getWorkload() != null)
					simulator.setWorkloadAccumulated(simulator
							.getWorkloadAccumulated()
							+ ((JJTestcase) node.getData()).getWorkload());
			} else {
				if (node.getData() instanceof JJRequirement
						&& ((JJRequirement) node.getData())
								.getWorkload() != null)
					simulator.setWorkloadAccumulated(simulator
							.getWorkloadAccumulated()
							- ((JJRequirement) node.getData()).getWorkload());
				else if (node.getData() instanceof JJBug
						&& ((JJBug) node.getData()).getWorkload() != null)
					simulator.setWorkloadAccumulated(
							simulator.getWorkloadAccumulated()
									- ((JJBug) node.getData()).getWorkload());
				else if (node.getData() instanceof JJTestcase
						&& ((JJTestcase) node.getData()).getWorkload() != null)
					simulator.setWorkloadAccumulated(simulator
							.getWorkloadAccumulated()
							- ((JJTestcase) node.getData()).getWorkload());
			}

		} else {
			for (TreeNode child : node.getChildren()) {
				calclutateSimulatorWorkload(child, b);
			}
		}

	}

	public void calclutateSimulatorWorkload(NodeUnselectEvent unselect) {

		TreeNode node = unselect.getTreeNode();
		if (node.getChildren().isEmpty()) {
			if (node.getData() instanceof JJRequirement
					&& ((JJRequirement) node.getData()).getWorkload() != null)
				simulator.setWorkloadAccumulated(simulator
						.getWorkloadAccumulated()
						- ((JJRequirement) node.getData()).getWorkload());
			else if (node.getData() instanceof JJBug
					&& ((JJBug) node.getData()).getWorkload() != null)
				simulator.setWorkloadAccumulated(
						simulator.getWorkloadAccumulated()
								- ((JJBug) node.getData()).getWorkload());
			else if (node.getData() instanceof JJTestcase
					&& ((JJTestcase) node.getData()).getWorkload() != null)
				simulator.setWorkloadAccumulated(
						simulator.getWorkloadAccumulated()
								- ((JJTestcase) node.getData()).getWorkload());
		} else {
			for (TreeNode child : node.getChildren()) {
				calclutateSimulatorWorkload(child, false);
			}
		}
	}

	public void updateView() {
		JJRequirementBean jjRequirementBean = (JJRequirementBean) LoginBean
				.findBean("jJRequirementBean");
		JJTestcaseBean jjTestcaseBean = (JJTestcaseBean) LoginBean
				.findBean("jJTestcaseBean");
		if (jjTestcaseBean == null)
			jjTestcaseBean = new JJTestcaseBean();
		if (jjRequirementBean == null)
			jjRequirementBean = new JJRequirementBean();
		JJCategory cat = jJCategoryService.getCategory("BUSINESS",
				LoginBean.getCompany(), true);

		simulator = new SimulatorUtil(jjRequirementBean.jJRequirementService,
				jjTestcaseBean.jJTestcaseService, jJBugService, cat,
				simulator.isDisplayProgress(), simulator.isDisplayDone(),
				simulator.isDisplayFinishedBug());
	}

	public void onCellEditTask(CellEditEvent event) {

		DataTable dataTable = (DataTable) event.getSource();
		JJTask t = (JJTask) dataTable.getRowData();
		t.setAssignedTo((JJContact) event.getNewValue());
		getJJTaskBean().saveJJTask(t, true, new MutableInt(0));
		sprintUtil = SprintUtil.getSprintUtil(t.getSprint().getId(),
				sprintList);
		sprintUtil.setSprint(
				jJSprintService.findJJSprint(sprintUtil.getSprint().getId()));
		sprintUtil = new SprintUtil(sprintUtil.getSprint(),
				jJTaskService.getSprintTasks(sprintUtil.getSprint(),
						LoginBean.getProduct()),
				jJContactService, jJTaskService);
		sprintList.set(contains(sprintUtil.getSprint().getId()), sprintUtil);
		String message = "message_successfully_updated";
		FacesMessage facesMessage = MessageFactory.getMessage(message,
				MessageFactory.getMessage("label_task", "").getDetail(), "e");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}

	public void initJJSprintPage(JJProject pr) {

		sprintList = SprintUtil.generateSprintUtilList(
				jJSprintService.getSprints(pr, true), jJTaskService,
				jJContactService);

		if (sprintList == null)
			sprintList = new ArrayList<SprintUtil>();
		else {
			activeTabSprintIndex = -1;
			Date now = new Date();
			int i = 0;
			while (i < sprintList.size()) {
				if (now.after(sprintList.get(i).getSprint().getStartDate())
						&& now.before(
								sprintList.get(i).getSprint().getEndDate())) {
					activeTabSprintIndex = i;
					i = sprintList.size();
				}
				i++;
			}

			if (activeTabSprintIndex == -1) {
				long minDiff = -1, currentTime = new Date().getTime();
				SprintUtil minDate = null;
				for (SprintUtil s : sprintList) {
					long diff = Math.abs(
							currentTime - s.getSprint().getEndDate().getTime());
					if ((minDiff == -1) || (diff < minDiff)) {
						minDiff = diff;
						minDate = s;
					}
				}
				if (minDate != null)
					activeTabSprintIndex = sprintList.indexOf(minDate);
				else
					activeTabSprintIndex = 0;
			}
		}

		JJSprint sp = new JJSprint();
		sp.setProject(pr);
		sprintList
				.add(new SprintUtil(sp, null, jJContactService, jJTaskService));

	}

	public void editSprint() {

		sprintUtil.getSprint()
				.setContacts(new HashSet<JJContact>(sprintUtil.getContacts()));

		updateJJSprint(sprintUtil.getSprint());
		sprintUtil.setSprint(
				jJSprintService.findJJSprint(sprintUtil.getSprint().getId()));
		sprintUtil = new SprintUtil(sprintUtil.getSprint(),
				jJTaskService.getSprintTasks(sprintUtil.getSprint(),
						LoginBean.getProduct()),
				jJContactService, jJTaskService);
		sprintList.set(contains(sprintUtil.getSprint().getId()), sprintUtil);
		getJJTaskBean().onSprintUpdate(sprintUtil.getSprint());

		String message = "message_successfully_updated";
		FacesMessage facesMessage = MessageFactory.getMessage(message, "Sprint",
				"");
		getJJTaskBean().setSprints(null);
		getJJTaskBean().setSprint(null);
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}

	public void onTabSprintChange() {

		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, String> paramMap = context.getExternalContext()
				.getRequestParameterMap();
		if (paramMap.get("activeScrumIndex") != null) {
			String paramIndex = paramMap.get("activeScrumIndex");
			setActiveTabSprintIndex(Integer.valueOf(paramIndex));
		} else if (paramMap.get("activeGantIndex") != null) {
			String paramIndex = paramMap.get("activeGantIndex");
			setActiveTabGantIndex(Integer.valueOf(paramIndex));

			if (activeTabGantIndex == PlanningConfiguration.getSrumIndex()) {
				getJJTaskBean().setMode("scrum");
			} else if (activeTabGantIndex == PlanningConfiguration
					.getGanttIndex())
				getJJTaskBean().setMode("planning");
		}
	}

	public void deleteSprint(SprintUtil sp) {

		JJSprint spr = sp.getSprint();
		spr.setEnabled(false);

		updateJJSprint(spr);
		sprintList.remove(contains(spr.getId()));

		FacesMessage facesMessage = MessageFactory
				.getMessage("message_successfully_deleted", "Sprint", "");

		getJJTaskBean().setSprints(null);
		getJJTaskBean().setSprint(null);
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}

	public void saveSimulator() {

		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();

		JJSprint sprint = new JJSprint();

		sprint.setName(simulator.getName());
		sprint.setStartDate(simulator.getStartDate());
		sprint.setEndDate(simulator.getEndDate());
		sprint.setProject(LoginBean.getProject());
		sprint.setEnabled(true);
		sprint.setDescription(sprint.getName() + " /CreatedBy:"
				+ contact.getName() + " at :" + new Date());

		saveJJSprint(sprint);

		ContactCalendarUtil calendarUtil = new ContactCalendarUtil(
				LoginBean.getCompany());
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH");

		for (TreeNode node : simulator.getSelectedObjects()) {
			String name = "";
			if (node.getChildren().isEmpty()) {
				JJTask task = new JJTask();
				task.setEnabled(true);
				task.setSprint(sprint);
				task.setStartDatePlanned(
						calendarUtil.nextWorkingDate(sprint.getStartDate()));
				task.setCreationDate(new Date());

				task.setCreatedBy(((LoginBean) LoginBean.findBean("loginBean"))
						.getContact());

				task.setStatus(
						jJStatusService.getOneStatus("TODO", "Task", true));
				task.setEnabled(true);
				task.setSprint(sprint);

				if (node.getData() instanceof JJRequirement) {

					JJRequirement requirement = (JJRequirement) node.getData();
					name = requirement.getName() + " (" + df.format(new Date())
							+ "h)";

					task.setRequirement(requirement);
					task.setWorkloadPlanned(requirement.getWorkload() != null
							? requirement.getWorkload()
							: 3);
					calendarUtil.getEndDate(task, JJTaskBean.Planned,
							jJTaskService);

				} else if (node.getData() instanceof JJBug) {

					JJBug bug = (JJBug) node.getData();
					name = bug.getName() + " (" + df.format(new Date()) + "h)";

					task.setBug(bug);

					task.setWorkloadPlanned(
							bug.getWorkload() != null ? bug.getWorkload() : 3);
					calendarUtil.getEndDate(task, JJTaskBean.Planned,
							jJTaskService);

				} else if (node.getData() instanceof JJTestcase) {

					JJTestcase testcase = (JJTestcase) node.getData();

					name = testcase.getName() + " (" + df.format(new Date())
							+ "h)";
					task.setTestcase(testcase);

					task.setWorkloadPlanned(testcase.getWorkload() != null
							? testcase.getWorkload()
							: 3);
					calendarUtil.getEndDate(task, JJTaskBean.Planned,
							jJTaskService);
				}

				if (name.length() > 100) {
					name = name.substring(0, 99);
				}

				task.setName(name);
				task.setDescription("This is task " + task.getName());

				getJJTaskBean().saveJJTask(task, false, new MutableInt(0));
				getJJTaskBean().updateView(task, JJTaskBean.ADD_OPERATION);

			}
		}

		if (sprintList != null) {
			sprintUtil = new SprintUtil(sprint,
					jJTaskService.getSprintTasks(sprint,
							LoginBean.getProduct()),
					jJContactService, jJTaskService);

			sprintList.set(sprintList.size() - 1, sprintUtil);

			JJSprint sp = new JJSprint();
			sp.setProject(LoginBean.getProject());
			sprintList.add(
					new SprintUtil(sp, null, jJContactService, jJTaskService));
		}

		category = null;
		categoryList = null;

		getJJTaskBean().setSprints(null);
		getJJTaskBean().setSprint(null);

		RequestContext.getCurrentInstance()
				.execute("PF('sprintDialogWidget').hide()");
		RequestContext.getCurrentInstance().execute("hideSimulationDialog()");

	}

	public void resetSimulation() {
		simulator = null;

		if (sprintList != null) {
			activeTabGantIndex = PlanningConfiguration.getSrumIndex();
			activeTabSprintIndex = sprintList.size() - 2;
		}

		String message = "message_successfully_created";
		FacesMessage facesMessage = MessageFactory.getMessage(message, "Sprint",
				"");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}

	public void createSprint() {

		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();

		setJJSprint_(sprintUtil.getSprint());

		getJJSprint_().setEnabled(true);
		getJJSprint_().setDescription(getJJSprint_().getName() + " /CreatedBy:"
				+ contact.getName() + " at :" + new Date());
		saveJJSprint(getJJSprint_());
		for (JJContact c : getJJSprint_().getContacts()) {
			c.getSprints().add(getJJSprint_());
		}
		String message = "message_successfully_created";
		FacesMessage facesMessage = MessageFactory.getMessage(message, "Sprint",
				"");

		sprintUtil.setSprint(getJJSprint_());
		sprintUtil.setNeditabale(true);
		getJJTaskBean().setSprints(null);
		getJJTaskBean().setSprint(null);
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}

	public void reset() {

		if (getJJSprint_() != null) {
			if (getJJSprint_().getProject() != null && project != null) {
				if (getJJSprint_().getProject().equals(project)) {
					project = null;
				}
			}
		}
		sprintUtil = null;
		category = null;
		setJJSprint_(null);
		setSelectedBuilds(null);
		setSelectedObstacles(null);
		setCreateDialogVisible(false);
		setSelectedContacts(null);

	}

	public void addTaskToTodo(DragDropEvent ddevent) {

		JJTask dropedTask = (JJTask) ddevent.getData();
		Long sprintId = dropedTask.getSprint().getId();

		if (ddevent.getDragId().contains(":progIcon")
				|| ddevent.getDragId().contains(":progPanel")
				|| ddevent.getDragId().contains(":doneIcon")
				|| ddevent.getDragId().contains(":donePanel")) {

			JJStatus status = jJStatusService.getOneStatus("TODO", "Task",
					true);

			dropedTask.setStatus(status);
			dropedTask.setStartDateReal(null);
			dropedTask.setEndDateReal(null);
			dropedTask.setWorkloadReal(null);
			dropedTask.setAssignedTo(null);
			dropedTask.setCompleted(false);
			getJJTaskBean().saveJJTask(dropedTask, true, new MutableInt(0));
			// resetJJTaskBean();
			getJJTaskBean().updateView(dropedTask, JJTaskBean.UPDATE_OPERATION);

			JJSprint s = jJSprintService.findJJSprint(sprintId);
			sprintUtil = new SprintUtil(s,
					jJTaskService.getSprintTasks(s, LoginBean.getProduct()),
					jJContactService, jJTaskService);

			sprintList.set(contains(sprintUtil.getSprint().getId()),
					sprintUtil);
			String message = "message_successfully_updated";
			FacesMessage facesMessage = MessageFactory.getMessage(message,
					MessageFactory.getMessage("label_task", "").getDetail(),
					"e");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}

		else {

			FacesMessage facesMessage = new FacesMessage(
					FacesMessage.SEVERITY_WARN, "non autorisée", "");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}
	}

	public void addTaskToProg(DragDropEvent ddevent) {

		JJTask dropedTask = (JJTask) ddevent.getData();
		Long sprintId = dropedTask.getSprint().getId();

		if (ddevent.getDragId().contains(":todoIcon")
				|| ddevent.getDragId().contains(":todoPanel")) {
			JJContact assignedTo = ((LoginBean) LoginBean.findBean("loginBean"))
					.getContact();
			JJStatus status = jJStatusService.getOneStatus("IN PROGRESS",
					"Task", true);

			dropedTask.setStatus(status);
			dropedTask.setStartDateReal(new Date());
			dropedTask.setAssignedTo(assignedTo);
			dropedTask.setCompleted(false);
			getJJTaskBean().saveJJTask(dropedTask, true, new MutableInt(0));
			// resetJJTaskBean();
			getJJTaskBean().updateView(dropedTask, JJTaskBean.UPDATE_OPERATION);

			JJSprint s = jJSprintService.findJJSprint(sprintId);
			sprintUtil = new SprintUtil(s,
					jJTaskService.getSprintTasks(s, LoginBean.getProduct()),
					jJContactService, jJTaskService);

			sprintList.set(contains(sprintUtil.getSprint().getId()),
					sprintUtil);
			String message = "message_successfully_updated";
			FacesMessage facesMessage = MessageFactory.getMessage(message,
					MessageFactory.getMessage("label_task", "").getDetail(),
					"e");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		} else if (ddevent.getDragId().contains(":doneIcon")
				|| ddevent.getDragId().contains(":donePanel")) {

			JJStatus status = jJStatusService.getOneStatus("IN PROGRESS",
					"Task", true);

			dropedTask.setStatus(status);
			dropedTask.setEndDateReal(null);
			dropedTask.setCompleted(false);
			getJJTaskBean().saveJJTask(dropedTask, true, new MutableInt(0));
			// resetJJTaskBean();

			getJJTaskBean().updateView(dropedTask, JJTaskBean.UPDATE_OPERATION);

			JJSprint s = jJSprintService.findJJSprint(sprintId);
			sprintUtil = new SprintUtil(s,
					jJTaskService.getSprintTasks(s, LoginBean.getProduct()),
					jJContactService, jJTaskService);

			sprintList.set(contains(sprintUtil.getSprint().getId()),
					sprintUtil);
			String message = "message_successfully_updated";
			FacesMessage facesMessage = MessageFactory.getMessage(message,
					MessageFactory.getMessage("label_task", "").getDetail(),
					"e");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		} else {

			FacesMessage facesMessage = new FacesMessage(
					FacesMessage.SEVERITY_WARN, "non autorisée", "");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}
	}

	public void addTaskToDone(DragDropEvent ddevent) {

		Long id = null;
		JJTask dropedTask = (JJTask) ddevent.getData();

		Long sprintId = dropedTask.getSprint().getId();

		if (ddevent.getDragId().contains(":progIcon")
				|| ddevent.getDragId().contains(":progPanel")) {

			id = dropedTask.getId();
			JJStatus status = jJStatusService.getOneStatus("DONE", "Task",
					true);

			dropedTask.setEndDateReal(new Date());
			dropedTask.setCompleted(true);
			dropedTask.setStatus(status);
			getJJTaskBean().saveJJTask(dropedTask, true, new MutableInt(0));
			// resetJJTaskBean();
			getJJTaskBean().updateView(dropedTask, JJTaskBean.UPDATE_OPERATION);

			JJSprint s = jJSprintService.findJJSprint(sprintId);
			sprintUtil = new SprintUtil(s,
					jJTaskService.getSprintTasks(s, LoginBean.getProduct()),
					jJContactService, jJTaskService);

			sprintList.set(contains(sprintUtil.getSprint().getId()),
					sprintUtil);

			String message = "message_successfully_updated";
			FacesMessage facesMessage = MessageFactory.getMessage(message,
					MessageFactory.getMessage("label_task", "").getDetail(),
					"e");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		} else {
			FacesMessage facesMessage = new FacesMessage(
					FacesMessage.SEVERITY_WARN, "non autorisée", "");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}
		RequestContext context = RequestContext.getCurrentInstance();

		if (id != null) {
			JJTask t = jJTaskService.findJJTask(id);
			if (t.getBug() != null) {

				bug = t.getBug();
				// context.execute("PF('blockUIWidget').unblock()");
				context.execute("PF('editBugDialogWidget').show()");
			}

		}

	}

	public void addTaskToTodo_newScrum(DragDropEvent ddevent) {

		JJTask dropedTask = (JJTask) ddevent.getData();
		Long sprintId = dropedTask.getSprint().getId();

		if (ddevent.getDragId().contains(":progIcon")
				|| ddevent.getDragId().contains(":progPanel")
				|| ddevent.getDragId().contains(":doneIcon")
				|| ddevent.getDragId().contains(":donePanel")) {

			JJStatus status = jJStatusService.getOneStatus("TODO", "Task",
					true);

			dropedTask.setStatus(status);
			dropedTask.setStartDateReal(null);
			dropedTask.setEndDateReal(null);
			dropedTask.setWorkloadReal(null);
			dropedTask.setAssignedTo(null);
			dropedTask.setCompleted(false);
			getJJTaskBean().saveJJTask(dropedTask, true, new MutableInt(0));
			// resetJJTaskBean();
			getJJTaskBean().updateView(dropedTask, JJTaskBean.UPDATE_OPERATION);

			JJSprint s = jJSprintService.findJJSprint(sprintId);
			sprintUtil = new SprintUtil(s,
					jJTaskService.getSprintTasks(s, LoginBean.getProduct()),
					jJContactService, jJTaskService);

			sprintList.set(contains(sprintUtil.getSprint().getId()),
					sprintUtil);
			String message = "message_successfully_updated";
			FacesMessage facesMessage = MessageFactory.getMessage(message,
					MessageFactory.getMessage("label_task", "").getDetail(),
					"e");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}

		else {

			FacesMessage facesMessage = new FacesMessage(
					FacesMessage.SEVERITY_WARN, "non autorisée", "");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}
	}

	public void addTaskToProg_newScrum(DragDropEvent ddevent) {

		JJTask dropedTask = (JJTask) ddevent.getData();
		Long sprintId = dropedTask.getSprint().getId();

		if (ddevent.getDragId().contains(":todoIcon")
				|| ddevent.getDragId().contains(":todoPanel")) {
			JJContact assignedTo = ((LoginBean) LoginBean.findBean("loginBean"))
					.getContact();
			JJStatus status = jJStatusService.getOneStatus("IN PROGRESS",
					"Task", true);

			dropedTask.setStatus(status);
			dropedTask.setStartDateReal(new Date());
			dropedTask.setAssignedTo(assignedTo);
			dropedTask.setCompleted(false);
			getJJTaskBean().saveJJTask(dropedTask, true, new MutableInt(0));
			// resetJJTaskBean();
			getJJTaskBean().updateView(dropedTask, JJTaskBean.UPDATE_OPERATION);

			JJSprint s = jJSprintService.findJJSprint(sprintId);
			sprintUtil = new SprintUtil(s,
					jJTaskService.getSprintTasks(s, LoginBean.getProduct()),
					jJContactService, jJTaskService);

			sprintList.set(contains(sprintUtil.getSprint().getId()),
					sprintUtil);
			String message = "message_successfully_updated";
			FacesMessage facesMessage = MessageFactory.getMessage(message,
					MessageFactory.getMessage("label_task", "").getDetail(),
					"e");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		} else if (ddevent.getDragId().contains(":doneIcon")
				|| ddevent.getDragId().contains(":donePanel")) {

			JJStatus status = jJStatusService.getOneStatus("IN PROGRESS",
					"Task", true);

			dropedTask.setStatus(status);
			dropedTask.setEndDateReal(null);
			dropedTask.setCompleted(false);
			getJJTaskBean().saveJJTask(dropedTask, true, new MutableInt(0));
			// resetJJTaskBean();

			getJJTaskBean().updateView(dropedTask, JJTaskBean.UPDATE_OPERATION);

			JJSprint s = jJSprintService.findJJSprint(sprintId);
			sprintUtil = new SprintUtil(s,
					jJTaskService.getSprintTasks(s, LoginBean.getProduct()),
					jJContactService, jJTaskService);

			sprintList.set(contains(sprintUtil.getSprint().getId()),
					sprintUtil);
			String message = "message_successfully_updated";
			FacesMessage facesMessage = MessageFactory.getMessage(message,
					MessageFactory.getMessage("label_task", "").getDetail(),
					"e");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		} else {

			FacesMessage facesMessage = new FacesMessage(
					FacesMessage.SEVERITY_WARN, "non autorisée", "");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}
	}

	public void addTaskToDone_newScrum(DragDropEvent ddevent) {

		Long id = null;
		JJTask dropedTask = (JJTask) ddevent.getData();

		Long sprintId = dropedTask.getSprint().getId();

		if (ddevent.getDragId().contains(":progIcon")
				|| ddevent.getDragId().contains(":progPanel")) {

			id = dropedTask.getId();
			JJStatus status = jJStatusService.getOneStatus("DONE", "Task",
					true);

			dropedTask.setEndDateReal(new Date());
			dropedTask.setCompleted(true);
			dropedTask.setStatus(status);
			getJJTaskBean().saveJJTask(dropedTask, true, new MutableInt(0));
			// resetJJTaskBean();
			getJJTaskBean().updateView(dropedTask, JJTaskBean.UPDATE_OPERATION);

			JJSprint s = jJSprintService.findJJSprint(sprintId);
			sprintUtil = new SprintUtil(s,
					jJTaskService.getSprintTasks(s, LoginBean.getProduct()),
					jJContactService, jJTaskService);

			sprintList.set(contains(sprintUtil.getSprint().getId()),
					sprintUtil);

			String message = "message_successfully_updated";
			FacesMessage facesMessage = MessageFactory.getMessage(message,
					MessageFactory.getMessage("label_task", "").getDetail(),
					"e");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		} else {
			FacesMessage facesMessage = new FacesMessage(
					FacesMessage.SEVERITY_WARN, "non autorisée", "");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}
		RequestContext context = RequestContext.getCurrentInstance();

		if (id != null) {
			JJTask t = jJTaskService.findJJTask(id);
			if (t.getBug() != null) {

				bug = t.getBug();
				// context.execute("PF('blockUIWidget').unblock()");
				context.execute("PF('editBugDialogWidget').show()");
			}

		}

	}

	public void doneEvent(ActionEvent e) {

		attrListener(e);
		sprintUtil = new SprintUtil(sprintUtil.getSprint(),
				jJTaskService.getSprintTasks(sprintUtil.getSprint(),
						LoginBean.getProduct()),
				jJContactService, jJTaskService);

		sprintList.set(contains(sprintUtil.getSprint().getId()), sprintUtil);

		JJSprint sp = new JJSprint();
		sp.setProject(LoginBean.getProject());
		sprintList
				.add(new SprintUtil(sp, null, jJContactService, jJTaskService));

		category = null;
		categoryList = null;

	}

	public void doneEvent() {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		// long id = sprintUtil.getSprint().getId();
		session.setAttribute("jJSprintBean", new JJSprintBean());
		// RequestContext context = RequestContext.getCurrentInstance();
		//
		// int i = 0;
		// if (((LoginBean) LoginBean.findBean("loginBean")).isRenderGantt())
		// i = 1;
		// else
		// i = 0;
		// context.execute("PF('projectTabView').select(" + i + ")");
		// update = false;
		// context.execute("PF('SprintTab').select(" + contains(id) + ")");

	}

	public void reloadSprintUtil() {

		if (!sprintUtil.isRender()) {

			System.out.println("!jJSprintBean.getSprintUtil().isRender()");
			System.out.println(sprintUtil.getSprint().getName());
			setSprintUtil(new SprintUtil(
					jJSprintService
							.findJJSprint(sprintUtil.getSprint().getId()),
					jJTaskService.getSprintTasks(
							jJSprintService.findJJSprint(
									sprintUtil.getSprint().getId()),
							LoginBean.getProduct()),
					jJContactService, jJTaskService));
			System.out.println(jJTaskService.getSprintTasks(
					jJSprintService
							.findJJSprint(sprintUtil.getSprint().getId()),
					LoginBean.getProduct()).size());

			sprintList.set(contains(sprintUtil.getSprint().getId()),
					sprintUtil);
			System.out.println(contains(sprintUtil.getSprint().getId()));

		}

		System.out.println("reloadSprintUtil finiched");

	}

	public void deleteTask() {

		System.out.println("deleteTask");
		task = jJTaskService.findJJTask(task.getId());
		task.setEnabled(false);
		getJJTaskBean().saveJJTask(task, false, new MutableInt(0));
		sprintUtil = new SprintUtil(
				jJSprintService.findJJSprint(task.getSprint().getId()),
				jJTaskService.getSprintTasks(
						jJSprintService.findJJSprint(task.getSprint().getId()),
						LoginBean.getProduct()),
				jJContactService, jJTaskService);

		// sprintUtil.setRenderTaskForm(false);
		sprintList.set(contains(sprintUtil.getSprint().getId()), sprintUtil);
		// requirement = null;
		category = null;
		categoryList = null;
		getJJTaskBean().updateView(task, JJTaskBean.DELETE_OPERATION);
		// reqList = null;
		task = null;
		// resetJJTaskBean();

		RequestContext context = RequestContext.getCurrentInstance();

		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_deleted",
				MessageFactory.getMessage("label_task", "").getDetail(), "e");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		// int i = 0;
		// if (((LoginBean) LoginBean.findBean("loginBean")).isRenderGantt())
		// i = 1;
		// else
		// i = 0;
		// context.execute("PF('projectTabView').select(" + i + ")");
		// update = false;
		// context.execute("PF('SprintTab').select("
		// + contains(sprintUtil.getSprint().getId()) + ")");
		//
		context.execute("PF('deleteDialogWidget').hide()");
	}

	public void resetJJTaskBean() {
		getJJTaskBean().setProject(null);
		getJJTaskBean().setTasksData(null);
		getJJTaskBean().setModel(null);
	}

	public int contains(Long id) {
		int i = 0;
		int j = -1;

		if (sprintList != null && id != null && id != 0) {
			while (i < sprintList.size()) {
				if (sprintList.get(i).getSprint() != null
						&& sprintList.get(i).getSprint().getId() != null
						&& sprintList.get(i).getSprint().getId().equals(id)) {
					j = i;
					i = sprintList.size();
				} else
					i++;
			}
		}
		if (id == null || id == 0)
			j = sprintList.size() - 1;

		return j;

	}

	public void handleDialogCloseEvent(CloseEvent event) {
		task = null;
	}

	public List<JJBug> completeBug(String query) {

		List<JJBug> suggestions = new ArrayList<JJBug>();
		for (JJBug jJBug : jJBugService.getBugs(LoginBean.getCompany(), project,
				LoginBean.getProduct(), null, null, true, true)) {
			String jJBugStr = String.valueOf(jJBug.getName());
			if (jJBugStr.toLowerCase().startsWith(query.toLowerCase())) {
				suggestions.add(jJBug);
			}
		}
		return suggestions;
	}

	public List<JJContact> completeAssignedTo(String query) {
		List<JJContact> suggestions = new ArrayList<JJContact>();
		for (JJContact jJContact : sprintUtil.getSprint().getContacts()) {
			String jJBugStr = String.valueOf(
					jJContact.getName() + "Mail: " + jJContact.getEmail());
			if (jJBugStr.toLowerCase().startsWith(query.toLowerCase())) {
				suggestions.add(jJContact);
			}
		}
		return suggestions;
	}

	public void saveJJSprint(JJSprint b) {

		b.setCreationDate(new Date());
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setCreatedBy(contact);
		jJSprintService.saveJJSprint(b);
	}

	public void updateJJSprint(JJSprint b) {
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJSprintService.updateJJSprint(b);
	}

	public void persistBugTask() {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJBugBean jJbugBean = (JJBugBean) session.getAttribute("jJBugBean");
		String message = "";
		if (jJbugBean == null)
			jJbugBean = new JJBugBean();
		jJbugBean.updateJJBug(bug);
		message = "message_successfully_updated";

		FacesMessage facesMessage = MessageFactory.getMessage(message, "Bug",
				"");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		jJbugBean.reset();
	}

}

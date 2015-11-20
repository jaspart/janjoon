package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.http.HttpSession;

import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.message.Message;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.spinner.Spinner;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.DragDropEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJBugService;
import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJCategoryService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJContactService;
import com.starit.janjoonweb.domain.JJPermissionService;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJRequirementService;
import com.starit.janjoonweb.domain.JJSprint;
import com.starit.janjoonweb.domain.JJSprintService;
import com.starit.janjoonweb.domain.JJStatus;
import com.starit.janjoonweb.domain.JJStatusService;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJTaskService;
import com.starit.janjoonweb.ui.mb.converter.JJBugConverter;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;
import com.starit.janjoonweb.ui.mb.util.PlanningConfiguration;
import com.starit.janjoonweb.ui.mb.util.SprintUtil;

@RooSerializable
@RooJsfManagedBean(entity = JJSprint.class, beanName = "jJSprintBean")
public class JJSprintBean {

	@Autowired
	private JJTaskService jJTaskService;

	@Autowired
	private JJPermissionService jJPermissionService;

	private JJTaskBean jJTaskBean;

	@Autowired
	private JJCategoryService jJCategoryService;

	@Autowired
	private JJRequirementService jJRequirementService;

	@Autowired
	private JJStatusService jJStatusService;

	@Autowired
	private JJBugService jJBugService;

	public void setjJBugService(JJBugService jJBugService) {
		this.jJBugService = jJBugService;
	}

	private List<JJCategory> categoryList;
	private List<JJContact> contacts;
	private HtmlPanelGrid createTaskPanelGrid;
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

	public void setjJTaskBean(JJTaskBean jJTaskBean) {
		this.jJTaskBean = jJTaskBean;
	}

	public void setjJTaskService(JJTaskService jJTaskService) {
		this.jJTaskService = jJTaskService;
	}

	public void setjJPermissionService(JJPermissionService jJPermissionService) {
		this.jJPermissionService = jJPermissionService;
	}

	public void setjJCategoryService(JJCategoryService jJCategoryService) {
		this.jJCategoryService = jJCategoryService;
	}

	public void setjJRequirementService(
			JJRequirementService jJRequirementService) {
		this.jJRequirementService = jJRequirementService;
	}

	public void setjJStatusService(JJStatusService jJStatusService) {
		this.jJStatusService = jJStatusService;
	}

	public HtmlPanelGrid getCreateTaskPanelGrid() {

		createTaskPanelGrid = populateCreateTaskPanel();
		return createTaskPanelGrid;
	}

	public void setCreateTaskPanelGrid(HtmlPanelGrid createTaskPanelGrid) {
		this.createTaskPanelGrid = createTaskPanelGrid;
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

	public boolean isDisableDragDrop(SprintUtil jJSprint) {
		JJContact loginContact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		return !jJSprint.getContacts().contains(loginContact);

	}

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

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);

		if (jJTaskBean == null)
			jJTaskBean = (JJTaskBean) session.getAttribute("jJTaskBean");
		if (jJTaskBean == null)
			jJTaskBean = new JJTaskBean();

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

			contacts = jJPermissionService.areAuthorized(
					LoginBean.getCompany(), null, LoginBean.getProject(),
					LoginBean.getProduct(), "sprintContact", null, true, null,
					true);
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

	// public void updatereqPanel() {
	//
	// if (category != null) {
	// reqList = jJRequirementService.getRequirements(
	// ((LoginBean) LoginBean.findBean("loginBean")).getContact()
	// .getCompany(), category, project, null, null, null,
	// null, false, true, true);
	// bugs = null;
	// bug = null;
	// requirement = null;
	// if (!reqList.isEmpty())
	// requirement = reqList.get(0);
	// } else {
	// bugs = jJBugService.getBugs(((LoginBean) LoginBean
	// .findBean("loginBean")).getContact().getCompany(), project,
	// null, null);
	// bug = null;
	// reqList = null;
	// requirement = null;
	//
	// }
	//
	// }

	public void onCellEditTask(CellEditEvent event) {

		DataTable dataTable = (DataTable) event.getSource();
		JJTask t = (JJTask) dataTable.getRowData();
		t.setAssignedTo((JJContact) event.getNewValue());
		jJTaskBean.saveJJTask(t, true);
		sprintUtil = SprintUtil
				.getSprintUtil(t.getSprint().getId(), sprintList);
		sprintUtil.setSprint(jJSprintService.findJJSprint(sprintUtil
				.getSprint().getId()));
		sprintUtil = new SprintUtil(sprintUtil.getSprint(),
				jJTaskService.getSprintTasks(sprintUtil.getSprint(),
						LoginBean.getProduct()), jJContactService);
		sprintList.set(contains(sprintUtil.getSprint().getId()), sprintUtil);
		String message = "message_successfully_updated";
		FacesMessage facesMessage = MessageFactory.getMessage(message,
				MessageFactory.getMessage("label_task", "").getDetail());
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
						&& now.before(sprintList.get(i).getSprint()
								.getEndDate())) {
					activeTabSprintIndex = i;
					i = sprintList.size();
				}
				i++;
			}

			if (activeTabSprintIndex == -1) {
				long minDiff = -1, currentTime = new Date().getTime();
				SprintUtil minDate = null;
				for (SprintUtil s : sprintList) {
					long diff = Math.abs(currentTime
							- s.getSprint().getEndDate().getTime());
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
		sprintList.add(new SprintUtil(sp, null, jJContactService));

	}

	public void editSprint() {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);

		sprintUtil.getSprint().setContacts(
				new HashSet<JJContact>(sprintUtil.getContacts()));

		updateJJSprint(sprintUtil.getSprint());
		sprintUtil.setSprint(jJSprintService.findJJSprint(sprintUtil
				.getSprint().getId()));
		sprintUtil = new SprintUtil(sprintUtil.getSprint(),
				jJTaskService.getSprintTasks(sprintUtil.getSprint(),
						LoginBean.getProduct()), jJContactService);
		sprintList.set(contains(sprintUtil.getSprint().getId()), sprintUtil);
		JJTaskBean jjTaskBean = (JJTaskBean) session.getAttribute("jJTaskBean");
		if (jJTaskBean == null)
			jJTaskBean = new JJTaskBean();
		jjTaskBean.onSprintUpdate(sprintUtil.getSprint());

		String message = "message_successfully_updated";
		FacesMessage facesMessage = MessageFactory
				.getMessage(message, "Sprint");
		jJTaskBean.setSprints(null);
		jJTaskBean.setSprint(null);
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}

	public void onTabSprintChange() {

		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, String> paramMap = context.getExternalContext()
				.getRequestParameterMap();
		if (paramMap.get("activeScrumIndex") != null) {
			String paramIndex = paramMap.get("activeScrumIndex");
			setActiveTabSprintIndex(Integer.valueOf(paramIndex));
			System.out.println("###### ACtive tab: " + activeTabSprintIndex);
			SprintUtil su = sprintList.get(activeTabSprintIndex);

			if (!su.isRender()) {
				System.err.println("###### ACtive activeTabSprintIndex: "
						+ activeTabSprintIndex);
			}
		} else if (paramMap.get("activeGantIndex") != null) {
			String paramIndex = paramMap.get("activeGantIndex");
			setActiveTabGantIndex(Integer.valueOf(paramIndex));

			if (activeTabGantIndex == PlanningConfiguration.getSrumIndex()) {
				SprintUtil su = sprintList.get(activeTabSprintIndex);

				if (!su.isRender()) {
					System.err.println("###### ACtive activeTabSprintIndex: "
							+ activeTabSprintIndex);
				}

				((JJTaskBean) LoginBean.findBean("jJTaskBean"))
						.setMode("scrum");
			} else
				((JJTaskBean) LoginBean.findBean("jJTaskBean"))
						.setMode("planning");
			// if (update && activeTabGantIndex ==
			// PlanningConfiguration.getSrumIndex()) {
			// RequestContext.getCurrentInstance()
			// .execute("updateTabSprint()");
			// update = false;
			// }else if(activeTabGantIndex ==
			// PlanningConfiguration.getGanttIndex())
			// update = true;
			System.err.println("###### ACtive activeTabGantIndex: "
					+ activeTabGantIndex);

		}
	}

	// public void onTabSprintChange(AjaxBehaviorEvent e) {
	//
	// TabChangeEvent event = (TabChangeEvent) e;
	//
	// if (event.getData() instanceof SprintUtil) {
	// SprintUtil su = (SprintUtil) event.getData();
	// if (!su.isRender()) {
	// JJSprint s = jJSprintService.findJJSprint(su.getSprint()
	// .getId());
	// sprintUtil = new SprintUtil(s, jJTaskService.getSprintTasks(s,
	// LoginBean.getProduct()), jJContactService);
	// sprintList.set(contains(su.getSprint().getId()), sprintUtil);
	// TabView tv = (TabView) event.getComponent();
	// System.out.println(this.activeTabSprintIndex);
	// this.activeTabSprintIndex = tv.getChildren().indexOf(
	// event.getTab());
	// System.out
	// .println("###### ACtive tab: " + activeTabSprintIndex);
	//
	// }
	// } else {
	// TabView tv = (TabView) event.getComponent();
	// if (tv.getChildren().indexOf(event.getTab()) == 1)
	// ((JJTaskBean) LoginBean.findBean("jJTaskBean"))
	// .setMode("scrum");
	// else
	// ((JJTaskBean) LoginBean.findBean("jJTaskBean"))
	// .setMode("planning");
	//
	// if (update && tv.getChildren().indexOf(event.getTab()) == 1)
	// RequestContext.getCurrentInstance()
	// .execute("updateTabSprint()");
	// update = true;
	// }
	//
	// }

	public void createSprint() {

		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();

		setJJSprint_(sprintUtil.getSprint());

		getJJSprint_().setEnabled(true);
		getJJSprint_().setDescription(
				getJJSprint_().getName() + " /CreatedBy:" + contact.getName()
						+ " at :" + new Date());
		saveJJSprint(getJJSprint_());
		for (JJContact c : getJJSprint_().getContacts()) {
			c.getSprints().add(getJJSprint_());
		}
		String message = "message_successfully_created";
		FacesMessage facesMessage = MessageFactory
				.getMessage(message, "Sprint");

		sprintUtil.setSprint(getJJSprint_());
		sprintUtil.setNeditabale(true);
		jJTaskBean.setSprints(null);
		jJTaskBean.setSprint(null);
		// RequestContext context = RequestContext.getCurrentInstance();
		//
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		//
		// int i = 0;
		// if (((LoginBean) LoginBean.findBean("loginBean")).isRenderGantt())
		// i = 1;
		// else
		// i = 0;

		// context.execute("PF('projectTabView').select(" + i + ")");
		//
		// System.err.println("SprintTab.select("
		// + contains(sprintUtil.getSprint().getId()) + ")");
		// update = false;
		// context.execute("PF('SprintTab').select("
		// + contains(sprintUtil.getSprint().getId()) + ")");
		// context.update(":projecttabview");

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
			jJTaskBean.saveJJTask(dropedTask, true);
			// resetJJTaskBean();

			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);
			JJTaskBean jJTaskBean = (JJTaskBean) session
					.getAttribute("jJTaskBean");
			if (jJTaskBean == null)
				jJTaskBean = new JJTaskBean();
			jJTaskBean.updateView(jJTaskService.findJJTask(dropedTask.getId()),
					JJTaskBean.UPDATE_OPERATION);

			JJSprint s = jJSprintService.findJJSprint(sprintId);
			sprintUtil = new SprintUtil(s, jJTaskService.getSprintTasks(s,
					LoginBean.getProduct()), jJContactService);

			sprintList
					.set(contains(sprintUtil.getSprint().getId()), sprintUtil);
			String message = "message_successfully_updated";
			FacesMessage facesMessage = MessageFactory.getMessage(message,
					MessageFactory.getMessage("label_task", "").getDetail());
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}

		else {

			FacesMessage facesMessage = new FacesMessage(
					FacesMessage.SEVERITY_WARN, "non autorisée", "");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}

		// RequestContext context = RequestContext.getCurrentInstance();
		//
		// int i = 0;
		// if (((LoginBean) LoginBean.findBean("loginBean")).isRenderGantt())
		// i = 1;
		// else
		// i = 0;

		// context.execute("PF('projectTabView').select(" + i + ")");
		// update = false;
		// context.execute("PF('SprintTab').select(" + contains(sprintId) +
		// ")");

	}

	public void addTaskToDone(DragDropEvent ddevent) {

		Long id = null;
		JJTask dropedTask = (JJTask) ddevent.getData();

		Long sprintId = dropedTask.getSprint().getId();

		if (ddevent.getDragId().contains(":progIcon")
				|| ddevent.getDragId().contains(":progPanel")) {

			id = dropedTask.getId();
			JJStatus status = jJStatusService
					.getOneStatus("DONE", "Task", true);

			dropedTask.setEndDateReal(new Date());
			dropedTask.setCompleted(true);
			dropedTask.setStatus(status);
			jJTaskBean.saveJJTask(dropedTask, true);
			// resetJJTaskBean();

			HttpSession session = (HttpSession) FacesContext
					.getCurrentInstance().getExternalContext()
					.getSession(false);
			JJTaskBean jJTaskBean = (JJTaskBean) session
					.getAttribute("jJTaskBean");
			if (jJTaskBean == null)
				jJTaskBean = new JJTaskBean();
			jJTaskBean.updateView(jJTaskService.findJJTask(dropedTask.getId()),
					JJTaskBean.UPDATE_OPERATION);

			JJSprint s = jJSprintService.findJJSprint(sprintId);
			sprintUtil = new SprintUtil(s, jJTaskService.getSprintTasks(s,
					LoginBean.getProduct()), jJContactService);

			sprintList
					.set(contains(sprintUtil.getSprint().getId()), sprintUtil);

			String message = "message_successfully_updated";
			FacesMessage facesMessage = MessageFactory.getMessage(message,
					MessageFactory.getMessage("label_task", "").getDetail());
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		} else {
			FacesMessage facesMessage = new FacesMessage(
					FacesMessage.SEVERITY_WARN, "non autorisée", "");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}

		// int i = 0;
		// if (((LoginBean) LoginBean.findBean("loginBean")).isRenderGantt())
		// i = 1;
		// else
		// i = 0;
		RequestContext context = RequestContext.getCurrentInstance();

		// context.execute("PF('projectTabView').select(" + i + ")");
		// update = false;
		// context.execute("PF('SprintTab').select(" + contains(sprintId) +
		// ")");
		if (id != null) {
			JJTask t = jJTaskService.findJJTask(id);
			if (t.getBug() != null) {

				bug = t.getBug();
				context.execute("PF('blockUIWidget').unblock()");
				context.execute("PF('editBugDialogWidget').show()");
			}

		}

	}

	// public void handleAddButton(ActionEvent e) {
	//
	// attrListener(e);
	// logger.info("handleAddButton");
	//
	// sprintUtil.setRenderTaskForm(!sprintUtil.isRenderTaskForm());
	//
	// if (sprintUtil.isRenderTaskForm()) {
	// categoryList = null;
	// getCategoryList();
	// getBugs();
	//
	// } else {
	// requirement = null;
	// category = null;
	// categoryList = null;
	// bug = null;
	// bugs = null;
	// reqList = null;
	// }
	// }

	// public void persistTask() {
	// JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
	// .getContact();
	// JJStatus status = jJStatusService.getOneStatus("TODO", "Task", true);
	// if (status != null)
	// task.setStatus(status);
	// if (requirement != null) {
	// task.setName(requirement.getName());
	// task.setRequirement(requirement);
	// } else if (bug != null) {
	// task.setName(bug.getName());
	// task.setBug(bug);
	// task.setRequirement(bug.getRequirement());
	// }
	//
	// task.setStartDatePlanned(sprintUtil.getSprint().getStartDate());
	// task.setEndDatePlanned(sprintUtil.getSprint().getEndDate());
	// task.setSprint(sprintUtil.getSprint());
	// task.setEnabled(true);
	// task.setDescription(task.getName() + " /CreatedBy:" + contact.getName()
	// + " at :" + new Date());
	//
	// jJTaskBean.saveJJTask(task, false);
	//
	// if (!sprintUtil.isRender()) {
	//
	// sprintUtil = new SprintUtil(jJSprintService.findJJSprint(sprintUtil
	// .getSprint().getId()),
	// jJTaskService.getSprintTasks(jJSprintService
	// .findJJSprint(sprintUtil.getSprint().getId())));
	// sprintList
	// .set(contains(sprintUtil.getSprint().getId()), sprintUtil);
	//
	// }
	// String message = "message_successfully_created";
	// RequestContext context = RequestContext.getCurrentInstance();
	// context.execute("PF('createTaskDialogWidget').hide()");
	// FacesMessage facesMessage = MessageFactory.getMessage(message, "Task :"
	// + task.getName());
	// FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	// }

	public void doneEvent(ActionEvent e) {

		attrListener(e);
		sprintUtil = new SprintUtil(sprintUtil.getSprint(),
				jJTaskService.getSprintTasks(sprintUtil.getSprint(),
						LoginBean.getProduct()), jJContactService);

		sprintList.set(contains(sprintUtil.getSprint().getId()), sprintUtil);

		JJSprint sp = new JJSprint();
		sp.setProject(LoginBean.getProject());
		sprintList.add(new SprintUtil(sp, null, jJContactService));
		// requirement = null;
		category = null;
		categoryList = null;
		// reqList = null;
		// int i = 0;
		// if (((LoginBean) LoginBean.findBean("loginBean")).isRenderGantt())
		// i = 1;
		// else
		// i = 0;
		// RequestContext context = RequestContext.getCurrentInstance();
		//
		// context.execute("PF('projectTabView').select(" + i + ")");
		// update = false;
		// context.execute("PF('SprintTab').select("
		// + contains(sprintUtil.getSprint().getId()) + ")");

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
					jJTaskService.getSprintTasks(jJSprintService
							.findJJSprint(sprintUtil.getSprint().getId()),
							LoginBean.getProduct()), jJContactService));
			System.out.println(jJTaskService.getSprintTasks(
					jJSprintService
							.findJJSprint(sprintUtil.getSprint().getId()),
					LoginBean.getProduct()).size());

			sprintList
					.set(contains(sprintUtil.getSprint().getId()), sprintUtil);
			System.out.println(contains(sprintUtil.getSprint().getId()));

		}

		System.out.println("reloadSprintUtil finiched");

	}

	public void deleteTask() {

		System.out.println("deleteTask");
		task = jJTaskService.findJJTask(task.getId());
		task.setEnabled(false);
		jJTaskBean.saveJJTask(task, false);
		sprintUtil = new SprintUtil(jJSprintService.findJJSprint(task
				.getSprint().getId()), jJTaskService.getSprintTasks(
				jJSprintService.findJJSprint(task.getSprint().getId()),
				LoginBean.getProduct()), jJContactService);

		// sprintUtil.setRenderTaskForm(false);
		sprintList.set(contains(sprintUtil.getSprint().getId()), sprintUtil);
		// requirement = null;
		category = null;
		categoryList = null;

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJTaskBean jJTaskBean = (JJTaskBean) session.getAttribute("jJTaskBean");
		if (jJTaskBean == null)
			jJTaskBean = new JJTaskBean();
		jJTaskBean.updateView(jJTaskService.findJJTask(task.getId()),
				JJTaskBean.DELETE_OPERATION);
		// reqList = null;
		task = null;
		// resetJJTaskBean();

		RequestContext context = RequestContext.getCurrentInstance();

		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_deleted",
				MessageFactory.getMessage("label_task", "").getDetail());
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
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJTaskBean jJTaskBean = (JJTaskBean) session.getAttribute("jJTaskBean");
		if (jJTaskBean == null)
			jJTaskBean = new JJTaskBean();
		jJTaskBean.setProject(null);
		jJTaskBean.setTasksData(null);
		jJTaskBean.setModel(null);
	}

	public int contains(Long id) {
		int i = 0;
		int j = -1;

		if (sprintList != null && id != null) {
			while (i < sprintList.size()) {
				if (sprintList.get(i).getSprint().getId().equals(id)) {
					j = i;
					i = sprintList.size();
				} else
					i++;
			}
		}
		if (id == null)
			j = sprintList.size() - 1;

		return j;

	}

	public void handleDialogCloseEvent(CloseEvent event) {
		task = null;
	}

	public List<JJBug> completeBug(String query) {

		List<JJBug> suggestions = new ArrayList<JJBug>();
		for (JJBug jJBug : jJBugService.getBugs(LoginBean.getCompany(),
				project, LoginBean.getProduct(), null, null, true, true)) {
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
			String jJBugStr = String.valueOf(jJContact.getName() + "Mail: "
					+ jJContact.getEmail());
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

		FacesMessage facesMessage = MessageFactory.getMessage(message, "Bug");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		jJbugBean.reset();
		// RequestContext context = RequestContext.getCurrentInstance();
		// int i = 0;
		// if (((LoginBean) LoginBean.findBean("loginBean")).isRenderGantt())
		// i = 1;
		// else
		// i = 0;
		// context.execute("PF('projectTabView').select(" + i + ")");
		// update = false;
		// context.execute("PF('SprintTab').select("
		// + contains(getSprintUtil().getSprint().getId()) + ")");
	}

	public HtmlPanelGrid populateCreateTaskPanel() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		javax.faces.application.Application application = facesContext
				.getApplication();
		ExpressionFactory expressionFactory = application
				.getExpressionFactory();
		ELContext elContext = facesContext.getELContext();

		JJBug b = (JJBug) expressionFactory.createValueExpression(elContext,
				"#{jJSprintBean.bug}", JJBug.class).getValue(elContext);

		HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application
				.createComponent(HtmlPanelGrid.COMPONENT_TYPE);

		OutputLabel workloadPlannedCreateOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		workloadPlannedCreateOutput.setFor("workloadPlannedCreateInput");
		workloadPlannedCreateOutput.setId("workloadPlannedCreateOutput");
		workloadPlannedCreateOutput.setValue("Workload Planned:");
		htmlPanelGrid.getChildren().add(workloadPlannedCreateOutput);

		Spinner workloadPlannedCreateInput = (Spinner) application
				.createComponent(Spinner.COMPONENT_TYPE);
		workloadPlannedCreateInput.setId("workloadPlannedCreateInput");
		workloadPlannedCreateInput.setValueExpression("value",
				expressionFactory.createValueExpression(elContext,
						"#{jJSprintBean.task.workloadPlanned}", Integer.class));
		workloadPlannedCreateInput.setRequired(false);

		htmlPanelGrid.getChildren().add(workloadPlannedCreateInput);

		Message workloadPlannedCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		workloadPlannedCreateInputMessage
				.setId("workloadPlannedCreateInputMessage");
		workloadPlannedCreateInputMessage.setFor("workloadPlannedCreateInput");
		workloadPlannedCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(workloadPlannedCreateInputMessage);

		OutputLabel bugCreateOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		bugCreateOutput.setRendered(b == null);
		bugCreateOutput.setFor("bugCreateInput");
		bugCreateOutput.setId("bugCreateOutput");
		bugCreateOutput.setValue("Bug:");
		htmlPanelGrid.getChildren().add(bugCreateOutput);

		AutoComplete bugCreateInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		bugCreateInput.setId("bugCreateInput");
		bugCreateInput.setRendered(b == null);
		bugCreateInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{jJSprintBean.task.bug}",
						JJBug.class));
		bugCreateInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{jJSprintBean.completeBug}", List.class,
						new Class[] { String.class }));
		bugCreateInput.setDropdown(true);
		bugCreateInput.setValueExpression("var", expressionFactory
				.createValueExpression(elContext, "bug", String.class));
		bugCreateInput.setValueExpression("itemLabel", expressionFactory
				.createValueExpression(elContext, "#{bug.name}", String.class));
		bugCreateInput.setValueExpression("itemValue", expressionFactory
				.createValueExpression(elContext, "#{bug}", JJBug.class));
		bugCreateInput.setConverter(new JJBugConverter());
		bugCreateInput.setRequired(false);
		htmlPanelGrid.getChildren().add(bugCreateInput);

		Message bugCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		bugCreateInputMessage.setId("bugCreateInputMessage");
		bugCreateInputMessage.setRendered(b == null);
		bugCreateInputMessage.setFor("bugCreateInput");
		bugCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(bugCreateInputMessage);

		return htmlPanelGrid;
	}

}

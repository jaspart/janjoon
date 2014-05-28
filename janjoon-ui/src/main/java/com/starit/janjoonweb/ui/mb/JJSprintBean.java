package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.primefaces.component.autocomplete.AutoComplete;
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
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJRequirementService;
import com.starit.janjoonweb.domain.JJSprint;
import com.starit.janjoonweb.domain.JJStatus;
import com.starit.janjoonweb.domain.JJStatusService;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJTaskService;
import com.starit.janjoonweb.ui.mb.converter.JJBugConverter;
import com.starit.janjoonweb.ui.mb.converter.JJContactConverter;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;
import com.starit.janjoonweb.ui.mb.util.service.SprintUtil;

@RooSerializable
@RooJsfManagedBean(entity = JJSprint.class, beanName = "jJSprintBean")
public class JJSprintBean {

	@Autowired
	private JJTaskService jJTaskService;

	@Autowired
	private JJCategoryService jJCategoryService;

	@Autowired
	private JJRequirementService jJRequirementService;

	@Autowired
	private JJStatusService jJStatusService;

	@Autowired
	private JJBugService jJBugService;

	private List<JJCategory> categoryList;
	private HtmlPanelGrid createTaskPanelGrid;
	private JJCategory category;
	private JJRequirement requirement;
	private List<JJRequirement> reqList;
	private JJProject project;
	private List<SprintUtil> sprintList;
	private SprintUtil sprintUtil;
	private JJTask task;

	private static final Logger logger = Logger.getLogger(JJSprintBean.class);

	public void setjJTaskService(JJTaskService jJTaskService) {
		this.jJTaskService = jJTaskService;
	}

	public void setjBugService(JJBugService jjBugService) {
		this.jJBugService = jjBugService;
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

	public List<JJCategory> getCategoryList() {

		if (categoryList == null)
			categoryList = jJCategoryService.getCategories(null, false, true,
					false);

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

	public JJRequirement getRequirement() {
		return requirement;
	}

	public void setRequirement(JJRequirement requirement) {
		this.requirement = requirement;
	}

	public List<JJRequirement> getReqList() {

		return reqList;
	}

	public void setReqList(List<JJRequirement> reqList) {
		this.reqList = reqList;
	}

	public JJProject getProject() {
		return project;
	}

	public void setProject(JJProject project) {
		this.project = project;
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
		reqList = null;
		requirement = null;

	}

	public List<SprintUtil> getSprintList() {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJProjectBean jJProjectBean = (JJProjectBean) session
				.getAttribute("jJProjectBean");

		JJContactBean jJContactBean = (JJContactBean) session
				.getAttribute("jJContactBean");
		if (jJContactBean == null)
			jJContactBean = new JJContactBean();
		jJContactBean.getContacts();

		if (jJProjectBean.getProject() == null) {

			if (sprintList == null) {
				project = null;
				initJJSprintPage(null);
			} else if (project != null) {

				project = null;
				initJJSprintPage(null);
			}

		} else if (project != null) {

			if (!project.equals(jJProjectBean.getProject())) {
				project = jJProjectBean.getProject();
				initJJSprintPage(project);
			}
		} else {
			project = jJProjectBean.getProject();
			initJJSprintPage(project);
		}

		return sprintList;
	}

	public void setSprintList(List<SprintUtil> sprintList) {
		this.sprintList = sprintList;
	}

	public SprintUtil getSprintUtil() {

		return sprintUtil;
	}

	public void setSprintUtil(SprintUtil sprintUtil) {
		this.sprintUtil = sprintUtil;
	}

	public void attrListener(ActionEvent event) {

		sprintUtil = (SprintUtil) event.getComponent().getAttributes()
				.get("sprintUtilValue");
	}

	public void updatereqPanel() {
		reqList = jJRequirementService.getRequirements(category, project, null,
				null, null, null, false, true, true);
		requirement = null;
		if (!reqList.isEmpty())
			requirement = reqList.get(0);
	}

	public void onCellEditTask(CellEditEvent event) {

		JJTask t = jJTaskService.findJJTask(Long.parseLong(event.getColumn()
				.getColumnKey()));

		t.setAssignedTo((JJContact) event.getNewValue());
		jJTaskService.updateJJTask(t);
		sprintUtil = SprintUtil
				.getSprintUtil(t.getSprint().getId(), sprintList);
		sprintUtil.setSprint(jJSprintService.findJJSprint(sprintUtil
				.getSprint().getId()));
		sprintUtil = new SprintUtil(sprintUtil.getSprint(),
				jJTaskService.getSprintTasks(sprintUtil.getSprint()));
		sprintList.set(contains(sprintUtil), sprintUtil);
		String message = "message_successfully_updated";
		FacesMessage facesMessage = MessageFactory
				.getMessage(message, "JJTask");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}

	public void initJJSprintPage(JJProject pr) {

		sprintList = SprintUtil.generateSprintUtilList(
				jJSprintService.getSprintsByProjects(pr, true), jJTaskService);

		if (sprintList == null)
			sprintList = new ArrayList<SprintUtil>();
		JJSprint sp = new JJSprint();
		sp.setProject(pr);
		sprintList.add(new SprintUtil(sp, null));

	}

	public void editSprint() {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJContact contact = (JJContact) session.getAttribute("JJContact");

		sprintUtil.getSprint().setUpdatedBy(contact);
		sprintUtil.getSprint().setUpdatedDate(new Date());
		jJSprintService.updateJJSprint(sprintUtil.getSprint());
		sprintUtil.setSprint(jJSprintService.findJJSprint(sprintUtil
				.getSprint().getId()));
		sprintUtil = new SprintUtil(sprintUtil.getSprint(),
				jJTaskService.getSprintTasks(sprintUtil.getSprint()));
		sprintList.set(contains(sprintUtil), sprintUtil);

		String message = "message_successfully_updated";
		FacesMessage facesMessage = MessageFactory.getMessage(message,
				"JJSprint");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}

	public void createSprint() {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);

		JJContact contact = (JJContact) session.getAttribute("JJContact");

		setJJSprint_(sprintUtil.getSprint());

		getJJSprint_().setCreatedBy(contact);
		getJJSprint_().setCreationDate(new Date());
		getJJSprint_().setEnabled(true);
		getJJSprint_().setDescription(
				getJJSprint_().getName() + " /CreatedBy:"
						+ getJJSprint_().getCreatedBy().getName() + " at :"
						+ getJJSprint_().getCreationDate());
		jJSprintService.saveJJSprint(getJJSprint_());
		for (JJContact c : getJJSprint_().getContacts()) {
			c.getSprints().add(getJJSprint_());
		}
		String message = "message_successfully_created";
		FacesMessage facesMessage = MessageFactory.getMessage(message,
				"JJSprint");
		sprintUtil.setRenderTaskForm(true);
		sprintUtil.setSprint(getJJSprint_());
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
		setSelectedTasks(null);
		setSelectedObstacles(null);
		setSelectedMessages(null);
		setCreateDialogVisible(false);
		setSelectedContacts(null);

	}

	public void addTaskToProg(DragDropEvent ddevent) {

		if (ddevent.getDragId().contains(":todoIcon")) {
			JJTask dropedTask = (JJTask) ddevent.getData();

			JJStatus status = jJStatusService.getOneStatus("IN PROGRESS",
					"JJTask", true);
			sprintUtil = SprintUtil.getSprintUtil(dropedTask.getSprint()
					.getId(), sprintList);
			dropedTask.setStatus(status);
			dropedTask.setCompleted(false);
			jJTaskService.updateJJTask(dropedTask);
			sprintUtil.setSprint(jJSprintService.findJJSprint(sprintUtil
					.getSprint().getId()));
			sprintUtil = new SprintUtil(sprintUtil.getSprint(),
					jJTaskService.getSprintTasks(sprintUtil.getSprint()));
			sprintList.set(contains(sprintUtil), sprintUtil);
			String message = "message_successfully_updated";
			FacesMessage facesMessage = MessageFactory.getMessage(message,
					"JJTask");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}

		else {

			FacesMessage facesMessage = new FacesMessage(
					FacesMessage.SEVERITY_WARN, "non autorisée", "");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}

	}

	public void addTaskToDone(DragDropEvent ddevent) {

		if (ddevent.getDragId().contains(":progIcon")) {
			JJTask dropedTask = (JJTask) ddevent.getData();

			JJStatus status = jJStatusService.getOneStatus("DONE", "JJTask",
					true);

			sprintUtil = SprintUtil.getSprintUtil(dropedTask.getSprint()
					.getId(), sprintList);

			dropedTask.setEndDateReal(new Date());
			dropedTask.setCompleted(true);
			dropedTask.setStatus(status);
			jJTaskService.updateJJTask(dropedTask);

			sprintUtil.setSprint(jJSprintService.findJJSprint(sprintUtil
					.getSprint().getId()));
			sprintUtil = new SprintUtil(sprintUtil.getSprint(),
					jJTaskService.getSprintTasks(sprintUtil.getSprint()));
			sprintList.set(contains(sprintUtil), sprintUtil);
			String message = "message_successfully_updated";
			FacesMessage facesMessage = MessageFactory.getMessage(message,
					"JJTask");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);

		} else {
			FacesMessage facesMessage = new FacesMessage(
					FacesMessage.SEVERITY_WARN, "non autorisée", "");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}

	}

	public void handleAddButton(ActionEvent e) {

		attrListener(e);
		logger.info("deedededede");

		sprintUtil.setRenderTaskForm(!sprintUtil.isRenderTaskForm());

		if (sprintUtil.isRenderTaskForm()) {
			categoryList = null;
			getCategoryList();
			getReqList();

		} else {
			requirement = null;
			category = null;
			categoryList = null;
			reqList = null;
		}
	}

	public void persistTask() {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJContact contact = (JJContact) session.getAttribute("JJContact");
		JJStatus status = jJStatusService.getOneStatus("TODO", "JJTask", true);
		if (status != null)
			task.setStatus(status);
		task.setName("Task For Requirement: " + requirement.getName());
		task.setStartDatePlanned(sprintUtil.getSprint().getStartDate());
		task.setEndDatePlanned(sprintUtil.getSprint().getEndDate());
		task.setSprint(sprintUtil.getSprint());
		task.setCreatedBy(contact);
		task.setCreationDate(new Date());
		task.setRequirement(requirement);
		task.setEnabled(true);
		task.setDescription(task.getName() + " /CreatedBy:"
				+ task.getCreatedBy().getName() + " at :"
				+ task.getCreationDate());

		jJTaskService.saveJJTask(task);
		if (!sprintUtil.isRender()) {
			sprintUtil = new SprintUtil(sprintUtil.getSprint(),
					jJTaskService.getSprintTasks(sprintUtil.getSprint()));
			sprintUtil.setRenderTaskForm(true);
			sprintUtil.setTaskList(null);
			sprintList.set(contains(sprintUtil), sprintUtil);

		} else {

			sprintUtil.getTaskList().add(task);
		}
		String message = "message_successfully_created";
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("createTaskDialogWidget.hide()");
		FacesMessage facesMessage = MessageFactory.getMessage(message,
				"JJTask :" + task.getName());
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);

	}

	public void doneEvent(ActionEvent e) {

		attrListener(e);
		sprintUtil = new SprintUtil(sprintUtil.getSprint(),
				jJTaskService.getSprintTasks(sprintUtil.getSprint()));

		sprintUtil.setRenderTaskForm(false);
		sprintList.set(contains(sprintUtil), sprintUtil);
		requirement = null;
		category = null;
		categoryList = null;
		reqList = null;

	}

	public void doneEvent() {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		session.setAttribute("jJSprintBean", new JJSprintBean());

	}

	public int contains(SprintUtil s) {
		int i = 0;
		int j = -1;

		if (sprintList != null) {
			while (i < sprintList.size()) {
				if (sprintList.get(i).getSprint().getId() == s.getSprint()
						.getId()) {
					j = i;
					i = sprintList.size();
				} else
					i++;
			}
		}

		return j;

	}

	public void handleDialogCloseEvent(CloseEvent event) {
		task = null;
	}

	public List<JJBug> completeBug(String query) {
		List<JJBug> suggestions = new ArrayList<JJBug>();
		for (JJBug jJBug : jJBugService
				.getBugs(project, null, null, true, true)) {
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

	public HtmlPanelGrid populateCreateTaskPanel() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		javax.faces.application.Application application = facesContext
				.getApplication();
		ExpressionFactory expressionFactory = application
				.getExpressionFactory();
		ELContext elContext = facesContext.getELContext();

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
		bugCreateOutput.setFor("bugCreateInput");
		bugCreateOutput.setId("bugCreateOutput");
		bugCreateOutput.setValue("Bug:");
		htmlPanelGrid.getChildren().add(bugCreateOutput);

		AutoComplete bugCreateInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		bugCreateInput.setId("bugCreateInput");
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
		bugCreateInputMessage.setFor("bugCreateInput");
		bugCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(bugCreateInputMessage);

		OutputLabel assignedToCreateOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		assignedToCreateOutput.setFor("assignedToCreateInput");
		assignedToCreateOutput.setId("assignedToCreateOutput");
		assignedToCreateOutput.setValue("Assigned To:");
		htmlPanelGrid.getChildren().add(assignedToCreateOutput);

		AutoComplete assignedToCreateInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		assignedToCreateInput.setId("assignedToCreateInput");
		assignedToCreateInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJSprintBean.task.assignedTo}", JJContact.class));
		assignedToCreateInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{jJSprintBean.completeAssignedTo}", List.class,
						new Class[] { String.class }));
		assignedToCreateInput.setDropdown(true);
		assignedToCreateInput.setValueExpression("var", expressionFactory
				.createValueExpression(elContext, "assignedTo", String.class));
		assignedToCreateInput
				.setValueExpression("itemLabel", expressionFactory
						.createValueExpression(elContext,
								"#{assignedTo.name} #{assignedTo.email}",
								String.class));
		assignedToCreateInput.setValueExpression("itemValue", expressionFactory
				.createValueExpression(elContext, "#{assignedTo}",
						JJContact.class));
		assignedToCreateInput.setConverter(new JJContactConverter());
		assignedToCreateInput.setRequired(false);
		htmlPanelGrid.getChildren().add(assignedToCreateInput);

		Message assignedToCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		assignedToCreateInputMessage.setId("assignedToCreateInputMessage");
		assignedToCreateInputMessage.setFor("assignedToCreateInput");
		assignedToCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(assignedToCreateInputMessage);

		return htmlPanelGrid;
	}
}

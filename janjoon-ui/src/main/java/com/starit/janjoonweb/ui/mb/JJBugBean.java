package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.LengthValidator;
import javax.servlet.http.HttpSession;

import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.message.Message;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.context.RequestContext;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJBugService;
import com.starit.janjoonweb.domain.JJBuild;
import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJCriticity;
import com.starit.janjoonweb.domain.JJImportance;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJSprint;
import com.starit.janjoonweb.domain.JJStatus;
import com.starit.janjoonweb.domain.JJTeststep;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.domain.reference.JJRelationship;
import com.starit.janjoonweb.ui.mb.converter.JJBugConverter;
import com.starit.janjoonweb.ui.mb.converter.JJBuildConverter;
import com.starit.janjoonweb.ui.mb.converter.JJCategoryConverter;
import com.starit.janjoonweb.ui.mb.converter.JJContactConverter;
import com.starit.janjoonweb.ui.mb.converter.JJCriticityConverter;
import com.starit.janjoonweb.ui.mb.converter.JJImportanceConverter;
import com.starit.janjoonweb.ui.mb.converter.JJProjectConverter;
import com.starit.janjoonweb.ui.mb.converter.JJRequirementConverter;
import com.starit.janjoonweb.ui.mb.converter.JJSprintConverter;
import com.starit.janjoonweb.ui.mb.converter.JJStatusConverter;
import com.starit.janjoonweb.ui.mb.converter.JJTeststepConverter;
import com.starit.janjoonweb.ui.mb.converter.JJVersionConverter;
import com.starit.janjoonweb.ui.mb.lazyLoadingDataTable.LazyBugDataModel;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJBug.class, beanName = "jJBugBean")
public class JJBugBean {

	private JJBug bug;
	private JJBug JJBug_;

	private JJProject bugProjectSelected;
	private JJRequirement bugRequirementSelected;

	private JJProject project;
	private LazyBugDataModel bugList;
	private List<JJBug> filteredJJBug;
	private List<JJBug> selectedBugList;
	private SelectItem[] criticityOptions;
	private SelectItem[] importanceOptions;
	private SelectItem[] statusOptions;

	public JJProject getBugProjectSelected() {
		return bugProjectSelected;
	}

	public void setBugProjectSelected(JJProject bugProjectSelected) {
		this.bugProjectSelected = bugProjectSelected;
	}

	public JJRequirement getBugRequirementSelected() {
		return bugRequirementSelected;
	}

	public void setBugRequirementSelected(JJRequirement bugRequirementSelected) {
		this.bugRequirementSelected = bugRequirementSelected;
	}

	public JJBug getBug() {

		return bug;
	}

	public void setBug(JJBug bug) {
		this.bug = bug;
	}

	public JJBug getJJBug_() {
		if (JJBug_ == null) {
			JJBug_ = new JJBug();
			bugProjectSelected = project;
			JJBug_.setStatus(jJStatusService.getOneStatus("NEW", "JJBUG", true));
		}
		return JJBug_;
	}

	public void setJJBug_(JJBug JJBug_) {
		this.JJBug_ = JJBug_;
	}

	public JJProject getProject() {

		return project;
	}

	public void setProject(JJProject project) {
		this.project = project;
	}

	public LazyBugDataModel getBugList() {
		if (bugList == null)
			bugList = new LazyBugDataModel(jJBugService,project);
		return bugList;
	}

	public void setBugList(LazyBugDataModel bugList) {
		this.bugList = bugList;
	}

	public List<JJBug> getFilteredJJBug() {

		String referrer = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestHeaderMap().get("referer");
		if (referrer != null) {
			if (!referrer.contains("bugs")) {
				filteredJJBug = (List<JJBug>) bugList.getWrappedData();
			}
		}
		return filteredJJBug;
	}

	public void setFilteredJJBug(List<JJBug> filteredJJBug) {
		this.filteredJJBug = filteredJJBug;
	}

	public List<JJBug> getSelectedBugList() {
		return selectedBugList;
	}

	public void setSelectedBugList(List<JJBug> selectedBugList) {
		this.selectedBugList = selectedBugList;
	}

	public SelectItem[] getcriticityOptions() {
		return criticityOptions;
	}

	public SelectItem[] getImportanceOptions() {

		return importanceOptions;
	}

	public SelectItem[] getStatusOptions() {
		return statusOptions;
	}

	public void initJJBug(ComponentSystemEvent e) {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);

		JJProjectBean jJProjectBean = (JJProjectBean) session
				.getAttribute("jJProjectBean");

		if (jJProjectBean.getProject() == null) {

			if (bugList == null) {
				project = null;
				bugList = new LazyBugDataModel(jJBugService,project);
				initJJBugTable();
			} else if (project != null) {

				project = null;
				bugList = new LazyBugDataModel(jJBugService,project);
				initJJBugTable();
			}

		} else if (project != null) {

			if (!project.equals(jJProjectBean.getProject())) {
				project = jJProjectBean.getProject();
				bugList = new LazyBugDataModel(jJBugService,project);
				initJJBugTable();
			}
		} else {
			project = jJProjectBean.getProject();
			bugList = new LazyBugDataModel(jJBugService,project);
			initJJBugTable();
		}

	}

	public void deleteMultiple() {
		for (JJBug b : selectedBugList) {
			b.setEnabled(false);
			jJBugService.updateJJBug(b);
		}
		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_deleted", "JJBug");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		reset();
	}

	public void deleteSingle() {

		getJJBug_().setEnabled(false);
		jJBugService.updateJJBug(getJJBug_());
		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_deleted", "JJBug");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		reset();
	}

	public void initJJBugTable() {	

		List<JJCriticity> criticities = new ArrayList<JJCriticity>();
		List<JJStatus> status = new ArrayList<JJStatus>();
		List<JJImportance> importances = new ArrayList<JJImportance>();
		
		List<JJBug> data=jJBugService.getBugs(project);
		for (JJBug b : data) {

			if (b.getCriticity() != null
					&& !listContaines(criticities, b.getCriticity()))
				criticities.add(b.getCriticity());

			if (b.getStatus() != null && !listContaines(status, b.getStatus()))
				status.add(b.getStatus());

			if (b.getImportance() != null
					&& !listContaines(importances, b.getImportance()))
				importances.add(b.getImportance());

		}
		criticityOptions = createFilterOptions(criticities);
		importanceOptions = createFilterOptions(importances);
		statusOptions = createFilterOptions(status);

	}

	private SelectItem[] createFilterOptions(Object objet) {

		List<Object> data = (List<Object>) objet;

		SelectItem[] options = new SelectItem[data.size() + 1];

		options[0] = new SelectItem("", "Select");
		for (int i = 0; i < data.size(); i++) {

			if (data.get(i) instanceof JJCriticity) {
				JJCriticity criticity = (JJCriticity) data.get(i);
				options[i + 1] = new SelectItem(criticity.getName(),
						criticity.getName());
			} else if (data.get(i) instanceof JJStatus) {
				JJStatus status = (JJStatus) data.get(i);
				options[i + 1] = new SelectItem(status.getName(),
						status.getName());

			} else if (data.get(i) instanceof JJImportance) {
				JJImportance importance = (JJImportance) data.get(i);
				options[i + 1] = new SelectItem(importance.getName(),
						importance.getName());
			}

		}
		return options;

	}

	public boolean listContaines(Object objet, Object find) {

		if (find instanceof JJStatus) {
			List<JJStatus> list = (List<JJStatus>) objet;
			int i = 0;
			JJStatus status = (JJStatus) find;
			boolean contain = false;

			while (i < list.size() && !contain) {
				contain = (list.get(i).equals(status));
				i++;
			}

			return contain;
		} else if (find instanceof JJImportance) {
			List<JJImportance> list = (List<JJImportance>) objet;
			int i = 0;
			JJImportance importance = (JJImportance) find;
			boolean contain = false;

			while (i < list.size() && !contain) {
				contain = (list.get(i).equals(importance));
				i++;
			}

			return contain;
		} else if (find instanceof JJCriticity) {
			List<JJCriticity> list = (List<JJCriticity>) objet;
			int i = 0;
			JJCriticity criticity = (JJCriticity) find;
			boolean contain = false;

			while (i < list.size() && !contain) {
				contain = (list.get(i).equals(criticity));
				i++;
			}

			return contain;
		} else
			return false;

	}

	public void createJJBug(JJTeststep jJTeststep) {

		JJBug bug = new JJBug();
		bug.setName("BUG NAME");
		bug.setCreationDate(new Date());
		bug.setEnabled(true);
		bug.setDescription("Insert a comment");
		bug.setProject(project);
		bug.setTeststep(jJTeststep);

		jJBugService.saveJJBug(bug);
		reset();

	}

	public void reset() {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);

		session.setAttribute("jJBugBean", new JJBugBean());
		// JJBug_ = null;
		// bugProjectSelected = null;
		// bugRequirementSelected = null;
		// bugList = null;
		// selectedBugList = null;
		// filteredJJBug=null;
		//
		// criticityOptions = null;
		// importanceOptions = null;
		// statusOptions = null;
		//
		// setSelectedBugs(null);
		// setSelectedTasks(null);
		// setSelectedMessages(null);
		// setCreateDialogVisible(false);
	}

	public void saveBug() {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJContact contact = (JJContact) session.getAttribute("JJContact");

		JJTeststepexecutionBean jJTeststepexecutionBean = (JJTeststepexecutionBean) session
				.getAttribute("jJTeststepexecutionBean");

		// if (JJBug_ == null) {
		// System.out.println("null");
		// } else {
		// System.out.println("not null");
		// }

		if (JJBug_.getId() == null) {

			JJBug_.setCreationDate(new Date());
			JJBug_.setCreatedBy(contact);
			JJTeststep teststep = jJTeststepService
					.findJJTeststep(jJTeststepexecutionBean
							.getTeststepexecution().getTeststep().getId());

			JJBug_.setTeststep(teststep);
			teststep.getBugs().add(JJBug_);

			jJBugService.saveJJBug(JJBug_);
		} else {

			JJBug_.setUpdatedDate(new Date());
			JJBug_.setUpdatedBy(contact);
			jJBugService.updateJJBug(JJBug_);
		}

		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('bugTestDialogWidget').hide()");

		if (jJTeststepexecutionBean.getDisabledTestcase()) {
			jJTeststepexecutionBean.nextTab();
			jJTeststepexecutionBean.onTabChange();
		} else {
			jJTeststepexecutionBean.changeTestcaseStatus();
		}

	}

	public String persistBug() {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJContact contact = (JJContact) session.getAttribute("JJContact");
		JJBug_.setRequirement(bugRequirementSelected);
		if (project == null) {
			// if(JJBug_.getProject() !=null && bugProjectSelected != null)
			// {
			// if (!JJBug_.getProject().equals(bugProjectSelected)) {
			// JJBug_.setProject(bugProjectSelected);
			// }
			// }

			JJBug_.setProject(bugProjectSelected);

		} else
			JJBug_.setProject(project);

		if (JJBug_.getRequirement() != null) {

			JJBug_.setCategory(JJBug_.getRequirement().getCategory());
		}

		if (JJBug_.getId() != null) {

			JJBug_.setUpdatedDate(new Date());
			JJBug_.setUpdatedBy(contact);

		} else {
			JJBug_.setCreatedBy(contact);
			JJBug_.setCreationDate(new Date());
			JJBug_.setEnabled(true);
		}

		// if (JJBug_.getRequirement() != null)
		// System.out.println(JJBug_.getRequirement().getName());

		String message = "";
		if (JJBug_.getId() != null) {
			jJBugService.updateJJBug(JJBug_);
			message = "message_successfully_updated";
		} else {
			jJBugService.saveJJBug(JJBug_);
			message = "message_successfully_created";
		}

		FacesMessage facesMessage = MessageFactory.getMessage(message, "JJBug");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		reset();
		System.err.println(facesMessage.getDetail());
		return "";
	}

	public String findStyleColor(JJBug b) {
		if (b.getStatus() != null)

			return b.getStatus().getName();
		else
			return "";

	}

	public List<JJRequirement> completeReqBug(String query) {

		List<JJRequirement> suggestions = new ArrayList<JJRequirement>();

		for (JJRequirement req : jJRequirementService.getRequirements(
				bugProjectSelected, null, null)) {
			String jJCriticityStr = String.valueOf(req.getName());
			if (jJCriticityStr.toLowerCase().startsWith(query.toLowerCase())) {
				suggestions.add(req);
			}
		}
		return suggestions;
	}

	public List<JJImportance> completeImportance(String query) {
		List<JJImportance> suggestions = new ArrayList<JJImportance>();
		for (JJImportance jJImportance : jJImportanceService.getBugImportance()) {
			String jJImportanceStr = String.valueOf(jJImportance.getName());
			if (jJImportanceStr.toLowerCase().startsWith(query.toLowerCase())) {
				suggestions.add(jJImportance);
			}
		}
		return suggestions;
	}

	public List<JJTeststep> completeTestStepsBug(String query) {
		List<JJTeststep> suggestions = new ArrayList<JJTeststep>();

		for (JJTeststep testStep : jJTeststepService.getJJtestSteps(
				bugRequirementSelected, bugProjectSelected)) {
			String jJCriticityStr = String.valueOf(testStep.getName());
			if (jJCriticityStr.toLowerCase().startsWith(query.toLowerCase())) {
				suggestions.add(testStep);
			}
		}
		return suggestions;
	}

	public List<JJVersion> completeVersioningBug(String query) {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);

		JJProduct prod = ((JJProductBean) session.getAttribute("jJProductBean"))
				.getProduct();

		List<JJVersion> suggestions = new ArrayList<JJVersion>();

		for (JJVersion req : jJVersionService.getVersions(true, prod != null,
				prod)) {
			String jJCriticityStr = String.valueOf(req.getName());
			if (jJCriticityStr.toLowerCase().startsWith(query.toLowerCase())) {
				suggestions.add(req);
			}
		}
		return suggestions;
	}

	public List<JJSprint> completeSprintBug(String query) {
		List<JJSprint> suggestions = new ArrayList<JJSprint>();

		for (JJSprint req : jJSprintService
				.getSprints(bugProjectSelected, true)) {
			String jJCriticityStr = String.valueOf(req.getName());
			if (jJCriticityStr.toLowerCase().startsWith(query.toLowerCase())) {
				suggestions.add(req);
			}
		}
		return suggestions;
	}

	public List<JJStatus> completeStatusBug(String query) {
		List<JJStatus> suggestions = new ArrayList<JJStatus>();

		for (JJStatus jJStatus : jJStatusService.getStatus("JJBug", true, null,
				true)) {
			String jJCriticityStr = String.valueOf(jJStatus.getName());
			if (jJCriticityStr.toLowerCase().startsWith(query.toLowerCase())) {
				suggestions.add(jJStatus);
			}
		}
		return suggestions;
	}

	public List<JJCriticity> completeCriticityBug(String query) {
		List<JJCriticity> suggestions = new ArrayList<JJCriticity>();

		for (JJCriticity jJCriticity : jJCriticityService.getCriticities(
				"JJBug", true)) {
			String jJCriticityStr = String.valueOf(jJCriticity.getName());
			if (jJCriticityStr.toLowerCase().startsWith(query.toLowerCase())) {
				suggestions.add(jJCriticity);
			}
		}
		return suggestions;
	}

	public HtmlPanelGrid populateEditPanel() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		javax.faces.application.Application application = facesContext
				.getApplication();
		ExpressionFactory expressionFactory = application
				.getExpressionFactory();
		ELContext elContext = facesContext.getELContext();

		HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application
				.createComponent(HtmlPanelGrid.COMPONENT_TYPE);

		OutputLabel nameEditOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		nameEditOutput.setFor("nameEditInput");
		nameEditOutput.setId("nameEditOutput");
		nameEditOutput.setValue("Name:");
		htmlPanelGrid.getChildren().add(nameEditOutput);

		InputTextarea nameEditInput = (InputTextarea) application
				.createComponent(InputTextarea.COMPONENT_TYPE);
		nameEditInput.setId("nameEditInput");
		nameEditInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{jJBugBean.JJBug_.name}",
						String.class));
		LengthValidator nameEditInputValidator = new LengthValidator();
		nameEditInputValidator.setMaximum(100);
		nameEditInput.addValidator(nameEditInputValidator);
		nameEditInput.setRequired(true);
		htmlPanelGrid.getChildren().add(nameEditInput);

		Message nameEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		nameEditInputMessage.setId("nameEditInputMessage");
		nameEditInputMessage.setFor("nameEditInput");
		nameEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(nameEditInputMessage);

		OutputLabel descriptionEditOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		descriptionEditOutput.setFor("descriptionEditInput");
		descriptionEditOutput.setId("descriptionEditOutput");
		descriptionEditOutput.setValue("Description:");
		htmlPanelGrid.getChildren().add(descriptionEditOutput);

		InputTextarea descriptionEditInput = (InputTextarea) application
				.createComponent(InputTextarea.COMPONENT_TYPE);
		descriptionEditInput.setId("descriptionEditInput");
		descriptionEditInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJBugBean.JJBug_.description}", String.class));
		descriptionEditInput.setRequired(true);
		htmlPanelGrid.getChildren().add(descriptionEditInput);

		Message descriptionEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		descriptionEditInputMessage.setId("descriptionEditInputMessage");
		descriptionEditInputMessage.setFor("descriptionEditInput");
		descriptionEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(descriptionEditInputMessage);

		OutputLabel categoryEditOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		categoryEditOutput.setFor("categoryEditInput");
		categoryEditOutput.setId("categoryEditOutput");
		categoryEditOutput.setValue("Category:");
		htmlPanelGrid.getChildren().add(categoryEditOutput);

		AutoComplete categoryEditInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		categoryEditInput.setId("categoryEditInput");
		categoryEditInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJBugBean.JJBug_.category}", JJCategory.class));
		categoryEditInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{jJBugBean.completeCategory}", List.class,
						new Class[] { String.class }));
		categoryEditInput.setDropdown(true);
		categoryEditInput.setValueExpression("var", expressionFactory
				.createValueExpression(elContext, "category", String.class));
		categoryEditInput.setValueExpression("itemLabel", expressionFactory
				.createValueExpression(elContext, "#{category.name}",
						String.class));
		categoryEditInput.setValueExpression("itemValue", expressionFactory
				.createValueExpression(elContext, "#{category}",
						JJCategory.class));
		categoryEditInput.setConverter(new JJCategoryConverter());
		categoryEditInput.setRequired(false);
		htmlPanelGrid.getChildren().add(categoryEditInput);

		Message categoryEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		categoryEditInputMessage.setId("categoryEditInputMessage");
		categoryEditInputMessage.setFor("categoryEditInput");
		categoryEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(categoryEditInputMessage);

		OutputLabel criticityEditOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		criticityEditOutput.setFor("criticityEditInput");
		criticityEditOutput.setId("criticityEditOutput");
		criticityEditOutput.setValue("Criticity:");
		htmlPanelGrid.getChildren().add(criticityEditOutput);

		AutoComplete criticityEditInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		criticityEditInput.setId("criticityEditInput");
		criticityEditInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJBugBean.JJBug_.criticity}", JJCriticity.class));
		criticityEditInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{jJBugBean.completeCriticityBug}", List.class,
						new Class[] { String.class }));
		criticityEditInput.setDropdown(true);
		criticityEditInput.setValueExpression("var", expressionFactory
				.createValueExpression(elContext, "criticity", String.class));
		criticityEditInput.setValueExpression("itemLabel", expressionFactory
				.createValueExpression(elContext, "#{criticity.name}",
						String.class));
		criticityEditInput.setValueExpression("itemValue", expressionFactory
				.createValueExpression(elContext, "#{criticity}",
						JJCriticity.class));
		criticityEditInput.setConverter(new JJCriticityConverter());
		criticityEditInput.setRequired(false);
		htmlPanelGrid.getChildren().add(criticityEditInput);

		Message criticityEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		criticityEditInputMessage.setId("criticityEditInputMessage");
		criticityEditInputMessage.setFor("criticityEditInput");
		criticityEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(criticityEditInputMessage);

		OutputLabel importanceEditOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		importanceEditOutput.setFor("importanceEditInput");
		importanceEditOutput.setId("importanceEditOutput");
		importanceEditOutput.setValue("Importance:");
		htmlPanelGrid.getChildren().add(importanceEditOutput);

		AutoComplete importanceEditInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		importanceEditInput.setId("importanceEditInput");
		importanceEditInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJBugBean.JJBug_.importance}", JJImportance.class));
		importanceEditInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{jJBugBean.completeImportance}", List.class,
						new Class[] { String.class }));
		importanceEditInput.setDropdown(true);
		importanceEditInput.setValueExpression("var", expressionFactory
				.createValueExpression(elContext, "importance", String.class));
		importanceEditInput.setValueExpression("itemLabel", expressionFactory
				.createValueExpression(elContext, "#{importance.name}",
						String.class));
		importanceEditInput.setValueExpression("itemValue", expressionFactory
				.createValueExpression(elContext, "#{importance}",
						JJImportance.class));
		importanceEditInput.setConverter(new JJImportanceConverter());
		importanceEditInput.setRequired(false);
		htmlPanelGrid.getChildren().add(importanceEditInput);

		Message importanceEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		importanceEditInputMessage.setId("importanceEditInputMessage");
		importanceEditInputMessage.setFor("importanceEditInput");
		importanceEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(importanceEditInputMessage);

		OutputLabel statusEditOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		statusEditOutput.setFor("statusEditInput");
		statusEditOutput.setId("statusEditOutput");
		statusEditOutput.setValue("Status:");
		htmlPanelGrid.getChildren().add(statusEditOutput);

		AutoComplete statusEditInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		statusEditInput.setId("statusEditInput");
		statusEditInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{jJBugBean.JJBug_.status}",
						JJStatus.class));
		statusEditInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{jJBugBean.completeStatusBug}", List.class,
						new Class[] { String.class }));
		statusEditInput.setDropdown(true);
		statusEditInput.setValueExpression("var", expressionFactory
				.createValueExpression(elContext, "status", String.class));
		statusEditInput.setValueExpression("itemLabel", expressionFactory
				.createValueExpression(elContext, "#{status.name}",
						String.class));
		statusEditInput.setValueExpression("itemValue", expressionFactory
				.createValueExpression(elContext, "#{status}", JJStatus.class));
		statusEditInput.setConverter(new JJStatusConverter());
		statusEditInput.setRequired(false);
		htmlPanelGrid.getChildren().add(statusEditInput);

		Message statusEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		statusEditInputMessage.setId("statusEditInputMessage");
		statusEditInputMessage.setFor("statusEditInput");
		statusEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(statusEditInputMessage);

		OutputLabel requirementEditOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		requirementEditOutput.setFor("requirementEditInput");
		requirementEditOutput.setId("requirementEditOutput");
		requirementEditOutput.setValue("Requirement:");
		htmlPanelGrid.getChildren().add(requirementEditOutput);

		AutoComplete requirementEditInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		requirementEditInput.setId("requirementEditInput");
		requirementEditInput
				.setValueExpression("value", expressionFactory
						.createValueExpression(elContext,
								"#{jJBugBean.JJBug_.requirement}",
								JJRequirement.class));
		requirementEditInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{jJBugBean.completeRequirement}", List.class,
						new Class[] { String.class }));
		requirementEditInput.setDropdown(true);
		requirementEditInput.setValueExpression("var", expressionFactory
				.createValueExpression(elContext, "requirement", String.class));
		requirementEditInput.setValueExpression("itemLabel", expressionFactory
				.createValueExpression(elContext, "#{requirement.name}",
						String.class));
		requirementEditInput.setValueExpression("itemValue", expressionFactory
				.createValueExpression(elContext, "#{requirement}",
						JJRequirement.class));
		requirementEditInput.setConverter(new JJRequirementConverter());
		requirementEditInput.setRequired(false);
		htmlPanelGrid.getChildren().add(requirementEditInput);

		Message requirementEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		requirementEditInputMessage.setId("requirementEditInputMessage");
		requirementEditInputMessage.setFor("requirementEditInput");
		requirementEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(requirementEditInputMessage);

		OutputLabel teststepEditOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		teststepEditOutput.setFor("teststepEditInput");
		teststepEditOutput.setId("teststepEditOutput");
		teststepEditOutput.setValue("Teststep:");
		htmlPanelGrid.getChildren().add(teststepEditOutput);

		AutoComplete teststepEditInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		teststepEditInput.setId("teststepEditInput");
		teststepEditInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJBugBean.JJBug_.teststep}", JJTeststep.class));
		teststepEditInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{jJBugBean.completeTeststep}", List.class,
						new Class[] { String.class }));
		teststepEditInput.setDropdown(true);
		teststepEditInput.setValueExpression("var", expressionFactory
				.createValueExpression(elContext, "teststep", String.class));
		teststepEditInput.setValueExpression("itemLabel", expressionFactory
				.createValueExpression(elContext, "#{teststep.name}",
						String.class));
		teststepEditInput.setValueExpression("itemValue", expressionFactory
				.createValueExpression(elContext, "#{teststep}",
						JJTeststep.class));
		teststepEditInput.setConverter(new JJTeststepConverter());
		teststepEditInput.setRequired(false);
		htmlPanelGrid.getChildren().add(teststepEditInput);

		Message teststepEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		teststepEditInputMessage.setId("teststepEditInputMessage");
		teststepEditInputMessage.setFor("teststepEditInput");
		teststepEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(teststepEditInputMessage);

		OutputLabel relationEditOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		relationEditOutput.setFor("relationEditInput");
		relationEditOutput.setId("relationEditOutput");
		relationEditOutput.setValue("Relation:");
		htmlPanelGrid.getChildren().add(relationEditOutput);

		AutoComplete relationEditInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		relationEditInput.setId("relationEditInput");
		relationEditInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJBugBean.JJBug_.relation}", JJRelationship.class));
		relationEditInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{jJBugBean.completeRelation}", List.class,
						new Class[] { String.class }));
		relationEditInput.setDropdown(true);
		relationEditInput.setRequired(false);
		htmlPanelGrid.getChildren().add(relationEditInput);

		Message relationEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		relationEditInputMessage.setId("relationEditInputMessage");
		relationEditInputMessage.setFor("relationEditInput");
		relationEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(relationEditInputMessage);

		OutputLabel sprintEditOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		sprintEditOutput.setFor("sprintEditInput");
		sprintEditOutput.setId("sprintEditOutput");
		sprintEditOutput.setValue("Sprint:");
		htmlPanelGrid.getChildren().add(sprintEditOutput);

		AutoComplete sprintEditInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		sprintEditInput.setId("sprintEditInput");
		sprintEditInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{jJBugBean.JJBug_.sprint}",
						JJSprint.class));
		sprintEditInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{jJBugBean.completeSprint}", List.class,
						new Class[] { String.class }));
		sprintEditInput.setDropdown(true);
		sprintEditInput.setValueExpression("var", expressionFactory
				.createValueExpression(elContext, "sprint", String.class));
		sprintEditInput.setValueExpression("itemLabel", expressionFactory
				.createValueExpression(elContext, "#{sprint.name}",
						String.class));
		sprintEditInput.setValueExpression("itemValue", expressionFactory
				.createValueExpression(elContext, "#{sprint}", JJSprint.class));
		sprintEditInput.setConverter(new JJSprintConverter());
		sprintEditInput.setRequired(false);
		htmlPanelGrid.getChildren().add(sprintEditInput);

		Message sprintEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		sprintEditInputMessage.setId("sprintEditInputMessage");
		sprintEditInputMessage.setFor("sprintEditInput");
		sprintEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(sprintEditInputMessage);

		OutputLabel buildEditOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		buildEditOutput.setFor("buildEditInput");
		buildEditOutput.setId("buildEditOutput");
		buildEditOutput.setValue("Build:");
		htmlPanelGrid.getChildren().add(buildEditOutput);

		AutoComplete buildEditInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		buildEditInput.setId("buildEditInput");
		buildEditInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{jJBugBean.JJBug_.build}",
						JJBuild.class));
		buildEditInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{jJBugBean.completeBuild}", List.class,
						new Class[] { String.class }));
		buildEditInput.setDropdown(true);
		buildEditInput.setValueExpression("var", expressionFactory
				.createValueExpression(elContext, "build", String.class));
		buildEditInput.setValueExpression("itemLabel",
				expressionFactory.createValueExpression(elContext,
						"#{build.name}", String.class));
		buildEditInput.setValueExpression("itemValue", expressionFactory
				.createValueExpression(elContext, "#{build}", JJBuild.class));
		buildEditInput.setConverter(new JJBuildConverter());
		buildEditInput.setRequired(false);
		htmlPanelGrid.getChildren().add(buildEditInput);

		Message buildEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		buildEditInputMessage.setId("buildEditInputMessage");
		buildEditInputMessage.setFor("buildEditInput");
		buildEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(buildEditInputMessage);

		HtmlOutputText bugsEditOutput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		bugsEditOutput.setId("bugsEditOutput");
		bugsEditOutput.setValue("Bugs:");
		htmlPanelGrid.getChildren().add(bugsEditOutput);

		HtmlOutputText bugsEditInput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		bugsEditInput.setId("bugsEditInput");
		bugsEditInput
				.setValue("This relationship is managed from the JJBug side");
		htmlPanelGrid.getChildren().add(bugsEditInput);

		Message bugsEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		bugsEditInputMessage.setId("bugsEditInputMessage");
		bugsEditInputMessage.setFor("bugsEditInput");
		bugsEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(bugsEditInputMessage);

		OutputLabel bugUpEditOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		bugUpEditOutput.setFor("bugUpEditInput");
		bugUpEditOutput.setId("bugUpEditOutput");
		bugUpEditOutput.setValue("Bug Up:");
		htmlPanelGrid.getChildren().add(bugUpEditOutput);

		AutoComplete bugUpEditInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		bugUpEditInput.setId("bugUpEditInput");
		bugUpEditInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{jJBugBean.JJBug_.bugUp}",
						JJBug.class));
		bugUpEditInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{jJBugBean.completeBugUp}", List.class,
						new Class[] { String.class }));
		bugUpEditInput.setDropdown(true);
		bugUpEditInput.setValueExpression("var", expressionFactory
				.createValueExpression(elContext, "bugUp", String.class));
		bugUpEditInput.setValueExpression("itemLabel",
				expressionFactory.createValueExpression(elContext,
						"#{bugUp.name}", String.class));
		bugUpEditInput.setValueExpression("itemValue", expressionFactory
				.createValueExpression(elContext, "#{bugUp}", JJBug.class));
		bugUpEditInput.setConverter(new JJBugConverter());
		bugUpEditInput.setRequired(false);
		htmlPanelGrid.getChildren().add(bugUpEditInput);

		Message bugUpEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		bugUpEditInputMessage.setId("bugUpEditInputMessage");
		bugUpEditInputMessage.setFor("bugUpEditInput");
		bugUpEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(bugUpEditInputMessage);

		HtmlOutputText tasksEditOutput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		tasksEditOutput.setId("tasksEditOutput");
		tasksEditOutput.setValue("Tasks:");
		htmlPanelGrid.getChildren().add(tasksEditOutput);

		HtmlOutputText tasksEditInput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		tasksEditInput.setId("tasksEditInput");
		tasksEditInput
				.setValue("This relationship is managed from the JJTask side");
		htmlPanelGrid.getChildren().add(tasksEditInput);

		Message tasksEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		tasksEditInputMessage.setId("tasksEditInputMessage");
		tasksEditInputMessage.setFor("tasksEditInput");
		tasksEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(tasksEditInputMessage);

		OutputLabel assignedTosEditOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		assignedTosEditOutput.setFor("assignedTosEditInput");
		assignedTosEditOutput.setId("assignedTosEditOutput");
		assignedTosEditOutput.setValue("Assigned Tos:");
		htmlPanelGrid.getChildren().add(assignedTosEditOutput);

		AutoComplete assignedTosEditInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		assignedTosEditInput.setId("assignedTosEditInput");
		assignedTosEditInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJBugBean.JJBug_.assignedTos}", JJContact.class));
		assignedTosEditInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{jJBugBean.completeAssignedTos}", List.class,
						new Class[] { String.class }));
		assignedTosEditInput.setDropdown(true);
		assignedTosEditInput.setValueExpression("var", expressionFactory
				.createValueExpression(elContext, "assignedTos", String.class));
		assignedTosEditInput.setValueExpression("itemLabel", expressionFactory
				.createValueExpression(elContext, "#{assignedTos.name}",
						String.class));
		assignedTosEditInput.setValueExpression("itemValue", expressionFactory
				.createValueExpression(elContext, "#{assignedTos}",
						JJContact.class));
		assignedTosEditInput.setConverter(new JJContactConverter());
		assignedTosEditInput.setRequired(false);
		htmlPanelGrid.getChildren().add(assignedTosEditInput);

		Message assignedTosEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		assignedTosEditInputMessage.setId("assignedTosEditInputMessage");
		assignedTosEditInputMessage.setFor("assignedTosEditInput");
		assignedTosEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(assignedTosEditInputMessage);

		HtmlOutputText messagesEditOutput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		messagesEditOutput.setId("messagesEditOutput");
		messagesEditOutput.setValue("Messages:");
		htmlPanelGrid.getChildren().add(messagesEditOutput);

		HtmlOutputText messagesEditInput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		messagesEditInput.setId("messagesEditInput");
		messagesEditInput
				.setValue("This relationship is managed from the JJMessage side");
		htmlPanelGrid.getChildren().add(messagesEditInput);

		Message messagesEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		messagesEditInputMessage.setId("messagesEditInputMessage");
		messagesEditInputMessage.setFor("messagesEditInput");
		messagesEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(messagesEditInputMessage);

		return htmlPanelGrid;
	}

	public HtmlPanelGrid populateCreatePanel() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		javax.faces.application.Application application = facesContext
				.getApplication();
		ExpressionFactory expressionFactory = application
				.getExpressionFactory();
		ELContext elContext = facesContext.getELContext();

		HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application
				.createComponent(HtmlPanelGrid.COMPONENT_TYPE);

		OutputLabel nameCreateOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		nameCreateOutput.setFor("nameCreateInput");
		nameCreateOutput.setId("nameCreateOutput");
		nameCreateOutput.setValue("Name:");
		htmlPanelGrid.getChildren().add(nameCreateOutput);

		InputTextarea nameCreateInput = (InputTextarea) application
				.createComponent(InputTextarea.COMPONENT_TYPE);
		nameCreateInput.setId("nameCreateInput");
		nameCreateInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{jJBugBean.JJBug_.name}",
						String.class));
		LengthValidator nameCreateInputValidator = new LengthValidator();
		nameCreateInputValidator.setMaximum(100);
		nameCreateInput.addValidator(nameCreateInputValidator);
		nameCreateInput.setRequired(true);
		htmlPanelGrid.getChildren().add(nameCreateInput);

		Message nameCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		nameCreateInputMessage.setId("nameCreateInputMessage");
		nameCreateInputMessage.setFor("nameCreateInput");
		nameCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(nameCreateInputMessage);

		OutputLabel descriptionCreateOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		descriptionCreateOutput.setFor("descriptionCreateInput");
		descriptionCreateOutput.setId("descriptionCreateOutput");
		descriptionCreateOutput.setValue("Description:");
		htmlPanelGrid.getChildren().add(descriptionCreateOutput);

		InputTextarea descriptionCreateInput = (InputTextarea) application
				.createComponent(InputTextarea.COMPONENT_TYPE);
		descriptionCreateInput.setId("descriptionCreateInput");
		descriptionCreateInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJBugBean.JJBug_.description}", String.class));
		descriptionCreateInput.setRequired(true);
		htmlPanelGrid.getChildren().add(descriptionCreateInput);

		Message descriptionCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		descriptionCreateInputMessage.setId("descriptionCreateInputMessage");
		descriptionCreateInputMessage.setFor("descriptionCreateInput");
		descriptionCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(descriptionCreateInputMessage);

		OutputLabel categoryCreateOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		categoryCreateOutput.setFor("categoryCreateInput");
		categoryCreateOutput.setId("categoryCreateOutput");
		categoryCreateOutput.setValue("Category:");
		htmlPanelGrid.getChildren().add(categoryCreateOutput);

		AutoComplete categoryCreateInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		categoryCreateInput.setId("categoryCreateInput");
		categoryCreateInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJBugBean.JJBug_.category}", JJCategory.class));
		categoryCreateInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{jJBugBean.completeCategory}", List.class,
						new Class[] { String.class }));
		categoryCreateInput.setDropdown(true);
		categoryCreateInput.setValueExpression("var", expressionFactory
				.createValueExpression(elContext, "category", String.class));
		categoryCreateInput.setValueExpression("itemLabel", expressionFactory
				.createValueExpression(elContext, "#{category.name}",
						String.class));
		categoryCreateInput.setValueExpression("itemValue", expressionFactory
				.createValueExpression(elContext, "#{category}",
						JJCategory.class));
		categoryCreateInput.setConverter(new JJCategoryConverter());
		categoryCreateInput.setRequired(false);
		htmlPanelGrid.getChildren().add(categoryCreateInput);

		Message categoryCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		categoryCreateInputMessage.setId("categoryCreateInputMessage");
		categoryCreateInputMessage.setFor("categoryCreateInput");
		categoryCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(categoryCreateInputMessage);

		OutputLabel criticityCreateOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		criticityCreateOutput.setFor("criticityCreateInput");
		criticityCreateOutput.setId("criticityCreateOutput");
		criticityCreateOutput.setValue("Criticity:");
		htmlPanelGrid.getChildren().add(criticityCreateOutput);

		AutoComplete criticityCreateInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		criticityCreateInput.setId("criticityCreateInput");
		criticityCreateInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJBugBean.JJBug_.criticity}", JJCriticity.class));
		criticityCreateInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{jJBugBean.completeCriticityBug}", List.class,
						new Class[] { String.class }));
		criticityCreateInput.setDropdown(true);
		criticityCreateInput.setValueExpression("var", expressionFactory
				.createValueExpression(elContext, "criticity", String.class));
		criticityCreateInput.setValueExpression("itemLabel", expressionFactory
				.createValueExpression(elContext, "#{criticity.name}",
						String.class));
		criticityCreateInput.setValueExpression("itemValue", expressionFactory
				.createValueExpression(elContext, "#{criticity}",
						JJCriticity.class));
		criticityCreateInput.setConverter(new JJCriticityConverter());
		criticityCreateInput.setRequired(false);
		htmlPanelGrid.getChildren().add(criticityCreateInput);

		Message criticityCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		criticityCreateInputMessage.setId("criticityCreateInputMessage");
		criticityCreateInputMessage.setFor("criticityCreateInput");
		criticityCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(criticityCreateInputMessage);

		OutputLabel importanceCreateOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		importanceCreateOutput.setFor("importanceCreateInput");
		importanceCreateOutput.setId("importanceCreateOutput");
		importanceCreateOutput.setValue("Importance:");
		htmlPanelGrid.getChildren().add(importanceCreateOutput);

		AutoComplete importanceCreateInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		importanceCreateInput.setId("importanceCreateInput");
		importanceCreateInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJBugBean.JJBug_.importance}", JJImportance.class));
		importanceCreateInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{jJBugBean.completeImportance}", List.class,
						new Class[] { String.class }));
		importanceCreateInput.setDropdown(true);
		importanceCreateInput.setValueExpression("var", expressionFactory
				.createValueExpression(elContext, "importance", String.class));
		importanceCreateInput.setValueExpression("itemLabel", expressionFactory
				.createValueExpression(elContext, "#{importance.name}",
						String.class));
		importanceCreateInput.setValueExpression("itemValue", expressionFactory
				.createValueExpression(elContext, "#{importance}",
						JJImportance.class));
		importanceCreateInput.setConverter(new JJImportanceConverter());
		importanceCreateInput.setRequired(false);
		htmlPanelGrid.getChildren().add(importanceCreateInput);

		Message importanceCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		importanceCreateInputMessage.setId("importanceCreateInputMessage");
		importanceCreateInputMessage.setFor("importanceCreateInput");
		importanceCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(importanceCreateInputMessage);

		OutputLabel statusCreateOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		statusCreateOutput.setFor("statusCreateInput");
		statusCreateOutput.setId("statusCreateOutput");
		statusCreateOutput.setValue("Status:");
		htmlPanelGrid.getChildren().add(statusCreateOutput);

		AutoComplete statusCreateInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		statusCreateInput.setId("statusCreateInput");
		statusCreateInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{jJBugBean.JJBug_.status}",
						JJStatus.class));
		statusCreateInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{jJBugBean.completeStatusBug}", List.class,
						new Class[] { String.class }));
		statusCreateInput.setDropdown(true);
		statusCreateInput.setValueExpression("var", expressionFactory
				.createValueExpression(elContext, "status", String.class));
		statusCreateInput.setValueExpression("itemLabel", expressionFactory
				.createValueExpression(elContext, "#{status.name}",
						String.class));
		statusCreateInput.setValueExpression("itemValue", expressionFactory
				.createValueExpression(elContext, "#{status}", JJStatus.class));
		statusCreateInput.setConverter(new JJStatusConverter());
		statusCreateInput.setRequired(false);
		htmlPanelGrid.getChildren().add(statusCreateInput);

		Message statusCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		statusCreateInputMessage.setId("statusCreateInputMessage");
		statusCreateInputMessage.setFor("statusCreateInput");
		statusCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(statusCreateInputMessage);

		OutputLabel requirementCreateOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		requirementCreateOutput.setFor("requirementCreateInput");
		requirementCreateOutput.setId("requirementCreateOutput");
		requirementCreateOutput.setValue("Requirement:");
		htmlPanelGrid.getChildren().add(requirementCreateOutput);

		AutoComplete requirementCreateInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		requirementCreateInput.setId("requirementCreateInput");
		requirementCreateInput
				.setValueExpression("value", expressionFactory
						.createValueExpression(elContext,
								"#{jJBugBean.JJBug_.requirement}",
								JJRequirement.class));
		requirementCreateInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{jJBugBean.completeRequirement}", List.class,
						new Class[] { String.class }));
		requirementCreateInput.setDropdown(true);
		requirementCreateInput.setValueExpression("var", expressionFactory
				.createValueExpression(elContext, "requirement", String.class));
		requirementCreateInput.setValueExpression("itemLabel",
				expressionFactory.createValueExpression(elContext,
						"#{requirement.name}", String.class));
		requirementCreateInput.setValueExpression("itemValue",
				expressionFactory.createValueExpression(elContext,
						"#{requirement}", JJRequirement.class));
		requirementCreateInput.setConverter(new JJRequirementConverter());
		requirementCreateInput.setRequired(false);
		htmlPanelGrid.getChildren().add(requirementCreateInput);

		Message requirementCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		requirementCreateInputMessage.setId("requirementCreateInputMessage");
		requirementCreateInputMessage.setFor("requirementCreateInput");
		requirementCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(requirementCreateInputMessage);

		OutputLabel teststepCreateOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		teststepCreateOutput.setFor("teststepCreateInput");
		teststepCreateOutput.setId("teststepCreateOutput");
		teststepCreateOutput.setValue("Teststep:");
		htmlPanelGrid.getChildren().add(teststepCreateOutput);

		AutoComplete teststepCreateInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		teststepCreateInput.setId("teststepCreateInput");
		teststepCreateInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJBugBean.JJBug_.teststep}", JJTeststep.class));
		teststepCreateInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{jJBugBean.completeTeststep}", List.class,
						new Class[] { String.class }));
		teststepCreateInput.setDropdown(true);
		teststepCreateInput.setValueExpression("var", expressionFactory
				.createValueExpression(elContext, "teststep", String.class));
		teststepCreateInput.setValueExpression("itemLabel", expressionFactory
				.createValueExpression(elContext, "#{teststep.name}",
						String.class));
		teststepCreateInput.setValueExpression("itemValue", expressionFactory
				.createValueExpression(elContext, "#{teststep}",
						JJTeststep.class));
		teststepCreateInput.setConverter(new JJTeststepConverter());
		teststepCreateInput.setRequired(false);
		htmlPanelGrid.getChildren().add(teststepCreateInput);

		Message teststepCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		teststepCreateInputMessage.setId("teststepCreateInputMessage");
		teststepCreateInputMessage.setFor("teststepCreateInput");
		teststepCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(teststepCreateInputMessage);

		OutputLabel relationCreateOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		relationCreateOutput.setFor("relationCreateInput");
		relationCreateOutput.setId("relationCreateOutput");
		relationCreateOutput.setValue("Relation:");
		htmlPanelGrid.getChildren().add(relationCreateOutput);

		AutoComplete relationCreateInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		relationCreateInput.setId("relationCreateInput");
		relationCreateInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJBugBean.JJBug_.relation}", JJRelationship.class));
		relationCreateInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{jJBugBean.completeRelation}", List.class,
						new Class[] { String.class }));
		relationCreateInput.setDropdown(true);
		relationCreateInput.setRequired(false);
		htmlPanelGrid.getChildren().add(relationCreateInput);

		Message relationCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		relationCreateInputMessage.setId("relationCreateInputMessage");
		relationCreateInputMessage.setFor("relationCreateInput");
		relationCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(relationCreateInputMessage);

		OutputLabel sprintCreateOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		sprintCreateOutput.setFor("sprintCreateInput");
		sprintCreateOutput.setId("sprintCreateOutput");
		sprintCreateOutput.setValue("Sprint:");
		htmlPanelGrid.getChildren().add(sprintCreateOutput);

		AutoComplete sprintCreateInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		sprintCreateInput.setId("sprintCreateInput");
		sprintCreateInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{jJBugBean.JJBug_.sprint}",
						JJSprint.class));
		sprintCreateInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{jJBugBean.completeSprint}", List.class,
						new Class[] { String.class }));
		sprintCreateInput.setDropdown(true);
		sprintCreateInput.setValueExpression("var", expressionFactory
				.createValueExpression(elContext, "sprint", String.class));
		sprintCreateInput.setValueExpression("itemLabel", expressionFactory
				.createValueExpression(elContext, "#{sprint.name}",
						String.class));
		sprintCreateInput.setValueExpression("itemValue", expressionFactory
				.createValueExpression(elContext, "#{sprint}", JJSprint.class));
		sprintCreateInput.setConverter(new JJSprintConverter());
		sprintCreateInput.setRequired(false);
		htmlPanelGrid.getChildren().add(sprintCreateInput);

		Message sprintCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		sprintCreateInputMessage.setId("sprintCreateInputMessage");
		sprintCreateInputMessage.setFor("sprintCreateInput");
		sprintCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(sprintCreateInputMessage);

		OutputLabel buildCreateOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		buildCreateOutput.setFor("buildCreateInput");
		buildCreateOutput.setId("buildCreateOutput");
		buildCreateOutput.setValue("Build:");
		htmlPanelGrid.getChildren().add(buildCreateOutput);

		AutoComplete buildCreateInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		buildCreateInput.setId("buildCreateInput");
		buildCreateInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{jJBugBean.JJBug_.build}",
						JJBuild.class));
		buildCreateInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{jJBugBean.completeBuild}", List.class,
						new Class[] { String.class }));
		buildCreateInput.setDropdown(true);
		buildCreateInput.setValueExpression("var", expressionFactory
				.createValueExpression(elContext, "build", String.class));
		buildCreateInput.setValueExpression("itemLabel",
				expressionFactory.createValueExpression(elContext,
						"#{build.name}", String.class));
		buildCreateInput.setValueExpression("itemValue", expressionFactory
				.createValueExpression(elContext, "#{build}", JJBuild.class));
		buildCreateInput.setConverter(new JJBuildConverter());
		buildCreateInput.setRequired(false);
		htmlPanelGrid.getChildren().add(buildCreateInput);

		Message buildCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		buildCreateInputMessage.setId("buildCreateInputMessage");
		buildCreateInputMessage.setFor("buildCreateInput");
		buildCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(buildCreateInputMessage);

		HtmlOutputText bugsCreateOutput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		bugsCreateOutput.setId("bugsCreateOutput");
		bugsCreateOutput.setValue("Bugs:");
		htmlPanelGrid.getChildren().add(bugsCreateOutput);

		HtmlOutputText bugsCreateInput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		bugsCreateInput.setId("bugsCreateInput");
		bugsCreateInput
				.setValue("This relationship is managed from the JJBug side");
		htmlPanelGrid.getChildren().add(bugsCreateInput);

		Message bugsCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		bugsCreateInputMessage.setId("bugsCreateInputMessage");
		bugsCreateInputMessage.setFor("bugsCreateInput");
		bugsCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(bugsCreateInputMessage);

		OutputLabel bugUpCreateOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		bugUpCreateOutput.setFor("bugUpCreateInput");
		bugUpCreateOutput.setId("bugUpCreateOutput");
		bugUpCreateOutput.setValue("Bug Up:");
		htmlPanelGrid.getChildren().add(bugUpCreateOutput);

		AutoComplete bugUpCreateInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		bugUpCreateInput.setId("bugUpCreateInput");
		bugUpCreateInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{jJBugBean.JJBug_.bugUp}",
						JJBug.class));
		bugUpCreateInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{jJBugBean.completeBugUp}", List.class,
						new Class[] { String.class }));
		bugUpCreateInput.setDropdown(true);
		bugUpCreateInput.setValueExpression("var", expressionFactory
				.createValueExpression(elContext, "bugUp", String.class));
		bugUpCreateInput.setValueExpression("itemLabel",
				expressionFactory.createValueExpression(elContext,
						"#{bugUp.name}", String.class));
		bugUpCreateInput.setValueExpression("itemValue", expressionFactory
				.createValueExpression(elContext, "#{bugUp}", JJBug.class));
		bugUpCreateInput.setConverter(new JJBugConverter());
		bugUpCreateInput.setRequired(false);
		htmlPanelGrid.getChildren().add(bugUpCreateInput);

		Message bugUpCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		bugUpCreateInputMessage.setId("bugUpCreateInputMessage");
		bugUpCreateInputMessage.setFor("bugUpCreateInput");
		bugUpCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(bugUpCreateInputMessage);

		HtmlOutputText tasksCreateOutput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		tasksCreateOutput.setId("tasksCreateOutput");
		tasksCreateOutput.setValue("Tasks:");
		htmlPanelGrid.getChildren().add(tasksCreateOutput);

		HtmlOutputText tasksCreateInput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		tasksCreateInput.setId("tasksCreateInput");
		tasksCreateInput
				.setValue("This relationship is managed from the JJTask side");
		htmlPanelGrid.getChildren().add(tasksCreateInput);

		Message tasksCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		tasksCreateInputMessage.setId("tasksCreateInputMessage");
		tasksCreateInputMessage.setFor("tasksCreateInput");
		tasksCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(tasksCreateInputMessage);

		OutputLabel assignedTosCreateOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		assignedTosCreateOutput.setFor("assignedTosCreateInput");
		assignedTosCreateOutput.setId("assignedTosCreateOutput");
		assignedTosCreateOutput.setValue("Assigned Tos:");
		htmlPanelGrid.getChildren().add(assignedTosCreateOutput);

		AutoComplete assignedTosCreateInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		assignedTosCreateInput.setId("assignedTosCreateInput");
		assignedTosCreateInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJBugBean.JJBug_.assignedTos}", JJContact.class));
		assignedTosCreateInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{jJBugBean.completeAssignedTos}", List.class,
						new Class[] { String.class }));
		assignedTosCreateInput.setDropdown(true);
		assignedTosCreateInput.setValueExpression("var", expressionFactory
				.createValueExpression(elContext, "assignedTos", String.class));
		assignedTosCreateInput.setValueExpression("itemLabel",
				expressionFactory.createValueExpression(elContext,
						"#{assignedTos.name}", String.class));
		assignedTosCreateInput.setValueExpression("itemValue",
				expressionFactory.createValueExpression(elContext,
						"#{assignedTos}", JJContact.class));
		assignedTosCreateInput.setConverter(new JJContactConverter());
		assignedTosCreateInput.setRequired(false);
		htmlPanelGrid.getChildren().add(assignedTosCreateInput);

		Message assignedTosCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		assignedTosCreateInputMessage.setId("assignedTosCreateInputMessage");
		assignedTosCreateInputMessage.setFor("assignedTosCreateInput");
		assignedTosCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(assignedTosCreateInputMessage);

		HtmlOutputText messagesCreateOutput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		messagesCreateOutput.setId("messagesCreateOutput");
		messagesCreateOutput.setValue("Messages:");
		htmlPanelGrid.getChildren().add(messagesCreateOutput);

		HtmlOutputText messagesCreateInput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		messagesCreateInput.setId("messagesCreateInput");
		messagesCreateInput
				.setValue("This relationship is managed from the JJMessage side");
		htmlPanelGrid.getChildren().add(messagesCreateInput);

		Message messagesCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		messagesCreateInputMessage.setId("messagesCreateInputMessage");
		messagesCreateInputMessage.setFor("messagesCreateInput");
		messagesCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(messagesCreateInputMessage);

		return htmlPanelGrid;
	}

	public HtmlPanelGrid populateViewPanel() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		javax.faces.application.Application application = facesContext
				.getApplication();
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
				.createValueExpression(elContext, "#{jJBugBean.JJBug_.name}",
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
						"#{jJBugBean.JJBug_.description}", String.class));
		descriptionValue.setReadonly(true);
		descriptionValue.setDisabled(true);
		htmlPanelGrid.getChildren().add(descriptionValue);

		HtmlOutputText creationDateLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		creationDateLabel.setId("creationDateLabel");
		creationDateLabel.setValue("Creation Date:");
		htmlPanelGrid.getChildren().add(creationDateLabel);

		HtmlOutputText creationDateValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		creationDateValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJBugBean.JJBug_.creationDate}", Date.class));
		DateTimeConverter creationDateValueConverter = (DateTimeConverter) application
				.createConverter(DateTimeConverter.CONVERTER_ID);
		creationDateValueConverter.setPattern("dd/MM/yyyy");
		creationDateValue.setConverter(creationDateValueConverter);
		htmlPanelGrid.getChildren().add(creationDateValue);

		HtmlOutputText createdByLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		createdByLabel.setId("createdByLabel");
		createdByLabel.setValue("Created By:");
		htmlPanelGrid.getChildren().add(createdByLabel);

		HtmlOutputText createdByValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		createdByValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJBugBean.JJBug_.createdBy}", JJContact.class));
		createdByValue.setConverter(new JJContactConverter());
		htmlPanelGrid.getChildren().add(createdByValue);

		HtmlOutputText updatedDateLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		updatedDateLabel.setId("updatedDateLabel");
		updatedDateLabel.setValue("Updated Date:");
		htmlPanelGrid.getChildren().add(updatedDateLabel);

		HtmlOutputText updatedDateValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		updatedDateValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJBugBean.JJBug_.updatedDate}", Date.class));
		DateTimeConverter updatedDateValueConverter = (DateTimeConverter) application
				.createConverter(DateTimeConverter.CONVERTER_ID);
		updatedDateValueConverter.setPattern("dd/MM/yyyy");
		updatedDateValue.setConverter(updatedDateValueConverter);
		htmlPanelGrid.getChildren().add(updatedDateValue);

		HtmlOutputText updatedByLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		updatedByLabel.setId("updatedByLabel");
		updatedByLabel.setValue("Updated By:");
		htmlPanelGrid.getChildren().add(updatedByLabel);

		HtmlOutputText updatedByValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		updatedByValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJBugBean.JJBug_.updatedBy}", JJContact.class));
		updatedByValue.setConverter(new JJContactConverter());
		htmlPanelGrid.getChildren().add(updatedByValue);

		HtmlOutputText enabledLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		enabledLabel.setId("enabledLabel");
		enabledLabel.setValue("Enabled:");
		htmlPanelGrid.getChildren().add(enabledLabel);

		HtmlOutputText enabledValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		enabledValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJBugBean.JJBug_.enabled}", String.class));
		htmlPanelGrid.getChildren().add(enabledValue);

		HtmlOutputText projectLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		projectLabel.setId("projectLabel");
		projectLabel.setValue("Project:");
		htmlPanelGrid.getChildren().add(projectLabel);

		HtmlOutputText projectValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		projectValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJBugBean.JJBug_.project}", JJProject.class));
		projectValue.setConverter(new JJProjectConverter());
		htmlPanelGrid.getChildren().add(projectValue);

		HtmlOutputText versioningLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		versioningLabel.setId("versioningLabel");
		versioningLabel.setValue("Versioning:");
		htmlPanelGrid.getChildren().add(versioningLabel);

		HtmlOutputText versioningValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		versioningValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJBugBean.JJBug_.versioning}", JJVersion.class));
		versioningValue.setConverter(new JJVersionConverter());
		htmlPanelGrid.getChildren().add(versioningValue);

		HtmlOutputText categoryLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		categoryLabel.setId("categoryLabel");
		categoryLabel.setValue("Category:");
		htmlPanelGrid.getChildren().add(categoryLabel);

		HtmlOutputText categoryValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		categoryValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJBugBean.JJBug_.category}", JJCategory.class));
		categoryValue.setConverter(new JJCategoryConverter());
		htmlPanelGrid.getChildren().add(categoryValue);

		HtmlOutputText criticityLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		criticityLabel.setId("criticityLabel");
		criticityLabel.setValue("Criticity:");
		htmlPanelGrid.getChildren().add(criticityLabel);

		HtmlOutputText criticityValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		criticityValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJBugBean.JJBug_.criticity}", JJCriticity.class));
		criticityValue.setConverter(new JJCriticityConverter());
		htmlPanelGrid.getChildren().add(criticityValue);

		HtmlOutputText importanceLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		importanceLabel.setId("importanceLabel");
		importanceLabel.setValue("Importance:");
		htmlPanelGrid.getChildren().add(importanceLabel);

		HtmlOutputText importanceValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		importanceValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJBugBean.JJBug_.importance}", JJImportance.class));
		importanceValue.setConverter(new JJImportanceConverter());
		htmlPanelGrid.getChildren().add(importanceValue);

		HtmlOutputText statusLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		statusLabel.setId("statusLabel");
		statusLabel.setValue("Status:");
		htmlPanelGrid.getChildren().add(statusLabel);

		HtmlOutputText statusValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		statusValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{jJBugBean.JJBug_.status}",
						JJStatus.class));
		statusValue.setConverter(new JJStatusConverter());
		htmlPanelGrid.getChildren().add(statusValue);

		HtmlOutputText requirementLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		requirementLabel.setId("requirementLabel");
		requirementLabel.setValue("Requirement:");
		htmlPanelGrid.getChildren().add(requirementLabel);

		HtmlOutputText requirementValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		requirementValue
				.setValueExpression("value", expressionFactory
						.createValueExpression(elContext,
								"#{jJBugBean.JJBug_.requirement}",
								JJRequirement.class));
		requirementValue.setConverter(new JJRequirementConverter());
		htmlPanelGrid.getChildren().add(requirementValue);

		HtmlOutputText teststepLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		teststepLabel.setId("teststepLabel");
		teststepLabel.setValue("Teststep:");
		htmlPanelGrid.getChildren().add(teststepLabel);

		HtmlOutputText teststepValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		teststepValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJBugBean.JJBug_.teststep}", JJTeststep.class));
		teststepValue.setConverter(new JJTeststepConverter());
		htmlPanelGrid.getChildren().add(teststepValue);

		HtmlOutputText relationLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		relationLabel.setId("relationLabel");
		relationLabel.setValue("Relation:");
		htmlPanelGrid.getChildren().add(relationLabel);

		HtmlOutputText relationValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		relationValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJBugBean.JJBug_.relation}", String.class));
		htmlPanelGrid.getChildren().add(relationValue);

		HtmlOutputText sprintLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		sprintLabel.setId("sprintLabel");
		sprintLabel.setValue("Sprint:");
		htmlPanelGrid.getChildren().add(sprintLabel);

		HtmlOutputText sprintValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		sprintValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{jJBugBean.JJBug_.sprint}",
						JJSprint.class));
		sprintValue.setConverter(new JJSprintConverter());
		htmlPanelGrid.getChildren().add(sprintValue);

		HtmlOutputText buildLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		buildLabel.setId("buildLabel");
		buildLabel.setValue("Build:");
		htmlPanelGrid.getChildren().add(buildLabel);

		HtmlOutputText buildValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		buildValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{jJBugBean.JJBug_.build}",
						JJBuild.class));
		buildValue.setConverter(new JJBuildConverter());
		htmlPanelGrid.getChildren().add(buildValue);

		HtmlOutputText bugsLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		bugsLabel.setId("bugsLabel");
		bugsLabel.setValue("Bugs:");
		htmlPanelGrid.getChildren().add(bugsLabel);

		HtmlOutputText bugsValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		bugsValue.setId("bugsValue");
		bugsValue.setValue("This relationship is managed from the JJBug side");
		htmlPanelGrid.getChildren().add(bugsValue);

		HtmlOutputText bugUpLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		bugUpLabel.setId("bugUpLabel");
		bugUpLabel.setValue("Bug Up:");
		htmlPanelGrid.getChildren().add(bugUpLabel);

		HtmlOutputText bugUpValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		bugUpValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{jJBugBean.JJBug_.bugUp}",
						JJBug.class));
		bugUpValue.setConverter(new JJBugConverter());
		htmlPanelGrid.getChildren().add(bugUpValue);

		HtmlOutputText tasksLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		tasksLabel.setId("tasksLabel");
		tasksLabel.setValue("Tasks:");
		htmlPanelGrid.getChildren().add(tasksLabel);

		HtmlOutputText tasksValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		tasksValue.setId("tasksValue");
		tasksValue
				.setValue("This relationship is managed from the JJTask side");
		htmlPanelGrid.getChildren().add(tasksValue);

		HtmlOutputText assignedTosLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		assignedTosLabel.setId("assignedTosLabel");
		assignedTosLabel.setValue("Assigned Tos:");
		htmlPanelGrid.getChildren().add(assignedTosLabel);

		HtmlOutputText assignedTosValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		assignedTosValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJBugBean.JJBug_.assignedTos}", JJContact.class));
		assignedTosValue.setConverter(new JJContactConverter());
		htmlPanelGrid.getChildren().add(assignedTosValue);

		HtmlOutputText messagesLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		messagesLabel.setId("messagesLabel");
		messagesLabel.setValue("Messages:");
		htmlPanelGrid.getChildren().add(messagesLabel);

		HtmlOutputText messagesValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		messagesValue.setId("messagesValue");
		messagesValue
				.setValue("This relationship is managed from the JJMessage side");
		htmlPanelGrid.getChildren().add(messagesValue);

		return htmlPanelGrid;
	}

}

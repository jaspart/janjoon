package com.starit.janjoonweb.ui.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;
import javax.faces.event.ActionEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.*;
import com.starit.janjoonweb.ui.mb.converter.*;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJMessage.class, beanName = "jJMessageBean")
public class JJMessageBean {

	private JJMessage message;
	private boolean loadFiltredJJmessage = false;
	private boolean alertOrInfo;
	private SelectItem[] projectOptions;
	private SelectItem[] productOptions;
	private SelectItem[] creatorOptions;
	private SelectItem[] criticityOptions;
	private List<JJMessage> allJJMessages;
	private List<JJMessage> filteredJJMessage;
	private List<String> columns;
	private boolean collapsedMesPanel = true;
	private boolean collapsedLayoutPanel = true;
	private HtmlPanelGrid viewPanel;
	private JJMessage viewedMessage;

	@PostConstruct
	public void init() {
		columns = new ArrayList<String>();
		columns.add("name");
		columns.add("description");
		columns.add("creationDate");
		columns.add("updatedDate");
		columns.add("message");

		if (jJMessageService.findAllJJMessages().isEmpty()) {
			System.out
					.println("----------------------------------------------------------");
			for (int j = 0; j < 30; j++) {
				JJMessage mes = new JJMessage();
				mes.setName("mes : " + j);
				mes.setDescription("mesDescription : " + j);
				mes.setCreationDate(new Date());
				mes.setMessage("message tttttt" + j);
				jJMessageService.saveJJMessage(mes);
			}
		}

		allJJMessages = jJMessageService.findAllJJMessages();
		setMessage(new JJMessage());
		initJJmessageTable(null);

	}

	public List<String> getColumns() {
		return columns;
	}

	public List<JJMessage> getAllJJMessages() {
		return allJJMessages;
	}

	public void setAllJJMessages(List<JJMessage> allJJMessages) {
		this.allJJMessages = allJJMessages;
	}

	public JJMessage getMessage() {
		return message;
	}

	public void setMessage(JJMessage message) {
		this.message = message;
	}

	public HtmlPanelGrid getViewPanel() {
		return populateMessagePanel();
	}

	public void setViewPanel(HtmlPanelGrid viewPanel) {
		this.viewPanel = viewPanel;
	}

	public JJMessage getViewedMessage() {
		return viewedMessage;
	}

	public void setViewedMessage(JJMessage viewedMessage) {
		this.viewedMessage = viewedMessage;
	}

	public List<JJMessage> getFilteredJJMessage() {
		return filteredJJMessage;
	}

	public void setFilteredJJMessage(List<JJMessage> filteredJJMessage) {
		this.filteredJJMessage = filteredJJMessage;
	}

	public boolean isLoadFiltredJJmessage() {
		return loadFiltredJJmessage;
	}

	public void setLoadFiltredJJmessage(boolean loadFiltredJJmessage) {
		this.loadFiltredJJmessage = loadFiltredJJmessage;
	}

	public boolean isCollapsedMesPanel() {
		return collapsedMesPanel;
	}

	public void setCollapsedMesPanel(boolean collapsedMesPanel) {
		this.collapsedMesPanel = collapsedMesPanel;
	}

	public boolean isCollapsedLayoutPanel() {
		return collapsedLayoutPanel;
	}

	public void setCollapsedLayoutPanel(boolean collapsedLayoutPanel) {
		this.collapsedLayoutPanel = collapsedLayoutPanel;
	}

	public boolean isAlertOrInfo() {
		return alertOrInfo;
	}

	public void setAlertOrInfo(boolean alertOrInfo) {
		this.alertOrInfo = alertOrInfo;
	}

	public SelectItem[] getProjectOptions() {

		return projectOptions;

	}

	public SelectItem[] getProductOptions() {
		return productOptions;
	}

	public SelectItem[] getCreatorOptions() {
		return creatorOptions;
	}

	public SelectItem[] getCriticityOptions() {
		return criticityOptions;
	}

	private SelectItem[] createFilterOptions(List<JJAbstractEntity> data) {
		SelectItem[] options = new SelectItem[data.size() + 1];

		options[0] = new SelectItem("", "Select");
		for (int i = 0; i < data.size(); i++) {
			options[i + 1] = new SelectItem(data.get(i).getName(), data.get(i)
					.getName());
		}
		return options;
	}

	public void handleMesToggle(ToggleEvent event) {

		this.collapsedMesPanel = !this.collapsedMesPanel;
	}

	public void handleLayoutToggle(ToggleEvent event) {

		this.collapsedLayoutPanel = !this.collapsedLayoutPanel;
	}

	public void initJJmessageTable(ComponentSystemEvent e) {

		if (!loadFiltredJJmessage) {
			List<JJAbstractEntity> criticities = new ArrayList<JJAbstractEntity>();
			List<JJAbstractEntity> productes = new ArrayList<JJAbstractEntity>();
			List<JJAbstractEntity> projectes = new ArrayList<JJAbstractEntity>();
			List<JJAbstractEntity> contactes = new ArrayList<JJAbstractEntity>();
			allJJMessages = jJMessageService.findAllJJMessages();

			System.out
					.println("----------------initJJmesTable--------------------------");
			for (JJMessage mes : allJJMessages) {
				if (mes.getCreatedBy() != null
						&& !listContaines(contactes, mes.getCreatedBy().getId()))
					contactes.add(mes.getCreatedBy());
				if (mes.getProduct() != null
						&& !listContaines(productes, mes.getProduct().getId()))
					productes.add(mes.getProduct());
				if (mes.getProject() != null
						&& !listContaines(projectes, mes.getProject().getId()))
					projectes.add(mes.getProject());
				if (mes.getCriticity() != null
						&& !listContaines(criticities, mes.getCriticity()
								.getId()))
					criticities.add(mes.getCriticity());
			}
			criticityOptions = createFilterOptions(criticities);
			projectOptions = createFilterOptions(projectes);
			productOptions = createFilterOptions(productes);
			creatorOptions = createFilterOptions(contactes);
			filteredJJMessage = allJJMessages;
			loadFiltredJJmessage = true;

		}
		String referrer = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestHeaderMap().get("referer");
		if (referrer != null) {
			if (!referrer.contains("main")) {
				filteredJJMessage = allJJMessages;
			}
		}

	}

	public boolean listContaines(Object list, Long long1) {
		int i = 0;
		@SuppressWarnings("unchecked")
		List<JJAbstractEntity> list2 = (List<JJAbstractEntity>) list;
		boolean contain = false;
		while (i < list2.size() && !contain) {
			contain = (list2.get(i).getId() == long1);
			i++;
		}

		return contain;
	}

	public void save(JJMessage mes) {
		String message = "";
		if (mes.getId() != null) {
			jJMessageService.updateJJMessage(mes);
			message = "message_successfully_updated";
		} else {
			jJMessageService.saveJJMessage(mes);
			message = "message_successfully_created";
		}

		FacesMessage facesMessage = MessageFactory.getMessage(message,
				"JJMessage");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		reset();
	}

	public void createMessage(ActionEvent e) {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);

		JJCriticityBean jJCriticityBean = (JJCriticityBean) session
				.getAttribute("jJCriticityBean");
		if (jJCriticityBean == null) {
			jJCriticityBean = new JJCriticityBean();
			jJCriticityBean.findAllJJCriticitys();

		}

		List<JJCriticity> criticity = jJCriticityBean.getAllJJCriticitys();

		if (alertOrInfo) {
			System.out.println("info");
			int i = 0;
			while (i < criticity.size()) {
				if (criticity.get(i).getName().equalsIgnoreCase("Info")) {
					message.setCriticity(criticity.get(i));
					i = criticity.size();
				}
				i++;
			}

		} else {
			int i = 0;
			while (i < criticity.size()) {
				if (criticity.get(i).getName().equalsIgnoreCase("Alert")) {
					message.setCriticity(criticity.get(i));
					i = criticity.size();
				}
				i++;
			}
			System.out.println("alert");
		}
		try {
			message.setName(message.getMessage().substring(0, 20));
		} catch (StringIndexOutOfBoundsException c) {
			message.setName(message.getMessage());
		}
		// set attribute
		JJVersionBean jJVersionBean = (JJVersionBean) session
				.getAttribute("jJVersionBean");
		JJProductBean jJProductBean = (JJProductBean) session
				.getAttribute("jJProductBean");
		JJProjectBean jJProjectBean = (JJProjectBean) session
				.getAttribute("jJProjectBean");
		JJContact contact = (JJContact) session.getAttribute("JJContact");
		if (jJProductBean.getProduct() != null)
			message.setProduct(jJProductBean.getProduct());
		if (jJProjectBean.getProject() != null)
			message.setProject(jJProjectBean.getProject());
		if (jJVersionBean.getVersion() != null)
			message.setVersioning(jJVersionBean.getVersion());

		message.setContact(contact);
		message.setCreatedBy(contact);

		message.setCreationDate(new Date());
		message.setDescription(message.getMessage());
		save(message);
		message = new JJMessage();
	}

	public void onRowSelect(SelectEvent event) {

		viewedMessage = (JJMessage) event.getObject();

	}

	public void reset() {

		setAllJJMessages(jJMessageService.findAllJJMessages());
		RequestContext context = RequestContext.getCurrentInstance();
		context.update("messagePanel");
		if (FacesContext.getCurrentInstance().getViewRoot().getViewId()
				.contains("main"))
			context.update("contentPanel");
		setJJMessage_(null);
		setCreateDialogVisible(false);
		loadFiltredJJmessage = false;
		initJJmessageTable(null);
	}

	public HtmlPanelGrid populateMessagePanel() {
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
				.createValueExpression(elContext,
						"#{jJMessageBean.viewedMessage.name}", String.class));
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
						"#{jJMessageBean.viewedMessage.description}",
						String.class));
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
						"#{jJMessageBean.viewedMessage.creationDate}",
						Date.class));
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
						"#{jJMessageBean.viewedMessage.createdBy}",
						JJContact.class));
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
						"#{jJMessageBean.viewedMessage.updatedDate}",
						Date.class));
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
						"#{jJMessageBean.viewedMessage.updatedBy}",
						JJContact.class));
		updatedByValue.setConverter(new JJContactConverter());
		htmlPanelGrid.getChildren().add(updatedByValue);

		HtmlOutputText enabledLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		enabledLabel.setId("enabledLabel");
		enabledLabel.setValue("Enabled:");
		htmlPanelGrid.getChildren().add(enabledLabel);

		HtmlOutputText enabledValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		enabledValue
				.setValueExpression("value", expressionFactory
						.createValueExpression(elContext,
								"#{jJMessageBean.viewedMessage.enabled}",
								String.class));
		htmlPanelGrid.getChildren().add(enabledValue);

		HtmlOutputText messageLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		messageLabel.setId("messageLabel");
		messageLabel.setValue("Message:");
		htmlPanelGrid.getChildren().add(messageLabel);

		InputTextarea messageValue = (InputTextarea) application
				.createComponent(InputTextarea.COMPONENT_TYPE);
		messageValue.setId("messageValue");
		messageValue
				.setValueExpression("value", expressionFactory
						.createValueExpression(elContext,
								"#{jJMessageBean.viewedMessage.message}",
								String.class));
		messageValue.setReadonly(true);
		messageValue.setDisabled(true);
		htmlPanelGrid.getChildren().add(messageValue);

		HtmlOutputText bugLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		bugLabel.setId("bugLabel");
		bugLabel.setValue("Bug:");
		htmlPanelGrid.getChildren().add(bugLabel);

		HtmlOutputText bugValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		bugValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJMessageBean.viewedMessage.bug}", JJBug.class));
		bugValue.setConverter(new JJBugConverter());
		htmlPanelGrid.getChildren().add(bugValue);

		HtmlOutputText buildLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		buildLabel.setId("buildLabel");
		buildLabel.setValue("Build:");
		htmlPanelGrid.getChildren().add(buildLabel);

		HtmlOutputText buildValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		buildValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJMessageBean.viewedMessage.build}", JJBuild.class));
		buildValue.setConverter(new JJBuildConverter());
		htmlPanelGrid.getChildren().add(buildValue);

		HtmlOutputText chapterLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		chapterLabel.setId("chapterLabel");
		chapterLabel.setValue("Chapter:");
		htmlPanelGrid.getChildren().add(chapterLabel);

		HtmlOutputText chapterValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		chapterValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJMessageBean.viewedMessage.chapter}",
						JJChapter.class));
		chapterValue.setConverter(new JJChapterConverter());
		htmlPanelGrid.getChildren().add(chapterValue);

		HtmlOutputText contactLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		contactLabel.setId("contactLabel");
		contactLabel.setValue("Contact:");
		htmlPanelGrid.getChildren().add(contactLabel);

		HtmlOutputText contactValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		contactValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJMessageBean.viewedMessage.contact}",
						JJContact.class));
		contactValue.setConverter(new JJContactConverter());
		htmlPanelGrid.getChildren().add(contactValue);

		HtmlOutputText criticityLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		criticityLabel.setId("criticityLabel");
		criticityLabel.setValue("Criticity:");
		htmlPanelGrid.getChildren().add(criticityLabel);

		HtmlOutputText criticityValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		criticityValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJMessageBean.viewedMessage.criticity}",
						JJCriticity.class));
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
						"#{jJMessageBean.viewedMessage.importance}",
						JJImportance.class));
		importanceValue.setConverter(new JJImportanceConverter());
		htmlPanelGrid.getChildren().add(importanceValue);

		HtmlOutputText productLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		productLabel.setId("productLabel");
		productLabel.setValue("Product:");
		htmlPanelGrid.getChildren().add(productLabel);

		HtmlOutputText productValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		productValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJMessageBean.viewedMessage.product}",
						JJProduct.class));
		productValue.setConverter(new JJProductConverter());
		htmlPanelGrid.getChildren().add(productValue);

		HtmlOutputText projectLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		projectLabel.setId("projectLabel");
		projectLabel.setValue("Project:");
		htmlPanelGrid.getChildren().add(projectLabel);

		HtmlOutputText projectValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		projectValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJMessageBean.viewedMessage.project}",
						JJProject.class));
		projectValue.setConverter(new JJProjectConverter());
		htmlPanelGrid.getChildren().add(projectValue);

		HtmlOutputText requirementLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		requirementLabel.setId("requirementLabel");
		requirementLabel.setValue("Requirement:");
		htmlPanelGrid.getChildren().add(requirementLabel);

		HtmlOutputText requirementValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		requirementValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJMessageBean.viewedMessage.requirement}",
						JJRequirement.class));
		requirementValue.setConverter(new JJRequirementConverter());
		htmlPanelGrid.getChildren().add(requirementValue);

		HtmlOutputText sprintLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		sprintLabel.setId("sprintLabel");
		sprintLabel.setValue("Sprint:");
		htmlPanelGrid.getChildren().add(sprintLabel);

		HtmlOutputText sprintValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		sprintValue.setValueExpression("value",
				expressionFactory
						.createValueExpression(elContext,
								"#{jJMessageBean.viewedMessage.sprint}",
								JJSprint.class));
		sprintValue.setConverter(new JJSprintConverter());
		htmlPanelGrid.getChildren().add(sprintValue);

		HtmlOutputText statusLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		statusLabel.setId("statusLabel");
		statusLabel.setValue("Status:");
		htmlPanelGrid.getChildren().add(statusLabel);

		HtmlOutputText statusValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		statusValue.setValueExpression("value",
				expressionFactory
						.createValueExpression(elContext,
								"#{jJMessageBean.viewedMessage.status}",
								JJStatus.class));
		statusValue.setConverter(new JJStatusConverter());
		htmlPanelGrid.getChildren().add(statusValue);

		HtmlOutputText taskLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		taskLabel.setId("taskLabel");
		taskLabel.setValue("Task:");
		htmlPanelGrid.getChildren().add(taskLabel);

		HtmlOutputText taskValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		taskValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJMessageBean.viewedMessage.task}", JJTask.class));
		taskValue.setConverter(new JJTaskConverter());
		htmlPanelGrid.getChildren().add(taskValue);

		HtmlOutputText testcaseLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		testcaseLabel.setId("testcaseLabel");
		testcaseLabel.setValue("Testcase:");
		htmlPanelGrid.getChildren().add(testcaseLabel);

		HtmlOutputText testcaseValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		testcaseValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJMessageBean.viewedMessage.testcase}",
						JJTestcase.class));
		testcaseValue.setConverter(new JJTestcaseConverter());
		htmlPanelGrid.getChildren().add(testcaseValue);

		HtmlOutputText teststepLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		teststepLabel.setId("teststepLabel");
		teststepLabel.setValue("Teststep:");
		htmlPanelGrid.getChildren().add(teststepLabel);

		HtmlOutputText teststepValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		teststepValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJMessageBean.viewedMessage.teststep}",
						JJTestcase.class));
		teststepValue.setConverter(new JJTestcaseConverter());
		htmlPanelGrid.getChildren().add(teststepValue);

		HtmlOutputText versioningLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		versioningLabel.setId("versioningLabel");
		versioningLabel.setValue("Versioning:");
		htmlPanelGrid.getChildren().add(versioningLabel);

		HtmlOutputText versioningValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		versioningValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJMessageBean.viewedMessage.versioning}",
						JJVersion.class));
		versioningValue.setConverter(new JJVersionConverter());
		htmlPanelGrid.getChildren().add(versioningValue);

		return htmlPanelGrid;

	}
}

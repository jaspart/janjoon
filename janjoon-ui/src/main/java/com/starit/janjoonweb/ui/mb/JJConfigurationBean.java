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
import javax.faces.validator.LengthValidator;
import javax.servlet.http.HttpSession;

import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.message.Message;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.context.RequestContext;
import org.primefaces.extensions.component.ckeditor.CKEditor;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJCompany;
import com.starit.janjoonweb.domain.JJConfiguration;
import com.starit.janjoonweb.domain.JJConfigurationService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.ui.mb.lazyLoadingDataTable.LazyConfDataTable;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJConfiguration.class, beanName = "jJConfigurationBean")
public class JJConfigurationBean {

	private LazyConfDataTable configList;
	private JJConfiguration selectedConf;
	private List<String> columns;
	private JJConfiguration jJconfiguration;
	private boolean renderCreate;

	private HtmlPanelGrid createPanel;

	private HtmlPanelGrid editPanel;

	private HtmlPanelGrid viewPanel;

	@PostConstruct
	public void init() {

		columns = new ArrayList<String>();
		columns.add("name");
		columns.add("description");
		columns.add("creationDate");
		columns.add("updatedDate");
		columns.add("param");
		columns.add("val");
	}

	public JJConfigurationService getJjConfigurationService() {
		return jJConfigurationService;
	}

	public JJConfiguration getjJconfiguration() {
		return jJconfiguration;
	}

	public void setjJconfiguration(JJConfiguration jJconfiguration) {
		this.jJconfiguration = jJconfiguration;
	}

	public LazyConfDataTable getConfigList() {
		if (configList == null)
			configList = new LazyConfDataTable(jJConfigurationService);
		return configList;
	}

	public void setConfigList(LazyConfDataTable configList) {
		this.configList = configList;
	}

	public JJConfiguration getSelectedConf() {
		return selectedConf;
	}

	public void setSelectedConf(JJConfiguration selectedConf) {
		this.selectedConf = selectedConf;
	}

	public HtmlPanelGrid getCreatePanel() {
		if (createPanel == null)
			createPanel = populatePanelCreate();
		return createPanel;
	}

	public void setCreatePanel(HtmlPanelGrid createPanel) {
		this.createPanel = createPanel;
	}

	public HtmlPanelGrid getEditPanel() {
		if (editPanel == null)
			editPanel = populatePanelEdit();
		return editPanel;
	}

	public void setEditPanel(HtmlPanelGrid editPanel) {
		this.editPanel = editPanel;
	}

	public HtmlPanelGrid getViewPanel() {
		if (viewPanel == null)
			viewPanel = populatePanelView();

		return viewPanel;
	}

	public void setViewPanel(HtmlPanelGrid viewPanel) {
		this.viewPanel = viewPanel;
	}

	public List<String> getColumns() {
		return columns;
	}

	public boolean isRenderCreate() {
		return renderCreate;
	}

	public void setRenderCreate(boolean renderCreate) {
		System.out.println("jJConfigurationBean renderCreate" + renderCreate);
		this.renderCreate = renderCreate;
	}

	public void deleteConfig() {

		selectedConf.setEnabled(false);
		updateJJConfiguration(selectedConf);
		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_deleted", "Configuration");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		configList = null;

	}

	public String persistConf() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);

		if (getJJConfiguration_().getId() == null) {
			getJJConfiguration_().setEnabled(true);
		}

		return persist();
	}

	public String persist() {

		String message = "";
		if (getJJConfiguration_().getId() != null) {
			updateJJConfiguration(getJJConfiguration_());
			message = "message_successfully_updated";
		} else {
			saveJJConfiguration(getJJConfiguration_());
			message = "message_successfully_created";
		}
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('createDialogWidget').hide()");

		FacesMessage facesMessage = MessageFactory.getMessage(message,
				"Configuration");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		reset();
		return findAllJJConfigurations();
	}

	public void reset() {

		setJJConfiguration_(null);
		configList = null;
		setCreateDialogVisible(false);
		// jJconfiguration = jJConfigurationService.getConfigurations(
		// "ConfigurationManager", "git", true).get(0);
	}

	public void beforeDialogShow(ActionEvent event) {

		setJJConfiguration_(new JJConfiguration());
		renderCreate = true;
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('createDialogWidget').show()");

	}

	public void saveJJConfiguration(JJConfiguration b) {
		b.setCreationDate(new Date());
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setCreatedBy(contact);
		jJConfigurationService.saveJJConfiguration(b);
	}

	public void updateJJConfiguration(JJConfiguration b) {
		if (((LoginBean) LoginBean.findBean("loginBean"))
				.getPlanningConfiguration() != null
				&& b.equals(((LoginBean) LoginBean.findBean("loginBean"))
						.getPlanningConfiguration().getPlaningTabsConf()))
			((LoginBean) LoginBean.findBean("loginBean"))
					.setPlanningConfiguration(null);
		JJContact contact = ((LoginBean) LoginBean.findBean("loginBean"))
				.getContact();
		b.setUpdatedBy(contact);
		b.setUpdatedDate(new Date());
		jJConfigurationService.updateJJConfiguration(b);
	}

	public HtmlPanelGrid populatePanelCreate() {

		System.out.println("CreatepanelPop");
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
				.createValueExpression(elContext,
						"#{jJConfigurationBean.JJConfiguration_.name}",
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

		CKEditor descriptionCreateInput = (CKEditor) application
				.createComponent(CKEditor.COMPONENT_TYPE);
		String path = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestContextPath();
		descriptionCreateInput.setCustomConfig(path
				+ "/resources/js/ckEditor.js");
		descriptionCreateInput
				.setToolbar("[['Bold','Italic','Underline','Strike','NumberedList','BulletedList','Image','TextColor','BGColor','Undo','Table]]");
		descriptionCreateInput.setId("descriptionCreateInput");
		descriptionCreateInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJConfigurationBean.JJConfiguration_.description}",
						String.class));
		descriptionCreateInput.setRequired(true);
		htmlPanelGrid.getChildren().add(descriptionCreateInput);

		Message descriptionCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		descriptionCreateInputMessage.setId("descriptionCreateInputMessage");
		descriptionCreateInputMessage.setFor("descriptionCreateInput");
		descriptionCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(descriptionCreateInputMessage);

		OutputLabel paramCreateOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		paramCreateOutput.setFor("paramCreateInput");
		paramCreateOutput.setId("paramCreateOutput");
		paramCreateOutput.setValue("Param:");
		htmlPanelGrid.getChildren().add(paramCreateOutput);

		InputText paramCreateInput = (InputText) application
				.createComponent(InputText.COMPONENT_TYPE);
		paramCreateInput.setId("paramCreateInput");
		paramCreateInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJConfigurationBean.JJConfiguration_.param}",
						String.class));
		LengthValidator paramCreateInputValidator = new LengthValidator();
		paramCreateInputValidator.setMaximum(100);
		paramCreateInput.addValidator(paramCreateInputValidator);
		paramCreateInput.setRequired(true);
		htmlPanelGrid.getChildren().add(paramCreateInput);

		Message paramCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		paramCreateInputMessage.setId("paramCreateInputMessage");
		paramCreateInputMessage.setFor("paramCreateInput");
		paramCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(paramCreateInputMessage);

		OutputLabel valCreateOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		valCreateOutput.setFor("valCreateInput");
		valCreateOutput.setId("valCreateOutput");
		valCreateOutput.setValue("Val:");
		htmlPanelGrid.getChildren().add(valCreateOutput);

		InputTextarea valCreateInput = (InputTextarea) application
				.createComponent(InputTextarea.COMPONENT_TYPE);
		valCreateInput.setId("valCreateInput");
		valCreateInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJConfigurationBean.JJConfiguration_.val}",
						String.class));
		LengthValidator valCreateInputValidator = new LengthValidator();
		valCreateInputValidator.setMaximum(100);
		valCreateInput.addValidator(valCreateInputValidator);
		valCreateInput.setRequired(true);
		htmlPanelGrid.getChildren().add(valCreateInput);

		Message valCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		valCreateInputMessage.setId("valCreateInputMessage");
		valCreateInputMessage.setFor("valCreateInput");
		valCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(valCreateInputMessage);

		return htmlPanelGrid;

	}

	public HtmlPanelGrid populatePanelEdit() {

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
				.createValueExpression(elContext,
						"#{jJConfigurationBean.JJConfiguration_.name}",
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

		CKEditor descriptionEditInput = (CKEditor) application
				.createComponent(CKEditor.COMPONENT_TYPE);
		descriptionEditInput.setId("descriptionEditInput");
		String path = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestContextPath();
		descriptionEditInput
				.setCustomConfig(path + "/resources/js/ckEditor.js");
		descriptionEditInput
				.setToolbar("[['Bold','Italic','Underline','Strike','NumberedList','BulletedList','Image','TextColor','BGColor','Undo','Table]]");
		descriptionEditInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJConfigurationBean.JJConfiguration_.description}",
						String.class));
		descriptionEditInput.setRequired(true);
		htmlPanelGrid.getChildren().add(descriptionEditInput);

		Message descriptionEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		descriptionEditInputMessage.setId("descriptionEditInputMessage");
		descriptionEditInputMessage.setFor("descriptionEditInput");
		descriptionEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(descriptionEditInputMessage);

		OutputLabel paramEditOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		paramEditOutput.setFor("paramEditInput");
		paramEditOutput.setId("paramEditOutput");
		paramEditOutput.setValue("Param:");
		htmlPanelGrid.getChildren().add(paramEditOutput);

		InputText paramEditInput = (InputText) application
				.createComponent(InputText.COMPONENT_TYPE);
		paramEditInput.setId("paramEditInput");
		paramEditInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJConfigurationBean.JJConfiguration_.param}",
						String.class));
		LengthValidator paramEditInputValidator = new LengthValidator();
		paramEditInputValidator.setMaximum(100);
		paramEditInput.addValidator(paramEditInputValidator);
		paramEditInput.setRequired(true);
		htmlPanelGrid.getChildren().add(paramEditInput);

		Message paramEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		paramEditInputMessage.setId("paramEditInputMessage");
		paramEditInputMessage.setFor("paramEditInput");
		paramEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(paramEditInputMessage);

		OutputLabel valEditOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		valEditOutput.setFor("valEditInput");
		valEditOutput.setId("valEditOutput");
		valEditOutput.setValue("Val:");
		htmlPanelGrid.getChildren().add(valEditOutput);

		InputTextarea valEditInput = (InputTextarea) application
				.createComponent(InputTextarea.COMPONENT_TYPE);
		valEditInput.setId("valEditInput");
		valEditInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJConfigurationBean.JJConfiguration_.val}",
						String.class));
		LengthValidator valEditInputValidator = new LengthValidator();
		valEditInputValidator.setMaximum(100);
		valEditInput.addValidator(valEditInputValidator);
		valEditInput.setRequired(true);
		htmlPanelGrid.getChildren().add(valEditInput);

		Message valEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		valEditInputMessage.setId("valEditInputMessage");
		valEditInputMessage.setFor("valEditInput");
		valEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(valEditInputMessage);

		return htmlPanelGrid;

	}

	public HtmlPanelGrid populatePanelView() {

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
						"#{jJConfigurationBean.JJConfiguration_.name}",
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
						"#{jJConfigurationBean.JJConfiguration_.description}",
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
						"#{jJConfigurationBean.JJConfiguration_.creationDate}",
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
		createdByValue
				.setValueExpression(
						"value",
						expressionFactory
								.createValueExpression(
										elContext,
										"#{jJConfigurationBean.JJConfiguration_.createdBy.name}",
										String.class));
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
						"#{jJConfigurationBean.JJConfiguration_.updatedDate}",
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
		updatedByValue
				.setValueExpression(
						"value",
						expressionFactory
								.createValueExpression(
										elContext,
										"#{jJConfigurationBean.JJConfiguration_.updatedBy.name}",
										String.class));
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
						"#{jJConfigurationBean.JJConfiguration_.enabled}",
						String.class));
		htmlPanelGrid.getChildren().add(enabledValue);

		HtmlOutputText paramLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		paramLabel.setId("paramLabel");
		paramLabel.setValue("Param:");
		htmlPanelGrid.getChildren().add(paramLabel);

		HtmlOutputText paramValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		paramValue.setId("paramValue");
		paramValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJConfigurationBean.JJConfiguration_.param}",
						String.class));
		htmlPanelGrid.getChildren().add(paramValue);

		HtmlOutputText valLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		valLabel.setId("valLabel");
		valLabel.setValue("Val:");
		htmlPanelGrid.getChildren().add(valLabel);

		InputTextarea valValue = (InputTextarea) application
				.createComponent(InputTextarea.COMPONENT_TYPE);
		valValue.setId("valValue");
		valValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJConfigurationBean.JJConfiguration_.val}",
						String.class));
		valValue.setReadonly(true);
		valValue.setDisabled(true);
		htmlPanelGrid.getChildren().add(valValue);

		return htmlPanelGrid;

	}

}

package com.starit.janjoonweb.ui.mb;

import java.util.Date;
import java.util.List;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;
import javax.faces.validator.LengthValidator;
import javax.servlet.http.HttpSession;

import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.message.Message;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.spinner.Spinner;
import org.primefaces.context.RequestContext;
import org.primefaces.model.chart.PieChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.jsf.managedbean.RooJsfManagedBean;
import org.springframework.roo.addon.serializable.RooSerializable;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJRequirementService;
import com.starit.janjoonweb.domain.JJStatus;
import com.starit.janjoonweb.ui.mb.converter.JJContactConverter;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;

@RooSerializable
@RooJsfManagedBean(entity = JJStatus.class, beanName = "jJStatusBean")
public class JJStatusBean {

	private List<JJStatus> statusList;

	@Autowired
	private JJRequirementService jJRequirementService;

	private JJStatus selectedStatus;

	private PieChartModel pieChart;

	public List<JJStatus> getStatusList() {

		statusList = jJStatusService.getStatus(null, true, null, true);
		return statusList;
	}

	public void setjJRequirementService(
			JJRequirementService jJRequirementService) {
		this.jJRequirementService = jJRequirementService;
	}

	public void setStatusList(List<JJStatus> statusList) {
		this.statusList = statusList;
	}

	public JJStatus getSelectedStatus() {
		return selectedStatus;
	}

	public void setSelectedStatus(JJStatus selectedStatus) {
		this.selectedStatus = selectedStatus;
	}

	public PieChartModel getPieChart() {

		if (pieChart == null) {
			initPieChart();
		}
		return pieChart;
	}

	private void initPieChart() {

		pieChart = new PieChartModel();
		List<JJStatus> statReq = jJStatusService.getStatus("JJRequirement",
				true, null, false);
		for (JJStatus s : statReq) {

			int i = Integer.parseInt(""
					+ jJRequirementService.getReqCountByStaus(s, true));
			pieChart.set(s.getName(), i);
		}

	}

	public void setPieChart(PieChartModel pieChart) {
		this.pieChart = pieChart;
	}

	public void deleteStatus() {

		selectedStatus.setEnabled(false);
		jJStatusService.updateJJStatus(selectedStatus);
		FacesMessage facesMessage = MessageFactory.getMessage(
				"message_successfully_deleted", "JJStatus");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		statusList = jJStatusService.getStatus(null, true, null, true);
		pieChart = null;

	}

	public void reset() {
		setJJStatus_(null);
		pieChart = null;
		setSelectedMessages(null);
		setCreateDialogVisible(false);
	}

	public List<String> completeObject(String query) {

		return jJStatusService.getTablesName();
	}

	public String persistStatus() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		JJContact contact = (JJContact) session.getAttribute("JJContact");
		String message = "";

		if (getJJStatus_().getId() != null) {
			getJJStatus_().setUpdatedDate(new Date());
			getJJStatus_().setUpdatedBy(contact);
			jJStatusService.updateJJStatus(getJJStatus_());
			message = "message_successfully_updated";

		} else {
			getJJStatus_().setCreatedBy(contact);
			getJJStatus_().setDescription(
					"Satus : " + getJJStatus_().getName() + " for "
							+ getJJStatus_().getObjet() + "object");
			getJJStatus_().setCreationDate(new Date());
			getJJStatus_().setEnabled(true);
			jJStatusService.saveJJStatus(getJJStatus_());
			message = "message_successfully_created";
		}
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("createSatDialogWidget.hide()");
		context.execute("editSatDialogWidget.hide()");

		FacesMessage facesMessage = MessageFactory.getMessage(message,
				"JJStatus");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		reset();
		return findAllJJStatuses();

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
		nameCreateOutput.setFor("nameStatCreateInput");
		nameCreateOutput.setId("nameStatCreateOutput");
		nameCreateOutput.setValue("Name:");
		htmlPanelGrid.getChildren().add(nameCreateOutput);

		InputTextarea nameCreateInput = (InputTextarea) application
				.createComponent(InputTextarea.COMPONENT_TYPE);
		nameCreateInput.setId("nameStatCreateInput");
		nameCreateInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJStatusBean.JJStatus_.name}", String.class));
		LengthValidator nameCreateInputValidator = new LengthValidator();
		nameCreateInputValidator.setMaximum(100);
		nameCreateInput.addValidator(nameCreateInputValidator);
		nameCreateInput.setRequired(true);
		htmlPanelGrid.getChildren().add(nameCreateInput);

		Message nameCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		nameCreateInputMessage.setId("nameStatCreateInputMessage");
		nameCreateInputMessage.setFor("nameStatCreateInput");
		nameCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(nameCreateInputMessage);

		OutputLabel objetCreateOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		objetCreateOutput.setFor("objetStatCreateInput");
		objetCreateOutput.setId("objetStatCreateOutput");
		objetCreateOutput.setValue("Objet:");
		htmlPanelGrid.getChildren().add(objetCreateOutput);

		AutoComplete objetCreateInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		objetCreateInput.setId("objetStatCreateInput");
		objetCreateInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJStatusBean.JJStatus_.objet}", String.class));
		objetCreateInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{jJStatusBean.completeObject}", List.class,
						new Class[] { String.class }));
		objetCreateInput.setDropdown(true);
		objetCreateInput.setScrollHeight(200);
		objetCreateInput.setValueExpression("var", expressionFactory
				.createValueExpression(elContext, "objet", String.class));
		objetCreateInput.setValueExpression("itemLabel", expressionFactory
				.createValueExpression(elContext, "#{objet}", String.class));
		objetCreateInput.setValueExpression("itemValue", expressionFactory
				.createValueExpression(elContext, "#{objet}", String.class));
		objetCreateInput.setRequired(false);
		htmlPanelGrid.getChildren().add(objetCreateInput);

		Message objetCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		objetCreateInputMessage.setId("objetStatCreateInputMessage");
		objetCreateInputMessage.setFor("objetStatCreateInput");
		objetCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(objetCreateInputMessage);

		OutputLabel levelStatusCreateOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		levelStatusCreateOutput.setFor("levelStatusCreateInput");
		levelStatusCreateOutput.setId("levelStatusCreateOutput");
		levelStatusCreateOutput.setValue("Level Status:");
		htmlPanelGrid.getChildren().add(levelStatusCreateOutput);

		Spinner levelStatusCreateInput = (Spinner) application
				.createComponent(Spinner.COMPONENT_TYPE);
		levelStatusCreateInput.setId("levelStatusCreateInput");
		levelStatusCreateInput
				.setValueExpression("value", expressionFactory
						.createValueExpression(elContext,
								"#{jJStatusBean.JJStatus_.levelStatus}",
								Integer.class));
		levelStatusCreateInput.setRequired(false);

		htmlPanelGrid.getChildren().add(levelStatusCreateInput);

		Message levelStatusCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		levelStatusCreateInputMessage.setId("levelStatusCreateInputMessage");
		levelStatusCreateInputMessage.setFor("levelStatusCreateInput");
		levelStatusCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(levelStatusCreateInputMessage);

		HtmlOutputText messagesCreateOutput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		messagesCreateOutput.setId("messagesStatCreateOutput");
		messagesCreateOutput.setValue("Messages:");
		htmlPanelGrid.getChildren().add(messagesCreateOutput);

		HtmlOutputText messagesCreateInput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		messagesCreateInput.setId("messagesStatCreateInput");
		messagesCreateInput
				.setValue("This relationship is managed from the JJMessage side");
		htmlPanelGrid.getChildren().add(messagesCreateInput);

		Message messagesCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		messagesCreateInputMessage.setId("messagesStatCreateInputMessage");
		messagesCreateInputMessage.setFor("messagesStatCreateInput");
		messagesCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(messagesCreateInputMessage);

		return htmlPanelGrid;
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
		nameEditOutput.setFor("nameStatEditInput");
		nameEditOutput.setId("nameStatEditOutput");
		nameEditOutput.setValue("Name:");
		htmlPanelGrid.getChildren().add(nameEditOutput);

		InputTextarea nameEditInput = (InputTextarea) application
				.createComponent(InputTextarea.COMPONENT_TYPE);
		nameEditInput.setId("nameStatEditInput");
		nameEditInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJStatusBean.JJStatus_.name}", String.class));
		LengthValidator nameEditInputValidator = new LengthValidator();
		nameEditInputValidator.setMaximum(100);
		nameEditInput.addValidator(nameEditInputValidator);
		nameEditInput.setRequired(true);
		htmlPanelGrid.getChildren().add(nameEditInput);

		Message nameEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		nameEditInputMessage.setId("nameStatEditInputMessage");
		nameEditInputMessage.setFor("nameStatEditInput");
		nameEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(nameEditInputMessage);

		OutputLabel objetEditOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		objetEditOutput.setFor("objetStatEditInput");
		objetEditOutput.setId("objetStatEditOutput");
		objetEditOutput.setValue("Objet:");
		htmlPanelGrid.getChildren().add(objetEditOutput);

		AutoComplete objetEditInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		objetEditInput.setId("objetStatEditInput");
		// objetEditInput.
		objetEditInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJStatusBean.JJStatus_.objet}", String.class));
		objetEditInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{jJStatusBean.completeObject}", List.class,
						new Class[] { String.class }));
		objetEditInput.setDropdown(true);
		objetEditInput.setScrollHeight(200);
		objetEditInput.setValueExpression("var", expressionFactory
				.createValueExpression(elContext, "objet", String.class));
		objetEditInput.setValueExpression("itemLabel", expressionFactory
				.createValueExpression(elContext, "#{objet}", String.class));
		objetEditInput.setValueExpression("itemValue", expressionFactory
				.createValueExpression(elContext, "#{objet}", String.class));
		objetEditInput.setRequired(true);
		htmlPanelGrid.getChildren().add(objetEditInput);

		Message objetEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		objetEditInputMessage.setId("objetStatEditInputMessage");
		objetEditInputMessage.setFor("objetStatEditInput");
		objetEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(objetEditInputMessage);

		OutputLabel levelStatusEditOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		levelStatusEditOutput.setFor("levelStatusEditInput");
		levelStatusEditOutput.setId("levelStatusEditOutput");
		levelStatusEditOutput.setValue("Level Status:");
		htmlPanelGrid.getChildren().add(levelStatusEditOutput);

		Spinner levelStatusEditInput = (Spinner) application
				.createComponent(Spinner.COMPONENT_TYPE);
		levelStatusEditInput.setId("levelStatusEditInput");
		levelStatusEditInput
				.setValueExpression("value", expressionFactory
						.createValueExpression(elContext,
								"#{jJStatusBean.JJStatus_.levelStatus}",
								Integer.class));
		levelStatusEditInput.setRequired(false);

		htmlPanelGrid.getChildren().add(levelStatusEditInput);

		Message levelStatusEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		levelStatusEditInputMessage.setId("levelStatusEditInputMessage");
		levelStatusEditInputMessage.setFor("levelStatusEditInput");
		levelStatusEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(levelStatusEditInputMessage);

		HtmlOutputText messagesEditOutput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		messagesEditOutput.setId("messagesStatEditOutput");
		messagesEditOutput.setValue("Messages:");
		htmlPanelGrid.getChildren().add(messagesEditOutput);

		HtmlOutputText messagesEditInput = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		messagesEditInput.setId("messagesStatEditInput");
		messagesEditInput
				.setValue("This relationship is managed from the JJMessage side");
		htmlPanelGrid.getChildren().add(messagesEditInput);

		Message messagesEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		messagesEditInputMessage.setId("messagesStatEditInputMessage");
		messagesEditInputMessage.setFor("messagesStatEditInput");
		messagesEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(messagesEditInputMessage);

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
				.createValueExpression(elContext,
						"#{jJStatusBean.JJStatus_.name}", String.class));
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
						"#{jJStatusBean.JJStatus_.description}", String.class));
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
						"#{jJStatusBean.JJStatus_.creationDate}", Date.class));
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
				.setValueExpression("value", expressionFactory
						.createValueExpression(elContext,
								"#{jJStatusBean.JJStatus_.createdBy}",
								JJContact.class));
		createdByValue.setConverter(new JJContactConverter());
		htmlPanelGrid.getChildren().add(createdByValue);

		HtmlOutputText updatedDateLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		updatedDateLabel.setId("updatedStatDateLabel");
		updatedDateLabel.setValue("Updated Date:");
		htmlPanelGrid.getChildren().add(updatedDateLabel);

		HtmlOutputText updatedDateValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		updatedDateValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJStatusBean.JJStatus_.updatedDate}", Date.class));
		DateTimeConverter updatedDateValueConverter = (DateTimeConverter) application
				.createConverter(DateTimeConverter.CONVERTER_ID);
		updatedDateValueConverter.setPattern("dd/MM/yyyy");
		updatedDateValue.setConverter(updatedDateValueConverter);
		htmlPanelGrid.getChildren().add(updatedDateValue);

		HtmlOutputText updatedByLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		updatedByLabel.setId("updatedStatByLabel");
		updatedByLabel.setValue("Updated By:");
		htmlPanelGrid.getChildren().add(updatedByLabel);

		HtmlOutputText updatedByValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		updatedByValue
				.setValueExpression("value", expressionFactory
						.createValueExpression(elContext,
								"#{jJStatusBean.JJStatus_.updatedBy}",
								JJContact.class));
		updatedByValue.setConverter(new JJContactConverter());
		htmlPanelGrid.getChildren().add(updatedByValue);

		HtmlOutputText enabledLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		enabledLabel.setId("enabledStatLabel");
		enabledLabel.setValue("Enabled:");
		htmlPanelGrid.getChildren().add(enabledLabel);

		HtmlOutputText enabledValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		enabledValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJStatusBean.JJStatus_.enabled}", String.class));
		htmlPanelGrid.getChildren().add(enabledValue);

		HtmlOutputText objetLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		objetLabel.setId("objetLabel");
		objetLabel.setValue("Objet:");
		htmlPanelGrid.getChildren().add(objetLabel);

		HtmlOutputText objetValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		objetValue.setId("objetValue");
		objetValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJStatusBean.JJStatus_.objet}", String.class));
		htmlPanelGrid.getChildren().add(objetValue);

		HtmlOutputText levelStatusLabel = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		levelStatusLabel.setId("levelStatusLabel");
		levelStatusLabel.setValue("Level Status:");
		htmlPanelGrid.getChildren().add(levelStatusLabel);

		HtmlOutputText levelStatusValue = (HtmlOutputText) application
				.createComponent(HtmlOutputText.COMPONENT_TYPE);
		levelStatusValue.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJStatusBean.JJStatus_.levelStatus}", String.class));
		htmlPanelGrid.getChildren().add(levelStatusValue);

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

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
import javax.faces.event.ComponentSystemEvent;
import javax.faces.validator.LengthValidator;

import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.message.Message;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.selectbooleancheckbox.SelectBooleanCheckbox;
import org.primefaces.component.spinner.Spinner;
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
			System.out.println("-------------------------init pieChart");
			List<JJStatus> statReq = jJStatusService.getStatus("JJRequirement",
					true, null, false);
			for (JJStatus s : statReq) {

				System.out.println(s.getName() + "  " + s.getObjet());
				int i = Integer.parseInt(""
						+ jJRequirementService.getReqCountByStaus(s, true));
				System.out.println(i);
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
				.createValueExpression(elContext,
						"#{jJStatusBean.JJStatus_.name}", String.class));
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
						"#{jJStatusBean.JJStatus_.description}", String.class));
		descriptionCreateInput.setRequired(true);
		htmlPanelGrid.getChildren().add(descriptionCreateInput);

		Message descriptionCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		descriptionCreateInputMessage.setId("descriptionCreateInputMessage");
		descriptionCreateInputMessage.setFor("descriptionCreateInput");
		descriptionCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(descriptionCreateInputMessage);

		OutputLabel creationDateCreateOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		creationDateCreateOutput.setFor("creationDateCreateInput");
		creationDateCreateOutput.setId("creationDateCreateOutput");
		creationDateCreateOutput.setValue("Creation Date:");
		htmlPanelGrid.getChildren().add(creationDateCreateOutput);

		Calendar creationDateCreateInput = (Calendar) application
				.createComponent(Calendar.COMPONENT_TYPE);
		creationDateCreateInput.setId("creationDateCreateInput");
		creationDateCreateInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJStatusBean.JJStatus_.creationDate}", Date.class));
		creationDateCreateInput.setNavigator(true);
		creationDateCreateInput.setEffect("slideDown");
		creationDateCreateInput.setPattern("dd/MM/yyyy");
		creationDateCreateInput.setRequired(true);
		htmlPanelGrid.getChildren().add(creationDateCreateInput);

		Message creationDateCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		creationDateCreateInputMessage.setId("creationDateCreateInputMessage");
		creationDateCreateInputMessage.setFor("creationDateCreateInput");
		creationDateCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(creationDateCreateInputMessage);

		OutputLabel createdByCreateOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		createdByCreateOutput.setFor("createdByCreateInput");
		createdByCreateOutput.setId("createdByCreateOutput");
		createdByCreateOutput.setValue("Created By:");
		htmlPanelGrid.getChildren().add(createdByCreateOutput);

		AutoComplete createdByCreateInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		createdByCreateInput.setId("createdByCreateInput");
		createdByCreateInput
				.setValueExpression("value", expressionFactory
						.createValueExpression(elContext,
								"#{jJStatusBean.JJStatus_.createdBy}",
								JJContact.class));
		createdByCreateInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{jJStatusBean.completeCreatedBy}", List.class,
						new Class[] { String.class }));
		createdByCreateInput.setDropdown(true);
		createdByCreateInput.setValueExpression("var", expressionFactory
				.createValueExpression(elContext, "createdBy", String.class));
		createdByCreateInput
				.setValueExpression(
						"itemLabel",
						expressionFactory
								.createValueExpression(
										elContext,
										"#{createdBy.name} #{createdBy.description} #{createdBy.creationDate} #{createdBy.updatedDate}",
										String.class));
		createdByCreateInput.setValueExpression("itemValue", expressionFactory
				.createValueExpression(elContext, "#{createdBy}",
						JJContact.class));
		createdByCreateInput.setConverter(new JJContactConverter());
		createdByCreateInput.setRequired(false);
		htmlPanelGrid.getChildren().add(createdByCreateInput);

		Message createdByCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		createdByCreateInputMessage.setId("createdByCreateInputMessage");
		createdByCreateInputMessage.setFor("createdByCreateInput");
		createdByCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(createdByCreateInputMessage);

		OutputLabel updatedDateCreateOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		updatedDateCreateOutput.setFor("updatedDateCreateInput");
		updatedDateCreateOutput.setId("updatedDateCreateOutput");
		updatedDateCreateOutput.setValue("Updated Date:");
		htmlPanelGrid.getChildren().add(updatedDateCreateOutput);

		Calendar updatedDateCreateInput = (Calendar) application
				.createComponent(Calendar.COMPONENT_TYPE);
		updatedDateCreateInput.setId("updatedDateCreateInput");
		updatedDateCreateInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJStatusBean.JJStatus_.updatedDate}", Date.class));
		updatedDateCreateInput.setNavigator(true);
		updatedDateCreateInput.setEffect("slideDown");
		updatedDateCreateInput.setPattern("dd/MM/yyyy");
		updatedDateCreateInput.setRequired(false);
		htmlPanelGrid.getChildren().add(updatedDateCreateInput);

		Message updatedDateCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		updatedDateCreateInputMessage.setId("updatedDateCreateInputMessage");
		updatedDateCreateInputMessage.setFor("updatedDateCreateInput");
		updatedDateCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(updatedDateCreateInputMessage);

		OutputLabel updatedByCreateOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		updatedByCreateOutput.setFor("updatedByCreateInput");
		updatedByCreateOutput.setId("updatedByCreateOutput");
		updatedByCreateOutput.setValue("Updated By:");
		htmlPanelGrid.getChildren().add(updatedByCreateOutput);

		AutoComplete updatedByCreateInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		updatedByCreateInput.setId("updatedByCreateInput");
		updatedByCreateInput
				.setValueExpression("value", expressionFactory
						.createValueExpression(elContext,
								"#{jJStatusBean.JJStatus_.updatedBy}",
								JJContact.class));
		updatedByCreateInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{jJStatusBean.completeUpdatedBy}", List.class,
						new Class[] { String.class }));
		updatedByCreateInput.setDropdown(true);
		updatedByCreateInput.setValueExpression("var", expressionFactory
				.createValueExpression(elContext, "updatedBy", String.class));
		updatedByCreateInput
				.setValueExpression(
						"itemLabel",
						expressionFactory
								.createValueExpression(
										elContext,
										"#{updatedBy.name} #{updatedBy.description} #{updatedBy.creationDate} #{updatedBy.updatedDate}",
										String.class));
		updatedByCreateInput.setValueExpression("itemValue", expressionFactory
				.createValueExpression(elContext, "#{updatedBy}",
						JJContact.class));
		updatedByCreateInput.setConverter(new JJContactConverter());
		updatedByCreateInput.setRequired(false);
		htmlPanelGrid.getChildren().add(updatedByCreateInput);

		Message updatedByCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		updatedByCreateInputMessage.setId("updatedByCreateInputMessage");
		updatedByCreateInputMessage.setFor("updatedByCreateInput");
		updatedByCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(updatedByCreateInputMessage);

		OutputLabel enabledCreateOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		enabledCreateOutput.setFor("enabledCreateInput");
		enabledCreateOutput.setId("enabledCreateOutput");
		enabledCreateOutput.setValue("Enabled:");
		htmlPanelGrid.getChildren().add(enabledCreateOutput);

		SelectBooleanCheckbox enabledCreateInput = (SelectBooleanCheckbox) application
				.createComponent(SelectBooleanCheckbox.COMPONENT_TYPE);
		enabledCreateInput.setId("enabledCreateInput");
		enabledCreateInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJStatusBean.JJStatus_.enabled}", Boolean.class));
		enabledCreateInput.setRequired(false);
		htmlPanelGrid.getChildren().add(enabledCreateInput);

		Message enabledCreateInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		enabledCreateInputMessage.setId("enabledCreateInputMessage");
		enabledCreateInputMessage.setFor("enabledCreateInput");
		enabledCreateInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(enabledCreateInputMessage);

		OutputLabel objetCreateOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		objetCreateOutput.setFor("objetCreateInput");
		objetCreateOutput.setId("objetCreateOutput");
		objetCreateOutput.setValue("Objet:");
		htmlPanelGrid.getChildren().add(objetCreateOutput);

		AutoComplete objetCreateInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		objetCreateInput.setId("objetCreateInput");
		objetCreateInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJStatusBean.JJStatus_.objet}", String.class));
		objetCreateInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{jJStatusBean.completeObject}", List.class,
						new Class[] { String.class }));
		objetCreateInput.setDropdown(true);
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
		objetCreateInputMessage.setId("objetCreateInputMessage");
		objetCreateInputMessage.setFor("objetCreateInput");
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
				.createValueExpression(elContext,
						"#{jJStatusBean.JJStatus_.name}", String.class));
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
						"#{jJStatusBean.JJStatus_.description}", String.class));
		descriptionEditInput.setRequired(true);
		htmlPanelGrid.getChildren().add(descriptionEditInput);

		Message descriptionEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		descriptionEditInputMessage.setId("descriptionEditInputMessage");
		descriptionEditInputMessage.setFor("descriptionEditInput");
		descriptionEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(descriptionEditInputMessage);

		OutputLabel creationDateEditOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		creationDateEditOutput.setFor("creationDateEditInput");
		creationDateEditOutput.setId("creationDateEditOutput");
		creationDateEditOutput.setValue("Creation Date:");
		htmlPanelGrid.getChildren().add(creationDateEditOutput);

		Calendar creationDateEditInput = (Calendar) application
				.createComponent(Calendar.COMPONENT_TYPE);
		creationDateEditInput.setId("creationDateEditInput");
		creationDateEditInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJStatusBean.JJStatus_.creationDate}", Date.class));
		creationDateEditInput.setNavigator(true);
		creationDateEditInput.setEffect("slideDown");
		creationDateEditInput.setPattern("dd/MM/yyyy");
		creationDateEditInput.setRequired(true);
		htmlPanelGrid.getChildren().add(creationDateEditInput);

		Message creationDateEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		creationDateEditInputMessage.setId("creationDateEditInputMessage");
		creationDateEditInputMessage.setFor("creationDateEditInput");
		creationDateEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(creationDateEditInputMessage);

		OutputLabel createdByEditOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		createdByEditOutput.setFor("createdByEditInput");
		createdByEditOutput.setId("createdByEditOutput");
		createdByEditOutput.setValue("Created By:");
		htmlPanelGrid.getChildren().add(createdByEditOutput);

		AutoComplete createdByEditInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		createdByEditInput.setId("createdByEditInput");
		createdByEditInput
				.setValueExpression("value", expressionFactory
						.createValueExpression(elContext,
								"#{jJStatusBean.JJStatus_.createdBy}",
								JJContact.class));
		createdByEditInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{jJStatusBean.completeCreatedBy}", List.class,
						new Class[] { String.class }));
		createdByEditInput.setDropdown(true);
		createdByEditInput.setValueExpression("var", expressionFactory
				.createValueExpression(elContext, "createdBy", String.class));
		createdByEditInput
				.setValueExpression(
						"itemLabel",
						expressionFactory
								.createValueExpression(
										elContext,
										"#{createdBy.name} #{createdBy.description} #{createdBy.creationDate} #{createdBy.updatedDate}",
										String.class));
		createdByEditInput.setValueExpression("itemValue", expressionFactory
				.createValueExpression(elContext, "#{createdBy}",
						JJContact.class));
		createdByEditInput.setConverter(new JJContactConverter());
		createdByEditInput.setRequired(false);
		htmlPanelGrid.getChildren().add(createdByEditInput);

		Message createdByEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		createdByEditInputMessage.setId("createdByEditInputMessage");
		createdByEditInputMessage.setFor("createdByEditInput");
		createdByEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(createdByEditInputMessage);

		OutputLabel updatedDateEditOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		updatedDateEditOutput.setFor("updatedDateEditInput");
		updatedDateEditOutput.setId("updatedDateEditOutput");
		updatedDateEditOutput.setValue("Updated Date:");
		htmlPanelGrid.getChildren().add(updatedDateEditOutput);

		Calendar updatedDateEditInput = (Calendar) application
				.createComponent(Calendar.COMPONENT_TYPE);
		updatedDateEditInput.setId("updatedDateEditInput");
		updatedDateEditInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJStatusBean.JJStatus_.updatedDate}", Date.class));
		updatedDateEditInput.setNavigator(true);
		updatedDateEditInput.setEffect("slideDown");
		updatedDateEditInput.setPattern("dd/MM/yyyy");
		updatedDateEditInput.setRequired(false);
		htmlPanelGrid.getChildren().add(updatedDateEditInput);

		Message updatedDateEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		updatedDateEditInputMessage.setId("updatedDateEditInputMessage");
		updatedDateEditInputMessage.setFor("updatedDateEditInput");
		updatedDateEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(updatedDateEditInputMessage);

		OutputLabel updatedByEditOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		updatedByEditOutput.setFor("updatedByEditInput");
		updatedByEditOutput.setId("updatedByEditOutput");
		updatedByEditOutput.setValue("Updated By:");
		htmlPanelGrid.getChildren().add(updatedByEditOutput);

		AutoComplete updatedByEditInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		updatedByEditInput.setId("updatedByEditInput");
		updatedByEditInput
				.setValueExpression("value", expressionFactory
						.createValueExpression(elContext,
								"#{jJStatusBean.JJStatus_.updatedBy}",
								JJContact.class));
		updatedByEditInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{jJStatusBean.completeUpdatedBy}", List.class,
						new Class[] { String.class }));
		updatedByEditInput.setDropdown(true);
		updatedByEditInput.setValueExpression("var", expressionFactory
				.createValueExpression(elContext, "updatedBy", String.class));
		updatedByEditInput
				.setValueExpression(
						"itemLabel",
						expressionFactory
								.createValueExpression(
										elContext,
										"#{updatedBy.name} #{updatedBy.description} #{updatedBy.creationDate} #{updatedBy.updatedDate}",
										String.class));
		updatedByEditInput.setValueExpression("itemValue", expressionFactory
				.createValueExpression(elContext, "#{updatedBy}",
						JJContact.class));
		updatedByEditInput.setConverter(new JJContactConverter());
		updatedByEditInput.setRequired(false);
		htmlPanelGrid.getChildren().add(updatedByEditInput);

		Message updatedByEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		updatedByEditInputMessage.setId("updatedByEditInputMessage");
		updatedByEditInputMessage.setFor("updatedByEditInput");
		updatedByEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(updatedByEditInputMessage);

		OutputLabel enabledEditOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		enabledEditOutput.setFor("enabledEditInput");
		enabledEditOutput.setId("enabledEditOutput");
		enabledEditOutput.setValue("Enabled:");
		htmlPanelGrid.getChildren().add(enabledEditOutput);

		SelectBooleanCheckbox enabledEditInput = (SelectBooleanCheckbox) application
				.createComponent(SelectBooleanCheckbox.COMPONENT_TYPE);
		enabledEditInput.setId("enabledEditInput");
		enabledEditInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJStatusBean.JJStatus_.enabled}", Boolean.class));
		enabledEditInput.setRequired(false);
		htmlPanelGrid.getChildren().add(enabledEditInput);

		Message enabledEditInputMessage = (Message) application
				.createComponent(Message.COMPONENT_TYPE);
		enabledEditInputMessage.setId("enabledEditInputMessage");
		enabledEditInputMessage.setFor("enabledEditInput");
		enabledEditInputMessage.setDisplay("icon");
		htmlPanelGrid.getChildren().add(enabledEditInputMessage);

		OutputLabel objetEditOutput = (OutputLabel) application
				.createComponent(OutputLabel.COMPONENT_TYPE);
		objetEditOutput.setFor("objetEditInput");
		objetEditOutput.setId("objetEditOutput");
		objetEditOutput.setValue("Objet:");
		htmlPanelGrid.getChildren().add(objetEditOutput);

		AutoComplete objetEditInput = (AutoComplete) application
				.createComponent(AutoComplete.COMPONENT_TYPE);
		objetEditInput.setId("objetEditInput");
		objetEditInput.setValueExpression("value", expressionFactory
				.createValueExpression(elContext,
						"#{jJStatusBean.JJStatus_.objet}", String.class));
		objetEditInput.setCompleteMethod(expressionFactory
				.createMethodExpression(elContext,
						"#{jJStatusBean.completeObject}", List.class,
						new Class[] { String.class }));
		objetEditInput.setDropdown(true);
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
		objetEditInputMessage.setId("objetEditInputMessage");
		objetEditInputMessage.setFor("objetEditInput");
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
		updatedDateLabel.setId("updatedDateLabel");
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
		updatedByLabel.setId("updatedByLabel");
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
		enabledLabel.setId("enabledLabel");
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

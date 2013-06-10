// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.ui.mb;

import com.funder.janjoonweb.domain.JJContact;
import com.funder.janjoonweb.domain.JJContactService;
import com.funder.janjoonweb.domain.JJStatus;
import com.funder.janjoonweb.domain.JJStatusService;
import com.funder.janjoonweb.ui.mb.JJStatusBean;
import com.funder.janjoonweb.ui.mb.converter.JJContactConverter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;
import javax.faces.validator.LengthValidator;
import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.message.Message;
import org.primefaces.component.selectbooleancheckbox.SelectBooleanCheckbox;
import org.primefaces.component.spinner.Spinner;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;
import org.springframework.beans.factory.annotation.Autowired;

privileged aspect JJStatusBean_Roo_ManagedBean {
    
    declare @type: JJStatusBean: @ManagedBean(name = "jJStatusBean");
    
    declare @type: JJStatusBean: @SessionScoped;
    
    @Autowired
    JJStatusService JJStatusBean.jJStatusService;
    
    @Autowired
    JJContactService JJStatusBean.jJContactService;
    
    private String JJStatusBean.name = "JJStatuses";
    
    private JJStatus JJStatusBean.JJStatus_;
    
    private List<JJStatus> JJStatusBean.allJJStatuses;
    
    private boolean JJStatusBean.dataVisible = false;
    
    private List<String> JJStatusBean.columns;
    
    private HtmlPanelGrid JJStatusBean.createPanelGrid;
    
    private HtmlPanelGrid JJStatusBean.editPanelGrid;
    
    private HtmlPanelGrid JJStatusBean.viewPanelGrid;
    
    private boolean JJStatusBean.createDialogVisible = false;
    
    @PostConstruct
    public void JJStatusBean.init() {
        columns = new ArrayList<String>();
        columns.add("name");
        columns.add("description");
        columns.add("creationDate");
        columns.add("updatedDate");
        columns.add("statusLevel");
    }
    
    public String JJStatusBean.getName() {
        return name;
    }
    
    public List<String> JJStatusBean.getColumns() {
        return columns;
    }
    
    public List<JJStatus> JJStatusBean.getAllJJStatuses() {
        return allJJStatuses;
    }
    
    public void JJStatusBean.setAllJJStatuses(List<JJStatus> allJJStatuses) {
        this.allJJStatuses = allJJStatuses;
    }
    
    public String JJStatusBean.findAllJJStatuses() {
        allJJStatuses = jJStatusService.findAllJJStatuses();
        dataVisible = !allJJStatuses.isEmpty();
        return null;
    }
    
    public boolean JJStatusBean.isDataVisible() {
        return dataVisible;
    }
    
    public void JJStatusBean.setDataVisible(boolean dataVisible) {
        this.dataVisible = dataVisible;
    }
    
    public HtmlPanelGrid JJStatusBean.getCreatePanelGrid() {
        if (createPanelGrid == null) {
            createPanelGrid = populateCreatePanel();
        }
        return createPanelGrid;
    }
    
    public void JJStatusBean.setCreatePanelGrid(HtmlPanelGrid createPanelGrid) {
        this.createPanelGrid = createPanelGrid;
    }
    
    public HtmlPanelGrid JJStatusBean.getEditPanelGrid() {
        if (editPanelGrid == null) {
            editPanelGrid = populateEditPanel();
        }
        return editPanelGrid;
    }
    
    public void JJStatusBean.setEditPanelGrid(HtmlPanelGrid editPanelGrid) {
        this.editPanelGrid = editPanelGrid;
    }
    
    public HtmlPanelGrid JJStatusBean.getViewPanelGrid() {
        return populateViewPanel();
    }
    
    public void JJStatusBean.setViewPanelGrid(HtmlPanelGrid viewPanelGrid) {
        this.viewPanelGrid = viewPanelGrid;
    }
    
    public HtmlPanelGrid JJStatusBean.populateCreatePanel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        
        HtmlOutputText nameCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        nameCreateOutput.setId("nameCreateOutput");
        nameCreateOutput.setValue("Name: * ");
        htmlPanelGrid.getChildren().add(nameCreateOutput);
        
        InputText nameCreateInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        nameCreateInput.setId("nameCreateInput");
        nameCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJStatusBean.JJStatus_.name}", String.class));
        LengthValidator nameCreateInputValidator = new LengthValidator();
        nameCreateInputValidator.setMaximum(25);
        nameCreateInput.addValidator(nameCreateInputValidator);
        nameCreateInput.setRequired(true);
        htmlPanelGrid.getChildren().add(nameCreateInput);
        
        Message nameCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        nameCreateInputMessage.setId("nameCreateInputMessage");
        nameCreateInputMessage.setFor("nameCreateInput");
        nameCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(nameCreateInputMessage);
        
        HtmlOutputText descriptionCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        descriptionCreateOutput.setId("descriptionCreateOutput");
        descriptionCreateOutput.setValue("Description: * ");
        htmlPanelGrid.getChildren().add(descriptionCreateOutput);
        
        InputTextarea descriptionCreateInput = (InputTextarea) application.createComponent(InputTextarea.COMPONENT_TYPE);
        descriptionCreateInput.setId("descriptionCreateInput");
        descriptionCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJStatusBean.JJStatus_.description}", String.class));
        LengthValidator descriptionCreateInputValidator = new LengthValidator();
        descriptionCreateInputValidator.setMaximum(250);
        descriptionCreateInput.addValidator(descriptionCreateInputValidator);
        descriptionCreateInput.setRequired(true);
        htmlPanelGrid.getChildren().add(descriptionCreateInput);
        
        Message descriptionCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        descriptionCreateInputMessage.setId("descriptionCreateInputMessage");
        descriptionCreateInputMessage.setFor("descriptionCreateInput");
        descriptionCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(descriptionCreateInputMessage);
        
        HtmlOutputText creationDateCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        creationDateCreateOutput.setId("creationDateCreateOutput");
        creationDateCreateOutput.setValue("Creation Date: * ");
        htmlPanelGrid.getChildren().add(creationDateCreateOutput);
        
        Calendar creationDateCreateInput = (Calendar) application.createComponent(Calendar.COMPONENT_TYPE);
        creationDateCreateInput.setId("creationDateCreateInput");
        creationDateCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJStatusBean.JJStatus_.creationDate}", Date.class));
        creationDateCreateInput.setNavigator(true);
        creationDateCreateInput.setEffect("slideDown");
        creationDateCreateInput.setPattern("dd/MM/yyyy");
        creationDateCreateInput.setRequired(true);
        htmlPanelGrid.getChildren().add(creationDateCreateInput);
        
        Message creationDateCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        creationDateCreateInputMessage.setId("creationDateCreateInputMessage");
        creationDateCreateInputMessage.setFor("creationDateCreateInput");
        creationDateCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(creationDateCreateInputMessage);
        
        HtmlOutputText createdByCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        createdByCreateOutput.setId("createdByCreateOutput");
        createdByCreateOutput.setValue("Created By:   ");
        htmlPanelGrid.getChildren().add(createdByCreateOutput);
        
        AutoComplete createdByCreateInput = (AutoComplete) application.createComponent(AutoComplete.COMPONENT_TYPE);
        createdByCreateInput.setId("createdByCreateInput");
        createdByCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJStatusBean.JJStatus_.createdBy}", JJContact.class));
        createdByCreateInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{jJStatusBean.completeCreatedBy}", List.class, new Class[] { String.class }));
        createdByCreateInput.setDropdown(true);
        createdByCreateInput.setValueExpression("var", expressionFactory.createValueExpression(elContext, "createdBy", String.class));
        createdByCreateInput.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext, "#{createdBy.name} #{createdBy.description} #{createdBy.creationDate} #{createdBy.updatedDate}", String.class));
        createdByCreateInput.setValueExpression("itemValue", expressionFactory.createValueExpression(elContext, "#{createdBy}", JJContact.class));
        createdByCreateInput.setConverter(new JJContactConverter());
        createdByCreateInput.setRequired(false);
        htmlPanelGrid.getChildren().add(createdByCreateInput);
        
        Message createdByCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        createdByCreateInputMessage.setId("createdByCreateInputMessage");
        createdByCreateInputMessage.setFor("createdByCreateInput");
        createdByCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(createdByCreateInputMessage);
        
        HtmlOutputText updatedDateCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        updatedDateCreateOutput.setId("updatedDateCreateOutput");
        updatedDateCreateOutput.setValue("Updated Date:   ");
        htmlPanelGrid.getChildren().add(updatedDateCreateOutput);
        
        Calendar updatedDateCreateInput = (Calendar) application.createComponent(Calendar.COMPONENT_TYPE);
        updatedDateCreateInput.setId("updatedDateCreateInput");
        updatedDateCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJStatusBean.JJStatus_.updatedDate}", Date.class));
        updatedDateCreateInput.setNavigator(true);
        updatedDateCreateInput.setEffect("slideDown");
        updatedDateCreateInput.setPattern("dd/MM/yyyy");
        updatedDateCreateInput.setRequired(false);
        htmlPanelGrid.getChildren().add(updatedDateCreateInput);
        
        Message updatedDateCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        updatedDateCreateInputMessage.setId("updatedDateCreateInputMessage");
        updatedDateCreateInputMessage.setFor("updatedDateCreateInput");
        updatedDateCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(updatedDateCreateInputMessage);
        
        HtmlOutputText updatedByCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        updatedByCreateOutput.setId("updatedByCreateOutput");
        updatedByCreateOutput.setValue("Updated By:   ");
        htmlPanelGrid.getChildren().add(updatedByCreateOutput);
        
        AutoComplete updatedByCreateInput = (AutoComplete) application.createComponent(AutoComplete.COMPONENT_TYPE);
        updatedByCreateInput.setId("updatedByCreateInput");
        updatedByCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJStatusBean.JJStatus_.updatedBy}", JJContact.class));
        updatedByCreateInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{jJStatusBean.completeUpdatedBy}", List.class, new Class[] { String.class }));
        updatedByCreateInput.setDropdown(true);
        updatedByCreateInput.setValueExpression("var", expressionFactory.createValueExpression(elContext, "updatedBy", String.class));
        updatedByCreateInput.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext, "#{updatedBy.name} #{updatedBy.description} #{updatedBy.creationDate} #{updatedBy.updatedDate}", String.class));
        updatedByCreateInput.setValueExpression("itemValue", expressionFactory.createValueExpression(elContext, "#{updatedBy}", JJContact.class));
        updatedByCreateInput.setConverter(new JJContactConverter());
        updatedByCreateInput.setRequired(false);
        htmlPanelGrid.getChildren().add(updatedByCreateInput);
        
        Message updatedByCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        updatedByCreateInputMessage.setId("updatedByCreateInputMessage");
        updatedByCreateInputMessage.setFor("updatedByCreateInput");
        updatedByCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(updatedByCreateInputMessage);
        
        HtmlOutputText enabledCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        enabledCreateOutput.setId("enabledCreateOutput");
        enabledCreateOutput.setValue("Enabled:   ");
        htmlPanelGrid.getChildren().add(enabledCreateOutput);
        
        SelectBooleanCheckbox enabledCreateInput = (SelectBooleanCheckbox) application.createComponent(SelectBooleanCheckbox.COMPONENT_TYPE);
        enabledCreateInput.setId("enabledCreateInput");
        enabledCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJStatusBean.JJStatus_.enabled}", Boolean.class));
        enabledCreateInput.setRequired(false);
        htmlPanelGrid.getChildren().add(enabledCreateInput);
        
        Message enabledCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        enabledCreateInputMessage.setId("enabledCreateInputMessage");
        enabledCreateInputMessage.setFor("enabledCreateInput");
        enabledCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(enabledCreateInputMessage);
        
        HtmlOutputText statusLevelCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        statusLevelCreateOutput.setId("statusLevelCreateOutput");
        statusLevelCreateOutput.setValue("Status Level:   ");
        htmlPanelGrid.getChildren().add(statusLevelCreateOutput);
        
        Spinner statusLevelCreateInput = (Spinner) application.createComponent(Spinner.COMPONENT_TYPE);
        statusLevelCreateInput.setId("statusLevelCreateInput");
        statusLevelCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJStatusBean.JJStatus_.statusLevel}", Integer.class));
        statusLevelCreateInput.setRequired(false);
        
        htmlPanelGrid.getChildren().add(statusLevelCreateInput);
        
        Message statusLevelCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        statusLevelCreateInputMessage.setId("statusLevelCreateInputMessage");
        statusLevelCreateInputMessage.setFor("statusLevelCreateInput");
        statusLevelCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(statusLevelCreateInputMessage);
        
        return htmlPanelGrid;
    }
    
    public HtmlPanelGrid JJStatusBean.populateEditPanel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        
        HtmlOutputText nameEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        nameEditOutput.setId("nameEditOutput");
        nameEditOutput.setValue("Name: * ");
        htmlPanelGrid.getChildren().add(nameEditOutput);
        
        InputText nameEditInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        nameEditInput.setId("nameEditInput");
        nameEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJStatusBean.JJStatus_.name}", String.class));
        LengthValidator nameEditInputValidator = new LengthValidator();
        nameEditInputValidator.setMaximum(25);
        nameEditInput.addValidator(nameEditInputValidator);
        nameEditInput.setRequired(true);
        htmlPanelGrid.getChildren().add(nameEditInput);
        
        Message nameEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        nameEditInputMessage.setId("nameEditInputMessage");
        nameEditInputMessage.setFor("nameEditInput");
        nameEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(nameEditInputMessage);
        
        HtmlOutputText descriptionEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        descriptionEditOutput.setId("descriptionEditOutput");
        descriptionEditOutput.setValue("Description: * ");
        htmlPanelGrid.getChildren().add(descriptionEditOutput);
        
        InputTextarea descriptionEditInput = (InputTextarea) application.createComponent(InputTextarea.COMPONENT_TYPE);
        descriptionEditInput.setId("descriptionEditInput");
        descriptionEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJStatusBean.JJStatus_.description}", String.class));
        LengthValidator descriptionEditInputValidator = new LengthValidator();
        descriptionEditInputValidator.setMaximum(250);
        descriptionEditInput.addValidator(descriptionEditInputValidator);
        descriptionEditInput.setRequired(true);
        htmlPanelGrid.getChildren().add(descriptionEditInput);
        
        Message descriptionEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        descriptionEditInputMessage.setId("descriptionEditInputMessage");
        descriptionEditInputMessage.setFor("descriptionEditInput");
        descriptionEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(descriptionEditInputMessage);
        
        HtmlOutputText creationDateEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        creationDateEditOutput.setId("creationDateEditOutput");
        creationDateEditOutput.setValue("Creation Date: * ");
        htmlPanelGrid.getChildren().add(creationDateEditOutput);
        
        Calendar creationDateEditInput = (Calendar) application.createComponent(Calendar.COMPONENT_TYPE);
        creationDateEditInput.setId("creationDateEditInput");
        creationDateEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJStatusBean.JJStatus_.creationDate}", Date.class));
        creationDateEditInput.setNavigator(true);
        creationDateEditInput.setEffect("slideDown");
        creationDateEditInput.setPattern("dd/MM/yyyy");
        creationDateEditInput.setRequired(true);
        htmlPanelGrid.getChildren().add(creationDateEditInput);
        
        Message creationDateEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        creationDateEditInputMessage.setId("creationDateEditInputMessage");
        creationDateEditInputMessage.setFor("creationDateEditInput");
        creationDateEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(creationDateEditInputMessage);
        
        HtmlOutputText createdByEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        createdByEditOutput.setId("createdByEditOutput");
        createdByEditOutput.setValue("Created By:   ");
        htmlPanelGrid.getChildren().add(createdByEditOutput);
        
        AutoComplete createdByEditInput = (AutoComplete) application.createComponent(AutoComplete.COMPONENT_TYPE);
        createdByEditInput.setId("createdByEditInput");
        createdByEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJStatusBean.JJStatus_.createdBy}", JJContact.class));
        createdByEditInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{jJStatusBean.completeCreatedBy}", List.class, new Class[] { String.class }));
        createdByEditInput.setDropdown(true);
        createdByEditInput.setValueExpression("var", expressionFactory.createValueExpression(elContext, "createdBy", String.class));
        createdByEditInput.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext, "#{createdBy.name} #{createdBy.description} #{createdBy.creationDate} #{createdBy.updatedDate}", String.class));
        createdByEditInput.setValueExpression("itemValue", expressionFactory.createValueExpression(elContext, "#{createdBy}", JJContact.class));
        createdByEditInput.setConverter(new JJContactConverter());
        createdByEditInput.setRequired(false);
        htmlPanelGrid.getChildren().add(createdByEditInput);
        
        Message createdByEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        createdByEditInputMessage.setId("createdByEditInputMessage");
        createdByEditInputMessage.setFor("createdByEditInput");
        createdByEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(createdByEditInputMessage);
        
        HtmlOutputText updatedDateEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        updatedDateEditOutput.setId("updatedDateEditOutput");
        updatedDateEditOutput.setValue("Updated Date:   ");
        htmlPanelGrid.getChildren().add(updatedDateEditOutput);
        
        Calendar updatedDateEditInput = (Calendar) application.createComponent(Calendar.COMPONENT_TYPE);
        updatedDateEditInput.setId("updatedDateEditInput");
        updatedDateEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJStatusBean.JJStatus_.updatedDate}", Date.class));
        updatedDateEditInput.setNavigator(true);
        updatedDateEditInput.setEffect("slideDown");
        updatedDateEditInput.setPattern("dd/MM/yyyy");
        updatedDateEditInput.setRequired(false);
        htmlPanelGrid.getChildren().add(updatedDateEditInput);
        
        Message updatedDateEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        updatedDateEditInputMessage.setId("updatedDateEditInputMessage");
        updatedDateEditInputMessage.setFor("updatedDateEditInput");
        updatedDateEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(updatedDateEditInputMessage);
        
        HtmlOutputText updatedByEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        updatedByEditOutput.setId("updatedByEditOutput");
        updatedByEditOutput.setValue("Updated By:   ");
        htmlPanelGrid.getChildren().add(updatedByEditOutput);
        
        AutoComplete updatedByEditInput = (AutoComplete) application.createComponent(AutoComplete.COMPONENT_TYPE);
        updatedByEditInput.setId("updatedByEditInput");
        updatedByEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJStatusBean.JJStatus_.updatedBy}", JJContact.class));
        updatedByEditInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{jJStatusBean.completeUpdatedBy}", List.class, new Class[] { String.class }));
        updatedByEditInput.setDropdown(true);
        updatedByEditInput.setValueExpression("var", expressionFactory.createValueExpression(elContext, "updatedBy", String.class));
        updatedByEditInput.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext, "#{updatedBy.name} #{updatedBy.description} #{updatedBy.creationDate} #{updatedBy.updatedDate}", String.class));
        updatedByEditInput.setValueExpression("itemValue", expressionFactory.createValueExpression(elContext, "#{updatedBy}", JJContact.class));
        updatedByEditInput.setConverter(new JJContactConverter());
        updatedByEditInput.setRequired(false);
        htmlPanelGrid.getChildren().add(updatedByEditInput);
        
        Message updatedByEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        updatedByEditInputMessage.setId("updatedByEditInputMessage");
        updatedByEditInputMessage.setFor("updatedByEditInput");
        updatedByEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(updatedByEditInputMessage);
        
        HtmlOutputText enabledEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        enabledEditOutput.setId("enabledEditOutput");
        enabledEditOutput.setValue("Enabled:   ");
        htmlPanelGrid.getChildren().add(enabledEditOutput);
        
        SelectBooleanCheckbox enabledEditInput = (SelectBooleanCheckbox) application.createComponent(SelectBooleanCheckbox.COMPONENT_TYPE);
        enabledEditInput.setId("enabledEditInput");
        enabledEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJStatusBean.JJStatus_.enabled}", Boolean.class));
        enabledEditInput.setRequired(false);
        htmlPanelGrid.getChildren().add(enabledEditInput);
        
        Message enabledEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        enabledEditInputMessage.setId("enabledEditInputMessage");
        enabledEditInputMessage.setFor("enabledEditInput");
        enabledEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(enabledEditInputMessage);
        
        HtmlOutputText statusLevelEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        statusLevelEditOutput.setId("statusLevelEditOutput");
        statusLevelEditOutput.setValue("Status Level:   ");
        htmlPanelGrid.getChildren().add(statusLevelEditOutput);
        
        Spinner statusLevelEditInput = (Spinner) application.createComponent(Spinner.COMPONENT_TYPE);
        statusLevelEditInput.setId("statusLevelEditInput");
        statusLevelEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJStatusBean.JJStatus_.statusLevel}", Integer.class));
        statusLevelEditInput.setRequired(false);
        
        htmlPanelGrid.getChildren().add(statusLevelEditInput);
        
        Message statusLevelEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        statusLevelEditInputMessage.setId("statusLevelEditInputMessage");
        statusLevelEditInputMessage.setFor("statusLevelEditInput");
        statusLevelEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(statusLevelEditInputMessage);
        
        return htmlPanelGrid;
    }
    
    public HtmlPanelGrid JJStatusBean.populateViewPanel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        
        HtmlOutputText nameLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        nameLabel.setId("nameLabel");
        nameLabel.setValue("Name:   ");
        htmlPanelGrid.getChildren().add(nameLabel);
        
        HtmlOutputText nameValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        nameValue.setId("nameValue");
        nameValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJStatusBean.JJStatus_.name}", String.class));
        htmlPanelGrid.getChildren().add(nameValue);
        
        HtmlOutputText descriptionLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        descriptionLabel.setId("descriptionLabel");
        descriptionLabel.setValue("Description:   ");
        htmlPanelGrid.getChildren().add(descriptionLabel);
        
        InputTextarea descriptionValue = (InputTextarea) application.createComponent(InputTextarea.COMPONENT_TYPE);
        descriptionValue.setId("descriptionValue");
        descriptionValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJStatusBean.JJStatus_.description}", String.class));
        descriptionValue.setReadonly(true);
        descriptionValue.setDisabled(true);
        htmlPanelGrid.getChildren().add(descriptionValue);
        
        HtmlOutputText creationDateLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        creationDateLabel.setId("creationDateLabel");
        creationDateLabel.setValue("Creation Date:   ");
        htmlPanelGrid.getChildren().add(creationDateLabel);
        
        HtmlOutputText creationDateValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        creationDateValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJStatusBean.JJStatus_.creationDate}", Date.class));
        DateTimeConverter creationDateValueConverter = (DateTimeConverter) application.createConverter(DateTimeConverter.CONVERTER_ID);
        creationDateValueConverter.setPattern("dd/MM/yyyy");
        creationDateValue.setConverter(creationDateValueConverter);
        htmlPanelGrid.getChildren().add(creationDateValue);
        
        HtmlOutputText createdByLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        createdByLabel.setId("createdByLabel");
        createdByLabel.setValue("Created By:   ");
        htmlPanelGrid.getChildren().add(createdByLabel);
        
        HtmlOutputText createdByValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        createdByValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJStatusBean.JJStatus_.createdBy}", JJContact.class));
        createdByValue.setConverter(new JJContactConverter());
        htmlPanelGrid.getChildren().add(createdByValue);
        
        HtmlOutputText updatedDateLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        updatedDateLabel.setId("updatedDateLabel");
        updatedDateLabel.setValue("Updated Date:   ");
        htmlPanelGrid.getChildren().add(updatedDateLabel);
        
        HtmlOutputText updatedDateValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        updatedDateValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJStatusBean.JJStatus_.updatedDate}", Date.class));
        DateTimeConverter updatedDateValueConverter = (DateTimeConverter) application.createConverter(DateTimeConverter.CONVERTER_ID);
        updatedDateValueConverter.setPattern("dd/MM/yyyy");
        updatedDateValue.setConverter(updatedDateValueConverter);
        htmlPanelGrid.getChildren().add(updatedDateValue);
        
        HtmlOutputText updatedByLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        updatedByLabel.setId("updatedByLabel");
        updatedByLabel.setValue("Updated By:   ");
        htmlPanelGrid.getChildren().add(updatedByLabel);
        
        HtmlOutputText updatedByValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        updatedByValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJStatusBean.JJStatus_.updatedBy}", JJContact.class));
        updatedByValue.setConverter(new JJContactConverter());
        htmlPanelGrid.getChildren().add(updatedByValue);
        
        HtmlOutputText enabledLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        enabledLabel.setId("enabledLabel");
        enabledLabel.setValue("Enabled:   ");
        htmlPanelGrid.getChildren().add(enabledLabel);
        
        HtmlOutputText enabledValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        enabledValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJStatusBean.JJStatus_.enabled}", String.class));
        htmlPanelGrid.getChildren().add(enabledValue);
        
        HtmlOutputText statusLevelLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        statusLevelLabel.setId("statusLevelLabel");
        statusLevelLabel.setValue("Status Level:   ");
        htmlPanelGrid.getChildren().add(statusLevelLabel);
        
        HtmlOutputText statusLevelValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        statusLevelValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJStatusBean.JJStatus_.statusLevel}", String.class));
        htmlPanelGrid.getChildren().add(statusLevelValue);
        
        return htmlPanelGrid;
    }
    
    public JJStatus JJStatusBean.getJJStatus_() {
        if (JJStatus_ == null) {
            JJStatus_ = new JJStatus();
        }
        return JJStatus_;
    }
    
    public void JJStatusBean.setJJStatus_(JJStatus JJStatus_) {
        this.JJStatus_ = JJStatus_;
    }
    
    public List<JJContact> JJStatusBean.completeCreatedBy(String query) {
        List<JJContact> suggestions = new ArrayList<JJContact>();
        for (JJContact jJContact : jJContactService.findAllJJContacts()) {
            String jJContactStr = String.valueOf(jJContact.getName() +  " "  + jJContact.getDescription() +  " "  + jJContact.getCreationDate() +  " "  + jJContact.getUpdatedDate());
            if (jJContactStr.toLowerCase().startsWith(query.toLowerCase())) {
                suggestions.add(jJContact);
            }
        }
        return suggestions;
    }
    
    public List<JJContact> JJStatusBean.completeUpdatedBy(String query) {
        List<JJContact> suggestions = new ArrayList<JJContact>();
        for (JJContact jJContact : jJContactService.findAllJJContacts()) {
            String jJContactStr = String.valueOf(jJContact.getName() +  " "  + jJContact.getDescription() +  " "  + jJContact.getCreationDate() +  " "  + jJContact.getUpdatedDate());
            if (jJContactStr.toLowerCase().startsWith(query.toLowerCase())) {
                suggestions.add(jJContact);
            }
        }
        return suggestions;
    }
    
    public String JJStatusBean.onEdit() {
        return null;
    }
    
    public boolean JJStatusBean.isCreateDialogVisible() {
        return createDialogVisible;
    }
    
    public void JJStatusBean.setCreateDialogVisible(boolean createDialogVisible) {
        this.createDialogVisible = createDialogVisible;
    }
    
    public String JJStatusBean.displayList() {
        createDialogVisible = false;
        findAllJJStatuses();
        return "JJStatus_";
    }
    
    public String JJStatusBean.displayCreateDialog() {
        JJStatus_ = new JJStatus();
        createDialogVisible = true;
        return "JJStatus_";
    }
    
    public String JJStatusBean.persist() {
        String message = "";
        if (JJStatus_.getId() != null) {
            jJStatusService.updateJJStatus(JJStatus_);
            message = "Successfully updated";
        } else {
            jJStatusService.saveJJStatus(JJStatus_);
            message = "Successfully created";
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("createDialog.hide()");
        context.execute("editDialog.hide()");
        
        FacesMessage facesMessage = new FacesMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllJJStatuses();
    }
    
    public String JJStatusBean.delete() {
        jJStatusService.deleteJJStatus(JJStatus_);
        FacesMessage facesMessage = new FacesMessage("Successfully deleted");
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllJJStatuses();
    }
    
    public void JJStatusBean.reset() {
        JJStatus_ = null;
        createDialogVisible = false;
    }
    
    public void JJStatusBean.handleDialogClose(CloseEvent event) {
        reset();
    }
    
}

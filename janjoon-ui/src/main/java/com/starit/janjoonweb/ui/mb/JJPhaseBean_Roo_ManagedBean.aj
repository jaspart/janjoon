// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.ui.mb;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJContactService;
import com.starit.janjoonweb.domain.JJPhase;
import com.starit.janjoonweb.domain.JJPhaseService;
import com.starit.janjoonweb.ui.mb.JJPhaseBean;
import com.starit.janjoonweb.ui.mb.converter.JJContactConverter;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
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
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.message.Message;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.selectbooleancheckbox.SelectBooleanCheckbox;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;
import org.springframework.beans.factory.annotation.Autowired;

privileged aspect JJPhaseBean_Roo_ManagedBean {
    
    declare @type: JJPhaseBean: @ManagedBean(name = "jJPhaseBean");
    
    declare @type: JJPhaseBean: @SessionScoped;
    
    @Autowired
    JJPhaseService JJPhaseBean.jJPhaseService;
    
    @Autowired
    JJContactService JJPhaseBean.jJContactService;
    
    private String JJPhaseBean.name = "JJPhases";
    
    private JJPhase JJPhaseBean.JJPhase_;
    
    private List<JJPhase> JJPhaseBean.allJJPhases;
    
    private boolean JJPhaseBean.dataVisible = false;
    
    private List<String> JJPhaseBean.columns;
    
    private HtmlPanelGrid JJPhaseBean.createPanelGrid;
    
    private HtmlPanelGrid JJPhaseBean.editPanelGrid;
    
    private HtmlPanelGrid JJPhaseBean.viewPanelGrid;
    
    private boolean JJPhaseBean.createDialogVisible = false;
    
    @PostConstruct
    public void JJPhaseBean.init() {
        columns = new ArrayList<String>();
        columns.add("name");
        columns.add("description");
        columns.add("creationDate");
        columns.add("updatedDate");
    }
    
    public String JJPhaseBean.getName() {
        return name;
    }
    
    public List<String> JJPhaseBean.getColumns() {
        return columns;
    }
    
    public List<JJPhase> JJPhaseBean.getAllJJPhases() {
        return allJJPhases;
    }
    
    public void JJPhaseBean.setAllJJPhases(List<JJPhase> allJJPhases) {
        this.allJJPhases = allJJPhases;
    }
    
    public String JJPhaseBean.findAllJJPhases() {
        allJJPhases = jJPhaseService.findAllJJPhases();
        dataVisible = !allJJPhases.isEmpty();
        return null;
    }
    
    public boolean JJPhaseBean.isDataVisible() {
        return dataVisible;
    }
    
    public void JJPhaseBean.setDataVisible(boolean dataVisible) {
        this.dataVisible = dataVisible;
    }
    
    public HtmlPanelGrid JJPhaseBean.getCreatePanelGrid() {
        if (createPanelGrid == null) {
            createPanelGrid = populateCreatePanel();
        }
        return createPanelGrid;
    }
    
    public void JJPhaseBean.setCreatePanelGrid(HtmlPanelGrid createPanelGrid) {
        this.createPanelGrid = createPanelGrid;
    }
    
    public HtmlPanelGrid JJPhaseBean.getEditPanelGrid() {
        if (editPanelGrid == null) {
            editPanelGrid = populateEditPanel();
        }
        return editPanelGrid;
    }
    
    public void JJPhaseBean.setEditPanelGrid(HtmlPanelGrid editPanelGrid) {
        this.editPanelGrid = editPanelGrid;
    }
    
    public HtmlPanelGrid JJPhaseBean.getViewPanelGrid() {
        return populateViewPanel();
    }
    
    public void JJPhaseBean.setViewPanelGrid(HtmlPanelGrid viewPanelGrid) {
        this.viewPanelGrid = viewPanelGrid;
    }
    
    public HtmlPanelGrid JJPhaseBean.populateCreatePanel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        javax.faces.application.Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        
        OutputLabel nameCreateOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        nameCreateOutput.setFor("nameCreateInput");
        nameCreateOutput.setId("nameCreateOutput");
        nameCreateOutput.setValue("Name:");
        htmlPanelGrid.getChildren().add(nameCreateOutput);
        
        InputTextarea nameCreateInput = (InputTextarea) application.createComponent(InputTextarea.COMPONENT_TYPE);
        nameCreateInput.setId("nameCreateInput");
        nameCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJPhaseBean.JJPhase_.name}", String.class));
        LengthValidator nameCreateInputValidator = new LengthValidator();
        nameCreateInputValidator.setMaximum(100);
        nameCreateInput.addValidator(nameCreateInputValidator);
        nameCreateInput.setRequired(true);
        htmlPanelGrid.getChildren().add(nameCreateInput);
        
        Message nameCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        nameCreateInputMessage.setId("nameCreateInputMessage");
        nameCreateInputMessage.setFor("nameCreateInput");
        nameCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(nameCreateInputMessage);
        
        OutputLabel descriptionCreateOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        descriptionCreateOutput.setFor("descriptionCreateInput");
        descriptionCreateOutput.setId("descriptionCreateOutput");
        descriptionCreateOutput.setValue("Description:");
        htmlPanelGrid.getChildren().add(descriptionCreateOutput);
        
        InputTextarea descriptionCreateInput = (InputTextarea) application.createComponent(InputTextarea.COMPONENT_TYPE);
        descriptionCreateInput.setId("descriptionCreateInput");
        descriptionCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJPhaseBean.JJPhase_.description}", String.class));
        descriptionCreateInput.setRequired(true);
        htmlPanelGrid.getChildren().add(descriptionCreateInput);
        
        Message descriptionCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        descriptionCreateInputMessage.setId("descriptionCreateInputMessage");
        descriptionCreateInputMessage.setFor("descriptionCreateInput");
        descriptionCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(descriptionCreateInputMessage);
        
        OutputLabel creationDateCreateOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        creationDateCreateOutput.setFor("creationDateCreateInput");
        creationDateCreateOutput.setId("creationDateCreateOutput");
        creationDateCreateOutput.setValue("Creation Date:");
        htmlPanelGrid.getChildren().add(creationDateCreateOutput);
        
        Calendar creationDateCreateInput = (Calendar) application.createComponent(Calendar.COMPONENT_TYPE);
        creationDateCreateInput.setId("creationDateCreateInput");
        creationDateCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJPhaseBean.JJPhase_.creationDate}", Date.class));
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
        
        OutputLabel createdByCreateOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        createdByCreateOutput.setFor("createdByCreateInput");
        createdByCreateOutput.setId("createdByCreateOutput");
        createdByCreateOutput.setValue("Created By:");
        htmlPanelGrid.getChildren().add(createdByCreateOutput);
        
        AutoComplete createdByCreateInput = (AutoComplete) application.createComponent(AutoComplete.COMPONENT_TYPE);
        createdByCreateInput.setId("createdByCreateInput");
        createdByCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJPhaseBean.JJPhase_.createdBy}", JJContact.class));
        createdByCreateInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{jJPhaseBean.completeCreatedBy}", List.class, new Class[] { String.class }));
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
        
        OutputLabel updatedDateCreateOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        updatedDateCreateOutput.setFor("updatedDateCreateInput");
        updatedDateCreateOutput.setId("updatedDateCreateOutput");
        updatedDateCreateOutput.setValue("Updated Date:");
        htmlPanelGrid.getChildren().add(updatedDateCreateOutput);
        
        Calendar updatedDateCreateInput = (Calendar) application.createComponent(Calendar.COMPONENT_TYPE);
        updatedDateCreateInput.setId("updatedDateCreateInput");
        updatedDateCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJPhaseBean.JJPhase_.updatedDate}", Date.class));
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
        
        OutputLabel updatedByCreateOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        updatedByCreateOutput.setFor("updatedByCreateInput");
        updatedByCreateOutput.setId("updatedByCreateOutput");
        updatedByCreateOutput.setValue("Updated By:");
        htmlPanelGrid.getChildren().add(updatedByCreateOutput);
        
        AutoComplete updatedByCreateInput = (AutoComplete) application.createComponent(AutoComplete.COMPONENT_TYPE);
        updatedByCreateInput.setId("updatedByCreateInput");
        updatedByCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJPhaseBean.JJPhase_.updatedBy}", JJContact.class));
        updatedByCreateInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{jJPhaseBean.completeUpdatedBy}", List.class, new Class[] { String.class }));
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
        
        OutputLabel enabledCreateOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        enabledCreateOutput.setFor("enabledCreateInput");
        enabledCreateOutput.setId("enabledCreateOutput");
        enabledCreateOutput.setValue("Enabled:");
        htmlPanelGrid.getChildren().add(enabledCreateOutput);
        
        SelectBooleanCheckbox enabledCreateInput = (SelectBooleanCheckbox) application.createComponent(SelectBooleanCheckbox.COMPONENT_TYPE);
        enabledCreateInput.setId("enabledCreateInput");
        enabledCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJPhaseBean.JJPhase_.enabled}", Boolean.class));
        enabledCreateInput.setRequired(false);
        htmlPanelGrid.getChildren().add(enabledCreateInput);
        
        Message enabledCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        enabledCreateInputMessage.setId("enabledCreateInputMessage");
        enabledCreateInputMessage.setFor("enabledCreateInput");
        enabledCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(enabledCreateInputMessage);
        
        return htmlPanelGrid;
    }
    
    public HtmlPanelGrid JJPhaseBean.populateEditPanel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        javax.faces.application.Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        
        OutputLabel nameEditOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        nameEditOutput.setFor("nameEditInput");
        nameEditOutput.setId("nameEditOutput");
        nameEditOutput.setValue("Name:");
        htmlPanelGrid.getChildren().add(nameEditOutput);
        
        InputTextarea nameEditInput = (InputTextarea) application.createComponent(InputTextarea.COMPONENT_TYPE);
        nameEditInput.setId("nameEditInput");
        nameEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJPhaseBean.JJPhase_.name}", String.class));
        LengthValidator nameEditInputValidator = new LengthValidator();
        nameEditInputValidator.setMaximum(100);
        nameEditInput.addValidator(nameEditInputValidator);
        nameEditInput.setRequired(true);
        htmlPanelGrid.getChildren().add(nameEditInput);
        
        Message nameEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        nameEditInputMessage.setId("nameEditInputMessage");
        nameEditInputMessage.setFor("nameEditInput");
        nameEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(nameEditInputMessage);
        
        OutputLabel descriptionEditOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        descriptionEditOutput.setFor("descriptionEditInput");
        descriptionEditOutput.setId("descriptionEditOutput");
        descriptionEditOutput.setValue("Description:");
        htmlPanelGrid.getChildren().add(descriptionEditOutput);
        
        InputTextarea descriptionEditInput = (InputTextarea) application.createComponent(InputTextarea.COMPONENT_TYPE);
        descriptionEditInput.setId("descriptionEditInput");
        descriptionEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJPhaseBean.JJPhase_.description}", String.class));
        descriptionEditInput.setRequired(true);
        htmlPanelGrid.getChildren().add(descriptionEditInput);
        
        Message descriptionEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        descriptionEditInputMessage.setId("descriptionEditInputMessage");
        descriptionEditInputMessage.setFor("descriptionEditInput");
        descriptionEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(descriptionEditInputMessage);
        
        OutputLabel creationDateEditOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        creationDateEditOutput.setFor("creationDateEditInput");
        creationDateEditOutput.setId("creationDateEditOutput");
        creationDateEditOutput.setValue("Creation Date:");
        htmlPanelGrid.getChildren().add(creationDateEditOutput);
        
        Calendar creationDateEditInput = (Calendar) application.createComponent(Calendar.COMPONENT_TYPE);
        creationDateEditInput.setId("creationDateEditInput");
        creationDateEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJPhaseBean.JJPhase_.creationDate}", Date.class));
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
        
        OutputLabel createdByEditOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        createdByEditOutput.setFor("createdByEditInput");
        createdByEditOutput.setId("createdByEditOutput");
        createdByEditOutput.setValue("Created By:");
        htmlPanelGrid.getChildren().add(createdByEditOutput);
        
        AutoComplete createdByEditInput = (AutoComplete) application.createComponent(AutoComplete.COMPONENT_TYPE);
        createdByEditInput.setId("createdByEditInput");
        createdByEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJPhaseBean.JJPhase_.createdBy}", JJContact.class));
        createdByEditInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{jJPhaseBean.completeCreatedBy}", List.class, new Class[] { String.class }));
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
        
        OutputLabel updatedDateEditOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        updatedDateEditOutput.setFor("updatedDateEditInput");
        updatedDateEditOutput.setId("updatedDateEditOutput");
        updatedDateEditOutput.setValue("Updated Date:");
        htmlPanelGrid.getChildren().add(updatedDateEditOutput);
        
        Calendar updatedDateEditInput = (Calendar) application.createComponent(Calendar.COMPONENT_TYPE);
        updatedDateEditInput.setId("updatedDateEditInput");
        updatedDateEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJPhaseBean.JJPhase_.updatedDate}", Date.class));
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
        
        OutputLabel updatedByEditOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        updatedByEditOutput.setFor("updatedByEditInput");
        updatedByEditOutput.setId("updatedByEditOutput");
        updatedByEditOutput.setValue("Updated By:");
        htmlPanelGrid.getChildren().add(updatedByEditOutput);
        
        AutoComplete updatedByEditInput = (AutoComplete) application.createComponent(AutoComplete.COMPONENT_TYPE);
        updatedByEditInput.setId("updatedByEditInput");
        updatedByEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJPhaseBean.JJPhase_.updatedBy}", JJContact.class));
        updatedByEditInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{jJPhaseBean.completeUpdatedBy}", List.class, new Class[] { String.class }));
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
        
        OutputLabel enabledEditOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        enabledEditOutput.setFor("enabledEditInput");
        enabledEditOutput.setId("enabledEditOutput");
        enabledEditOutput.setValue("Enabled:");
        htmlPanelGrid.getChildren().add(enabledEditOutput);
        
        SelectBooleanCheckbox enabledEditInput = (SelectBooleanCheckbox) application.createComponent(SelectBooleanCheckbox.COMPONENT_TYPE);
        enabledEditInput.setId("enabledEditInput");
        enabledEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJPhaseBean.JJPhase_.enabled}", Boolean.class));
        enabledEditInput.setRequired(false);
        htmlPanelGrid.getChildren().add(enabledEditInput);
        
        Message enabledEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        enabledEditInputMessage.setId("enabledEditInputMessage");
        enabledEditInputMessage.setFor("enabledEditInput");
        enabledEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(enabledEditInputMessage);
        
        return htmlPanelGrid;
    }
    
    public HtmlPanelGrid JJPhaseBean.populateViewPanel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        javax.faces.application.Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        
        HtmlOutputText nameLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        nameLabel.setId("nameLabel");
        nameLabel.setValue("Name:");
        htmlPanelGrid.getChildren().add(nameLabel);
        
        InputTextarea nameValue = (InputTextarea) application.createComponent(InputTextarea.COMPONENT_TYPE);
        nameValue.setId("nameValue");
        nameValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJPhaseBean.JJPhase_.name}", String.class));
        nameValue.setReadonly(true);
        nameValue.setDisabled(true);
        htmlPanelGrid.getChildren().add(nameValue);
        
        HtmlOutputText descriptionLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        descriptionLabel.setId("descriptionLabel");
        descriptionLabel.setValue("Description:");
        htmlPanelGrid.getChildren().add(descriptionLabel);
        
        InputTextarea descriptionValue = (InputTextarea) application.createComponent(InputTextarea.COMPONENT_TYPE);
        descriptionValue.setId("descriptionValue");
        descriptionValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJPhaseBean.JJPhase_.description}", String.class));
        descriptionValue.setReadonly(true);
        descriptionValue.setDisabled(true);
        htmlPanelGrid.getChildren().add(descriptionValue);
        
        HtmlOutputText creationDateLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        creationDateLabel.setId("creationDateLabel");
        creationDateLabel.setValue("Creation Date:");
        htmlPanelGrid.getChildren().add(creationDateLabel);
        
        HtmlOutputText creationDateValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        creationDateValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJPhaseBean.JJPhase_.creationDate}", Date.class));
        DateTimeConverter creationDateValueConverter = (DateTimeConverter) application.createConverter(DateTimeConverter.CONVERTER_ID);
        creationDateValueConverter.setPattern("dd/MM/yyyy");
        creationDateValue.setConverter(creationDateValueConverter);
        htmlPanelGrid.getChildren().add(creationDateValue);
        
        HtmlOutputText createdByLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        createdByLabel.setId("createdByLabel");
        createdByLabel.setValue("Created By:");
        htmlPanelGrid.getChildren().add(createdByLabel);
        
        HtmlOutputText createdByValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        createdByValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJPhaseBean.JJPhase_.createdBy}", JJContact.class));
        createdByValue.setConverter(new JJContactConverter());
        htmlPanelGrid.getChildren().add(createdByValue);
        
        HtmlOutputText updatedDateLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        updatedDateLabel.setId("updatedDateLabel");
        updatedDateLabel.setValue("Updated Date:");
        htmlPanelGrid.getChildren().add(updatedDateLabel);
        
        HtmlOutputText updatedDateValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        updatedDateValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJPhaseBean.JJPhase_.updatedDate}", Date.class));
        DateTimeConverter updatedDateValueConverter = (DateTimeConverter) application.createConverter(DateTimeConverter.CONVERTER_ID);
        updatedDateValueConverter.setPattern("dd/MM/yyyy");
        updatedDateValue.setConverter(updatedDateValueConverter);
        htmlPanelGrid.getChildren().add(updatedDateValue);
        
        HtmlOutputText updatedByLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        updatedByLabel.setId("updatedByLabel");
        updatedByLabel.setValue("Updated By:");
        htmlPanelGrid.getChildren().add(updatedByLabel);
        
        HtmlOutputText updatedByValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        updatedByValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJPhaseBean.JJPhase_.updatedBy}", JJContact.class));
        updatedByValue.setConverter(new JJContactConverter());
        htmlPanelGrid.getChildren().add(updatedByValue);
        
        HtmlOutputText enabledLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        enabledLabel.setId("enabledLabel");
        enabledLabel.setValue("Enabled:");
        htmlPanelGrid.getChildren().add(enabledLabel);
        
        HtmlOutputText enabledValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        enabledValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJPhaseBean.JJPhase_.enabled}", String.class));
        htmlPanelGrid.getChildren().add(enabledValue);
        
        return htmlPanelGrid;
    }
    
    public JJPhase JJPhaseBean.getJJPhase_() {
        if (JJPhase_ == null) {
            JJPhase_ = new JJPhase();
        }
        return JJPhase_;
    }
    
    public void JJPhaseBean.setJJPhase_(JJPhase JJPhase_) {
        this.JJPhase_ = JJPhase_;
    }
    
    public List<JJContact> JJPhaseBean.completeCreatedBy(String query) {
        List<JJContact> suggestions = new ArrayList<JJContact>();
        for (JJContact jJContact : jJContactService.findAllJJContacts()) {
            String jJContactStr = String.valueOf(jJContact.getName() +  " "  + jJContact.getDescription() +  " "  + jJContact.getCreationDate() +  " "  + jJContact.getUpdatedDate());
            if (jJContactStr.toLowerCase().startsWith(query.toLowerCase())) {
                suggestions.add(jJContact);
            }
        }
        return suggestions;
    }
    
    public List<JJContact> JJPhaseBean.completeUpdatedBy(String query) {
        List<JJContact> suggestions = new ArrayList<JJContact>();
        for (JJContact jJContact : jJContactService.findAllJJContacts()) {
            String jJContactStr = String.valueOf(jJContact.getName() +  " "  + jJContact.getDescription() +  " "  + jJContact.getCreationDate() +  " "  + jJContact.getUpdatedDate());
            if (jJContactStr.toLowerCase().startsWith(query.toLowerCase())) {
                suggestions.add(jJContact);
            }
        }
        return suggestions;
    }
    
    public String JJPhaseBean.onEdit() {
        return null;
    }
    
    public boolean JJPhaseBean.isCreateDialogVisible() {
        return createDialogVisible;
    }
    
    public void JJPhaseBean.setCreateDialogVisible(boolean createDialogVisible) {
        this.createDialogVisible = createDialogVisible;
    }
    
    public String JJPhaseBean.displayList() {
        createDialogVisible = false;
        findAllJJPhases();
        return "JJPhase_";
    }
    
    public String JJPhaseBean.displayCreateDialog() {
        JJPhase_ = new JJPhase();
        createDialogVisible = true;
        return "JJPhase_";
    }
    
    public String JJPhaseBean.persist() {
        String message = "";
        if (JJPhase_.getId() != null) {
            jJPhaseService.updateJJPhase(JJPhase_);
            message = "message_successfully_updated";
        } else {
            jJPhaseService.saveJJPhase(JJPhase_);
            message = "message_successfully_created";
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("createDialogWidget.hide()");
        context.execute("editDialogWidget.hide()");
        
        FacesMessage facesMessage = MessageFactory.getMessage(message, "JJPhase");
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllJJPhases();
    }
    
    public String JJPhaseBean.delete() {
        jJPhaseService.deleteJJPhase(JJPhase_);
        FacesMessage facesMessage = MessageFactory.getMessage("message_successfully_deleted", "JJPhase");
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllJJPhases();
    }
    
    public void JJPhaseBean.reset() {
        JJPhase_ = null;
        createDialogVisible = false;
    }
    
    public void JJPhaseBean.handleDialogClose(CloseEvent event) {
        reset();
    }
    
}
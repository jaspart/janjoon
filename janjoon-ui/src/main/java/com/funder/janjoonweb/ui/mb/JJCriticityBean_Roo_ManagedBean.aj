// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.ui.mb;

import com.funder.janjoonweb.domain.JJContact;
import com.funder.janjoonweb.domain.JJContactService;
import com.funder.janjoonweb.domain.JJCriticity;
import com.funder.janjoonweb.domain.JJCriticityService;
import com.funder.janjoonweb.ui.mb.JJCriticityBean;
import com.funder.janjoonweb.ui.mb.converter.JJContactConverter;
import com.funder.janjoonweb.ui.mb.util.MessageFactory;
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
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.message.Message;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.selectbooleancheckbox.SelectBooleanCheckbox;
import org.primefaces.component.spinner.Spinner;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;
import org.springframework.beans.factory.annotation.Autowired;

privileged aspect JJCriticityBean_Roo_ManagedBean {
    
    declare @type: JJCriticityBean: @ManagedBean(name = "jJCriticityBean");
    
    declare @type: JJCriticityBean: @SessionScoped;
    
    @Autowired
    JJCriticityService JJCriticityBean.jJCriticityService;
    
    @Autowired
    JJContactService JJCriticityBean.jJContactService;
    
    private String JJCriticityBean.name = "JJCriticitys";
    
    private JJCriticity JJCriticityBean.JJCriticity_;
    
    private List<JJCriticity> JJCriticityBean.allJJCriticitys;
    
    private boolean JJCriticityBean.dataVisible = false;
    
    private List<String> JJCriticityBean.columns;
    
    private HtmlPanelGrid JJCriticityBean.createPanelGrid;
    
    private HtmlPanelGrid JJCriticityBean.editPanelGrid;
    
    private HtmlPanelGrid JJCriticityBean.viewPanelGrid;
    
    private boolean JJCriticityBean.createDialogVisible = false;
    
    @PostConstruct
    public void JJCriticityBean.init() {
        columns = new ArrayList<String>();
        columns.add("name");
        columns.add("description");
        columns.add("creationDate");
        columns.add("updatedDate");
        columns.add("criticityLevel");
    }
    
    public String JJCriticityBean.getName() {
        return name;
    }
    
    public List<String> JJCriticityBean.getColumns() {
        return columns;
    }
    
    public List<JJCriticity> JJCriticityBean.getAllJJCriticitys() {
        return allJJCriticitys;
    }
    
    public void JJCriticityBean.setAllJJCriticitys(List<JJCriticity> allJJCriticitys) {
        this.allJJCriticitys = allJJCriticitys;
    }
    
    public String JJCriticityBean.findAllJJCriticitys() {
        allJJCriticitys = jJCriticityService.findAllJJCriticitys();
        dataVisible = !allJJCriticitys.isEmpty();
        return null;
    }
    
    public boolean JJCriticityBean.isDataVisible() {
        return dataVisible;
    }
    
    public void JJCriticityBean.setDataVisible(boolean dataVisible) {
        this.dataVisible = dataVisible;
    }
    
    public HtmlPanelGrid JJCriticityBean.getCreatePanelGrid() {
        if (createPanelGrid == null) {
            createPanelGrid = populateCreatePanel();
        }
        return createPanelGrid;
    }
    
    public void JJCriticityBean.setCreatePanelGrid(HtmlPanelGrid createPanelGrid) {
        this.createPanelGrid = createPanelGrid;
    }
    
    public HtmlPanelGrid JJCriticityBean.getEditPanelGrid() {
        if (editPanelGrid == null) {
            editPanelGrid = populateEditPanel();
        }
        return editPanelGrid;
    }
    
    public void JJCriticityBean.setEditPanelGrid(HtmlPanelGrid editPanelGrid) {
        this.editPanelGrid = editPanelGrid;
    }
    
    public HtmlPanelGrid JJCriticityBean.getViewPanelGrid() {
        return populateViewPanel();
    }
    
    public void JJCriticityBean.setViewPanelGrid(HtmlPanelGrid viewPanelGrid) {
        this.viewPanelGrid = viewPanelGrid;
    }
    
    public HtmlPanelGrid JJCriticityBean.populateCreatePanel() {
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
        
        InputText nameCreateInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        nameCreateInput.setId("nameCreateInput");
        nameCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCriticityBean.JJCriticity_.name}", String.class));
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
        
        OutputLabel descriptionCreateOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        descriptionCreateOutput.setFor("descriptionCreateInput");
        descriptionCreateOutput.setId("descriptionCreateOutput");
        descriptionCreateOutput.setValue("Description:");
        htmlPanelGrid.getChildren().add(descriptionCreateOutput);
        
        InputTextarea descriptionCreateInput = (InputTextarea) application.createComponent(InputTextarea.COMPONENT_TYPE);
        descriptionCreateInput.setId("descriptionCreateInput");
        descriptionCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCriticityBean.JJCriticity_.description}", String.class));
        LengthValidator descriptionCreateInputValidator = new LengthValidator();
        descriptionCreateInputValidator.setMaximum(500);
        descriptionCreateInput.addValidator(descriptionCreateInputValidator);
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
        creationDateCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCriticityBean.JJCriticity_.creationDate}", Date.class));
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
        createdByCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCriticityBean.JJCriticity_.createdBy}", JJContact.class));
        createdByCreateInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{jJCriticityBean.completeCreatedBy}", List.class, new Class[] { String.class }));
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
        updatedDateCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCriticityBean.JJCriticity_.updatedDate}", Date.class));
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
        updatedByCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCriticityBean.JJCriticity_.updatedBy}", JJContact.class));
        updatedByCreateInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{jJCriticityBean.completeUpdatedBy}", List.class, new Class[] { String.class }));
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
        enabledCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCriticityBean.JJCriticity_.enabled}", Boolean.class));
        enabledCreateInput.setRequired(false);
        htmlPanelGrid.getChildren().add(enabledCreateInput);
        
        Message enabledCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        enabledCreateInputMessage.setId("enabledCreateInputMessage");
        enabledCreateInputMessage.setFor("enabledCreateInput");
        enabledCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(enabledCreateInputMessage);
        
        OutputLabel criticityLevelCreateOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        criticityLevelCreateOutput.setFor("criticityLevelCreateInput");
        criticityLevelCreateOutput.setId("criticityLevelCreateOutput");
        criticityLevelCreateOutput.setValue("Criticity Level:");
        htmlPanelGrid.getChildren().add(criticityLevelCreateOutput);
        
        Spinner criticityLevelCreateInput = (Spinner) application.createComponent(Spinner.COMPONENT_TYPE);
        criticityLevelCreateInput.setId("criticityLevelCreateInput");
        criticityLevelCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCriticityBean.JJCriticity_.criticityLevel}", Integer.class));
        criticityLevelCreateInput.setRequired(false);
        
        htmlPanelGrid.getChildren().add(criticityLevelCreateInput);
        
        Message criticityLevelCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        criticityLevelCreateInputMessage.setId("criticityLevelCreateInputMessage");
        criticityLevelCreateInputMessage.setFor("criticityLevelCreateInput");
        criticityLevelCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(criticityLevelCreateInputMessage);
        
        return htmlPanelGrid;
    }
    
    public HtmlPanelGrid JJCriticityBean.populateEditPanel() {
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
        
        InputText nameEditInput = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
        nameEditInput.setId("nameEditInput");
        nameEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCriticityBean.JJCriticity_.name}", String.class));
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
        
        OutputLabel descriptionEditOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        descriptionEditOutput.setFor("descriptionEditInput");
        descriptionEditOutput.setId("descriptionEditOutput");
        descriptionEditOutput.setValue("Description:");
        htmlPanelGrid.getChildren().add(descriptionEditOutput);
        
        InputTextarea descriptionEditInput = (InputTextarea) application.createComponent(InputTextarea.COMPONENT_TYPE);
        descriptionEditInput.setId("descriptionEditInput");
        descriptionEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCriticityBean.JJCriticity_.description}", String.class));
        LengthValidator descriptionEditInputValidator = new LengthValidator();
        descriptionEditInputValidator.setMaximum(500);
        descriptionEditInput.addValidator(descriptionEditInputValidator);
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
        creationDateEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCriticityBean.JJCriticity_.creationDate}", Date.class));
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
        createdByEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCriticityBean.JJCriticity_.createdBy}", JJContact.class));
        createdByEditInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{jJCriticityBean.completeCreatedBy}", List.class, new Class[] { String.class }));
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
        updatedDateEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCriticityBean.JJCriticity_.updatedDate}", Date.class));
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
        updatedByEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCriticityBean.JJCriticity_.updatedBy}", JJContact.class));
        updatedByEditInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{jJCriticityBean.completeUpdatedBy}", List.class, new Class[] { String.class }));
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
        enabledEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCriticityBean.JJCriticity_.enabled}", Boolean.class));
        enabledEditInput.setRequired(false);
        htmlPanelGrid.getChildren().add(enabledEditInput);
        
        Message enabledEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        enabledEditInputMessage.setId("enabledEditInputMessage");
        enabledEditInputMessage.setFor("enabledEditInput");
        enabledEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(enabledEditInputMessage);
        
        OutputLabel criticityLevelEditOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        criticityLevelEditOutput.setFor("criticityLevelEditInput");
        criticityLevelEditOutput.setId("criticityLevelEditOutput");
        criticityLevelEditOutput.setValue("Criticity Level:");
        htmlPanelGrid.getChildren().add(criticityLevelEditOutput);
        
        Spinner criticityLevelEditInput = (Spinner) application.createComponent(Spinner.COMPONENT_TYPE);
        criticityLevelEditInput.setId("criticityLevelEditInput");
        criticityLevelEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCriticityBean.JJCriticity_.criticityLevel}", Integer.class));
        criticityLevelEditInput.setRequired(false);
        
        htmlPanelGrid.getChildren().add(criticityLevelEditInput);
        
        Message criticityLevelEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        criticityLevelEditInputMessage.setId("criticityLevelEditInputMessage");
        criticityLevelEditInputMessage.setFor("criticityLevelEditInput");
        criticityLevelEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(criticityLevelEditInputMessage);
        
        return htmlPanelGrid;
    }
    
    public HtmlPanelGrid JJCriticityBean.populateViewPanel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        javax.faces.application.Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        
        HtmlOutputText nameLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        nameLabel.setId("nameLabel");
        nameLabel.setValue("Name:");
        htmlPanelGrid.getChildren().add(nameLabel);
        
        HtmlOutputText nameValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        nameValue.setId("nameValue");
        nameValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCriticityBean.JJCriticity_.name}", String.class));
        htmlPanelGrid.getChildren().add(nameValue);
        
        HtmlOutputText descriptionLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        descriptionLabel.setId("descriptionLabel");
        descriptionLabel.setValue("Description:");
        htmlPanelGrid.getChildren().add(descriptionLabel);
        
        InputTextarea descriptionValue = (InputTextarea) application.createComponent(InputTextarea.COMPONENT_TYPE);
        descriptionValue.setId("descriptionValue");
        descriptionValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCriticityBean.JJCriticity_.description}", String.class));
        descriptionValue.setReadonly(true);
        descriptionValue.setDisabled(true);
        htmlPanelGrid.getChildren().add(descriptionValue);
        
        HtmlOutputText creationDateLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        creationDateLabel.setId("creationDateLabel");
        creationDateLabel.setValue("Creation Date:");
        htmlPanelGrid.getChildren().add(creationDateLabel);
        
        HtmlOutputText creationDateValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        creationDateValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCriticityBean.JJCriticity_.creationDate}", Date.class));
        DateTimeConverter creationDateValueConverter = (DateTimeConverter) application.createConverter(DateTimeConverter.CONVERTER_ID);
        creationDateValueConverter.setPattern("dd/MM/yyyy");
        creationDateValue.setConverter(creationDateValueConverter);
        htmlPanelGrid.getChildren().add(creationDateValue);
        
        HtmlOutputText createdByLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        createdByLabel.setId("createdByLabel");
        createdByLabel.setValue("Created By:");
        htmlPanelGrid.getChildren().add(createdByLabel);
        
        HtmlOutputText createdByValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        createdByValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCriticityBean.JJCriticity_.createdBy}", JJContact.class));
        createdByValue.setConverter(new JJContactConverter());
        htmlPanelGrid.getChildren().add(createdByValue);
        
        HtmlOutputText updatedDateLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        updatedDateLabel.setId("updatedDateLabel");
        updatedDateLabel.setValue("Updated Date:");
        htmlPanelGrid.getChildren().add(updatedDateLabel);
        
        HtmlOutputText updatedDateValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        updatedDateValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCriticityBean.JJCriticity_.updatedDate}", Date.class));
        DateTimeConverter updatedDateValueConverter = (DateTimeConverter) application.createConverter(DateTimeConverter.CONVERTER_ID);
        updatedDateValueConverter.setPattern("dd/MM/yyyy");
        updatedDateValue.setConverter(updatedDateValueConverter);
        htmlPanelGrid.getChildren().add(updatedDateValue);
        
        HtmlOutputText updatedByLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        updatedByLabel.setId("updatedByLabel");
        updatedByLabel.setValue("Updated By:");
        htmlPanelGrid.getChildren().add(updatedByLabel);
        
        HtmlOutputText updatedByValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        updatedByValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCriticityBean.JJCriticity_.updatedBy}", JJContact.class));
        updatedByValue.setConverter(new JJContactConverter());
        htmlPanelGrid.getChildren().add(updatedByValue);
        
        HtmlOutputText enabledLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        enabledLabel.setId("enabledLabel");
        enabledLabel.setValue("Enabled:");
        htmlPanelGrid.getChildren().add(enabledLabel);
        
        HtmlOutputText enabledValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        enabledValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCriticityBean.JJCriticity_.enabled}", String.class));
        htmlPanelGrid.getChildren().add(enabledValue);
        
        HtmlOutputText criticityLevelLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        criticityLevelLabel.setId("criticityLevelLabel");
        criticityLevelLabel.setValue("Criticity Level:");
        htmlPanelGrid.getChildren().add(criticityLevelLabel);
        
        HtmlOutputText criticityLevelValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        criticityLevelValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCriticityBean.JJCriticity_.criticityLevel}", String.class));
        htmlPanelGrid.getChildren().add(criticityLevelValue);
        
        return htmlPanelGrid;
    }
    
    public JJCriticity JJCriticityBean.getJJCriticity_() {
        if (JJCriticity_ == null) {
            JJCriticity_ = new JJCriticity();
        }
        return JJCriticity_;
    }
    
    public void JJCriticityBean.setJJCriticity_(JJCriticity JJCriticity_) {
        this.JJCriticity_ = JJCriticity_;
    }
    
    public List<JJContact> JJCriticityBean.completeCreatedBy(String query) {
        List<JJContact> suggestions = new ArrayList<JJContact>();
        for (JJContact jJContact : jJContactService.findAllJJContacts()) {
            String jJContactStr = String.valueOf(jJContact.getName() +  " "  + jJContact.getDescription() +  " "  + jJContact.getCreationDate() +  " "  + jJContact.getUpdatedDate());
            if (jJContactStr.toLowerCase().startsWith(query.toLowerCase())) {
                suggestions.add(jJContact);
            }
        }
        return suggestions;
    }
    
    public List<JJContact> JJCriticityBean.completeUpdatedBy(String query) {
        List<JJContact> suggestions = new ArrayList<JJContact>();
        for (JJContact jJContact : jJContactService.findAllJJContacts()) {
            String jJContactStr = String.valueOf(jJContact.getName() +  " "  + jJContact.getDescription() +  " "  + jJContact.getCreationDate() +  " "  + jJContact.getUpdatedDate());
            if (jJContactStr.toLowerCase().startsWith(query.toLowerCase())) {
                suggestions.add(jJContact);
            }
        }
        return suggestions;
    }
    
    public String JJCriticityBean.onEdit() {
        return null;
    }
    
    public boolean JJCriticityBean.isCreateDialogVisible() {
        return createDialogVisible;
    }
    
    public void JJCriticityBean.setCreateDialogVisible(boolean createDialogVisible) {
        this.createDialogVisible = createDialogVisible;
    }
    
    public String JJCriticityBean.displayList() {
        createDialogVisible = false;
        findAllJJCriticitys();
        return "JJCriticity_";
    }
    
    public String JJCriticityBean.displayCreateDialog() {
        JJCriticity_ = new JJCriticity();
        createDialogVisible = true;
        return "JJCriticity_";
    }
    
    public String JJCriticityBean.persist() {
        String message = "";
        if (JJCriticity_.getId() != null) {
            jJCriticityService.updateJJCriticity(JJCriticity_);
            message = "message_successfully_updated";
        } else {
            jJCriticityService.saveJJCriticity(JJCriticity_);
            message = "message_successfully_created";
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("createDialogWidget.hide()");
        context.execute("editDialogWidget.hide()");
        
        FacesMessage facesMessage = MessageFactory.getMessage(message, "JJCriticity");
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllJJCriticitys();
    }
    
    public String JJCriticityBean.delete() {
        jJCriticityService.deleteJJCriticity(JJCriticity_);
        FacesMessage facesMessage = MessageFactory.getMessage("message_successfully_deleted", "JJCriticity");
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllJJCriticitys();
    }
    
    public void JJCriticityBean.reset() {
        JJCriticity_ = null;
        createDialogVisible = false;
    }
    
    public void JJCriticityBean.handleDialogClose(CloseEvent event) {
        reset();
    }
    
}

// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.ui.mb;

import com.funder.janjoonweb.domain.JJCategory;
import com.funder.janjoonweb.domain.JJCategoryService;
import com.funder.janjoonweb.domain.JJContact;
import com.funder.janjoonweb.domain.JJContactService;
import com.funder.janjoonweb.ui.mb.JJCategoryBean;
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

privileged aspect JJCategoryBean_Roo_ManagedBean {
    
    declare @type: JJCategoryBean: @ManagedBean(name = "jJCategoryBean");
    
    declare @type: JJCategoryBean: @SessionScoped;
    
    @Autowired
    JJCategoryService JJCategoryBean.jJCategoryService;
    
    @Autowired
    JJContactService JJCategoryBean.jJContactService;
    
    private String JJCategoryBean.name = "JJCategorys";
    
    private JJCategory JJCategoryBean.JJCategory_;
    
    private List<JJCategory> JJCategoryBean.allJJCategorys;
    
    private boolean JJCategoryBean.dataVisible = false;
    
    private List<String> JJCategoryBean.columns;
    
    private HtmlPanelGrid JJCategoryBean.createPanelGrid;
    
    private HtmlPanelGrid JJCategoryBean.editPanelGrid;
    
    private HtmlPanelGrid JJCategoryBean.viewPanelGrid;
    
    private boolean JJCategoryBean.createDialogVisible = false;
    
    @PostConstruct
    public void JJCategoryBean.init() {
        columns = new ArrayList<String>();
        columns.add("name");
        columns.add("description");
        columns.add("creationDate");
        columns.add("updatedDate");
        columns.add("stage");
    }
    
    public String JJCategoryBean.getName() {
        return name;
    }
    
    public List<String> JJCategoryBean.getColumns() {
        return columns;
    }
    
    public List<JJCategory> JJCategoryBean.getAllJJCategorys() {
        return allJJCategorys;
    }
    
    public void JJCategoryBean.setAllJJCategorys(List<JJCategory> allJJCategorys) {
        this.allJJCategorys = allJJCategorys;
    }
    
    public String JJCategoryBean.findAllJJCategorys() {
        allJJCategorys = jJCategoryService.findAllJJCategorys();
        dataVisible = !allJJCategorys.isEmpty();
        return null;
    }
    
    public boolean JJCategoryBean.isDataVisible() {
        return dataVisible;
    }
    
    public void JJCategoryBean.setDataVisible(boolean dataVisible) {
        this.dataVisible = dataVisible;
    }
    
    public HtmlPanelGrid JJCategoryBean.getCreatePanelGrid() {
        if (createPanelGrid == null) {
            createPanelGrid = populateCreatePanel();
        }
        return createPanelGrid;
    }
    
    public void JJCategoryBean.setCreatePanelGrid(HtmlPanelGrid createPanelGrid) {
        this.createPanelGrid = createPanelGrid;
    }
    
    public HtmlPanelGrid JJCategoryBean.getEditPanelGrid() {
        if (editPanelGrid == null) {
            editPanelGrid = populateEditPanel();
        }
        return editPanelGrid;
    }
    
    public void JJCategoryBean.setEditPanelGrid(HtmlPanelGrid editPanelGrid) {
        this.editPanelGrid = editPanelGrid;
    }
    
    public HtmlPanelGrid JJCategoryBean.getViewPanelGrid() {
        return populateViewPanel();
    }
    
    public void JJCategoryBean.setViewPanelGrid(HtmlPanelGrid viewPanelGrid) {
        this.viewPanelGrid = viewPanelGrid;
    }
    
    public HtmlPanelGrid JJCategoryBean.populateCreatePanel() {
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
        nameCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCategoryBean.JJCategory_.name}", String.class));
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
        descriptionCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCategoryBean.JJCategory_.description}", String.class));
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
        creationDateCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCategoryBean.JJCategory_.creationDate}", Date.class));
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
        createdByCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCategoryBean.JJCategory_.createdBy}", JJContact.class));
        createdByCreateInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{jJCategoryBean.completeCreatedBy}", List.class, new Class[] { String.class }));
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
        updatedDateCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCategoryBean.JJCategory_.updatedDate}", Date.class));
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
        updatedByCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCategoryBean.JJCategory_.updatedBy}", JJContact.class));
        updatedByCreateInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{jJCategoryBean.completeUpdatedBy}", List.class, new Class[] { String.class }));
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
        enabledCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCategoryBean.JJCategory_.enabled}", Boolean.class));
        enabledCreateInput.setRequired(false);
        htmlPanelGrid.getChildren().add(enabledCreateInput);
        
        Message enabledCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        enabledCreateInputMessage.setId("enabledCreateInputMessage");
        enabledCreateInputMessage.setFor("enabledCreateInput");
        enabledCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(enabledCreateInputMessage);
        
        HtmlOutputText stageCreateOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        stageCreateOutput.setId("stageCreateOutput");
        stageCreateOutput.setValue("Stage:   ");
        htmlPanelGrid.getChildren().add(stageCreateOutput);
        
        Spinner stageCreateInput = (Spinner) application.createComponent(Spinner.COMPONENT_TYPE);
        stageCreateInput.setId("stageCreateInput");
        stageCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCategoryBean.JJCategory_.stage}", Integer.class));
        stageCreateInput.setRequired(false);
        
        htmlPanelGrid.getChildren().add(stageCreateInput);
        
        Message stageCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        stageCreateInputMessage.setId("stageCreateInputMessage");
        stageCreateInputMessage.setFor("stageCreateInput");
        stageCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(stageCreateInputMessage);
        
        return htmlPanelGrid;
    }
    
    public HtmlPanelGrid JJCategoryBean.populateEditPanel() {
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
        nameEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCategoryBean.JJCategory_.name}", String.class));
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
        descriptionEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCategoryBean.JJCategory_.description}", String.class));
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
        creationDateEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCategoryBean.JJCategory_.creationDate}", Date.class));
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
        createdByEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCategoryBean.JJCategory_.createdBy}", JJContact.class));
        createdByEditInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{jJCategoryBean.completeCreatedBy}", List.class, new Class[] { String.class }));
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
        updatedDateEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCategoryBean.JJCategory_.updatedDate}", Date.class));
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
        updatedByEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCategoryBean.JJCategory_.updatedBy}", JJContact.class));
        updatedByEditInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{jJCategoryBean.completeUpdatedBy}", List.class, new Class[] { String.class }));
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
        enabledEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCategoryBean.JJCategory_.enabled}", Boolean.class));
        enabledEditInput.setRequired(false);
        htmlPanelGrid.getChildren().add(enabledEditInput);
        
        Message enabledEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        enabledEditInputMessage.setId("enabledEditInputMessage");
        enabledEditInputMessage.setFor("enabledEditInput");
        enabledEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(enabledEditInputMessage);
        
        HtmlOutputText stageEditOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        stageEditOutput.setId("stageEditOutput");
        stageEditOutput.setValue("Stage:   ");
        htmlPanelGrid.getChildren().add(stageEditOutput);
        
        Spinner stageEditInput = (Spinner) application.createComponent(Spinner.COMPONENT_TYPE);
        stageEditInput.setId("stageEditInput");
        stageEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCategoryBean.JJCategory_.stage}", Integer.class));
        stageEditInput.setRequired(false);
        
        htmlPanelGrid.getChildren().add(stageEditInput);
        
        Message stageEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        stageEditInputMessage.setId("stageEditInputMessage");
        stageEditInputMessage.setFor("stageEditInput");
        stageEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(stageEditInputMessage);
        
        return htmlPanelGrid;
    }
    
    public HtmlPanelGrid JJCategoryBean.populateViewPanel() {
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
        nameValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCategoryBean.JJCategory_.name}", String.class));
        htmlPanelGrid.getChildren().add(nameValue);
        
        HtmlOutputText descriptionLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        descriptionLabel.setId("descriptionLabel");
        descriptionLabel.setValue("Description:   ");
        htmlPanelGrid.getChildren().add(descriptionLabel);
        
        InputTextarea descriptionValue = (InputTextarea) application.createComponent(InputTextarea.COMPONENT_TYPE);
        descriptionValue.setId("descriptionValue");
        descriptionValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCategoryBean.JJCategory_.description}", String.class));
        descriptionValue.setReadonly(true);
        descriptionValue.setDisabled(true);
        htmlPanelGrid.getChildren().add(descriptionValue);
        
        HtmlOutputText creationDateLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        creationDateLabel.setId("creationDateLabel");
        creationDateLabel.setValue("Creation Date:   ");
        htmlPanelGrid.getChildren().add(creationDateLabel);
        
        HtmlOutputText creationDateValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        creationDateValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCategoryBean.JJCategory_.creationDate}", Date.class));
        DateTimeConverter creationDateValueConverter = (DateTimeConverter) application.createConverter(DateTimeConverter.CONVERTER_ID);
        creationDateValueConverter.setPattern("dd/MM/yyyy");
        creationDateValue.setConverter(creationDateValueConverter);
        htmlPanelGrid.getChildren().add(creationDateValue);
        
        HtmlOutputText createdByLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        createdByLabel.setId("createdByLabel");
        createdByLabel.setValue("Created By:   ");
        htmlPanelGrid.getChildren().add(createdByLabel);
        
        HtmlOutputText createdByValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        createdByValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCategoryBean.JJCategory_.createdBy}", JJContact.class));
        createdByValue.setConverter(new JJContactConverter());
        htmlPanelGrid.getChildren().add(createdByValue);
        
        HtmlOutputText updatedDateLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        updatedDateLabel.setId("updatedDateLabel");
        updatedDateLabel.setValue("Updated Date:   ");
        htmlPanelGrid.getChildren().add(updatedDateLabel);
        
        HtmlOutputText updatedDateValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        updatedDateValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCategoryBean.JJCategory_.updatedDate}", Date.class));
        DateTimeConverter updatedDateValueConverter = (DateTimeConverter) application.createConverter(DateTimeConverter.CONVERTER_ID);
        updatedDateValueConverter.setPattern("dd/MM/yyyy");
        updatedDateValue.setConverter(updatedDateValueConverter);
        htmlPanelGrid.getChildren().add(updatedDateValue);
        
        HtmlOutputText updatedByLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        updatedByLabel.setId("updatedByLabel");
        updatedByLabel.setValue("Updated By:   ");
        htmlPanelGrid.getChildren().add(updatedByLabel);
        
        HtmlOutputText updatedByValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        updatedByValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCategoryBean.JJCategory_.updatedBy}", JJContact.class));
        updatedByValue.setConverter(new JJContactConverter());
        htmlPanelGrid.getChildren().add(updatedByValue);
        
        HtmlOutputText enabledLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        enabledLabel.setId("enabledLabel");
        enabledLabel.setValue("Enabled:   ");
        htmlPanelGrid.getChildren().add(enabledLabel);
        
        HtmlOutputText enabledValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        enabledValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCategoryBean.JJCategory_.enabled}", String.class));
        htmlPanelGrid.getChildren().add(enabledValue);
        
        HtmlOutputText stageLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        stageLabel.setId("stageLabel");
        stageLabel.setValue("Stage:   ");
        htmlPanelGrid.getChildren().add(stageLabel);
        
        HtmlOutputText stageValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        stageValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJCategoryBean.JJCategory_.stage}", String.class));
        htmlPanelGrid.getChildren().add(stageValue);
        
        return htmlPanelGrid;
    }
    
    public JJCategory JJCategoryBean.getJJCategory_() {
        if (JJCategory_ == null) {
            JJCategory_ = new JJCategory();
        }
        return JJCategory_;
    }
    
    public void JJCategoryBean.setJJCategory_(JJCategory JJCategory_) {
        this.JJCategory_ = JJCategory_;
    }
    
    public List<JJContact> JJCategoryBean.completeCreatedBy(String query) {
        List<JJContact> suggestions = new ArrayList<JJContact>();
        for (JJContact jJContact : jJContactService.findAllJJContacts()) {
            String jJContactStr = String.valueOf(jJContact.getName() +  " "  + jJContact.getDescription() +  " "  + jJContact.getCreationDate() +  " "  + jJContact.getUpdatedDate());
            if (jJContactStr.toLowerCase().startsWith(query.toLowerCase())) {
                suggestions.add(jJContact);
            }
        }
        return suggestions;
    }
    
    public List<JJContact> JJCategoryBean.completeUpdatedBy(String query) {
        List<JJContact> suggestions = new ArrayList<JJContact>();
        for (JJContact jJContact : jJContactService.findAllJJContacts()) {
            String jJContactStr = String.valueOf(jJContact.getName() +  " "  + jJContact.getDescription() +  " "  + jJContact.getCreationDate() +  " "  + jJContact.getUpdatedDate());
            if (jJContactStr.toLowerCase().startsWith(query.toLowerCase())) {
                suggestions.add(jJContact);
            }
        }
        return suggestions;
    }
    
    public String JJCategoryBean.onEdit() {
        return null;
    }
    
    public boolean JJCategoryBean.isCreateDialogVisible() {
        return createDialogVisible;
    }
    
    public void JJCategoryBean.setCreateDialogVisible(boolean createDialogVisible) {
        this.createDialogVisible = createDialogVisible;
    }
    
    public String JJCategoryBean.displayList() {
        createDialogVisible = false;
        findAllJJCategorys();
        return "JJCategory_";
    }
    
    public String JJCategoryBean.displayCreateDialog() {
        JJCategory_ = new JJCategory();
        createDialogVisible = true;
        return "JJCategory_";
    }
    
    public String JJCategoryBean.persist() {
        String message = "";
        if (JJCategory_.getId() != null) {
            jJCategoryService.updateJJCategory(JJCategory_);
            message = "Successfully updated";
        } else {
            jJCategoryService.saveJJCategory(JJCategory_);
            message = "Successfully created";
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("createDialog.hide()");
        context.execute("editDialog.hide()");
        
        FacesMessage facesMessage = new FacesMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllJJCategorys();
    }
    
    public String JJCategoryBean.delete() {
        jJCategoryService.deleteJJCategory(JJCategory_);
        FacesMessage facesMessage = new FacesMessage("Successfully deleted");
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllJJCategorys();
    }
    
    public void JJCategoryBean.reset() {
        JJCategory_ = null;
        createDialogVisible = false;
    }
    
    public void JJCategoryBean.handleDialogClose(CloseEvent event) {
        reset();
    }
    
}

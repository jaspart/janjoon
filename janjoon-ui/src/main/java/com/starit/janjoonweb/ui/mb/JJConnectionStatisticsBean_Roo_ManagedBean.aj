// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.ui.mb;

import com.starit.janjoonweb.domain.JJConnectionStatistics;
import com.starit.janjoonweb.domain.JJConnectionStatisticsService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJContactService;
import com.starit.janjoonweb.ui.mb.JJConnectionStatisticsBean;
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
import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.message.Message;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.spinner.Spinner;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;
import org.springframework.beans.factory.annotation.Autowired;

privileged aspect JJConnectionStatisticsBean_Roo_ManagedBean {
    
    declare @type: JJConnectionStatisticsBean: @ManagedBean(name = "jJConnectionStatisticsBean");
    
    declare @type: JJConnectionStatisticsBean: @SessionScoped;
    
    @Autowired
    JJConnectionStatisticsService JJConnectionStatisticsBean.jJConnectionStatisticsService;
    
    @Autowired
    JJContactService JJConnectionStatisticsBean.jJContactService;
    
    private String JJConnectionStatisticsBean.name = "JJConnectionStatisticses";
    
    private JJConnectionStatistics JJConnectionStatisticsBean.JJConnectionStatistics_;
    
    private List<JJConnectionStatistics> JJConnectionStatisticsBean.allJJConnectionStatisticses;
    
    private boolean JJConnectionStatisticsBean.dataVisible = false;
    
    private List<String> JJConnectionStatisticsBean.columns;
    
    private HtmlPanelGrid JJConnectionStatisticsBean.createPanelGrid;
    
    private HtmlPanelGrid JJConnectionStatisticsBean.editPanelGrid;
    
    private HtmlPanelGrid JJConnectionStatisticsBean.viewPanelGrid;
    
    private boolean JJConnectionStatisticsBean.createDialogVisible = false;
    
    @PostConstruct
    public void JJConnectionStatisticsBean.init() {
        columns = new ArrayList<String>();
        columns.add("creationDate");
        columns.add("loginDate");
        columns.add("logoutDate");
        columns.add("duration");
    }
    
    public String JJConnectionStatisticsBean.getName() {
        return name;
    }
    
    public List<String> JJConnectionStatisticsBean.getColumns() {
        return columns;
    }
    
    public List<JJConnectionStatistics> JJConnectionStatisticsBean.getAllJJConnectionStatisticses() {
        return allJJConnectionStatisticses;
    }
    
    public void JJConnectionStatisticsBean.setAllJJConnectionStatisticses(List<JJConnectionStatistics> allJJConnectionStatisticses) {
        this.allJJConnectionStatisticses = allJJConnectionStatisticses;
    }
    
    public String JJConnectionStatisticsBean.findAllJJConnectionStatisticses() {
        allJJConnectionStatisticses = jJConnectionStatisticsService.findAllJJConnectionStatisticses();
        dataVisible = !allJJConnectionStatisticses.isEmpty();
        return null;
    }
    
    public boolean JJConnectionStatisticsBean.isDataVisible() {
        return dataVisible;
    }
    
    public void JJConnectionStatisticsBean.setDataVisible(boolean dataVisible) {
        this.dataVisible = dataVisible;
    }
    
    public HtmlPanelGrid JJConnectionStatisticsBean.getCreatePanelGrid() {
        if (createPanelGrid == null) {
            createPanelGrid = populateCreatePanel();
        }
        return createPanelGrid;
    }
    
    public void JJConnectionStatisticsBean.setCreatePanelGrid(HtmlPanelGrid createPanelGrid) {
        this.createPanelGrid = createPanelGrid;
    }
    
    public HtmlPanelGrid JJConnectionStatisticsBean.getEditPanelGrid() {
        if (editPanelGrid == null) {
            editPanelGrid = populateEditPanel();
        }
        return editPanelGrid;
    }
    
    public void JJConnectionStatisticsBean.setEditPanelGrid(HtmlPanelGrid editPanelGrid) {
        this.editPanelGrid = editPanelGrid;
    }
    
    public HtmlPanelGrid JJConnectionStatisticsBean.getViewPanelGrid() {
        return populateViewPanel();
    }
    
    public void JJConnectionStatisticsBean.setViewPanelGrid(HtmlPanelGrid viewPanelGrid) {
        this.viewPanelGrid = viewPanelGrid;
    }
    
    public HtmlPanelGrid JJConnectionStatisticsBean.populateCreatePanel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        javax.faces.application.Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        
        OutputLabel contactCreateOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        contactCreateOutput.setFor("contactCreateInput");
        contactCreateOutput.setId("contactCreateOutput");
        contactCreateOutput.setValue("Contact:");
        htmlPanelGrid.getChildren().add(contactCreateOutput);
        
        AutoComplete contactCreateInput = (AutoComplete) application.createComponent(AutoComplete.COMPONENT_TYPE);
        contactCreateInput.setId("contactCreateInput");
        contactCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJConnectionStatisticsBean.JJConnectionStatistics_.contact}", JJContact.class));
        contactCreateInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{jJConnectionStatisticsBean.completeContact}", List.class, new Class[] { String.class }));
        contactCreateInput.setDropdown(true);
        contactCreateInput.setValueExpression("var", expressionFactory.createValueExpression(elContext, "contact", String.class));
        contactCreateInput.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext, "#{contact.name} #{contact.description} #{contact.creationDate} #{contact.updatedDate}", String.class));
        contactCreateInput.setValueExpression("itemValue", expressionFactory.createValueExpression(elContext, "#{contact}", JJContact.class));
        contactCreateInput.setConverter(new JJContactConverter());
        contactCreateInput.setRequired(true);
        htmlPanelGrid.getChildren().add(contactCreateInput);
        
        Message contactCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        contactCreateInputMessage.setId("contactCreateInputMessage");
        contactCreateInputMessage.setFor("contactCreateInput");
        contactCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(contactCreateInputMessage);
        
        OutputLabel creationDateCreateOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        creationDateCreateOutput.setFor("creationDateCreateInput");
        creationDateCreateOutput.setId("creationDateCreateOutput");
        creationDateCreateOutput.setValue("Creation Date:");
        htmlPanelGrid.getChildren().add(creationDateCreateOutput);
        
        Calendar creationDateCreateInput = (Calendar) application.createComponent(Calendar.COMPONENT_TYPE);
        creationDateCreateInput.setId("creationDateCreateInput");
        creationDateCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJConnectionStatisticsBean.JJConnectionStatistics_.creationDate}", Date.class));
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
        
        OutputLabel loginDateCreateOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        loginDateCreateOutput.setFor("loginDateCreateInput");
        loginDateCreateOutput.setId("loginDateCreateOutput");
        loginDateCreateOutput.setValue("Login Date:");
        htmlPanelGrid.getChildren().add(loginDateCreateOutput);
        
        Calendar loginDateCreateInput = (Calendar) application.createComponent(Calendar.COMPONENT_TYPE);
        loginDateCreateInput.setId("loginDateCreateInput");
        loginDateCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJConnectionStatisticsBean.JJConnectionStatistics_.loginDate}", Date.class));
        loginDateCreateInput.setNavigator(true);
        loginDateCreateInput.setEffect("slideDown");
        loginDateCreateInput.setPattern("dd/MM/yyyy");
        loginDateCreateInput.setRequired(true);
        htmlPanelGrid.getChildren().add(loginDateCreateInput);
        
        Message loginDateCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        loginDateCreateInputMessage.setId("loginDateCreateInputMessage");
        loginDateCreateInputMessage.setFor("loginDateCreateInput");
        loginDateCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(loginDateCreateInputMessage);
        
        OutputLabel logoutDateCreateOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        logoutDateCreateOutput.setFor("logoutDateCreateInput");
        logoutDateCreateOutput.setId("logoutDateCreateOutput");
        logoutDateCreateOutput.setValue("Logout Date:");
        htmlPanelGrid.getChildren().add(logoutDateCreateOutput);
        
        Calendar logoutDateCreateInput = (Calendar) application.createComponent(Calendar.COMPONENT_TYPE);
        logoutDateCreateInput.setId("logoutDateCreateInput");
        logoutDateCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJConnectionStatisticsBean.JJConnectionStatistics_.logoutDate}", Date.class));
        logoutDateCreateInput.setNavigator(true);
        logoutDateCreateInput.setEffect("slideDown");
        logoutDateCreateInput.setPattern("dd/MM/yyyy");
        logoutDateCreateInput.setRequired(true);
        htmlPanelGrid.getChildren().add(logoutDateCreateInput);
        
        Message logoutDateCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        logoutDateCreateInputMessage.setId("logoutDateCreateInputMessage");
        logoutDateCreateInputMessage.setFor("logoutDateCreateInput");
        logoutDateCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(logoutDateCreateInputMessage);
        
        OutputLabel durationCreateOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        durationCreateOutput.setFor("durationCreateInput");
        durationCreateOutput.setId("durationCreateOutput");
        durationCreateOutput.setValue("Duration:");
        htmlPanelGrid.getChildren().add(durationCreateOutput);
        
        Spinner durationCreateInput = (Spinner) application.createComponent(Spinner.COMPONENT_TYPE);
        durationCreateInput.setId("durationCreateInput");
        durationCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJConnectionStatisticsBean.JJConnectionStatistics_.duration}", Long.class));
        durationCreateInput.setRequired(false);
        
        htmlPanelGrid.getChildren().add(durationCreateInput);
        
        Message durationCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        durationCreateInputMessage.setId("durationCreateInputMessage");
        durationCreateInputMessage.setFor("durationCreateInput");
        durationCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(durationCreateInputMessage);
        
        return htmlPanelGrid;
    }
    
    public HtmlPanelGrid JJConnectionStatisticsBean.populateEditPanel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        javax.faces.application.Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        
        OutputLabel contactEditOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        contactEditOutput.setFor("contactEditInput");
        contactEditOutput.setId("contactEditOutput");
        contactEditOutput.setValue("Contact:");
        htmlPanelGrid.getChildren().add(contactEditOutput);
        
        AutoComplete contactEditInput = (AutoComplete) application.createComponent(AutoComplete.COMPONENT_TYPE);
        contactEditInput.setId("contactEditInput");
        contactEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJConnectionStatisticsBean.JJConnectionStatistics_.contact}", JJContact.class));
        contactEditInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{jJConnectionStatisticsBean.completeContact}", List.class, new Class[] { String.class }));
        contactEditInput.setDropdown(true);
        contactEditInput.setValueExpression("var", expressionFactory.createValueExpression(elContext, "contact", String.class));
        contactEditInput.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext, "#{contact.name} #{contact.description} #{contact.creationDate} #{contact.updatedDate}", String.class));
        contactEditInput.setValueExpression("itemValue", expressionFactory.createValueExpression(elContext, "#{contact}", JJContact.class));
        contactEditInput.setConverter(new JJContactConverter());
        contactEditInput.setRequired(true);
        htmlPanelGrid.getChildren().add(contactEditInput);
        
        Message contactEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        contactEditInputMessage.setId("contactEditInputMessage");
        contactEditInputMessage.setFor("contactEditInput");
        contactEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(contactEditInputMessage);
        
        OutputLabel creationDateEditOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        creationDateEditOutput.setFor("creationDateEditInput");
        creationDateEditOutput.setId("creationDateEditOutput");
        creationDateEditOutput.setValue("Creation Date:");
        htmlPanelGrid.getChildren().add(creationDateEditOutput);
        
        Calendar creationDateEditInput = (Calendar) application.createComponent(Calendar.COMPONENT_TYPE);
        creationDateEditInput.setId("creationDateEditInput");
        creationDateEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJConnectionStatisticsBean.JJConnectionStatistics_.creationDate}", Date.class));
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
        
        OutputLabel loginDateEditOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        loginDateEditOutput.setFor("loginDateEditInput");
        loginDateEditOutput.setId("loginDateEditOutput");
        loginDateEditOutput.setValue("Login Date:");
        htmlPanelGrid.getChildren().add(loginDateEditOutput);
        
        Calendar loginDateEditInput = (Calendar) application.createComponent(Calendar.COMPONENT_TYPE);
        loginDateEditInput.setId("loginDateEditInput");
        loginDateEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJConnectionStatisticsBean.JJConnectionStatistics_.loginDate}", Date.class));
        loginDateEditInput.setNavigator(true);
        loginDateEditInput.setEffect("slideDown");
        loginDateEditInput.setPattern("dd/MM/yyyy");
        loginDateEditInput.setRequired(true);
        htmlPanelGrid.getChildren().add(loginDateEditInput);
        
        Message loginDateEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        loginDateEditInputMessage.setId("loginDateEditInputMessage");
        loginDateEditInputMessage.setFor("loginDateEditInput");
        loginDateEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(loginDateEditInputMessage);
        
        OutputLabel logoutDateEditOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        logoutDateEditOutput.setFor("logoutDateEditInput");
        logoutDateEditOutput.setId("logoutDateEditOutput");
        logoutDateEditOutput.setValue("Logout Date:");
        htmlPanelGrid.getChildren().add(logoutDateEditOutput);
        
        Calendar logoutDateEditInput = (Calendar) application.createComponent(Calendar.COMPONENT_TYPE);
        logoutDateEditInput.setId("logoutDateEditInput");
        logoutDateEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJConnectionStatisticsBean.JJConnectionStatistics_.logoutDate}", Date.class));
        logoutDateEditInput.setNavigator(true);
        logoutDateEditInput.setEffect("slideDown");
        logoutDateEditInput.setPattern("dd/MM/yyyy");
        logoutDateEditInput.setRequired(true);
        htmlPanelGrid.getChildren().add(logoutDateEditInput);
        
        Message logoutDateEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        logoutDateEditInputMessage.setId("logoutDateEditInputMessage");
        logoutDateEditInputMessage.setFor("logoutDateEditInput");
        logoutDateEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(logoutDateEditInputMessage);
        
        OutputLabel durationEditOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        durationEditOutput.setFor("durationEditInput");
        durationEditOutput.setId("durationEditOutput");
        durationEditOutput.setValue("Duration:");
        htmlPanelGrid.getChildren().add(durationEditOutput);
        
        Spinner durationEditInput = (Spinner) application.createComponent(Spinner.COMPONENT_TYPE);
        durationEditInput.setId("durationEditInput");
        durationEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJConnectionStatisticsBean.JJConnectionStatistics_.duration}", Long.class));
        durationEditInput.setRequired(false);
        
        htmlPanelGrid.getChildren().add(durationEditInput);
        
        Message durationEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        durationEditInputMessage.setId("durationEditInputMessage");
        durationEditInputMessage.setFor("durationEditInput");
        durationEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(durationEditInputMessage);
        
        return htmlPanelGrid;
    }
    
    public HtmlPanelGrid JJConnectionStatisticsBean.populateViewPanel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        javax.faces.application.Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        
        HtmlOutputText contactLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        contactLabel.setId("contactLabel");
        contactLabel.setValue("Contact:");
        htmlPanelGrid.getChildren().add(contactLabel);
        
        HtmlOutputText contactValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        contactValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJConnectionStatisticsBean.JJConnectionStatistics_.contact}", JJContact.class));
        contactValue.setConverter(new JJContactConverter());
        htmlPanelGrid.getChildren().add(contactValue);
        
        HtmlOutputText creationDateLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        creationDateLabel.setId("creationDateLabel");
        creationDateLabel.setValue("Creation Date:");
        htmlPanelGrid.getChildren().add(creationDateLabel);
        
        HtmlOutputText creationDateValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        creationDateValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJConnectionStatisticsBean.JJConnectionStatistics_.creationDate}", Date.class));
        DateTimeConverter creationDateValueConverter = (DateTimeConverter) application.createConverter(DateTimeConverter.CONVERTER_ID);
        creationDateValueConverter.setPattern("dd/MM/yyyy");
        creationDateValue.setConverter(creationDateValueConverter);
        htmlPanelGrid.getChildren().add(creationDateValue);
        
        HtmlOutputText loginDateLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        loginDateLabel.setId("loginDateLabel");
        loginDateLabel.setValue("Login Date:");
        htmlPanelGrid.getChildren().add(loginDateLabel);
        
        HtmlOutputText loginDateValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        loginDateValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJConnectionStatisticsBean.JJConnectionStatistics_.loginDate}", Date.class));
        DateTimeConverter loginDateValueConverter = (DateTimeConverter) application.createConverter(DateTimeConverter.CONVERTER_ID);
        loginDateValueConverter.setPattern("dd/MM/yyyy");
        loginDateValue.setConverter(loginDateValueConverter);
        htmlPanelGrid.getChildren().add(loginDateValue);
        
        HtmlOutputText logoutDateLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        logoutDateLabel.setId("logoutDateLabel");
        logoutDateLabel.setValue("Logout Date:");
        htmlPanelGrid.getChildren().add(logoutDateLabel);
        
        HtmlOutputText logoutDateValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        logoutDateValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJConnectionStatisticsBean.JJConnectionStatistics_.logoutDate}", Date.class));
        DateTimeConverter logoutDateValueConverter = (DateTimeConverter) application.createConverter(DateTimeConverter.CONVERTER_ID);
        logoutDateValueConverter.setPattern("dd/MM/yyyy");
        logoutDateValue.setConverter(logoutDateValueConverter);
        htmlPanelGrid.getChildren().add(logoutDateValue);
        
        HtmlOutputText durationLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        durationLabel.setId("durationLabel");
        durationLabel.setValue("Duration:");
        htmlPanelGrid.getChildren().add(durationLabel);
        
        HtmlOutputText durationValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        durationValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJConnectionStatisticsBean.JJConnectionStatistics_.duration}", String.class));
        htmlPanelGrid.getChildren().add(durationValue);
        
        return htmlPanelGrid;
    }
    
    public JJConnectionStatistics JJConnectionStatisticsBean.getJJConnectionStatistics_() {
        if (JJConnectionStatistics_ == null) {
            JJConnectionStatistics_ = new JJConnectionStatistics();
        }
        return JJConnectionStatistics_;
    }
    
    public void JJConnectionStatisticsBean.setJJConnectionStatistics_(JJConnectionStatistics JJConnectionStatistics_) {
        this.JJConnectionStatistics_ = JJConnectionStatistics_;
    }
    
    public List<JJContact> JJConnectionStatisticsBean.completeContact(String query) {
        List<JJContact> suggestions = new ArrayList<JJContact>();
        for (JJContact jJContact : jJContactService.findAllJJContacts()) {
            String jJContactStr = String.valueOf(jJContact.getName() +  " "  + jJContact.getDescription() +  " "  + jJContact.getCreationDate() +  " "  + jJContact.getUpdatedDate());
            if (jJContactStr.toLowerCase().startsWith(query.toLowerCase())) {
                suggestions.add(jJContact);
            }
        }
        return suggestions;
    }
    
    public String JJConnectionStatisticsBean.onEdit() {
        return null;
    }
    
    public boolean JJConnectionStatisticsBean.isCreateDialogVisible() {
        return createDialogVisible;
    }
    
    public void JJConnectionStatisticsBean.setCreateDialogVisible(boolean createDialogVisible) {
        this.createDialogVisible = createDialogVisible;
    }
    
    public String JJConnectionStatisticsBean.displayList() {
        createDialogVisible = false;
        findAllJJConnectionStatisticses();
        return "JJConnectionStatistics_";
    }
    
    public String JJConnectionStatisticsBean.displayCreateDialog() {
        JJConnectionStatistics_ = new JJConnectionStatistics();
        createDialogVisible = true;
        return "JJConnectionStatistics_";
    }
    
    public String JJConnectionStatisticsBean.persist() {
        String message = "";
        if (JJConnectionStatistics_.getId() != null) {
            jJConnectionStatisticsService.updateJJConnectionStatistics(JJConnectionStatistics_);
            message = "message_successfully_updated";
        } else {
            jJConnectionStatisticsService.saveJJConnectionStatistics(JJConnectionStatistics_);
            message = "message_successfully_created";
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("createDialogWidget.hide()");
        context.execute("editDialogWidget.hide()");
        
        FacesMessage facesMessage = MessageFactory.getMessage(message, "JJConnectionStatistics");
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllJJConnectionStatisticses();
    }
    
    public String JJConnectionStatisticsBean.delete() {
        jJConnectionStatisticsService.deleteJJConnectionStatistics(JJConnectionStatistics_);
        FacesMessage facesMessage = MessageFactory.getMessage("message_successfully_deleted", "JJConnectionStatistics");
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllJJConnectionStatisticses();
    }
    
    public void JJConnectionStatisticsBean.reset() {
        JJConnectionStatistics_ = null;
        createDialogVisible = false;
    }
    
    public void JJConnectionStatisticsBean.handleDialogClose(CloseEvent event) {
        reset();
    }
    
}

// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.ui.mb;

import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJContactService;
import com.starit.janjoonweb.domain.JJPermission;
import com.starit.janjoonweb.domain.JJPermissionService;
import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProductService;
import com.starit.janjoonweb.domain.JJProfile;
import com.starit.janjoonweb.domain.JJProfileService;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJProjectService;
import com.starit.janjoonweb.ui.mb.JJPermissionBean;
import com.starit.janjoonweb.ui.mb.converter.JJContactConverter;
import com.starit.janjoonweb.ui.mb.converter.JJProductConverter;
import com.starit.janjoonweb.ui.mb.converter.JJProfileConverter;
import com.starit.janjoonweb.ui.mb.converter.JJProjectConverter;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;
import java.util.ArrayList;
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
import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.component.message.Message;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;
import org.springframework.beans.factory.annotation.Autowired;

privileged aspect JJPermissionBean_Roo_ManagedBean {
    
    declare @type: JJPermissionBean: @ManagedBean(name = "jJPermissionBean");
    
    declare @type: JJPermissionBean: @SessionScoped;
    
    @Autowired
    JJPermissionService JJPermissionBean.jJPermissionService;
    
    @Autowired
    JJProjectService JJPermissionBean.jJProjectService;
    
    @Autowired
    JJProductService JJPermissionBean.jJProductService;
    
    @Autowired
    JJContactService JJPermissionBean.jJContactService;
    
    @Autowired
    JJProfileService JJPermissionBean.jJProfileService;
    
    private String JJPermissionBean.name = "JJPermissions";
    
    private JJPermission JJPermissionBean.JJPermission_;
    
    private List<JJPermission> JJPermissionBean.allJJPermissions;
    
    private boolean JJPermissionBean.dataVisible = false;
    
    private List<String> JJPermissionBean.columns;
    
    private HtmlPanelGrid JJPermissionBean.createPanelGrid;
    
    private HtmlPanelGrid JJPermissionBean.editPanelGrid;
    
    private HtmlPanelGrid JJPermissionBean.viewPanelGrid;
    
    private boolean JJPermissionBean.createDialogVisible = false;
    
    @PostConstruct
    public void JJPermissionBean.init() {
        columns = new ArrayList<String>();
    }
    
    public String JJPermissionBean.getName() {
        return name;
    }
    
    public List<String> JJPermissionBean.getColumns() {
        return columns;
    }
    
    public List<JJPermission> JJPermissionBean.getAllJJPermissions() {
        return allJJPermissions;
    }
    
    public void JJPermissionBean.setAllJJPermissions(List<JJPermission> allJJPermissions) {
        this.allJJPermissions = allJJPermissions;
    }
    
    public String JJPermissionBean.findAllJJPermissions() {
        allJJPermissions = jJPermissionService.findAllJJPermissions();
        dataVisible = !allJJPermissions.isEmpty();
        return null;
    }
    
    public boolean JJPermissionBean.isDataVisible() {
        return dataVisible;
    }
    
    public void JJPermissionBean.setDataVisible(boolean dataVisible) {
        this.dataVisible = dataVisible;
    }
    
    public HtmlPanelGrid JJPermissionBean.getCreatePanelGrid() {
        if (createPanelGrid == null) {
            createPanelGrid = populateCreatePanel();
        }
        return createPanelGrid;
    }
    
    public void JJPermissionBean.setCreatePanelGrid(HtmlPanelGrid createPanelGrid) {
        this.createPanelGrid = createPanelGrid;
    }
    
    public HtmlPanelGrid JJPermissionBean.getEditPanelGrid() {
        if (editPanelGrid == null) {
            editPanelGrid = populateEditPanel();
        }
        return editPanelGrid;
    }
    
    public void JJPermissionBean.setEditPanelGrid(HtmlPanelGrid editPanelGrid) {
        this.editPanelGrid = editPanelGrid;
    }
    
    public HtmlPanelGrid JJPermissionBean.getViewPanelGrid() {
        return populateViewPanel();
    }
    
    public void JJPermissionBean.setViewPanelGrid(HtmlPanelGrid viewPanelGrid) {
        this.viewPanelGrid = viewPanelGrid;
    }
    
    public HtmlPanelGrid JJPermissionBean.populateCreatePanel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        javax.faces.application.Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        
        OutputLabel projectCreateOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        projectCreateOutput.setFor("projectCreateInput");
        projectCreateOutput.setId("projectCreateOutput");
        projectCreateOutput.setValue("Project:");
        htmlPanelGrid.getChildren().add(projectCreateOutput);
        
        AutoComplete projectCreateInput = (AutoComplete) application.createComponent(AutoComplete.COMPONENT_TYPE);
        projectCreateInput.setId("projectCreateInput");
        projectCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJPermissionBean.JJPermission_.project}", JJProject.class));
        projectCreateInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{jJPermissionBean.completeProject}", List.class, new Class[] { String.class }));
        projectCreateInput.setDropdown(true);
        projectCreateInput.setValueExpression("var", expressionFactory.createValueExpression(elContext, "project", String.class));
        projectCreateInput.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext, "#{project.name} #{project.description} #{project.creationDate} #{project.updatedDate}", String.class));
        projectCreateInput.setValueExpression("itemValue", expressionFactory.createValueExpression(elContext, "#{project}", JJProject.class));
        projectCreateInput.setConverter(new JJProjectConverter());
        projectCreateInput.setRequired(false);
        htmlPanelGrid.getChildren().add(projectCreateInput);
        
        Message projectCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        projectCreateInputMessage.setId("projectCreateInputMessage");
        projectCreateInputMessage.setFor("projectCreateInput");
        projectCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(projectCreateInputMessage);
        
        OutputLabel productCreateOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        productCreateOutput.setFor("productCreateInput");
        productCreateOutput.setId("productCreateOutput");
        productCreateOutput.setValue("Product:");
        htmlPanelGrid.getChildren().add(productCreateOutput);
        
        AutoComplete productCreateInput = (AutoComplete) application.createComponent(AutoComplete.COMPONENT_TYPE);
        productCreateInput.setId("productCreateInput");
        productCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJPermissionBean.JJPermission_.product}", JJProduct.class));
        productCreateInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{jJPermissionBean.completeProduct}", List.class, new Class[] { String.class }));
        productCreateInput.setDropdown(true);
        productCreateInput.setValueExpression("var", expressionFactory.createValueExpression(elContext, "product", String.class));
        productCreateInput.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext, "#{product.name} #{product.description} #{product.creationDate} #{product.updatedDate}", String.class));
        productCreateInput.setValueExpression("itemValue", expressionFactory.createValueExpression(elContext, "#{product}", JJProduct.class));
        productCreateInput.setConverter(new JJProductConverter());
        productCreateInput.setRequired(false);
        htmlPanelGrid.getChildren().add(productCreateInput);
        
        Message productCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        productCreateInputMessage.setId("productCreateInputMessage");
        productCreateInputMessage.setFor("productCreateInput");
        productCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(productCreateInputMessage);
        
        OutputLabel contactCreateOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        contactCreateOutput.setFor("contactCreateInput");
        contactCreateOutput.setId("contactCreateOutput");
        contactCreateOutput.setValue("Contact:");
        htmlPanelGrid.getChildren().add(contactCreateOutput);
        
        AutoComplete contactCreateInput = (AutoComplete) application.createComponent(AutoComplete.COMPONENT_TYPE);
        contactCreateInput.setId("contactCreateInput");
        contactCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJPermissionBean.JJPermission_.contact}", JJContact.class));
        contactCreateInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{jJPermissionBean.completeContact}", List.class, new Class[] { String.class }));
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
        
        OutputLabel profileCreateOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        profileCreateOutput.setFor("profileCreateInput");
        profileCreateOutput.setId("profileCreateOutput");
        profileCreateOutput.setValue("Profile:");
        htmlPanelGrid.getChildren().add(profileCreateOutput);
        
        AutoComplete profileCreateInput = (AutoComplete) application.createComponent(AutoComplete.COMPONENT_TYPE);
        profileCreateInput.setId("profileCreateInput");
        profileCreateInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJPermissionBean.JJPermission_.profile}", JJProfile.class));
        profileCreateInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{jJPermissionBean.completeProfile}", List.class, new Class[] { String.class }));
        profileCreateInput.setDropdown(true);
        profileCreateInput.setValueExpression("var", expressionFactory.createValueExpression(elContext, "profile", String.class));
        profileCreateInput.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext, "#{profile.name}", String.class));
        profileCreateInput.setValueExpression("itemValue", expressionFactory.createValueExpression(elContext, "#{profile}", JJProfile.class));
        profileCreateInput.setConverter(new JJProfileConverter());
        profileCreateInput.setRequired(true);
        htmlPanelGrid.getChildren().add(profileCreateInput);
        
        Message profileCreateInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        profileCreateInputMessage.setId("profileCreateInputMessage");
        profileCreateInputMessage.setFor("profileCreateInput");
        profileCreateInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(profileCreateInputMessage);
        
        return htmlPanelGrid;
    }
    
    public HtmlPanelGrid JJPermissionBean.populateEditPanel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        javax.faces.application.Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        
        OutputLabel projectEditOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        projectEditOutput.setFor("projectEditInput");
        projectEditOutput.setId("projectEditOutput");
        projectEditOutput.setValue("Project:");
        htmlPanelGrid.getChildren().add(projectEditOutput);
        
        AutoComplete projectEditInput = (AutoComplete) application.createComponent(AutoComplete.COMPONENT_TYPE);
        projectEditInput.setId("projectEditInput");
        projectEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJPermissionBean.JJPermission_.project}", JJProject.class));
        projectEditInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{jJPermissionBean.completeProject}", List.class, new Class[] { String.class }));
        projectEditInput.setDropdown(true);
        projectEditInput.setValueExpression("var", expressionFactory.createValueExpression(elContext, "project", String.class));
        projectEditInput.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext, "#{project.name} #{project.description} #{project.creationDate} #{project.updatedDate}", String.class));
        projectEditInput.setValueExpression("itemValue", expressionFactory.createValueExpression(elContext, "#{project}", JJProject.class));
        projectEditInput.setConverter(new JJProjectConverter());
        projectEditInput.setRequired(false);
        htmlPanelGrid.getChildren().add(projectEditInput);
        
        Message projectEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        projectEditInputMessage.setId("projectEditInputMessage");
        projectEditInputMessage.setFor("projectEditInput");
        projectEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(projectEditInputMessage);
        
        OutputLabel productEditOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        productEditOutput.setFor("productEditInput");
        productEditOutput.setId("productEditOutput");
        productEditOutput.setValue("Product:");
        htmlPanelGrid.getChildren().add(productEditOutput);
        
        AutoComplete productEditInput = (AutoComplete) application.createComponent(AutoComplete.COMPONENT_TYPE);
        productEditInput.setId("productEditInput");
        productEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJPermissionBean.JJPermission_.product}", JJProduct.class));
        productEditInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{jJPermissionBean.completeProduct}", List.class, new Class[] { String.class }));
        productEditInput.setDropdown(true);
        productEditInput.setValueExpression("var", expressionFactory.createValueExpression(elContext, "product", String.class));
        productEditInput.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext, "#{product.name} #{product.description} #{product.creationDate} #{product.updatedDate}", String.class));
        productEditInput.setValueExpression("itemValue", expressionFactory.createValueExpression(elContext, "#{product}", JJProduct.class));
        productEditInput.setConverter(new JJProductConverter());
        productEditInput.setRequired(false);
        htmlPanelGrid.getChildren().add(productEditInput);
        
        Message productEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        productEditInputMessage.setId("productEditInputMessage");
        productEditInputMessage.setFor("productEditInput");
        productEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(productEditInputMessage);
        
        OutputLabel contactEditOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        contactEditOutput.setFor("contactEditInput");
        contactEditOutput.setId("contactEditOutput");
        contactEditOutput.setValue("Contact:");
        htmlPanelGrid.getChildren().add(contactEditOutput);
        
        AutoComplete contactEditInput = (AutoComplete) application.createComponent(AutoComplete.COMPONENT_TYPE);
        contactEditInput.setId("contactEditInput");
        contactEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJPermissionBean.JJPermission_.contact}", JJContact.class));
        contactEditInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{jJPermissionBean.completeContact}", List.class, new Class[] { String.class }));
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
        
        OutputLabel profileEditOutput = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);
        profileEditOutput.setFor("profileEditInput");
        profileEditOutput.setId("profileEditOutput");
        profileEditOutput.setValue("Profile:");
        htmlPanelGrid.getChildren().add(profileEditOutput);
        
        AutoComplete profileEditInput = (AutoComplete) application.createComponent(AutoComplete.COMPONENT_TYPE);
        profileEditInput.setId("profileEditInput");
        profileEditInput.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJPermissionBean.JJPermission_.profile}", JJProfile.class));
        profileEditInput.setCompleteMethod(expressionFactory.createMethodExpression(elContext, "#{jJPermissionBean.completeProfile}", List.class, new Class[] { String.class }));
        profileEditInput.setDropdown(true);
        profileEditInput.setValueExpression("var", expressionFactory.createValueExpression(elContext, "profile", String.class));
        profileEditInput.setValueExpression("itemLabel", expressionFactory.createValueExpression(elContext, "#{profile.name}", String.class));
        profileEditInput.setValueExpression("itemValue", expressionFactory.createValueExpression(elContext, "#{profile}", JJProfile.class));
        profileEditInput.setConverter(new JJProfileConverter());
        profileEditInput.setRequired(true);
        htmlPanelGrid.getChildren().add(profileEditInput);
        
        Message profileEditInputMessage = (Message) application.createComponent(Message.COMPONENT_TYPE);
        profileEditInputMessage.setId("profileEditInputMessage");
        profileEditInputMessage.setFor("profileEditInput");
        profileEditInputMessage.setDisplay("icon");
        htmlPanelGrid.getChildren().add(profileEditInputMessage);
        
        return htmlPanelGrid;
    }
    
    public HtmlPanelGrid JJPermissionBean.populateViewPanel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        javax.faces.application.Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
        HtmlPanelGrid htmlPanelGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        
        HtmlOutputText projectLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        projectLabel.setId("projectLabel");
        projectLabel.setValue("Project:");
        htmlPanelGrid.getChildren().add(projectLabel);
        
        HtmlOutputText projectValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        projectValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJPermissionBean.JJPermission_.project}", JJProject.class));
        projectValue.setConverter(new JJProjectConverter());
        htmlPanelGrid.getChildren().add(projectValue);
        
        HtmlOutputText productLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        productLabel.setId("productLabel");
        productLabel.setValue("Product:");
        htmlPanelGrid.getChildren().add(productLabel);
        
        HtmlOutputText productValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        productValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJPermissionBean.JJPermission_.product}", JJProduct.class));
        productValue.setConverter(new JJProductConverter());
        htmlPanelGrid.getChildren().add(productValue);
        
        HtmlOutputText contactLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        contactLabel.setId("contactLabel");
        contactLabel.setValue("Contact:");
        htmlPanelGrid.getChildren().add(contactLabel);
        
        HtmlOutputText contactValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        contactValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJPermissionBean.JJPermission_.contact}", JJContact.class));
        contactValue.setConverter(new JJContactConverter());
        htmlPanelGrid.getChildren().add(contactValue);
        
        HtmlOutputText profileLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        profileLabel.setId("profileLabel");
        profileLabel.setValue("Profile:");
        htmlPanelGrid.getChildren().add(profileLabel);
        
        HtmlOutputText profileValue = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        profileValue.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{jJPermissionBean.JJPermission_.profile}", JJProfile.class));
        profileValue.setConverter(new JJProfileConverter());
        htmlPanelGrid.getChildren().add(profileValue);
        
        return htmlPanelGrid;
    }
    
    public JJPermission JJPermissionBean.getJJPermission_() {
        if (JJPermission_ == null) {
            JJPermission_ = new JJPermission();
        }
        return JJPermission_;
    }
    
    public void JJPermissionBean.setJJPermission_(JJPermission JJPermission_) {
        this.JJPermission_ = JJPermission_;
    }
    
    public List<JJProject> JJPermissionBean.completeProject(String query) {
        List<JJProject> suggestions = new ArrayList<JJProject>();
        for (JJProject jJProject : jJProjectService.findAllJJProjects()) {
            String jJProjectStr = String.valueOf(jJProject.getName() +  " "  + jJProject.getDescription() +  " "  + jJProject.getCreationDate() +  " "  + jJProject.getUpdatedDate());
            if (jJProjectStr.toLowerCase().startsWith(query.toLowerCase())) {
                suggestions.add(jJProject);
            }
        }
        return suggestions;
    }
    
    public List<JJProduct> JJPermissionBean.completeProduct(String query) {
        List<JJProduct> suggestions = new ArrayList<JJProduct>();
        for (JJProduct jJProduct : jJProductService.findAllJJProducts()) {
            String jJProductStr = String.valueOf(jJProduct.getName() +  " "  + jJProduct.getDescription() +  " "  + jJProduct.getCreationDate() +  " "  + jJProduct.getUpdatedDate());
            if (jJProductStr.toLowerCase().startsWith(query.toLowerCase())) {
                suggestions.add(jJProduct);
            }
        }
        return suggestions;
    }
    
    public List<JJContact> JJPermissionBean.completeContact(String query) {
        List<JJContact> suggestions = new ArrayList<JJContact>();
        for (JJContact jJContact : jJContactService.findAllJJContacts()) {
            String jJContactStr = String.valueOf(jJContact.getName() +  " "  + jJContact.getDescription() +  " "  + jJContact.getCreationDate() +  " "  + jJContact.getUpdatedDate());
            if (jJContactStr.toLowerCase().startsWith(query.toLowerCase())) {
                suggestions.add(jJContact);
            }
        }
        return suggestions;
    }
    
    public List<JJProfile> JJPermissionBean.completeProfile(String query) {
        List<JJProfile> suggestions = new ArrayList<JJProfile>();
        for (JJProfile jJProfile : jJProfileService.findAllJJProfiles()) {
            String jJProfileStr = String.valueOf(jJProfile.getName());
            if (jJProfileStr.toLowerCase().startsWith(query.toLowerCase())) {
                suggestions.add(jJProfile);
            }
        }
        return suggestions;
    }
    
    public String JJPermissionBean.onEdit() {
        return null;
    }
    
    public boolean JJPermissionBean.isCreateDialogVisible() {
        return createDialogVisible;
    }
    
    public void JJPermissionBean.setCreateDialogVisible(boolean createDialogVisible) {
        this.createDialogVisible = createDialogVisible;
    }
    
    public String JJPermissionBean.displayList() {
        createDialogVisible = false;
        findAllJJPermissions();
        return "JJPermission_";
    }
    
    public String JJPermissionBean.displayCreateDialog() {
        JJPermission_ = new JJPermission();
        createDialogVisible = true;
        return "JJPermission_";
    }
    
    public String JJPermissionBean.persist() {
        String message = "";
        if (JJPermission_.getId() != null) {
            jJPermissionService.updateJJPermission(JJPermission_);
            message = "message_successfully_updated";
        } else {
            jJPermissionService.saveJJPermission(JJPermission_);
            message = "message_successfully_created";
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("createDialogWidget.hide()");
        context.execute("editDialogWidget.hide()");
        
        FacesMessage facesMessage = MessageFactory.getMessage(message, "JJPermission");
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllJJPermissions();
    }
    
    public String JJPermissionBean.delete() {
        jJPermissionService.deleteJJPermission(JJPermission_);
        FacesMessage facesMessage = MessageFactory.getMessage("message_successfully_deleted", "JJPermission");
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllJJPermissions();
    }
    
    public void JJPermissionBean.reset() {
        JJPermission_ = null;
        createDialogVisible = false;
    }
    
    public void JJPermissionBean.handleDialogClose(CloseEvent event) {
        reset();
    }
    
}
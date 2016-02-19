// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.ui.mb;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJContactService;
import com.starit.janjoonweb.domain.JJTestcase;
import com.starit.janjoonweb.domain.JJTestcaseService;
import com.starit.janjoonweb.domain.JJTeststep;
import com.starit.janjoonweb.domain.JJTeststepService;
import com.starit.janjoonweb.ui.mb.JJTeststepBean;
import com.starit.janjoonweb.ui.mb.util.MessageFactory;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;
import org.springframework.beans.factory.annotation.Autowired;

privileged aspect JJTeststepBean_Roo_ManagedBean {
    
    declare @type: JJTeststepBean: @ManagedBean(name = "jJTeststepBean");
    
    declare @type: JJTeststepBean: @SessionScoped;
    
    @Autowired
    JJTeststepService JJTeststepBean.jJTeststepService;
    
    @Autowired
    JJContactService JJTeststepBean.jJContactService;
    
    @Autowired
    JJTestcaseService JJTeststepBean.jJTestcaseService;
    
    private String JJTeststepBean.name = "JJTeststeps";
    
    private JJTeststep JJTeststepBean.JJTeststep_;
    
    private List<JJTeststep> JJTeststepBean.allJJTeststeps;
    
    private boolean JJTeststepBean.dataVisible = false;
    
    private List<String> JJTeststepBean.columns;
    
    private HtmlPanelGrid JJTeststepBean.createPanelGrid;
    
    private HtmlPanelGrid JJTeststepBean.editPanelGrid;
    
    private HtmlPanelGrid JJTeststepBean.viewPanelGrid;
    
    private boolean JJTeststepBean.createDialogVisible = false;
    
    private List<JJBug> JJTeststepBean.selectedBugs;
    
    @PostConstruct
    public void JJTeststepBean.init() {
        columns = new ArrayList<String>();
        columns.add("name");
        columns.add("description");
        columns.add("creationDate");
        columns.add("updatedDate");
        columns.add("ordering");
    }
    
    public String JJTeststepBean.getName() {
        return name;
    }
    
    public List<String> JJTeststepBean.getColumns() {
        return columns;
    }
    
    public List<JJTeststep> JJTeststepBean.getAllJJTeststeps() {
        return allJJTeststeps;
    }
    
    public void JJTeststepBean.setAllJJTeststeps(List<JJTeststep> allJJTeststeps) {
        this.allJJTeststeps = allJJTeststeps;
    }
    
    public String JJTeststepBean.findAllJJTeststeps() {
        allJJTeststeps = jJTeststepService.findAllJJTeststeps();
        dataVisible = !allJJTeststeps.isEmpty();
        return null;
    }
    
    public boolean JJTeststepBean.isDataVisible() {
        return dataVisible;
    }
    
    public void JJTeststepBean.setDataVisible(boolean dataVisible) {
        this.dataVisible = dataVisible;
    }
    
    public HtmlPanelGrid JJTeststepBean.getCreatePanelGrid() {
        if (createPanelGrid == null) {
            createPanelGrid = populateCreatePanel();
        }
        return createPanelGrid;
    }
    
    public void JJTeststepBean.setCreatePanelGrid(HtmlPanelGrid createPanelGrid) {
        this.createPanelGrid = createPanelGrid;
    }
    
    public HtmlPanelGrid JJTeststepBean.getEditPanelGrid() {
        if (editPanelGrid == null) {
            editPanelGrid = populateEditPanel();
        }
        return editPanelGrid;
    }
    
    public void JJTeststepBean.setEditPanelGrid(HtmlPanelGrid editPanelGrid) {
        this.editPanelGrid = editPanelGrid;
    }
    
    public HtmlPanelGrid JJTeststepBean.getViewPanelGrid() {
        return populateViewPanel();
    }
    
    public void JJTeststepBean.setViewPanelGrid(HtmlPanelGrid viewPanelGrid) {
        this.viewPanelGrid = viewPanelGrid;
    }
    
    public JJTeststep JJTeststepBean.getJJTeststep_() {
        if (JJTeststep_ == null) {
            JJTeststep_ = new JJTeststep();
        }
        return JJTeststep_;
    }
    
    public void JJTeststepBean.setJJTeststep_(JJTeststep JJTeststep_) {
        this.JJTeststep_ = JJTeststep_;
    }
    
    public List<JJContact> JJTeststepBean.completeCreatedBy(String query) {
        List<JJContact> suggestions = new ArrayList<JJContact>();
        for (JJContact jJContact : jJContactService.findAllJJContacts()) {
            String jJContactStr = String.valueOf(jJContact.getName() +  " "  + jJContact.getDescription() +  " "  + jJContact.getCreationDate() +  " "  + jJContact.getUpdatedDate());
            if (jJContactStr.toLowerCase().startsWith(query.toLowerCase())) {
                suggestions.add(jJContact);
            }
        }
        return suggestions;
    }
    
    public List<JJContact> JJTeststepBean.completeUpdatedBy(String query) {
        List<JJContact> suggestions = new ArrayList<JJContact>();
        for (JJContact jJContact : jJContactService.findAllJJContacts()) {
            String jJContactStr = String.valueOf(jJContact.getName() +  " "  + jJContact.getDescription() +  " "  + jJContact.getCreationDate() +  " "  + jJContact.getUpdatedDate());
            if (jJContactStr.toLowerCase().startsWith(query.toLowerCase())) {
                suggestions.add(jJContact);
            }
        }
        return suggestions;
    }
    
    public List<JJTestcase> JJTeststepBean.completeTestcase(String query) {
        List<JJTestcase> suggestions = new ArrayList<JJTestcase>();
        for (JJTestcase jJTestcase : jJTestcaseService.findAllJJTestcases()) {
            String jJTestcaseStr = String.valueOf(jJTestcase.getName() +  " "  + jJTestcase.getDescription() +  " "  + jJTestcase.getCreationDate() +  " "  + jJTestcase.getUpdatedDate());
            if (jJTestcaseStr.toLowerCase().startsWith(query.toLowerCase())) {
                suggestions.add(jJTestcase);
            }
        }
        return suggestions;
    }
    
    public List<JJBug> JJTeststepBean.getSelectedBugs() {
        return selectedBugs;
    }
    
    public void JJTeststepBean.setSelectedBugs(List<JJBug> selectedBugs) {
        if (selectedBugs != null) {
            JJTeststep_.setBugs(new HashSet<JJBug>(selectedBugs));
        }
        this.selectedBugs = selectedBugs;
    }
    
    public String JJTeststepBean.onEdit() {
        if (JJTeststep_ != null && JJTeststep_.getBugs() != null) {
            selectedBugs = new ArrayList<JJBug>(JJTeststep_.getBugs());
        }
        return null;
    }
    
    public boolean JJTeststepBean.isCreateDialogVisible() {
        return createDialogVisible;
    }
    
    public void JJTeststepBean.setCreateDialogVisible(boolean createDialogVisible) {
        this.createDialogVisible = createDialogVisible;
    }
    
    public String JJTeststepBean.displayList() {
        createDialogVisible = false;
        findAllJJTeststeps();
        return "JJTeststep_";
    }
    
    public String JJTeststepBean.displayCreateDialog() {
        JJTeststep_ = new JJTeststep();
        createDialogVisible = true;
        return "JJTeststep_";
    }
    
    public String JJTeststepBean.persist() {
        String message = "";
        if (JJTeststep_.getId() != null) {
            jJTeststepService.updateJJTeststep(JJTeststep_);
            message = "message_successfully_updated";
        } else {
            jJTeststepService.saveJJTeststep(JJTeststep_);
            message = "message_successfully_created";
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("createDialogWidget.hide()");
        context.execute("editDialogWidget.hide()");
        
        FacesMessage facesMessage = MessageFactory.getMessage(message, "JJTeststep");
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllJJTeststeps();
    }
    
    public String JJTeststepBean.delete() {
        jJTeststepService.deleteJJTeststep(JJTeststep_);
        FacesMessage facesMessage = MessageFactory.getMessage("message_successfully_deleted", "JJTeststep");
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllJJTeststeps();
    }
    
    public void JJTeststepBean.reset() {
        JJTeststep_ = null;
        selectedBugs = null;
        createDialogVisible = false;
    }
    
    public void JJTeststepBean.handleDialogClose(CloseEvent event) {
        reset();
    }
    
}

// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.ui.mb;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJBugService;
import com.starit.janjoonweb.domain.JJBuild;
import com.starit.janjoonweb.domain.JJBuildService;
import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJCategoryService;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJContactService;
import com.starit.janjoonweb.domain.JJCriticity;
import com.starit.janjoonweb.domain.JJCriticityService;
import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJRequirementService;
import com.starit.janjoonweb.domain.JJSprint;
import com.starit.janjoonweb.domain.JJSprintService;
import com.starit.janjoonweb.domain.JJStatus;
import com.starit.janjoonweb.domain.JJStatusService;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJTeststep;
import com.starit.janjoonweb.domain.JJTeststepService;
import com.starit.janjoonweb.domain.JJVersion;
import com.starit.janjoonweb.domain.JJVersionService;
import com.starit.janjoonweb.domain.reference.JJRelationship;
import com.starit.janjoonweb.ui.mb.JJBugBean;
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

privileged aspect JJBugBean_Roo_ManagedBean {
    
    declare @type: JJBugBean: @ManagedBean(name = "jJBugBean");
    
    declare @type: JJBugBean: @SessionScoped;
    
    @Autowired
    JJBugService JJBugBean.jJBugService;
    
    @Autowired
    JJContactService JJBugBean.jJContactService;
    
    @Autowired
    JJVersionService JJBugBean.jJVersionService;
    
    @Autowired
    JJCategoryService JJBugBean.jJCategoryService;
    
    @Autowired
    JJCriticityService JJBugBean.jJCriticityService;
    
    @Autowired
    JJStatusService JJBugBean.jJStatusService;
    
    @Autowired
    JJRequirementService JJBugBean.jJRequirementService;
    
    @Autowired
    JJTeststepService JJBugBean.jJTeststepService;
    
    @Autowired
    JJSprintService JJBugBean.jJSprintService;
    
    @Autowired
    JJBuildService JJBugBean.jJBuildService;
    
    private String JJBugBean.name = "JJBugs";
    
    private List<JJBug> JJBugBean.allJJBugs;
    
    private boolean JJBugBean.dataVisible = false;
    
    private List<String> JJBugBean.columns;
    
    private HtmlPanelGrid JJBugBean.createPanelGrid;
    
    private HtmlPanelGrid JJBugBean.editPanelGrid;
    
    private HtmlPanelGrid JJBugBean.viewPanelGrid;
    
    private boolean JJBugBean.createDialogVisible = false;
    
    private List<JJBug> JJBugBean.selectedBugs;
    
    private List<JJTask> JJBugBean.selectedTasks;
    
    private List<JJContact> JJBugBean.selectedContacts;
    
    @PostConstruct
    public void JJBugBean.init() {
        columns = new ArrayList<String>();
        columns.add("name");
        columns.add("description");
        columns.add("creationDate");
        columns.add("updatedDate");
    }
    
    public String JJBugBean.getName() {
        return name;
    }
    
    public List<String> JJBugBean.getColumns() {
        return columns;
    }
    
    public List<JJBug> JJBugBean.getAllJJBugs() {
        return allJJBugs;
    }
    
    public void JJBugBean.setAllJJBugs(List<JJBug> allJJBugs) {
        this.allJJBugs = allJJBugs;
    }
    
    public String JJBugBean.findAllJJBugs() {
        allJJBugs = jJBugService.findAllJJBugs();
        dataVisible = !allJJBugs.isEmpty();
        return null;
    }
    
    public boolean JJBugBean.isDataVisible() {
        return dataVisible;
    }
    
    public void JJBugBean.setDataVisible(boolean dataVisible) {
        this.dataVisible = dataVisible;
    }
    
    public HtmlPanelGrid JJBugBean.getCreatePanelGrid() {
        if (createPanelGrid == null) {
            createPanelGrid = populateCreatePanel();
        }
        return createPanelGrid;
    }
    
    public void JJBugBean.setCreatePanelGrid(HtmlPanelGrid createPanelGrid) {
        this.createPanelGrid = createPanelGrid;
    }
    
    public HtmlPanelGrid JJBugBean.getEditPanelGrid() {
        if (editPanelGrid == null) {
            editPanelGrid = populateEditPanel();
        }
        return editPanelGrid;
    }
    
    public void JJBugBean.setEditPanelGrid(HtmlPanelGrid editPanelGrid) {
        this.editPanelGrid = editPanelGrid;
    }
    
    public HtmlPanelGrid JJBugBean.getViewPanelGrid() {
        return populateViewPanel();
    }
    
    public void JJBugBean.setViewPanelGrid(HtmlPanelGrid viewPanelGrid) {
        this.viewPanelGrid = viewPanelGrid;
    }
    
    public List<JJContact> JJBugBean.completeCreatedBy(String query) {
        List<JJContact> suggestions = new ArrayList<JJContact>();
        for (JJContact jJContact : jJContactService.findAllJJContacts()) {
            String jJContactStr = String.valueOf(jJContact.getName() +  " "  + jJContact.getDescription() +  " "  + jJContact.getCreationDate() +  " "  + jJContact.getUpdatedDate());
            if (jJContactStr.toLowerCase().startsWith(query.toLowerCase())) {
                suggestions.add(jJContact);
            }
        }
        return suggestions;
    }
    
    public List<JJContact> JJBugBean.completeUpdatedBy(String query) {
        List<JJContact> suggestions = new ArrayList<JJContact>();
        for (JJContact jJContact : jJContactService.findAllJJContacts()) {
            String jJContactStr = String.valueOf(jJContact.getName() +  " "  + jJContact.getDescription() +  " "  + jJContact.getCreationDate() +  " "  + jJContact.getUpdatedDate());
            if (jJContactStr.toLowerCase().startsWith(query.toLowerCase())) {
                suggestions.add(jJContact);
            }
        }
        return suggestions;
    }
    
    public List<JJVersion> JJBugBean.completeVersioning(String query) {
        List<JJVersion> suggestions = new ArrayList<JJVersion>();
        for (JJVersion jJVersion : jJVersionService.findAllJJVersions()) {
            String jJVersionStr = String.valueOf(jJVersion.getName() +  " "  + jJVersion.getDescription() +  " "  + jJVersion.getCreationDate() +  " "  + jJVersion.getUpdatedDate());
            if (jJVersionStr.toLowerCase().startsWith(query.toLowerCase())) {
                suggestions.add(jJVersion);
            }
        }
        return suggestions;
    }
    
    public List<JJCategory> JJBugBean.completeCategory(String query) {
        List<JJCategory> suggestions = new ArrayList<JJCategory>();
        for (JJCategory jJCategory : jJCategoryService.findAllJJCategorys()) {
            String jJCategoryStr = String.valueOf(jJCategory.getName() +  " "  + jJCategory.getDescription() +  " "  + jJCategory.getCreationDate() +  " "  + jJCategory.getUpdatedDate());
            if (jJCategoryStr.toLowerCase().startsWith(query.toLowerCase())) {
                suggestions.add(jJCategory);
            }
        }
        return suggestions;
    }
    
    public List<JJCriticity> JJBugBean.completeCriticity(String query) {
        List<JJCriticity> suggestions = new ArrayList<JJCriticity>();
        for (JJCriticity jJCriticity : jJCriticityService.findAllJJCriticitys()) {
            String jJCriticityStr = String.valueOf(jJCriticity.getName() +  " "  + jJCriticity.getDescription() +  " "  + jJCriticity.getCreationDate() +  " "  + jJCriticity.getUpdatedDate());
            if (jJCriticityStr.toLowerCase().startsWith(query.toLowerCase())) {
                suggestions.add(jJCriticity);
            }
        }
        return suggestions;
    }
    
    public List<JJStatus> JJBugBean.completeStatus(String query) {
        List<JJStatus> suggestions = new ArrayList<JJStatus>();
        for (JJStatus jJStatus : jJStatusService.findAllJJStatuses()) {
            String jJStatusStr = String.valueOf(jJStatus.getName() +  " "  + jJStatus.getDescription() +  " "  + jJStatus.getCreationDate() +  " "  + jJStatus.getUpdatedDate());
            if (jJStatusStr.toLowerCase().startsWith(query.toLowerCase())) {
                suggestions.add(jJStatus);
            }
        }
        return suggestions;
    }
    
    public List<JJRequirement> JJBugBean.completeRequirement(String query) {
        List<JJRequirement> suggestions = new ArrayList<JJRequirement>();
        for (JJRequirement jJRequirement : jJRequirementService.findAllJJRequirements()) {
            String jJRequirementStr = String.valueOf(jJRequirement.getName() +  " "  + jJRequirement.getDescription() +  " "  + jJRequirement.getCreationDate() +  " "  + jJRequirement.getUpdatedDate());
            if (jJRequirementStr.toLowerCase().startsWith(query.toLowerCase())) {
                suggestions.add(jJRequirement);
            }
        }
        return suggestions;
    }
    
    public List<JJTeststep> JJBugBean.completeTeststep(String query) {
        List<JJTeststep> suggestions = new ArrayList<JJTeststep>();
        for (JJTeststep jJTeststep : jJTeststepService.findAllJJTeststeps()) {
            String jJTeststepStr = String.valueOf(jJTeststep.getName() +  " "  + jJTeststep.getDescription() +  " "  + jJTeststep.getCreationDate() +  " "  + jJTeststep.getUpdatedDate());
            if (jJTeststepStr.toLowerCase().startsWith(query.toLowerCase())) {
                suggestions.add(jJTeststep);
            }
        }
        return suggestions;
    }
    
    public List<JJRelationship> JJBugBean.completeRelation(String query) {
        List<JJRelationship> suggestions = new ArrayList<JJRelationship>();
        for (JJRelationship jJRelationship : JJRelationship.values()) {
            if (jJRelationship.name().toLowerCase().startsWith(query.toLowerCase())) {
                suggestions.add(jJRelationship);
            }
        }
        return suggestions;
    }
    
    public List<JJSprint> JJBugBean.completeSprint(String query) {
        List<JJSprint> suggestions = new ArrayList<JJSprint>();
        for (JJSprint jJSprint : jJSprintService.findAllJJSprints()) {
            String jJSprintStr = String.valueOf(jJSprint.getName() +  " "  + jJSprint.getDescription() +  " "  + jJSprint.getCreationDate() +  " "  + jJSprint.getUpdatedDate());
            if (jJSprintStr.toLowerCase().startsWith(query.toLowerCase())) {
                suggestions.add(jJSprint);
            }
        }
        return suggestions;
    }
    
    public List<JJBuild> JJBugBean.completeBuild(String query) {
        List<JJBuild> suggestions = new ArrayList<JJBuild>();
        for (JJBuild jJBuild : jJBuildService.findAllJJBuilds()) {
            String jJBuildStr = String.valueOf(jJBuild.getName() +  " "  + jJBuild.getDescription() +  " "  + jJBuild.getCreationDate() +  " "  + jJBuild.getUpdatedDate());
            if (jJBuildStr.toLowerCase().startsWith(query.toLowerCase())) {
                suggestions.add(jJBuild);
            }
        }
        return suggestions;
    }
    
    public List<JJBug> JJBugBean.getSelectedBugs() {
        return selectedBugs;
    }
    
    public void JJBugBean.setSelectedBugs(List<JJBug> selectedBugs) {
        if (selectedBugs != null) {
            JJBug_.setBugs(new HashSet<JJBug>(selectedBugs));
        }
        this.selectedBugs = selectedBugs;
    }
    
    public List<JJBug> JJBugBean.completeBugUp(String query) {
        List<JJBug> suggestions = new ArrayList<JJBug>();
        for (JJBug jJBug : jJBugService.findAllJJBugs()) {
            String jJBugStr = String.valueOf(jJBug.getName() +  " "  + jJBug.getDescription() +  " "  + jJBug.getCreationDate() +  " "  + jJBug.getUpdatedDate());
            if (jJBugStr.toLowerCase().startsWith(query.toLowerCase())) {
                suggestions.add(jJBug);
            }
        }
        return suggestions;
    }
    
    public List<JJTask> JJBugBean.getSelectedTasks() {
        return selectedTasks;
    }
    
    public void JJBugBean.setSelectedTasks(List<JJTask> selectedTasks) {
        if (selectedTasks != null) {
            JJBug_.setTasks(new HashSet<JJTask>(selectedTasks));
        }
        this.selectedTasks = selectedTasks;
    }
    
    public List<JJContact> JJBugBean.getSelectedContacts() {
        return selectedContacts;
    }
    
    public void JJBugBean.setSelectedContacts(List<JJContact> selectedContacts) {
        if (selectedContacts != null) {
            JJBug_.setContacts(new HashSet<JJContact>(selectedContacts));
        }
        this.selectedContacts = selectedContacts;
    }
    
    public List<JJContact> JJBugBean.completeAssignedTos(String query) {
        List<JJContact> suggestions = new ArrayList<JJContact>();
        for (JJContact jJContact : jJContactService.findAllJJContacts()) {
            String jJContactStr = String.valueOf(jJContact.getName() +  " "  + jJContact.getDescription() +  " "  + jJContact.getCreationDate() +  " "  + jJContact.getUpdatedDate());
            if (jJContactStr.toLowerCase().startsWith(query.toLowerCase())) {
                suggestions.add(jJContact);
            }
        }
        return suggestions;
    }
    
    public String JJBugBean.onEdit() {
        if (JJBug_ != null && JJBug_.getBugs() != null) {
            selectedBugs = new ArrayList<JJBug>(JJBug_.getBugs());
        }
        if (JJBug_ != null && JJBug_.getTasks() != null) {
            selectedTasks = new ArrayList<JJTask>(JJBug_.getTasks());
        }
        if (JJBug_ != null && JJBug_.getContacts() != null) {
            selectedContacts = new ArrayList<JJContact>(JJBug_.getContacts());
        }
        return null;
    }
    
    public boolean JJBugBean.isCreateDialogVisible() {
        return createDialogVisible;
    }
    
    public void JJBugBean.setCreateDialogVisible(boolean createDialogVisible) {
        this.createDialogVisible = createDialogVisible;
    }
    
    public String JJBugBean.displayList() {
        createDialogVisible = false;
        findAllJJBugs();
        return "JJBug_";
    }
    
    public String JJBugBean.displayCreateDialog() {
        JJBug_ = new JJBug();
        createDialogVisible = true;
        return "JJBug_";
    }
    
    public String JJBugBean.persist() {
        String message = "";
        if (JJBug_.getId() != null) {
            jJBugService.updateJJBug(JJBug_);
            message = "message_successfully_updated";
        } else {
            jJBugService.saveJJBug(JJBug_);
            message = "message_successfully_created";
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("createDialogWidget.hide()");
        context.execute("editDialogWidget.hide()");
        
        FacesMessage facesMessage = MessageFactory.getMessage(message, "JJBug");
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllJJBugs();
    }
    
    public String JJBugBean.delete() {
        jJBugService.deleteJJBug(JJBug_);
        FacesMessage facesMessage = MessageFactory.getMessage("message_successfully_deleted", "JJBug");
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        reset();
        return findAllJJBugs();
    }
    
    public void JJBugBean.handleDialogClose(CloseEvent event) {
        reset();
    }
    
}

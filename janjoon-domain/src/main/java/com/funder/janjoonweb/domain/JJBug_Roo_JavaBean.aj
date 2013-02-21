// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.domain;

import com.funder.janjoonweb.domain.JJBug;
import com.funder.janjoonweb.domain.JJCategory;
import com.funder.janjoonweb.domain.JJContact;
import com.funder.janjoonweb.domain.JJCriticity;
import com.funder.janjoonweb.domain.JJImportance;
import com.funder.janjoonweb.domain.JJMessage;
import com.funder.janjoonweb.domain.JJProject;
import com.funder.janjoonweb.domain.JJRequirement;
import com.funder.janjoonweb.domain.JJStatus;
import com.funder.janjoonweb.domain.JJTask;
import com.funder.janjoonweb.domain.JJVersion;
import com.funder.janjoonweb.domain.reference.JJRelationship;
import java.util.Date;
import java.util.Set;

privileged aspect JJBug_Roo_JavaBean {
    
    public JJProject JJBug.getProject() {
        return this.project;
    }
    
    public void JJBug.setProject(JJProject project) {
        this.project = project;
    }
    
    public Date JJBug.getStartDate() {
        return this.startDate;
    }
    
    public void JJBug.setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public Date JJBug.getEndDate() {
        return this.endDate;
    }
    
    public void JJBug.setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    public Integer JJBug.getWorkload() {
        return this.workload;
    }
    
    public void JJBug.setWorkload(Integer workload) {
        this.workload = workload;
    }
    
    public JJVersion JJBug.getJjversion() {
        return this.jjversion;
    }
    
    public void JJBug.setJjversion(JJVersion jjversion) {
        this.jjversion = jjversion;
    }
    
    public JJCategory JJBug.getCategory() {
        return this.category;
    }
    
    public void JJBug.setCategory(JJCategory category) {
        this.category = category;
    }
    
    public JJCriticity JJBug.getCriticity() {
        return this.criticity;
    }
    
    public void JJBug.setCriticity(JJCriticity criticity) {
        this.criticity = criticity;
    }
    
    public JJImportance JJBug.getImportance() {
        return this.importance;
    }
    
    public void JJBug.setImportance(JJImportance importance) {
        this.importance = importance;
    }
    
    public JJStatus JJBug.getStatus() {
        return this.status;
    }
    
    public void JJBug.setStatus(JJStatus status) {
        this.status = status;
    }
    
    public JJRelationship JJBug.getRelation() {
        return this.relation;
    }
    
    public void JJBug.setRelation(JJRelationship relation) {
        this.relation = relation;
    }
    
    public JJBug JJBug.getBugUp() {
        return this.bugUp;
    }
    
    public void JJBug.setBugUp(JJBug bugUp) {
        this.bugUp = bugUp;
    }
    
    public JJRequirement JJBug.getRequirement() {
        return this.requirement;
    }
    
    public void JJBug.setRequirement(JJRequirement requirement) {
        this.requirement = requirement;
    }
    
    public Set<JJBug> JJBug.getBugs() {
        return this.bugs;
    }
    
    public void JJBug.setBugs(Set<JJBug> bugs) {
        this.bugs = bugs;
    }
    
    public Set<JJTask> JJBug.getTasks() {
        return this.tasks;
    }
    
    public void JJBug.setTasks(Set<JJTask> tasks) {
        this.tasks = tasks;
    }
    
    public Set<JJContact> JJBug.getAssignedTos() {
        return this.assignedTos;
    }
    
    public void JJBug.setAssignedTos(Set<JJContact> assignedTos) {
        this.assignedTos = assignedTos;
    }
    
    public Set<JJMessage> JJBug.getMessages() {
        return this.messages;
    }
    
    public void JJBug.setMessages(Set<JJMessage> messages) {
        this.messages = messages;
    }
    
}

// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJChapter;
import com.starit.janjoonweb.domain.JJMessage;
import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJRequirement;
import java.util.Set;

privileged aspect JJChapter_Roo_JavaBean {
    
    public Integer JJChapter.getOrdering() {
        return this.ordering;
    }
    
    public void JJChapter.setOrdering(Integer ordering) {
        this.ordering = ordering;
    }
    
    public JJProject JJChapter.getProject() {
        return this.project;
    }
    
    public void JJChapter.setProject(JJProject project) {
        this.project = project;
    }
    
    public JJCategory JJChapter.getCategory() {
        return this.category;
    }
    
    public void JJChapter.setCategory(JJCategory category) {
        this.category = category;
    }
    
    public JJChapter JJChapter.getParent() {
        return this.parent;
    }
    
    public void JJChapter.setParent(JJChapter parent) {
        this.parent = parent;
    }
    
    public Set<JJChapter> JJChapter.getChapters() {
        return this.chapters;
    }
    
    public void JJChapter.setChapters(Set<JJChapter> chapters) {
        this.chapters = chapters;
    }
    
    public Set<JJRequirement> JJChapter.getRequirements() {
        return this.requirements;
    }
    
    public void JJChapter.setRequirements(Set<JJRequirement> requirements) {
        this.requirements = requirements;
    }
    
    public Set<JJMessage> JJChapter.getMessages() {
        return this.messages;
    }
    
    public void JJChapter.setMessages(Set<JJMessage> messages) {
        this.messages = messages;
    }
    
}

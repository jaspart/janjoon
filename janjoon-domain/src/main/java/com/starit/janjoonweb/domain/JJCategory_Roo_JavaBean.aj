// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJCategory;
import com.starit.janjoonweb.domain.JJChapter;
import com.starit.janjoonweb.domain.JJCompany;
import com.starit.janjoonweb.domain.JJContact;
import java.util.Date;
import java.util.Set;

privileged aspect JJCategory_Roo_JavaBean {
    
    public String JJCategory.getName() {
        return this.name;
    }
    
    public void JJCategory.setName(String name) {
        this.name = name;
    }
    
    public String JJCategory.getDescription() {
        return this.description;
    }
    
    public void JJCategory.setDescription(String description) {
        this.description = description;
    }
    
    public Date JJCategory.getCreationDate() {
        return this.creationDate;
    }
    
    public void JJCategory.setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    
    public JJContact JJCategory.getCreatedBy() {
        return this.createdBy;
    }
    
    public void JJCategory.setCreatedBy(JJContact createdBy) {
        this.createdBy = createdBy;
    }
    
    public Date JJCategory.getUpdatedDate() {
        return this.updatedDate;
    }
    
    public void JJCategory.setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
    
    public JJContact JJCategory.getUpdatedBy() {
        return this.updatedBy;
    }
    
    public void JJCategory.setUpdatedBy(JJContact updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    public JJCompany JJCategory.getCompany() {
        return this.company;
    }
    
    public void JJCategory.setCompany(JJCompany company) {
        this.company = company;
    }
    
    public Boolean JJCategory.getEnabled() {
        return this.enabled;
    }
    
    public void JJCategory.setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    
    public Integer JJCategory.getStage() {
        return this.stage;
    }
    
    public void JJCategory.setStage(Integer stage) {
        this.stage = stage;
    }
    
    public Set<JJContact> JJCategory.getContacts() {
        return this.contacts;
    }
    
    public void JJCategory.setContacts(Set<JJContact> contacts) {
        this.contacts = contacts;
    }
    
    public Set<JJChapter> JJCategory.getChapters() {
        return this.chapters;
    }
    
    public void JJCategory.setChapters(Set<JJChapter> chapters) {
        this.chapters = chapters;
    }
    
}

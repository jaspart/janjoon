// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJBuild;
import com.starit.janjoonweb.domain.JJContact;
import com.starit.janjoonweb.domain.JJTestcase;
import com.starit.janjoonweb.domain.JJTestcaseexecution;
import com.starit.janjoonweb.domain.JJTeststepexecution;
import java.util.Date;
import java.util.Set;

privileged aspect JJTestcaseexecution_Roo_JavaBean {
    
    public String JJTestcaseexecution.getName() {
        return this.name;
    }
    
    public void JJTestcaseexecution.setName(String name) {
        this.name = name;
    }
    
    public String JJTestcaseexecution.getDescription() {
        return this.description;
    }
    
    public void JJTestcaseexecution.setDescription(String description) {
        this.description = description;
    }
    
    public Date JJTestcaseexecution.getCreationDate() {
        return this.creationDate;
    }
    
    public void JJTestcaseexecution.setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    
    public JJContact JJTestcaseexecution.getCreatedBy() {
        return this.createdBy;
    }
    
    public void JJTestcaseexecution.setCreatedBy(JJContact createdBy) {
        this.createdBy = createdBy;
    }
    
    public Date JJTestcaseexecution.getUpdatedDate() {
        return this.updatedDate;
    }
    
    public void JJTestcaseexecution.setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
    
    public JJContact JJTestcaseexecution.getUpdatedBy() {
        return this.updatedBy;
    }
    
    public void JJTestcaseexecution.setUpdatedBy(JJContact updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    public Boolean JJTestcaseexecution.getEnabled() {
        return this.enabled;
    }
    
    public void JJTestcaseexecution.setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    
    public JJTestcase JJTestcaseexecution.getTestcase() {
        return this.testcase;
    }
    
    public void JJTestcaseexecution.setTestcase(JJTestcase testcase) {
        this.testcase = testcase;
    }
    
    public JJBuild JJTestcaseexecution.getBuild() {
        return this.build;
    }
    
    public void JJTestcaseexecution.setBuild(JJBuild build) {
        this.build = build;
    }
    
    public Boolean JJTestcaseexecution.getPassed() {
        return this.passed;
    }
    
    public void JJTestcaseexecution.setPassed(Boolean passed) {
        this.passed = passed;
    }
    
    public Set<JJTeststepexecution> JJTestcaseexecution.getTeststepexecutions() {
        return this.teststepexecutions;
    }
    
    public void JJTestcaseexecution.setTeststepexecutions(Set<JJTeststepexecution> teststepexecutions) {
        this.teststepexecutions = teststepexecutions;
    }
    
}

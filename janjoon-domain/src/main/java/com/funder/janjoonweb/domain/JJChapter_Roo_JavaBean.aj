// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.domain;

import com.funder.janjoonweb.domain.JJCategory;
import com.funder.janjoonweb.domain.JJChapter;
import com.funder.janjoonweb.domain.JJProduct;
import com.funder.janjoonweb.domain.JJProject;
import com.funder.janjoonweb.domain.JJRequirement;
import com.funder.janjoonweb.domain.JJTestcase;
import java.util.Set;

privileged aspect JJChapter_Roo_JavaBean {
    
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
    
    public JJProduct JJChapter.getProduct() {
        return this.product;
    }
    
    public void JJChapter.setProduct(JJProduct product) {
        this.product = product;
    }
    
    public Set<JJRequirement> JJChapter.getRequirements() {
        return this.requirements;
    }
    
    public void JJChapter.setRequirements(Set<JJRequirement> requirements) {
        this.requirements = requirements;
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
    
    public Integer JJChapter.getOrdering() {
        return this.ordering;
    }
    
    public void JJChapter.setOrdering(Integer ordering) {
        this.ordering = ordering;
    }
    
    public Set<JJTestcase> JJChapter.getTestcases() {
        return this.testcases;
    }
    
    public void JJChapter.setTestcases(Set<JJTestcase> testcases) {
        this.testcases = testcases;
    }
    
}

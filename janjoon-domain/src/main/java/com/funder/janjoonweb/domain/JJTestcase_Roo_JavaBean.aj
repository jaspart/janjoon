// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.domain;

import com.funder.janjoonweb.domain.JJCategory;
import com.funder.janjoonweb.domain.JJChapter;
import com.funder.janjoonweb.domain.JJHardware;
import com.funder.janjoonweb.domain.JJMessage;
import com.funder.janjoonweb.domain.JJProduct;
import com.funder.janjoonweb.domain.JJRequirement;
import com.funder.janjoonweb.domain.JJSoftware;
import com.funder.janjoonweb.domain.JJSprint;
import com.funder.janjoonweb.domain.JJTask;
import com.funder.janjoonweb.domain.JJTestcase;
import com.funder.janjoonweb.domain.JJTeststep;
import java.util.Set;

privileged aspect JJTestcase_Roo_JavaBean {
    
    public Integer JJTestcase.getOrdering() {
        return this.ordering;
    }
    
    public void JJTestcase.setOrdering(Integer ordering) {
        this.ordering = ordering;
    }
    
    public String JJTestcase.getActioncase() {
        return this.actioncase;
    }
    
    public void JJTestcase.setActioncase(String actioncase) {
        this.actioncase = actioncase;
    }
    
    public String JJTestcase.getResultat() {
        return this.resultat;
    }
    
    public void JJTestcase.setResultat(String resultat) {
        this.resultat = resultat;
    }
    
    public JJProduct JJTestcase.getProduct() {
        return this.product;
    }
    
    public void JJTestcase.setProduct(JJProduct product) {
        this.product = product;
    }
    
    public JJCategory JJTestcase.getCategory() {
        return this.category;
    }
    
    public void JJTestcase.setCategory(JJCategory category) {
        this.category = category;
    }
    
    public JJChapter JJTestcase.getChapter() {
        return this.chapter;
    }
    
    public void JJTestcase.setChapter(JJChapter chapter) {
        this.chapter = chapter;
    }
    
    public JJRequirement JJTestcase.getRequirement() {
        return this.requirement;
    }
    
    public void JJTestcase.setRequirement(JJRequirement requirement) {
        this.requirement = requirement;
    }
    
    public JJSprint JJTestcase.getSprint() {
        return this.sprint;
    }
    
    public void JJTestcase.setSprint(JJSprint sprint) {
        this.sprint = sprint;
    }
    
    public Set<JJSoftware> JJTestcase.getSoftwares() {
        return this.softwares;
    }
    
    public void JJTestcase.setSoftwares(Set<JJSoftware> softwares) {
        this.softwares = softwares;
    }
    
    public Set<JJHardware> JJTestcase.getHardwares() {
        return this.hardwares;
    }
    
    public void JJTestcase.setHardwares(Set<JJHardware> hardwares) {
        this.hardwares = hardwares;
    }
    
    public Integer JJTestcase.getWorkload() {
        return this.workload;
    }
    
    public void JJTestcase.setWorkload(Integer workload) {
        this.workload = workload;
    }
    
    public Integer JJTestcase.getPricepoint() {
        return this.pricepoint;
    }
    
    public void JJTestcase.setPricepoint(Integer pricepoint) {
        this.pricepoint = pricepoint;
    }
    
    public Set<JJTeststep> JJTestcase.getTeststeps() {
        return this.teststeps;
    }
    
    public void JJTestcase.setTeststeps(Set<JJTeststep> teststeps) {
        this.teststeps = teststeps;
    }
    
    public Set<JJTask> JJTestcase.getTasks() {
        return this.tasks;
    }
    
    public void JJTestcase.setTasks(Set<JJTask> tasks) {
        this.tasks = tasks;
    }
    
    public Set<JJMessage> JJTestcase.getMessages() {
        return this.messages;
    }
    
    public void JJTestcase.setMessages(Set<JJMessage> messages) {
        this.messages = messages;
    }
    
}

// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJHardware;
import com.starit.janjoonweb.domain.JJMessage;
import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJSoftware;
import com.starit.janjoonweb.domain.JJSprint;
import com.starit.janjoonweb.domain.JJTask;
import com.starit.janjoonweb.domain.JJTestcase;
import com.starit.janjoonweb.domain.JJTeststep;
import java.util.Set;

privileged aspect JJTestcase_Roo_JavaBean {
    
    public Integer JJTestcase.getOrdering() {
        return this.ordering;
    }
    
    public void JJTestcase.setOrdering(Integer ordering) {
        this.ordering = ordering;
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
    
    public Boolean JJTestcase.getAutomatic() {
        return this.automatic;
    }
    
    public void JJTestcase.setAutomatic(Boolean automatic) {
        this.automatic = automatic;
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

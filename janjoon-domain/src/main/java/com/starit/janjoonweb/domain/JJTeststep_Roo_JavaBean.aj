// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJMessage;
import com.starit.janjoonweb.domain.JJTestcase;
import com.starit.janjoonweb.domain.JJTeststep;
import java.util.Set;

privileged aspect JJTeststep_Roo_JavaBean {
    
    public Integer JJTeststep.getOrdering() {
        return this.ordering;
    }
    
    public void JJTeststep.setOrdering(Integer ordering) {
        this.ordering = ordering;
    }
    
    public String JJTeststep.getActionstep() {
        return this.actionstep;
    }
    
    public void JJTeststep.setActionstep(String actionstep) {
        this.actionstep = actionstep;
    }
    
    public String JJTeststep.getResultstep() {
        return this.resultstep;
    }
    
    public void JJTeststep.setResultstep(String resultstep) {
        this.resultstep = resultstep;
    }
    
    public JJTestcase JJTeststep.getTestcase() {
        return this.testcase;
    }
    
    public void JJTeststep.setTestcase(JJTestcase testcase) {
        this.testcase = testcase;
    }
    
    public Set<JJBug> JJTeststep.getBugs() {
        return this.bugs;
    }
    
    public void JJTeststep.setBugs(Set<JJBug> bugs) {
        this.bugs = bugs;
    }
    
    public Set<JJMessage> JJTeststep.getMessages() {
        return this.messages;
    }
    
    public void JJTeststep.setMessages(Set<JJMessage> messages) {
        this.messages = messages;
    }
    
}

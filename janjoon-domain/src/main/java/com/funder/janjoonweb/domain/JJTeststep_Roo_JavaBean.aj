// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.domain;

import com.funder.janjoonweb.domain.JJMessage;
import com.funder.janjoonweb.domain.JJTestcase;
import com.funder.janjoonweb.domain.JJTeststep;
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
    
    public String JJTeststep.getResultat() {
        return this.resultat;
    }
    
    public void JJTeststep.setResultat(String resultat) {
        this.resultat = resultat;
    }
    
    public Boolean JJTeststep.getPassed() {
        return this.passed;
    }
    
    public void JJTeststep.setPassed(Boolean passed) {
        this.passed = passed;
    }
    
    public JJTestcase JJTeststep.getTestcase() {
        return this.testcase;
    }
    
    public void JJTeststep.setTestcase(JJTestcase testcase) {
        this.testcase = testcase;
    }
    
    public Set<JJMessage> JJTeststep.getMessages() {
        return this.messages;
    }
    
    public void JJTeststep.setMessages(Set<JJMessage> messages) {
        this.messages = messages;
    }
    
}

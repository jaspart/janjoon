// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.domain;

import com.funder.janjoonweb.domain.JJBuild;
import com.funder.janjoonweb.domain.JJProject;
import com.funder.janjoonweb.domain.JJTestcase;
import com.funder.janjoonweb.domain.JJTestplan;
import com.funder.janjoonweb.domain.JJVersion;
import java.util.Set;

privileged aspect JJTestplan_Roo_JavaBean {
    
    public JJProject JJTestplan.getProject() {
        return this.project;
    }
    
    public void JJTestplan.setProject(JJProject project) {
        this.project = project;
    }
    
    public JJVersion JJTestplan.getJjversion() {
        return this.jjversion;
    }
    
    public void JJTestplan.setJjversion(JJVersion jjversion) {
        this.jjversion = jjversion;
    }
    
    public JJBuild JJTestplan.getBuild() {
        return this.build;
    }
    
    public void JJTestplan.setBuild(JJBuild build) {
        this.build = build;
    }
    
    public Set<JJTestcase> JJTestplan.getTestcases() {
        return this.testcases;
    }
    
    public void JJTestplan.setTestcases(Set<JJTestcase> testcases) {
        this.testcases = testcases;
    }
    
}

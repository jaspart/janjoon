// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.domain;

import com.funder.janjoonweb.domain.JJSprint;
import com.funder.janjoonweb.domain.JJSprintService;
import java.util.List;

privileged aspect JJSprintService_Roo_Service {
    
    public abstract long JJSprintService.countAllJJSprints();    
    public abstract void JJSprintService.deleteJJSprint(JJSprint JJSprint_);    
    public abstract JJSprint JJSprintService.findJJSprint(Long id);    
    public abstract List<JJSprint> JJSprintService.findAllJJSprints();    
    public abstract List<JJSprint> JJSprintService.findJJSprintEntries(int firstResult, int maxResults);    
    public abstract void JJSprintService.saveJJSprint(JJSprint JJSprint_);    
    public abstract JJSprint JJSprintService.updateJJSprint(JJSprint JJSprint_);    
}
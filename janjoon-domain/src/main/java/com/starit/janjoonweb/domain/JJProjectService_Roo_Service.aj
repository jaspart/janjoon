// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJProject;
import com.starit.janjoonweb.domain.JJProjectService;
import java.util.List;

privileged aspect JJProjectService_Roo_Service {
    
    public abstract long JJProjectService.countAllJJProjects();    
    public abstract void JJProjectService.deleteJJProject(JJProject JJProject_);    
    public abstract JJProject JJProjectService.findJJProject(Long id);    
    public abstract List<JJProject> JJProjectService.findAllJJProjects();    
    public abstract List<JJProject> JJProjectService.findJJProjectEntries(int firstResult, int maxResults);    
    public abstract void JJProjectService.saveJJProject(JJProject JJProject_);    
    public abstract JJProject JJProjectService.updateJJProject(JJProject JJProject_);    
}
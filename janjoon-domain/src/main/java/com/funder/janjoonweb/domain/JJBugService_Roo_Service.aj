// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.domain;

import com.funder.janjoonweb.domain.JJBug;
import com.funder.janjoonweb.domain.JJBugService;
import java.util.List;

privileged aspect JJBugService_Roo_Service {
    
    public abstract long JJBugService.countAllJJBugs();    
    public abstract void JJBugService.deleteJJBug(JJBug JJBug_);    
    public abstract JJBug JJBugService.findJJBug(Long id);    
    public abstract List<JJBug> JJBugService.findAllJJBugs();    
    public abstract List<JJBug> JJBugService.findJJBugEntries(int firstResult, int maxResults);    
    public abstract void JJBugService.saveJJBug(JJBug JJBug_);    
    public abstract JJBug JJBugService.updateJJBug(JJBug JJBug_);    
}

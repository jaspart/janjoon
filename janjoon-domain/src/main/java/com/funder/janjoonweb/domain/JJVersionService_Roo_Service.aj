// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.domain;

import com.funder.janjoonweb.domain.JJVersion;
import com.funder.janjoonweb.domain.JJVersionService;
import java.util.List;

privileged aspect JJVersionService_Roo_Service {
    
    public abstract long JJVersionService.countAllJJVersions();    
    public abstract void JJVersionService.deleteJJVersion(JJVersion JJVersion_);    
    public abstract JJVersion JJVersionService.findJJVersion(Long id);    
    public abstract List<JJVersion> JJVersionService.findAllJJVersions();    
    public abstract List<JJVersion> JJVersionService.findJJVersionEntries(int firstResult, int maxResults);    
    public abstract void JJVersionService.saveJJVersion(JJVersion JJVersion_);    
    public abstract JJVersion JJVersionService.updateJJVersion(JJVersion JJVersion_);    
}

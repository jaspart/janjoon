// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJRequirementService;
import java.util.List;

privileged aspect JJRequirementService_Roo_Service {
    
    public abstract long JJRequirementService.countAllJJRequirements();    
    public abstract void JJRequirementService.deleteJJRequirement(JJRequirement JJRequirement_);    
    public abstract JJRequirement JJRequirementService.findJJRequirement(Long id);    
    public abstract List<JJRequirement> JJRequirementService.findAllJJRequirements();    
    public abstract List<JJRequirement> JJRequirementService.findJJRequirementEntries(int firstResult, int maxResults);    
    public abstract void JJRequirementService.saveJJRequirement(JJRequirement JJRequirement_);    
    public abstract JJRequirement JJRequirementService.updateJJRequirement(JJRequirement JJRequirement_);    
}

// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJTestcase;
import com.starit.janjoonweb.domain.JJTestcaseService;
import java.util.List;

privileged aspect JJTestcaseService_Roo_Service {
    
    public abstract long JJTestcaseService.countAllJJTestcases();    
    public abstract void JJTestcaseService.deleteJJTestcase(JJTestcase JJTestcase_);    
    public abstract JJTestcase JJTestcaseService.findJJTestcase(Long id);    
    public abstract List<JJTestcase> JJTestcaseService.findAllJJTestcases();    
    public abstract List<JJTestcase> JJTestcaseService.findJJTestcaseEntries(int firstResult, int maxResults);    
    public abstract void JJTestcaseService.saveJJTestcase(JJTestcase JJTestcase_);    
    public abstract JJTestcase JJTestcaseService.updateJJTestcase(JJTestcase JJTestcase_);    
}

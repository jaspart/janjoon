// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJWorflow;
import com.starit.janjoonweb.domain.JJWorflowServiceImpl;
import com.starit.janjoonweb.domain.JJWorkflowRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

privileged aspect JJWorflowServiceImpl_Roo_Service {
    
    declare @type: JJWorflowServiceImpl: @Service;
    
    declare @type: JJWorflowServiceImpl: @Transactional;
    
    @Autowired
    JJWorkflowRepository JJWorflowServiceImpl.jJWorkflowRepository;
    
    public long JJWorflowServiceImpl.countAllJJWorflows() {
        return jJWorkflowRepository.count();
    }
    
    public void JJWorflowServiceImpl.deleteJJWorflow(JJWorflow JJWorflow_) {
        jJWorkflowRepository.delete(JJWorflow_);
    }
    
    public JJWorflow JJWorflowServiceImpl.findJJWorflow(Long id) {
        return jJWorkflowRepository.findOne(id);
    }
    
    public List<JJWorflow> JJWorflowServiceImpl.findAllJJWorflows() {
        return jJWorkflowRepository.findAll();
    }
    
    public List<JJWorflow> JJWorflowServiceImpl.findJJWorflowEntries(int firstResult, int maxResults) {
        return jJWorkflowRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }
    
    public void JJWorflowServiceImpl.saveJJWorflow(JJWorflow JJWorflow_) {
        jJWorkflowRepository.save(JJWorflow_);
    }
    
    public JJWorflow JJWorflowServiceImpl.updateJJWorflow(JJWorflow JJWorflow_) {
        return jJWorkflowRepository.save(JJWorflow_);
    }
    
}

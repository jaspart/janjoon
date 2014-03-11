// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJTeststepexecution;
import com.starit.janjoonweb.domain.JJTeststepexecutionRepository;
import com.starit.janjoonweb.domain.JJTeststepexecutionServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

privileged aspect JJTeststepexecutionServiceImpl_Roo_Service {
    
    declare @type: JJTeststepexecutionServiceImpl: @Service;
    
    declare @type: JJTeststepexecutionServiceImpl: @Transactional;
    
    @Autowired
    JJTeststepexecutionRepository JJTeststepexecutionServiceImpl.jJTeststepexecutionRepository;
    
    public long JJTeststepexecutionServiceImpl.countAllJJTeststepexecutions() {
        return jJTeststepexecutionRepository.count();
    }
    
    public void JJTeststepexecutionServiceImpl.deleteJJTeststepexecution(JJTeststepexecution JJTeststepexecution_) {
        jJTeststepexecutionRepository.delete(JJTeststepexecution_);
    }
    
    public JJTeststepexecution JJTeststepexecutionServiceImpl.findJJTeststepexecution(Long id) {
        return jJTeststepexecutionRepository.findOne(id);
    }
    
    public List<JJTeststepexecution> JJTeststepexecutionServiceImpl.findAllJJTeststepexecutions() {
        return jJTeststepexecutionRepository.findAll();
    }
    
    public List<JJTeststepexecution> JJTeststepexecutionServiceImpl.findJJTeststepexecutionEntries(int firstResult, int maxResults) {
        return jJTeststepexecutionRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }
    
    public void JJTeststepexecutionServiceImpl.saveJJTeststepexecution(JJTeststepexecution JJTeststepexecution_) {
        jJTeststepexecutionRepository.save(JJTeststepexecution_);
    }
    
    public JJTeststepexecution JJTeststepexecutionServiceImpl.updateJJTeststepexecution(JJTeststepexecution JJTeststepexecution_) {
        return jJTeststepexecutionRepository.save(JJTeststepexecution_);
    }
    
}
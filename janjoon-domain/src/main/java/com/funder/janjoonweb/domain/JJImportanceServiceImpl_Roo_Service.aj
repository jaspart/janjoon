// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.domain;

import com.funder.janjoonweb.domain.JJImportance;
import com.funder.janjoonweb.domain.JJImportanceRepository;
import com.funder.janjoonweb.domain.JJImportanceServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

privileged aspect JJImportanceServiceImpl_Roo_Service {
    
    declare @type: JJImportanceServiceImpl: @Service;
    
    declare @type: JJImportanceServiceImpl: @Transactional;
    
    @Autowired
    JJImportanceRepository JJImportanceServiceImpl.jJImportanceRepository;
    
    public long JJImportanceServiceImpl.countAllJJImportances() {
        return jJImportanceRepository.count();
    }
    
    public void JJImportanceServiceImpl.deleteJJImportance(JJImportance JJImportance_) {
        jJImportanceRepository.delete(JJImportance_);
    }
    
    public JJImportance JJImportanceServiceImpl.findJJImportance(Long id) {
        return jJImportanceRepository.findOne(id);
    }
    
    public List<JJImportance> JJImportanceServiceImpl.findAllJJImportances() {
        return jJImportanceRepository.findAll();
    }
    
    public List<JJImportance> JJImportanceServiceImpl.findJJImportanceEntries(int firstResult, int maxResults) {
        return jJImportanceRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }
    
    public void JJImportanceServiceImpl.saveJJImportance(JJImportance JJImportance_) {
        jJImportanceRepository.save(JJImportance_);
    }
    
    public JJImportance JJImportanceServiceImpl.updateJJImportance(JJImportance JJImportance_) {
        return jJImportanceRepository.save(JJImportance_);
    }
    
}

// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.domain;

import com.funder.janjoonweb.domain.JJTeststep;
import com.funder.janjoonweb.domain.JJTeststepRepository;
import com.funder.janjoonweb.domain.JJTeststepServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

privileged aspect JJTeststepServiceImpl_Roo_Service {
    
    declare @type: JJTeststepServiceImpl: @Service;
    
    declare @type: JJTeststepServiceImpl: @Transactional;
    
    @Autowired
    JJTeststepRepository JJTeststepServiceImpl.jJTeststepRepository;
    
    public long JJTeststepServiceImpl.countAllJJTeststeps() {
        return jJTeststepRepository.count();
    }
    
    public void JJTeststepServiceImpl.deleteJJTeststep(JJTeststep JJTeststep_) {
        jJTeststepRepository.delete(JJTeststep_);
    }
    
    public JJTeststep JJTeststepServiceImpl.findJJTeststep(Long id) {
        return jJTeststepRepository.findOne(id);
    }
    
    public List<JJTeststep> JJTeststepServiceImpl.findAllJJTeststeps() {
        return jJTeststepRepository.findAll();
    }
    
    public List<JJTeststep> JJTeststepServiceImpl.findJJTeststepEntries(int firstResult, int maxResults) {
        return jJTeststepRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }
    
    public void JJTeststepServiceImpl.saveJJTeststep(JJTeststep JJTeststep_) {
        jJTeststepRepository.save(JJTeststep_);
    }
    
    public JJTeststep JJTeststepServiceImpl.updateJJTeststep(JJTeststep JJTeststep_) {
        return jJTeststepRepository.save(JJTeststep_);
    }
    
}

// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJSprint;
import com.starit.janjoonweb.domain.JJSprintRepository;
import com.starit.janjoonweb.domain.JJSprintServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

privileged aspect JJSprintServiceImpl_Roo_Service {
    
    declare @type: JJSprintServiceImpl: @Service;
    
    declare @type: JJSprintServiceImpl: @Transactional;
    
    @Autowired
    JJSprintRepository JJSprintServiceImpl.jJSprintRepository;
    
    public long JJSprintServiceImpl.countAllJJSprints() {
        return jJSprintRepository.count();
    }
    
    public void JJSprintServiceImpl.deleteJJSprint(JJSprint JJSprint_) {
        jJSprintRepository.delete(JJSprint_);
    }
    
    public JJSprint JJSprintServiceImpl.findJJSprint(Long id) {
        return jJSprintRepository.findOne(id);
    }
    
    public List<JJSprint> JJSprintServiceImpl.findAllJJSprints() {
        return jJSprintRepository.findAll();
    }
    
    public List<JJSprint> JJSprintServiceImpl.findJJSprintEntries(int firstResult, int maxResults) {
        return jJSprintRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }
    
}

// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.domain;

import com.funder.janjoonweb.domain.JJProfile;
import com.funder.janjoonweb.domain.JJProfileRepository;
import com.funder.janjoonweb.domain.JJProfileServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

privileged aspect JJProfileServiceImpl_Roo_Service {
    
    declare @type: JJProfileServiceImpl: @Service;
    
    declare @type: JJProfileServiceImpl: @Transactional;
    
    @Autowired
    JJProfileRepository JJProfileServiceImpl.jJProfileRepository;
    
    public long JJProfileServiceImpl.countAllJJProfiles() {
        return jJProfileRepository.count();
    }
    
    public void JJProfileServiceImpl.deleteJJProfile(JJProfile JJProfile_) {
        jJProfileRepository.delete(JJProfile_);
    }
    
    public JJProfile JJProfileServiceImpl.findJJProfile(Long id) {
        return jJProfileRepository.findOne(id);
    }
    
    public List<JJProfile> JJProfileServiceImpl.findAllJJProfiles() {
        return jJProfileRepository.findAll();
    }
    
    public List<JJProfile> JJProfileServiceImpl.findJJProfileEntries(int firstResult, int maxResults) {
        return jJProfileRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }
    
    public void JJProfileServiceImpl.saveJJProfile(JJProfile JJProfile_) {
        jJProfileRepository.save(JJProfile_);
    }
    
    public JJProfile JJProfileServiceImpl.updateJJProfile(JJProfile JJProfile_) {
        return jJProfileRepository.save(JJProfile_);
    }
    
}

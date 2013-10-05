// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.domain;

import com.funder.janjoonweb.domain.JJConfiguration;
import com.funder.janjoonweb.domain.JJConfigurationRepository;
import com.funder.janjoonweb.domain.JJConfigurationServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

privileged aspect JJConfigurationServiceImpl_Roo_Service {
    
    declare @type: JJConfigurationServiceImpl: @Service;
    
    declare @type: JJConfigurationServiceImpl: @Transactional;
    
    @Autowired
    JJConfigurationRepository JJConfigurationServiceImpl.jJConfigurationRepository;
    
    public long JJConfigurationServiceImpl.countAllJJConfigurations() {
        return jJConfigurationRepository.count();
    }
    
    public void JJConfigurationServiceImpl.deleteJJConfiguration(JJConfiguration JJConfiguration_) {
        jJConfigurationRepository.delete(JJConfiguration_);
    }
    
    public JJConfiguration JJConfigurationServiceImpl.findJJConfiguration(Long id) {
        return jJConfigurationRepository.findOne(id);
    }
    
    public List<JJConfiguration> JJConfigurationServiceImpl.findAllJJConfigurations() {
        return jJConfigurationRepository.findAll();
    }
    
    public List<JJConfiguration> JJConfigurationServiceImpl.findJJConfigurationEntries(int firstResult, int maxResults) {
        return jJConfigurationRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }
    
    public void JJConfigurationServiceImpl.saveJJConfiguration(JJConfiguration JJConfiguration_) {
        jJConfigurationRepository.save(JJConfiguration_);
    }
    
    public JJConfiguration JJConfigurationServiceImpl.updateJJConfiguration(JJConfiguration JJConfiguration_) {
        return jJConfigurationRepository.save(JJConfiguration_);
    }
    
}